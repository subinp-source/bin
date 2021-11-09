/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.navigationentrytypes;


import de.hybris.platform.cmsfacades.data.NavigationEntryTypeData;

import java.util.List;


/**
 * Navigation Entry Types facade interface which deals with methods related to navigation node entry types operations.
 * @deprecated since 1811 - no longer needed
 */
@Deprecated(since = "1811", forRemoval = true)
public interface NavigationEntryTypesFacade
{

	/**
	 * Get the navigation entry types available. The types are defined in configuration time and are used for clients
	 * that need to know which types that are supported on the current implementation.
	 *
	 * @return a list of {@link NavigationEntryTypeData}
	 */
	List<NavigationEntryTypeData> getNavigationEntryTypes();

}

