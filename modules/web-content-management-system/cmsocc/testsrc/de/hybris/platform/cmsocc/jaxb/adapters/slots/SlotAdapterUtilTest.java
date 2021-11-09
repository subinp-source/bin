/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsocc.jaxb.adapters.slots;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cmsocc.data.ComponentListWsDTO;
import de.hybris.platform.cmsocc.data.ContentSlotWsDTO;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class SlotAdapterUtilTest
{
	private static final String SLOT_ID = "slotId";
	private static final String SLOT_UUID = "slotUuid";
	private static final String POSITION = "position";
	private static final String NAME = "name";
	private static final Boolean SLOT_SHARED = Boolean.TRUE;
	private static final String SLOT_STATUS = "slotStatus";
	private static final String OTHER_PROPERTY_KEY = "otherPropertyKey";
	private static final String OTHER_PROPERTY_VALUE = "otherPropertyValue";

	@Mock
	private ComponentListWsDTO components;

	ContentSlotWsDTO slotDTO;

	@Before
	public void setUp()
	{
		final Map<String, Object> otherProperties = new HashMap<>();
		otherProperties.put(OTHER_PROPERTY_KEY, OTHER_PROPERTY_VALUE);

		slotDTO = new ContentSlotWsDTO();
		slotDTO.setSlotId(SLOT_ID);
		slotDTO.setSlotUuid(SLOT_UUID);
		slotDTO.setPosition(POSITION);
		slotDTO.setName(NAME);
		slotDTO.setSlotShared(SLOT_SHARED);
		slotDTO.setSlotStatus(SLOT_STATUS);
		slotDTO.setComponents(components);
		slotDTO.setOtherProperties(otherProperties);
	}

	@Test
	public void shouldConvertContentSlotWsDTOToSlotAdaptedData()
	{
		// WHEN
		final SlotAdapterUtil.SlotAdaptedData slotAdaptedData = SlotAdapterUtil.convert(slotDTO);

		// THEN
		assertThat(slotAdaptedData, is(notNullValue()));
		assertThat(slotAdaptedData.slotId, equalTo(SLOT_ID));
		assertThat(slotAdaptedData.slotUuid, equalTo(SLOT_UUID));
		assertThat(slotAdaptedData.position, equalTo(POSITION));
		assertThat(slotAdaptedData.name, equalTo(NAME));
		assertThat(slotAdaptedData.slotShared, equalTo(SLOT_SHARED));
		assertThat(slotAdaptedData.slotStatus, equalTo(SLOT_STATUS));
		assertThat(slotAdaptedData.components, is(notNullValue()));
		assertFalse(slotAdaptedData.entries.isEmpty());
	}

	@Test
	public void shouldNotPopulateEntriesIfOtherPropertiesIsNull()
	{
		// GIVEN
		slotDTO.setOtherProperties(null);

		// WHEN
		final SlotAdapterUtil.SlotAdaptedData slotAdaptedData = SlotAdapterUtil.convert(slotDTO);

		// THEN
		assertTrue(slotAdaptedData.entries.isEmpty());
	}
}
