/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.personalizationservices.segment.impl;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.personalizationservices.consent.CxConsentService;
import de.hybris.platform.personalizationservices.model.CxSegmentModel;
import de.hybris.platform.personalizationservices.segment.CxUserSegmentService;
import de.hybris.platform.personalizationservices.segment.CxUserSegmentSessionService;
import de.hybris.platform.personalizationservices.segment.converters.CxUserSegmentConversionHelper;
import de.hybris.platform.personalizationservices.segment.dao.CxSegmentDao;
import de.hybris.platform.personalizationservices.segment.dao.CxUserToSegmentDao;
import de.hybris.platform.personalizationservices.strategies.UpdateUserSegmentStrategy;
import de.hybris.platform.servicelayer.search.exceptions.FlexibleSearchException;
import de.hybris.platform.site.BaseSiteService;

import java.util.Collection;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class DefaultCxSegmentServiceTest
{
	private DefaultCxSegmentService service;

	@Mock
	private CxConsentService cxConsentService;

	@Mock
	private BaseSiteService baseSiteService;

	@Mock
	private CxUserSegmentService cxUserSegmentService;

	@Mock
	private CxUserSegmentSessionService cxUserSegmentSessionService;

	@Mock
	private CxUserSegmentConversionHelper cxUserSegmentConversionHelper;

	@Mock
	private CxSegmentDao cxSegmentDao;

	@Mock
	private CxUserToSegmentDao cxUserToSegmentDao;

	@Mock
	private UpdateUserSegmentStrategy updateUserSegmentStrategy;

	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);

		service = new DefaultCxSegmentService();
		service.setCxConsentService(cxConsentService);
		service.setBaseSiteService(baseSiteService);
		service.setCxUserSegmentService(cxUserSegmentService);
		service.setCxUserSegmentSessionService(cxUserSegmentSessionService);
		service.setCxUserSegmentConversionHelper(cxUserSegmentConversionHelper);
		service.setCxSegmentDao(cxSegmentDao);
		service.setCxUserToSegmentDao(cxUserToSegmentDao);
		service.setUpdateUserSegmentStrategy(updateUserSegmentStrategy);
	}

	@Test
	public void testGetSegmentsForInvalidCodes()
	{
		//given

		when(cxSegmentDao.findSegmentsByCodes(anyObject())).thenThrow(new FlexibleSearchException("FlexibleSearchException"));

		//when

		final Collection<CxSegmentModel> segmentModels = service.getSegmentsForCodes(Collections.emptyList());

		//then

		assertTrue(segmentModels.isEmpty());
	}
}
