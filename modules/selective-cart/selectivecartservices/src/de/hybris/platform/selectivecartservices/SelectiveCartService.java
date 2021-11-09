/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.selectivecartservices;

import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.wishlist2.model.Wishlist2EntryModel;
import de.hybris.platform.wishlist2.model.Wishlist2Model;

import java.util.Date;
import java.util.Optional;


/**
 * Deals with selective cart related Models using existing DAOs
 */
public interface SelectiveCartService
{
	/**
	 * Gets wishlist for selective cart and the current user
	 *
	 * @return the wishlist model
	 */
	Wishlist2Model getWishlistForSelectiveCart();

	/**
	 * Gets wishlist entry by product code
	 *
	 * @param product
	 *           the product code
	 * @return the wishlist entry model
	 */
	Wishlist2EntryModel getWishlistEntryForProduct(ProductModel product);

	/**
	 * Gets wishlist entry by product code and wishlist
	 *
	 * @param product
	 *           the product code
	 * @param wishlist
	 *           the wishlist model
	 * @return the wishlist entry model
	 */
	Wishlist2EntryModel getWishlistEntryForProduct(ProductModel product, Wishlist2Model wishlist);

	/**
	 * Removes wishlist entry by product code and wishlist
	 *
	 * @param product
	 *           the product code
	 * @param wishlist
	 *           the wishlist model
	 */
	void removeWishlistEntryForProduct(ProductModel product, Wishlist2Model wishlist);

	/**
	 * Updates the quantity of the give wishlist entry
	 *
	 * @param wishlistEntry
	 *           the wishlist entry model
	 * @param quantity
	 *           the total number of quantity
	 */
	void updateQuantityForWishlistEntry(Wishlist2EntryModel wishlistEntry, Integer quantity);


	/**
	 * Creates a wish list for the current customer
	 *
	 * @return the wishlist model
	 */
	Wishlist2Model createWishlist();

	/**
	 * Saves wishlist entry and addToCartTime by product and wishlist
	 *
	 * @param product
	 *           the product code
	 * @param wishlist
	 *           the wishlist model
	 * @param addToCartTime
	 *           the added to cart time for a wishlist entry
	 * @return the saved wishlist entry
	 */
	Wishlist2EntryModel saveWishlistEntryForProduct(ProductModel product, Wishlist2Model wishlist, Date addToCartTime);

	/**
	 * Updates to the original addToCartTime when moving the wishlist entry from cart
	 *
	 * @param cartCode
	 *           the cart code used for getting cart entry
	 * @param entryNumber
	 *           the entry number used for getting cart entry
	 * @param addToCartTime
	 *           the new "added to cart" time
	 */
	void updateCartTimeForOrderEntry(final String cartCode, final int entryNumber, Date addToCartTime);

	/**
	 * Finds invisible saved cart for selective cart
	 * 
	 * @param user
	 *           the user
	 * @return the Optional CartModel
	 * @deprecated since 2005.
	 */
	@Deprecated(since = "2005", forRemoval = true)
	Optional<CartModel> getSelectiveCartForUser(final UserModel user);
	
	/**
	 * Finds invisible saved cart for selective cart
	 *
	 * @param code
	 *           cart code
	 * @param user
	 *           the user
	 * @return the Optional CartModel
	 */
	Optional<CartModel> getSelectiveCartForCode(final String code);
}
