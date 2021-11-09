/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.personalizationintegration.strategies.impl;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyCollectionOf;
import static org.mockito.Matchers.anyMapOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.servicelayer.data.PaginationData;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.personalizationintegration.segment.SegmentsProvider;
import de.hybris.platform.personalizationservices.CxUpdateSegmentContext;
import de.hybris.platform.personalizationservices.data.BaseSegmentData;
import de.hybris.platform.personalizationservices.model.CxSegmentModel;
import de.hybris.platform.personalizationservices.segment.dao.CxSegmentDao;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.site.BaseSiteService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class DefaultCxUpdateSegmentStrategyTest
{
	private static final String PROVIDER_1 = "provider1";
	private static final String PROVIDER_2 = "provider2";
	private static final String PROVIDER_3 = "provider3";

	private static final String SEGMENT_1_P1 = "s1provider1";
	private static final String SEGMENT_2_P1 = "s2provider1";
	private static final String SEGMENT_3_P1 = "s3provider1";
	private static final String SEGMENT_4_P1 = "s4provider1";
	private static final String SEGMENT_1_P2 = "s1provider2";
	private static final String SEGMENT_2_P2 = "s2provider2";
	private static final String SEGMENT_3_P2 = "s3provider2";
	private static final String SEGMENT_1_P3 = "s1provider3";

	private static final String PREFIX = "pref_";
	private static final String SEPARATOR = StringUtils.SPACE;
	private static final String PREFIX_PROP_KEY = "personalizationintegration.provider.provider1.prefix";
	private static final String SEPARATOR_PROP_KEY = "personalizationintegration.provider.provider1.separator";


	private final DefaultCxUpdateSegmentStrategy segmentUpdateStrategy = new DefaultCxUpdateSegmentStrategy();

	@Mock
	private ModelService modelService;
	@Mock
	private CxSegmentDao cxSegmentDao;
	@Mock
	private ConfigurationService configurationService;
	@Mock
	private BaseSiteService baseSiteService;
	@Mock
	Configuration configuration;
	@Mock
	private SegmentsProvider provider1;
	@Mock
	private SegmentsProvider provider2;
	@Mock
	private SegmentsProvider provider3;
	@Mock
	private List<SegmentsProvider> providers;
	@Mock
	private SearchPageData<CxSegmentModel> searchPageData;
	@Mock
	private BaseSiteModel baseSite1;
	@Mock
	private BaseSiteModel baseSite2;

	@Before
	public void init()
	{
		MockitoAnnotations.initMocks(this);

		buildUpData();

		segmentUpdateStrategy.setConfigurationService(configurationService);
		segmentUpdateStrategy.setProviders(providers);
		segmentUpdateStrategy.setCxSegmentDao(cxSegmentDao);
		segmentUpdateStrategy.setModelService(modelService);
		segmentUpdateStrategy.setBaseSiteService(baseSiteService);

		when(configurationService.getConfiguration()).thenReturn(configuration);
		when(configuration.getString(PREFIX_PROP_KEY)).thenReturn(PREFIX);
		when(configuration.getString(SEPARATOR_PROP_KEY)).thenReturn(SEPARATOR);
		when(modelService.create(CxSegmentModel.class)).thenReturn(new CxSegmentModel());
		when(cxSegmentDao.findSegments(anyMapOf(String.class, String.class), any())).thenReturn(searchPageData);
	}

	@Test
	public void createSegmentsInOneBatch()
	{
		// given
		segmentUpdateStrategy.setBatchSize(10);

		// when
		segmentUpdateStrategy.updateSegments(createUpdateContext(false));

		// then
		verify(cxSegmentDao, times(providers.size())).findSegmentsByCodes(anyCollectionOf(String.class));
		verify(modelService, times(8)).create(CxSegmentModel.class);
	}

	@Test
	public void removeSegments()
	{
		// given
		final PaginationData pagination = new PaginationData();
		pagination.setCurrentPage(0);
		pagination.setPageSize(10);
		pagination.setHasNext(Boolean.FALSE);
		when(searchPageData.getPagination()).thenReturn(pagination);
		when(searchPageData.getResults()).thenReturn(Collections.emptyList());
		segmentUpdateStrategy.setBatchSize(10);

		// when
		segmentUpdateStrategy.updateSegments(createUpdateContext(true));

		//then
		verify(cxSegmentDao, times(providers.size())).findSegmentsByCodes(anyCollectionOf(String.class));
		verify(modelService, times(8)).create(CxSegmentModel.class);
		verify(modelService, times(providers.size())).removeAll(anyCollectionOf(String.class));
	}

	@Test
	public void updateAutoCreatedSegmentsWithOneProvider()
	{
		// given
		final PaginationData pagination = new PaginationData();
		pagination.setCurrentPage(0);
		pagination.setPageSize(10);
		pagination.setHasNext(Boolean.FALSE);
		when(searchPageData.getPagination()).thenReturn(pagination);
		final CxSegmentModel cxSegmentModel = new CxSegmentModel();
		cxSegmentModel.setAutoCreated(true);
		cxSegmentModel.setCode(SEGMENT_1_P2);
		cxSegmentModel.setProviders(Set.of(PROVIDER_2));
		when(searchPageData.getResults()).thenReturn(Collections.singletonList(cxSegmentModel));
		segmentUpdateStrategy.setBatchSize(10);

		// when
		segmentUpdateStrategy.updateSegments(createUpdateContext(true));

		//then
		verify(cxSegmentDao, times(providers.size())).findSegmentsByCodes(anyCollectionOf(String.class));
		verify(modelService, times(8)).create(CxSegmentModel.class);
		verify(modelService, times(providers.size())).removeAll(anyCollectionOf(CxSegmentModel.class));
		verify(modelService, times(1)).save(cxSegmentModel);
	}


	@Test
	public void createSegmentsInManyBatches()
	{
		// given
		segmentUpdateStrategy.setBatchSize(2);

		// when
		segmentUpdateStrategy.updateSegments(createUpdateContext(false));

		// then
		verify(cxSegmentDao, times(5)).findSegmentsByCodes(anyCollectionOf(String.class));
		verify(modelService, times(8)).create(CxSegmentModel.class);
	}


	@Test
	public void updateAndCreateSegmentsInManyBatches()
	{
		// given
		segmentUpdateStrategy.setBatchSize(3);
		final List<String> existingCodes = Arrays.asList(PREFIX + SEPARATOR + SEGMENT_1_P1, PREFIX + SEPARATOR + SEGMENT_2_P1,
				PREFIX + SEPARATOR + SEGMENT_3_P1);
		final Collection<CxSegmentModel> modelsToReturn = existingCodes.stream().map(this::createSegmentModel)
				.collect(Collectors.toList());
		when(cxSegmentDao.findSegmentsByCodes(existingCodes)).thenReturn(modelsToReturn);

		// when
		segmentUpdateStrategy.updateSegments(createUpdateContext(false));

		// then
		verify(cxSegmentDao, times(4)).findSegmentsByCodes(anyCollectionOf(String.class));
		verify(modelService, times(5)).create(CxSegmentModel.class);
		verify(modelService, times(8)).save(any());
		assertTrue("Provider has not been updated",
				modelsToReturn.stream().filter(s -> s.getProviders().contains(PROVIDER_1)).findAny().isPresent());
	}

	@Test
	public void testUpdateSegmentsForBaseSite()
	{
		// given
		final CxUpdateSegmentContext context = createUpdateContext(null, false, Set.of(baseSite1));

		// when
		segmentUpdateStrategy.updateSegments(context);

		// then
		final int segmentsFromProvidersCount = providers.stream().mapToInt(p -> p.getSegments().get().size()).sum();

		verify(baseSiteService, times(providers.size())).setCurrentBaseSite(baseSite1, true);
		verify(cxSegmentDao, times(providers.size())).findSegmentsByCodes(anyCollectionOf(String.class));
		verify(modelService, times(segmentsFromProvidersCount)).create(CxSegmentModel.class);

	}

	@Test
	public void testUpdateSegmentsForMultipleBaseSites()
	{
		// given
		final CxUpdateSegmentContext context = createUpdateContext(null, false, Set.of(baseSite1, baseSite2));

		// when
		segmentUpdateStrategy.updateSegments(context);

		// then
		final int segmentsFromProvidersCount = providers.stream().mapToInt(p -> p.getSegments().get().size()).sum();

		verify(baseSiteService, times(providers.size())).setCurrentBaseSite(baseSite1, true);
		verify(baseSiteService, times(providers.size())).setCurrentBaseSite(baseSite2, true);
		verify(cxSegmentDao, times(providers.size())).findSegmentsByCodes(anyCollectionOf(String.class));
		verify(modelService, times(segmentsFromProvidersCount)).create(CxSegmentModel.class);
	}

	@Test
	public void testUpdateSegmentsForSelectedProviders()
	{
		// given
		final CxUpdateSegmentContext context = createUpdateContext(Set.of(PROVIDER_1, PROVIDER_2), false, Set.of(baseSite1));

		// when
		segmentUpdateStrategy.updateSegments(context);

		// then
		verify(baseSiteService, times(2)).setCurrentBaseSite(baseSite1, true);
		verify(cxSegmentDao, times(2)).findSegmentsByCodes(anyCollectionOf(String.class));
		verify(modelService, times(7)).create(CxSegmentModel.class);
	}

	@Test
	public void testUpdateSegmentsForNullContext()
	{
		// given
		final CxUpdateSegmentContext context = null;

		// when
		segmentUpdateStrategy.updateSegments(context);

		// then
		verify(baseSiteService, times(0)).setCurrentBaseSite(any(BaseSiteModel.class), eq(true));
		verify(cxSegmentDao, times(providers.size())).findSegmentsByCodes(anyCollectionOf(String.class));
		verify(modelService, times(8)).create(CxSegmentModel.class);
	}

	protected CxUpdateSegmentContext createUpdateContext(final boolean fullUpdate)
	{
		final CxUpdateSegmentContext context = new CxUpdateSegmentContext();
		context.setFullUpdate(fullUpdate);
		return context;
	}

	protected CxUpdateSegmentContext createUpdateContext(final Set<String> providers, final boolean fullUpdate,
			final Set<BaseSiteModel> baseSites)
	{
		final CxUpdateSegmentContext context = new CxUpdateSegmentContext();
		context.setFullUpdate(fullUpdate);
		context.setSegmentProviders(providers);
		context.setBaseSites(baseSites);
		return context;
	}

	private void buildUpData()
	{
		providers = new ArrayList<>();
		providers.add(provider1);
		providers.add(provider2);
		providers.add(provider3);

		when(provider1.getProviderId()).thenReturn(PROVIDER_1);
		when(provider2.getProviderId()).thenReturn(PROVIDER_2);
		when(provider3.getProviderId()).thenReturn(PROVIDER_3);

		when(provider1.getSegments())//
				.thenReturn(Optional.of(Arrays.asList(SEGMENT_1_P1, SEGMENT_2_P1, SEGMENT_3_P1, SEGMENT_4_P1)//
						.stream().map(this::createSegmentDataForProvider).collect(Collectors.toList())));
		when(provider2.getSegments())//
				.thenReturn(Optional.of(Arrays.asList(SEGMENT_1_P2, SEGMENT_2_P2, SEGMENT_3_P2)//
						.stream().map(this::createSegmentData).collect(Collectors.toList())));
		when(provider3.getSegments())//
				.thenReturn(Optional.of(Arrays.asList(SEGMENT_1_P3)//
						.stream().map(this::createSegmentData).collect(Collectors.toList())));
	}

	private BaseSegmentData createSegmentDataForProvider(final String code)
	{
		final BaseSegmentData segmentData = createSegmentData(code);
		segmentData.setProvider(PROVIDER_1);
		return segmentData;
	}

	private BaseSegmentData createSegmentData(final String code)
	{
		final BaseSegmentData segmentData = new BaseSegmentData();
		segmentData.setCode(code);
		return segmentData;
	}

	private CxSegmentModel createSegmentModel(final String code)
	{
		final CxSegmentModel segmentModel = new CxSegmentModel();
		segmentModel.setCode(code);
		return segmentModel;
	}

}
