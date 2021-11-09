/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.odata2services.filter;

import de.hybris.platform.integrationservices.search.FilterNotSupportedException;

/**
 * Throw this exception if nested attribute: someObject/IntegrationKey filter is used with logical operators other than 'eq'.
 * The integration key is the unique identifier of an item. It is used to locate a particular item.
 * Therefore, only the eq operator is applicable to the integration key.
 */
public class IntegrationKeyNestedFilteringNotSupportedException extends FilterNotSupportedException
{
	/**
	 * Constructor to create IntegrationKeyNestedFilteringNotSupported Exception
	 */
	public IntegrationKeyNestedFilteringNotSupportedException()
	{
		super("For nested attribute filtering at someObject/IntegrationKey, logical operators other than 'eq' are not supported.");
	}
}
