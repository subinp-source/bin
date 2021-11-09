/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bacceleratorfacades.order;

import java.util.Comparator;

import de.hybris.platform.b2bacceleratorfacades.api.cart.CartFacade;
import de.hybris.platform.commercefacades.order.data.AbstractOrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.data.VariantOptionData;


/**
 * @deprecated Since 5.3. See {@link de.hybris.platform.b2bacceleratorfacades.api.cart.CartFacade}
 */
@Deprecated(since = "5.3", forRemoval = true)
public interface B2BCartFacade extends CartFacade
{

	/**
	 * Group multi-dimensional items in a cart.
	 *
	 * @param orderData
	 *           parameter containing the order data object.
	 * @param variantSortStrategy
	 *           the strategy used to sort the variants.
	 *
	 */
	<T extends AbstractOrderData> void groupMultiDimensionalProducts(final T orderData,
			final Comparator<VariantOptionData> variantSortStrategy);

}
