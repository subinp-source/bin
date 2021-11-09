/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.personalizationintegration.strategies.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.servicelayer.data.PaginationData;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.personalizationservices.CxUpdateSegmentContext;
import de.hybris.platform.personalizationservices.data.BaseSegmentData;
import de.hybris.platform.personalizationservices.model.CxSegmentModel;
import de.hybris.platform.personalizationservices.segment.CxSegmentService;
import de.hybris.platform.personalizationservices.segment.dao.CxSegmentDao;
import de.hybris.platform.personalizationservices.strategies.UpdateSegmentStrategy;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.impex.impl.ClasspathImpExResource;
import de.hybris.platform.site.BaseSiteService;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.junit.Before;
import org.junit.Test;


/**
 * Integration test for default implementation of {@link UpdateSegmentStrategy}
 */
@IntegrationTest
public class DefaultCxUpdateSegmentStrategyIntegrationTest extends ServicelayerTest
{
	private static final String PROVIDER_1 = "provider1";
	private static final String PROVIDER_2 = "provider2";
	private static final String SEGMENT1 = "segment1";
	private static final String SEGMENT2 = "segment2";
	private static final String SEGMENT3 = "segment3";
	private static final String SEGMENT4 = "segment4";
	private static final String SEGMENT5 = "segment5";
	private static final String SEGMENT6 = "segment6";
	private static final String NEW_SEGMENT = "newSegment";
	private static final String BASE_SITE = "testSite";
	private static final String BASE_SITE2 = "testSite2";
	private static final String SEGMENT4_NOT_UPDATED = "Segment 4 was not updated";
	private static final String NEW_SEGMENT_NOT_CREATED = "New segment was not created";
	private static final String SEGMENT4_NOT_UPDATED_WITH_PROVIDER_1 = "Segment 4 was not updated with provider 1";
	private static final String SEGMENT_UPDATED_DESCRIPTION = "New description";

	@Resource
	private DefaultCxUpdateSegmentStrategy updateSegmentStrategy;

	@Resource
	private CxSegmentService cxSegmentService;

	@Resource
	private CxSegmentDao cxSegmentDao;

	@Resource
	private BaseSiteService baseSiteService;


	private TestSegmentsProvider provider1;
	private TestSegmentsProvider provider2;


	@Before
	public void setUp() throws Exception
	{
		createCoreData();
		createDefaultCatalog();
		importData(
				new ClasspathImpExResource("/personalizationintegration/test/testdata_personalizationintegration.impex", "UTF-8"));

		provider1 = new TestSegmentsProvider(PROVIDER_1, baseSiteService);
		provider2 = new TestSegmentsProvider(PROVIDER_2, baseSiteService);
		updateSegmentStrategy.setProviders(Arrays.asList(provider1, provider2));
	}


	@Test
	public void testSegmentUpdate()
	{
		// given
		configureSegmentProvider(provider2, SEGMENT4);

		// when
		updateSegmentStrategy.updateSegments(createUpdateContext(false));

		// then
		assertTrue(SEGMENT4_NOT_UPDATED, cxSegmentService.getSegment(SEGMENT4).get().getProviders().contains(PROVIDER_2));
	}

	@Test
	public void testSegmentCreate()
	{
		// given
		configureSegmentProvider(provider2, NEW_SEGMENT);

		// when
		updateSegmentStrategy.updateSegments(createUpdateContext(false));

		// then
		final Optional<CxSegmentModel> segmentModel = cxSegmentService.getSegment(NEW_SEGMENT);
		assertTrue(NEW_SEGMENT_NOT_CREATED, segmentModel.isPresent());
		assertTrue(NEW_SEGMENT.equals(segmentModel.get().getCode()));
		assertTrue(segmentModel.get().getProviders().contains(PROVIDER_2));
	}

	@Test
	public void testSegmentCreateForSelectedProvider()
	{
		// given
		final String newSegmentForProvider1 = NEW_SEGMENT + 1;
		final String newSegmentForProvider2 = NEW_SEGMENT + 2;

		configureSegmentProvider(provider1, newSegmentForProvider1);
		configureSegmentProvider(provider2, newSegmentForProvider2);

		// when
		updateSegmentStrategy.updateSegments(createUpdateContext(null, false, PROVIDER_1));

		// then
		Optional<CxSegmentModel> segment = cxSegmentService.getSegment(newSegmentForProvider1);
		assertTrue(NEW_SEGMENT_NOT_CREATED, segment.isPresent());
		assertTrue(segment.get().getProviders().contains(PROVIDER_1));
		segment = cxSegmentService.getSegment(newSegmentForProvider2);
		assertTrue("New segment for provider2 should not be created", segment.isEmpty());
	}

