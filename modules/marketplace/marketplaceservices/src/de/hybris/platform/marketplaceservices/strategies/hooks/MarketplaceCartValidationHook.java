/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.marketplaceservices.strategies.hooks;

import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationStatus;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.commerceservices.strategies.hooks.CartValidationHook;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.marketplaceservices.dao.MarketplaceCartEntryDao;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;


/**
 * Varifies if the products in cart are saleable before the checkout process. It's implementation of
 * {@link CartValidationHook}
 * 
 * @deprecated since 1905. use
 *             {@link de.hybris.platform.marketplaceservices.strategies.hooks.impl.MarketplaceCartValidationHook}
 *             replace
 * 
 */
@Deprecated(since = "1905", forRemoval= true )
public class MarketplaceCartValidationHook implements CartValidationHook
{
	private ModelService modelService;
	private MarketplaceCartEntryDao cartEntryDao;

	@Override
	public void beforeValidateCart(final CommerceCartParameter parameter, final List<CommerceCartModification> modifications)
	{
		//Nothing to do
	}

	@Override
	public void afterValidateCart(final CommerceCartParameter parameter, final List<CommerceCartModification> modifications)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("cart", parameter.getCart());
		final CartModel cartModel = parameter.getCart();
		getModelService().removeAll(getCartEntryDao().findUnSaleableCartEntries(cartModel));
		getModelService().refresh(cartModel);
		modifications.stream().filter(modification -> !(modification.getEntry().getProduct().getSaleable().booleanValue()))
				.forEach(modification -> {
					modification.setStatusCode(CommerceCartModificationStatus.UNAVAILABLE);
					final CartEntryModel entry = new CartEntryModel();
					entry.setProduct(modification.getEntry().getProduct());
					modification.setEntry(entry);
				});
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

	protected MarketplaceCartEntryDao getCartEntryDao()
	{
		return cartEntryDao;
	}

	@Required
	public void setCartEntryDao(final MarketplaceCartEntryDao cartEntryDao)
	{
		this.cartEntryDao = cartEntryDao;
	}

}
