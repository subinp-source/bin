/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
/**
 * A hook strategy to run custom code before adding product to cart
 */
package de.hybris.platform.notificationservices.service.hooks;

import de.hybris.platform.core.model.user.CustomerModel;

/**
 * Hook for Customer setting changing
 */
public interface CustomerSettingsChangedHook
{
	/**
	 * Specific customized logic after unbinding mobile number
	 *
	 * @param parameters
	 *           A customer who wants to unbind mobile number.
	 *
	 */
	void afterUnbindMobileNumber(final CustomerModel customer);

}
