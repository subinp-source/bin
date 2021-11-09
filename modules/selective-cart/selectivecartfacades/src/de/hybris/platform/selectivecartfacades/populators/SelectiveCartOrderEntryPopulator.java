/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.selectivecartfacades.populators;

import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.selectivecartservices.enums.CartSourceType;


/**
 * Populates {@link AbstractOrderEntryModel} to {@link OrderEntryData}
 */
public class SelectiveCartOrderEntryPopulator implements Populator<AbstractOrderEntryModel, OrderEntryData>
{
	@Override
	public void populate(final AbstractOrderEntryModel source, final OrderEntryData target)
	{
		if (source.getAddToCartTime() == null)
		{
			target.setAddToCartTime(source.getCreationtime());
		}
		else
		{
			target.setAddToCartTime(source.getAddToCartTime());
		}
		target.setCartSourceType(CartSourceType.STOREFRONT);
	}
}
