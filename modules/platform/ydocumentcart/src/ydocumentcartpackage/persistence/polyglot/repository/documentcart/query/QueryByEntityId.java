/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package ydocumentcartpackage.persistence.polyglot.repository.documentcart.query;

import de.hybris.platform.persistence.polyglot.model.Identity;
import de.hybris.platform.persistence.polyglot.PolyglotPersistence.CoreAttributes;

import java.util.Objects;
import java.util.Optional;


public class QueryByEntityId implements BaseQuery
{
	private final Identity id;

	public QueryByEntityId(final Identity id)
	{
		this.id = Objects.requireNonNull(id, "id mustn't be null.");
	}

	@Override
	public Optional<Identity> getEntityId()
	{
		return Optional.of(id);
	}

	@Override
	public Optional<EntityCondition> getEntityCondition()
	{
		return Optional.of(EntityCondition.from(CoreAttributes.pk(), id));
	}

}
