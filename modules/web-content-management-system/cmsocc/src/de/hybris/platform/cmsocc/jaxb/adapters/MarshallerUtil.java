/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsocc.jaxb.adapters;

import static java.util.stream.Collectors.toList;

import de.hybris.platform.cmsocc.data.ComponentWsDTO;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.apache.commons.lang3.ClassUtils;

import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * Utility class to use with marshaling.
 */
public class MarshallerUtil
{

	private static final String UID = "uid";
	private static final String UUID = "uuid";
	private static final String TYPE_CODE = "typeCode";
	private static final String NAME = "name";

	/**
	 * Converts map with String/Object pairs into a list of KeyMapAdaptedEntry
	 *
	 * @param map
	 *           the source map to be converted
	 * @return List<KeyMapAdaptedEntry>
	 */
	public static List<KeyMapAdaptedEntryAdapter.KeyMapAdaptedEntry> marshalMap(final Map<String, Object> map)
	{
		return map.entrySet().stream().filter(entry -> entryContainsNullPredicate.negate().test(entry)) //
				.map(MarshallerUtil::convertToAdaptedEntry) //
				.collect(toList());
	}

	/**
	 * Predicate to test whether the {@link java.util.Map.Entry} contains null value.
	 */
	protected static Predicate<Map.Entry<String, Object>> entryContainsNullPredicate = entry -> entry.getValue() == null;


	/**
	 * Method tests whether the {@link Object} is a primitive type or a string.
	 *
	 * @param valueObj
	 *           the {@link Object} to test
	 * @return true if the {@link Object} is a primitive type or a string, false otherwise.
	 */
	protected static boolean isPrimitive(final Object valueObj)
	{
		return ClassUtils.isPrimitiveOrWrapper(valueObj.getClass()) || valueObj.getClass() == String.class;
	}

	/**
	 * Method tests whether the {@link Object} is a {@link Collection}.
	 *
	 * @param valueObj
	 *           the {@link Object} to test
	 * @return true if the {@link Object} is a {@link Collection}, false otherwise.
	 */
	protected static boolean isCollection(final Object valueObj)
	{
		return valueObj instanceof Collection;
	}

	/**
	 * Method tests whether the {@link Object} is a {@link Map}.
	 *
	 * @param valueObj
	 *           the {@link Object} to test
	 * @return true if the {@link Object} is a {@link Map}, false otherwise.
	 */
	protected static boolean isMap(final Object valueObj)
	{
		return valueObj instanceof Map;
	}

	public static Map<String, Object> convertNestedComponentToMap(final ComponentWsDTO componentDTO)
	{
		final Map<String, Object> map = new HashMap<>();
		map.put(NAME, componentDTO.getName());
		map.put(UUID, componentDTO.getUuid());
		map.put(UID, componentDTO.getUid());
		map.put(TYPE_CODE, componentDTO.getTypeCode());
		map.putAll(componentDTO.getOtherProperties());

		return map;
	}

	/**
	 * Convert {@link java.util.Map.Entry} entry to {@link KeyMapAdaptedEntryAdapter.KeyMapAdaptedEntry} object
	 *
	 * @param entry
	 *           the {@link java.util.Map.Entry} to convert.
	 * @return the {@link KeyMapAdaptedEntryAdapter.KeyMapAdaptedEntry} object
	 */
	public static KeyMapAdaptedEntryAdapter.KeyMapAdaptedEntry convertToAdaptedEntry(final Map.Entry<String, Object> entry)
	{
		final KeyMapAdaptedEntryAdapter.KeyMapAdaptedEntry adaptedEntry = new KeyMapAdaptedEntryAdapter.KeyMapAdaptedEntry();
		adaptedEntry.key = entry.getKey();
		final Object valueObj = entry.getValue();
		if (isMap(valueObj))
		{
			adaptedEntry.mapValue = marshalMap((Map) valueObj);
		}
		else if (isPrimitive(valueObj))
		{
			adaptedEntry.strValue = valueObj.toString();
		}
		else if (isCollection(valueObj))
		{
			adaptedEntry.arrayValue = ((Collection<Object>) valueObj).stream() //
					.filter(MarshallerUtil::isPrimitive) //
					.map(Object::toString) //
					.collect(toList());
		}
		else if (valueObj instanceof ComponentWsDTO)
		{
			adaptedEntry.mapValue = marshalMap(convertNestedComponentToMap((ComponentWsDTO) valueObj));
		}
		else
		{
			final ObjectMapper objectMapper = new ObjectMapper();
			final Map<String, Object> props = objectMapper.convertValue(valueObj, Map.class);
			adaptedEntry.mapValue = marshalMap(props);
		}
		return adaptedEntry;
	}
}
