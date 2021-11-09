/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.pages.comparator;


import de.hybris.platform.cmsfacades.data.AbstractPageData;

import java.util.Comparator;


/**
 * Compare {@link AbstractPageData} by name and sort by ascending (alphabetical) order.
 * 
 * @deprecated since 6.6
 */
@Deprecated(since = "6.6", forRemoval = true)
public class PageNameComparator implements Comparator<AbstractPageData>
{

	@Override
	public int compare(final AbstractPageData page1, final AbstractPageData page2)
	{
		return page1.getName().compareTo(page2.getName());
	}

}
