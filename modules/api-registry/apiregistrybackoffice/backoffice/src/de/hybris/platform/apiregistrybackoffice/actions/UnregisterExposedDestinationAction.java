/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistrybackoffice.actions;

import com.hybris.cockpitng.dataaccess.context.impl.DefaultContext;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectCRUDHandler;
import com.hybris.cockpitng.dataaccess.util.CockpitGlobalEventPublisher;
import com.hybris.cockpitng.widgets.collectionbrowser.CollectionBrowserController;
import de.hybris.platform.apiregistrybackoffice.constants.ApiregistrybackofficeConstants;
import de.hybris.platform.apiregistryservices.exceptions.ApiRegistrationException;
import de.hybris.platform.apiregistryservices.model.AbstractDestinationModel;
import de.hybris.platform.apiregistryservices.model.ExposedDestinationModel;
import de.hybris.platform.apiregistryservices.services.ApiRegistrationService;

import javax.annotation.Resource;

import org.apache.commons.lang.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;

import java.util.Collections;
import java.util.Objects;
import com.hybris.cockpitng.util.notifications.NotificationService;
import com.hybris.cockpitng.util.notifications.event.NotificationEventTypes;
import com.hybris.cockpitng.util.notifications.event.NotificationEvent;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;


/**
 * Action responsible for unregistering API
 */
public class UnregisterExposedDestinationAction implements CockpitAction<ExposedDestinationModel, String>
{
	private static final String API_CONFIGURATION_UNREGISTER_ACTION_CONFIRMATION = "unregisterApiSpecificationAction.confirmation";
	private static final String API_CONFIGURATION_UNREGISTER_SUCCESS = "unregisterApiSpecificationAction.success";
	private static final String API_CONFIGURATION_UNREGISTER_FAILURE = "unregisterApiSpecificationAction.failure";
	private static final String API_CONFIGURATION_UNREGISTER_NO_REMOTESYSTEM_FAILURE = "unregisterApiSpecificationAction.noRemoteSystem.failure";

	private static final Logger LOG = LoggerFactory.getLogger(UnregisterExposedDestinationAction.class);

	@Resource
	private ApiRegistrationService registrationService;

	@Resource
	private NotificationService notificationService;

	@Resource
	private CockpitGlobalEventPublisher eventPublisher;

	@Override
	public ActionResult<String> perform(final ActionContext<ExposedDestinationModel> ctx)
	{
		final ExposedDestinationModel destination = ctx.getData();

		LOG.info("Triggering Unregister Exposed Destination Action\n\tExposed Destination : [{}]", destination.getId());

		try
		{
			getRegistrationService().unregisterExposedDestination(destination);
		}
		catch (final ApiRegistrationException e)

		{
			final String errorMessage;
			if (e.getCause() != null &&  e.getCause() instanceof HttpClientErrorException && ((HttpClientErrorException) e.getCause()).getStatusCode() == HttpStatus.NOT_FOUND)
			{
				errorMessage = ctx.getLabel(API_CONFIGURATION_UNREGISTER_NO_REMOTESYSTEM_FAILURE, new String[]
						{destination.getDestinationTarget().getId()});
			}
			else
			{
				errorMessage = ctx.getLabel(API_CONFIGURATION_UNREGISTER_FAILURE, new String[]
						{ destination.getId() }) + ' ' + e.getMessage();
			}

			getNotificationService().notifyUser(getNotificationService().getWidgetNotificationSource(ctx),
					NotificationEventTypes.EVENT_TYPE_GENERAL, NotificationEvent.Level.FAILURE, errorMessage);
			LOG.error(errorMessage, e);
			return new ActionResult<>(ActionResult.ERROR);
		}
		final String successMessage = ctx.getLabel(API_CONFIGURATION_UNREGISTER_SUCCESS, new String[]
		{ destination.getUrl() });
		getNotificationService().notifyUser(getNotificationService().getWidgetNotificationSource(ctx),
				ApiregistrybackofficeConstants.NOTIFICATION_TYPE, NotificationEvent.Level.SUCCESS, successMessage);
		LOG.info(successMessage);

		final DefaultContext context = new DefaultContext();
		context.addAttribute(CollectionBrowserController.SHOULD_RELOAD_AFTER_UPDATE, Boolean.TRUE);
		eventPublisher.publish(ObjectCRUDHandler.OBJECTS_UPDATED_EVENT, Collections.singleton(destination), context);

		return new ActionResult<>(ActionResult.SUCCESS);
	}

	@Override
	public boolean canPerform(final ActionContext<ExposedDestinationModel> ctx)
	{
		final ExposedDestinationModel apiConfiguration = ctx.getData();
		return apiConfiguration != null && apiConfiguration.isActive() && Objects.nonNull(apiConfiguration.getDestinationTarget())
				&& BooleanUtils.isNotTrue(apiConfiguration.getDestinationTarget().getTemplate());
	}

	@Override
	public boolean needsConfirmation(final ActionContext<ExposedDestinationModel> ctx)
	{
		return ctx != null && ctx.getData() != null;
	}

	@Override
	public String getConfirmationMessage(final ActionContext<ExposedDestinationModel> ctx)
	{
		final AbstractDestinationModel apiConfiguration = ctx.getData();
		return ctx.getLabel(API_CONFIGURATION_UNREGISTER_ACTION_CONFIRMATION, new String[]
		{ apiConfiguration.getId() });
	}

	protected NotificationService getNotificationService()
	{
		return notificationService;
	}

	protected ApiRegistrationService getRegistrationService()
	{
		return registrationService;
	}
}
