/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorfacades.flow;

import de.hybris.platform.acceleratorfacades.order.AcceleratorCheckoutFacade;
import de.hybris.platform.acceleratorservices.enums.CheckoutPciOptionEnum;


/**
 * CheckoutFlowFacade interface extends the {@link AcceleratorCheckoutFacade}.
 *
 * @since 4.6
 * @spring.bean checkoutFacade
 */
public interface CheckoutFlowFacade extends AcceleratorCheckoutFacade
{

	/**
	 * Gets the subscription pci option
	 *
	 * @return the pci option
	 */
	CheckoutPciOptionEnum getSubscriptionPciOption();
}
