/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.authentication.registration;

import static de.hybris.platform.integrationbackoffice.widgets.authentication.registration.RegistrationIntegrationObjectConstants.AUTHORITIES;
import static de.hybris.platform.integrationbackoffice.widgets.authentication.registration.RegistrationIntegrationObjectConstants.CALLBACK_URL;
import static de.hybris.platform.integrationbackoffice.widgets.authentication.registration.RegistrationIntegrationObjectConstants.DEFAULT_PASSWORD_LENGTH;
import static de.hybris.platform.integrationbackoffice.widgets.authentication.registration.RegistrationIntegrationObjectConstants.DELIMITER;
import static de.hybris.platform.integrationbackoffice.widgets.authentication.registration.RegistrationIntegrationObjectConstants.EMPTY_STRING;
import static de.hybris.platform.integrationbackoffice.widgets.authentication.registration.RegistrationIntegrationObjectConstants.ENDPOINT_SUFFIX;
import static de.hybris.platform.integrationbackoffice.widgets.authentication.registration.RegistrationIntegrationObjectConstants.ENDPOINT_URL;
import static de.hybris.platform.integrationbackoffice.widgets.authentication.registration.RegistrationIntegrationObjectConstants.ENDPOINT_VERSION;
import static de.hybris.platform.integrationbackoffice.widgets.authentication.registration.RegistrationIntegrationObjectConstants.EXPOSED_DESTINATION_PREFIX;
import static de.hybris.platform.integrationbackoffice.widgets.authentication.registration.RegistrationIntegrationObjectConstants.EXPOSED_DESTINATION_URL;
import static de.hybris.platform.integrationbackoffice.widgets.authentication.registration.RegistrationIntegrationObjectConstants.ICC_CREDENTIAL_BASIC;
import static de.hybris.platform.integrationbackoffice.widgets.authentication.registration.RegistrationIntegrationObjectConstants.ICC_CREDENTIAL_OAUTH;
import static de.hybris.platform.integrationbackoffice.widgets.authentication.registration.RegistrationIntegrationObjectConstants.METADATA;
import static de.hybris.platform.integrationbackoffice.widgets.authentication.registration.RegistrationIntegrationObjectConstants.OAUTH_URL;
import static de.hybris.platform.integrationbackoffice.widgets.authentication.registration.RegistrationIntegrationObjectConstants.OBJECTS_UPDATED_EVENT;
import static de.hybris.platform.integrationbackoffice.widgets.authentication.registration.RegistrationIntegrationObjectConstants.OBJECTS_UPDATED_EVENT_TYPE;
import static de.hybris.platform.integrationbackoffice.widgets.authentication.registration.RegistrationIntegrationObjectConstants.QUERY;
import static de.hybris.platform.integrationbackoffice.widgets.authentication.registration.RegistrationIntegrationObjectConstants.RESOURCES;
import static de.hybris.platform.integrationbackoffice.widgets.authentication.registration.RegistrationIntegrationObjectConstants.VALID_CHARS;

import de.hybris.platform.apiregistryservices.model.AbstractCredentialModel;
import de.hybris.platform.apiregistryservices.model.BasicCredentialModel;
import de.hybris.platform.apiregistryservices.model.DestinationTargetModel;
import de.hybris.platform.apiregistryservices.model.EndpointModel;
import de.hybris.platform.apiregistryservices.model.ExposedDestinationModel;
import de.hybris.platform.apiregistryservices.model.ExposedOAuthCredentialModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.inboundservices.model.InboundChannelConfigurationModel;
import de.hybris.platform.inboundservices.model.IntegrationClientCredentialsDetailsModel;
import de.hybris.platform.integrationbackoffice.widgets.authentication.utility.impl.NameSequenceNumberGenerator;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.util.Config;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.RandomStringUtils;

import com.google.common.collect.Sets;
import com.hybris.cockpitng.core.events.CockpitEventQueue;
import com.hybris.cockpitng.core.events.impl.DefaultCockpitEvent;


/**
 * The default implementation for this interface {@link RegisterIntegrationObjectService}.
 */
public class RegisterIntegrationObjectDefaultService implements RegisterIntegrationObjectService
{
	private FlexibleSearchService flexibleSearchService;
	private NameSequenceNumberGenerator nameSequenceNumberGenerator;
	private ModelService modelService;
	private CockpitEventQueue cockpitEventQueue;
	private KeyGenerator iccCredentialGenerator;

