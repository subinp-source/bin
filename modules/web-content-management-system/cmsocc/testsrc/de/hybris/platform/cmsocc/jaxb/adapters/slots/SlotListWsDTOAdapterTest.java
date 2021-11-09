/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsocc.jaxb.adapters.slots;

import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cmsocc.data.ContentSlotListWsDTO;
import de.hybris.platform.cmsocc.data.ContentSlotWsDTO;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class SlotListWsDTOAdapterTest
{
	@InjectMocks
	private SlotListWsDTOAdapter slotListWsDTOAdapter;

	@Test
	public void shouldReturnNullIfContentSlotListWsDTOIsNull()
	{
		// WHEN
		final SlotListWsDTOAdapter.SlotListAdaptedComponents marshal = slotListWsDTOAdapter.marshal(null);

		// THEN
		assertThat(marshal, nullValue());
	}

	@Test
	public void shouldCreateSlotListAdaptedComponentsAndPopulateContentSlotAttribute()
	{
		// GIVEN
		final ContentSlotWsDTO contentSlotWsDTO = new ContentSlotWsDTO();

		final ContentSlotListWsDTO contentSlotListWsDTO = new ContentSlotListWsDTO();
		contentSlotListWsDTO.setContentSlot(Arrays.asList(contentSlotWsDTO));

		// WHEN
		final SlotListWsDTOAdapter.SlotListAdaptedComponents result = slotListWsDTOAdapter.marshal(contentSlotListWsDTO);

		// THEN
		assertFalse(result.contentSlot.isEmpty());
	}
}
