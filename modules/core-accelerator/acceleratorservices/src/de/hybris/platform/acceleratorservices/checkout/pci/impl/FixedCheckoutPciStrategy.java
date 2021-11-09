/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.checkout.pci.impl;

import de.hybris.platform.acceleratorservices.checkout.pci.CheckoutPciStrategy;
import de.hybris.platform.acceleratorservices.enums.CheckoutPciOptionEnum;

import org.springframework.beans.factory.annotation.Required;


/**
 * Uses fixed {@link CheckoutPciOptionEnum} as result. Used most likely on the end of checkout PCI option strategy
 * chain.
 */
public class FixedCheckoutPciStrategy implements CheckoutPciStrategy
{
	private CheckoutPciOptionEnum subscriptionPciOption;

	@Override
	public CheckoutPciOptionEnum getSubscriptionPciOption()
	{
		return this.subscriptionPciOption;
	}

	@Required
	public void setSubscriptionPciOption(final CheckoutPciOptionEnum subscriptionPciOption)
	{
		this.subscriptionPciOption = subscriptionPciOption;
	}
}
