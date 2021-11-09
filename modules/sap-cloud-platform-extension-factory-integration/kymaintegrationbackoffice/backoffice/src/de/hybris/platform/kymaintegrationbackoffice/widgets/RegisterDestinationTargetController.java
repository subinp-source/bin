/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.kymaintegrationbackoffice.widgets;

import static de.hybris.platform.kymaintegrationbackoffice.constants.KymaintegrationbackofficeConstants.DESTINATION_TARGET_REGISTER_SUCCESS;
import static de.hybris.platform.kymaintegrationbackoffice.constants.KymaintegrationbackofficeConstants.DESTINATION_TARGET_REGISTER_WIDGET_EMPTY_FIELD;
import static de.hybris.platform.kymaintegrationbackoffice.constants.KymaintegrationbackofficeConstants.ERROR_MESSAGE;
import static de.hybris.platform.kymaintegrationbackoffice.constants.KymaintegrationbackofficeConstants.REGISTER_DESTINATION_TARGET;
import static de.hybris.platform.kymaintegrationbackoffice.constants.KymaintegrationbackofficeConstants.REGISTER_DESTINATION_TARGET_FAILED;
import static de.hybris.platform.kymaintegrationbackoffice.constants.KymaintegrationbackofficeConstants.SUCCESS_MESSAGE;
import static de.hybris.platform.kymaintegrationbackoffice.constants.KymaintegrationbackofficeConstants.TEMPLATE_DESTINATION_TARGET_SOCKET_ID;

import static com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent.Level.FAILURE;
import static com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent.Level.SUCCESS;
import static com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent.Level.WARNING;

import de.hybris.platform.apiregistryservices.model.DestinationTargetModel;
import de.hybris.platform.apiregistryservices.services.DestinationTargetService;
import de.hybris.platform.kymaintegrationbackoffice.constants.KymaintegrationbackofficeConstants;
import de.hybris.platform.kymaintegrationservices.dto.KymaRegistrationRequest;
import de.hybris.platform.kymaintegrationservices.exceptions.KymaDestinationTargetRegistrationException;
import de.hybris.platform.kymaintegrationservices.services.KymaDestinationTargetRegistrationService;

import java.util.Collections;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Textbox;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.dataaccess.context.impl.DefaultContext;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectCRUDHandler;
import com.hybris.cockpitng.dataaccess.util.CockpitGlobalEventPublisher;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.cockpitng.util.notifications.NotificationService;
import com.hybris.cockpitng.widgets.collectionbrowser.CollectionBrowserController;


public class RegisterDestinationTargetController extends DefaultWidgetController
{

	private static final Logger LOG = LoggerFactory.getLogger(RegisterDestinationTargetController.class);

	@Wire
	private Textbox tokenUrl;
	@Wire
	private Textbox newDestinationTargetId;

	@Resource
	private transient NotificationService notificationService;
	@Resource
	private transient DestinationTargetService destinationTargetService;
	@Resource
	private transient CockpitGlobalEventPublisher eventPublisher;
	@Resource
	private transient KymaDestinationTargetRegistrationService kymaDestinationTargetRegistrationService;

	@SocketEvent(socketId = TEMPLATE_DESTINATION_TARGET_SOCKET_ID)
	public void initializeWithContext(final DestinationTargetModel destinationTarget)
	{
		this.setValue(TEMPLATE_DESTINATION_TARGET_SOCKET_ID, destinationTarget);
	}

	@ViewEvent(componentID = REGISTER_DESTINATION_TARGET, eventName = Events.ON_CLICK)
	public void registerDestinationTarget()
	{
		if (StringUtils.isEmpty(tokenUrl.getValue()) || StringUtils.isEmpty(newDestinationTargetId.getValue()))
		{
			notificationService.notifyUser(getWidgetInstanceManager(), KymaintegrationbackofficeConstants.NOTIFICATION_TYPE,
					WARNING, getLabel(DESTINATION_TARGET_REGISTER_WIDGET_EMPTY_FIELD));
			sendOutput(REGISTER_DESTINATION_TARGET, ERROR_MESSAGE);
			return;
		}

		final DestinationTargetModel templateDestinationTarget = this.getValue(TEMPLATE_DESTINATION_TARGET_SOCKET_ID,
				DestinationTargetModel.class);
		try
		{
			final KymaRegistrationRequest kymaRegistrationRequest = new KymaRegistrationRequest();
			kymaRegistrationRequest.setTokenUrl(tokenUrl.getValue());
			kymaRegistrationRequest.setDestinationTargetId(newDestinationTargetId.getValue());
			kymaRegistrationRequest.setServicesApiUrl(null);
			kymaRegistrationRequest.setTemplateDestinationTargetId(templateDestinationTarget.getId());

			kymaDestinationTargetRegistrationService.registerDestinationTarget(kymaRegistrationRequest,false);

			final String successMessage = getLabel(DESTINATION_TARGET_REGISTER_SUCCESS,
					new String[]{ newDestinationTargetId.getValue() });

			LOG.info(successMessage);

			notificationService.notifyUser(getWidgetInstanceManager(), KymaintegrationbackofficeConstants.NOTIFICATION_TYPE,
					SUCCESS, successMessage);

			final DefaultContext context = new DefaultContext();
			context.addAttribute(CollectionBrowserController.SHOULD_RELOAD_AFTER_UPDATE, Boolean.TRUE);
			eventPublisher.publish(ObjectCRUDHandler.OBJECTS_UPDATED_EVENT,
					Collections.singleton(destinationTargetService.getDestinationTargetById(newDestinationTargetId.getValue())),
					context);

			sendOutput(REGISTER_DESTINATION_TARGET, SUCCESS_MESSAGE);
		}
		catch (final KymaDestinationTargetRegistrationException ex)
		{
			final String errorMessage = String.format(getLabel(REGISTER_DESTINATION_TARGET_FAILED),
					newDestinationTargetId.getValue()) + ' ' + ex.getMessage();
			LOG.error(errorMessage, ex);
			notificationService.notifyUser(getWidgetInstanceManager(), KymaintegrationbackofficeConstants.NOTIFICATION_TYPE,
					FAILURE, errorMessage);

			sendOutput(REGISTER_DESTINATION_TARGET, ERROR_MESSAGE);
		}
	}

}
