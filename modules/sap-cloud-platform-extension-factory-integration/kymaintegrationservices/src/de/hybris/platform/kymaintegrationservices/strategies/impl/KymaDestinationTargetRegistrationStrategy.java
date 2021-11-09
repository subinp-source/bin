/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.kymaintegrationservices.strategies.impl;


import static de.hybris.platform.kymaintegrationservices.utils.KymaApiExportHelper.API_REG_SERVICE_ID;
import static de.hybris.platform.kymaintegrationservices.utils.KymaApiExportHelper.GETINFO_DESTINATION_ID_KEY;
import static de.hybris.platform.kymaintegrationservices.utils.KymaApiExportHelper.RENEWAL_SERVICE_ID;
import static de.hybris.platform.kymaintegrationservices.utils.KymaConfigurationUtils.getTargetName;
import static de.hybris.platform.kymaintegrationservices.utils.KymaEventExportUtils.EVENTS_SERVICE_ID;

import de.hybris.platform.apiregistryservices.dto.RegisteredDestinationData;
import de.hybris.platform.apiregistryservices.enums.DestinationChannel;
import de.hybris.platform.apiregistryservices.enums.RegistrationStatus;
import de.hybris.platform.apiregistryservices.exceptions.ApiRegistrationException;
import de.hybris.platform.apiregistryservices.exceptions.CredentialException;
import de.hybris.platform.apiregistryservices.exceptions.DeleteDestinationTargetNotPossibleException;
import de.hybris.platform.apiregistryservices.model.AbstractDestinationModel;
import de.hybris.platform.apiregistryservices.model.ConsumedCertificateCredentialModel;
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel;
import de.hybris.platform.apiregistryservices.model.DestinationTargetCronJobModel;
import de.hybris.platform.apiregistryservices.model.DestinationTargetModel;
import de.hybris.platform.apiregistryservices.model.EndpointModel;
import de.hybris.platform.apiregistryservices.model.ExposedDestinationModel;
import de.hybris.platform.apiregistryservices.services.ApiRegistrationService;
import de.hybris.platform.apiregistryservices.services.DestinationService;
import de.hybris.platform.apiregistryservices.strategies.DestinationTargetRegistrationStrategy;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.TriggerModel;
import de.hybris.platform.kymaintegrationservices.event.InvalidateCertificateCredentialsCacheEvent;
import de.hybris.platform.kymaintegrationservices.services.CertificateService;
import de.hybris.platform.kymaintegrationservices.utils.KymaApiExportHelper;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.util.Config;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;


/**
 * Kyma specific implementation of {@link DestinationTargetRegistrationStrategy}
 */
public class KymaDestinationTargetRegistrationStrategy implements DestinationTargetRegistrationStrategy
{
	private static final int KYMA_GETINFO_CRONJOB_TIME_INITIAL_DELAY_MAX = 60;
	private static final int KYMA_GETINFO_CRONJOB_TIME_INITIAL_DELAY_MIN = 0;
	private static final int KYMA_GETINFO_CRONJOB_TIME_INTERVAL_MAX = 60;
	private static final int KYMA_GETINFO_CRONJOB_TIME_INTERVAL_MIN = 0;
	public static final String TOKEN_URL = "tokenUrl";
	private static final Logger LOG = LoggerFactory.getLogger(KymaDestinationTargetRegistrationStrategy.class);
	private static final String KYMA_GETINFO_CRONJOB_TIME_INTERVAL = "kymaintegrationservices.kyma_getinfo_time_interval";
	private static final int DEFAULT_KYMA_GETINFO_CRONJOB_TIME_INTERVAL = 5;
	private static final String KYMA_GETINFO_CRONJOB_TIME_INITIAL_DELAY = "kymaintegrationservices.kyma_getinfo_time_initial_delay";
	private static final int DEFAULT_KYMA_GETINFO_CRONJOB_TIME_INITIAL_DELAY = 5;
	private static final String PLACEHOLDER_URL = "https://mykyma.hybris";

