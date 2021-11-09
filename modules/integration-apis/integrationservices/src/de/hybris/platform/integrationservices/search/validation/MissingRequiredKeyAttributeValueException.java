/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.search.validation;

import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;
import de.hybris.platform.integrationservices.search.ItemSearchRequest;

/**
 * Indicates that a required key attribute is not specified or has null value for searching an item by its
 * key.
 */
public class MissingRequiredKeyAttributeValueException extends ItemSearchRequestValidationException
{
	private static final String MESSAGE_TEMPLATE = "Required key attribute '%s' in item of '%s' type has no value";
	private final transient TypeAttributeDescriptor violatedAttribute;

	/**
	 * Instantiates this exception
	 *
	 * @param request   request rejected due to the validation failure.
	 * @param attribute required key attribute that had no value in the request.
	 */
	public MissingRequiredKeyAttributeValueException(final ItemSearchRequest request, final TypeAttributeDescriptor attribute)
	{
		super(request, message(attribute));
		violatedAttribute = attribute;
	}

	/**
	 * Retrieves attribute that failed validation
	 *
	 * @return a required key attribute that has no value in the request
	 */
	public TypeAttributeDescriptor getViolatedAttribute()
	{
		return violatedAttribute;
	}

	private static String message(final TypeAttributeDescriptor descriptor)
	{
		return String.format(MESSAGE_TEMPLATE, descriptor.getAttributeName(), descriptor.getTypeDescriptor().getItemCode());
	}
}
