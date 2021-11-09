/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsocc.jaxb.adapters.slots;

import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cmsocc.data.ContentSlotWsDTO;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class SlotWsDTOAdapterTest
{
	@InjectMocks
	@Spy
	private SlotWsDTOAdapter slotWsDTOAdapter;

	@Test
	public void shouldReturnNullIfContentSlotWsDTOIsNull()
	{
		// WHEN
		final SlotAdapterUtil.SlotAdaptedData marshal = slotWsDTOAdapter.marshal(null);

		// THEN
		assertThat(marshal, nullValue());
	}

	@Test
	public void shouldCallSlotAdapterUtilToConvertSlot()
	{
		// GIVEN
		final ContentSlotWsDTO slot = new ContentSlotWsDTO();

		// WHEN
		slotWsDTOAdapter.marshal(slot);

		// THEN
		verify(slotWsDTOAdapter).convert(slot);
	}
}
