/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.types.service.predicate;

import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.validation.model.constraints.NotEmptyConstraintModel;
import de.hybris.platform.validation.model.constraints.jsr303.NotNullConstraintModel;

import java.util.HashSet;
import java.util.function.Predicate;

import com.google.common.collect.Sets;

/**
 * Predicate to test if a given attribute {@link AttributeDescriptorModel} is required.  
 */
public class RequiredStructureTypeAttributePredicate implements Predicate<AttributeDescriptorModel>
{
	@Override
	public boolean test(final AttributeDescriptorModel attributeDescriptor)
	{
		final HashSet<String> constraints = Sets.newHashSet(NotNullConstraintModel._TYPECODE, NotEmptyConstraintModel._TYPECODE);
		
		return attributeDescriptor.getConstraints() //
				.stream() //
				.anyMatch(attributeConstraintModel -> constraints.contains(attributeConstraintModel.getItemtype()));
	}

}
