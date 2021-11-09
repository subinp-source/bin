/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.types.service;

import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.TypeModel;


/**
 * Helper service to retrieve item types and attribute types information
 */
public interface CMSAttributeTypeService
{
	/**
	 * Retrieves the {@code TypeModel} of the attribute. For primitive/atomic attributes, the attribute's type is
	 * returned. For collection or localized attributes, look into the data structure to inspect the containing type.
	 *
	 * @param attributeDescriptorModel
	 *           - The descriptor that specifies the attribute whose read permission to check.
	 * @return the type of the attribute or the contained type
	 */
	TypeModel getAttributeContainedType(AttributeDescriptorModel attributeDescriptorModel);
}
