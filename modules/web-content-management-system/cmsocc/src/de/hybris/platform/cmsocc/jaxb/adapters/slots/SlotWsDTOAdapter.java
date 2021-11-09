/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsocc.jaxb.adapters.slots;

import static de.hybris.platform.cmsocc.jaxb.adapters.slots.SlotAdapterUtil.SlotAdaptedData;

import de.hybris.platform.cmsocc.data.ContentSlotWsDTO;

import java.util.Objects;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * This adapter is used to convert {@link ContentSlotWsDTO} into {@link SlotAdaptedData}
 */
public class SlotWsDTOAdapter extends XmlAdapter<SlotAdaptedData, ContentSlotWsDTO>
{
	@Override
	public SlotAdaptedData marshal(final ContentSlotWsDTO slot)
	{
		return Objects.nonNull(slot) ? convert(slot) : null;
	}

	protected SlotAdaptedData convert(final ContentSlotWsDTO slot)
	{
		return SlotAdapterUtil.convert(slot);
	}

	@Override
	public ContentSlotWsDTO unmarshal(final SlotAdaptedData slotAdaptedData)
	{
		throw new UnsupportedOperationException();
	}
}
