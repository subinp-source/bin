/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.order.impl;

import de.hybris.platform.commerceservices.order.CommerceCloneSavedCartStrategy;
import de.hybris.platform.commerceservices.order.CommerceSaveCartException;
import de.hybris.platform.commerceservices.service.data.CommerceSaveCartParameter;
import de.hybris.platform.commerceservices.service.data.CommerceSaveCartResult;
import de.hybris.platform.core.model.order.CartModel;


/**
 * Default implementation of the interface {@link CommerceCloneSavedCartStrategy}.
 *
 */
public class DefaultCommerceCloneSavedCartStrategy extends AbstractCommerceCloneSavedCartStrategy
{
	@Override
	public CommerceSaveCartResult cloneSavedCart(final CommerceSaveCartParameter parameter) throws CommerceSaveCartException
	{
		final CommerceSaveCartResult cloneCartResult = new CommerceSaveCartResult();

		this.beforeCloneSaveCart(parameter);

		final CartModel clonedCart = getCartService().clone(null, null, parameter.getCart(), null);
		clonedCart.setPaymentTransactions(null);
		clonedCart.setCode(null); // save new cart, do not update existing one
		cloneCartResult.setSavedCart(clonedCart);
		getModelService().save(clonedCart);

		this.afterCloneSaveCart(parameter, cloneCartResult);

		return cloneCartResult;
	}
}