/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.kymaintegrationservices.jobs;

import static de.hybris.platform.kymaintegrationservices.utils.KymaApiExportHelper.API_REG_SERVICE_ID;
import static de.hybris.platform.kymaintegrationservices.utils.KymaApiExportHelper.DEFAULT_GETINFO_DESTINATION_ID;
import static de.hybris.platform.kymaintegrationservices.utils.KymaApiExportHelper.GETINFO_DESTINATION_ID_KEY;
import static de.hybris.platform.kymaintegrationservices.utils.KymaApiExportHelper.RENEWAL_SERVICE_ID;
import static de.hybris.platform.kymaintegrationservices.utils.KymaApiExportHelper.isUrlsEqualIgnoringQuery;
import static de.hybris.platform.kymaintegrationservices.utils.KymaConfigurationUtils.getTargetName;
import static de.hybris.platform.kymaintegrationservices.utils.KymaEventExportUtils.EVENTS_SERVICE_ID;

import de.hybris.platform.apiregistryservices.enums.DestinationChannel;
import de.hybris.platform.apiregistryservices.enums.RegistrationStatus;
import de.hybris.platform.apiregistryservices.exceptions.ApiRegistrationException;
import de.hybris.platform.apiregistryservices.exceptions.CredentialException;
import de.hybris.platform.apiregistryservices.model.AbstractDestinationModel;
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel;
import de.hybris.platform.apiregistryservices.model.DestinationTargetCronJobModel;
import de.hybris.platform.apiregistryservices.model.DestinationTargetModel;
import de.hybris.platform.apiregistryservices.services.DestinationService;
import de.hybris.platform.apiregistryservices.services.DestinationTargetService;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.kymaintegrationservices.dto.KymaInfoData;
import de.hybris.platform.kymaintegrationservices.dto.KymaServicesUrls;
import de.hybris.platform.kymaintegrationservices.utils.RestTemplateWrapper;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.Config;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;



/**
 * A job responsible for updating consumed destinations of a destination target and registration of exposed destinations
 */
public class KymaUpdateConsumedDestinationsJobPerformable extends AbstractJobPerformable<CronJobModel>
{
	private static final int DEFAULT_CRONJOB_LIFESPAN_MINUTES = 20;
	private static final Logger LOG = LoggerFactory.getLogger(KymaUpdateConsumedDestinationsJobPerformable.class);
	public static final String CRONJOB_LIFESPAN_PROP_KEY = "kymaintegrationservice.getinfo.cronjob.lifespan";
	public static final int DEFAULT_CRONJOB_LIFESPAN = 20;
	private DestinationService<AbstractDestinationModel> destinationService;
	private DestinationTargetService destinationTargetService;
	private RestTemplateWrapper restTemplate;

