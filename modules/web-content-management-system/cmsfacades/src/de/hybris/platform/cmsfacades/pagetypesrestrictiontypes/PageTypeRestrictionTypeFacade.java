/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.pagetypesrestrictiontypes;


import de.hybris.platform.cmsfacades.data.PageTypeRestrictionTypeData;

import java.util.List;


/**
 * Facade for managing pageTypes-restrictionTypes relations.
 */
public interface PageTypeRestrictionTypeFacade
{

	/**
	 * Get a list of restrictions types for all page types.
	 *
	 * @return list of page types - restriction types pairs; never <tt>null</tt>
	 */
	List<PageTypeRestrictionTypeData> getRestrictionTypesForAllPageTypes();

}
