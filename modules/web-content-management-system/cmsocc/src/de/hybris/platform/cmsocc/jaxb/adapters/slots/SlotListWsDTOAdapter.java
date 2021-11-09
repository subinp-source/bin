/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsocc.jaxb.adapters.slots;

import static de.hybris.platform.cmsocc.jaxb.adapters.slots.SlotAdapterUtil.SlotAdaptedData;
import static de.hybris.platform.cmsocc.jaxb.adapters.slots.SlotListWsDTOAdapter.SlotListAdaptedComponents;
import static java.util.stream.Collectors.toList;

import de.hybris.platform.cmsocc.data.ContentSlotListWsDTO;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.eclipse.persistence.jaxb.JAXBMarshaller;

/**
 * This adapter is used to convert {@link ContentSlotListWsDTO} into {@link SlotListAdaptedComponents}
 */
public class SlotListWsDTOAdapter extends XmlAdapter<SlotListAdaptedComponents, ContentSlotListWsDTO>
{
	public static class SlotListAdaptedComponents
	{
		@XmlElement(name = "contentSlot")
		public List<SlotAdaptedData> contentSlot = new ArrayList<>();

		void beforeMarshal(final Marshaller m)
		{
			((JAXBMarshaller) m).getXMLMarshaller().setReduceAnyArrays(true);
		}

		void afterMarshal(final Marshaller m)
		{
			((JAXBMarshaller) m).getXMLMarshaller().setReduceAnyArrays(true);
		}
	}

	@Override
	public SlotListAdaptedComponents marshal(final ContentSlotListWsDTO contentSlots)
	{
		if (contentSlots == null || contentSlots.getContentSlot() == null)
		{
			return null;
		}
		final SlotListAdaptedComponents slotListAdaptedComponents = new SlotListAdaptedComponents();

		final List<SlotAdaptedData> convertedSlots = contentSlots.getContentSlot() //
				.stream() //
				.map(SlotAdapterUtil::convert) //
				.collect(toList());

		slotListAdaptedComponents.contentSlot.addAll(convertedSlots);

		return slotListAdaptedComponents;
	}

	@Override
	public ContentSlotListWsDTO unmarshal(final SlotListAdaptedComponents slotListAdaptedComponents)
	{
		throw new UnsupportedOperationException();
	}
}
