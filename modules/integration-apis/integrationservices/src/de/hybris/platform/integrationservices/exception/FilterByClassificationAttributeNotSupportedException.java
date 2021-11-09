/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.exception;

import de.hybris.platform.integrationservices.model.IntegrationObjectItemClassificationAttributeModel;
import de.hybris.platform.integrationservices.search.FilterNotSupportedException;

/**
 * An exception indicating that filtering using a classification attribute is not supported.
 */
public class FilterByClassificationAttributeNotSupportedException extends FilterNotSupportedException
{
	private static final String MSG_TEMPLATE = "Filtering by classification attribute %s is not supported.";
	private final IntegrationObjectItemClassificationAttributeModel attribute;

	/**
	 * Instantiates the exception
	 * @param attr The classification attribute used in a search filter condition.
	 */
	public FilterByClassificationAttributeNotSupportedException(final IntegrationObjectItemClassificationAttributeModel attr)
	{
		super(String.format(MSG_TEMPLATE, attr.getAttributeName()));
		attribute = attr;
	}

	/**
	 * Retrieves definition of the attribute that was attempted to be used in the filter condition.
	 *
	 * @return classification attribute that was attempted to be used in the filter condition; or more
	 * accurately, the attribute model passed into the constructor.
	 */
	public IntegrationObjectItemClassificationAttributeModel getAttribute()
	{
		return attribute;
	}
}