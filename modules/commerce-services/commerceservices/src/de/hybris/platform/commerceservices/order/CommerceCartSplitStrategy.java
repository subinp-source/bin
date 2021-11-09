/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.order;

import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;


/**
 * A strategy for splitting the cart
 */
public interface CommerceCartSplitStrategy
{
	long split(CommerceCartParameter parameters) throws CommerceCartModificationException;
}
