/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsocc.mapping.converters;

import java.util.function.Predicate;

import ma.glasnost.orika.MapperFactory;


/**
 * Interface to convert object of type A to object of type B using jaxb.
 * 
 * @param <DATA>
 *           source type
 * @param <WSDTO>
 *           target type
 */
public interface DataToWsConverter<DATA, WSDTO>
{
	/**
	 * Class that represents a source object
	 * 
	 * @return the {@link Class}
	 */
	Class getDataClass();

	/**
	 * Class that represents a target object
	 * 
	 * @return the {@link Class}
	 */
	Class getWsClass();

	/**
	 * Predicate to verify whether the convert method can be used or not.
	 * 
	 * @return {@code TRUE} when {{@link #convert(Object, String)} can be performed; {@code FALSE} otherwise.
	 */
	Predicate<Object> canConvert();

	/**
	 * Method to convert a source object to a target object.
	 * 
	 * @param source
	 *           the source object
	 * @param fields
	 *           the fields that should be populated in target object
	 * @return the target object
	 */
	WSDTO convert(DATA source, String fields);

	/**
	 * Method to customize conversion between source and target object. This method is called after the basic conversion
	 * is finished.
	 * 
	 * @param factory
	 *           the {@link MapperFactory} that can be used to add new customization.
	 */
	void customize(final MapperFactory factory);
}
