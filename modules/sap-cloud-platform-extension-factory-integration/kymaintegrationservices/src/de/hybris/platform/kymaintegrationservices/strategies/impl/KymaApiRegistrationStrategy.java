/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.kymaintegrationservices.strategies.impl;

import de.hybris.platform.apiregistryservices.dao.EventConfigurationDao;
import de.hybris.platform.apiregistryservices.dto.RegisteredDestinationData;
import de.hybris.platform.apiregistryservices.exceptions.ApiRegistrationException;
import de.hybris.platform.apiregistryservices.exceptions.CredentialException;
import de.hybris.platform.apiregistryservices.exceptions.DestinationNotFoundException;
import de.hybris.platform.apiregistryservices.model.AbstractDestinationModel;
import de.hybris.platform.apiregistryservices.model.DestinationTargetModel;
import de.hybris.platform.apiregistryservices.model.ExposedDestinationModel;
import de.hybris.platform.apiregistryservices.services.DestinationService;
import de.hybris.platform.apiregistryservices.strategies.ApiRegistrationStrategy;
import de.hybris.platform.kymaintegrationservices.dto.ApiRegistrationResponseData;
import de.hybris.platform.kymaintegrationservices.dto.EventsSpecificationSourceData;
import de.hybris.platform.kymaintegrationservices.dto.ServiceRegistrationData;
import static de.hybris.platform.kymaintegrationservices.utils.KymaConfigurationUtils.getTargetName;
import de.hybris.platform.kymaintegrationservices.utils.RestTemplateWrapper;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.Config;

import java.io.IOException;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;

import static de.hybris.platform.kymaintegrationservices.utils.KymaApiExportHelper.API_REG_SERVICE_ID;
import static de.hybris.platform.kymaintegrationservices.utils.KymaHttpHelper.getDefaultHeaders;
import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;


/**
 * Kyma impl of @{@link ApiRegistrationStrategy}
 */
public class KymaApiRegistrationStrategy implements ApiRegistrationStrategy
{
	public static final String EVENTS_ED_ID = "kymaintegrationservices.events.exposedDestinationId";
	public static final String DESTINATION_NOT_FOUND = "kymaintegrationservices.destination_not_found.message.format";

	private static final Logger LOG = LoggerFactory.getLogger(KymaApiRegistrationStrategy.class);
	private static final String CREDENTIAL_ERROR_MESSAGE = "Cannot sign request to %s; reason: [{%s}]";
	private Converter<ExposedDestinationModel, ServiceRegistrationData> webserviceSpecificationConverter;
	private Converter<EventsSpecificationSourceData, ServiceRegistrationData> eventsSpecificationConverter;
	private ObjectMapper jacksonObjectMapper;
	private RestTemplateWrapper restTemplate;
	private ConfigurationService configurationService;
	private ModelService modelService;

	private EventConfigurationDao eventConfigurationDao;
	private DestinationService<AbstractDestinationModel> destinationService;

	@Override
	public void registerExposedDestination(final ExposedDestinationModel exposedDestination) throws ApiRegistrationException
	{
		final ServiceRegistrationData serviceRegistrationData;

		if (exposedDestination.getId().equals(Config.getParameter(EVENTS_ED_ID)))
		{ // special logic for event registration only
			final EventsSpecificationSourceData eventSpecificationSourceData = new EventsSpecificationSourceData();
			eventSpecificationSourceData.setExposedDestination(exposedDestination);
			eventSpecificationSourceData.setEvents(getEventConfigurationDao()
					.findActiveEventConfigsByDestinationTargetId(exposedDestination.getDestinationTarget().getId()));
			serviceRegistrationData = getEventsSpecificationConverter().convert(eventSpecificationSourceData);
		}
		else
		{
			try
			{
				serviceRegistrationData = getWebserviceSpecificationConverter().convert(exposedDestination);
			}
			catch (final ConversionException e)
			{
				throw new ApiRegistrationException(e.getMessage(), e);
			}
		}

		final HttpHeaders headers = getDefaultHeaders();
		final HttpEntity<String> request = new HttpEntity(serviceRegistrationData, headers);


		if (LOG.isInfoEnabled())
		{
			LOG.info("Sending Exposed Destination '{}' to the {}", serviceRegistrationData.getIdentifier(),
					getTargetName());
		}

		if (StringUtils.isBlank(exposedDestination.getTargetId()))
		{
			registerNewSpecAtKyma(serviceRegistrationData, exposedDestination, request);
		}
		else
		{
			updateExistingSpecAtKyma(serviceRegistrationData, exposedDestination, request);
		}

	}

