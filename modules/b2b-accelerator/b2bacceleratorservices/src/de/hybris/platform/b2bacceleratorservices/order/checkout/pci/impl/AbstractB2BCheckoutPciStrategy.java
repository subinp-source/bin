/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bacceleratorservices.order.checkout.pci.impl;

import de.hybris.platform.b2bacceleratorservices.order.checkout.pci.B2BCheckoutPciStrategy;

import org.springframework.beans.factory.annotation.Required;


/**
 * 
 */
public abstract class AbstractB2BCheckoutPciStrategy implements B2BCheckoutPciStrategy
{
	private B2BCheckoutPciStrategy defaultCheckoutPciStrategy;


	protected B2BCheckoutPciStrategy getDefaultCheckoutPciStrategy()
	{
		return this.defaultCheckoutPciStrategy;
	}

	@Required
	public void setDefaultCheckoutPciStrategy(final B2BCheckoutPciStrategy defaultCheckoutPciStrategy)
	{
		this.defaultCheckoutPciStrategy = defaultCheckoutPciStrategy;
	}
}
