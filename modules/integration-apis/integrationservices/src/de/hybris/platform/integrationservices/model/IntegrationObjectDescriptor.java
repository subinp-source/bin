/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.model;

import de.hybris.platform.core.model.ItemModel;

import java.util.Optional;
import java.util.Set;

/**
 * Describes {@code IntegrationObject}.
 */
public interface IntegrationObjectDescriptor
{
	/**
	 * Reads integration object code.
	 * @return a code identifying the {@code IntegrationObject}.
	 */
	String getCode();

	/**
	 * Retrieves descriptors for all integration object items defined in this integration object.
	 * @return a set of all non-primitive item type descriptors defined in this integration object or an empty set, if this
	 * integration object does not have a single integration object item defined yet.
	 */
	Set<TypeDescriptor> getItemTypeDescriptors();

	/**
	 * Retrieves item type defined in this integration object, corresponding to the specified data item.
	 * @param item an item to find a type descriptor for.
	 * @return a descriptor for the item type corresponding to the specified item or an empty value, if the specified item is not
	 * related to this integration object.
	 */
	Optional<TypeDescriptor> getItemTypeDescriptor(ItemModel item);

	/**
	 * Retrieves root item type in this integration object.
	 * @return a descriptor for the root item type in this {@code IntegrationObject} or an empty value, if root item type is not
	 * defined in this object.
	 */
	Optional<TypeDescriptor> getRootItemType();
}
