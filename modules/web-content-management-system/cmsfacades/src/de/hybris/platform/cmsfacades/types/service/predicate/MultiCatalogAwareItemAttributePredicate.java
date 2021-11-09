/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.types.service.predicate;

import de.hybris.platform.core.model.type.AttributeDescriptorModel;

import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Required;

/**
 * Predicate to test if given attribute is a collection of the provided assignable form
 */
public class MultiCatalogAwareItemAttributePredicate implements Predicate<AttributeDescriptorModel>
{

	private Predicate<AttributeDescriptorModel> isCollectionPredicate;
	private AssignableFromAttributePredicate assignableFromAttributePredicate;

	@Override
	public boolean test(final AttributeDescriptorModel attributeDescriptor)
	{
		return getIsCollectionPredicate().test(attributeDescriptor) && assignableFromAttributePredicate.test(attributeDescriptor);
	}

	protected Predicate<AttributeDescriptorModel> getIsCollectionPredicate()
	{
		return isCollectionPredicate;
	}

	@Required
	public void setIsCollectionPredicate(final Predicate<AttributeDescriptorModel> isCollectionPredicate)
	{
		this.isCollectionPredicate = isCollectionPredicate;
	}

	public AssignableFromAttributePredicate getAssignableFromAttributePredicate()
	{
		return assignableFromAttributePredicate;
	}

	public void setAssignableFromAttributePredicate(final AssignableFromAttributePredicate assignableFromAttributePredicate)
	{
		this.assignableFromAttributePredicate = assignableFromAttributePredicate;
	}

}
