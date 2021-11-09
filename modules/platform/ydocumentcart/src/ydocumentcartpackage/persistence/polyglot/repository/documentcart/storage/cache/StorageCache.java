/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package ydocumentcartpackage.persistence.polyglot.repository.documentcart.storage.cache;

import ydocumentcartpackage.persistence.polyglot.repository.documentcart.Document;
import ydocumentcartpackage.persistence.polyglot.repository.documentcart.Query;
import ydocumentcartpackage.persistence.polyglot.repository.documentcart.QueryResult;
import ydocumentcartpackage.persistence.polyglot.repository.documentcart.Storage;

public interface StorageCache
{
	QueryResult findInCache(Query baseQuery, LoadFromStorageStrategy loadFromStorageStrategy);

	/**
	 * @param document document to be removed
	 * @return <code>true</code> if document has been marked for removal in cache. The removal will be done in storage when
	 * the {@see CacheContext} is closed. Otherwise, returns <code>false</code> and document should be removed from storage
	 * manually.
	 */
	boolean remove(Document document);

	/**
	 * @param document document to be saved
	 * @return <code>true</code> if document has been stored in cache. The save of document in storage will be done when
	 * the {@see CacheContext} is closed. Otherwise, returns <code>false</code> and document should be saved in storage manually.
	 */
	boolean save(Document document);

	CacheContext initCacheContext(Storage storage);

	interface CacheContext extends AutoCloseable
	{
		void markAsSuccess();

		@Override
		void close();
	}

	interface LoadFromStorageStrategy
	{
		QueryResult loadFromStorage(Query query);
	}
}
