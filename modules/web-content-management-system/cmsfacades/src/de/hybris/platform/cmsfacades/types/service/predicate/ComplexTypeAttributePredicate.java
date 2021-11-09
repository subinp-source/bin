/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.types.service.predicate;

import de.hybris.platform.cms2.servicelayer.services.AttributeDescriptorModelHelperService;
import de.hybris.platform.cmsfacades.common.predicate.attributes.EnumTypeAttributePredicate;
import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;

import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Required;


/**
 * Predicate that returns true if the {@link AttributeDescriptorModel} is a complex type, i.e. assignable from {@link ItemModel},
 * a native java {@link Enum} or a SAP Hybris {@link HybrisEnumValue}.
 */
public class ComplexTypeAttributePredicate implements Predicate<AttributeDescriptorModel>
{
	private AttributeDescriptorModelHelperService attributeDescriptorModelHelperService;
	private EnumTypeAttributePredicate isEnumPredicate;

	@Override
	public boolean test(final AttributeDescriptorModel attributeDescriptor)
	{

		final Predicate<Class<?>> extendsItemModel = attributeClass -> ItemModel.class.isAssignableFrom(attributeClass);

		Class<?> attributeClass = getAttributeDescriptorModelHelperService().getAttributeClass(attributeDescriptor);

		return extendsItemModel.test(attributeClass) || getIsEnumPredicate().test(attributeDescriptor);
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

	protected EnumTypeAttributePredicate getIsEnumPredicate()
	{
		return isEnumPredicate;
	}

	public void setIsEnumPredicate(EnumTypeAttributePredicate isEnumPredicate)
	{
		this.isEnumPredicate = isEnumPredicate;
	}
}
