/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.order.converters.populator;

import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.core.model.order.CartModel;

import org.springframework.util.Assert;


public class MiniCartPopulator extends AbstractOrderPopulator<CartModel, CartData>
{

	@Override
	public void populate(final CartModel source, final CartData target)
	{
		Assert.notNull(target, "Parameter target cannot be null.");

		if (source == null)
		{
			target.setTotalPrice(createZeroPrice());
			target.setDeliveryCost(null);
			target.setSubTotal(createZeroPrice());
			target.setTotalItems(Integer.valueOf(0));
			target.setTotalUnitCount(Integer.valueOf(0));
		}
		else
		{
			addCommon(source, target);
			addTotals(source, target);

			target.setTotalUnitCount(calcTotalUnitCount(source));
		}
	}
}
