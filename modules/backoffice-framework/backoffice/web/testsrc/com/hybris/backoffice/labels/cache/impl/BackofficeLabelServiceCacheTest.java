/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.labels.cache.impl;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.platform.regioncache.CacheController;

import java.util.function.Supplier;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.backoffice.cache.ObjectCacheKeyGenerator;


@RunWith(MockitoJUnitRunner.class)
public class BackofficeLabelServiceCacheTest
{

	@InjectMocks
	private BackofficeLabelServiceCache backofficeLabelServiceCache;

	@Mock
	private CacheController cacheController;

	@Mock
	private ObjectCacheKeyGenerator objectCacheKeyGenerator;

	private final Supplier<String> defaultValue = () -> "Hi";

	private Object objectKey = new Object();

	@Before
	public void setUp()
	{
		when(objectCacheKeyGenerator.computeKey(any())).thenReturn(objectKey);
	}

	@Test
	public void testGetObjectLabel()
	{
		final Object object = new Object();
		backofficeLabelServiceCache.getObjectLabel(object, defaultValue);
		verify(cacheController).getWithLoader(any(), any());
		verify(objectCacheKeyGenerator).createCacheKey(eq(BackofficeLabelServiceCache.CACHED_TYPE_LABEL), eq(objectKey));
	}

	@Test
	public void testGetShortObjectLabel()
	{
		backofficeLabelServiceCache.getShortObjectLabel(new Object(), defaultValue);
		verify(cacheController).getWithLoader(any(), any());
		verify(objectCacheKeyGenerator).createCacheKey(eq(BackofficeLabelServiceCache.CACHED_TYPE_SHORT_LABEL), eq(objectKey));
	}

	@Test
	public void testGetObjectDescription()
	{
		backofficeLabelServiceCache.getObjectDescription(new Object(), defaultValue);
		verify(cacheController).getWithLoader(any(), any());
		verify(objectCacheKeyGenerator).createCacheKey(eq(BackofficeLabelServiceCache.CACHED_TYPE_DESCRIPTION), eq(objectKey));
	}

	@Test
	public void testGetObjectIconPath()
	{
		backofficeLabelServiceCache.getObjectIconPath(new Object(), defaultValue);
		verify(cacheController).getWithLoader(any(), any());
		verify(objectCacheKeyGenerator).createCacheKey(eq(BackofficeLabelServiceCache.CACHED_TYPE_ICON_PATH), eq(objectKey));
	}

}
