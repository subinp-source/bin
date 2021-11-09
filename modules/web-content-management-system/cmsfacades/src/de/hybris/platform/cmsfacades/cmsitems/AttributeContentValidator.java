/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.cmsitems;

import de.hybris.platform.cmsfacades.validator.data.ValidationError;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;

import java.util.List;


/**
 * Interface to validate the value of the attributes. 
 * @param <T> type of the object being validated
 */
public interface AttributeContentValidator<T> 
{
	/**
	 * Performs validation on the given arguments.
	 *
	 * @param value the value object 
	 * @param attributeDescriptor the attribute descriptor of the given {@code value}. 
	 */
	List<ValidationError> validate(T value, AttributeDescriptorModel attributeDescriptor);

}
