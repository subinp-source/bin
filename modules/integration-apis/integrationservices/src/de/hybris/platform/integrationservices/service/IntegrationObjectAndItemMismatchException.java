/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.service;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectDescriptor;

/**
 * Indicates that a data item does not match the specified integration object model. This happens for example, when
 * the data item needs to be converted to or from the integration object payload.
 */
public class IntegrationObjectAndItemMismatchException extends RuntimeException
{
	private final transient ItemModel dataItem;
	private final transient IntegrationObjectDescriptor integrationObject;

	public IntegrationObjectAndItemMismatchException(final ItemModel item, final IntegrationObjectDescriptor io)
	{
		super("Item " + item + " is not present in " + io);
		dataItem = item;
		integrationObject = io;
	}

	public ItemModel getDataItem()
	{
		return dataItem;
	}

	public IntegrationObjectDescriptor getIntegrationObject()
	{
		return integrationObject;
	}
}
