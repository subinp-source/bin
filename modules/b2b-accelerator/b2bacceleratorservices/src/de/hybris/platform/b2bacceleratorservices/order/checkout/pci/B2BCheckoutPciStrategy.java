/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bacceleratorservices.order.checkout.pci;

import de.hybris.platform.acceleratorservices.enums.CheckoutPciOptionEnum;



/**
 *
 */
public interface B2BCheckoutPciStrategy
{
	/**
	 * Returns one of the possible {@link CheckoutPciOptionEnum} values - to select the PCI options.
	 */
	CheckoutPciOptionEnum getSubscriptionPciOption();
}
