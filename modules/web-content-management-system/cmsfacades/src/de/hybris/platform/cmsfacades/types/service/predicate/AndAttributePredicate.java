/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.types.service.predicate;

import de.hybris.platform.core.model.type.AttributeDescriptorModel;

import java.util.List;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Required;

/**
 * Predicate to test that a list of {@link Predicate<AttributeDescriptorModel>} are matched
 */
public class AndAttributePredicate implements Predicate<AttributeDescriptorModel>
{

	private List<Predicate<AttributeDescriptorModel>> predicates;

	@Override
	public boolean test(final AttributeDescriptorModel attributeDescriptor)
	{
		//check that there is NO predicate that returns false
		return getPredicates().stream().map(predicate -> predicate.test(attributeDescriptor)).allMatch(isSuccessful -> isSuccessful == true);
	}

 
	protected List<Predicate<AttributeDescriptorModel>> getPredicates()
	{
		return predicates;
	}
	
	@Required
	public void setPredicates(List<Predicate<AttributeDescriptorModel>> predicates)
	{
		this.predicates = predicates;
	}
}