	@Test
	public void testSegmentUpdateWithWrongProvider()
	{
		// given
		final BaseSegmentData segmentData = new BaseSegmentData();
		segmentData.setCode(SEGMENT4);
		segmentData.setProvider(PROVIDER_2);
		provider1.addSegment(segmentData);

		// when
		updateSegmentStrategy.updateSegments(createUpdateContext(false));

		// then
		assertTrue(SEGMENT4_NOT_UPDATED_WITH_PROVIDER_1,
				cxSegmentService.getSegment(SEGMENT4).get().getProviders().contains(PROVIDER_1));
	}

	@Test
	public void testSegmentUpdateAndCreate()
	{
		// given
		configureSegmentProvider(provider1, SEGMENT4);
		configureSegmentProvider(provider2, NEW_SEGMENT);

		// when
		updateSegmentStrategy.updateSegments(createUpdateContext(false));

		// then
		final Optional<CxSegmentModel> segmentModel = cxSegmentService.getSegment(NEW_SEGMENT);
		assertTrue(NEW_SEGMENT_NOT_CREATED, segmentModel.isPresent());
		assertTrue(segmentModel.get().getProviders().contains(PROVIDER_2));
		assertTrue(SEGMENT4_NOT_UPDATED_WITH_PROVIDER_1,
				cxSegmentService.getSegment(SEGMENT4).get().getProviders().contains(PROVIDER_1));
	}

	@Test
	public void testSegmentUpdateAndCreateInBatch()
	{
		// given
		final int PROVIDER1_SIZE = 27;
		final int PROVIDER2_SIZE = 33;

		configureSegmentProvider(provider1, SEGMENT4);
		for (int i = 0; i < PROVIDER1_SIZE; i++)
		{
			configureSegmentProvider(provider1, NEW_SEGMENT + i);
		}

		for (int i = 0; i < PROVIDER2_SIZE; i++)
		{
			configureSegmentProvider(provider2, NEW_SEGMENT + i);
		}

		// when
		updateSegmentStrategy.updateSegments(createUpdateContext(false));

		// then
		assertTrue(SEGMENT4_NOT_UPDATED_WITH_PROVIDER_1,
				cxSegmentService.getSegment(SEGMENT4).get().getProviders().contains(PROVIDER_1));

		final Set<String> prov1Codes = provider1.getSegments().get().stream().map(s -> s.getCode()).collect(Collectors.toSet());
		final Collection<CxSegmentModel> prov1Segments = cxSegmentService.getSegmentsForCodes(prov1Codes);
		assertTrue(CollectionUtils.isNotEmpty(prov1Segments));
		assertEquals(PROVIDER1_SIZE + 1, prov1Segments.size());
		assertTrue(prov1Segments.stream().allMatch(s -> s.getProviders().contains(PROVIDER_1)));

		final Set<String> prov2Codes = provider2.getSegments().get().stream().map(s -> s.getCode()).collect(Collectors.toSet());
		final Collection<CxSegmentModel> prov2Segments = cxSegmentService.getSegmentsForCodes(prov2Codes);
		assertTrue(CollectionUtils.isNotEmpty(prov2Segments));
		assertEquals(PROVIDER2_SIZE, prov2Segments.size());
		assertTrue(prov2Segments.stream().allMatch(s -> s.getProviders().contains(PROVIDER_2)));
	}

	@Test
	public void testSegmentCreateForBaseSite()
	{
		// given
		final String newSegment = NEW_SEGMENT + 1;
		final String newSegmentForBaseSite = NEW_SEGMENT + 2;
		final BaseSiteModel baseSite = baseSiteService.getBaseSiteForUID(BASE_SITE);

		configureSegmentProvider(provider1, newSegment);
		configureSegmentProvider(baseSite, provider1, newSegmentForBaseSite);

		// when
		updateSegmentStrategy.updateSegments(createUpdateContext(baseSite, false, PROVIDER_1));

		// then
		Optional<CxSegmentModel> segment = cxSegmentService.getSegment(newSegmentForBaseSite);
		assertTrue(NEW_SEGMENT_NOT_CREATED, segment.isPresent());
		assertTrue(segment.get().getProviders().contains(PROVIDER_1));
		segment = cxSegmentService.getSegment(newSegment);
		assertTrue("New segment for null baseSite should not be created", segment.isEmpty());
	}

