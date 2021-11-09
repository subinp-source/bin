/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.strategies;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.store.BaseStoreModel;


/**
 * Strategy that provides indication if a product is available for pickup within specified baseStore
 */
public interface PickupAvailabilityStrategy
{
	Boolean isPickupAvailableForProduct(ProductModel product, BaseStoreModel baseStore);
}
