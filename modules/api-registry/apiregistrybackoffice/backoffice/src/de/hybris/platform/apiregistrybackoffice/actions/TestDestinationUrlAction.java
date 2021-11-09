/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistrybackoffice.actions;

import de.hybris.platform.apiregistrybackoffice.constants.ApiregistrybackofficeConstants;
import de.hybris.platform.apiregistryservices.exceptions.DestinationNotFoundException;
import de.hybris.platform.apiregistryservices.model.AbstractDestinationModel;
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel;
import de.hybris.platform.apiregistryservices.model.ExposedDestinationModel;
import de.hybris.platform.apiregistryservices.services.DestinationService;

import javax.annotation.Resource;

import java.net.SocketTimeoutException;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.util.notifications.NotificationService;
import com.hybris.cockpitng.util.notifications.event.NotificationEvent;
import com.hybris.cockpitng.util.notifications.event.NotificationEventTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;



public class TestDestinationUrlAction implements CockpitAction<AbstractDestinationModel, String>
{

	private static final String PING_REMOTE_SYSTEM_SUCCESS = "testDestinationUrl.success";
	private static final String PING_REMOTE_SYSTEM_FAILURE  = "testDestinationUrl.failure";
	private static final String PING_REMOTE_SYSTEM_FAILURE_TIMEOUT  = "testDestinationUrl.failure.timeout";
	private static final String PING_REMOTE_SYSTEM_FAILURE_OTHER_REASON  = "testDestinationUrl.failure.other.reason";

	private static final Logger LOG = LoggerFactory.getLogger(TestDestinationUrlAction.class);

	@Resource
	private DestinationService<AbstractDestinationModel> destinationService;

	@Resource
	private NotificationService notificationService;

	@Override
	public ActionResult<String> perform(final ActionContext<AbstractDestinationModel> actionContext)
	{
		final AbstractDestinationModel destinationModel = actionContext.getData();

		LOG.info("Triggering Ping Destination Url Action\n\tDestination : [{}]", destinationModel.getId());

		try
		{
			getDestinationService().testDestinationUrl(destinationModel);
		}
		catch (final DestinationNotFoundException e)
		{
			final String errorMessage;
			if (e.getCause() instanceof HttpClientErrorException)

			{
				errorMessage = actionContext.getLabel(PING_REMOTE_SYSTEM_FAILURE, new String[]
						{destinationModel.getUrl(), ((HttpClientErrorException) e.getCause()).getStatusCode().toString()});
			}
			else if(e.getCause() instanceof HttpServerErrorException)
			{
				errorMessage = actionContext.getLabel(PING_REMOTE_SYSTEM_FAILURE, new String[]
						{destinationModel.getUrl(), ((HttpServerErrorException) e.getCause()).getStatusCode().toString()});
			}
			else if(e.getCause() instanceof SocketTimeoutException)
			{
				errorMessage = actionContext.getLabel(PING_REMOTE_SYSTEM_FAILURE_TIMEOUT, new String[]
						{destinationModel.getUrl()});
			}
			else
			{
				errorMessage = actionContext.getLabel(PING_REMOTE_SYSTEM_FAILURE_OTHER_REASON, new String[]
						{destinationModel.getUrl()});
			}
			getNotificationService().notifyUser(getNotificationService().getWidgetNotificationSource(actionContext),
					NotificationEventTypes.EVENT_TYPE_GENERAL, NotificationEvent.Level.FAILURE, errorMessage);
			LOG.error(errorMessage, e);
			return new ActionResult<>(ActionResult.ERROR);
		}
		final String successMessage = actionContext.getLabel(PING_REMOTE_SYSTEM_SUCCESS, new String[]
				{ destinationModel.getUrl() });
		getNotificationService().notifyUser(getNotificationService().getWidgetNotificationSource(actionContext),
				ApiregistrybackofficeConstants.NOTIFICATION_TYPE, NotificationEvent.Level.SUCCESS, successMessage);
		LOG.info(successMessage);
		return new ActionResult<>(ActionResult.SUCCESS);
	}

	@Override
	public boolean canPerform(final ActionContext<AbstractDestinationModel> ctx)
	{
		final AbstractDestinationModel destinationModel = ctx.getData();
		if(destinationModel instanceof ConsumedDestinationModel || destinationModel instanceof ExposedDestinationModel)
		{
			return destinationModel.getUrl() != null;
		}

		return false;
	}

	protected NotificationService getNotificationService()
	{
		return notificationService;
	}

	protected DestinationService<AbstractDestinationModel> getDestinationService()
	{
		return destinationService;
	}

}
