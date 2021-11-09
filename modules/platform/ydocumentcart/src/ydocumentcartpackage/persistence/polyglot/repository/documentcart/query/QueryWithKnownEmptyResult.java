/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package ydocumentcartpackage.persistence.polyglot.repository.documentcart.query;

public class QueryWithKnownEmptyResult implements BaseQuery
{
	@Override
	public boolean isKnownThereIsNoResult()
	{
		return true;
	}
}
