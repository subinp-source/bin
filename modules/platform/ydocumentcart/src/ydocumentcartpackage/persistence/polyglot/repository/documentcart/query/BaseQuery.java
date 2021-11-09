/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package ydocumentcartpackage.persistence.polyglot.repository.documentcart.query;

import ydocumentcartpackage.persistence.polyglot.repository.documentcart.Document;
import ydocumentcartpackage.persistence.polyglot.repository.documentcart.Query;
import de.hybris.platform.persistence.polyglot.model.Identity;

import java.util.Optional;


public interface BaseQuery extends Query
{
	default boolean isKnownThereIsNoResult()
	{
		return false;
	}

	default Optional<Document> getKnownDocument()
	{
		return Optional.empty();
	}

	default boolean isDocumentInstantiation()
	{
		return false;
	}

	default Optional<Identity> getRootId()
	{
		return Optional.empty();
	}

	default Optional<EntityCondition> getUniqueRootCondition()
	{
		return Optional.empty();
	}

	default Optional<Identity> getEntityId()
	{
		return Optional.empty();
	}

	default Optional<EntityCondition> getRootCondition()
	{
		return Optional.empty();
	}

	default Optional<EntityCondition> getEntityCondition()
	{
		return Optional.empty();
	}
}
