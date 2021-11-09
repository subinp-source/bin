/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package ydocumentcartpackage.persistence.polyglot.repository.documentcart.query;

import java.util.Objects;
import java.util.Optional;


public class QueryByEntityAttributes implements BaseQuery
{
	private final EntityCondition conditions;

	public QueryByEntityAttributes(final EntityCondition conditions)
	{
		this.conditions = Objects.requireNonNull(conditions);
	}

	@Override
	public Optional<EntityCondition> getEntityCondition()
	{
		return Optional.of(conditions);
	}


}
