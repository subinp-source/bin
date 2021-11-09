/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bacceleratorfacades.futurestock;

import de.hybris.platform.commercefacades.product.data.FutureStockData;

import java.util.List;
import java.util.Map;


/**
 * Facade for 'Future Stock Management'.
 */
public interface B2BFutureStockFacade
{

	/**
	 * Gets the future product availability for the specified product, for each future date.
	 *
	 * @param productCode
	 *           the product code
	 * @return A list of quantity ordered by date. If there is no availability for this product in the future, an empty
	 *         list is returned.
	 */
	List<FutureStockData> getFutureAvailability(final String productCode);

	/**
	 * Gets the future product availability for the list of specified products, for each future date.
	 *
	 * @param productCodes
	 *           the product codes
	 * @return A map of product codes with a list of quantity ordered by date.
	 */
	Map<String, List<FutureStockData>> getFutureAvailability(final List<String> productCodes);

	/**
	 * Gets the future product availability for the list of specified variants related to a given product.
	 *
	 * @param productCode
	 *           the product code
	 * @param skus
	 *           Product codes of the desired variants related to the productCode.
	 * @return A map of product codes with a list of quantity ordered by date. If product is not variant, returns null.
	 */
	Map<String, List<FutureStockData>> getFutureAvailabilityForSelectedVariants(final String productCode, final List<String> skus);


}
