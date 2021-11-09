/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.promotion;


import de.hybris.platform.commerceservices.model.promotions.PromotionOrderRestrictionModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.promotions.model.AbstractPromotionRestrictionModel;

import java.util.List;


/**
 * Service for managing PromotionRestrictions
 */
public interface CommercePromotionRestrictionService
{
	/**
	 * Gets the list of {@link AbstractPromotionRestrictionModel} for {@link AbstractPromotionModel}.
	 * 
	 * @param promotion
	 *           promotion
	 * @return The list of {@link AbstractPromotionRestrictionModel}
	 */
	List<AbstractPromotionRestrictionModel> getPromotionRestrictions(final AbstractPromotionModel promotion);

	/**
	 * Gets the {@link PromotionOrderRestrictionModel} instance for {@link AbstractPromotionModel} if exists.
	 * 
	 * @param promotion
	 *           promotion
	 * @return instance of {@link AbstractPromotionRestrictionModel}
	 */
	PromotionOrderRestrictionModel getPromotionOrderRestriction(final AbstractPromotionModel promotion);

	/**
	 * Adds unique order to {@link PromotionOrderRestrictionModel}
	 * 
	 * @param restriction
	 *           restriction
	 * @param order
	 *           order
	 */
	void addOrderToRestriction(final PromotionOrderRestrictionModel restriction, final AbstractOrderModel order);


	/**
	 * Removes order from {@link PromotionOrderRestrictionModel}
	 * 
	 * @param restriction
	 *           restriction
	 * @param order
	 *           order
	 */
	void removeOrderFromRestriction(final PromotionOrderRestrictionModel restriction, final AbstractOrderModel order);
}
