/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.types.service.predicate;

import de.hybris.platform.core.model.type.AttributeDescriptorModel;

import java.util.function.Predicate;

/**
 * Predicate to test if a given attribute {@link AttributeDescriptorModel} is localized.  
 */
public class LocalizedStructureTypeAttributePredicate implements Predicate<AttributeDescriptorModel>
{
	@Override
	public boolean test(final AttributeDescriptorModel attributeDescriptor)
	{
		return attributeDescriptor.getLocalized();
	}

}
