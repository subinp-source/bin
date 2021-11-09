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
 * Throw this exception if filtering by more than one level of nesting (e.g. catalogVersion/catalog/id eq 'Default')
 *
 * Will result in HttpStatus 400
 */
public class NestedFilterNotSupportedException extends InvalidLookupDataException
{
	private static final String MESSAGE = "Nested filter [%s] of more than one level is not supported";

	/**
	 * Constructor to create NestedFilterNotSupportedException
	 * 
	 * @param filter invalid filter expression
	 */
	public NestedFilterNotSupportedException(final String filter)
	{
		super(String.format(MESSAGE, filter), "filter_not_supported");
	}
}