	private ModelService modelService;
	private CertificateService certificateService;
	private DestinationService<AbstractDestinationModel> destinationService;
	private ApiRegistrationService apiRegistrationService;
	private AbstractJobPerformable<CronJobModel> destinationTargetJobPerformable;
	private EventService eventService;

	@Override
	public void registerDestinationTarget(final DestinationTargetModel destinationTarget, final Map<String, String> params)
			throws ApiRegistrationException
	{
		Transaction.current().begin();

		final ConsumedCertificateCredentialModel consumedCertificateCredential = createConsumedCertificateCredential(
				destinationTarget.getId());

		createConsumedDestinations(destinationTarget, consumedCertificateCredential);

		final URI tokenUri;
		try
		{
			tokenUri = new URI(params.get(TOKEN_URL));

			getCertificateService().retrieveCertificate(tokenUri, consumedCertificateCredential);

			destinationTarget.setRegistrationStatus(RegistrationStatus.IN_PROGRESS);
			getModelService().save(destinationTarget);

			startGetInfoCronJob(destinationTarget);

			Transaction.current().commit();
		}
		catch (final URISyntaxException e)
		{
			final String errorMessage = "Token url is not a valid Uri";
			LOG.error(errorMessage);

			Transaction.current().rollback();

			throw new ApiRegistrationException(errorMessage, e);
		}
		catch (final CredentialException e)
		{
			LOG.error(e.getMessage());

			Transaction.current().rollback();

			throw new ApiRegistrationException(e.getMessage(), e);
		}
		catch (final RuntimeException e)
		{
			LOG.error(e.getMessage());

			Transaction.current().rollback();

			throw new ApiRegistrationException(e.getMessage(), e);
		}
	}

	@Override
	public void deregisterDestinationTarget(final DestinationTargetModel destinationTarget) throws ApiRegistrationException,
			DeleteDestinationTargetNotPossibleException
	{
		if (destinationTarget.getRegistrationStatus() == RegistrationStatus.IN_PROGRESS)
		{
			throw new DeleteDestinationTargetNotPossibleException("Deletion of the destination target cannot be performed when its status is IN_PROGRESS");
		}

		syncDestinationTargetWithRemoteSystem(destinationTarget);

		final List<AbstractDestinationModel> destinations = getDestinationService()
				.getDestinationsByDestinationTargetId(destinationTarget.getId());
		final List<ExposedDestinationModel> exposedDestinations = destinations.stream()
				.filter(ExposedDestinationModel.class::isInstance).map(ExposedDestinationModel.class::cast)
				.collect(Collectors.toList());
		final List<ConsumedDestinationModel> consumedDestinations = destinations.stream()
				.filter(ConsumedDestinationModel.class::isInstance).map(ConsumedDestinationModel.class::cast)
				.collect(Collectors.toList());

		for (final ExposedDestinationModel destination : exposedDestinations)
		{
			if (StringUtils.isNotEmpty(destination.getTargetId()))
			{
				getApiRegistrationService().unregisterExposedDestination(destination);
			}
		}

		for (final ConsumedDestinationModel destination : consumedDestinations)
		{
			if (destination.getCredential() != null && destination.getCredential() instanceof ConsumedCertificateCredentialModel)
			{
				getEventService().publishEvent(new InvalidateCertificateCredentialsCacheEvent(destination.getCredential().getId()));
				break;
			}
		}
	}

