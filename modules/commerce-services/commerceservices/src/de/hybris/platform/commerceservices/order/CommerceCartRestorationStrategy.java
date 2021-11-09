/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.order;

import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;


/**
 * A strategy for restoring users cart.
 */
public interface CommerceCartRestorationStrategy
{
	CommerceCartRestoration restoreCart(CommerceCartParameter parameters) throws CommerceCartRestorationException;
}
