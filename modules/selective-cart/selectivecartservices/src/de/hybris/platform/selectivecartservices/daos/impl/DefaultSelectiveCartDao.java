/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.selectivecartservices.daos.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.selectivecartservices.daos.SelectiveCartDao;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.wishlist2.model.Wishlist2Model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * Default implementation of {@link SelectiveCartDao}
 */
public class DefaultSelectiveCartDao extends DefaultGenericDao<Wishlist2Model> implements SelectiveCartDao
{
	protected static final String VISIBLE = "visible";

	private static final String FIND_WISHLIST_BY_NAME = "SELECT {" + Wishlist2Model.PK + "} FROM {" + Wishlist2Model._TYPECODE
			+ "} WHERE {" + Wishlist2Model.USER + "} = ?" + Wishlist2Model.USER + " AND {" + Wishlist2Model.NAME + "} = ?"
			+ Wishlist2Model.NAME;

	private static final String FIND_CART_CODE = "GET {Cart} where {CODE} =?cartCode";
	private static final String FIND_CART_USER = "GET {Cart} WHERE {USER} = ?user AND {VISIBLE} = ?" + VISIBLE;

	public DefaultSelectiveCartDao()
	{
		super(Wishlist2Model._TYPECODE);
	}

	@Override
	public Wishlist2Model findWishlistByName(final UserModel user, final String name)
	{
		final FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_WISHLIST_BY_NAME);
		query.addQueryParameter(Wishlist2Model.USER, user);
		query.addQueryParameter(Wishlist2Model.NAME, name);
		return getFlexibleSearchService().searchUnique(query);
	}

	@Override
	public CartEntryModel findCartEntryByCartCodeAndEntryNumber(final String cartCode
			, final Integer entryNumber)
	{
		final FlexibleSearchQuery queryForCart = new FlexibleSearchQuery(FIND_CART_CODE);
		queryForCart.addQueryParameter("cartCode", cartCode);
		final CartModel cartModel = getFlexibleSearchService().searchUnique(queryForCart);
		final List<AbstractOrderEntryModel> cartEntryList = cartModel.getEntries().stream()
				.filter(e -> e.getEntryNumber().equals(entryNumber)).collect(Collectors.toList());

		if (cartEntryList.isEmpty())
		{
			throw new ModelNotFoundException("No cart entry found.");
		}
		else if (cartEntryList.size() > 1)
		{
			throw new AmbiguousIdentifierException("Found " + cartEntryList.size() + " cart entries found.");
		}
		else
		{
			return (CartEntryModel) cartEntryList.get(0);
		}
	}

	/**
	 * @deprecated since 2005
	 */
	@Deprecated(since = "2005", forRemoval = true)
	@Override
	public Optional<CartModel> findSelectiveCartByUser(final UserModel user)
	{
		final Map<String, Object> params = new HashMap<>();
		params.put("user", user);
		params.put(VISIBLE, Boolean.FALSE);
		final FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_CART_USER, params);
		return getFlexibleSearchService().<CartModel>search(query).getResult().stream().findFirst();
	}

	@Override
	public Optional<CartModel> findSelectiveCartByCode(String code)
	{
		final Map<String, Object> params = new HashMap<>();
		params.put("cartCode", code);
		params.put(VISIBLE, Boolean.FALSE);
		final FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_CART_CODE, params);
		return getFlexibleSearchService().<CartModel>search(query).getResult().stream().findFirst();
	}
}
