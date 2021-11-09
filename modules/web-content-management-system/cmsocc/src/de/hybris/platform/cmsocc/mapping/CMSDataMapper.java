/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsocc.mapping;

import de.hybris.platform.webservicescommons.mapping.DataMapper;


/**
 * Interface to convert a source cms object to a JSON or XML using jaxb.
 */
public interface CMSDataMapper extends DataMapper
{
	/**
	 * Method to convert a source object to a target object using jaxb.
	 * @param data the source object
	 * @param fields the list of fields to populate in the target object
	 * @return the target object
	 */
	Object map(final Object data, final String fields);
}
