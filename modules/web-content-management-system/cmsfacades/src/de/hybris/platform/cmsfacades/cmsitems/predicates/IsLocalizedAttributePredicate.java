/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.cmsitems.predicates;

import de.hybris.platform.core.model.type.AttributeDescriptorModel;

import java.util.function.Predicate;

/**
 * Returns true if this attribute is localized as per our platform type system.
 */
public class IsLocalizedAttributePredicate implements Predicate<AttributeDescriptorModel>
{
	@Override
	public boolean test(final AttributeDescriptorModel attribute)
	{
		return attribute.getLocalized();
	}
}
