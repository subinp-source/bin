/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package ydocumentcartpackage.persistence.polyglot.repository.documentcart.cart;

import de.hybris.platform.persistence.polyglot.model.Key;
import de.hybris.platform.persistence.polyglot.PolyglotPersistence;
import de.hybris.platform.persistence.polyglot.model.SingleAttributeKey;


public final class CartAttributes
{
	private static final SingleAttributeKey ORDER_ATTRIBUTE = PolyglotPersistence.getNonlocalizedKey("order");
	private static final SingleAttributeKey CODE_ATTRIBUTE = PolyglotPersistence.getNonlocalizedKey("code");
	private static final SingleAttributeKey GUID_ATTRIBUTE = PolyglotPersistence.getNonlocalizedKey("guid");
	private static final SingleAttributeKey USER_ATTRIBUTE = PolyglotPersistence.getNonlocalizedKey("user");
	private static final SingleAttributeKey SITE_ATTRIBUTE = PolyglotPersistence.getNonlocalizedKey("site");

	private CartAttributes()
	{
		//no instantiation
	}

	public static SingleAttributeKey order()
	{
		return ORDER_ATTRIBUTE;
	}

	public static boolean isOrder(final Key key)
	{
		return order().equals(key);
	}

	public static SingleAttributeKey code()
	{
		return CODE_ATTRIBUTE;
	}

	public static boolean isCode(final Key key)
	{
		return code().equals(key);
	}

	public static SingleAttributeKey guid()
	{
		return GUID_ATTRIBUTE;
	}

	public static boolean isGuid(final Key key)
	{
		return guid().equals(key);
	}

	public static SingleAttributeKey user()
	{
		return USER_ATTRIBUTE;
	}

	public static SingleAttributeKey site()
	{
		return SITE_ATTRIBUTE;
	}

	static class ReferenceAttributes{
		static final SingleAttributeKey PROMOTION_RESULT = PolyglotPersistence.getNonlocalizedKey("promotionResult");
		static final SingleAttributeKey ORDER_ENTRY = PolyglotPersistence.getNonlocalizedKey("orderEntry");
		static final SingleAttributeKey ORDER = PolyglotPersistence.getNonlocalizedKey("order");
	}
}