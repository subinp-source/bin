/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */

package de.hybris.platform.odata2services.converter;

import de.hybris.platform.odata2services.odata.OData2ServicesException;

import org.apache.olingo.odata2.api.commons.HttpStatusCodes;

/**
 * An exception that indicates that integration object item not found in the system.
 *
 * Will result in HttpStatus 400
 */
public class IntegrationObjectItemNotFoundException extends OData2ServicesException
{
	private final String integrationObjectCode;
	private final String integrationItemType;

	/**
	 * Instantiates this exception
	 * @param obj code of the requested integration object
	 * @param item code of the request integration object item
	 */
	public IntegrationObjectItemNotFoundException(final String obj, final String item)
	{
		super(message(obj, item), HttpStatusCodes.BAD_REQUEST, "not_found");
		integrationObjectCode = obj;
		integrationItemType = item;
	}

	private static String message(final String intObj, final String intItem)
	{
		return String.format("Integration object %s does not contain item type %s", intObj, intItem);
	}

	public String getIntegrationObjectCode()
	{
		return integrationObjectCode;
	}

	public String getIntegrationItemType()
	{
		return integrationItemType;
	}
}
