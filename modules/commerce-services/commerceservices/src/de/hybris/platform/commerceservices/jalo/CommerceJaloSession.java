/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.jalo;

import de.hybris.platform.jalo.JaloSession;


/**
 * JaloSession that does not delete the cart so we have a chance to restore it later.
 */
public class CommerceJaloSession extends JaloSession
{
	@Override
	public void removeCart()
	{
		setAttachedCart(null);
	}
}
