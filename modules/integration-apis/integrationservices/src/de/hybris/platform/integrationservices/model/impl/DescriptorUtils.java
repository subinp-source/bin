/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.model.impl;

import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel;
import de.hybris.platform.integrationservices.model.TypeDescriptor;

import java.util.Optional;

/**
 * Utilities for manipulating the Descriptor classes in this package.
 */
public class DescriptorUtils
{
	private DescriptorUtils()
	{
		// non-instantiable class
	}

	/**
	 * Retrieves item model corresponding to the specified type descriptor.
	 * @param type a type descriptor to extract the item model from.
	 * @return an {@code Optional} with the item model described by the specified {@code TypeDescriptor} or {@code Optional.empty()},
	 * if the specified type descriptor is {@code null} or describes a primitive type.
	 */
	public static Optional<IntegrationObjectItemModel> extractModelFrom(final TypeDescriptor type)
	{
		return Optional.ofNullable(type)
				.filter(ItemTypeDescriptor.class::isInstance)
				.map(ItemTypeDescriptor.class::cast)
				.map(ItemTypeDescriptor::getItemTypeModel);
	}
}
