/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.integrationkey;

import de.hybris.platform.integrationservices.model.TypeDescriptor;

import java.util.Map;

import org.apache.olingo.odata2.api.ep.entry.ODataEntry;

/**
 * Temporary interface that will be removed when retrieval refactoring of OData dependent code is made.
 */
public interface ODataEntryToMapConverter
{
	/**
	 * Converts the attributes from the ODataEntry to a Map<String, Object>
	 *
	 * @param typeDesc describes integration object item
	 * @param entry payload of the request/response for the item
	 * @return a Map representation of the entry properties that can be part of the Item's integrationKey
	 */
	Map<String, Object> convert(TypeDescriptor typeDesc, ODataEntry entry);
}
