/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.cache.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;


@RunWith(MockitoJUnitRunner.class)
public class ObjectCacheKeyTest
{

	private final String typeCode = "TestCode";
	private final String tenantId = "TestTenant";
	private final UUID context = UUID.randomUUID();
	private final Object objectKey = Lists.newArrayList("A", Locale.ENGLISH, context);

	private ObjectCacheKey objectCacheKey;

	@Before
	public void setUp()
	{
		objectCacheKey = new ObjectCacheKey(typeCode, objectKey, tenantId);
	}

	@Test
	public void testHashCode()
	{
		assertThat(objectCacheKey.hashCode()).isNotNull();
	}

	@Test
	public void testHashCodeIsEqual()
	{
		final ObjectCacheKey anotherCacheKey = new ObjectCacheKey(typeCode, objectKey, tenantId);
		assertThat(objectCacheKey.hashCode()).isEqualTo(anotherCacheKey.hashCode());
	}

	@Test
	public void testEquals()
	{
		final Object aKey = Lists.newArrayList("A", Locale.ENGLISH, context);
		final ObjectCacheKey sameCacheKey = new ObjectCacheKey(typeCode, aKey, tenantId);
		assertThat(objectCacheKey).isEqualTo(sameCacheKey);

		final Object bKey = Lists.newArrayList("B", Locale.ENGLISH, context);
		final ObjectCacheKey anotherCacheKey = new ObjectCacheKey(typeCode, bKey, tenantId);
		assertThat(objectCacheKey).isNotEqualTo(anotherCacheKey);

		final Object aZhKey = Lists.newArrayList("A", Locale.CHINESE, context);
		final ObjectCacheKey anotherCacheKey2 = new ObjectCacheKey(typeCode, aZhKey, tenantId);
		assertThat(objectCacheKey).isNotEqualTo(anotherCacheKey2);

		final Object aKeyCtx = Lists.newArrayList("A", Locale.ENGLISH, UUID.randomUUID());
		final ObjectCacheKey anotherCacheKey3 = new ObjectCacheKey(typeCode, aKeyCtx, tenantId);
		assertThat(objectCacheKey).isNotEqualTo(anotherCacheKey3);
	}

}
