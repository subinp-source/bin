/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsocc.jaxb.adapters.slots;

import static de.hybris.platform.cmsocc.jaxb.adapters.components.ComponentListWsDTOAdapter.ListAdaptedComponents;

import de.hybris.platform.cmsocc.data.ContentSlotWsDTO;
import de.hybris.platform.cmsocc.jaxb.adapters.KeyMapAdaptedEntryAdapter;
import de.hybris.platform.cmsocc.jaxb.adapters.MarshallerUtil;
import de.hybris.platform.cmsocc.jaxb.adapters.components.ComponentListWsDTOAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.eclipse.persistence.jaxb.JAXBMarshaller;

/**
 * This class is used by adapters to convert {@link ContentSlotWsDTO} into XML/JSON objects.
 */
public class SlotAdapterUtil
{
	@XmlRootElement(name = "contentSlot")
	public static class SlotAdaptedData
	{

		@XmlElement
		String slotId;

		@XmlElement
		String slotUuid;

		@XmlElement
		String position;

		@XmlElement
		String name;

		@XmlElement
		Boolean slotShared;

		@XmlElement
		String slotStatus;

		@XmlElement
		ListAdaptedComponents components;

		@XmlAnyElement
		@XmlJavaTypeAdapter(KeyMapAdaptedEntryAdapter.class)
		List<KeyMapAdaptedEntryAdapter.KeyMapAdaptedEntry> entries = new ArrayList<>();


		void beforeMarshal(final Marshaller m)
		{
			((JAXBMarshaller) m).getXMLMarshaller().setReduceAnyArrays(true);
		}

		void afterMarshal(final Marshaller m)
		{
			((JAXBMarshaller) m).getXMLMarshaller().setReduceAnyArrays(true);
		}
	}

	/**
	 * Converts {@link ContentSlotWsDTO} to {@link SlotAdaptedData} that can be easily converted to JSON/XML.
	 * All attributes from entries field will be moved to upper level.
	 * @param slotDTO the {@link ContentSlotWsDTO} to convert.
	 * @return the {@link SlotAdaptedData}.
	 */
	public static SlotAdaptedData convert(final ContentSlotWsDTO slotDTO)
	{
		final ComponentListWsDTOAdapter componentListWsDTOAdapter = new ComponentListWsDTOAdapter();
		final SlotAdaptedData slotAdaptedData = new SlotAdaptedData();
		slotAdaptedData.slotId = slotDTO.getSlotId();
		slotAdaptedData.slotUuid = slotDTO.getSlotUuid();
		slotAdaptedData.position = slotDTO.getPosition();
		slotAdaptedData.name = slotDTO.getName();
		slotAdaptedData.slotShared = slotDTO.getSlotShared();
		slotAdaptedData.slotStatus = slotDTO.getSlotStatus();
		slotAdaptedData.components = componentListWsDTOAdapter.marshal(slotDTO.getComponents());

		if (slotDTO.getOtherProperties() != null)
		{
			slotAdaptedData.entries.addAll(MarshallerUtil.marshalMap(slotDTO.getOtherProperties()));
		}

		return slotAdaptedData;
	}
}
