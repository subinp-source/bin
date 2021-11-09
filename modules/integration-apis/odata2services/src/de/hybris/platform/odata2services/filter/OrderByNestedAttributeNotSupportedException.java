/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.filter;

import de.hybris.platform.odata2services.odata.OData2ServicesException;

import org.apache.olingo.odata2.api.commons.HttpStatusCodes;
import org.apache.olingo.odata2.api.uri.expression.OrderByExpression;

/**
 * Throws this exception when the {@link OrderByExpression} is not supported
 *
 * Will result in HttpStatus 400
 */
public class OrderByNestedAttributeNotSupportedException extends OData2ServicesException
{
	private static final String NESTED_ATTRIBUTE_MESSAGE_KEY = "order_by_nested_attribute_not_supported";
	private static final String NESTED_ATTRIBUTE_MESSAGE = "Ordering by nested attribute [%s] is not currently supported!";

	/**
	 * Constructor for unsupported order by nested attribute exception
	 *
	 * @param nestedAttribute unsupported nested attribute
	 */
	public OrderByNestedAttributeNotSupportedException(final String nestedAttribute)
	{
		super(String.format(NESTED_ATTRIBUTE_MESSAGE, nestedAttribute), HttpStatusCodes.BAD_REQUEST, NESTED_ATTRIBUTE_MESSAGE_KEY);
	}


}
