/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsocc.jaxb.adapters.components;


import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cmsocc.data.ComponentWsDTO;
import de.hybris.platform.cmsocc.data.NavigationNodeWsDTO;
import de.hybris.platform.cmsocc.jaxb.adapters.KeyMapAdaptedEntryAdapter.KeyMapAdaptedEntry;
import de.hybris.platform.cmsocc.jaxb.adapters.components.ComponentAdapterUtil.ComponentAdaptedData;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;



/**
 * JUnit Tests for the ComponentAdapterUtil
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class ComponentAdapterUtilTest
{
	private static final String TEST_UID = "testUid";
	private static final String TEST_UUID = "testUUid";
	private static final String TEST_NAME = "testName";
	private static final String TEST_TYPECODE = "testTypeCode";
	private static final Date TEST_DATE = mock(Date.class);

	@Test
	public void shouldConvertComponentWsDTOToComponentAdaptedData()
	{
		final ComponentWsDTO testComponentDTO = new ComponentWsDTO();

		final Map<String, Object> testOtherProperties = new HashMap<String, Object>();
		final String testOtherPropertiesString1 = "testOtherPropertiesString1";
		final NavigationNodeWsDTO testOtherPropertiesObject1 = new NavigationNodeWsDTO();
		testOtherPropertiesObject1.setUid("test navigation node uid");

		final String testOtherPropertiesString2 = "testOtherPropertiesString2";
		final String testOtherPropertiesObject2 = "testOtherPropertiesObject2";

		testOtherProperties.put(testOtherPropertiesString1, testOtherPropertiesObject1);
		testOtherProperties.put(testOtherPropertiesString2, testOtherPropertiesObject2);

		testComponentDTO.setUid(TEST_UID);
		testComponentDTO.setUuid(TEST_UUID);
		testComponentDTO.setName(TEST_NAME);
		testComponentDTO.setTypeCode(TEST_TYPECODE);
		testComponentDTO.setModifiedtime(TEST_DATE);
		testComponentDTO.setOtherProperties(testOtherProperties);

		final ComponentAdaptedData componentAdaptedDataResult = ComponentAdapterUtil.convert(testComponentDTO);

		assertThat(componentAdaptedDataResult, is(notNullValue()));
		assertThat(componentAdaptedDataResult.uid, equalTo(TEST_UID));
		assertThat(componentAdaptedDataResult.uuid, equalTo(TEST_UUID));
		assertThat(componentAdaptedDataResult.name, equalTo(TEST_NAME));
		assertThat(componentAdaptedDataResult.typeCode, equalTo(TEST_TYPECODE));
		assertThat(componentAdaptedDataResult.modifiedtime, equalTo(TEST_DATE));
		assertThat(componentAdaptedDataResult.navigationNode.getUid(), equalTo(testOtherPropertiesObject1.getUid()));
		assertThat(componentAdaptedDataResult.entries.get(0).strValue, equalTo(testOtherPropertiesObject2));
	}

	@Test
	public void shouldConvertComponentWsDTOWithoutOtherPropertiesToComponentAdaptedData()
	{
		final ComponentWsDTO testComponentDTO = new ComponentWsDTO();
		testComponentDTO.setUid(TEST_UID);
		testComponentDTO.setUuid(TEST_UUID);
		testComponentDTO.setName(TEST_NAME);
		testComponentDTO.setTypeCode(TEST_TYPECODE);
		testComponentDTO.setModifiedtime(TEST_DATE);

		final ComponentAdaptedData componentAdaptedDataResult = ComponentAdapterUtil.convert(testComponentDTO);

		assertThat(componentAdaptedDataResult, is(notNullValue()));
		assertThat(componentAdaptedDataResult.uid, equalTo(TEST_UID));
		assertThat(componentAdaptedDataResult.uuid, equalTo(TEST_UUID));
		assertThat(componentAdaptedDataResult.name, equalTo(TEST_NAME));
		assertThat(componentAdaptedDataResult.typeCode, equalTo(TEST_TYPECODE));
		assertThat(componentAdaptedDataResult.modifiedtime, equalTo(TEST_DATE));
		assertThat(componentAdaptedDataResult.navigationNode, equalTo(null));
		assertThat(componentAdaptedDataResult.entries, Matchers.<KeyMapAdaptedEntry> empty());
	}
}
