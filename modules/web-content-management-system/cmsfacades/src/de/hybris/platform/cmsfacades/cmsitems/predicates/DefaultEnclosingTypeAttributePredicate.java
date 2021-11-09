/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.cmsitems.predicates;

import de.hybris.platform.core.model.type.AttributeDescriptorModel;

import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Required;


/**
 * Predicate to identify if an attribute is a content of an enclosing type.  
 */
public class DefaultEnclosingTypeAttributePredicate implements Predicate<AttributeDescriptorModel>
{

	private String typeCode;

	@Override
	public boolean test(final AttributeDescriptorModel attributeDescriptor)
	{
		return attributeDescriptor.getEnclosingType().getCode().equals(typeCode);
	}

	protected String getTypeCode()
	{
		return typeCode;
	}

	@Required
	public void setTypeCode(final String typeCode)
	{
		this.typeCode = typeCode;
	}
}