	@Test
	public void testSegmentCreateForTwoBasesites()
	{
		// given
		final String newSegmentForBaseSite = NEW_SEGMENT + 2;
		final String newSegmentForBaseSite2 = NEW_SEGMENT + 3;
		final BaseSiteModel baseSite = baseSiteService.getBaseSiteForUID(BASE_SITE);
		final BaseSiteModel baseSite2 = baseSiteService.getBaseSiteForUID(BASE_SITE2);

		configureSegmentProvider(baseSite, provider1, newSegmentForBaseSite);
		configureSegmentProvider(baseSite2, provider1, newSegmentForBaseSite2);
		configureSegmentProvider(baseSite2, provider1, newSegmentForBaseSite);

		final CxUpdateSegmentContext segmentContext = createUpdateContext(baseSite, true, PROVIDER_1);
		segmentContext.getBaseSites().add(baseSite2);

		// when
		updateSegmentStrategy.updateSegments(segmentContext);

		// then
		final Optional<CxSegmentModel> segment = cxSegmentService.getSegment(newSegmentForBaseSite);
		assertTrue(NEW_SEGMENT_NOT_CREATED + newSegmentForBaseSite, segment.isPresent());
		assertTrue(segment.get().getProviders().contains(PROVIDER_1));

		final Optional<CxSegmentModel> segment2 = cxSegmentService.getSegment(newSegmentForBaseSite2);
		assertTrue(NEW_SEGMENT_NOT_CREATED + newSegmentForBaseSite2, segment2.isPresent());
		assertTrue(segment2.get().getProviders().contains(PROVIDER_1));
	}

	@Test
	public void testSegmentRemoval()
	{
		// given
		assertTrue(cxSegmentService.getSegment(SEGMENT6).isPresent());
		assertTrue(cxSegmentService.getSegment(SEGMENT5).isPresent());
		configureSegmentProvider(provider2, SEGMENT1, SEGMENT3);
		configureSegmentProvider(provider1, SEGMENT1);

		// when
		updateSegmentStrategy.updateSegments(createUpdateContext(true));

		// then
		// segment6 and segment5 will be removed - no more providers, no triggers, auto created
		assertTrue(cxSegmentService.getSegment(SEGMENT6).isEmpty());
		assertTrue(cxSegmentService.getSegment(SEGMENT5).isEmpty());
	}

	@Test
	public void testSegmentRemovalForSelectedProvider()
	{
		// given
		assertTrue(cxSegmentService.getSegment(SEGMENT6).isPresent());
		configureSegmentProvider(provider2, SEGMENT1, SEGMENT3);

		// when
		updateSegmentStrategy.updateSegments(createUpdateContext(null, true, PROVIDER_2));

		// then
		// segment6 will be removed - no more providers, no triggers, auto created
		assertTrue(cxSegmentService.getSegment(SEGMENT6).isEmpty());
		// and segment5 will no longer have provider2 on provider list
		final Optional<CxSegmentModel> segment = cxSegmentService.getSegment(SEGMENT5);
		assertTrue(segment.isPresent());
		assertFalse(segment.get().getProviders().contains(PROVIDER_2));
	}

	@Test
	public void testSegmentRemovalForSelectedBaseSite()
	{
		// given
		assertTrue(cxSegmentService.getSegment(SEGMENT6).isPresent());
		final BaseSiteModel baseSite = baseSiteService.getBaseSiteForUID(BASE_SITE);
		configureSegmentProvider(provider2, SEGMENT3, SEGMENT5);
		configureSegmentProvider(baseSite, provider2, SEGMENT3, SEGMENT1);

		// when
		updateSegmentStrategy.updateSegments(createUpdateContext(baseSite, true, PROVIDER_2));

		// then
		// segment6 will be removed - no more providers, no triggers, auto created
		assertTrue(cxSegmentService.getSegment(SEGMENT6).isEmpty());
		// and segment5 will no longer have provider2 on provider list
		Optional<CxSegmentModel> segment = cxSegmentService.getSegment(SEGMENT5);
		assertTrue(segment.isPresent());
		assertFalse(segment.get().getProviders().contains(PROVIDER_2));
		//segment1 should still contain provider2
		segment = cxSegmentService.getSegment(SEGMENT1);
		assertTrue(segment.isPresent());
		assertTrue(segment.get().getProviders().contains(PROVIDER_2));
	}

