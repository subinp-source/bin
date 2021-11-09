/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.converter;

import java.util.*;

import javax.validation.constraints.NotNull;

/**
 * Default implementation of the ODataEntry attribute value converter, which handles different kinds of attribute values,
 * e.g. a map, a collection, an ODataFeed, etc by delegating to the registered {@link ValueConverter}s.
 */
public class DefaultPayloadAttributeValueConverter implements PayloadAttributeValueConverter
{
	private final List<ValueConverter> valueConverters = new ArrayList<>();

	@Override
	public Object convertAttributeValue(final ConversionParameters parameters)
	{
		return valueConverters.stream()
		                      .filter(converter -> converter.isApplicable(parameters))
		                      .findFirst()
		                      .map(c -> c.convert(parameters))
		                      .orElseGet(parameters::getAttributeValue);
	}

	/**
	 * Injects a list of value converters that will handle different kinds of possible ODataEntry attribute values.
	 * Calling this method subsequently with different lists of converters, resets the previously set converters and
	 * keeps only converters of the last call.
	 *
	 * @param converters a list of converters in the order of how they will be applied for the value conversion. Ideally
	 *                   the converters should be mutually exclusive and only one converter should be responsible for
	 *                   conversion of a given {@link ConversionParameters}. If it's not so, keep in mind that order
	 *                   of the converters is important because once a converter for handling the attribute value is found
	 *                   no other converters will be even assessed.
	 * @see ValueConverter#isApplicable(ConversionParameters)
	 */
	public void setValueConverters(@NotNull final List<ValueConverter> converters)
	{
		valueConverters.clear();
		valueConverters.addAll(converters);
	}
}