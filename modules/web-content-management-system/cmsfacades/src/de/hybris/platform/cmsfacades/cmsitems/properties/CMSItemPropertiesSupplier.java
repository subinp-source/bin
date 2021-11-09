/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.cmsitems.properties;

import de.hybris.platform.cms2.model.contents.CMSItemModel;

import java.util.Map;
import java.util.function.Predicate;


/**
 * Supplier to provide a {@link Map} of properties for {@link CMSItemModel}.
 */
public interface CMSItemPropertiesSupplier
{
	/**
	 * The {@link Predicate} to test whether this supplier can be applied to provided {@link CMSItemModel}
	 * @return the {@link Predicate}
	 */
	Predicate<CMSItemModel> getConstrainedBy();

	/**
	 * Returns a {@link Map} of properties.
	 * @param itemModel the {@link CMSItemModel}
	 * @return the {@link Map} of properties.
	 */
	Map<String, Object> getProperties(CMSItemModel itemModel);

	/**
	 * Method to provide the properties' group name.
	 * @return the properties' group name.
	 */
	String groupName();

	/**
	 * Method to verify whether properties should be applied or not to {@link CMSItemModel}.
	 * @param itemModel the {@link CMSItemModel}.
	 * @return true if properties should be applied, false otherwise; default is true.
	 */
	default boolean isEnabled(final CMSItemModel itemModel) {
		return true;
	}
}
