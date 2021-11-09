/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.pages.service;

import java.util.Optional;


/**
 * Registry that holds the {@code PageTypeMapping} to be used for each type of page.
 * 
 * @deprecated since 6.6
 */
@Deprecated(since = "6.6", forRemoval = true)
public interface PageTypeMappingRegistry
{
	/**
	 * Get the page type mapping given a page type code.
	 *
	 * @param typecode
	 *           - the type code of a page
	 * @return the matching page type mapping
	 */
	Optional<PageTypeMapping> getPageTypeMapping(String typecode);
}
