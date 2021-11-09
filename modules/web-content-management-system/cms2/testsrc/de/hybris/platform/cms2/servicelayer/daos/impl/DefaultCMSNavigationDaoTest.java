/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cms2.servicelayer.daos.impl;

import de.hybris.bootstrap.annotations.UnitTest;

import org.junit.Before;
import org.junit.Test;

/**
 *
 */
@UnitTest
public class DefaultCMSNavigationDaoTest
{
	private DefaultCMSNavigationDao cmsNavigationDao;

	@Before
	public void createSampleCatalogStructure()
	{
		cmsNavigationDao = new DefaultCMSNavigationDao();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFindPageNavigationEntriesByUid_nullUid()
	{
		// WHEN
		cmsNavigationDao.findNavigationEntryByUid(null, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFindPageNavigationEntriesByUid_nullCatalogVersion()
	{
		// WHEN
		cmsNavigationDao.findNavigationEntryByUid("UID", null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFindPageNavigationEntriesByContentPage_nullContentPageParam()
	{
		// WHEN
		cmsNavigationDao.findNavigationEntriesByPage(null);
	}
}
