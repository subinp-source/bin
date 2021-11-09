/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.modals.data.ItemTypeMatchModal;

import de.hybris.platform.integrationservices.enums.ItemTypeMatchEnum;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel;

/**
 * Represents one of the row of the ItemTypeMatchModal after the save button is clicked. It is used to
 * check if the ItemTypeMatch value is changed for the IntegrationObjectItemModel and needs to be saved
 * to the database.
 */
public class ItemTypeMatchChangeDetector
{
	private final IntegrationObjectItemModel integrationObjectItemModel;
	private boolean isDirty = false;

	public ItemTypeMatchChangeDetector(final IntegrationObjectItemModel integrationObjectItemModel,
	                                   final String selectedItemTypeMatchCode)
	{
		this.integrationObjectItemModel = integrationObjectItemModel;
		setItemTypeMatchEnumByCode(selectedItemTypeMatchCode);
	}

	private void setItemTypeMatchEnumByCode(final String selectedItemTypeMatchCode)
	{
		final ItemTypeMatchEnum itemTypeMatchEnum = ItemTypeMatchEnum.valueOf(selectedItemTypeMatchCode);
		final ItemTypeMatchEnum itemTypeMatchEnumIOI = integrationObjectItemModel.getItemTypeMatch();
		if (itemTypeMatchEnumIOI == null || !itemTypeMatchEnumIOI.equals(itemTypeMatchEnum))
		{
			isDirty = true;
			integrationObjectItemModel.setItemTypeMatch(itemTypeMatchEnum);
		}
	}

	public IntegrationObjectItemModel getIntegrationObjectItemModel()
	{
		return integrationObjectItemModel;
	}

	public boolean isDirty()
	{
		return isDirty;
	}
}
