/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package ydocumentcartpackage.persistence.polyglot.repository.documentcart.storage;

import de.hybris.platform.persistence.polyglot.model.Identity;
import ydocumentcartpackage.persistence.polyglot.repository.documentcart.Document;
import ydocumentcartpackage.persistence.polyglot.repository.documentcart.Query;
import ydocumentcartpackage.persistence.polyglot.repository.documentcart.QueryResult;
import ydocumentcartpackage.persistence.polyglot.repository.documentcart.Storage;
import ydocumentcartpackage.persistence.polyglot.repository.documentcart.query.BaseQuery;
import ydocumentcartpackage.persistence.polyglot.repository.documentcart.query.EntityCondition;
import ydocumentcartpackage.persistence.polyglot.repository.documentcart.storage.util.StorageUtils;

import java.util.Optional;


public abstract class BaseStorage implements Storage
{

	@Override
	public QueryResult find(final Query query)
	{
		final BaseQuery abstractQuery = StorageUtils.requireBaseQuery(query);

		if (abstractQuery.isKnownThereIsNoResult())
		{
			return QueryResult.empty();
		}
		final Optional<Document> document = abstractQuery.getKnownDocument();

		if (document.isPresent())
		{
			return singleDocument(document.get());
		}

		if (abstractQuery.isDocumentInstantiation())
		{
			final Identity rootId = abstractQuery.getRootId().orElseThrow(this::missingRootIdForInstantiation);
			return singleDocument(instantiateNewDocument(rootId));
		}

		final Optional<Identity> rootId = abstractQuery.getRootId();
		if (rootId.isPresent())
		{
			return findByRootId(rootId.get()).requireAtMostOneDocument();
		}

		final Optional<EntityCondition> rootUniqueCondition = abstractQuery.getUniqueRootCondition();
		if (rootUniqueCondition.isPresent())
		{
			return findByRootAttributes(rootUniqueCondition.get()).requireAtMostOneDocument();
		}

		final Optional<Identity> entityId = abstractQuery.getEntityId();
		if (entityId.isPresent())
		{
			return findByEntityId(entityId.get()).requireAtMostOneDocument();
		}

		final Optional<EntityCondition> rootCondition = abstractQuery.getRootCondition();
		if (rootCondition.isPresent())
		{
			return findByRootAttributes(rootCondition.get());
		}

		final Optional<EntityCondition> entityCondition = abstractQuery.getEntityCondition();
		if (entityCondition.isPresent())
		{
			return findByEntityAttributes(entityCondition.get());
		}

		return handleUnknownQuery(query);
	}

	protected abstract Document instantiateNewDocument(Identity rootId);

	protected abstract QueryResult findByRootId(Identity identity);

	protected abstract QueryResult findByRootAttributes(EntityCondition condition);

	protected abstract QueryResult findByEntityId(Identity identity);

	protected abstract QueryResult findByEntityAttributes(EntityCondition condition);

	protected QueryResult singleDocument(final Document document)
	{
		return QueryResult.from(document);
	}

	protected QueryResult handleUnknownQuery(final Query query)
	{
		throw new UnsupportedOperationException("'" + query + "' is not supported.");
	}

	protected IllegalStateException missingRootIdForInstantiation()
	{
		return new IllegalStateException("rootId must be provided for document instantiation.");
	}
}