	@Override
	public void unregisterExposedDestination(final ExposedDestinationModel destination) throws ApiRegistrationException
	{
		LOG.info("Sending Exposed Destination '{}' to the {}", destination.getId(), getTargetName());

		unregisterExposedDestinationByTargetId(destination.getTargetId(), destination.getDestinationTarget().getId());

		updateApiConfigurationUid(null, destination);
	}

	@Override
	public void unregisterExposedDestinationByTargetId(final String targetId, final String destinationTargetId) throws ApiRegistrationException
	{
		final AbstractDestinationModel apiDestination = getApiDestination(destinationTargetId);
		final String url = apiDestination.getUrl() + "/{targetId}";
		final HttpHeaders headers = getDefaultHeaders();
		final ResponseEntity<String> response;
		final HttpEntity request = new HttpEntity<>(headers);

		try
		{
			response = getRestTemplate().getRestTemplate(apiDestination.getCredential()).exchange(url, HttpMethod.DELETE, request, String.class, targetId);
			if (response.getStatusCode().series() == HttpStatus.Series.SUCCESSFUL)
			{
				LOG.info("Unregistration of Destination in {} with targetId [{}] successful", getTargetName(),
						targetId);
			}
			else
			{
				final String errorMessage = String
						.format("Cannot unregister Destination in %s with targetId: [{%s}]; response status: [{%s}]",
								getTargetName(), targetId,
								response.getStatusCode());
				throw new ApiRegistrationException(errorMessage);
			}
		}
		catch (final HttpClientErrorException e)
		{
			throw logAndCreateApiRegistrationException(e.getResponseBodyAsString(), e);
		}
		catch (final ResourceAccessException e)
		{
			final String errorMessage = String.format(
					"Cannot unregister Registered Exposed Destination in %s with targetId: [{%s}]; reason : [{%s}]",
					getTargetName(), targetId, e.getMessage());
			throw logAndCreateApiRegistrationException(errorMessage, e);
		}
		catch (final RestClientException e)
		{
			final String errorMessage = String.format("Cannot unregister Registered Exposed Destination in %s with targetId: [{%s}]",
					getTargetName(), targetId);
			throw logAndCreateApiRegistrationException(errorMessage, e);
		}
		catch (final CredentialException e)
		{
			final String errorMessage = String.format(CREDENTIAL_ERROR_MESSAGE, getTargetName(),
					e.getMessage());
			throw logAndCreateApiRegistrationException(errorMessage, e);
		}
	}

	protected ApiRegistrationException logAndCreateApiRegistrationException(final String errorMessage, final Exception e)
	{
		LOG.error(errorMessage, e);
		return new ApiRegistrationException(errorMessage, e);
	}

	@Override
	public List<RegisteredDestinationData> retrieveRegisteredExposedDestinations(final DestinationTargetModel destinationTarget) throws ApiRegistrationException
	{
		final AbstractDestinationModel apiDestination = getApiDestination(destinationTarget.getId());
		final String url = apiDestination.getUrl();
		final HttpHeaders headers = getDefaultHeaders();
		final ResponseEntity<String> response;
		try
		{
			response = getRestTemplate().getRestTemplate(apiDestination.getCredential()).getForEntity(url, String.class, headers);
			return getJacksonObjectMapper().readValue(response.getBody(),
					getJacksonObjectMapper().getTypeFactory().constructCollectionType(List.class, RegisteredDestinationData.class));
		}
		catch (final HttpClientErrorException e)
		{
			final String errorMessage = e.getResponseBodyAsString();
			throw logAndCreateApiRegistrationException(errorMessage, e);
		}
		catch (final ResourceAccessException e)
		{
			final String errorMessage = String.format(
					"Cannot retrieve all registered destinations from %s with URL: [{%s}]; reason : [{%s}]",
					getTargetName(), url, e.getMessage());
			throw logAndCreateApiRegistrationException(errorMessage, e);
		}
		catch (final RestClientException e)
		{
			final String errorMessage = String.format("Cannot retrieve all registered destinations from %s with URL: [{%s}]",
					getTargetName(), url);
			throw logAndCreateApiRegistrationException(errorMessage, e);
		}
		catch (final CredentialException e)
		{
			final String errorMessage = String.format(CREDENTIAL_ERROR_MESSAGE, getTargetName(),
					e.getMessage());
			throw logAndCreateApiRegistrationException(errorMessage, e);
		}
		catch (final IOException e)
		{
			final String errorMessage = String
					.format("Cannot parse response for \"get all registered destinations\" request: [{%s}]", e.getMessage());
			throw logAndCreateApiRegistrationException(errorMessage, e);
		}
	}

