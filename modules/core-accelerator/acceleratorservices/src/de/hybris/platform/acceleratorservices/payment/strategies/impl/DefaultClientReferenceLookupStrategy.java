/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.payment.strategies.impl;

import de.hybris.platform.acceleratorservices.payment.strategies.ClientReferenceLookupStrategy;
import de.hybris.platform.order.CartService;
import org.springframework.beans.factory.annotation.Required;


public class DefaultClientReferenceLookupStrategy implements ClientReferenceLookupStrategy
{
	private CartService cartService;
	private static final String DEFAULT_CLIENTREF_ID = "Default_Client_Ref";


	public String lookupClientReferenceId()
	{
		if (getCartService().hasSessionCart())
		{
			return getCartService().getSessionCart().getGuid();
		}
		return DEFAULT_CLIENTREF_ID;
	}


	protected CartService getCartService()
	{
		return cartService;
	}

	@Required
	public void setCartService(final CartService cartService)
	{
		this.cartService = cartService;
	}
}