	@Override
	public void createExposedDestinations(final List<DestinationTargetModel> destinationTargets,
	                                      final InboundChannelConfigurationModel inboundChannelConfig,
	                                      final AbstractCredentialModel credential)
	{
		final String ioName = inboundChannelConfig.getIntegrationObject().getCode();
		final List<ExposedDestinationModel> exposedDestinations = new ArrayList<>();
		final int[] nameSequenceNumber = { getNameSequenceNumber(ioName) };

		destinationTargets.forEach(destination ->
				exposedDestinations.add(createExposedDestination(destination, ioName, nameSequenceNumber[0]++, credential))
		);

		inboundChannelConfig.setExposedDestinations(exposedDestinations);
		modelService.save(inboundChannelConfig);
		publishEvent(inboundChannelConfig);
	}

	@Override
	public void createExposedOAuthCredential(final IntegrationClientCredentialsDetailsModel integrationCCD)
	{
		final ExposedOAuthCredentialModel exposedOAuthCredential = modelService.create(ExposedOAuthCredentialModel.class);

		exposedOAuthCredential.setId(integrationCCD.getClientId());
		exposedOAuthCredential.setPassword(integrationCCD.getClientSecret());
		exposedOAuthCredential.setOAuthClientDetails(setIntegrationCCDConfiguredProperties(integrationCCD));

		modelService.save(exposedOAuthCredential);
	}

	@Override
	public AbstractCredentialModel createBasicCredential(final String username, final String password)
	{
		final String clientId = ICC_CREDENTIAL_BASIC.concat(iccCredentialGenerator.generate().toString());

		final BasicCredentialModel basicCredential = modelService.create(BasicCredentialModel.class);
		basicCredential.setId(clientId);
		basicCredential.setUsername(username);
		basicCredential.setPassword(password);

		modelService.save(basicCredential);
		return basicCredential;
	}

	@Override
	public AbstractCredentialModel createOAuthCredential(final EmployeeModel employee)
	{
		final String clientId = ICC_CREDENTIAL_OAUTH.concat(iccCredentialGenerator.generate().toString());
		final char[] clientSecret = generateClientSecret();

		final IntegrationClientCredentialsDetailsModel integrationClientCredentialsDetails = createIntegrationClientCredentialsDetails(
				employee, clientId, clientSecret);
		final ExposedOAuthCredentialModel exposedOAuthCredential = createExposedOAuthCredential(
				integrationClientCredentialsDetails, clientId, clientSecret);

		Arrays.fill(clientSecret, '\u0000');
		return exposedOAuthCredential;
	}

	@Override
	public List<DestinationTargetModel> readDestinationTargets()
	{
		final SearchResult<DestinationTargetModel> result = flexibleSearchService.search(
				buildQuery(DestinationTargetModel._TYPECODE));
		return new ArrayList<>(result.getResult());
	}

	@Override
	public List<BasicCredentialModel> readBasicCredentials()
	{
		final SearchResult<BasicCredentialModel> result = flexibleSearchService.search(
				buildQuery(BasicCredentialModel._TYPECODE));
		return new ArrayList<>(result.getResult());
	}

	@Override
	public List<ExposedOAuthCredentialModel> readExposedOAuthCredentials()
	{
		final SearchResult<ExposedOAuthCredentialModel> result = flexibleSearchService.search(
				buildQuery(ExposedOAuthCredentialModel._TYPECODE));
		return new ArrayList<>(result.getResult());

	}

	@Override
	public List<EmployeeModel> readEmployees()
	{
		final SearchResult<EmployeeModel> result = flexibleSearchService.search(buildQuery(EmployeeModel._TYPECODE));
		return new ArrayList<>(result.getResult());
	}

	private IntegrationClientCredentialsDetailsModel setIntegrationCCDConfiguredProperties(
			final IntegrationClientCredentialsDetailsModel integrationCCD)
	{
		integrationCCD.setOAuthUrl(OAUTH_URL);
		integrationCCD.setAuthorities(getAssignedProperty(AUTHORITIES));
		integrationCCD.setResourceIds(getAssignedProperty(RESOURCES));
		integrationCCD.setRegisteredRedirectUri(Sets.newHashSet(CALLBACK_URL));
		return integrationCCD;
	}

	private IntegrationClientCredentialsDetailsModel createIntegrationClientCredentialsDetails(final EmployeeModel employee,
	                                                                                           final String clientId,
	                                                                                           final char[] clientSecret)
	{
		final IntegrationClientCredentialsDetailsModel clientCredentialsDetails = modelService.create(
				IntegrationClientCredentialsDetailsModel.class);
		clientCredentialsDetails.setClientId(clientId);
		clientCredentialsDetails.setClientSecret(String.valueOf(clientSecret));
		clientCredentialsDetails.setUser(employee);

		modelService.save(setIntegrationCCDConfiguredProperties(clientCredentialsDetails));
		return clientCredentialsDetails;
	}

