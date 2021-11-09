/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package ydocumentcartpackage.persistence.polyglot.repository.documentcart.query;

import de.hybris.platform.persistence.polyglot.model.Identity;

import java.util.Objects;
import java.util.Optional;


public class QueryByRootId extends QueryByEntityId
{
	public QueryByRootId(final Identity rootId)
	{
		super(Objects.requireNonNull(rootId, "rootId mustn't be null."));
	}

	@Override
	public Optional<Identity> getRootId()
	{
		return getEntityId();
	}

	@Override
	public Optional<EntityCondition> getUniqueRootCondition()
	{
		return getEntityCondition();
	}


	@Override
	public Optional<EntityCondition> getRootCondition()
	{
		return getEntityCondition();
	}

}
