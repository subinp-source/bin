/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsoccaddon.jaxb.adapters.pages;

import de.hybris.platform.cmsoccaddon.data.CMSPageWsDTO;
import de.hybris.platform.cmsoccaddon.data.ContentSlotListWsDTO;
import de.hybris.platform.cmsoccaddon.jaxb.adapters.KeyMapAdaptedEntryAdapter;
import de.hybris.platform.cmsoccaddon.jaxb.adapters.MarshallerUtil;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.eclipse.persistence.jaxb.JAXBMarshaller;


/**
 * This class is used by adapters to convert {@link CMSPageWsDTO} into XML/JSON objects.
 */
public class PageAdapterUtil
{
	@XmlRootElement(name = "page")
	public static class PageAdaptedData
	{

		@XmlElement
		String uid;

		@XmlElement
		String uuid;

		@XmlElement
		String title;

		@XmlElement
		String template;

		@XmlElement
		String typeCode;

		@XmlElement
		String name;

		@XmlElement
		String description;

		@XmlElement
		String robotTag;

		@XmlElement
		Boolean defaultPage;

		@XmlElement
		ContentSlotListWsDTO contentSlots;

		@XmlElement
		String catalogVersionUuid;

		@XmlAnyElement
		@XmlJavaTypeAdapter(KeyMapAdaptedEntryAdapter.class)
		List<KeyMapAdaptedEntryAdapter.KeyMapAdaptedEntry> entries = new ArrayList<>();


		void beforeMarshal(final Marshaller m)
		{
			((JAXBMarshaller) m).getXMLMarshaller().setReduceAnyArrays(true);
		}

		void afterMarshal(final Marshaller m)
		{
			((JAXBMarshaller) m).getXMLMarshaller().setReduceAnyArrays(false);
		}
	}

	/**
	 * Converts {@link CMSPageWsDTO} to {@link PageAdaptedData} that can be easily converted to JSON/XML. All attributes
	 * from entries field will be moved to upper level.
	 *
	 * @param pageDTO
	 *           the {@link CMSPageWsDTO} to convert.
	 * @return the {@link PageAdaptedData}.
	 */
	public static PageAdaptedData convert(final CMSPageWsDTO pageDTO)
	{
		return convert(pageDTO, true);
	}

	public static PageAdaptedData convert(final CMSPageWsDTO pageDTO, final boolean includeSlots)
	{
		final PageAdaptedData pageAdaptedData = new PageAdaptedData();
		pageAdaptedData.uid = pageDTO.getUid();
		pageAdaptedData.uuid = pageDTO.getUuid();
		pageAdaptedData.title = pageDTO.getTitle();
		pageAdaptedData.template = pageDTO.getTemplate();
		pageAdaptedData.typeCode = pageDTO.getTypeCode();
		pageAdaptedData.name = pageDTO.getName();
		pageAdaptedData.description = pageDTO.getDescription();
		pageAdaptedData.robotTag = pageDTO.getRobotTag();
		if (includeSlots)
		{
			pageAdaptedData.contentSlots = pageDTO.getContentSlots();
		}
		pageAdaptedData.defaultPage = pageDTO.getDefaultPage();
		pageAdaptedData.catalogVersionUuid = pageDTO.getCatalogVersionUuid();

		if (pageDTO.getOtherProperties() != null)
		{
			pageAdaptedData.entries.addAll(MarshallerUtil.marshalMap(pageDTO.getOtherProperties()));
		}

		return pageAdaptedData;
	}
}
