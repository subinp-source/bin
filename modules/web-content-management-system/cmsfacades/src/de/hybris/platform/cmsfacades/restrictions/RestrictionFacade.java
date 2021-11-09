/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.restrictions;


import de.hybris.platform.cmsfacades.data.RestrictionTypeData;

import java.util.List;


/**
 * Restriction facade interface which deals with methods related to restriction operations.
 */
public interface RestrictionFacade
{

	/**
	 * Find all restriction types.
	 *
	 * @return list of all {@link RestrictionTypeData}; never <code>null</code>
	 */
	List<RestrictionTypeData> findAllRestrictionTypes();

}
