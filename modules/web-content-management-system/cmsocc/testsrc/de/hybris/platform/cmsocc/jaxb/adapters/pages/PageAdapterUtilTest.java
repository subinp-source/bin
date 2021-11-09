/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsocc.jaxb.adapters.pages;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cmsocc.data.CMSPageWsDTO;
import de.hybris.platform.cmsocc.data.ContentSlotListWsDTO;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class PageAdapterUtilTest
{
	private static final String UID = "uid";
	private static final String UUID = "uuid";
	private static final String TITLE = "title";
	private static final String TEMPLATE = "template";
	private static final String TYPECODE = "typeCode";
	private static final String NAME = "name";
	private static final Boolean DEFAULT_PAGE = Boolean.TRUE;
	private static final String CATALOG_VERSION_UUID = "catalogVersionUuid";
	private static final String OTHER_PROPERTY_KEY = "otherPropertyKey";
	private static final String OTHER_PROPERTY_VALUE = "otherPropertyValue";


	@Mock
	private ContentSlotListWsDTO contentSlotListWsDTO;

	CMSPageWsDTO pageDTO;

	@Before
	public void setUp()
	{
		final Map<String, Object> otherProperties = new HashMap<>();
		otherProperties.put(OTHER_PROPERTY_KEY, OTHER_PROPERTY_VALUE);

		pageDTO = new CMSPageWsDTO();
		pageDTO.setUid(UID);
		pageDTO.setUuid(UUID);
		pageDTO.setTitle(TITLE);
		pageDTO.setTemplate(TEMPLATE);
		pageDTO.setTypeCode(TYPECODE);
		pageDTO.setName(NAME);
		pageDTO.setDefaultPage(DEFAULT_PAGE);
		pageDTO.setCatalogVersionUuid(CATALOG_VERSION_UUID);
		pageDTO.setContentSlots(contentSlotListWsDTO);
		pageDTO.setOtherProperties(otherProperties);
	}

	@Test
	public void shouldConvertCMSPageWsDtoToPageAdaptedData()
	{
		// WHEN
		final PageAdapterUtil.PageAdaptedData pageAdaptedData = PageAdapterUtil.convert(pageDTO);

		// THEN
		assertThat(pageAdaptedData, is(notNullValue()));
		assertThat(pageAdaptedData.uid, equalTo(UID));
		assertThat(pageAdaptedData.uuid, equalTo(UUID));
		assertThat(pageAdaptedData.title, equalTo(TITLE));
		assertThat(pageAdaptedData.template, equalTo(TEMPLATE));
		assertThat(pageAdaptedData.typeCode, equalTo(TYPECODE));
		assertThat(pageAdaptedData.name, equalTo(NAME));
		assertThat(pageAdaptedData.defaultPage, equalTo(DEFAULT_PAGE));
		assertThat(pageAdaptedData.catalogVersionUuid, equalTo(CATALOG_VERSION_UUID));
		assertThat(pageAdaptedData.contentSlots, equalTo(contentSlotListWsDTO));
		assertFalse(pageAdaptedData.entries.isEmpty());
	}

	@Test
	public void shouldNotPopulateEntriesIfOtherPropertiesIsNull()
	{
		// GIVEN
		pageDTO.setOtherProperties(null);

		// WHEN
		final PageAdapterUtil.PageAdaptedData pageAdaptedData = PageAdapterUtil.convert(pageDTO);

		// THEN
		assertTrue(pageAdaptedData.entries.isEmpty());
	}
}
