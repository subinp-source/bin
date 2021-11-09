/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsoccaddon.jaxb.adapters.components;

import static java.util.stream.Collectors.toMap;

import de.hybris.platform.cmsoccaddon.data.ComponentWsDTO;
import de.hybris.platform.cmsoccaddon.data.NavigationNodeWsDTO;
import de.hybris.platform.cmsoccaddon.jaxb.adapters.KeyMapAdaptedEntryAdapter;
import de.hybris.platform.cmsoccaddon.jaxb.adapters.KeyMapAdaptedEntryAdapter.KeyMapAdaptedEntry;
import de.hybris.platform.cmsoccaddon.jaxb.adapters.MarshallerUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.eclipse.persistence.jaxb.JAXBMarshaller;


/**
 * This class is used by adapters to convert {@link de.hybris.platform.cmsoccaddon.data.ComponentWsDTO} into XML/JSON
 * objects. Map<String, Object> are represented as a list of
 * {@link de.hybris.platform.cmsoccaddon.jaxb.adapters.KeyMapAdaptedEntryAdapter.KeyMapAdaptedEntry} objects.
 */
public class ComponentAdapterUtil
{

	/**
	 * This class represents the converted ComponentWsDTO data. Except the component common attributes, it contains a
	 * list of {@link de.hybris.platform.cmsoccaddon.jaxb.adapters.KeyMapAdaptedEntryAdapter.KeyMapAdaptedEntry} entries
	 * which hold the component-specific attributes.
	 */
	@XmlRootElement(name = "component")
	public static class ComponentAdaptedData
	{
		@XmlElement
		String uid;

		@XmlElement
		String uuid;

		@XmlElement
		String typeCode;

		@XmlElement
		Date modifiedtime;

		@XmlElement
		String name;

		@XmlAnyElement
		@XmlJavaTypeAdapter(KeyMapAdaptedEntryAdapter.class)
		List<KeyMapAdaptedEntry> entries = new ArrayList<KeyMapAdaptedEntry>();

		//move it out of entries
		@XmlElement
		NavigationNodeAdaptedData navigationNode;

		// When marshal xmlAnyCollection, JSON_REDUCE_ANY_ARRAYS is also considered.
		// We don't want any non-collection object in "List<KeyMapAdaptedEntry>" to be an array.
		void beforeMarshal(final Marshaller m)
		{
			((JAXBMarshaller) m).getXMLMarshaller().setReduceAnyArrays(true);
		}

		void afterMarshal(final Marshaller m)
		{
			((JAXBMarshaller) m).getXMLMarshaller().setReduceAnyArrays(false);
		}

	}

	@XmlRootElement
	public static class NavigationNodeAdaptedData extends NavigationNodeWsDTO
	{
		// for navigation node, JSON_REDUCE_ANY_ARRAYS should be false
		void beforeMarshal(final Marshaller m)
		{
			((JAXBMarshaller) m).getXMLMarshaller().setReduceAnyArrays(false);
		}

		void afterMarshal(final Marshaller m)
		{
			((JAXBMarshaller) m).getXMLMarshaller().setReduceAnyArrays(true);
		}
	}

	/**
	 * convert ComponentWsDTO object into ComponentAdaptedData object
	 *
	 * @param componentDTO
	 *           the source dto to be converted
	 * @return ComponentAdaptedData object
	 */
	public static ComponentAdaptedData convert(final ComponentWsDTO componentDTO)
	{
		final ComponentAdaptedData adaptedComponent = new ComponentAdaptedData();
		adaptedComponent.uid = componentDTO.getUid();
		adaptedComponent.uuid = componentDTO.getUuid();
		adaptedComponent.typeCode = componentDTO.getTypeCode();
		adaptedComponent.modifiedtime = componentDTO.getModifiedtime();
		adaptedComponent.name = componentDTO.getName();

		if (componentDTO.getOtherProperties() != null)
		{
			final Optional<NavigationNodeAdaptedData> firstNavigationNodeAdaptedDataOptional = componentDTO.getOtherProperties() //
					.entrySet().stream() //
					.filter(entry -> entryContainsNavigationNodeWsDTOPredicate.test(entry)) //
					.map(entry -> (NavigationNodeWsDTO) entry.getValue()) //
					.map(ComponentAdapterUtil::convertNavigationNode) //
					.findFirst();

			firstNavigationNodeAdaptedDataOptional.ifPresent((navigationNodeAdaptedData) -> {
				adaptedComponent.navigationNode = navigationNodeAdaptedData;
			});

			final Map<String, Object> propertiesWithoutNav = componentDTO.getOtherProperties() //
					.entrySet() //
					.stream() //
					.filter(entry -> entryContainsNavigationNodeWsDTOPredicate.negate().test(entry)) //
					.collect(toMap(e -> e.getKey(), e -> e.getValue()));

			adaptedComponent.entries.addAll(MarshallerUtil.marshalMap(propertiesWithoutNav));
		}
		return adaptedComponent;
	}

	/**
	 * Converts {@link NavigationNodeWsDTO} to {@Link NavigationNodeAdaptedData}
	 *
	 * @param dto
	 *           the {@link NavigationNodeWsDTO}
	 * @return the {@link NavigationNodeAdaptedData}
	 */
	protected static NavigationNodeAdaptedData convertNavigationNode(final NavigationNodeWsDTO dto)
	{
		final NavigationNodeAdaptedData navigationNodeAdaptedData = new NavigationNodeAdaptedData();
		navigationNodeAdaptedData.setName(dto.getName());
		navigationNodeAdaptedData.setUuid(dto.getUuid());
		navigationNodeAdaptedData.setUid(dto.getUid());
		navigationNodeAdaptedData.setChildren(dto.getChildren());
		navigationNodeAdaptedData.setEntries(dto.getEntries());
		navigationNodeAdaptedData.setTitle(dto.getTitle());
		navigationNodeAdaptedData.setLocalizedTitle(dto.getLocalizedTitle());
		navigationNodeAdaptedData.setPosition(dto.getPosition());
		return navigationNodeAdaptedData;
	}

	/**
	 * Predicate to test whether the {@link java.util.Map.Entry} contains a {@link NavigationNodeWsDTO} as a value.
	 */
	protected static Predicate<Map.Entry<String, Object>> entryContainsNavigationNodeWsDTOPredicate = entry -> entry
			.getValue() instanceof NavigationNodeWsDTO;
}
