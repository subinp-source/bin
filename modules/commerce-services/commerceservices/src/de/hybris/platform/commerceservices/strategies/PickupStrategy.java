/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.strategies;

import de.hybris.platform.commerceservices.enums.PickupInStoreMode;


/**
 * Abstraction for strategy determining Pickup mode.
 */
public interface PickupStrategy
{

	/**
	 * Returns one of the possible {@link de.hybris.platform.commerceservices.enums.PickupInStoreMode} values - to select
	 * the pickup-in-store mode
	 */
	PickupInStoreMode getPickupInStoreMode();
}
