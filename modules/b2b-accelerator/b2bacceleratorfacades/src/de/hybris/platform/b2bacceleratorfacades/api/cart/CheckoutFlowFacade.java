/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bacceleratorfacades.api.cart;

import de.hybris.platform.acceleratorservices.enums.CheckoutFlowEnum;
import de.hybris.platform.acceleratorservices.enums.CheckoutPciOptionEnum;



/**
 * The CheckoutFlowFacade supports resolving the {@link CheckoutFlowEnum} for the current request.
 */
public interface CheckoutFlowFacade
{
	/**
	 * Gets the checkout flow.
	 *
	 * @return the enum value of the checkout flow
	 */
	CheckoutFlowEnum getCheckoutFlow();

	/**
	 * Gets the subscription PCI Option.
	 *
	 * @return the enum value for subscription PCI Option
	 */
	CheckoutPciOptionEnum getSubscriptionPciOption();
}
