/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.utility;

import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel;
import de.hybris.platform.integrationservices.search.ItemTypeMatch;

import javax.validation.constraints.NotNull;

/**
 *  It is used to get an ItemTypeMatch for an IntegrationObjectItemModel
 */
public interface ItemTypeMatchSelector
{
	/**
	 * Receives an IntegrationObjectItemModel and returns an ItemTypeMatch associated with it or a default one
	 * based on its type.
	 *
	 * @param integrationObjectItemModel An Integration Object Item Model
	 * @return ItemTypeMatch for the integrationObjectItemModel
	 */
	ItemTypeMatch getToSelectItemTypeMatch(@NotNull final IntegrationObjectItemModel integrationObjectItemModel);
}
