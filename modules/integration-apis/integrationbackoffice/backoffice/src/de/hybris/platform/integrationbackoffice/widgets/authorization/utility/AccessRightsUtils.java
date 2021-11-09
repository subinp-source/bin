/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.authorization.utility;

import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel;

import org.zkoss.zul.Listitem;


public final class AccessRightsUtils
{
	private AccessRightsUtils()
	{
		throw new IllegalStateException("Utility class");
	}


	/**
	 * Creates a listitem
	 *
	 * @param item IntegrationObjectItem that the listitem will be created for
	 * @return Listitem
	 */
	public static Listitem createListitem(final IntegrationObjectItemModel item)
	{
		return new Listitem(item.getType().getCode(), item.getType());
	}
}
