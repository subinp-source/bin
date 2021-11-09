/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.personalizationyprofile.segment;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.personalizationservices.data.BaseSegmentData;
import de.hybris.platform.personalizationyprofile.mapper.impl.CxProviderSegmentsMapper;
import de.hybris.platform.personalizationyprofile.yaas.Segment;
import de.hybris.platform.personalizationyprofile.yaas.client.CxSegmentServiceClient;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.hybris.charon.exp.HttpException;


@UnitTest
public class ConsumptionLayerSegmentsProviderTest
{
	private final static String SEGMENT_DESC = "desc";
	private final static String SEGMENT_NAM = "name";
	private final static int SEGMENT_COUNT = 5;

	private final ConsumptionLayerSegmentsProvider consumptionLayerSegmentsProvider = new ConsumptionLayerSegmentsProvider();

	@Mock
	private CxSegmentServiceClient cxSegmentServiceClient;
	@Mock
	private Converter<Object, BaseSegmentData> converter;


	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		consumptionLayerSegmentsProvider.setCxSegmentServiceClient(cxSegmentServiceClient);
		consumptionLayerSegmentsProvider.setConverter(converter);
	}

	@Test
	public void testHappyPath()
	{
		// given
		final List<Segment> segments = createSegments();
		when(cxSegmentServiceClient.getSegments()).thenReturn(segments);

		// when
		final Optional<List<BaseSegmentData>> result = consumptionLayerSegmentsProvider.getSegments();

		// then
		verify(converter, Mockito.times(1)).convertAll(segments);
	}


	@Test
	public void testGetSegmentsWhenNoMappers()
	{
		//given
		consumptionLayerSegmentsProvider.setConverter(null);

		//when
		final Optional<List<BaseSegmentData>> result = consumptionLayerSegmentsProvider.getSegments();

		//then
		assertTrue("Segment mapping list optional should be empty", result.isEmpty());
	}


	@Test
	public void testGetUserSegmentsWhenCharonHttpException()
	{
		// given
		when(cxSegmentServiceClient.getSegments()).thenThrow(new HttpException(Integer.valueOf(400), "Bad request"));

		// when
		final Optional<List<BaseSegmentData>> result = consumptionLayerSegmentsProvider.getSegments();

		// then
		assertTrue("Segment mapping list optional should be empty", result.isEmpty());
	}


	private List<Segment> createSegments()
	{
		final List<Segment> segments = new ArrayList<>();
		for (int i = 0; i < SEGMENT_COUNT; i++)
		{
			final Segment segment = new Segment();
			segment.put(CxProviderSegmentsMapper.FIELD_CODE, SEGMENT_NAM + i);
			segment.put(CxProviderSegmentsMapper.FIELD_DESCRITPION, SEGMENT_DESC + i);
			segments.add(segment);
		}

		return segments;
	}

}