	@Test
	public void testSegmentRemovalTriggerAttached()
	{
		// given
		configureSegmentProvider(provider2, SEGMENT1, SEGMENT5, SEGMENT6);

		// when
		updateSegmentStrategy.updateSegments(createUpdateContext(null, true, PROVIDER_2));

		// then
		final Collection<CxSegmentModel> prov2Segments = getAllSegmentsForProvider(PROVIDER_2);
		assertTrue(CollectionUtils.isNotEmpty(prov2Segments));
		assertEquals(4, prov2Segments.size());
		//segment3 has trigger attached, so it will not be deleted
		assertTrue(prov2Segments.stream().anyMatch(s -> SEGMENT3.equals(s.getCode())));
	}

	@Test
	public void testSegmentRemovalNotLastProvider()
	{
		// given
		configureSegmentProvider(provider2, SEGMENT1, SEGMENT3, SEGMENT6);

		// when
		updateSegmentStrategy.updateSegments(createUpdateContext(null, true, PROVIDER_2));

		// then
		//segment5 will not be removed because belong also to provider1 but will not have provider2 on list
		final Optional<CxSegmentModel> segment = cxSegmentService.getSegment(SEGMENT5);
		assertTrue(segment.isPresent());
		assertFalse(segment.get().getProviders().contains(PROVIDER_2));
	}

	@Test
	public void testSegmentRemovalNotAutoCreated()
	{
		// given
		configureSegmentProvider(provider1, SEGMENT1, SEGMENT5);

		// when
		updateSegmentStrategy.updateSegments(createUpdateContext(null, true, PROVIDER_1));

		// then
		// segment2 has autoCreated=false, so it will not be deleted
		final Optional<CxSegmentModel> segment = cxSegmentService.getSegment(SEGMENT2);
		assertTrue(segment.isPresent());
	}

	@Test
	public void testSegmentUpdateAutoCreatedWithOneProvider()
	{
		// given
		Optional<CxSegmentModel> segment3 = cxSegmentService.getSegment(SEGMENT3);
		assertTrue(segment3.isPresent());
		assertFalse(SEGMENT_UPDATED_DESCRIPTION.equals(segment3.get().getDescription()));

		final BaseSegmentData segmentData3 = new BaseSegmentData();
		segmentData3.setCode(SEGMENT3);
		segmentData3.setDescription(SEGMENT_UPDATED_DESCRIPTION);
		segmentData3.setProvider(PROVIDER_2);
		provider2.addSegment(null, segmentData3);

		final BaseSegmentData segmentData2 = new BaseSegmentData();
		segmentData2.setCode(SEGMENT2);
		segmentData2.setDescription(SEGMENT_UPDATED_DESCRIPTION);
		segmentData2.setProvider(PROVIDER_2);
		provider2.addSegment(null, segmentData2);

		final BaseSegmentData segmentData1 = new BaseSegmentData();
		segmentData1.setCode(SEGMENT1);
		segmentData1.setDescription(SEGMENT_UPDATED_DESCRIPTION);
		segmentData1.setProvider(PROVIDER_2);
		provider2.addSegment(null, segmentData1);

		// when
		updateSegmentStrategy.updateSegments(createUpdateContext(null, true, PROVIDER_2));

		// then
		// only segment with one provider and auto created should be updated
		segment3 = cxSegmentService.getSegment(SEGMENT3);
		assertTrue(segment3.isPresent());
		assertTrue(SEGMENT_UPDATED_DESCRIPTION.equals(segment3.get().getDescription()));

		final Optional<CxSegmentModel> segment2 = cxSegmentService.getSegment(SEGMENT2);
		assertTrue(segment2.isPresent());
		assertFalse(SEGMENT_UPDATED_DESCRIPTION.equals(segment2.get().getDescription()));

		final Optional<CxSegmentModel> segment1 = cxSegmentService.getSegment(SEGMENT1);
		assertTrue(segment1.isPresent());
		assertFalse(SEGMENT_UPDATED_DESCRIPTION.equals(segment1.get().getDescription()));
	}


