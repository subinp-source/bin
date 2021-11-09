/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.checkout.pci;

import de.hybris.platform.acceleratorservices.enums.CheckoutPciOptionEnum;


/**
 *
 */
public interface CheckoutPciStrategy
{
	CheckoutPciOptionEnum getSubscriptionPciOption();
}