	@Override
	public List<String> syncDestinationTargetWithRemoteSystem(final DestinationTargetModel destinationTarget)
			throws ApiRegistrationException
	{
		final List<String> registrationFailedDestinations = new ArrayList<>();
		final Map<String, String> registeredDestinations;

		registeredDestinations = getApiRegistrationService()
				.retrieveRegisteredExposedDestinations(DestinationChannel.KYMA, destinationTarget).stream()
				.collect(Collectors.toMap(RegisteredDestinationData::getIdentifier, RegisteredDestinationData::getTargetId));


		final List<ExposedDestinationModel> exposedDestinations = getDestinationService()
				.getActiveExposedDestinationsByDestinationTargetId(destinationTarget.getId());

		for (final ExposedDestinationModel exposedDestination : exposedDestinations)
		{
			try
			{
				final String destinationId = KymaApiExportHelper.getDestinationId(exposedDestination);
				final String targetId = registeredDestinations.get(destinationId);
				if (targetId != null)
				{
					exposedDestination.setTargetId(targetId);
					getModelService().save(exposedDestination);

					getApiRegistrationService().registerExposedDestination(exposedDestination);

					registeredDestinations.remove(destinationId);
				}
				else if (exposedDestination.getTargetId() != null)
				{
					exposedDestination.setTargetId(null);
					getModelService().save(exposedDestination);
				}

			}
			catch (final ApiRegistrationException e)
			{
				LOG.error(String.format("Registration is failed for the destination: %s", destinationTarget.getId()), e);
				registrationFailedDestinations.add(exposedDestination.getId());
			}
		}
		unregisterExposedDestinations(registeredDestinations.values(), destinationTarget);

		return registrationFailedDestinations;
	}

	protected void unregisterExposedDestinations(final Collection<String> targetIds,
			final DestinationTargetModel destinationTarget)
	{
		targetIds.forEach(targetId -> {
			try
			{
				LOG.warn(String.format(
						"Registered in %s Destination with targetId: [%s] is unknown from EC side. Deleting the Destination in %s",
						getTargetName(), targetId, getTargetName()));
				getApiRegistrationService().unregisterExposedDestinationByTargetId(targetId, destinationTarget);
			}
			catch (final ApiRegistrationException e)
			{
				LOG.error(e.getMessage(), e);
			}
		});
	}

	protected ConsumedCertificateCredentialModel createConsumedCertificateCredential(final String credentialId)
	{
		final ConsumedCertificateCredentialModel consumedCertificateCredential = getModelService()
				.create(ConsumedCertificateCredentialModel.class);

		consumedCertificateCredential.setId(credentialId + "-cert");

		getModelService().save(consumedCertificateCredential);

		return consumedCertificateCredential;
	}

	protected void createConsumedDestinations(final DestinationTargetModel destinationTarget,
											  final ConsumedCertificateCredentialModel consumedCertificateCredential)
	{
		final ConsumedDestinationModel getInfoConsumedDestination = generateConsumedDestination(GETINFO_DESTINATION_ID_KEY,
				destinationTarget, consumedCertificateCredential, "GetInfo Endpoint v1", "GetInfo Endpoint v1");

		final ConsumedDestinationModel servicesConsumedDestination = generateConsumedDestination(API_REG_SERVICE_ID,
				destinationTarget, consumedCertificateCredential, "Metadata Services Endpoint v1", "Metadata Services Endpoint v1");

		final ConsumedDestinationModel eventsConsumedDestination = generateConsumedDestination(EVENTS_SERVICE_ID, destinationTarget,
				consumedCertificateCredential, "Events Endpoint v1", "Events Endpoint v1");

		final ConsumedDestinationModel renewalConsumedDestination = generateConsumedDestination(RENEWAL_SERVICE_ID,
				destinationTarget, consumedCertificateCredential, "Renewal Endpoint v1", "Renewal Endpoint v1");

		getModelService().saveAll(eventsConsumedDestination, getInfoConsumedDestination, renewalConsumedDestination,
				servicesConsumedDestination);
	}

	protected ConsumedDestinationModel generateConsumedDestination(final String destinationIdParameter,
			final DestinationTargetModel destinationTarget, final ConsumedCertificateCredentialModel credential,
			final String endpointName, final String endpointDescription)
	{
		final ConsumedDestinationModel destination = getModelService().create(ConsumedDestinationModel.class);
		destination.setId(Config.getParameter(destinationIdParameter));
		destination.setDestinationTarget(destinationTarget);
		destination.setActive(true);
		destination.setCredential(credential);
		destination.setUrl(PLACEHOLDER_URL);
		destination.setEndpoint(
				createEndpoint(destination.getId() + '_' + destinationTarget.getId(), endpointName, "v1", endpointDescription));
		return destination;
	}

