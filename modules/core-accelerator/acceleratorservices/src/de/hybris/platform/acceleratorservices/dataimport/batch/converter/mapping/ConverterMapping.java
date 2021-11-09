/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.dataimport.batch.converter.mapping;

import de.hybris.platform.acceleratorservices.dataimport.batch.converter.ImpexConverter;
import de.hybris.platform.acceleratorservices.dataimport.batch.task.ImpexTransformerTask;


/**
 * Interface for converters mappings see {@link ImpexTransformerTask}.
 */
public interface ConverterMapping
{
	/**
	 * @return converter associated with this mapping
	 */
	ImpexConverter getConverter();

	/**
	 * @return mapping of associated converter
	 */
	String getMapping();

}
