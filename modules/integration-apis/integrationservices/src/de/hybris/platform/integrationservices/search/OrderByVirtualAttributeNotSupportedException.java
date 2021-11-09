/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.search;

import de.hybris.platform.integrationservices.model.IntegrationObjectItemVirtualAttributeModel;

/**
 * An exception indicating that ordering using a virtual attribute is not supported.
 */
public class OrderByVirtualAttributeNotSupportedException extends OrderByNotSupportedException
{
	private static final String MSG_TEMPLATE = "Ordering by virtual attribute %s is not supported.";
	private final IntegrationObjectItemVirtualAttributeModel attribute;

	/**
	 * Instantiates the exception
	 * @param attr definition of the virtual attribute that was used in the "order by" clause for item search.
	 */
	public OrderByVirtualAttributeNotSupportedException(final IntegrationObjectItemVirtualAttributeModel attr)
	{
		super(String.format(MSG_TEMPLATE, attr.getAttributeName()));
		attribute = attr;
	}

	/**
	 * Retrieves definition of the attribute that was attempted to be used in the "order by" clause.
	 *
	 * @return virtual attribute that was attempted to be used in the "order by" clause; or more
	 * technically, the attribute model passed into the constructor.
	 */
	public IntegrationObjectItemVirtualAttributeModel getAttribute()
	{
		return attribute;
	}
}