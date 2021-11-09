/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.modals.controllers;

import de.hybris.platform.integrationbackoffice.constants.IntegrationbackofficeConstants;
import de.hybris.platform.integrationbackoffice.dto.ListItemVirtualAttributeDTO;
import de.hybris.platform.jalo.JaloObjectNoLongerValidException;

import org.zkoss.lang.Strings;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Textbox;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.cockpitng.util.notifications.NotificationService;
import com.hybris.cockpitng.util.notifications.event.NotificationEvent;

public class VirtualAttributeDetailsModalController extends DefaultWidgetController
{
	@WireVariable
	private transient NotificationService notificationService;

	private Textbox code;
	private Textbox location;
	private Textbox type;


	@SocketEvent(socketId = "showModal")
	public void showDetails(final ListItemVirtualAttributeDTO dto)
	{
		code.setValue(dto.getRetrievalDescriptor().getCode());
		try
		{
			location.setValue(dto.getRetrievalDescriptor().getLogicLocation());
		}
		catch (JaloObjectNoLongerValidException exception)
		{
			notificationService.notifyUser(Strings.EMPTY, IntegrationbackofficeConstants.NOTIFICATION_TYPE,
					NotificationEvent.Level.WARNING,
					getLabel("integrationbackoffice.virtualAttributeDetailsModal.VADMBroken"));
		}
		type.setValue(dto.getRetrievalDescriptor().getType().getCode());
	}
}
