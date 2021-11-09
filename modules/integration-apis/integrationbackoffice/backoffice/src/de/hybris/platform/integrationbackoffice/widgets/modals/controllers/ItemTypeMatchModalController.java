/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.modals.controllers;

import de.hybris.platform.integrationbackoffice.utility.ItemTypeMatchSelector;
import de.hybris.platform.integrationbackoffice.widgets.modals.data.ItemTypeMatchModal.ItemTypeMatchChangeDetector;
import de.hybris.platform.integrationservices.enums.ItemTypeMatchEnum;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectModel;
import de.hybris.platform.integrationservices.search.ItemTypeMatch;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.zkoss.zk.ui.event.Events;

import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;

/**
 * Used to update the Item Type Match setting of the Integration Object Items of an Integration Object in the Integration UI tool of the backoffice
 */
public final class ItemTypeMatchModalController extends DefaultWidgetController
{

	@Resource
	private transient ItemTypeMatchSelector itemTypeMatchSelector;

	private final static String SELECT_LISTBOX = "select";
	private final static String DROP_DOWN_COLUMN_ID_SUFFIX = "_ddc";
	private final static String DROP_DOWN_BOX_ID_SUFFIX = "_ddb";

	private Listbox itemTypeMatcherListBox;
	private Button saveButton;


	@SocketEvent(socketId = "openItemTypeIOIModal")
	public void loadModal(final IntegrationObjectModel integrationObjectModel)
	{
		saveButton.setVisible(false);
		getItemTypeMatcherListBox().getItems().clear();
		new ArrayList<>(integrationObjectModel.getItems())
				.stream()
				.sorted(Comparator.comparing(IntegrationObjectItemModel::getCode))
				.forEach(this::fillItemTypeMatcherListBox);
	}


	@ViewEvent(componentID = "saveButton", eventName = Events.ON_CLICK)
	public void saveItemTypeMatchSetting()
	{
		final List<IntegrationObjectItemModel> changedList = getChangedIntegrationObjectItems();
		sendOutput("saveButtonItemTypeMatch", changedList);
	}

	private void fillItemTypeMatcherListBox(final IntegrationObjectItemModel integrationObjectItemModel)
	{
		getItemTypeMatcherListBox().getItems().add(getListItem(integrationObjectItemModel));
	}

	private List<IntegrationObjectItemModel> getChangedIntegrationObjectItems()
	{
		final List<IntegrationObjectItemModel> changedList = getItemTypeMatcherListBox().getItems()
		                                                                                .stream()
		                                                                                .map(this::createChangeDetectorForEachRowOfTheModal)
		                                                                                .filter(ItemTypeMatchChangeDetector::isDirty)
		                                                                                .map(ItemTypeMatchChangeDetector::getIntegrationObjectItemModel)
		                                                                                .collect(Collectors.toList());
		return changedList;
	}

	private ItemTypeMatchChangeDetector createChangeDetectorForEachRowOfTheModal(final Listitem aRow)
	{
		final IntegrationObjectItemModel ioiModel = aRow.getValue();
		final String codeToDropDownColumn = getIDForDropDownColumn(ioiModel);
		final String codeToActualDropDownBox = getIDForDropDownBox(ioiModel);

		final Listcell itemTypeMatchDropDownColumn = (Listcell) aRow.getChildren()
		                                                                .stream()
		                                                                .filter(child -> child.getId()
		                                                                                      .equals(codeToDropDownColumn))
		                                                                .findFirst()
		                                                                .orElseThrow();

		final Listbox listboxInsideDropDownCol = (Listbox) itemTypeMatchDropDownColumn.getChildren()
		                                                                              .stream()
		                                                                              .filter(child -> child.getId()
		                                                                                                    .equals(codeToActualDropDownBox))
		                                                                              .findFirst()
		                                                                              .orElseThrow();
		final String itemTypeMatch = listboxInsideDropDownCol.getSelectedItem().getLabel();
		return new ItemTypeMatchChangeDetector(ioiModel, itemTypeMatch);

	}


	private Listitem getListItem(final IntegrationObjectItemModel integrationObjectItemModel)
	{
		final Listitem aRow = new Listitem();
		final Listcell ioiNameColumn = new Listcell(integrationObjectItemModel.getCode());
		final Listcell itemTypeMatchDropDownColumn = createItemTypeMatchDropDown(integrationObjectItemModel);
		itemTypeMatchDropDownColumn.setId(getIDForDropDownColumn(integrationObjectItemModel));
		aRow.appendChild(ioiNameColumn);
		aRow.appendChild(itemTypeMatchDropDownColumn);
		aRow.setValue(integrationObjectItemModel);
		return aRow;
	}


	private Listcell createItemTypeMatchDropDown(final IntegrationObjectItemModel integrationObjectItemModel)
	{
		final Listcell dropDown = new Listcell();
		final Listbox listBox = new Listbox();
		listBox.setMold(SELECT_LISTBOX);
		listBox.addEventListener(Events.ON_SELECT, event -> saveButton.setVisible(true));
		listBox.setId(getIDForDropDownBox(integrationObjectItemModel));
		final ItemTypeMatch itemTypeMatchToSelect = itemTypeMatchSelector.getToSelectItemTypeMatch(integrationObjectItemModel);
		final Collection<Listitem> itemTypeMatchEnumDropDownValues = integrationObjectItemModel.getAllowedItemTypeMatches()
		                                                                                       .stream()
		                                                                                       .map(allowedItemTypeMatch ->
				                                                                                       createDropDownOption(allowedItemTypeMatch,itemTypeMatchToSelect)
		                                                                                            )
		                                                                                       .collect(Collectors.toList());
		listBox.getItems().addAll(itemTypeMatchEnumDropDownValues);
		dropDown.appendChild(listBox);
		return dropDown;

	}

	private Listitem createDropDownOption(final ItemTypeMatchEnum allowedItemTypeMatch, final ItemTypeMatch itemTypeMatchToSelect)
	{
		final Listitem temp = new Listitem(allowedItemTypeMatch.getCode());
		final String valueOfAllowedItemTypeMatch = ItemTypeMatch.valueOf(allowedItemTypeMatch.getCode()).getValue();
		if (itemTypeMatchToSelect.getValue().equals(valueOfAllowedItemTypeMatch))
		{
			temp.setSelected(true);
		}
		return temp;

	}

	private Listbox getItemTypeMatcherListBox()
	{
		return itemTypeMatcherListBox;
	}

	public void setItemTypeMatchSelector(final ItemTypeMatchSelector itemTypeMatchSelector)
	{
		this.itemTypeMatchSelector = itemTypeMatchSelector;
	}

	private String getIDForDropDownColumn(final IntegrationObjectItemModel integrationObjectItemModel)
	{
		return integrationObjectItemModel.getPk().getHex() + DROP_DOWN_COLUMN_ID_SUFFIX;
	}

	private String getIDForDropDownBox(final IntegrationObjectItemModel integrationObjectItemModel)
	{
		return integrationObjectItemModel.getPk().getHex() + DROP_DOWN_BOX_ID_SUFFIX;
	}

}