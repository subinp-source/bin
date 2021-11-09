/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.editor.controllers;

import static de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorUtils.createComboItem;
import static de.hybris.platform.integrationbackoffice.widgets.editor.utility.IntegrationObjectRootUtils.resolveIntegrationObjectRoot;

import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.integrationbackoffice.services.ReadService;
import de.hybris.platform.integrationservices.model.IntegrationObjectModel;

import java.util.NoSuchElementException;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;

import com.hybris.cockpitng.annotations.GlobalCockpitEvent;
import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.components.Actions;
import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectCRUDHandler;
import com.hybris.cockpitng.util.DefaultWidgetController;

public final class IntegrationObjectSelectorController extends DefaultWidgetController
{

	@Wire
	private Actions actions;
	@WireVariable
	private transient ReadService readService;

	private Combobox integrationObjectComboBox;

	private static final String SETTING_ACTIONS_SLOT = "actions";
	private static final String MODEL_KEY_CURRENT_OBJECT = "currentObject";

	@Override
	public void initialize(final Component component)
	{
		super.initialize(component);
		loadIntegrationObjects();
		initActions();
		resetIntegrationObjectInModel();
	}

	@GlobalCockpitEvent(eventName = ObjectCRUDHandler.OBJECT_CREATED_EVENT, scope = CockpitEvent.SESSION)
	public void handleIntegrationObjectCreatedEvent(final CockpitEvent event)
	{
		if (event.getDataAsCollection().stream().anyMatch(IntegrationObjectModel.class::isInstance))
		{
			loadIntegrationObjects();
			setSelectedIntegrationObject((IntegrationObjectModel) event.getData());
			storeIntegrationObjectInModel(event.getData());
		}
	}

	@GlobalCockpitEvent(eventName = ObjectCRUDHandler.OBJECTS_UPDATED_EVENT, scope = CockpitEvent.SESSION)
	public void handleIntegrationObjectUpdatedEvent(final CockpitEvent event)
	{
		if (event.getDataAsCollection().stream().anyMatch(IntegrationObjectModel.class::isInstance))
		{
			loadIntegrationObjects();
			setSelectedIntegrationObject((IntegrationObjectModel) event.getData());
			storeIntegrationObjectInModel(event.getData());
		}
	}

	@GlobalCockpitEvent(eventName = ObjectCRUDHandler.OBJECTS_DELETED_EVENT, scope = CockpitEvent.SESSION)
	public void handleIntegrationObjectDeletedEvent(final CockpitEvent event)
	{
		if (event.getDataAsCollection().stream().anyMatch(IntegrationObjectModel.class::isInstance))
		{
			loadIntegrationObjects();
			integrationObjectComboBox.setValue(null);
			storeIntegrationObjectInModel((Object) null);
		}
	}

	@SocketEvent(socketId = "receiveRefresh")
	public void refreshButtonOnClick()
	{
		final Comboitem selectedItem = integrationObjectComboBox.getSelectedItem();
		if (selectedItem != null) {
			loadIntegrationObjects(selectedItem.getLabel());
		} else {
			loadIntegrationObjects();
		}
	}

	@SocketEvent(socketId = "receiveSyncNotice")
	public void syncComboBoxes(final IntegrationObjectModel selectedIO) {
		setSelectedIntegrationObject(selectedIO);
	}


	@ViewEvent(componentID = "integrationObjectComboBox", eventName = Events.ON_CHANGE)
	public void integrationObjectComboBoxOnChange()
	{
		if (integrationObjectComboBox.getSelectedItem() != null)
		{
			final IntegrationObjectModel selectedIO = integrationObjectComboBox.getSelectedItem().getValue();
			sendOutput("comboBoxOnChange", resolveIntegrationObjectRoot(selectedIO));
			storeIntegrationObjectInModel((Object) integrationObjectComboBox.getSelectedItem().getValue());
		}
	}

	private void initActions()
	{
		actions.setConfig(String.format("component=%s", getWidgetSettings().getString(SETTING_ACTIONS_SLOT)));
		actions.reload();
	}

	private void loadIntegrationObjects(final String integrationObjectCode)
	{
		loadIntegrationObjects();
		final Comboitem comboitem = integrationObjectComboBox.getItems()
		                                                     .stream()
		                                                     .filter(item -> item.getLabel().equals(integrationObjectCode))
		                                                     .findFirst()
		                                                     .orElseThrow(() -> new NoSuchElementException(
				                                                     String.format("No integration object was found with code %s",
						                                                     integrationObjectCode)));
		Events.sendEvent(Events.ON_CHANGE, integrationObjectComboBox, comboitem);
	}

	private void loadIntegrationObjects()
	{
		integrationObjectComboBox.getItems().clear();
		readService.getIntegrationObjectModels().forEach(integrationObject ->
				integrationObjectComboBox.appendChild(createComboItem(integrationObject.getCode(), integrationObject)));
	}

	private void setSelectedIntegrationObject(final IntegrationObjectModel integrationObject)
	{
		for (final Comboitem comboitem : integrationObjectComboBox.getItems())
		{
			if (comboitem.getValue().equals(integrationObject))
			{
				integrationObjectComboBox.setSelectedIndex(comboitem.getIndex());
				break;
			}
		}
	}

	private void storeIntegrationObjectInModel(Object integrationObject)
	{
		if (integrationObject == null)
		{
			storeIntegrationObjectInModel((String) null);
		}
		else
		{
			IntegrationObjectModel integrationObjectModel = (IntegrationObjectModel) integrationObject;
			storeIntegrationObjectInModel(integrationObjectModel.getRootItem().getType().getCode());
		}
	}

	private void storeIntegrationObjectInModel(String type)
	{
		getModel().setValue(MODEL_KEY_CURRENT_OBJECT, type);
	}

	private void resetIntegrationObjectInModel()
	{
		getModel().setValue(MODEL_KEY_CURRENT_OBJECT, null);
	}

	@SocketEvent(socketId = "selectedItem")
	public void getSelected(final ComposedTypeModel selectedComposedType)
	{
		storeIntegrationObjectInModel(selectedComposedType.getCode());
	}
}