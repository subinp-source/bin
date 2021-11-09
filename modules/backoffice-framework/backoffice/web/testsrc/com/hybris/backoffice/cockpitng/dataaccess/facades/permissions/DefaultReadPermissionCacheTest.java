/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.cockpitng.dataaccess.facades.permissions;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.platform.regioncache.CacheController;
import de.hybris.platform.servicelayer.security.permissions.PermissionCRUDService;

import org.junit.Before;
import org.junit.Test;

import com.hybris.backoffice.cache.ObjectCacheKeyGenerator;


public class DefaultReadPermissionCacheTest
{

	private DefaultReadPermissionCache defaultReadPermissionCache;

	private PermissionCRUDService permissionCRUDService;

	private CacheController cacheController;

	private ObjectCacheKeyGenerator objectCacheKeyGenerator;

	private Object objectKey = new Object();

	@Before
	public void setUp()
	{
		permissionCRUDService = mock(PermissionCRUDService.class);
		when(permissionCRUDService.canReadType("Product")).thenReturn(true);
		when(permissionCRUDService.canReadAttribute("Product", "name")).thenReturn(true);
		cacheController = mock(CacheController.class);
		when(cacheController.getWithLoader(any(), any())).thenReturn(true);
		objectCacheKeyGenerator = mock(ObjectCacheKeyGenerator.class);
		when(objectCacheKeyGenerator.computeKey(any())).thenReturn(objectKey);

		defaultReadPermissionCache = new DefaultReadPermissionCache(permissionCRUDService);
		defaultReadPermissionCache.setCacheController(cacheController);
		defaultReadPermissionCache.setObjectCacheKeyGenerator(objectCacheKeyGenerator);
	}

	@Test
	public void testCanReadType()
	{
		defaultReadPermissionCache.canReadType("Product");
		verify(cacheController).getWithLoader(any(), any());
		verify(objectCacheKeyGenerator).createCacheKey(eq(defaultReadPermissionCache.CACHED_TYPE_TYPE_READ_PERMISSION),
				eq(objectKey));
	}

	@Test
	public void testCanReadAttribute()
	{
		defaultReadPermissionCache.canReadAttribute("Product", "name");
		verify(cacheController).getWithLoader(any(), any());
		verify(objectCacheKeyGenerator).createCacheKey(eq(defaultReadPermissionCache.CACHED_TYPE_TYPE_ATTRIBUTES_READ_PERMISSION),
				eq(objectKey));
	}
}
