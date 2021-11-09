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
package de.hybris.platform.odata2services.odata;

import org.apache.olingo.odata2.api.commons.HttpStatusCodes;

public class InvalidServiceNameException extends OData2ServicesException
{
	/**
	 * Constructor to create InvalidServiceNameException
	 */
	public InvalidServiceNameException()
	{
		super("The service requested is invalid.", HttpStatusCodes.BAD_REQUEST, "invalid_service");
	}
}
