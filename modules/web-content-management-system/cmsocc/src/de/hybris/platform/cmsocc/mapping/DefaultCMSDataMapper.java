/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsocc.mapping;

import de.hybris.platform.cmsocc.mapping.converters.DataToWsConverter;
import de.hybris.platform.webservicescommons.mapping.impl.DefaultDataMapper;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import ma.glasnost.orika.MapperFactory;


/**
 * The mapper to convert an object to another object using {@link DefaultDataMapper}.
 * It uses the list of converters for different source types.
 */
public class DefaultCMSDataMapper extends DefaultDataMapper implements CMSDataMapper
{
	private List<DataToWsConverter> converters;

	@Override
	public Object map(final Object data, final String fields) {
		return getConverters().stream() //
				.filter(converter -> converter.canConvert().test(data)) //
				.map(converter -> converter.convert(data, fields)) //
				.findFirst() //
				.orElse(data);
	}

	@Override
	public <S, D> D map(final S sourceObject, final Class<D> destinationClass, final String fields)
	{
		return super.map(sourceObject, destinationClass, createMappingContext(destinationClass, fields));
	}

	@Override
	protected void configure(final MapperFactory factory)
	{
		super.configure(factory);
		getConverters().forEach(converter -> {
			converter.customize(factory);
		});
	}

	protected List<DataToWsConverter> getConverters()
	{
		return converters;
	}

	@Required
	public void setConverters(final List<DataToWsConverter> converters)
	{
		this.converters = converters;
	}
}
