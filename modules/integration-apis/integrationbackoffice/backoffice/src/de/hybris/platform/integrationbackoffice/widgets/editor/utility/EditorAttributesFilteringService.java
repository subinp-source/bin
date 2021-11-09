/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.editor.utility;

import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;

import java.util.Set;

/**
 * Interface for the attribute filtering service
 */

public interface EditorAttributesFilteringService
{
	/**
	 * Filters a set of attribute descriptors for Composed and Enumeration types that are not on the blacklist
	 *
	 * @param parentType the parent ComposedTypeModel to get the attribute descriptors to filter
	 * @return a filtered set of AttributeDescriptorModel
	 */
	Set<AttributeDescriptorModel> filterAttributesForTree(final ComposedTypeModel parentType);

	/**
	 * Filters a set of attribute descriptors for types that are not on the blacklist
	 *
	 * @param parentType the parent ComposedTypeModel to get the attribute descriptors to filter
	 * @return a filtered and sorted set of AttributeDescriptorModel
	 */
	Set<AttributeDescriptorModel> filterAttributesForAttributesMap(final ComposedTypeModel parentType);
}