	@Test
	public void testSegmentRemovalInBatch() throws Exception
	{
		// given
		importData(new ClasspathImpExResource("/personalizationintegration/test/testdata_newSegments.impex", "UTF-8"));
		Collection<CxSegmentModel> prov2Segments = getAllSegmentsForProvider(PROVIDER_2);
		assertEquals(prov2Segments.size(), 40);
		assertTrue(prov2Segments.size() > updateSegmentStrategy.getBatchSize());

		configureSegmentProvider(provider2, SEGMENT1, SEGMENT3, SEGMENT5, SEGMENT6);

		// when
		updateSegmentStrategy.updateSegments(createUpdateContext(null, true, PROVIDER_2));

		// then
		prov2Segments = getAllSegmentsForProvider(PROVIDER_2);
		assertTrue(CollectionUtils.isNotEmpty(prov2Segments));
		assertEquals(provider2.getSegments().get().size(), prov2Segments.size());
	}

	@Test
	public void testSegmentCodeLengthValidation()
	{
		// given
		SearchPageData<CxSegmentModel> searchPageData = new SearchPageData<>();
		final PaginationData pagination = new PaginationData();
		pagination.setCurrentPage(0);
		pagination.setPageSize(10);
		searchPageData.setPagination(pagination);

		final String LONG_SEGMENT_CODE = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip, quis nostrud exercitation ullamco laboris nisi ut aliquipb";
		do
		{
			searchPageData = cxSegmentDao.findSegments(MapUtils.EMPTY_MAP, searchPageData);
			for (final CxSegmentModel segment : searchPageData.getResults())
			{
				assertFalse("Segment with long segment code should not be present in the database",
						LONG_SEGMENT_CODE.equals(segment.getCode()));
			}
			searchPageData.getPagination().setCurrentPage(searchPageData.getPagination().getCurrentPage() + 1);
		}
		while (searchPageData.getPagination().getHasNext());
		configureSegmentProvider(provider1, LONG_SEGMENT_CODE);

		// when
		updateSegmentStrategy.updateSegments(createUpdateContext(false));

		// then
		pagination.setCurrentPage(0);
		do
		{
			searchPageData = cxSegmentDao.findSegments(MapUtils.EMPTY_MAP, searchPageData);
			for (final CxSegmentModel segment : searchPageData.getResults())
			{
				assertFalse("Segment with long segment code should not be created", LONG_SEGMENT_CODE.equals(segment.getCode()));
			}
			searchPageData.getPagination().setCurrentPage(searchPageData.getPagination().getCurrentPage() + 1);
		}
		while (searchPageData.getPagination().getHasNext());
	}

	protected void configureSegmentProvider(final TestSegmentsProvider provider, final String... segmentCodes)
	{
		configureSegmentProvider(null, provider, segmentCodes);
	}

	protected void configureSegmentProvider(final BaseSiteModel baseSite, final TestSegmentsProvider provider,
			final String... segmentCodes)
	{
		BaseSegmentData segment;
		for (final String segmentCode : segmentCodes)
		{
			segment = new BaseSegmentData();
			segment.setCode(segmentCode);
			segment.setProvider(provider.getProviderId());
			provider.addSegment(baseSite, segment);
		}
	}

	protected CxUpdateSegmentContext createUpdateContext(final boolean fullUpdate)
	{
		final CxUpdateSegmentContext context = new CxUpdateSegmentContext();
		context.setFullUpdate(fullUpdate);
		return context;
	}

	protected CxUpdateSegmentContext createUpdateContext(final BaseSiteModel baseSite, final boolean fullUpdate,
			final String... provider)
	{
		final CxUpdateSegmentContext context = new CxUpdateSegmentContext();
		context.setFullUpdate(fullUpdate);
		context.setSegmentProviders(Set.of(provider));
		if (baseSite != null)
		{
			if (context.getBaseSites() == null)
			{
				context.setBaseSites(new HashSet<>());
			}
			context.getBaseSites().add(baseSite);
		}
		return context;
	}

	protected List<CxSegmentModel> getAllSegments()
	{
		final SearchPageData<CxSegmentModel> searchPageData = new SearchPageData<>();
		final PaginationData pagination = new PaginationData();
		pagination.setCurrentPage(0);
		pagination.setPageSize(100);
		searchPageData.setPagination(pagination);

		return cxSegmentDao.findSegments(MapUtils.EMPTY_MAP, searchPageData).getResults();
	}

	protected List<CxSegmentModel> getAllSegmentsForProvider(final String provider)
	{
		return getAllSegments().stream()//
				.filter(seg -> seg.getProviders().contains(provider))//
				.collect(Collectors.toList());
	}
}
