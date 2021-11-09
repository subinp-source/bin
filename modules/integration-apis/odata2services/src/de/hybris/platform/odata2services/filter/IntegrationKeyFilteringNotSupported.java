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
package de.hybris.platform.odata2services.filter;

import de.hybris.platform.odata2services.odata.persistence.lookup.InvalidLookupDataException;

/**
 * Throw this exception if filtering at the integration object level.
 * The query could be made with using OData notation instead of the $filter query option.
 *
 * Will result in HttpStatus 400
 */
public class IntegrationKeyFilteringNotSupported extends InvalidLookupDataException
{
	/**
	 * Constructor to create IntegrationKeyFilteringNotSupported Exception
	 */
	public IntegrationKeyFilteringNotSupported()
	{
		super("Filtering by integration key is not supported. Please supply the key with the Integration Object (e.g. https://.../InboundProduct/Products('Staged|Default|MyProduct'))",
				"integration_key_not_supported");
	}
}
