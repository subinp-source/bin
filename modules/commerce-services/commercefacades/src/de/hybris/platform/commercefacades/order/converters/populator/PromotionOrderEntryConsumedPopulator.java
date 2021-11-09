/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.order.converters.populator;

import de.hybris.platform.commercefacades.order.data.PromotionOrderEntryConsumedData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.promotions.model.PromotionOrderEntryConsumedModel;

import org.springframework.util.Assert;


/**
 * Converter implementation for {@link de.hybris.platform.core.model.order.OrderModel} as source and
 * {@link de.hybris.platform.commercefacades.order.data.OrderHistoryData} as target type.
 *
 */
public class PromotionOrderEntryConsumedPopulator implements
		Populator<PromotionOrderEntryConsumedModel, PromotionOrderEntryConsumedData>
{

	@Override
	public void populate(final PromotionOrderEntryConsumedModel source, final PromotionOrderEntryConsumedData target)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		target.setCode(source.getCode());
		target.setAdjustedUnitPrice(source.getAdjustedUnitPrice());
		target.setOrderEntryNumber(source.getOrderEntryNumberWithFallback());
		target.setQuantity(source.getQuantity());
	}
}