	protected void registerNewSpecAtKyma(final ServiceRegistrationData serviceRegistrationData,
			final ExposedDestinationModel exposedDestination, final HttpEntity<String> request) throws ApiRegistrationException
	{

		final AbstractDestinationModel apiDestination = getApiDestination(exposedDestination.getDestinationTarget().getId());

		final String url = apiDestination.getUrl();

		final ResponseEntity<String> response;
		try
		{
			response = getRestTemplate().getRestTemplate(apiDestination.getCredential()).postForEntity(url, request, String.class);
		}
		catch (final CredentialException e)
		{
			final String errorMessage = String.format(CREDENTIAL_ERROR_MESSAGE, getTargetName(),
					e.getMessage());
			throw logAndCreateApiRegistrationException(errorMessage, e);
		}
		catch (final HttpClientErrorException e)
		{
			final String errorMessage = e.getResponseBodyAsString();
			throw logAndCreateApiRegistrationException(errorMessage, e);
		}
		catch (final ResourceAccessException e)
		{
			final String errorMessage = String.format(
					"Failed to register Exposed Destination in %s with URL: [{%s}]; reason : [{%s}]",
					getTargetName(), url, e.getMessage());
			throw logAndCreateApiRegistrationException(errorMessage, e);
		}
		catch (final RestClientException e)
		{
			final String errorMessage = String.format("Failed to register Exposed Destination in %s with URL: [{%s}]",
					getTargetName(), url);
			throw logAndCreateApiRegistrationException(errorMessage, e);
		}

		if (response.getStatusCode().series() == HttpStatus.Series.SUCCESSFUL)
		{
			LOG.info("Registration of Exposed Destination '{}' successful, response={}", serviceRegistrationData.getIdentifier(),
					response);

			final String serviceId = extractServiceIdFromResponseBody(response.getBody());

			if (StringUtils.isNotBlank(serviceId))
			{
				updateApiConfigurationUid(serviceId, exposedDestination);
			}
		}
		else
		{
			final String errorMessage = String.format(
					"Failed to register Exposed Destination in %s with URL: [{%s}]; response status: [{%s}]",
					getTargetName(), url,
					response.getStatusCode());
			LOG.error(errorMessage);
			throw new ApiRegistrationException(errorMessage);
		}
	}

	/**
	 * @deprecated since 1905. Use {@link KymaApiRegistrationStrategy#getApiDestination(String)}
	 */
	@Deprecated(since = "1905", forRemoval = true)
	protected AbstractDestinationModel getApiDestination() throws ApiRegistrationException
	{
		final String apiDestinationId = Config.getParameter(API_REG_SERVICE_ID);
		final AbstractDestinationModel apiDestination = getDestinationService().getDestinationById(apiDestinationId);
		if (apiDestination == null)
		{
			throw new ApiRegistrationException(
					String.format("Missed Services Consumed Destination with id: [{%s}]", apiDestinationId));
		}
		return apiDestination;
	}

	protected AbstractDestinationModel getApiDestination(final String destinationTargetId) throws ApiRegistrationException
	{
		final String apiDestinationId = Config.getParameter(API_REG_SERVICE_ID);
		final AbstractDestinationModel apiDestination = getDestinationService()
				.getDestinationByIdAndByDestinationTargetId(apiDestinationId, destinationTargetId);
		if (apiDestination == null)
		{
			throw new ApiRegistrationException(
					String.format("Missed Services Consumed Destination with id: [{%s}]", apiDestinationId));
		}
		return apiDestination;
	}


