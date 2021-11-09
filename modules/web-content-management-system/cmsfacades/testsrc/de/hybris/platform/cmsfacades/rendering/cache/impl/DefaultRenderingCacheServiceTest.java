/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.rendering.cache.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cmsfacades.data.AbstractPageData;
import de.hybris.platform.cmsfacades.rendering.cache.RenderingCacheKeyProvider;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.regioncache.CacheController;
import de.hybris.platform.regioncache.key.CacheKey;

import java.io.Serializable;
import java.util.Optional;
import java.util.function.Supplier;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultRenderingCacheServiceTest
{
	@InjectMocks
	@Spy
	private DefaultRenderingCacheService<Serializable> defaultRenderingCacheService;

	@Mock
	private CacheController cacheController;

	@Mock
	private RenderingCacheKeyProvider<ItemModel> cacheKeyProvider;

	@Mock
	private Supplier<Serializable> converter;

	@Mock
	private CacheKey key;

	@Mock
	private CMSItemModel item;

	@Mock
	private AbstractPageData itemData;

	@Before
	public void setup()
	{
		doReturn(Boolean.TRUE).when(defaultRenderingCacheService).cacheEnabled();
	}

	@Test
	public void shouldNotReturnItemFromCacheIfCacheDisabled()
	{
		// GIVEN
		doReturn(Boolean.FALSE).when(defaultRenderingCacheService).cacheEnabled();

		// WHEN
		final Optional<Serializable> optionalItem = defaultRenderingCacheService.get(key);

		// THEN
		assertTrue(optionalItem.isEmpty());
		verifyZeroInteractions(cacheController);
	}

	@Test
	public void shouldNotPutItemInCacheIfCacheDisabled()
	{
		// GIVEN
		doReturn(Boolean.FALSE).when(defaultRenderingCacheService).cacheEnabled();

		// WHEN
		defaultRenderingCacheService.put(key, item);

		// THEN
		verifyZeroInteractions(cacheController);
	}

	@Test
	public void shouldNotReturnCacheKeyIfCacheDisabled()
	{
		// GIVEN
		doReturn(Boolean.FALSE).when(defaultRenderingCacheService).cacheEnabled();

		// WHEN
		final Optional<CacheKey> key = defaultRenderingCacheService.getKey(item);

		// THEN
		assertTrue(key.isEmpty());
		verifyZeroInteractions(cacheKeyProvider);
	}

	@Test
	public void shouldCallConverterIfCacheDisabled()
	{
		// GIVEN
		doReturn(Boolean.FALSE).when(defaultRenderingCacheService).cacheEnabled();

		// WHEN
		final Serializable notCachedItem = defaultRenderingCacheService.cacheOrElse(item, converter);

		// THEN
		verify(converter).get();
	}

	@Test
	public void shouldCallConverterAndPutElementToCacheIfItemNotInCache()
	{
		// GIVEN
		when(cacheKeyProvider.getKey(item)).thenReturn(Optional.of(key));
		when(cacheController.get(key)).thenReturn(null);
		when(converter.get()).thenReturn(itemData);

		// WHEN
		defaultRenderingCacheService.cacheOrElse(item, converter);

		// THEN
		verify(converter).get();
		verify(defaultRenderingCacheService).put(key, itemData);
	}

	@Test
	public void shouldReturnElementFromCacheIfItemCached()
	{
		// GIVEN
		when(cacheKeyProvider.getKey(item)).thenReturn(Optional.of(key));
		when(cacheController.get(key)).thenReturn(itemData);

		// WHEN
		final Serializable serializable = defaultRenderingCacheService.cacheOrElse(item, converter);

		// THEN
		assertNotNull(serializable);
		verifyZeroInteractions(converter);
	}

	@Test
	public void shouldPutElementInCacheIfCacheEnabled()
	{
		// WHEN
		defaultRenderingCacheService.put(key, itemData);

		// THEN
		verify(cacheController).getWithLoader(any(), any());
	}

	@Test
	public void shouldReturnCacheKeyIfProviderAvailableAndCacheEnabled()
	{
		// GIVEN
		when(cacheKeyProvider.getKey(item)).thenReturn(Optional.of(key));

		// WHEN
		final Optional<CacheKey> key = defaultRenderingCacheService.getKey(item);

		// THEN
		verify(cacheKeyProvider).getKey(item);
		assertTrue(key.isPresent());
	}
}
