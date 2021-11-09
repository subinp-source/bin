/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.util;

import de.hybris.platform.commerceservices.util.builder.CommerceCartMetadataParameterBuilder;


/**
 * Utility class for commerce cart metadata parameter bean.
 */
public final class CommerceCartMetadataParameterUtils
{
	private CommerceCartMetadataParameterUtils()
	{
		throw new IllegalAccessError("Utility class may not be instantiated");
	}

	/**
	 * Creates a new {@link CommerceCartMetadataParameterBuilder} instance.
	 *
	 * @return the new {@link CommerceCartMetadataParameterBuilder}
	 */
	public static CommerceCartMetadataParameterBuilder parameterBuilder()
	{
		return new CommerceCartMetadataParameterBuilder();
	}
}
