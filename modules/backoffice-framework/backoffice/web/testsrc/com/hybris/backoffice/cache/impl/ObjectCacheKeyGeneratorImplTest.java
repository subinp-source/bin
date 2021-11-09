/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.cache.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.Locale;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;
import com.hybris.cockpitng.core.spring.RequestOperationContextHolder;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacade;
import com.hybris.cockpitng.labels.LabelStringObjectHandler;


@RunWith(MockitoJUnitRunner.class)
public class ObjectCacheKeyGeneratorImplTest
{

	@InjectMocks
	private ObjectCacheKeyGeneratorImpl objectCacheKeyGeneratorImpl;

	@Mock
	private ObjectFacade objectFacade;

	@Mock
	private LabelStringObjectHandler labelStringObjectHandler;

	@Before
	public void setUp()
	{
		when(labelStringObjectHandler.getCurrentLocale()).thenReturn(Locale.ENGLISH);
		when(objectFacade.getObjectId(any())).thenReturn(2000);
	}

	@Test
	public void testCreateCacheKey()
	{
		final String typeCode = "PRODUCT_CODE";
		final Object key = Lists.newArrayList(new Object(), Locale.ENGLISH, UUID.randomUUID());
		final ObjectCacheKey result = objectCacheKeyGeneratorImpl.createCacheKey(typeCode, key);
		assertThat(result).isNotNull();
		assertThat(result.getObjectKey()).isEqualTo(key);
		assertThat(result.getTypeCode()).isSameAs(typeCode);
	}

	@Test
	public void testGetTenantId()
	{
		final String result = objectCacheKeyGeneratorImpl.getTenantId();
		if (Registry.hasCurrentTenant())
		{
			assertThat(result).isSameAs(Registry.getCurrentTenant().getTenantID());
		}
		else
		{
			assertThat(result).isSameAs(objectCacheKeyGeneratorImpl.NO_ACTIVE_TENANT);
		}
	}

	@Test
	public void testComputeKey4String()
	{
		final Object expectedObj = Lists.newArrayList("A", labelStringObjectHandler.getCurrentLocale(),
				RequestOperationContextHolder.instance().getContext());
		final Object obj = objectCacheKeyGeneratorImpl.computeKey("A");

		assertThat(obj).isNotNull().isEqualTo(expectedObj);
	}

	@Test
	public void testComputeKey4ItemModel()
	{
		final Object expectedObj = Lists.newArrayList(2000, labelStringObjectHandler.getCurrentLocale(),
				RequestOperationContextHolder.instance().getContext());
		final ProductModel mdl = new ProductModel();
		final Object obj = objectCacheKeyGeneratorImpl.computeKey(mdl);

		assertThat(obj).isNotNull().isEqualTo(expectedObj);
	}
}
