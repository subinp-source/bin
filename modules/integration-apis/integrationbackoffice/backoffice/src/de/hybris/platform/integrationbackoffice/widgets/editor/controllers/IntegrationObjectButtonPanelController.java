/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.editor.controllers;

import com.hybris.cockpitng.annotations.GlobalCockpitEvent;
import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectCRUDHandler;
import com.hybris.cockpitng.util.DefaultWidgetController;

import de.hybris.platform.integrationbackoffice.widgets.editor.data.IntegrationFilterState;
import de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorUtils;
import de.hybris.platform.integrationservices.model.IntegrationObjectModel;

import org.zkoss.zhtml.Button;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Messagebox;

import java.util.ArrayList;
import java.util.List;

public final class IntegrationObjectButtonPanelController extends DefaultWidgetController
{

	private org.zkoss.zul.Button saveDefinitionsButton;
	private Div filterButtonDiv;
	private Button filterButton;

	@Override
	public void initialize(final Component component)
	{
		super.initialize(component);
		saveDefinitionsButton.setDisabled(true);
		filterButton = new Button();
		filterButton.setDisabled(true);
		addFilterMenu();
	}

	@GlobalCockpitEvent(eventName = ObjectCRUDHandler.OBJECT_CREATED_EVENT, scope = CockpitEvent.SESSION)
	public void handleIntegrationObjectCreatedEvent(final CockpitEvent event)
	{
		if (event.getDataAsCollection().stream().anyMatch(IntegrationObjectModel.class::isInstance))
		{
			saveDefinitionsButton.setDisabled(true);
			filterButton.setDisabled(false);
		}
	}

	@GlobalCockpitEvent(eventName = ObjectCRUDHandler.OBJECTS_DELETED_EVENT, scope = CockpitEvent.SESSION)
	public void handleIntegrationObjectDeletedEvent(final CockpitEvent event)
	{
		if (event.getDataAsCollection().stream().anyMatch(IntegrationObjectModel.class::isInstance))
		{
			saveDefinitionsButton.setDisabled(true);
			filterButton.setDisabled(true);
		}
	}

	@SocketEvent(socketId = "receiveIntegrationObjectComboBox")
	public void loadIntegrationObject(final IntegrationObjectModel integrationObjectModel)
	{
		saveDefinitionsButton.setDisabled(true);
		filterButton.setDisabled(false);
	}

	@SocketEvent(socketId = "enableSaveButtonEvent")
	public void enableSaveButton(final boolean isEnabled)
	{
		saveDefinitionsButton.setDisabled(!isEnabled);
	}

	@SocketEvent(socketId = "filterStateInput")
	public void updateFilterState(IntegrationFilterState state)
	{
		final Menupopup menu = (Menupopup) filterButton.getFirstChild();
		if (state == IntegrationFilterState.SHOW_ALL)
		{
			((Menuitem) menu.getFirstChild()).setChecked(true);
			((Menuitem) menu.getLastChild()).setChecked(false);
		}
		else
		{
			((Menuitem) menu.getFirstChild()).setChecked(false);
			((Menuitem) menu.getLastChild()).setChecked(true);
		}
	}

	@ViewEvent(componentID = "saveDefinitionsButton", eventName = Events.ON_CLICK)
	public void saveDefinitionsOnClick()
	{
		sendOutput("saveButtonClick", "");
	}

	@ViewEvent(componentID = "refreshButton", eventName = Events.ON_CLICK)
	public void refreshButtonOnClick()
	{
		if (!saveDefinitionsButton.isDisabled())
		{
			Messagebox.show(getLabel("integrationbackoffice.buttonPanel.warning.msg.refreshConfirmation"),
					getLabel("integrationbackoffice.buttonPanel.warning.title.refreshConfirmation"),
					new Messagebox.Button[]{ Messagebox.Button.YES, Messagebox.Button.NO },
					null, null, null, clickEvent -> {
						if (clickEvent.getButton() == Messagebox.Button.YES)
						{
							saveDefinitionsButton.setDisabled(true);
							sendOutput("refreshButtonClick", "");
						}
					});
		}
		else
		{
			saveDefinitionsButton.setDisabled(true);
			sendOutput("refreshButtonClick", "");
		}
	}

	private void addFilterMenu()
	{
		final List<String> labels = new ArrayList<>();
		labels.add(getLabel("integrationbackoffice.editMode.menuItem.showAll"));
		labels.add(getLabel("integrationbackoffice.editMode.menuItem.preview"));

		final Menupopup filterMenu = EditorUtils.createMenuPopup(labels);
		filterMenu.setSclass("yw-integrationbackoffice-filterMenu");
		filterButton.setSclass("yw-integrationbackoffice-filter-btn");

		filterButton.appendChild(filterMenu);
		filterButtonDiv.appendChild(filterButton);

		final Menuitem showAllItem = (Menuitem) filterButton.getFirstChild().getFirstChild();
		final Menuitem previewItem = (Menuitem) filterButton.getFirstChild().getFirstChild().getNextSibling();

		showAllItem.setCheckmark(true);
		previewItem.setCheckmark(true);

		showAllItem.setChecked(true);

		addMenuItemEvents(showAllItem, previewItem);
		filterButton.addEventListener(Events.ON_CLICK, event -> {
			filterMenu.open(filterButton);
		});

	}

	private void addMenuItemEvents(final Menuitem showAllItem, final Menuitem previewItem)
	{
		showAllItem.addEventListener(Events.ON_CLICK, event -> {
			if (!showAllItem.isChecked())
			{
				showAllItem.setChecked(true);
				previewItem.setChecked(false);
				sendOutput("filterStateOutput", IntegrationFilterState.SHOW_ALL);
			}
		});
		previewItem.addEventListener(Events.ON_CLICK, event -> {
			if (!previewItem.isChecked())
			{
				previewItem.setChecked(true);
				showAllItem.setChecked(false);
				sendOutput("filterStateOutput", IntegrationFilterState.SELECTED);
			}
		});
	}

}
