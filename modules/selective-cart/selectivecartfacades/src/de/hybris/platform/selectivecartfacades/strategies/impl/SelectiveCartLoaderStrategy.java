/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.selectivecartfacades.strategies.impl;

import de.hybris.platform.commerceservices.order.CommerceSaveCartException;
import de.hybris.platform.commerceservices.order.CommerceSaveCartService;
import de.hybris.platform.commerceservices.service.data.CommerceSaveCartParameter;
import de.hybris.platform.commercewebservicescommons.strategies.impl.DefaultCartLoaderStrategy;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.selectivecartservices.SelectiveCartService;
import de.hybris.platform.selectivecartservices.order.impl.DefaultSelectiveCartFactory;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Optional;

import org.apache.log4j.Logger;


/**
 * Strategy that loads cart into the session, implementing in selective cart
 */
public class SelectiveCartLoaderStrategy extends DefaultCartLoaderStrategy
{
	private static final String SELECTIVE_CART = "selectivecart";
	private static final String SELECTIVE_CART_DESCRIPTION = "Selective cart for ";
	private static final String SAVED_CART_ERROR = "Cannot create empty selective cart.";

	private final UserService userService;
	private final DefaultSelectiveCartFactory selectiveCartFactory;
	private final SelectiveCartService selectiveCartService;
	private final CommerceSaveCartService commerceSaveCartService;

	private static final Logger LOG = Logger.getLogger(SelectiveCartLoaderStrategy.class);

	public SelectiveCartLoaderStrategy(final UserService userService, final DefaultSelectiveCartFactory selectiveCartFactory,
			final SelectiveCartService selectiveCartService, final CommerceSaveCartService commerceSaveCartService)
	{
		this.userService = userService;
		this.selectiveCartFactory = selectiveCartFactory;
		this.selectiveCartService = selectiveCartService;
		this.commerceSaveCartService = commerceSaveCartService;
	}

	@Override
	protected void loadUserCart(final String cartID, final boolean refresh)
	{
                final String selectiveCartId = SELECTIVE_CART
                                + getBaseSiteService().getCurrentBaseSite().getUid()
                                + ((CustomerModel) getUserService().getCurrentUser()).getCustomerID();

		if (selectiveCartId.equals(cartID))
		{
			final Optional<CartModel> optionalCart = getSelectiveCartService()
					.getSelectiveCartForCode(cartID);
			if (!optionalCart.isPresent())
			{
				final CartModel newCart = getSelectiveCartFactory().createSelectiveCartModel();
				try
				{
					restoreCart(createEmptySelectiveCart(newCart), cartID, refresh);
				}
				catch (final CommerceSaveCartException e)
				{
					LOG.error(SAVED_CART_ERROR, e);
				}
			}
			else
			{
				restoreCart(optionalCart.get(), cartID, refresh);
			}
		}
		else
		{
			super.loadUserCart(cartID, refresh);
		}

	}

	protected CartModel createEmptySelectiveCart(final CartModel newCart)
			throws CommerceSaveCartException
	{
		final CommerceSaveCartParameter parameter = new CommerceSaveCartParameter();
		parameter.setCart(newCart);
		parameter.setEnableHooks(Boolean.TRUE);
		parameter.setName(SELECTIVE_CART);
		parameter.setDescription(
				SELECTIVE_CART_DESCRIPTION + "[" + ((CustomerModel) getUserService().getCurrentUser()).getName() + "].");

		return getCommerceSaveCartService().saveCart(parameter).getSavedCart();
	}

	protected UserService getUserService()
	{
		return userService;
	}

	protected DefaultSelectiveCartFactory getSelectiveCartFactory()
	{
		return selectiveCartFactory;
	}

	protected CommerceSaveCartService getCommerceSaveCartService()
	{
		return commerceSaveCartService;
	}

	protected SelectiveCartService getSelectiveCartService()
	{
		return selectiveCartService;
	}

}
