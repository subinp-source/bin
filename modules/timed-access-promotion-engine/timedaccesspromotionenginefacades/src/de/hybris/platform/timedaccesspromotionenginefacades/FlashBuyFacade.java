/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.timedaccesspromotionenginefacades;

import de.hybris.platform.commercefacades.product.data.ProductData;


/**
 * Deals with flash buy related DTOs using existing service
 */
public interface FlashBuyFacade
{
	/**
	 * Prepares flash buy information, sets product's max order quantity for product, and returns flashbuy coupon code
	 *
	 * @param product
	 *           ProductData of the product
	 * @return String FlashBuyCoupon code
	 */
	String prepareFlashBuyInfo(ProductData product);

	/**
	 * Updates flash buy status in cart
	 */
	void updateFlashBuyStatusForCart();
}
