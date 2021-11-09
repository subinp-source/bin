/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.cmsitems;

import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.converters.Populator;

import java.util.List;
import java.util.Map;


/**
 * Interface to provide a list of custom {@link Populator} populators for a given {@link CMSItemModel}.
 */
public interface ItemDataPopulatorProvider
{
	/**
	 * Interface method to return a list of {@link Populator} populators given the {@link CMSItemModel}.
	 * @param itemModel the CMSItemModel
	 * @return the list of Populators registered for the {@link CMSItemModel}.
	 */
	List<Populator<CMSItemModel, Map<String, Object>>> getItemDataPopulators(final CMSItemModel itemModel);
}
