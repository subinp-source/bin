/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved
 */
package de.hybris.platform.cockpit.services.sync.impl;

import de.hybris.platform.testframework.HybrisJUnit4Test;

import org.junit.Before;
import org.junit.Test;

import junit.framework.Assert;


/**
 * Simple test for PLA-12631
 */
public class SynchronizationServiceSearchRestrictionConfigurationTest extends HybrisJUnit4Test
{

	private SynchronizationServiceImpl synchronizationService;

	@Before
	public void setUp() throws Exception
	{
		synchronizationService = new SynchronizationServiceImpl();
	}

	@Test
	public void testSearchRestrictionsConfiguration()
	{
		Assert.assertTrue("SearchRestrictions should be disabled by default", synchronizationService.isSearchRestrictionDisabled()
				.booleanValue());

		synchronizationService.setSearchRestrictionsDisabled(false);
		Assert.assertFalse("By configuration, SearchRestrictions should NOT be disabled", synchronizationService
				.isSearchRestrictionDisabled().booleanValue());

		synchronizationService.setSearchRestrictionsDisabled(true);
		Assert.assertTrue("By configuration, SearchRestrictions should be disabled", synchronizationService
				.isSearchRestrictionDisabled().booleanValue());
	}
}
