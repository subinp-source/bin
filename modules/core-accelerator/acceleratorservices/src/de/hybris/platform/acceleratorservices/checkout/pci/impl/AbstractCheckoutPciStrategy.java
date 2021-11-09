/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.checkout.pci.impl;

import de.hybris.platform.acceleratorservices.checkout.pci.CheckoutPciStrategy;

import org.springframework.beans.factory.annotation.Required;


/**
 *
 */
public abstract class AbstractCheckoutPciStrategy implements CheckoutPciStrategy
{
	private CheckoutPciStrategy defaultCheckoutPciStrategy;


	protected CheckoutPciStrategy getDefaultCheckoutPciStrategy()
	{
		return this.defaultCheckoutPciStrategy;
	}

	@Required
	public void setDefaultCheckoutPciStrategy(final CheckoutPciStrategy defaultCheckoutPciStrategy)
	{
		this.defaultCheckoutPciStrategy = defaultCheckoutPciStrategy;
	}

	protected abstract boolean canSupport();
}
