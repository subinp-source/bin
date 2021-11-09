/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.unit.spi.service.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.searchservices.admin.SnIndexConfigurationNotFoundException;
import de.hybris.platform.searchservices.admin.data.SnIndexConfiguration;
import de.hybris.platform.searchservices.core.service.SnContext;
import de.hybris.platform.searchservices.model.AbstractSnSearchProviderConfigurationModel;
import de.hybris.platform.searchservices.spi.SnSearchProviderConfigurationNotFoundException;
import de.hybris.platform.searchservices.spi.SnSearchProviderMappingNotFoundException;
import de.hybris.platform.searchservices.spi.data.AbstractSnSearchProviderConfiguration;
import de.hybris.platform.searchservices.spi.service.SnSearchProvider;
import de.hybris.platform.searchservices.spi.service.SnSearchProviderMapping;
import de.hybris.platform.searchservices.spi.service.impl.DefaultSnSearchProviderFactory;
import de.hybris.platform.searchservices.spi.service.impl.DefaultSnSearchProviderMapping;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;


@UnitTest
public class DefaultSnSearchProviderFactoryTest
{
	private static final String SEARCH_PROVIDER_MAPPING_1_ID = "searchProviderMapping1";
	private static final String SEARCH_PROVIDER_MAPPING_2_ID = "searchProviderMapping2";

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Mock
	private ApplicationContext applicationContext;

	@Mock
	private SnContext context;

	@Mock
	private SnIndexConfiguration indexConfiguration;

	@Mock
	private SnSearchProvider<AbstractSnSearchProviderConfiguration> searchProvider1;

	@Mock
	private SnSearchProvider<AbstractSnSearchProviderConfiguration> searchProvider2;

	@Mock
	private DefaultSnSearchProviderMapping searchProviderMapping1;

	@Mock
	private DefaultSnSearchProviderMapping searchProviderMapping2;

	private SnSearchProviderConfigurationModel1 searchProviderConfigurationModel1;
	private SnSearchProviderConfigurationModel2 searchProviderConfigurationModel2;

	private SnSearchProviderConfiguration1 searchProviderConfiguration1;
	private SnSearchProviderConfiguration2 searchProviderConfiguration2;

	private DefaultSnSearchProviderFactory snSearchProviderFactory;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		searchProviderMapping1 = new DefaultSnSearchProviderMapping();
		searchProviderMapping1.setItemType(SnSearchProviderConfigurationModel1.class.getName());
		searchProviderMapping1.setType(SnSearchProviderConfiguration1.class.getName());
		searchProviderMapping1.setSearchProvider(searchProvider1);

		searchProviderMapping2 = new DefaultSnSearchProviderMapping();
		searchProviderMapping2.setItemType(SnSearchProviderConfigurationModel2.class.getName());
		searchProviderMapping2.setType(SnSearchProviderConfiguration2.class.getName());
		searchProviderMapping2.setSearchProvider(searchProvider2);

		searchProviderConfigurationModel1 = new SnSearchProviderConfigurationModel1();
		searchProviderConfigurationModel2 = new SnSearchProviderConfigurationModel2();

		searchProviderConfiguration1 = new SnSearchProviderConfiguration1();
		searchProviderConfiguration2 = new SnSearchProviderConfiguration2();

