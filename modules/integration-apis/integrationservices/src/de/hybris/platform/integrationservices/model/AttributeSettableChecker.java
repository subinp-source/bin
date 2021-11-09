/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.model;

import de.hybris.platform.core.model.ItemModel;

/**
 * Checks whether an attribute is settable
 */
public interface AttributeSettableChecker
{
	/**
	 * Indicates whether the attribute is settable to the item
	 *
	 * @param item      Item which the attribute is to be set
	 * @param attribute Metadata describing the attribute
	 * @return {@code true} if the attribute is settable to the item, else {@code false}
	 */
	boolean isSettable(ItemModel item, TypeAttributeDescriptor attribute);
}
