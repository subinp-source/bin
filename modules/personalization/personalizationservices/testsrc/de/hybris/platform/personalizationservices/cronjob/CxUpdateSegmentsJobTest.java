/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.personalizationservices.cronjob;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.personalizationservices.CxUpdateSegmentContext;
import de.hybris.platform.personalizationservices.model.CxUpdateSegmentsCronJobModel;
import de.hybris.platform.personalizationservices.segment.CxSegmentService;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.site.BaseSiteService;

import java.util.Collections;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class CxUpdateSegmentsJobTest
{
	private static final String PROVIDER1 = "provider1";

	@Mock
	private CxSegmentService cxSegmentService;
	@Mock
	private BaseSiteService baseSiteService;

	@Mock
	private BaseSiteModel baseSiteModel1;
	@Mock
	private BaseSiteModel baseSiteModel2;
	@Mock
	private BaseSiteModel baseSiteModel3;

	private Set<BaseSiteModel> allBaseSites;

	protected CxUpdateSegmentsJob job;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		job = new CxUpdateSegmentsJob();
		job.setBaseSiteService(baseSiteService);
		job.setCxSegmentService(cxSegmentService);

		allBaseSites = Set.of(baseSiteModel1, baseSiteModel2, baseSiteModel3);

		when(baseSiteService.getAllBaseSites()).thenReturn(allBaseSites);
	}

	@Test
	public void testPerform()
	{
		//given
		final CxUpdateSegmentsCronJobModel cronJob = createCronJob(true, Set.of(baseSiteModel1, baseSiteModel3), Set.of(PROVIDER1));

		//when
		final PerformResult result = job.perform(cronJob);

		//then
		assertEquals(CronJobResult.SUCCESS, result.getResult());
		assertEquals(CronJobStatus.FINISHED, result.getStatus());

		verifyUpdateSegmentContext(cronJob);
	}

	@Test
	public void testPerformWhenAllBaseSites()
	{
		//given
		final CxUpdateSegmentsCronJobModel cronJob = createCronJob(true, Set.of(baseSiteModel1, baseSiteModel3), Set.of(PROVIDER1));
		cronJob.setAllBaseSites(true);

		//when
		final PerformResult result = job.perform(cronJob);

		//then
		assertEquals(CronJobResult.SUCCESS, result.getResult());
		assertEquals(CronJobStatus.FINISHED, result.getStatus());

		verifyUpdateSegmentContext(cronJob);

	}

	@Test
	public void testPerformWhenEmptyBaseSites()
	{
		//given
		final CxUpdateSegmentsCronJobModel cronJob = createCronJob(true, Collections.emptySet(), Set.of(PROVIDER1));

		//when
		final PerformResult result = job.perform(cronJob);

		//then
		assertEquals(CronJobResult.SUCCESS, result.getResult());
		assertEquals(CronJobStatus.FINISHED, result.getStatus());

		verifyUpdateSegmentContext(cronJob);

	}

	@Test
	public void testPerformWhenNullBaseSites()
	{
		//given
		final CxUpdateSegmentsCronJobModel cronJob = createCronJob(true, null, Set.of(PROVIDER1));

		//when
		final PerformResult result = job.perform(cronJob);

		//then
		assertEquals(CronJobResult.SUCCESS, result.getResult());
		assertEquals(CronJobStatus.FINISHED, result.getStatus());

		verifyUpdateSegmentContext(cronJob);

	}

	@Test
	public void testPerformWhenAllProviders()
	{
		//given
		final CxUpdateSegmentsCronJobModel cronJob = createCronJob(true, Set.of(baseSiteModel1, baseSiteModel3), Set.of(PROVIDER1));
		cronJob.setAllProviders(true);

		//when
		final PerformResult result = job.perform(cronJob);

		//then
		assertEquals(CronJobResult.SUCCESS, result.getResult());
		assertEquals(CronJobStatus.FINISHED, result.getStatus());

		verifyUpdateSegmentContext(cronJob);
	}

	@Test
	public void testPerformWhenEmptyProviders()
	{
		//given
		final CxUpdateSegmentsCronJobModel cronJob = createCronJob(true, Set.of(baseSiteModel1, baseSiteModel3), Collections.emptySet());

		//when
		final PerformResult result = job.perform(cronJob);

		//then
		assertEquals(CronJobResult.SUCCESS, result.getResult());
		assertEquals(CronJobStatus.FINISHED, result.getStatus());

		verifyUpdateSegmentContext(cronJob);

	}

	@Test
	public void testPerformWhenNullProviders()
	{
		//given
		final CxUpdateSegmentsCronJobModel cronJob = createCronJob(true, Set.of(baseSiteModel1, baseSiteModel3),null);

		//when
		final PerformResult result = job.perform(cronJob);

		//then
		assertEquals(CronJobResult.SUCCESS, result.getResult());
		assertEquals(CronJobStatus.FINISHED, result.getStatus());

		verifyUpdateSegmentContext(cronJob);

	}

	protected CxUpdateSegmentsCronJobModel createCronJob(final boolean fullUpdate, final Set<BaseSiteModel> baseSites,
			final Set<String> providers)
	{
		final CxUpdateSegmentsCronJobModel cronJob = new CxUpdateSegmentsCronJobModel();
		cronJob.setBaseSites(baseSites);
		cronJob.setProviders(providers);
		cronJob.setFullUpdate(fullUpdate);
		return cronJob;
	}

	protected void verifyUpdateSegmentContext(final CxUpdateSegmentsCronJobModel cronJob)
	{
		final ArgumentCaptor<CxUpdateSegmentContext> contextCaptor = ArgumentCaptor.forClass(CxUpdateSegmentContext.class);
		verify(cxSegmentService).updateSegments(contextCaptor.capture());
		final CxUpdateSegmentContext context = contextCaptor.getValue();

		assertEquals(cronJob.isFullUpdate(), context.isFullUpdate());
		if (cronJob.isAllBaseSites())
		{
			assertEquals(allBaseSites, context.getBaseSites());
		}
		else
		{
			assertEquals(cronJob.getBaseSites(), context.getBaseSites());
		}

		if (cronJob.isAllProviders())
		{
			assertNull(context.getSegmentProviders());
		}
		else
		{
			assertEquals(cronJob.getProviders(), context.getSegmentProviders());
		}
	}
}
