/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.services.impl;

import de.hybris.platform.apiregistryservices.dao.DestinationDao;
import de.hybris.platform.apiregistryservices.enums.DestinationChannel;
import de.hybris.platform.apiregistryservices.exceptions.CredentialException;
import de.hybris.platform.apiregistryservices.exceptions.DestinationNotFoundException;
import de.hybris.platform.apiregistryservices.model.AbstractDestinationModel;
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel;
import de.hybris.platform.apiregistryservices.model.ExposedDestinationModel;
import de.hybris.platform.apiregistryservices.services.DestinationService;
import de.hybris.platform.apiregistryservices.utils.DefaultRestTemplateFactory;
import de.hybris.platform.util.Config;

import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


/**
 * Default implementation of {@link DestinationService}
 *
 * @param <T>
 *           the type parameter which extends the {@link AbstractDestinationModel} type
 */
public class DefaultDestinationService<T extends AbstractDestinationModel> implements DestinationService
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultDestinationService.class);
	private static final String HTTP_STATUS_ERROR_CODES = "apiregistryservices.testConsumedDestinationUrl.httpstatus.error.codes";
	private static final String DEFAULT_HTTP_STATUS_ERROR_CODES = "400,404,403,407,401";
	private static final String DEPLOYMENT_API_ENDPOINT_PROP = "{ccv2.services.backoffice.url.0}";
	private static final String DEPLOYMENT_API_ENDPOINT_PARAM = "ccv2.services.backoffice.url.0";

	private DestinationDao<T> destinationDao;

	/**
	 * @deprecated since 1905. Use {@link DestinationService#getDestinationsByDestinationTargetId(String)}
	 */
	@Override
	@Deprecated(since = "1905", forRemoval = true)
	public List<T> getDestinationsByChannel(final DestinationChannel channel)
	{
		return getDestinationDao().getDestinationsByChannel(channel);
	}

	@Override
	public List<T> getDestinationsByDestinationTargetId(final String  destinationTargetId)
	{
		return getDestinationDao().getDestinationsByDestinationTargetId(destinationTargetId);
	}

	@Override
	public List<ExposedDestinationModel> getActiveExposedDestinationsByClientId(final String clientId)
	{
		return getDestinationDao().findActiveExposedDestinationsByClientId(clientId);
	}

	/**
	 * @deprecated since 1905. Use {@link DestinationService#getActiveExposedDestinationsByDestinationTargetId(String)}
	 */
	@Override
	@Deprecated(since = "1905", forRemoval = true)
	public List<ExposedDestinationModel> getActiveExposedDestinationsByChannel(final DestinationChannel channel)
	{
		return getDestinationDao().findActiveExposedDestinationsByChannel(channel);
	}

	@Override
	public List<ExposedDestinationModel> getActiveExposedDestinationsByDestinationTargetId(final String destinationTargetId)
	{
		return getDestinationDao().findActiveExposedDestinationsByDestinationTargetId(destinationTargetId);
	}

	@Override
	public AbstractDestinationModel getDestinationByIdAndByDestinationTargetId(final String destinationId,
			final String destinationTargetId)
	{
		return getDestinationDao().findDestinationByIdAndByDestinationTargetId(destinationId,destinationTargetId);
	}

	/**
	 * @deprecated since 1905. Use {@link DestinationService#getDestinationByIdAndByDestinationTargetId(String, String)}
	 */
	@Override
	@Deprecated(since = "1905", forRemoval = true)
	public T getDestinationById(final String id)
	{
		return getDestinationDao().getDestinationById(id);
	}

	@Override
	public List getAllDestinations()
	{
		return getDestinationDao().findAllDestinations();
	}


	@Override
	public void testDestinationUrl(final AbstractDestinationModel destinationModel) throws DestinationNotFoundException
	{
		try
		{

			final HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.ALL));
			headers.setContentType(MediaType.APPLICATION_JSON);
			final HttpEntity<Object> httpEntity = new HttpEntity<>(headers);

			final RestTemplate restTemplate = getRestTemplate(destinationModel);
			final ResponseEntity<String> response = restTemplate.exchange(validateExposedDestinationUrl(destinationModel.getUrl()), HttpMethod.GET, httpEntity, String.class);

			if(response.getStatusCode().series() != HttpStatus.Series.SUCCESSFUL)
			{
				final String errorMessage = String.format("Remote System with the url [{%s}] is not reachable",
						destinationModel.getUrl());
				LOG.error(errorMessage);
				throw new DestinationNotFoundException(errorMessage, new RestClientException(errorMessage));
			}

		}
		catch (final HttpClientErrorException | HttpServerErrorException e)
		{
			final String errorCodes = Config.getString(HTTP_STATUS_ERROR_CODES,  DEFAULT_HTTP_STATUS_ERROR_CODES);
			final Pattern pattern = Pattern.compile(",");
			final List<Integer> httpStatusCodes = pattern.splitAsStream(errorCodes)
					.map(Integer::valueOf)
					.collect(Collectors.toList());

			if(httpStatusCodes.contains(e.getRawStatusCode()))
			{
				throw new DestinationNotFoundException(e.getMessage(), e);
			}
		}
		catch (final ResourceAccessException e)
		{
			if(e.getCause() instanceof SocketTimeoutException)
			{
				throw new DestinationNotFoundException(e.getMessage(), e.getCause());
			}
			else
			{
				throw new DestinationNotFoundException(e.getMessage(), e);
			}

		}
		catch (final Exception e)
		{
			final String errorMessage = String.format("Remote System with the url [{%s}] is not reachable",
					destinationModel.getUrl());
			LOG.error(errorMessage, e);
			throw new DestinationNotFoundException(e.getMessage(), e);
		}
	}

	protected DestinationDao<T> getDestinationDao()
	{
		return destinationDao;
	}

	@Required
	public void setDestinationDao(final DestinationDao<T> destinationDao)
	{
		this.destinationDao = destinationDao;
	}

	@Override
	public List<ConsumedDestinationModel> getAllConsumedDestinations()
	{
		return getDestinationDao().findAllConsumedDestinations();
	}

	protected RestTemplate getRestTemplate(final AbstractDestinationModel destinationModel) throws CredentialException
	{
		final DefaultRestTemplateFactory defaultRestTemplateFactory = new DefaultRestTemplateFactory();
		return defaultRestTemplateFactory.getRestTemplate(destinationModel);
	}

	protected String validateExposedDestinationUrl(final String exposedDestinationUrl)
	{
		return exposedDestinationUrl.contains(DEPLOYMENT_API_ENDPOINT_PROP) ?
				exposedDestinationUrl.replace(DEPLOYMENT_API_ENDPOINT_PROP, Config.getParameter(DEPLOYMENT_API_ENDPOINT_PARAM)) : exposedDestinationUrl;
	}

	@Override
	public List<ExposedDestinationModel> getExposedDestinationsByCredentialId(final String credentialId)
	{
		return getDestinationDao().findExposedDestinationsByCredentialId(credentialId);
	}
}
