/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.configurablebundlefacades.order.converters.populator;

import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.core.model.order.OrderModel;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;


/**
 * Bundling specific converter implementation for {@link OrderModel} as source and {@link OrderData} as target type.
 * 
 * @deprecated since 1905: The comparator compares only deprecated fields, so it is deprecated, too.
 */
@Deprecated(since = "1905", forRemoval = true)
public class BundleOrderPopulator<S extends OrderModel, T extends OrderData> extends AbstractBundleOrderPopulator<S, T>
{
	@Override
	public void populate(final S source, final T target)
	{
		validateParameterNotNullStandardMessage("source", source);
		validateParameterNotNullStandardMessage("target", target);

	}
}
