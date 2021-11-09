/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.promotion.impl;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.promotions.impl.DefaultPromotionGroupStrategy;
import de.hybris.platform.promotions.model.PromotionGroupModel;


public class DefaultCommercePromotionGroupStrategy extends DefaultPromotionGroupStrategy
{

	@Override
	public PromotionGroupModel getDefaultPromotionGroup(final AbstractOrderModel order)
	{
		if (order == null)
		{
			return getPromotionsService().getDefaultPromotionGroup();
		}

		final PromotionGroupModel promotionGroup = order.getSite() != null ? order.getSite().getDefaultPromotionGroup() : null;
		return (promotionGroup != null ? promotionGroup : getPromotionsService().getDefaultPromotionGroup());
	}
}
