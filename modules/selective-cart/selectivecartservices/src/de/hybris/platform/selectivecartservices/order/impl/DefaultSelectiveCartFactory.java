/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.selectivecartservices.order.impl;

import de.hybris.platform.commerceservices.order.impl.CommerceCartFactory;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.selectivecartservices.order.SelectiveCartFactory;
import de.hybris.platform.servicelayer.user.UserService;


/**
 * Implementation of the {@link SelectiveCartFactory}.
 */
public class DefaultSelectiveCartFactory extends CommerceCartFactory implements SelectiveCartFactory
{
	private final UserService userService;

	private static final String SELECTIVE_CART = "selectivecart";

	public DefaultSelectiveCartFactory(final UserService userService)
	{
		this.userService = userService;
	}
	@Override
	public CartModel createSelectiveCartModel()
	{
		final String selectiveCartId = SELECTIVE_CART 
                                + getBaseSiteService().getCurrentBaseSite().getUid() 
                                + ((CustomerModel) getUserService().getCurrentUser()).getCustomerID();
		final CartModel cartModel = super.createCartInternal();
		cartModel.setCode(selectiveCartId);
		cartModel.setVisible(Boolean.FALSE);
		return cartModel;
	}

	protected UserService getUserService()
	{
		return userService;
	}

}