	protected void updateExistingSpecAtKyma(final ServiceRegistrationData serviceRegistrationData,
			final ExposedDestinationModel apiConfiguration, final HttpEntity<String> request) throws ApiRegistrationException
	{

		final AbstractDestinationModel apiDestination = getApiDestination(apiConfiguration.getDestinationTarget().getId());

		final String url = apiDestination.getUrl() + "/{serviceId}";
		final String serviceId = apiConfiguration.getTargetId();
		final ResponseEntity<String> response;
		try
		{
			response = getRestTemplate().getRestTemplate(apiDestination.getCredential()).exchange(url, HttpMethod.PUT, request,
					String.class, serviceId);
		}
		catch (final CredentialException e)
		{
			final String errorMessage = String.format(CREDENTIAL_ERROR_MESSAGE, getTargetName(),
					e.getMessage());
			throw logAndCreateApiRegistrationException(errorMessage, e);
		}
		catch (final HttpClientErrorException e)
		{
			final String messageFormat = Config.getString(DESTINATION_NOT_FOUND, "service with ID '%s' has no API");
			final String serviceNotFoundErrorMessage = String.format(messageFormat, serviceId);
			if (HttpStatus.NOT_FOUND.equals(e.getStatusCode())
					&& serviceNotFoundErrorMessage.equals(e.getResponseBodyAsString().trim()))
			{
				final String errorMessage = String.format(
						"Failed to update registered Exposed Destination with id: %s. There is no targetId: %s in %s",
						apiConfiguration.getId(), serviceId, getTargetName());
				LOG.error(errorMessage);
				throw new DestinationNotFoundException(errorMessage, e);
			}
			else
			{
				final String errorMessage = e.getResponseBodyAsString();
				throw logAndCreateApiRegistrationException(errorMessage, e);
			}
		}
		catch (final ResourceAccessException e)
		{
			final String errorMessage = String.format(
					"Failed to register Exposed Destination in %s with URL: [{%s}]; reason : [{%s}]",
					getTargetName(), url, e.getMessage());
			throw logAndCreateApiRegistrationException(errorMessage, e);
		}
		catch (final RestClientException e)
		{
			final String errorMessage = String.format("Failed to register Exposed Destination in %s with URL: [{%s}]",
					getTargetName(), url);
			throw logAndCreateApiRegistrationException(errorMessage, e);
		}

		if (response.getStatusCode().series() == HttpStatus.Series.SUCCESSFUL)
		{
			LOG.info("Update of Exposed Destination '{}' successful, response={}", serviceRegistrationData.getIdentifier(),
					response);
		}
		else
		{
			final String errorMessage = String.format(
					"Failed to register Exposed Destination in %s with URL: [{%s}]; response status: [{%s}]",
					getTargetName(), url,
					response.getStatusCode());
			LOG.error(errorMessage);
			throw new ApiRegistrationException(errorMessage);
		}
	}


	protected String extractServiceIdFromResponseBody(final String responseBody)
	{
		if (StringUtils.isNotBlank(responseBody))
		{
			try
			{
				final ApiRegistrationResponseData registrationResponse = getJacksonObjectMapper().readValue(responseBody,
						ApiRegistrationResponseData.class);
				return registrationResponse.getId();
			}
			catch (final IOException e)
			{
				LOG.error("Cannot deserialize Json response body from {}; body: {}", getTargetName(),
						responseBody, e);
			}
		}

		LOG.error("responseBody is empty");
		return StringUtils.EMPTY;
	}

	protected void updateApiConfigurationUid(final String systemId, final ExposedDestinationModel destination)
	{
		validateParameterNotNull(destination, "Exposed Destination model cannot be null");

		destination.setTargetId(systemId);
		getModelService().save(destination);
	}

	protected ObjectMapper getJacksonObjectMapper()
	{
		return jacksonObjectMapper;
	}

	@Required
	public void setJacksonObjectMapper(final ObjectMapper jacksonObjectMapper)
	{
		this.jacksonObjectMapper = jacksonObjectMapper;
	}

	protected RestTemplateWrapper getRestTemplate()
	{
		return restTemplate;
	}

	@Required
	public void setRestTemplate(final RestTemplateWrapper restTemplate)
	{
		this.restTemplate = restTemplate;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	@Required
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	protected Converter<ExposedDestinationModel, ServiceRegistrationData> getWebserviceSpecificationConverter()
	{
		return webserviceSpecificationConverter;
	}

	@Required
	public void setWebserviceSpecificationConverter(
			final Converter<ExposedDestinationModel, ServiceRegistrationData> webserviceSpecificationConverter)
	{
		this.webserviceSpecificationConverter = webserviceSpecificationConverter;
	}

	protected Converter<EventsSpecificationSourceData, ServiceRegistrationData> getEventsSpecificationConverter()
	{
		return eventsSpecificationConverter;
	}

	@Required
	public void setEventsSpecificationConverter(
			final Converter<EventsSpecificationSourceData, ServiceRegistrationData> eventsSpecificationConverter)
	{
		this.eventsSpecificationConverter = eventsSpecificationConverter;
	}

	protected EventConfigurationDao getEventConfigurationDao()
	{
		return eventConfigurationDao;
	}

	public void setEventConfigurationDao(final EventConfigurationDao eventConfigurationDao)
	{
		this.eventConfigurationDao = eventConfigurationDao;
	}

	protected DestinationService<AbstractDestinationModel> getDestinationService()
	{
		return destinationService;
	}

	@Required
	public void setDestinationService(final DestinationService<AbstractDestinationModel> destinationService)
	{
		this.destinationService = destinationService;
	}
}
