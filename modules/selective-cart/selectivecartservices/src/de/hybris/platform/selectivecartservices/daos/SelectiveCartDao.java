/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.selectivecartservices.daos;

import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.wishlist2.model.Wishlist2Model;

import java.util.Optional;


/**
 * Looks up items related to selective cart
 */
public interface SelectiveCartDao
{

	/**
	 * Finds wishlist by name for the current user
	 * 
	 * @param user
	 *           the current user
	 * @param name
	 *           the Wishlist2Model name
	 * @return the Wishlist2Model
	 */
	Wishlist2Model findWishlistByName(UserModel user, String name);

	/**
	 * Finds cart entry by cart code and entry number
	 * 
	 * @param cartCode
	 *           the cart code
	 * @param entryNumber
	 *           the entry number
	 * @return the CartEntryModel
	 */
	CartEntryModel findCartEntryByCartCodeAndEntryNumber(String cartCode, Integer entryNumber);

	/**
	 * Finds invisible saved cart for selective cart
	 * 
	 * @param user
	 *           the user
	 * @return the Optional CartModel
	 * @deprecated since 2005.
	 */
	@Deprecated(since = "2005", forRemoval = true)
	Optional<CartModel> findSelectiveCartByUser(final UserModel user);

	/**
	 * Finds invisible saved cart for selective cart
	 *
	 * @param code
	 * @return the Optional CartModel
	 */
	Optional<CartModel> findSelectiveCartByCode(String code);
}
