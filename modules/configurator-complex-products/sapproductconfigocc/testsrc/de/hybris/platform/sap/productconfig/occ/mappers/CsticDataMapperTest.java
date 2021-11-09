/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.occ.mappers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.sap.productconfig.facades.CsticData;
import de.hybris.platform.sap.productconfig.facades.CsticValueData;
import de.hybris.platform.sap.productconfig.occ.CsticValueWsDTO;
import de.hybris.platform.sap.productconfig.occ.CsticWsDTO;
import de.hybris.platform.webservicescommons.mapping.FieldSelectionStrategy;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


@UnitTest
public class CsticDataMapperTest
{

	private static final String LANG_DEP_NAME = "Name";
	private static final String ENTRY_FIELD_MASK = "-###";
	@Mock
	private MappingContext context;
	@Mock
	private MapperFacade mapperFacade;
	@Mock
	FieldSelectionStrategy fieldSelectionStrategy;
	@InjectMocks
	CsticDataMapper classUnderTest;
	private final CsticData csticData = new CsticData();
	private final CsticWsDTO csticWsDto = new CsticWsDTO();
	private final CsticValueData domainValue = new CsticValueData();
	private final List<CsticValueData> domainValues = Arrays.asList(domainValue);
	private final CsticValueWsDTO domainValueWs = new CsticValueWsDTO();
	private final List<CsticValueWsDTO> domainValuesWs = Arrays.asList(domainValueWs);

	@Before
	public void initialize()
	{
		MockitoAnnotations.initMocks(this);
		when(fieldSelectionStrategy.shouldMap(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(true);
		when(mapperFacade.mapAsList(domainValues, CsticValueWsDTO.class, context)).thenReturn(domainValuesWs);
		when(mapperFacade.mapAsList(domainValuesWs, CsticValueData.class, context)).thenReturn(domainValues);
		csticData.setLangdepname(LANG_DEP_NAME);
		csticData.setEntryFieldMask(ENTRY_FIELD_MASK);
		csticData.setDomainvalues(domainValues);
	}

	@Test
	public void testMapAtoBLangDepName()
	{
		classUnderTest.mapAtoB(csticData, csticWsDto, context);
		assertEquals(LANG_DEP_NAME, csticWsDto.getLangDepName());
	}

	@Test
	public void testMapAtoBNegativeAllowed()
	{
		classUnderTest.mapAtoB(csticData, csticWsDto, context);
		assertTrue(csticWsDto.isNegativeAllowed());
	}

	@Test
	public void testMapAtoBNegativeAllowedEntryMaskNull()
	{
		csticData.setEntryFieldMask(null);
		classUnderTest.mapAtoB(csticData, csticWsDto, context);
		assertFalse(csticWsDto.isNegativeAllowed());
	}

	@Test
	public void testMapAtoBNegativeAllowedEntryMaskEmpty()
	{
		csticData.setEntryFieldMask("");
		classUnderTest.mapAtoB(csticData, csticWsDto, context);
		assertFalse(csticWsDto.isNegativeAllowed());
	}

	@Test
	public void testMapAtoBDomainValues()
	{
		classUnderTest.mapAtoB(csticData, csticWsDto, context);
		assertEquals(domainValuesWs, csticWsDto.getDomainValues());
	}

	@Test
	public void testMapAtoBNullDomainValues()
	{
		csticData.setDomainvalues(null);
		classUnderTest.mapAtoB(csticData, csticWsDto, context);
		assertNull(csticWsDto.getDomainValues());
	}

	@Test
	public void testMapBtoALangDepName()
	{
		csticWsDto.setLangDepName(LANG_DEP_NAME);
		csticData.setLangdepname(null);
		classUnderTest.mapBtoA(csticWsDto, csticData, context);
		assertEquals(LANG_DEP_NAME, csticData.getLangdepname());
	}

	@Test
	public void testMapBtoAEntryMask()
	{
		csticWsDto.setNegativeAllowed(true);
		csticData.setEntryFieldMask(null);
		classUnderTest.mapBtoA(csticWsDto, csticData, context);
		assertNull(csticData.getEntryFieldMask());
	}

	@Test
	public void testMapBtoADomainValues()
	{
		csticWsDto.setDomainValues(domainValuesWs);
		csticData.setDomainvalues(null);
		classUnderTest.mapBtoA(csticWsDto, csticData, context);
		assertEquals(domainValues, csticData.getDomainvalues());
	}

	@Test
	public void testMapBtoANullDomainValues()
	{
		csticWsDto.setDomainValues(null);
		csticData.setDomainvalues(null);
		classUnderTest.mapBtoA(csticWsDto, csticData, context);
		assertNull(csticData.getDomainvalues());
	}


}
