/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.customerinterestsfacades.futurestock;

import de.hybris.platform.commercefacades.product.data.FutureStockData;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.List;


/**
 * Facade for 'Future Stock Management'.
 */
public interface ExtendedFutureStockFacade
{

	/**
	 * Gets the future product availability for the specified product, for each future date.
	 *
	 * @param productModel
	 *           the product model
	 * @return A list of quantity ordered by date. If there is no availability for this product in the future, an empty
	 *         list is returned.
	 */
	List<FutureStockData> getFutureAvailability(final ProductModel productModel);
}
