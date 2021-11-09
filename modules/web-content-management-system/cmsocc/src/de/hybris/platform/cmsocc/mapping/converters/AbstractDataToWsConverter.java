/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsocc.mapping.converters;

import static java.util.stream.Collectors.toMap;

import de.hybris.platform.cmsocc.mapping.CMSDataMapper;

import java.util.Collection;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Required;

import ma.glasnost.orika.MapperFactory;


/**
 * The abstract implementation of {@link DataToWsConverter} that uses {@link CMSDataMapper} to convert data objects to
 * ws objects.
 *
 * @param <DATA>
 *           the source object to convert
 * @param <WSDTO>
 *           the target object to populate
 */
public abstract class AbstractDataToWsConverter<DATA, WSDTO> implements DataToWsConverter<DATA, WSDTO>
{
	private CMSDataMapper mapper;
	protected String fields;

	@Override
	public Predicate<Object> canConvert()
	{
		return source -> getDataClass().isAssignableFrom(source.getClass());
	}

	@Override
	public WSDTO convert(final DATA source, final String fields)
	{
		this.fields = fields;
		return (WSDTO) getMapper().map(source, getWsClass(), fields);
	}

	/**
	 * Convert a map that contains data objects to a map that contains ws objects.
	 *
	 * @param map
	 *           the map that contains data objects
	 * @return the map that contains ws objects.
	 */
	public Map<String, Object> convertMap(final Map<String, Object> map)
	{
		return map.entrySet().stream() //
				.collect( //
						toMap( //
								entry -> entry.getKey(), //
								entry -> {
									if (isCollection(entry.getValue()))
									{
										return convertCollection((Collection) entry.getValue());
									}
									else if (isMap(entry.getValue()))
									{
										return convertMap((Map<String, Object>) entry.getValue());
									}
									else
									{
										return convertPrimitive(entry.getValue());
									}
								} //
						) //
				);
	}

	/**
	 * Converts primitives from source to target
	 *
	 * @param source
	 *           the source primitive
	 * @return the target primitive
	 */
	public Object convertPrimitive(final Object source)
	{
		return getMapper().map(source, fields);
	}

	/**
	 * Converts collection that contains data objects to a collection that contains ws objects.
	 *
	 * @param source
	 *           the collection that contains data objects
	 * @return the collection that contains ws objects.
	 */
	public Collection<Object> convertCollection(final Collection<Object> source)
	{
		return source.stream() //
				.map(value -> getMapper().map(value, fields)) //
				.collect(Collectors.toList());
	}

	@Override
	public void customize(final MapperFactory factory)
	{
		// no-op
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

	protected CMSDataMapper getMapper()
	{
		return mapper;
	}

	@Required
	public void setMapper(final CMSDataMapper mapper)
	{
		this.mapper = mapper;
	}

}
