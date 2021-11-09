/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package ydocumentcartpackage.persistence.polyglot.repository.documentcart.storage.cache;

import ydocumentcartpackage.persistence.polyglot.repository.documentcart.query.BaseQuery;
import ydocumentcartpackage.persistence.polyglot.repository.documentcart.query.EntityCondition;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.same;

import org.mockito.Mockito;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import ydocumentcartpackage.persistence.polyglot.repository.documentcart.Document;
import ydocumentcartpackage.persistence.polyglot.repository.documentcart.Entity;
import ydocumentcartpackage.persistence.polyglot.repository.documentcart.Query;
import ydocumentcartpackage.persistence.polyglot.repository.documentcart.QueryResult;
import ydocumentcartpackage.persistence.polyglot.repository.documentcart.Storage;

import de.hybris.platform.persistence.polyglot.PolyglotPersistence;
import de.hybris.platform.persistence.polyglot.model.Identity;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@UnitTest
public class ThreadLocalDocumentCacheTest
{
	@Mock
	Document document;

	@Mock
	Identity identity;


	@Mock
	Identity matchingEntityId;


	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		when(document.getRootId()).thenReturn(identity);

		final Entity rootEntity = mock(Entity.class);
		when(document.getRootEntity()).thenReturn(rootEntity);
		when(document.getEntityIds()).thenReturn(Set.of(matchingEntityId));
		when(document.containsEntity(same(matchingEntityId))).thenReturn(true);
	}

	@Test
	public void shouldReturnQueryResultFromLoadFromStorageStrategy()
	{

		final Query query = mock(Query.class);
		final QueryResult storageResult = QueryResult.empty();

		final StorageCache.LoadFromStorageStrategy loadFromStorageStrategy = spy(new SingleResultStorageStrategy(storageResult));

		final QueryResult cachedResult = new ThreadLocalDocumentCache().findInCache(query, loadFromStorageStrategy);

		assertThat(cachedResult).isEqualTo(storageResult);
		verify(loadFromStorageStrategy).loadFromStorage(query);
	}


	@Test
	public void shouldNotQueryCacheWhenNoContextIsActive()
	{

		final Query query = mock(BaseQuery.class);
		final QueryResult storageResult = QueryResult.empty();

		final SingleResultStorageStrategy loadFromStorageStrategy = spy(new SingleResultStorageStrategy(storageResult));

		final ThreadLocalDocumentCache threadLocalDocumentCache = Mockito.spy(new ThreadLocalDocumentCache());
		final QueryResult cachedResult = threadLocalDocumentCache.findInCache(query, loadFromStorageStrategy);

		assertThat(cachedResult).isEqualTo(loadFromStorageStrategy.storageResult);
		verify(loadFromStorageStrategy).loadFromStorage(query);
		verify(threadLocalDocumentCache, never()).findInCache(any(Query.class));
	}


	@Test
	public void shouldQueryCacheWhenContextIsActive()
	{
		final BaseQuery query = getQueryWithRootId();

		final QueryResult storageResult = QueryResult.empty();
		final Storage storage = mock(Storage.class);

		final SingleResultStorageStrategy loadFromStorageStrategy = spy(new SingleResultStorageStrategy(storageResult));
		final ThreadLocalDocumentCache threadLocalDocumentCache = Mockito.spy(new ThreadLocalDocumentCache());

		final QueryResult cachedResult;

		final StorageCache.CacheContext context = spy(threadLocalDocumentCache.initCacheContext(storage));
		try (context)
		{
			cachedResult = threadLocalDocumentCache.findInCache(query, loadFromStorageStrategy);
		}

		assertThat(cachedResult.stream()).isEmpty();
		assertThat(cachedResult).isEqualTo(loadFromStorageStrategy.storageResult);
		verify(loadFromStorageStrategy).loadFromStorage(query);
		verify(threadLocalDocumentCache).findInCache(query);
		verify(context).close();
	}

	@Test
	public void shouldQueryCacheWhenContextIsActiveAndQueryIsUsingMatchingRootConditions()
	{
		final BaseQuery query = getQueryWithMatchingRootCondition();

		final QueryResult storageResult = QueryResult.empty();
		final Storage storage = mock(Storage.class);

		final SingleResultStorageStrategy loadFromStorageStrategy = spy(new SingleResultStorageStrategy(storageResult));
		final ThreadLocalDocumentCache threadLocalDocumentCache = Mockito.spy(new ThreadLocalDocumentCache());

		final QueryResult cachedResult;

		final StorageCache.CacheContext context = spy(threadLocalDocumentCache.initCacheContext(storage));
		try (context)
		{
			//fill the cache with the document
			threadLocalDocumentCache.save(document);

			cachedResult = threadLocalDocumentCache.findInCache(query, loadFromStorageStrategy);
		}

		assertThat(cachedResult.stream()).hasSize(1).contains(document);

		//should not ask the storage since the document has been cached and condition is matching
		verify(loadFromStorageStrategy, never()).loadFromStorage(query);
		verify(threadLocalDocumentCache).findInCache(query);
		verify(context).close();
	}


	@Test
	public void shouldQueryCacheWhenContextIsActiveAndQueryIsUsingNotMatchingRootConditions()
	{
		final BaseQuery query = getQueryWithNotMatchingRootCondition();

		final QueryResult storageResult = QueryResult.empty();
		final Storage storage = mock(Storage.class);

		final SingleResultStorageStrategy loadFromStorageStrategy = spy(new SingleResultStorageStrategy(storageResult));
		final ThreadLocalDocumentCache threadLocalDocumentCache = Mockito.spy(new ThreadLocalDocumentCache());

		final QueryResult cachedResult;

		final StorageCache.CacheContext context = spy(threadLocalDocumentCache.initCacheContext(storage));
		try (context)
		{
			//fill the cache with the document
			threadLocalDocumentCache.save(document);

			cachedResult = threadLocalDocumentCache.findInCache(query, loadFromStorageStrategy);
		}

		assertThat(cachedResult.stream()).isEmpty();

		//should ask the storage since the condition is not matching
		verify(loadFromStorageStrategy).loadFromStorage(query);
		verify(threadLocalDocumentCache).findInCache(query);
		verify(context).close();
	}

	@Test
	public void shouldQueryCacheWhenContextIsActiveAndQueryIsUsingMatchingEntityId()
	{
		final BaseQuery query = getQueryWithMatchingEntityId();

		final QueryResult storageResult = QueryResult.empty();
		final Storage storage = mock(Storage.class);

		final SingleResultStorageStrategy loadFromStorageStrategy = spy(new SingleResultStorageStrategy(storageResult));
		final ThreadLocalDocumentCache threadLocalDocumentCache = Mockito.spy(new ThreadLocalDocumentCache());

		final QueryResult cachedResult;

		final StorageCache.CacheContext context = spy(threadLocalDocumentCache.initCacheContext(storage));
		try (context)
		{
			//fill the cache with the document
			threadLocalDocumentCache.save(document);

			cachedResult = threadLocalDocumentCache.findInCache(query, loadFromStorageStrategy);
		}

		assertThat(cachedResult.stream()).hasSize(1).contains(document);

		//should not ask the storage since the document has been cached and condition is matching
		verify(loadFromStorageStrategy, never()).loadFromStorage(query);
		verify(threadLocalDocumentCache).findInCache(query);
		verify(context).close();
	}


	@Test
	public void shouldQueryCacheWhenContextIsActiveAndQueryIsUsingNotMatchingEntityId()
	{
		final BaseQuery query = getQueryWithNotMatchingEntityId();

		final QueryResult storageResult = QueryResult.empty();
		final Storage storage = mock(Storage.class);

		final SingleResultStorageStrategy loadFromStorageStrategy = spy(new SingleResultStorageStrategy(storageResult));
		final ThreadLocalDocumentCache threadLocalDocumentCache = Mockito.spy(new ThreadLocalDocumentCache());

		final QueryResult cachedResult;

		final StorageCache.CacheContext context = spy(threadLocalDocumentCache.initCacheContext(storage));
		try (context)
		{
			//fill the cache with the document
			threadLocalDocumentCache.save(document);

			cachedResult = threadLocalDocumentCache.findInCache(query, loadFromStorageStrategy);
		}

		assertThat(cachedResult.stream()).isEmpty();

		//should ask the storage since the condition is not matching
		verify(loadFromStorageStrategy).loadFromStorage(query);
		verify(threadLocalDocumentCache).findInCache(query);
		verify(context).close();
	}

	@Test
	public void shouldReturnEmptyResultWhenContextIsActiveAndQueryForRemovedDocumentAndUsingRootId()
	{
		final BaseQuery query = getQueryWithRootId();

		final Storage storage = mock(Storage.class);
		final ThreadLocalDocumentCache threadLocalDocumentCache = Mockito.spy(new ThreadLocalDocumentCache());

		final Optional<QueryResult> cachedResult;
		final StorageCache.CacheContext context = spy(threadLocalDocumentCache.initCacheContext(storage));
		try (context)
		{
			//fill the cache with the document
			threadLocalDocumentCache.remove(document);

			cachedResult = threadLocalDocumentCache.findInCache(query);
		}

		assertThat(cachedResult).isPresent();
		assertThat(cachedResult.stream().flatMap(QueryResult::stream)).isEmpty();
	}

	@Test
	public void shouldReturnEmptyResultWhenContextIsActiveAndQueryForRemovedDocumentAndUsingMatchingRootCondition()
	{
		final BaseQuery query = getQueryWithMatchingRootCondition();
		final Storage storage = mock(Storage.class);

		final ThreadLocalDocumentCache threadLocalDocumentCache = Mockito.spy(new ThreadLocalDocumentCache());

		final Optional<QueryResult> cachedResult;
		final StorageCache.CacheContext context = spy(threadLocalDocumentCache.initCacheContext(storage));
		try (context)
		{
			//fill the cache with the document
			threadLocalDocumentCache.remove(document);

			cachedResult = threadLocalDocumentCache.findInCache(query);
		}

		assertThat(cachedResult).isPresent();
		assertThat(cachedResult.stream().flatMap(QueryResult::stream)).isEmpty();
	}

	@Test
	public void shouldReturnEmptyResultWhenContextIsActiveAndQueryForRemovedDocumentAndUsingMatchingEntityId()
	{
		final BaseQuery query = getQueryWithMatchingEntityId();
		final Storage storage = mock(Storage.class);

		final ThreadLocalDocumentCache threadLocalDocumentCache = Mockito.spy(new ThreadLocalDocumentCache());

		final Optional<QueryResult> cachedResult;
		final StorageCache.CacheContext context = spy(threadLocalDocumentCache.initCacheContext(storage));
		try (context)
		{
			//fill the cache with the document
			threadLocalDocumentCache.remove(document);

			cachedResult = threadLocalDocumentCache.findInCache(query);
		}

		assertThat(cachedResult).isPresent();
		assertThat(cachedResult.stream().flatMap(QueryResult::stream)).isEmpty();
	}

	@Test
	public void shouldSaveDocumentsInStorageWhenClosingContextAndMarkedAsSuccessful()
	{
		final ThreadLocalDocumentCache threadLocalDocumentCache = Mockito.spy(new ThreadLocalDocumentCache());

		final Storage storage = mock(Storage.class);
		final StorageCache.CacheContext context = spy(threadLocalDocumentCache.initCacheContext(storage));
		final boolean isSaved;
		try (context)
		{
			isSaved = threadLocalDocumentCache.save(document);
			context.markAsSuccess();
		}

		assertThat(isSaved).isTrue();
		verify(context).close();
		verify(storage).save(document);
	}

	@Test
	public void shouldRemoveDocumentsInStorageWhenClosingContextAndMarkedAsSuccessful()
	{
		final ThreadLocalDocumentCache threadLocalDocumentCache = Mockito.spy(new ThreadLocalDocumentCache());

		final Storage storage = mock(Storage.class);
		final StorageCache.CacheContext context = spy(threadLocalDocumentCache.initCacheContext(storage));
		final boolean isSaved;
		try (context)
		{
			isSaved = threadLocalDocumentCache.remove(document);
			context.markAsSuccess();
		}

		assertThat(isSaved).isTrue();
		verify(context).close();
		verify(storage).remove(document);
	}

	@Test
	public void shouldNotSaveDocumentsInCacheWhenClosingContextAndNotMarkedAsSuccessful()
	{
		final BaseQuery query = getQueryWithRootId();

		final QueryResult storageResult = QueryResult.from(document);
		final Storage storage = mock(Storage.class);

		final SingleResultStorageStrategy loadFromStorageStrategy = spy(new SingleResultStorageStrategy(storageResult));
		final ThreadLocalDocumentCache threadLocalDocumentCache = Mockito.spy(new ThreadLocalDocumentCache());

		final QueryResult cachedResult;

		final StorageCache.CacheContext context = spy(threadLocalDocumentCache.initCacheContext(storage));
		try (context)
		{
			cachedResult = threadLocalDocumentCache.findInCache(query, loadFromStorageStrategy);
			//no marking as successful
		}

		assertThat(cachedResult.stream()).isNotEmpty();
		assertThat(cachedResult).isEqualTo(loadFromStorageStrategy.storageResult);
		verify(context).close();
		verify(storage, never()).save(any());
	}

	@Test
	public void shouldReturnDocumentFromCacheWhenAskingInSameContext()
	{
		final BaseQuery query = getQueryWithRootId();

		final Storage storage = mock(Storage.class);
		final QueryResult storageResult = QueryResult.from(document);
		final SingleResultStorageStrategy loadFromStorageStrategy = spy(new SingleResultStorageStrategy(storageResult));
		final ThreadLocalDocumentCache threadLocalDocumentCache = Mockito.spy(new ThreadLocalDocumentCache());

		final QueryResult cachedResult1;
		final QueryResult cachedResult2;

		final StorageCache.CacheContext context = spy(threadLocalDocumentCache.initCacheContext(storage));
		try (context)
		{
			cachedResult1 = threadLocalDocumentCache.findInCache(query, loadFromStorageStrategy);
			cachedResult2 = threadLocalDocumentCache.findInCache(query, loadFromStorageStrategy);
		}

		assertThat(cachedResult1.stream()).isNotEmpty();
		assertThat(cachedResult2.stream()).isNotEmpty();

		//first return should be same as from storage
		assertThat(cachedResult1).isEqualTo(loadFromStorageStrategy.storageResult);
		assertThat(cachedResult2).isNotEqualTo(cachedResult1);
		assertThat(cachedResult2.stream()).containsOnlyElementsOf(cachedResult1.stream().collect(Collectors.toList()));

		//is only once loaded from storage
		verify(loadFromStorageStrategy).loadFromStorage(query);
	}

	@Test
	public void shouldFlushOnceWhenContextIsActive()
	{
		final BaseQuery query = getQueryWithRootId();

		final Document document = mock(Document.class);
		when(document.getVersion()).thenReturn(1L);
		when(document.getRootId()).thenReturn(PolyglotPersistence.identityFromLong(1));
		final QueryResult storageResult = QueryResult.from(document);
		final Storage storage = mock(Storage.class);

		final StorageCache.LoadFromStorageStrategy loadFromStorageStrategy = spy(new SingleResultStorageStrategy(storageResult));
		final ThreadLocalDocumentCache threadLocalDocumentCache = Mockito.spy(new ThreadLocalDocumentCache());

		final QueryResult cachedResult;

		final StorageCache.CacheContext context = spy(threadLocalDocumentCache.initCacheContext(storage));
		try (context)
		{
			cachedResult = threadLocalDocumentCache.findInCache(query, loadFromStorageStrategy);
			context.markAsSuccess();
		}

		assertThat(cachedResult).isEqualTo(storageResult);
		verify(loadFromStorageStrategy).loadFromStorage(query);
		verify(threadLocalDocumentCache).findInCache(query);
		verify(threadLocalDocumentCache).createCacheContext(same(storage), any());
		verify(context).close();

		//should not flush the cache as no changes to document (save, remove) has been made
		verify(storage, never()).save(any());
		verify(storage, never()).remove(any());
	}


	@Test
	public void shouldSaveDocumentInCacheWhenContextIsActive()
	{
		final Storage storage = mock(Storage.class);

		final ThreadLocalDocumentCache threadLocalDocumentCache = Mockito.spy(new ThreadLocalDocumentCache());

		final StorageCache.CacheContext context = spy(threadLocalDocumentCache.initCacheContext(storage));
		final boolean isSaved;
		final Optional<QueryResult> cachedResult;
		try (context)
		{
			isSaved = threadLocalDocumentCache.save(document);

			cachedResult = threadLocalDocumentCache.findInCache(getQueryWithRootId());
		}

		assertThat(isSaved).isTrue();

		assertThat(cachedResult).isPresent();
		assertThat(cachedResult.stream().flatMap(QueryResult::stream)).hasSize(1).containsOnly(document);

		//no interaction with storage, because context has not been marked as successful
		verify(storage, never()).remove(any());
		verify(storage, never()).save(any());
	}

	@Test
	public void shouldNotSaveDocumentInCacheWhenContextIsNotActive()
	{
		final ThreadLocalDocumentCache threadLocalDocumentCache = Mockito.spy(new ThreadLocalDocumentCache());

		final boolean isSaved;
		final Optional<QueryResult> cachedResult;

		isSaved = threadLocalDocumentCache.save(document);
		cachedResult = threadLocalDocumentCache.findInCache(getQueryWithRootId());


		assertThat(isSaved).isFalse();

		assertThat(cachedResult).isEmpty();
	}


	@Test
	public void shouldRemoveDocumentInCacheWhenContextIsActive()
	{
		final Storage storage = mock(Storage.class);

		final ThreadLocalDocumentCache threadLocalDocumentCache = Mockito.spy(new ThreadLocalDocumentCache());

		final StorageCache.CacheContext context = spy(threadLocalDocumentCache.initCacheContext(storage));
		final boolean isRemoved;
		final Optional<QueryResult> cachedResult;
		try (context)
		{
			isRemoved = threadLocalDocumentCache.remove(document);

			cachedResult = threadLocalDocumentCache.findInCache(getQueryWithRootId());
		}

		assertThat(isRemoved).isTrue();

		assertThat(cachedResult).isPresent();
		assertThat(cachedResult.stream().flatMap(QueryResult::stream)).isEmpty();

		//no interaction with storage, because context has not been marked as successful
		verify(storage, never()).remove(any());
		verify(storage, never()).save(any());
	}

	@Test
	public void shouldNotRemoveDocumentInCacheWhenContextIsNotActive()
	{
		final ThreadLocalDocumentCache threadLocalDocumentCache = Mockito.spy(new ThreadLocalDocumentCache());

		final boolean isRemoved;
		final Optional<QueryResult> cachedResult;

		isRemoved = threadLocalDocumentCache.remove(document);
		cachedResult = threadLocalDocumentCache.findInCache(getQueryWithRootId());


		assertThat(isRemoved).isFalse();
		assertThat(cachedResult).isEmpty();
	}

	@Test
	public void shouldThrowExceptionWhenRemovingRemovedDocument()
	{
		final Storage storage = mock(Storage.class);

		final ThreadLocalDocumentCache threadLocalDocumentCache = Mockito.spy(new ThreadLocalDocumentCache());

		final StorageCache.CacheContext context = spy(threadLocalDocumentCache.initCacheContext(storage));
		final boolean isRemoved;
		final Optional<QueryResult> cachedResult;
		try (context)
		{
			isRemoved = threadLocalDocumentCache.remove(document);
			assertThatThrownBy(() -> threadLocalDocumentCache.remove(document)).isNotNull()
			                                                                   .isInstanceOf(IllegalStateException.class);
		}

		assertThat(isRemoved).isTrue();
	}

	private BaseQuery getQueryWithRootId()
	{
		final BaseQuery query = mock(BaseQuery.class);
		when(query.getRootId()).thenReturn(Optional.of(identity));
		when(query.getRootCondition()).thenReturn(Optional.empty());
		when(query.getEntityId()).thenReturn(Optional.empty());
		return query;
	}

	private BaseQuery getQueryWithNotMatchingRootCondition()
	{
		final EntityCondition entityCondition = mock(EntityCondition.class);
		when(entityCondition.getPredicate()).thenReturn(entity -> false);

		return getQueryWithRootCondition(entityCondition);
	}

	private BaseQuery getQueryWithMatchingRootCondition()
	{
		final EntityCondition entityCondition = mock(EntityCondition.class);
		when(entityCondition.getPredicate()).thenReturn(entity -> true);

		return getQueryWithRootCondition(entityCondition);
	}

	private BaseQuery getQueryWithRootCondition(final EntityCondition entityCondition)
	{
		final BaseQuery query = mock(BaseQuery.class);
		when(query.getRootId()).thenReturn(Optional.empty());
		when(query.getRootCondition()).thenReturn(Optional.of(entityCondition));
		when(query.getEntityId()).thenReturn(Optional.empty());
		return query;
	}

	private BaseQuery getQueryWithMatchingEntityId()
	{

		return getQueryWithEntityId(matchingEntityId);
	}

	private BaseQuery getQueryWithNotMatchingEntityId()
	{
		final Identity notMatchingEntityId = mock(Identity.class);

		return getQueryWithEntityId(notMatchingEntityId);
	}

	private BaseQuery getQueryWithEntityId(final Identity entityId)
	{
		final BaseQuery query = mock(BaseQuery.class);
		when(query.getRootId()).thenReturn(Optional.empty());
		when(query.getRootCondition()).thenReturn(Optional.empty());
		when(query.getEntityId()).thenReturn(Optional.of(entityId));
		return query;
	}

	public static class SingleResultStorageStrategy implements StorageCache.LoadFromStorageStrategy
	{

		private final QueryResult storageResult;

		SingleResultStorageStrategy(final QueryResult storageResult)
		{
			this.storageResult = storageResult;
		}

		@Override
		public QueryResult loadFromStorage(final Query query)
		{
			return storageResult;
		}
	}
}
