/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package ydocumentcartpackage.persistence.polyglot.repository.documentcart.storage.cache;

import de.hybris.platform.persistence.polyglot.model.Identity;

import java.util.Optional;

import ydocumentcartpackage.persistence.polyglot.repository.documentcart.Document;
import ydocumentcartpackage.persistence.polyglot.repository.documentcart.Query;
import ydocumentcartpackage.persistence.polyglot.repository.documentcart.QueryResult;
import ydocumentcartpackage.persistence.polyglot.repository.documentcart.Storage;

public abstract class BaseStorageCache implements StorageCache
{

	@Override
	public QueryResult findInCache(final Query baseQuery, final LoadFromStorageStrategy targetFunction)
	{
		if (isCacheActive())
		{
			final Optional<QueryResult> cachedResult = findInCache(baseQuery);

			if (cachedResult.isPresent())
			{
				return cachedResult.get();
			}
		}

		final QueryResult queryResult = findInStorage(baseQuery, targetFunction);

		cacheDocuments(queryResult);

		return queryResult;
	}

	protected abstract void cacheDocuments(final QueryResult queryResult);

	protected abstract boolean isCacheActive();

	protected abstract QueryResult findInStorage(final Query baseQuery, final LoadFromStorageStrategy targetFunction);

	/**
	 * Method to find a query result in cache. Is called only if {@see #isCacheActive} returns <code>true</code>
	 *
	 * @param query the query used to find documents
	 * @return <code>Optional.empty()</code> if no documents has been found in cache that meets the query, otherwise an optional
	 * of {@see QueryResult}
	 */
	protected abstract Optional<QueryResult> findInCache(Query query);

	@Override
	public CacheContext initCacheContext(final Storage storage)
	{
		return createCacheContext(storage, getFlushAction());
	}

	protected abstract CacheFlushAction getFlushAction();

	protected abstract CacheContext createCacheContext(Storage storage,
	                                                   CacheFlushAction flushAction);

	protected interface CacheFlushAction
	{
		void flushCacheEntry(Storage storage, Identity identity, DocumentCacheEntry entry);
	}

	static class DocumentCacheEntry
	{
		private final Document document;
		private boolean removed;
		private boolean dirty;

		DocumentCacheEntry(final Document document)
		{
			this(document, false);
		}

		DocumentCacheEntry(final Document document, final boolean dirty)
		{
			this.document = document;
			this.dirty = dirty;
		}

		void markAsRemoved()
		{
			removed = true;
			dirty = true;
		}

		Document getDocument()
		{
			return document;
		}

		boolean isRemoved()
		{
			return removed;
		}

		boolean isDirty()
		{
			return dirty;
		}
	}
}
