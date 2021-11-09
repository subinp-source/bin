/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.pagescontentslots.service;

import de.hybris.platform.cms2.model.relations.CMSRelationModel;

import java.util.Collection;
import java.util.Optional;


/**
 * Registry that stores a collection of <code>PageContentSlotType</code> elements.
 */
public interface PageContentSlotConverterRegistry
{
	/**
	 * Get a specific <code>PageContentSlotConverterType</code> by class type.
	 *
	 * @param classType
	 *           - the class type of the element to retrieve from the registry
	 * @return the element matching the class type
	 */
	Optional<PageContentSlotConverterType> getPageContentSlotConverterType(
			final Class<? extends CMSRelationModel> classType);

	/**
	 * Get all elements in the registry.
	 *
	 * @return all items in the registry; never <tt>null</tt>
	 */
	Collection<PageContentSlotConverterType> getPageContentSlotConverterTypes();
}
