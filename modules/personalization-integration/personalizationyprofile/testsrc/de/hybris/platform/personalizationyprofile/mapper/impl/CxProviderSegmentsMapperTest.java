/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.personalizationyprofile.mapper.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.personalizationservices.data.BaseSegmentData;
import de.hybris.platform.personalizationyprofile.yaas.Segment;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


@UnitTest
public class CxProviderSegmentsMapperTest
{
	private final static String SEGMENT_DESC = "desc";
	private final static String SEGMENT_NAM = "name";


	public CxProviderSegmentsMapper mapper = new CxProviderSegmentsMapper();
	private BaseSegmentData target;

	@Mock
	private ConfigurationService configurationService;
	@Mock
	Configuration configuration;


	@Before
	public void init()
	{
		MockitoAnnotations.initMocks(this);
		mapper.setConfigurationService(configurationService);
		target = new BaseSegmentData();

		Mockito.when(configurationService.getConfiguration()).thenReturn(configuration);
	}


	@Test
	public void testHappyPath()
	{
		// given
		Mockito.when(configuration.getBoolean(Mockito.any(), Mockito.anyBoolean())).thenReturn(Boolean.TRUE);

		// when
		mapper.populate(createSegment(), target);

		// then
		assertEquals(SEGMENT_DESC, target.getDescription());
		assertEquals(SEGMENT_NAM, target.getCode());
	}

	@Test
	public void testWhenNotEnabled()
	{
		// given
		Mockito.when(configuration.getBoolean(Mockito.any(), Mockito.anyBoolean())).thenReturn(Boolean.FALSE);

		// when
		mapper.populate(createSegment(), target);

		// then
		assertTrue(StringUtils.isEmpty(target.getCode()));
		assertTrue(StringUtils.isEmpty(target.getDescription()));
		assertTrue(StringUtils.isEmpty(target.getProvider()));
	}


	private Segment createSegment()
	{
		final Segment segment = new Segment();
		segment.setName(SEGMENT_NAM);
		segment.setDescription(SEGMENT_DESC);

		return segment;
	}


}
