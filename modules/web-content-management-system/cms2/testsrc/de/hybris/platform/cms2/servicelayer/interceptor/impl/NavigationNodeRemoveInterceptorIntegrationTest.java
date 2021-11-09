/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cms2.servicelayer.interceptor.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.navigation.CMSNavigationEntryModel;
import de.hybris.platform.cms2.model.navigation.CMSNavigationNodeModel;
import de.hybris.platform.cms2.servicelayer.services.CMSNavigationService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Optional;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

@IntegrationTest
public class NavigationNodeRemoveInterceptorIntegrationTest extends ServicelayerTest
{
	@Resource
	private CMSNavigationService cmsNavigationService;

	@Resource
	private ModelService modelService;

	@Resource
	private CatalogVersionService catalogVersionService;

	@Resource
	protected CMSAdminSiteService cmsAdminSiteService;

	private final String NAVIGATION_NODE_UID = "SignOutNavNode";
	private final String NAVIGATION_ENTRY_UID = "SignOutNavNodeEntry";
	public static final String CMS_CATALOG = "cms_Catalog";
	public static final String CATALOG_VERSION = "CatalogVersion";

	@Before
	public void setUp() throws Exception
	{
		createCoreData();

		importCsv("/test/cmsNavigationTestData.impex", "utf-8");

		cmsAdminSiteService.setActiveCatalogVersion(CMS_CATALOG, CATALOG_VERSION);
		catalogVersionService.setSessionCatalogVersion(CMS_CATALOG, CATALOG_VERSION);
	}

	@Test
	public void givenNavigationNodeWithNavigationEntryWhenNodeIsDeletedThenEntryMustNotHaveLinkToDeletedNode() throws CMSItemNotFoundException
	{
		// GIVEN
		final CMSNavigationNodeModel navigationNode = cmsNavigationService.getNavigationNodeForId(NAVIGATION_NODE_UID);
		final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion(CMS_CATALOG, CATALOG_VERSION);
		final Optional<CMSNavigationEntryModel> navigationEntryBefore = cmsNavigationService.getNavigationEntryForId(NAVIGATION_ENTRY_UID, catalogVersion);
		assertTrue(navigationEntryBefore.isPresent());
		assertNotNull(navigationEntryBefore.get().getNavigationNode());

		// THEN
		modelService.remove(navigationNode);

		// WHEN
		final Optional<CMSNavigationEntryModel> navigationEntryAfter = cmsNavigationService.getNavigationEntryForId(NAVIGATION_ENTRY_UID, catalogVersion);
		assertTrue(navigationEntryAfter.isPresent());
		assertNull(navigationEntryAfter.get().getNavigationNode());
	}
}
