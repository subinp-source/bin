/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.solrsearch.cache.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import java.util.Random;

import de.hybris.platform.core.PK;
import de.hybris.platform.servicelayer.model.ModelService;
import org.junit.Before;
import org.junit.Test;

import com.hybris.backoffice.solrsearch.model.BackofficeIndexedTypeToSolrFacetSearchConfigModel;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class DefaultBackofficeFacetSearchConfigCacheTest
{

	private static final String KEY_WITH_EXISTING_VALUE = "existingValue";
	private static final String KEY_WITH_NULL_VALUE = "nullValue";
	private static final String KEY_WITHOUT_VALUE = "noValue";
	private static final PK PK_1 = PK.fromLong(1L);

	@InjectMocks
	private DefaultBackofficeFacetSearchConfigCache cache;

	@Mock
	private ModelService modelService;

	@Mock
	private BackofficeIndexedTypeToSolrFacetSearchConfigModel facetSearchConfig;


	@Before
	public void setUp()
	{
		when(facetSearchConfig.getPk()).thenReturn(PK_1);
		when(modelService.get(eq(PK_1))).thenReturn(facetSearchConfig);
		cache.putSearchConfigForTypeCode(KEY_WITH_EXISTING_VALUE, facetSearchConfig);
		cache.putSearchConfigForTypeCode(KEY_WITH_NULL_VALUE, null);
	}

	@Test
	public void testExistingValue()
	{
		assertTrue(cache.containsSearchConfigForTypeCode(KEY_WITH_EXISTING_VALUE));
		assertEquals(facetSearchConfig, cache.getSearchConfigForTypeCode(KEY_WITH_EXISTING_VALUE));
	}

	@Test
	public void testNullValue()
	{
		assertTrue(cache.containsSearchConfigForTypeCode(KEY_WITH_NULL_VALUE));
		assertNull(cache.getSearchConfigForTypeCode(KEY_WITH_NULL_VALUE));
	}

	@Test
	public void testNoValue()
	{
		assertFalse(cache.containsSearchConfigForTypeCode(KEY_WITHOUT_VALUE));
		assertNull(cache.getSearchConfigForTypeCode(KEY_WITHOUT_VALUE));
	}

	@Test
	public void testInvalidation()
	{
		testExistingValue();
		cache.invalidateCache();
		assertFalse(cache.containsSearchConfigForTypeCode(KEY_WITH_EXISTING_VALUE));
	}

	@Test
	public void testConcurrency() throws InterruptedException
	{
		final Thread t1 = new ReadingThread(10 + new Random().nextInt(10));
		final Thread t2 = new ReadingThread(10 + new Random().nextInt(10));
		final Thread t3 = new ReadingThread(10 + new Random().nextInt(10));
		final Thread t4 = new ReadingAndWritingThread(30);

		t1.run();
		t2.run();
		t3.run();
		t4.run();

		t1.join();
		t2.join();
		t3.join();
		t4.join();

		assertFalse(cache.containsSearchConfigForTypeCode(KEY_WITH_EXISTING_VALUE));
		assertFalse(cache.containsSearchConfigForTypeCode(KEY_WITH_NULL_VALUE));
		assertFalse(cache.containsSearchConfigForTypeCode(KEY_WITHOUT_VALUE));
	}


	private class ReadingThread extends Thread
	{

		private final int iterations;

		public ReadingThread(final int iterations)
		{
			this.iterations = iterations;
		}

		@Override
		public void run()
		{
			for (int i = 0; i < iterations; i++)
			{
				final boolean contains = cache.containsSearchConfigForTypeCode(KEY_WITH_EXISTING_VALUE);
				final BackofficeIndexedTypeToSolrFacetSearchConfigModel value = cache
						.getSearchConfigForTypeCode(KEY_WITH_EXISTING_VALUE);
				System.out.println(Boolean.toString(contains) + " " + value);
				try
				{
					Thread.sleep(20L);
				}
				catch (final InterruptedException ignore)
				{
					// empty
				}
			}
		}

	}

	private class ReadingAndWritingThread extends Thread
	{

		private final int iterations;

		public ReadingAndWritingThread(final int iterations)
		{
			this.iterations = iterations;
		}

		@Override
		public void run()
		{
			for (int i = 0; i < iterations; i++)
			{
				cache.putSearchConfigForTypeCode(KEY_WITHOUT_VALUE, facetSearchConfig);
				cache.putSearchConfigForTypeCode(KEY_WITH_EXISTING_VALUE, null);
				cache.getSearchConfigForTypeCode(KEY_WITH_EXISTING_VALUE);
				cache.invalidateCache();
				cache.getSearchConfigForTypeCode(KEY_WITH_EXISTING_VALUE);
			}
		}

	}

}
