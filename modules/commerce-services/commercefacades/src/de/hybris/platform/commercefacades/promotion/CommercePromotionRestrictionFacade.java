/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.promotion;


import de.hybris.platform.commerceservices.promotion.CommercePromotionRestrictionException;


/**
 * PromotionRestriction facade interface.
 */
public interface CommercePromotionRestrictionFacade
{
	/**
	 * Enables OrderPromotion by adding current cart to PromotionOrderRestriction
	 * 
	 * @param promotionCode
	 *           promotion
	 * @throws {@link CommercePromotionRestrictionException}
	 */
	void enablePromotionForCurrentCart(String promotionCode) throws CommercePromotionRestrictionException;

	/**
	 * Disables OrderPromotion by removing current cart from PromotionOrderRestriction
	 * 
	 * @param promotionCode
	 *           promotion
	 * @throws {@link CommercePromotionRestrictionException}
	 */
	void disablePromotionForCurrentCart(String promotionCode) throws CommercePromotionRestrictionException;
}
