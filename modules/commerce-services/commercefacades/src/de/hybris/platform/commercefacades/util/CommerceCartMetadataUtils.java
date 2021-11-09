/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.util;

import de.hybris.platform.commercefacades.util.builder.CommerceCartMetadataBuilder;


/**
 * Utility class for commerce cart metadata bean.
 */
public final class CommerceCartMetadataUtils
{
	private CommerceCartMetadataUtils()
	{
		throw new IllegalAccessError("Utility class may not be instantiated");
	}

	/**
	 * Creates a new {@link CommerceCartMetadataBuilder} instance.
	 *
	 * @return the new {@link CommerceCartMetadataBuilder}
	 */
	public static CommerceCartMetadataBuilder metadataBuilder()
	{
		return new CommerceCartMetadataBuilder();
	}
}
