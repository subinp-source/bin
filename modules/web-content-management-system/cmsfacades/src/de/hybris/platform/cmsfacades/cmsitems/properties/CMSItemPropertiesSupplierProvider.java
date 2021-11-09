/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.cmsitems.properties;

import de.hybris.platform.cms2.model.contents.CMSItemModel;

import java.util.List;


/**
 * Provider that returns the list of {@link CMSItemPropertiesSupplier} that is
 * used to provide properties for {@link CMSItemModel}.
 */
public interface CMSItemPropertiesSupplierProvider
{
	/**
	 * Returns the list of {@link CMSItemPropertiesSupplier} for provided {@link CMSItemModel}.
	 * @param itemModel the {@link CMSItemModel}.
	 * @return the list of {@link CMSItemPropertiesSupplier}.
	 */
	List<CMSItemPropertiesSupplier> getSuppliers(CMSItemModel itemModel);
}