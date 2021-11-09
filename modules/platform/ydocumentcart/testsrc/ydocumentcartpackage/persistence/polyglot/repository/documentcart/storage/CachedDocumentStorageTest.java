/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package ydocumentcartpackage.persistence.polyglot.repository.documentcart.storage;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import ydocumentcartpackage.persistence.polyglot.repository.documentcart.Document;
import ydocumentcartpackage.persistence.polyglot.repository.documentcart.Query;
import ydocumentcartpackage.persistence.polyglot.repository.documentcart.QueryResult;
import ydocumentcartpackage.persistence.polyglot.repository.documentcart.Storage;
import ydocumentcartpackage.persistence.polyglot.repository.documentcart.storage.cache.BaseStorageCache;
import ydocumentcartpackage.persistence.polyglot.repository.documentcart.storage.cache.StorageCache;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@UnitTest
public class CachedDocumentStorageTest
{

	@Mock
	Storage targetStorage;

	@Mock
	StorageCache storageCache;

	private CachedDocumentStorage cachedDocumentStorage;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		cachedDocumentStorage = new CachedDocumentStorage(storageCache, targetStorage);
	}

	@Test
	public void shouldThrowExceptionWhenNoStorageCacheIsDefined()
	{
		assertThatThrownBy(() -> new CachedDocumentStorage(null, targetStorage)).isNotNull()
		                                                                        .isInstanceOf(NullPointerException.class);
	}

	@Test
	public void shouldThrowExceptionWhenNoTargetStorageIsDefined()
	{
		assertThatThrownBy(() -> new CachedDocumentStorage(storageCache, null)).isNotNull()
		                                                                       .isInstanceOf(NullPointerException.class);
	}

	@Test
	public void shouldSearchInStorageWhenNoDocumentInCache()
	{

		final StorageCache cache = new TestStorageCache();
		final Storage storage = new CachedDocumentStorage(cache, targetStorage);

		final Query query = mock(Query.class);

		storage.find(query);

		verify(targetStorage).find(query);
	}

	@Test
	public void shouldNotSaveInStorageIfSaveInCacheSucceeded()
	{
		when(storageCache.save(any())).thenReturn(true);
		final Document document = mock(Document.class);

		cachedDocumentStorage.save(document);

		verify(targetStorage, never()).save(any());
	}

	@Test
	public void shouldSaveInStorageIfSaveInCacheFails()
	{
		when(storageCache.save(any())).thenReturn(false);
		final Document document = mock(Document.class);

		cachedDocumentStorage.save(document);

		verify(targetStorage).save(document);
	}

	@Test
	public void shouldNotRemoveInStorageIfRemoveInCacheSucceeded()
	{
		when(storageCache.remove(any())).thenReturn(true);
		final Document document = mock(Document.class);

		cachedDocumentStorage.remove(document);

		verify(targetStorage, never()).remove(any());
	}

	@Test
	public void shouldRemoveInStorageIfRemoveInCacheFails()
	{
		when(storageCache.remove(any())).thenReturn(false);
		final Document document = mock(Document.class);

		cachedDocumentStorage.remove(document);

		verify(targetStorage).remove(document);
	}

	@Test
	public void shouldInitCacheContext()
	{
		cachedDocumentStorage.initCacheContext();

		verify(storageCache).initCacheContext(targetStorage);
	}

	class TestStorageCache extends BaseStorageCache
	{

		@Override
		protected void cacheDocuments(final QueryResult queryResult)
		{

		}

		@Override
		protected boolean isCacheActive()
		{
			return true;
		}

		@Override
		protected QueryResult findInStorage(final Query baseQuery, final LoadFromStorageStrategy targetFunction)
		{
			return targetFunction.loadFromStorage(baseQuery);
		}

		@Override
		protected Optional<QueryResult> findInCache(final Query query)
		{
			return Optional.empty();
		}

		@Override
		protected CacheFlushAction getFlushAction()
		{
			return null;
		}

		@Override
		protected CacheContext createCacheContext(final Storage storage, final CacheFlushAction flushAction)
		{
			return null;
		}

		@Override
		public boolean remove(final Document document)
		{
			return false;
		}

		@Override
		public boolean save(final Document document)
		{
			return false;
		}
	}
}
