/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.search;

import de.hybris.platform.integrationservices.model.IntegrationObjectItemVirtualAttributeModel;

/**
 * An exception indicating that filtering using a virtual attribute is not supported.
 */
public class FilterByVirtualAttributeNotSupportedException extends FilterNotSupportedException
{
	private static final String MSG_TEMPLATE = "Filtering by virtual attribute %s is not supported.";
	private final IntegrationObjectItemVirtualAttributeModel attribute;

	/**
	 * Instantiates the exception
	 * @param attr The virtual attribute value that is not supported.
	 */
	public FilterByVirtualAttributeNotSupportedException(final IntegrationObjectItemVirtualAttributeModel attr)
	{
		super(String.format(MSG_TEMPLATE, attr.getAttributeName()));
		attribute = attr;
	}

	/**
	 * Retrieves definition of the attribute that was attempted to be used in the filter condition.
	 *
	 * @return virtual attribute that was attempted to be used in the filter condition; or more
	 * accurately, the attribute model passed into the constructor.
	 */
	public IntegrationObjectItemVirtualAttributeModel getAttribute()
	{
		return attribute;
	}
}
