/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.selectivecartservices.strategies;

import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.impl.DefaultCommerceAddToCartStrategy;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.selectivecartservices.SelectiveCartService;
import de.hybris.platform.wishlist2.model.Wishlist2EntryModel;

import org.springframework.beans.factory.annotation.Required;


/**
 * Recalculates quantity of product before adding to cart
 */
public class SelectiveCartAddToCartStrategy extends DefaultCommerceAddToCartStrategy
{

	private SelectiveCartService selectiveCartService;


	@Override
	public CommerceCartModification addToCart(final CommerceCartParameter parameter) throws CommerceCartModificationException
	{
		final Wishlist2EntryModel wishlistEntry = getSelectiveCartService().getWishlistEntryForProduct(parameter.getProduct());
		if (wishlistEntry != null)
		{
			parameter.setQuantity(parameter.getQuantity() + wishlistEntry.getQuantity().intValue());
			selectiveCartService.removeWishlistEntryForProduct(parameter.getProduct(), getSelectiveCartService()
					.getWishlistForSelectiveCart());
		}
		return super.addToCart(parameter);
	}

	protected SelectiveCartService getSelectiveCartService()
	{
		return selectiveCartService;
	}

	@Required
	public void setSelectiveCartService(final SelectiveCartService selectiveCartService)
	{
		this.selectiveCartService = selectiveCartService;
	}

}
