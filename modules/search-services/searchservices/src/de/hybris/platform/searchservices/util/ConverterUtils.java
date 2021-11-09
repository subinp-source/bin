/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.util;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;


/**
 * Converter utilities.
 */
public class ConverterUtils
{
	private ConverterUtils()
	{
		// utility class
	}

	/**
	 * Convert a single source value using the converter function.
	 *
	 * @param source
	 *           - source value
	 * @param converter
	 *           - converter function
	 * @param <S>
	 *           - source type
	 * @param <T>
	 *           - target type
	 * @return converted value
	 */
	public static final <S, T> T convert(final S source, final Function<S, T> converter)
	{
		if (source == null)
		{
			return null;
		}

		return converter.apply(source);
	}

	/**
	 * Convert a collection of source values using the converter function.
	 *
	 * @param source
	 *           - source value
	 * @param converter
	 *           - converter function
	 * @param <S>
	 *           - source type
	 * @param <T>
	 *           - target type
	 * @return list of converted values
	 */
	public static final <S, T> List<T> convertAll(final Collection<? extends S> source, final Function<S, T> converter)
	{
		if (source == null || source.isEmpty())
		{
			return Collections.emptyList();
		}

		return source.stream().map(converter::apply).collect(Collectors.toList());
	}

	/**
	 * Convert a collection of source values using the converter function.
	 *
	 * @param source
	 *           - source value
	 * @param converter
	 *           - converter function
	 * @param filter
	 *           - filter
	 * @param <S>
	 *           - source type
	 * @param <T>
	 *           - target type
	 * @return list of converted values
	 */
	public static final <S, T> List<T> convertAll(final Collection<? extends S> source, final Function<S, T> converter,
			final Predicate<? super S> filter)
	{
		if (source == null || source.isEmpty())
		{
			return Collections.emptyList();
		}

		return source.stream().filter(filter).map(converter::apply).collect(Collectors.toList());
	}

	/**
	 * Convert a single source value using the converter function and an additional parameter.
	 *
	 * @param first
	 *           - additional parameter
	 * @param source
	 *           - source value
	 * @param converter
	 *           - converter function
	 * @param <P>
	 *           - parameter type
	 * @param <S>
	 *           - source type
	 * @param <T>
	 *           - target type
	 * @return converted value
	 */
	public static final <P, S, T> T convert(final P first, final S source, final BiFunction<P, S, T> converter)
	{
		if (source == null)
		{
			return null;
		}

		return converter.apply(first, source);
	}

	/**
	 * Convert a collection of source value using the converter function and an additional parameter.
	 *
	 * @param first
	 *           additional parameter
	 * @param source
	 *           source value
	 * @param converter
	 *           converter function
	 * @param <P>
	 *           parameter type
	 * @param <S>
	 *           source type
	 * @param <T>
	 *           target type
	 * @return list of converted values
	 */
	public static final <P, S, T> List<T> convertAll(final P first, final Collection<S> source,
			final BiFunction<P, S, T> converter)
	{
		return CollectionUtils.isEmpty(source) ? Collections.emptyList()
				: source.stream().map(s -> converter.apply(first, s)).collect(Collectors.toList());
	}
}
