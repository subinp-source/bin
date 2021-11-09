/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bacceleratorfacades.search.converters.impl;

import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;


/**
 * Defines common implementation of conversion from SearchPageData to Data (DTO) object.
 */
public abstract class AbstractB2BFlexibleSearchConverter<T> implements Converter<SearchPageData, T>
{

	@Override
	public T convert(final SearchPageData searchPageData) throws ConversionException
	{
		return convert(searchPageData, createDataObject());
	}


	/**
	 * Returns empty template instance for the conversion target
	 */
	protected abstract T createDataObject();

}