		snSearchProviderFactory = new DefaultSnSearchProviderFactory();
		snSearchProviderFactory.setApplicationContext(applicationContext);
	}

	@Test
	public void failToGetSearchProviderForContextWithNullContext()
	{
		// given
		snSearchProviderFactory.afterPropertiesSet();

		// expect
		expectedException.expect(NullPointerException.class);

		// when
		snSearchProviderFactory.getSearchProviderForContext(null);
	}

	@Test
	public void failToGetSearchProviderForContextWithNullIndexConfiguration()
	{
		// given
		when(context.getIndexConfiguration()).thenReturn(null);

		snSearchProviderFactory.afterPropertiesSet();

		// expect
		expectedException.expect(SnIndexConfigurationNotFoundException.class);

		// when
		snSearchProviderFactory.getSearchProviderForContext(context);
	}

	@Test
	public void failToGetSearchProviderForContextWithNullSearchProviderConfigurationForIndexConfiguration()
	{
		// given
		when(indexConfiguration.getSearchProviderConfiguration()).thenReturn(null);
		when(context.getIndexConfiguration()).thenReturn(indexConfiguration);

		snSearchProviderFactory.afterPropertiesSet();

		// expect
		expectedException.expect(SnSearchProviderConfigurationNotFoundException.class);

		// when
		snSearchProviderFactory.getSearchProviderForContext(context);
	}

	@Test
	public void failToGetSearchProviderForContextWithNoRegisteredSearchProvider()
	{
		// given
		when(context.getIndexConfiguration()).thenReturn(indexConfiguration);
		when(indexConfiguration.getSearchProviderConfiguration()).thenReturn(searchProviderConfiguration1);

		snSearchProviderFactory.afterPropertiesSet();

		// expect
		expectedException.expect(SnSearchProviderMappingNotFoundException.class);

		// when
		snSearchProviderFactory.getSearchProviderForContext(context);
	}


	@Test
	public void getSearchProviderForContextWithSingleRegisteredSearchProvider()
	{
		// given
		when(context.getIndexConfiguration()).thenReturn(indexConfiguration);
		when(indexConfiguration.getSearchProviderConfiguration()).thenReturn(searchProviderConfiguration1);

		final Map<String, SnSearchProviderMapping> beans = new HashMap<>();
		beans.put(SEARCH_PROVIDER_MAPPING_1_ID, searchProviderMapping1);

		when(applicationContext.getBeansOfType(SnSearchProviderMapping.class)).thenReturn(beans);

		snSearchProviderFactory.afterPropertiesSet();

		// when
		final SnSearchProvider<?> searchProvider = snSearchProviderFactory.getSearchProviderForContext(context);

		// then
		assertNotNull(searchProvider);
		assertSame(searchProvider1, searchProvider);
	}

	@Test
	public void getSearchProviderForContextWithMultipleRegisteredSearchProviders1()
	{
		// given
		when(context.getIndexConfiguration()).thenReturn(indexConfiguration);
		when(indexConfiguration.getSearchProviderConfiguration()).thenReturn(searchProviderConfiguration1);

		final Map<String, SnSearchProviderMapping> beans = new HashMap<>();
		beans.put(SEARCH_PROVIDER_MAPPING_1_ID, searchProviderMapping1);
		beans.put(SEARCH_PROVIDER_MAPPING_2_ID, searchProviderMapping2);

		when(applicationContext.getBeansOfType(SnSearchProviderMapping.class)).thenReturn(beans);

		snSearchProviderFactory.afterPropertiesSet();

		// when
		final SnSearchProvider<?> searchProvider = snSearchProviderFactory.getSearchProviderForContext(context);

		// then
		assertNotNull(searchProvider);
		assertSame(searchProvider1, searchProvider);
	}

	@Test
	public void getSearchProviderForContextWithMultipleRegisteredSearchProviders2()
	{
		// given
		when(context.getIndexConfiguration()).thenReturn(indexConfiguration);
		when(indexConfiguration.getSearchProviderConfiguration()).thenReturn(searchProviderConfiguration2);

		final Map<String, SnSearchProviderMapping> beans = new HashMap<>();
		beans.put(SEARCH_PROVIDER_MAPPING_1_ID, searchProviderMapping1);
		beans.put(SEARCH_PROVIDER_MAPPING_2_ID, searchProviderMapping2);

		when(applicationContext.getBeansOfType(SnSearchProviderMapping.class)).thenReturn(beans);

		snSearchProviderFactory.afterPropertiesSet();

		// when
		final SnSearchProvider<?> searchProvider = snSearchProviderFactory.getSearchProviderForContext(context);

		// then
		assertNotNull(searchProvider);
		assertSame(searchProvider2, searchProvider);
	}

	@Test
	public void failToGetSearchProviderMappingForConfigurationModelWithNullConfigurationModel()
	{
		// given
		snSearchProviderFactory.afterPropertiesSet();

		// expect
		expectedException.expect(NullPointerException.class);

		// when
		snSearchProviderFactory.getSearchProviderMappingForConfigurationModel(null);
	}

	@Test
	public void failToGetSearchProviderMappingForConfigurationModelWithNoRegisteredSearchProvider()
	{
		// given
		snSearchProviderFactory.afterPropertiesSet();

		// expect
		expectedException.expect(SnSearchProviderMappingNotFoundException.class);

		// when
		snSearchProviderFactory.getSearchProviderMappingForConfigurationModel(searchProviderConfigurationModel1);
	}

	@Test
	public void getSearchProviderMappingForConfigurationModelWithSingleRegisteredSearchProvider()
	{
		// given
		final Map<String, SnSearchProviderMapping> beans = new HashMap<>();
		beans.put(SEARCH_PROVIDER_MAPPING_1_ID, searchProviderMapping1);

		when(applicationContext.getBeansOfType(SnSearchProviderMapping.class)).thenReturn(beans);

		snSearchProviderFactory.afterPropertiesSet();

		// when
		final SnSearchProviderMapping searchProviderMapping = snSearchProviderFactory
				.getSearchProviderMappingForConfigurationModel(searchProviderConfigurationModel1);

		// then
		assertNotNull(searchProviderMapping);
		assertSame(searchProviderMapping1, searchProviderMapping);
	}

	@Test
	public void getSearchProviderMappingForConfigurationModelWithMultipleRegisteredSearchProviders1()
	{
		// given
		final Map<String, SnSearchProviderMapping> beans = new HashMap<>();
		beans.put(SEARCH_PROVIDER_MAPPING_1_ID, searchProviderMapping1);
		beans.put(SEARCH_PROVIDER_MAPPING_2_ID, searchProviderMapping2);

		when(applicationContext.getBeansOfType(SnSearchProviderMapping.class)).thenReturn(beans);

		snSearchProviderFactory.afterPropertiesSet();

		// when
		final SnSearchProviderMapping searchProviderMapping = snSearchProviderFactory
				.getSearchProviderMappingForConfigurationModel(searchProviderConfigurationModel1);

		// then
		assertNotNull(searchProviderMapping);
		assertSame(searchProviderMapping1, searchProviderMapping);
	}

	@Test
	public void getSearchProviderMappingForConfigurationModelWithMultipleRegisteredSearchProviders2()
	{
		// given
		final Map<String, SnSearchProviderMapping> beans = new HashMap<>();
		beans.put(SEARCH_PROVIDER_MAPPING_1_ID, searchProviderMapping1);
		beans.put(SEARCH_PROVIDER_MAPPING_2_ID, searchProviderMapping2);

		when(applicationContext.getBeansOfType(SnSearchProviderMapping.class)).thenReturn(beans);

		snSearchProviderFactory.afterPropertiesSet();

		// when
		final SnSearchProviderMapping searchProviderMapping = snSearchProviderFactory
				.getSearchProviderMappingForConfigurationModel(searchProviderConfigurationModel2);

		// then
		assertNotNull(searchProviderMapping);
		assertSame(searchProviderMapping2, searchProviderMapping);
	}

	@Test
	public void failToGetSearchProviderMappingForConfigurationWithNullConfiguration()
	{
		// given
		snSearchProviderFactory.afterPropertiesSet();

		// expect
		expectedException.expect(NullPointerException.class);

		// when
		snSearchProviderFactory.getSearchProviderMappingForConfiguration(null);
	}

	@Test
	public void failToGetSearchProviderMappingForConfigurationWithNoRegisteredSearchProvider()
	{
		// given
		snSearchProviderFactory.afterPropertiesSet();

		// expect
		expectedException.expect(SnSearchProviderMappingNotFoundException.class);

		// when
		snSearchProviderFactory.getSearchProviderMappingForConfiguration(searchProviderConfiguration1);
	}

	@Test
	public void getSearchProviderMappingForConfigurationWithSingleRegisteredSearchProvider()
	{
		// given
		final Map<String, SnSearchProviderMapping> beans = new HashMap<>();
		beans.put(SEARCH_PROVIDER_MAPPING_1_ID, searchProviderMapping1);

		when(applicationContext.getBeansOfType(SnSearchProviderMapping.class)).thenReturn(beans);

		snSearchProviderFactory.afterPropertiesSet();

		// when
		final SnSearchProviderMapping searchProviderMapping = snSearchProviderFactory
				.getSearchProviderMappingForConfiguration(searchProviderConfiguration1);

		// then
		assertNotNull(searchProviderMapping);
		assertSame(searchProviderMapping1, searchProviderMapping);
	}

	@Test
	public void getSearchProviderMappingForConfigurationWithMultipleRegisteredSearchProviders1()
	{
		// given
		final Map<String, SnSearchProviderMapping> beans = new HashMap<>();
		beans.put(SEARCH_PROVIDER_MAPPING_1_ID, searchProviderMapping1);
		beans.put(SEARCH_PROVIDER_MAPPING_2_ID, searchProviderMapping2);

		when(applicationContext.getBeansOfType(SnSearchProviderMapping.class)).thenReturn(beans);

		snSearchProviderFactory.afterPropertiesSet();

		// when
		final SnSearchProviderMapping searchProviderMapping = snSearchProviderFactory
				.getSearchProviderMappingForConfiguration(searchProviderConfiguration1);

		// then
		assertNotNull(searchProviderMapping);
		assertSame(searchProviderMapping1, searchProviderMapping);
	}

	@Test
	public void getSearchProviderMappingForConfigurationWithMultipleRegisteredSearchProviders2()
	{
		// given
		final Map<String, SnSearchProviderMapping> beans = new HashMap<>();
		beans.put(SEARCH_PROVIDER_MAPPING_1_ID, searchProviderMapping1);
		beans.put(SEARCH_PROVIDER_MAPPING_2_ID, searchProviderMapping2);

		when(applicationContext.getBeansOfType(SnSearchProviderMapping.class)).thenReturn(beans);

		snSearchProviderFactory.afterPropertiesSet();

		// when
		final SnSearchProviderMapping searchProviderMapping = snSearchProviderFactory
				.getSearchProviderMappingForConfiguration(searchProviderConfiguration2);

		// then
		assertNotNull(searchProviderMapping);
		assertSame(searchProviderMapping2, searchProviderMapping);
	}

	private static class SnSearchProviderConfigurationModel1 extends AbstractSnSearchProviderConfigurationModel
	{
		// empty
	}

	private static class SnSearchProviderConfigurationModel2 extends AbstractSnSearchProviderConfigurationModel
	{
		// empty
	}

	private static class SnSearchProviderConfiguration1 extends AbstractSnSearchProviderConfiguration
	{
		// empty
	}

	private static class SnSearchProviderConfiguration2 extends AbstractSnSearchProviderConfiguration
	{
		// empty
	}
}