	protected void startGetInfoCronJob(final DestinationTargetModel destinationTarget)
	{
		final DestinationTargetCronJobModel destinationTargetCronJob = getModelService().create(DestinationTargetCronJobModel.class);
		destinationTargetCronJob.setSpringId("destinationTargetJobPerformable");
		destinationTargetCronJob.setDestinationTargetId(destinationTarget.getId());
		destinationTargetCronJob.setCode(destinationTarget.getId());
		getModelService().save(destinationTargetCronJob);

		int period = Config.getInt(KYMA_GETINFO_CRONJOB_TIME_INTERVAL, DEFAULT_KYMA_GETINFO_CRONJOB_TIME_INTERVAL);
		period = (period > KYMA_GETINFO_CRONJOB_TIME_INTERVAL_MIN && period < KYMA_GETINFO_CRONJOB_TIME_INTERVAL_MAX)
				? period
				: DEFAULT_KYMA_GETINFO_CRONJOB_TIME_INTERVAL;

		int initialDelay = Config.getInt(KYMA_GETINFO_CRONJOB_TIME_INITIAL_DELAY,
				DEFAULT_KYMA_GETINFO_CRONJOB_TIME_INITIAL_DELAY);
		initialDelay = (initialDelay > KYMA_GETINFO_CRONJOB_TIME_INITIAL_DELAY_MIN
				&& initialDelay <= KYMA_GETINFO_CRONJOB_TIME_INITIAL_DELAY_MAX) ? initialDelay
						: DEFAULT_KYMA_GETINFO_CRONJOB_TIME_INITIAL_DELAY;

		final LocalTime time = LocalTime.now().plusSeconds(initialDelay);

		final String cronJobExpression = String.format("%d %d/%d * ? * * *", time.getSecond(), time.getMinute() % period, period);

		final TriggerModel timeBasedTrigger = getModelService().create(TriggerModel.class);
		timeBasedTrigger.setCronExpression(cronJobExpression);
		timeBasedTrigger.setActive(Boolean.TRUE);
		timeBasedTrigger.setJob(destinationTargetCronJob);
		timeBasedTrigger.setActivationTime(new Date(System.currentTimeMillis()));

		final CronJobModel cronJob = getModelService().create(CronJobModel.class);
		cronJob.setJob(destinationTargetCronJob);
		cronJob.setTriggers(Collections.singletonList(timeBasedTrigger));
		cronJob.setCode(destinationTarget.getId());

		getModelService().saveAll(timeBasedTrigger, cronJob);
	}

	protected EndpointModel createEndpoint(final String id, final String name, final String version, final String description)
	{
		final EndpointModel endpoint = getModelService().create(EndpointModel.class);
		endpoint.setId(id);
		endpoint.setName(name);
		endpoint.setVersion(version);
		endpoint.setDescription(description);
		endpoint.setSpecUrl(PLACEHOLDER_URL);

		getModelService().save(endpoint);

		return endpoint;
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

	protected ApiRegistrationService getApiRegistrationService()
	{
		return apiRegistrationService;
	}

	@Required
	public void setApiRegistrationService(final ApiRegistrationService apiRegistrationService)
	{
		this.apiRegistrationService = apiRegistrationService;
	}

	protected CertificateService getCertificateService()
	{
		return certificateService;
	}

	@Required
	public void setCertificateService(final CertificateService certificateService)
	{
		this.certificateService = certificateService;
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

	protected AbstractJobPerformable<CronJobModel> getDestinationTargetJobPerformable()
	{
		return destinationTargetJobPerformable;
	}

	@Required
	public void setDestinationTargetJobPerformable(final AbstractJobPerformable<CronJobModel> destinationTargetJobPerformable)
	{
		this.destinationTargetJobPerformable = destinationTargetJobPerformable;
	}

	protected EventService getEventService()
	{
		return eventService;
	}

	public void setEventService(final EventService eventService)
	{
		this.eventService = eventService;
	}
}
