/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.common.predicate;

import de.hybris.platform.cms2.servicelayer.services.AttributeDescriptorModelHelperService;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;

import java.lang.reflect.ParameterizedType;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Required;

import static org.apache.commons.lang3.ClassUtils.primitiveToWrapper;


/**
 * Predicate to identify if an attribute type is of a given type or a {@link ParameterizedType} containing the given type
 */
public class DefaultClassTypeAttributePredicate implements Predicate<AttributeDescriptorModel>
{

	private AttributeDescriptorModelHelperService attributeDescriptorModelHelperService;
	private Class typeClass;

	@Override
	public boolean test(final AttributeDescriptorModel attributeDescriptor)
	{
		Class<?> type = getAttributeDescriptorModelHelperService().getAttributeClass(attributeDescriptor);
		return primitiveToWrapper(type).equals(primitiveToWrapper(getTypeClass()));
	}

	public Class getTypeClass()
	{
		return typeClass;
	}

	@Required
	public void setTypeClass(final Class typeClass)
	{
		this.typeClass = typeClass;
	}

	protected AttributeDescriptorModelHelperService getAttributeDescriptorModelHelperService()
	{
		return attributeDescriptorModelHelperService;
	}

	@Required
	public void setAttributeDescriptorModelHelperService(
			AttributeDescriptorModelHelperService attributeDescriptorModelHelperService)
	{
		this.attributeDescriptorModelHelperService = attributeDescriptorModelHelperService;
	}

}
