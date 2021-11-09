/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.authorization.controllers;

import static de.hybris.platform.integrationbackoffice.widgets.authorization.utility.AccessRightsUtils.createListitem;

import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectModel;

import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;

import com.hybris.cockpitng.annotations.GlobalCockpitEvent;
import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectCRUDHandler;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.cockpitng.util.UITools;

/**
 * Controls functionality of the Access Rights widget (security matrix)
 */
public class IntegrationObjectAccessRightsController extends DefaultWidgetController
{
	private Listbox integrationObjectItemListbox;
	private IntegrationObjectModel selectedIO;
	private boolean isSearchBarEnabled = false;

	@Override
	public void initialize(final Component comp)
	{
		UITools.postponeExecution(comp, this::clearPermissionMatrix);
	}

	@GlobalCockpitEvent(eventName = ObjectCRUDHandler.OBJECT_CREATED_EVENT, scope = CockpitEvent.SESSION)
	public void handleIntegrationObjectCreatedEvent(final CockpitEvent event)
	{
		if (event.getDataAsCollection().stream().anyMatch(IntegrationObjectModel.class::isInstance))
		{
			setSelectedIO((IntegrationObjectModel) event.getData());
		}
	}

	@GlobalCockpitEvent(eventName = ObjectCRUDHandler.OBJECTS_UPDATED_EVENT, scope = CockpitEvent.SESSION)
	public void handleIntegrationObjectUpdatedEvent(final CockpitEvent event)
	{
		if (event.getDataAsCollection().stream().anyMatch(IntegrationObjectModel.class::isInstance))
		{
			setSelectedIO((IntegrationObjectModel) event.getData());
		}
	}

	@GlobalCockpitEvent(eventName = ObjectCRUDHandler.OBJECTS_DELETED_EVENT, scope = CockpitEvent.SESSION)
	public void handleIntegrationObjectDeletedEvent(final CockpitEvent event)
	{
		if (event.getDataAsCollection().stream().anyMatch(IntegrationObjectModel.class::isInstance))
		{
			integrationObjectItemListbox.getItems().clear();
			clearPermissionMatrix();
		}
	}

	@SocketEvent(socketId = "receiveIntegrationObjectComboBox")
	public void setSelectedIO(final IntegrationObjectModel integrationObject)
	{
		selectedIO = integrationObject;
		populateIntegrationObjectItemsListbox();
		clearPermissionMatrix();
	}

	private void populateIntegrationObjectItemsListbox()
	{
		integrationObjectItemListbox.getItems().clear();

		Set<IntegrationObjectItemModel> integrationObjectItems = selectedIO.getItems();
		integrationObjectItems.stream()
		                      .sorted((item1, item2) -> item1.getType().getCode().compareToIgnoreCase(item2.getType().getCode()))
		                      .forEach(item -> {
			                      final Listitem listitem = createListitem(item);
			                      addListitemEvent(listitem);
			                      integrationObjectItemListbox.appendChild(listitem);
		                      });
	}

	private void addListitemEvent(final Listitem listitem)
	{
		listitem.addEventListener(Events.ON_CLICK, event -> {
			isSearchBarEnabled = true;
			// Once the search bar is used, the PermissionManagementController will reserve the query as a filter and apply it automatically
			// to matrix of any IOItems. That means the filter will be auto applied if the user select a different IOItem.
			// User have to reset search bar manually.
			sendOutput("sendClickedItem", listitem.getValue());
		});
	}

	private void clearPermissionMatrix()
	{
		isSearchBarEnabled = false;
		this.sendOutput("principalPermissionInfos", null);
	}

	@SocketEvent(socketId = "queryFromSearchBar")
	public void getSearchQuery(String query)
	{
		if(isSearchBarEnabled)
		{
			this.sendOutput("queryToPermissionManager", query);
		}
	}
}
