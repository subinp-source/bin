/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.selectivecartfacades.strategies.impl;

import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.selectivecartfacades.SelectiveCartFacade;
import de.hybris.platform.selectivecartfacades.strategies.SelectiveCartUpdateStrategy;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link SelectiveCartUpdateStrategy}
 */
public class DefaultSelectiveCartUpdateStrategy implements SelectiveCartUpdateStrategy
{
	private static final Logger LOG = Logger.getLogger(DefaultSelectiveCartUpdateStrategy.class);
	private SelectiveCartFacade selectiveCartFacade;

	@Override
	public void update()
	{
		
		try
		{
			getSelectiveCartFacade().updateCartFromWishlist();

		}
		catch (final CommerceCartModificationException e) // NOSONAR
		{
			LOG.warn("Failed to update cart.");
		}
	}

	protected SelectiveCartFacade getSelectiveCartFacade()
	{
		return selectiveCartFacade;
	}

	@Required
	public void setSelectiveCartFacade(final SelectiveCartFacade selectiveCartFacade)
	{
		this.selectiveCartFacade = selectiveCartFacade;
	}
}
