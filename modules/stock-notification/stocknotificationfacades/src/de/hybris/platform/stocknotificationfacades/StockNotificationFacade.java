/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.stocknotificationfacades;

import de.hybris.platform.commercefacades.product.data.ProductData;


/**
 * interface of the Stock Notification Facade
 */
public interface StockNotificationFacade
{
	/**
	 * check if current product is watching.
	 *
	 * @param product
	 *           ProductData of the product
	 * @return true the product is watching.
	 */
	boolean isWatchingProduct(ProductData product);

}
