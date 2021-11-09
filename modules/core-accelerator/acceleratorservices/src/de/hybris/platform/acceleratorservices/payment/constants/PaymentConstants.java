/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.payment.constants;

/**
 * 
 */
public interface PaymentConstants
{
	interface PaymentProperties // NOSONAR
	{
		String HOP_POST_URL = "hop.post.url";
		String SOP_POST_URL = "sop.post.url";
		String HOP_PCI_STRATEGY_ENABLED = "hop.pci.strategy.enabled";
		String HOP_DEBUG_MODE = "hop.debug.mode";
		String SITE_PCI_STRATEGY="site.pci.strategy";
	}
}