	private void publishEvent(final InboundChannelConfigurationModel inboundChannelConfig)
	{
		final DefaultCockpitEvent cockpitEvent = new DefaultCockpitEvent(
				OBJECTS_UPDATED_EVENT, inboundChannelConfig, null);
		cockpitEvent.getContext().put(OBJECTS_UPDATED_EVENT_TYPE, true);
		cockpitEventQueue.publishEvent(cockpitEvent);
	}

	private ExposedDestinationModel createExposedDestination(
			final DestinationTargetModel destinationTargetModel,
			final String ioName, final int number, final AbstractCredentialModel credential)
	{
		final ExposedDestinationModel exposedDestination = modelService.create(ExposedDestinationModel.class);
		final EndpointModel endpoint = createEndPoint(ioName, number);
		final String exposedDestinationID = getExposedDestinationID(ioName, number);
		exposedDestination.setId(exposedDestinationID);
		exposedDestination.setUrl(String.format(EXPOSED_DESTINATION_URL, ioName));
		exposedDestination.setDestinationTarget(destinationTargetModel);
		exposedDestination.setEndpoint(endpoint);
		exposedDestination.setActive(true);
		if (credential != null)
		{
			exposedDestination.setCredential(credential);
		}

		return exposedDestination;
	}

	private EndpointModel createEndPoint(final String ioName, final int number)
	{
		final EndpointModel endpoint = modelService.create(EndpointModel.class);
		endpoint.setId(getEndPointID(ioName, number));
		endpoint.setVersion(ENDPOINT_VERSION);
		endpoint.setName(ioName.concat(ENDPOINT_SUFFIX));
		endpoint.setDescription(ioName.concat(ENDPOINT_SUFFIX));
		endpoint.setSpecUrl(String.format(ENDPOINT_URL, ioName));
		return endpoint;
	}

	private String getExposedDestinationID(final String ioName, final int number)
	{
		final String exposedDestinationID = number != 0 ? EXPOSED_DESTINATION_PREFIX.concat(ioName).concat("-" + number)
				: EXPOSED_DESTINATION_PREFIX.concat(ioName);
		return exposedDestinationID.toLowerCase(Locale.getDefault());
	}

	private String getEndPointID(final String ioName, final int number)
	{
		final String endPointID = number != 0 ? EXPOSED_DESTINATION_PREFIX.concat(ioName).concat("-" + number).concat(METADATA)
				: EXPOSED_DESTINATION_PREFIX.concat(ioName).concat(METADATA);
		return endPointID.toLowerCase(Locale.getDefault());
	}

	private int getNameSequenceNumber(final String ioName)
	{
		return nameSequenceNumberGenerator.getGeneratedNumber(ioName);
	}

	private String buildQuery(final String typecode)
	{
		return String.format(QUERY, ItemModel.PK, typecode);
	}

	private char[] generateClientSecret()
	{
		return RandomStringUtils
				.random(DEFAULT_PASSWORD_LENGTH, 0, VALID_CHARS
								.length(), false, false,
						VALID_CHARS.toCharArray(),
						new SecureRandom()).toCharArray();
	}

	private ExposedOAuthCredentialModel createExposedOAuthCredential(
			final IntegrationClientCredentialsDetailsModel clientCredentialsDetails,
			final String clientId,
			final char[] clientSecret)
	{
		final ExposedOAuthCredentialModel exposedOAuthCredential = modelService.create(ExposedOAuthCredentialModel.class);
		exposedOAuthCredential.setId(clientId);
		exposedOAuthCredential.setOAuthClientDetails(clientCredentialsDetails);
		exposedOAuthCredential.setPassword(String.valueOf(clientSecret));
		modelService.save(exposedOAuthCredential);
		return exposedOAuthCredential;
	}

	private String readConfiguredProperty(final String propertyName)
	{
		return Objects.toString(Config.getParameter(propertyName), EMPTY_STRING);
	}

	private Set<String> getAssignedProperty(final String propertyName)
	{
		final Set<String> assignedProperties = new HashSet<>();
		final String property = readConfiguredProperty(propertyName);
		if (property.contains(DELIMITER))
		{
			final String[] propertyArr = property.split(DELIMITER);
			assignedProperties.addAll(Arrays.asList(propertyArr));
		}
		else
		{
			assignedProperties.add(property);
		}

		return assignedProperties;
	}

	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}

	public void setNameSequenceNumberGenerator(
			final NameSequenceNumberGenerator nameSequenceNumberGenerator)
	{
		this.nameSequenceNumberGenerator = nameSequenceNumberGenerator;
	}

	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	public void setCockpitEventQueue(final CockpitEventQueue cockpitEventQueue)
	{
		this.cockpitEventQueue = cockpitEventQueue;
	}

	public void setIccCredentialGenerator(final KeyGenerator iccCredentialGenerator)
	{
		this.iccCredentialGenerator = iccCredentialGenerator;
	}

}
