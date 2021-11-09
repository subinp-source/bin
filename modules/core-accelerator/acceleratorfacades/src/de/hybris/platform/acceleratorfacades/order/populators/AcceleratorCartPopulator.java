/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorfacades.order.populators;

import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.CartModel;


/**
 * Converter implementation for {@link de.hybris.platform.core.model.order.CartModel} as source and
 * {@link de.hybris.platform.commercefacades.order.data.CartData} as target type.
 */
public class AcceleratorCartPopulator<T extends CartData> implements Populator<CartModel, T>
{

	@Override
	public void populate(final CartModel source, final T target)
	{
		target.setImportStatus(source.getImportStatus());
	}

}
