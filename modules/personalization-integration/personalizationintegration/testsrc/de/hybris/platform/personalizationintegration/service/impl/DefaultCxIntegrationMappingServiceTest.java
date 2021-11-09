/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.personalizationintegration.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.personalizationintegration.mapping.MappingData;
import de.hybris.platform.personalizationintegration.mapping.SegmentMappingData;
import de.hybris.platform.personalizationservices.model.CxSegmentModel;
import de.hybris.platform.personalizationservices.segment.CxSegmentService;
import de.hybris.platform.personalizationservices.segment.CxUserSegmentService;
import de.hybris.platform.personalizationservices.segment.CxUserSegmentSessionService;
import de.hybris.platform.personalizationservices.segment.converters.CxUserSegmentConversionHelper;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.site.BaseSiteService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class DefaultCxIntegrationMappingServiceTest
{
	private DefaultCxIntegrationMappingService service;

	@Mock
	private Map<String, Converter<Object, MappingData>> converters;

	@Mock
	private CxSegmentService cxSegmentService;

	@Mock
	private CxUserSegmentService cxUserSegmentService;

	@Mock
	private CxUserSegmentSessionService cxUserSegmentSessionService;

	@Mock
	private CxUserSegmentConversionHelper cxUserSegmentConversionHelper;

	@Mock
	private ModelService modelService;

	@Mock
	private BaseSiteService baseSiteService;

	private UserModel user;

	private List<SegmentMappingData> dataList;

	private MappingData mappingData;

	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);

		service = new DefaultCxIntegrationMappingService();
		service.setConverters(converters);
		service.setCxSegmentService(cxSegmentService);
		service.setCxUserSegmentService(cxUserSegmentService);
		service.setCxUserSegmentSessionService(cxUserSegmentSessionService);
		service.setCxUserSegmentConversionHelper(cxUserSegmentConversionHelper);
		service.setModelService(modelService);
		service.setBaseSiteService(baseSiteService);

		user = new UserModel();

		dataList = new ArrayList<>();
		final SegmentMappingData data1 = new SegmentMappingData();
		final SegmentMappingData data2 = new SegmentMappingData();
		final SegmentMappingData data3 = new SegmentMappingData();
		dataList.add(data1);
		dataList.add(data2);
		dataList.add(data3);

		mappingData = new MappingData();
		mappingData.setSegments(dataList);

		when(modelService.create(CxSegmentModel.class)).thenReturn(new CxSegmentModel());
	}

	@Test
	public void testAssignSegmentsToUserWithNonsaveableSegment()
	{
		//given

		//throw an exception only on the first modelService.save() call to simulate saving problem with the first segment
		doThrow(new ModelSavingException("ModelSavingException"))//
				.doNothing()//
				.doNothing()//
				.when(modelService).save(anyObject());

		when(cxSegmentService.getSegment(anyString())).thenReturn(Optional.of(new CxSegmentModel()));

		//when

		service.assignSegmentsToUser(user, mappingData, true, null);

		//then

		verify(modelService, times(3)).save(anyObject());
	}

	@Test
	public void testSegmentExistsForNonsaveableSegmentWhichDoesNotExistInDB()
	{
		//given

		final Map<String, CxSegmentModel> segmentsModelMap = new HashMap<>();
		final SegmentMappingData segmentMappingData = new SegmentMappingData();
		segmentMappingData.setCode("dummySegmentCode");

		when(cxSegmentService.getSegment(segmentMappingData.getCode())).thenReturn(Optional.empty());

		doThrow(new ModelSavingException("ModelSavingException")).when(modelService).save(anyObject());

		//when

		final boolean segmentExists = service.segmentExists(segmentMappingData, segmentsModelMap, true);

		//then

		assertFalse(segmentExists);
	}

	@Test
	public void testSegmentExistsForExistingSegmentWhichIsNotPresentInSegmentsModelMap()
	{
		//given

		final Map<String, CxSegmentModel> segmentsModelMap = new HashMap<>();
		final SegmentMappingData segmentMappingData = new SegmentMappingData();
		segmentMappingData.setCode("dummySegmentCode");

		when(cxSegmentService.getSegment(segmentMappingData.getCode())).thenReturn(Optional.of(new CxSegmentModel()));

		doThrow(new ModelSavingException("ModelSavingException")).when(modelService).save(anyObject());

		//when

		final boolean segmentExists = service.segmentExists(segmentMappingData, segmentsModelMap, true);

		//then

		assertTrue(segmentExists);
	}
}
