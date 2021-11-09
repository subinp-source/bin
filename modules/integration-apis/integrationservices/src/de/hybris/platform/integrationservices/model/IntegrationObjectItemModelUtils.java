/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.model;

import java.util.Objects;

/**
 *  Utilities used for IntegrationObjectItemModels
 */
public class IntegrationObjectItemModelUtils
{
	private IntegrationObjectItemModelUtils()
	{
	}

	/**
	 * @param item1 {@link IntegrationObjectItemModel} to compare with item2
	 * @param item2 {@link IntegrationObjectItemModel} to compare with item1
	 * @return boolean indicating if item1 is equal to item2
	 */
	public static boolean isEqual(final IntegrationObjectItemModel item1, final IntegrationObjectItemModel item2)
	{
		if(item1 == null && item2 == null)
		{
			return true;
		}

		if(item1 == null || item2 == null)
		{
			return false;
		}

		return Objects.equals(item1.getIntegrationObject().getCode(), item2.getIntegrationObject().getCode())
				&& Objects.equals(item1.getCode(), item2.getCode());
	}
}
