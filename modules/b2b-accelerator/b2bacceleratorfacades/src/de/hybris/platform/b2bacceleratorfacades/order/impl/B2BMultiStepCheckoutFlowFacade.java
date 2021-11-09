/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bacceleratorfacades.order.impl;

import de.hybris.platform.acceleratorfacades.flow.CheckoutFlowFacade;
import de.hybris.platform.acceleratorservices.enums.CheckoutPciOptionEnum;

import org.springframework.beans.factory.annotation.Required;


/**
 * Mutli step checkout implementation of the {@link CheckoutFlowFacade} interface. Delegates resolving the checkout flow
 * to an injected {@link CheckoutFlowFacade}.
 */
public class B2BMultiStepCheckoutFlowFacade extends DefaultB2BAcceleratorCheckoutFacade implements CheckoutFlowFacade
{
	private CheckoutFlowFacade checkoutFlowFacade;

	@Override
	public CheckoutPciOptionEnum getSubscriptionPciOption()
	{
		return getCheckoutFlowFacade().getSubscriptionPciOption();
	}

	protected CheckoutFlowFacade getCheckoutFlowFacade()
	{
		return checkoutFlowFacade;
	}

	@Required
	public void setCheckoutFlowFacade(final CheckoutFlowFacade facade)
	{
		checkoutFlowFacade = facade;
	}
}
