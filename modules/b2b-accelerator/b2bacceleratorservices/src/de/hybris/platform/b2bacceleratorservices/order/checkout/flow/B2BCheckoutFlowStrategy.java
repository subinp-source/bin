/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bacceleratorservices.order.checkout.flow;

import de.hybris.platform.acceleratorservices.enums.CheckoutFlowEnum;



/**
 * Abstraction for strategy determining flow for checkout logic.
 *
 * @since 4.6
 */
public interface B2BCheckoutFlowStrategy
{
	/**
	 * Returns one of the possible {@link CheckoutFlowEnum} values - to select the checkout flow
	 */
	CheckoutFlowEnum getCheckoutFlow();
}
