/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.sites;


import de.hybris.platform.cmsfacades.data.SiteData;

import java.util.Comparator;


/**
 * Implementation of a {@link Comparator} which uses the natural ordering of uid in a {@link SiteData} dto.
 */
public class SiteDataUidComparator implements Comparator<SiteData>
{

	@Override
	public int compare(final SiteData that, final SiteData other)
	{
		return that.getUid().compareTo(other.getUid());
	}
}
