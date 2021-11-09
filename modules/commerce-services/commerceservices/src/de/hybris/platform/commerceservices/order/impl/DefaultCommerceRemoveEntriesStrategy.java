/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.order.impl;

import de.hybris.platform.commerceservices.order.CommerceRemoveEntriesStrategy;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.servicelayer.model.ModelService;

import org.springframework.beans.factory.annotation.Required;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

/**
 *  A default commerce strategy for removing all entries from cart
 *
 */
public class DefaultCommerceRemoveEntriesStrategy implements CommerceRemoveEntriesStrategy
{
	private ModelService modelService;

	@Override
	public void removeAllEntries(final CommerceCartParameter parameter)
	{
		final CartModel cartModel = parameter.getCart();
		validateParameterNotNull(cartModel, "Cart model cannot be null");
		getModelService().removeAll(cartModel.getEntries());
		getModelService().refresh(cartModel);
		cartModel.setCalculated(Boolean.FALSE);
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}
}
