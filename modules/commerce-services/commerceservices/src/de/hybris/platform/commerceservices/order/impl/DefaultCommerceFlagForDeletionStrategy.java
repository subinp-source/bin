/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.order.impl;

import de.hybris.platform.commerceservices.order.CommerceSaveCartException;
import de.hybris.platform.commerceservices.service.data.CommerceSaveCartParameter;
import de.hybris.platform.commerceservices.service.data.CommerceSaveCartResult;
import de.hybris.platform.core.model.order.CartModel;

import java.util.Date;


/**
 * Default implementation of the interface
 * {@link de.hybris.platform.commerceservices.order.CommerceFlagForDeletionStrategy}. The provided
 * {@link de.hybris.platform.commerceservices.service.data.CommerceSaveCartParameter#cart} (or if empty the session
 * cart) is simply enriched with additional data to mark it as a saved cart. The cart is not cloned.
 */
public class DefaultCommerceFlagForDeletionStrategy extends AbstractCommerceFlagForDeletionStrategy
{
	@Override
	public CommerceSaveCartResult flagForDeletion(final CommerceSaveCartParameter parameters) throws CommerceSaveCartException
	{
		final CommerceSaveCartResult flagForDeletionResult = new CommerceSaveCartResult();
		this.beforeFlagForDeletion(parameters);

		validateFlagForDeletionParameters(parameters);

		final CartModel cartToBeFlagged = parameters.getCart();

		cartToBeFlagged.setExpirationTime(null);
		cartToBeFlagged.setSaveTime(null);
		cartToBeFlagged.setSavedBy(null);
		cartToBeFlagged.setName(null);
		cartToBeFlagged.setDescription(null);

		flagForDeletionResult.setSavedCart(cartToBeFlagged);
		getModelService().save(cartToBeFlagged);
		getModelService().refresh(cartToBeFlagged);

		// cartToBeFlagged becomes the latest cart after saving.
		// So, we need re-save the session cart to not lose it.
		final CartModel sessionCart = this.getCartService().getSessionCart();
		sessionCart.setModifiedtime(new Date());
		getModelService().save(sessionCart);
		getModelService().refresh(sessionCart);

		this.afterFlagForDeletion(parameters, flagForDeletionResult);
		return flagForDeletionResult;
	}
}
