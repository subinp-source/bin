/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.modals.controllers;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;

import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.integrationbackoffice.services.ReadService;
import de.hybris.platform.integrationbackoffice.widgets.modals.data.CreateIntegrationObjectModalData;
import de.hybris.platform.integrationbackoffice.widgets.modals.utility.ModalUtils;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectModel;
import de.hybris.platform.odata2webservices.enums.IntegrationType;

import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import static de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorUtils.createComboItem;

public final class CloneIntegrationObjectModalController extends DefaultWidgetController
{

	@WireVariable
	private transient ReadService readService;

	private Textbox integrationObjectName;
	private Combobox integrationTypeComboBox;
	private IntegrationObjectModel integrationObjectModel;

	public void setReadService(final ReadService readService)
	{
		this.readService = readService;
	}

	@SocketEvent(socketId = "showModal")
	public void loadCloneIntegrationObjectModal(IntegrationObjectModel integrationObjectModel)
	{
		this.integrationObjectModel = integrationObjectModel;
		loadIntegrationTypes();
	}

	@ViewEvent(componentID = "confirmButton", eventName = Events.ON_CLICK)
	public void cloneIntegrationObject()
	{
		final String serviceName = integrationObjectName.getValue();
		if (("").equals(serviceName))
		{
			Messagebox.show(getLabel("integrationbackoffice.editMode.error.msg.emptyService"),
					getLabel("integrationbackoffice.editMode.error.title.emptyService"), 1, Messagebox.ERROR);
		}
		else if (!ModalUtils.isAlphaNumericName(serviceName))
		{
			Messagebox.show(getLabel("integrationbackoffice.editMode.error.msg.invalidService"),
					getLabel("integrationbackoffice.editMode.error.title.invalidService"), 1, Messagebox.ERROR);
		}
		else if (!ModalUtils.isServiceNameUnique(serviceName, readService))
		{
			Messagebox.show(getLabel("integrationbackoffice.editMode.error.msg.serviceExists"),
					getLabel("integrationbackoffice.editMode.error.title.serviceExists"), 1, Messagebox.ERROR);
		}
		else
		{
			final IntegrationType type;
			if (integrationTypeComboBox.getSelectedItem() != null)
			{
				type = integrationTypeComboBox.getSelectedItem().getValue();
			}
			else
			{
				type = IntegrationType.INBOUND; //Defaults to INBOUND
			}

			ComposedTypeModel rootModel = null;
			if (integrationObjectModel != null)
			{
				IntegrationObjectItemModel rootItem = integrationObjectModel.getRootItem();
				if (rootItem != null)
				{
					rootModel = rootItem.getType();
				}
			}

			sendOutput("confirmButtonClick", new CreateIntegrationObjectModalData(serviceName, rootModel, type));
		}
	}

	private void loadIntegrationTypes()
	{
		readService.getIntegrationTypes()
		           .forEach((type -> integrationTypeComboBox.appendChild(createComboItem(type.getCode(), type))));
	}

}
