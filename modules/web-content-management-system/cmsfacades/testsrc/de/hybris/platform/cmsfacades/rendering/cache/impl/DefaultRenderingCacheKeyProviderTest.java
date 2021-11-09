/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.rendering.cache.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cmsfacades.rendering.cache.dto.RenderingCacheKey;
import de.hybris.platform.cmsfacades.rendering.cache.keyprovider.RenderingItemCacheKeyProvider;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.regioncache.key.CacheKey;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultRenderingCacheKeyProviderTest
{
	private static final String COMPONENT_CACHE_KEY = "ComponentCacheKey";
	private static final String PAGE_CACHE_KEY = "PageCacheKey";
	private static final String TENANT_ID = "TenantId";

	@InjectMocks
	@Spy
	private DefaultRenderingCacheKeyProvider defaultRenderingCacheKeyProvider;

	@Mock
	private TypeService typeService;

	@Mock
	private RenderingItemCacheKeyProvider componentRenderingItemCacheKeyProvider;
	@Mock
	private RenderingItemCacheKeyProvider pageRenderingItemCacheKeyProvider;

	private final Map<String, RenderingItemCacheKeyProvider<ItemModel>> cacheKeyProviders = new HashMap<>();

	@Mock
	private AbstractCMSComponentModel component;
	@Mock
	private ContentSlotModel slot;
	@Mock
	private AbstractPageModel page;


	@Before
	public void setup()
	{
		cacheKeyProviders.put(AbstractCMSComponentModel._TYPECODE, componentRenderingItemCacheKeyProvider);
		cacheKeyProviders.put(AbstractPageModel._TYPECODE, pageRenderingItemCacheKeyProvider);
		defaultRenderingCacheKeyProvider.setCacheKeyProviders(cacheKeyProviders);

		// tenant id
		doReturn(TENANT_ID).when(defaultRenderingCacheKeyProvider).getTenantId();

		// items
		when(component.getItemtype()).thenReturn(AbstractCMSComponentModel._TYPECODE);
		when(slot.getItemtype()).thenReturn(ContentSlotModel._TYPECODE);
		when(page.getItemtype()).thenReturn(AbstractPageModel._TYPECODE);

		// cache key providers
		when(componentRenderingItemCacheKeyProvider.getKey(component)).thenReturn(COMPONENT_CACHE_KEY);
		when(pageRenderingItemCacheKeyProvider.getKey(page)).thenReturn(PAGE_CACHE_KEY);


		// component
		when(typeService.isAssignableFrom(AbstractCMSComponentModel._TYPECODE, component.getItemtype())).thenReturn(Boolean.TRUE);
		when(typeService.isAssignableFrom(AbstractPageModel._TYPECODE, component.getItemtype())).thenReturn(Boolean.FALSE);
		// page
		when(typeService.isAssignableFrom(AbstractPageModel._TYPECODE, page.getItemtype())).thenReturn(Boolean.TRUE);
		when(typeService.isAssignableFrom(AbstractCMSComponentModel._TYPECODE, page.getItemtype())).thenReturn(Boolean.FALSE);
		// slot
		when(typeService.isAssignableFrom(AbstractCMSComponentModel._TYPECODE, slot.getItemtype())).thenReturn(Boolean.FALSE);
		when(typeService.isAssignableFrom(AbstractPageModel._TYPECODE, slot.getItemtype())).thenReturn(Boolean.FALSE);
	}

	@Test
	public void shouldReturnEmptyIfKeyCanNotBeGeneratedForProvidedItem()
	{
		// WHEN
		final Optional<CacheKey> key = defaultRenderingCacheKeyProvider.getKey(slot);

		// THEN
		assertTrue(key.isEmpty());
	}

	@Test
	public void shouldReturnCacheKeyForComponent()
	{
		// GIVEN
		final RenderingCacheKey renderingCacheKey = new RenderingCacheKey(COMPONENT_CACHE_KEY, TENANT_ID);
		// WHEN
		final Optional<CacheKey> key = defaultRenderingCacheKeyProvider.getKey(component);

		// THEN
		assertTrue(key.isPresent());
		assertEquals(renderingCacheKey, key.get());
	}

	@Test
	public void shouldReturnCacheKeyForPage()
	{
		// GIVEN
		final RenderingCacheKey renderingCacheKey = new RenderingCacheKey(PAGE_CACHE_KEY, TENANT_ID);

		// WHEN
		final Optional<CacheKey> key = defaultRenderingCacheKeyProvider.getKey(page);

		// THEN
		assertTrue(key.isPresent());
		assertEquals(renderingCacheKey, key.get());
	}
}
