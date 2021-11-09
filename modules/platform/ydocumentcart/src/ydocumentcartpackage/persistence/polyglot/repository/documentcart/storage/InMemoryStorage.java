/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package ydocumentcartpackage.persistence.polyglot.repository.documentcart.storage;

import de.hybris.platform.persistence.polyglot.model.Identity;
import de.hybris.platform.persistence.polyglot.view.ItemStateView;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import ydocumentcartpackage.persistence.polyglot.repository.documentcart.Document;
import ydocumentcartpackage.persistence.polyglot.repository.documentcart.DocumentConcurrentModificationException;
import ydocumentcartpackage.persistence.polyglot.repository.documentcart.Entity;
import ydocumentcartpackage.persistence.polyglot.repository.documentcart.QueryResult;
import ydocumentcartpackage.persistence.polyglot.repository.documentcart.query.EntityCondition;


public class InMemoryStorage extends BaseStorage
{
	private final ConcurrentHashMap<Identity, Document> documents = new ConcurrentHashMap<>();

	@Override
	public void save(final Document document)
	{
		final Identity id = document.getRootId();
		final Document current = documents.get(document.getRootId());

		if (current == null)
		{
			if (null != documents.putIfAbsent(id, document))
			{
				throw DocumentConcurrentModificationException.documentAlreadyExist(document);
			}
			return;
		}

		documents.put(id, document);
	}

	@Override
	public void remove(final Document document)
	{
		final Identity id = document.getRootId();
		documents.remove(id);
	}

	@Override
	protected Document instantiateNewDocument(final Identity id)
	{
		return new Document(id);
	}

	@Override
	protected QueryResult findByRootId(final Identity id)
	{
		return QueryResult.fromNullable(documents.get(id));
	}

	@Override
	protected QueryResult findByRootAttributes(final EntityCondition condition)
	{
		final Predicate<ItemStateView> rootEntityPredicate = condition.getPredicate();

		final List<Document> matchingDocuments = documents.values()
		                                                  .stream()
		                                                  .map(Document::getRootEntity)
		                                                  .filter(rootEntityPredicate)
		                                                  .map(Entity::getDocument)
		                                                  .collect(Collectors.toUnmodifiableList());

		return QueryResult.from(matchingDocuments);
	}

	@Override
	protected QueryResult findByEntityId(final Identity id)
	{
		final List<Document> matchingDocuments = documents.values().stream().flatMap(d -> d.getEntity(id).stream())
		                                                  .map(Entity::getDocument).collect(Collectors.toUnmodifiableList());

		return QueryResult.from(matchingDocuments);
	}

	@Override
	protected QueryResult findByEntityAttributes(final EntityCondition condition)
	{
		final Predicate<ItemStateView> entityPredicate = condition.getPredicate();

		final List<Document> matchingDocuments = documents.values()
		                                                  .stream()
		                                                  .flatMap(d -> d.allEntities())
		                                                  .filter(entityPredicate)
		                                                  .map(Entity::getDocument)
		                                                  .distinct()
		                                                  .collect(Collectors.toUnmodifiableList());

		return QueryResult.from(matchingDocuments);
	}
}