	@Override
	public PerformResult perform(final CronJobModel cronJob)
	{
		final DestinationTargetCronJobModel destinationTargetCronJob = (DestinationTargetCronJobModel) cronJob.getJob();
		final DestinationTargetModel destinationTarget = getDestinationTargetService().getDestinationTargetById(destinationTargetCronJob.getDestinationTargetId());
		LOG.info("Destination target cron job with id: {}", destinationTarget.getId());

		if (RegistrationStatus.ERROR.equals(destinationTarget.getRegistrationStatus()))
		{
			LOG.debug("Destination target registration status is ERROR, aborting cron job with id: {}.",
					destinationTarget.getId());
			getModelService().removeAll(cronJob.getTriggers());
			return new PerformResult(CronJobResult.FAILURE, CronJobStatus.ABORTED);
		}

		if (cronJob.getCreationtime().before(DateTime.now().minusMinutes(Config.getInt(CRONJOB_LIFESPAN_PROP_KEY, DEFAULT_CRONJOB_LIFESPAN_MINUTES)).toDate())
				&& destinationTarget.getRegistrationStatus() == RegistrationStatus.IN_PROGRESS)
		{
			final String errorMessage = String.format(
					"Could not retrieve required urls within %d minutes, integration failed. Destination target with id: %s - registration status is set to Error.",
					Config.getInt(CRONJOB_LIFESPAN_PROP_KEY, DEFAULT_CRONJOB_LIFESPAN), destinationTarget.getId());
			LOG.error(errorMessage);
			destinationTarget.setRegistrationStatus(RegistrationStatus.ERROR);
			destinationTarget.setRegistrationStatusInfo(errorMessage);
			getModelService().removeAll(cronJob.getTriggers());
			getModelService().save(destinationTarget);

			return new PerformResult(CronJobResult.FAILURE, CronJobStatus.FINISHED);
		}

		try
		{
			final String getInfoDestinationId = Config.getString(GETINFO_DESTINATION_ID_KEY, DEFAULT_GETINFO_DESTINATION_ID);
			final ConsumedDestinationModel getInfoDestination = (ConsumedDestinationModel) getDestinationService()
					.getDestinationByIdAndByDestinationTargetId(getInfoDestinationId, destinationTarget.getId());

			final KymaInfoData infoData = getRestTemplate().getRestTemplate(getInfoDestination.getCredential())
					.getForObject(getInfoDestination.getUrl(), KymaInfoData.class);

			if (!validateInfoData(infoData))
			{
				if(RegistrationStatus.REGISTERED.equals(destinationTarget.getRegistrationStatus()))
				{
					updateDestinationTargetRegistrationStatusInfo(destinationTarget, String.format("Invalid response from {%s} endpoint", getInfoDestinationId));
				}
				else
				{
					updateDestinationTargetRegistrationStatus(destinationTarget, RegistrationStatus.IN_PROGRESS);
				}
				return new PerformResult(CronJobResult.FAILURE, CronJobStatus.FINISHED);
			}

			//clear status field
			updateDestinationTargetRegistrationStatusInfo(destinationTarget, StringUtils.EMPTY);

			if (!needUpdateConsumedDestinations(infoData.getUrls(), destinationTarget))
			{
				LOG.info("Destination Target with id : {} is up to date", destinationTarget.getId());
				return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
			}

			updateConsumedDestinations(destinationTarget, infoData.getUrls());
			updateDestinationTargetRegistrationStatus(destinationTarget, RegistrationStatus.REGISTERED);
			registerExposedDestinations(destinationTarget);
		}
		catch (final CredentialException | ApiRegistrationException e)
		{
			LOG.error("Failed to update destination target destinations", e);
			return new PerformResult(CronJobResult.FAILURE, CronJobStatus.FINISHED);
		}

		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	@Override
	public boolean isAbortable()
	{
		return true;
	}

	protected boolean validateInfoData(final KymaInfoData infoData)
	{
		if (infoData == null || infoData.getUrls() == null || validateInfoUrls(infoData.getUrls()))
		{
			if (LOG.isWarnEnabled())
			{
				LOG.warn(String.format("%s cluster is not provisioned yet", getTargetName()));
			}
			return false;
		}
		return true;
	}

	protected boolean validateInfoUrls(final KymaServicesUrls urls)
	{
		final StringBuilder validationMessage = new StringBuilder();
		if(StringUtils.isEmpty(urls.getEventsUrl()))
		{
			validationMessage.append("Events url is empty\n");
		}
		if(StringUtils.isEmpty(urls.getMetadataUrl()))
		{
			validationMessage.append("Metadata url is empty\n");
		}
		if(StringUtils.isEmpty(urls.getRenewCertUrl()))
		{
			validationMessage.append("Renew Certificate url is empty\n");
		}

		//urls are invalid
		if(validationMessage.length() > 0)
		{
			if(LOG.isErrorEnabled())
			{
				LOG.error(validationMessage.toString());
			}
			return true;
		}

		return false;
	}

	protected void updateDestinationTargetRegistrationStatus(final DestinationTargetModel destinationTarget,
			final RegistrationStatus status)
	{
		if (status.equals(destinationTarget.getRegistrationStatus()))
		{
			return;
		}
		destinationTarget.setRegistrationStatus(status);
		getModelService().save(destinationTarget);
	}

	protected void updateDestinationTargetRegistrationStatusInfo(final DestinationTargetModel destinationTarget,
															 final String statusInfo)
	{
		if (destinationTarget == null)
		{
			LOG.error("Cannot change RegistrationStatusInfo of 'null' DestinationTarget");
			return;
		}
		if (statusInfo == null)
		{
			final String errorMessage = String.format("Cannot change RegistrationStatusInfo of DestinationTarget with id : %s to 'null' value", destinationTarget.getId());
			LOG.error(errorMessage);
			return;
		}

		if (statusInfo.equals(destinationTarget.getRegistrationStatusInfo()))
		{
			return;
		}
		destinationTarget.setRegistrationStatusInfo(statusInfo);
		getModelService().save(destinationTarget);
	}

	protected void updateConsumedDestinations(final DestinationTargetModel destinationTarget, final KymaServicesUrls urls)
			throws ApiRegistrationException
	{
		validateConsumedDestinations(urls, destinationTarget);

		final ConsumedDestinationModel eventsDestination = getConsumedDestination(destinationTarget, EVENTS_SERVICE_ID);
		final ConsumedDestinationModel metadataDestination = getConsumedDestination(destinationTarget, API_REG_SERVICE_ID);
		final ConsumedDestinationModel renewalDestination = getConsumedDestination(destinationTarget, RENEWAL_SERVICE_ID);

		updateConsumedDestination(eventsDestination, urls.getEventsUrl());
		updateConsumedDestination(metadataDestination, urls.getMetadataUrl());
		updateConsumedDestination(renewalDestination, urls.getRenewCertUrl());

		getModelService().saveAll(eventsDestination, metadataDestination, renewalDestination);
	}

	protected void registerExposedDestinations(final DestinationTargetModel destinationTarget) throws ApiRegistrationException
	{
		getDestinationTargetService().syncDestinationTargetWithRemoteSystem(destinationTarget);

		final Map<String, String> errors = getDestinationTargetService().registerExposedDestinations(destinationTarget);

		if (!errors.isEmpty())
		{
			final String listedErrorsAsString = errors.entrySet().stream().map(e -> String.join(":", e.getKey(), e.getValue())).collect(Collectors.joining(", "));
			final String errorMessage = String.format("Registration failed for the following destinations: %s",
					listedErrorsAsString);
			throw new ApiRegistrationException(errorMessage);
		}
	}

	protected void validateConsumedDestinations(final KymaServicesUrls urls, final DestinationTargetModel destinationTarget)
			throws ApiRegistrationException
	{
		final List<ConsumedDestinationModel> allConsumedDestinations = getDestinationService().getAllConsumedDestinations().stream()
				.filter(destination -> !destination.getDestinationTarget().getId().equals(destinationTarget.getId()) &&
						DestinationChannel.KYMA.equals(destination.getDestinationTarget().getDestinationChannel())
				)
				.collect(Collectors.toList());
		final String eventsUrl = urls.getEventsUrl();
		final String metadataUrl = urls.getMetadataUrl();
		for (final ConsumedDestinationModel consumedDestination : allConsumedDestinations)
		{
			try
			{
				if (isUrlsEqualIgnoringQuery(eventsUrl, consumedDestination.getUrl())
						|| isUrlsEqualIgnoringQuery(metadataUrl, consumedDestination.getUrl()))
				{
					final String errorMessage = String.format(
							"Destination target with id: [{%s}] already connected to the application with URL: [{%s}]",
							destinationTarget.getId(), consumedDestination.getUrl());
					destinationTarget.setRegistrationStatus(RegistrationStatus.ERROR);
					destinationTarget.setRegistrationStatusInfo(errorMessage);
					getModelService().save(destinationTarget);
					throw new ApiRegistrationException(errorMessage);
				}
			}
			catch (final URISyntaxException e)
			{
				final String errorMessage = String.format(
						"Destination target with id: [{%s}] has invalid destination(s). Please check events URL: [{%s}] and metadata URL: [{%s}]",
						destinationTarget.getId(), eventsUrl, metadataUrl);
				LOG.error(errorMessage, e);
				throw new ApiRegistrationException(errorMessage, e);
			}
		}
	}

	protected ConsumedDestinationModel getConsumedDestination(final DestinationTargetModel destinationTarget,
			final String serviceId)
	{
		final String consumedDestinationId = Config.getString(serviceId, "");
		final ConsumedDestinationModel consumedDestination = (ConsumedDestinationModel) getDestinationService()
				.getDestinationByIdAndByDestinationTargetId(consumedDestinationId, destinationTarget.getId());

		if (consumedDestination == null)
		{
			final String errorMessage = String.format("Missing Consumed Destination with id : [{%s}]", consumedDestinationId);
			LOG.error(errorMessage);
		}
		return consumedDestination;
	}

	protected boolean needUpdateConsumedDestinations(final KymaServicesUrls urls, final DestinationTargetModel destinationTarget)
	{
		final ConsumedDestinationModel eventsDestination = getConsumedDestination(destinationTarget, EVENTS_SERVICE_ID);
		final ConsumedDestinationModel metadataDestination = getConsumedDestination(destinationTarget, API_REG_SERVICE_ID);
		final ConsumedDestinationModel renewalDestination = getConsumedDestination(destinationTarget, RENEWAL_SERVICE_ID);

		return !urls.getMetadataUrl().equals(metadataDestination.getUrl())
				|| !urls.getEventsUrl().equals(eventsDestination.getUrl())
				|| !urls.getRenewCertUrl().equals(renewalDestination.getUrl());
	}

	protected void updateConsumedDestination(final ConsumedDestinationModel consumedDestination, final String destinationUrl)
	{
		consumedDestination.setUrl(destinationUrl);
	}

	protected DestinationTargetService getDestinationTargetService()
	{
		return destinationTargetService;
	}

	@Required
	public void setDestinationTargetService(final DestinationTargetService destinationTargetService)
	{
		this.destinationTargetService = destinationTargetService;
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

	protected ModelService getModelService()
	{
		return modelService;
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

}

