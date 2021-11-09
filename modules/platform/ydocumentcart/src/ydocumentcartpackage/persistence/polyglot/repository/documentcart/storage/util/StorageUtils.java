/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package ydocumentcartpackage.persistence.polyglot.repository.documentcart.storage.util;

import ydocumentcartpackage.persistence.polyglot.repository.documentcart.Query;
import ydocumentcartpackage.persistence.polyglot.repository.documentcart.query.BaseQuery;

import java.util.Objects;

public class StorageUtils
{
	private StorageUtils()
	{
		//empty
	}

	public static BaseQuery requireBaseQuery(final Query query)
	{
		Objects.requireNonNull(query, "query mustn't be null.");
		if (query instanceof BaseQuery)
		{
			return (BaseQuery) query;
		}
		throw new IllegalArgumentException("changeSet must be an implementation of '" + BaseQuery.class.getSimpleName() + "'.");
	}
}
