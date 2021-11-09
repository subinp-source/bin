/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.filter;

import de.hybris.platform.odata2services.odata.OData2ServicesException;

import org.apache.olingo.odata2.api.commons.HttpStatusCodes;
import org.apache.olingo.odata2.api.uri.expression.OrderByExpression;

/**
 * Throws this exception when the {@link OrderByExpression}  contains integrationKey as an order by attribute.
 *
 * Will result in HttpStatus 400
 */
public class OrderByIntegrationKeyNotSupportedException extends OData2ServicesException
{
	private static final String INTEGRATION_KEY_MESSAGE_KEY = "order_by_integration_key_not_supported";
	private static final String INTEGRATION_KEY_MESSAGE = "Ordering by integrationKey is not supported!";

	/**
	 * Constructor for unsupported order by integrationKey exception
	 */
	public OrderByIntegrationKeyNotSupportedException()
	{
		super(INTEGRATION_KEY_MESSAGE, HttpStatusCodes.BAD_REQUEST, INTEGRATION_KEY_MESSAGE_KEY);
	}

}
