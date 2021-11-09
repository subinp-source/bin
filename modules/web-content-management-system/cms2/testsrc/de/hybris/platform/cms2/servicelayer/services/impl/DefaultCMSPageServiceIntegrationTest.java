/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cms2.servicelayer.services.impl;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.data.ContentSlotData;
import de.hybris.platform.cms2.servicelayer.services.CMSPageService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminContentSlotService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminPageService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.core.servicelayer.data.PaginationData;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class DefaultCMSPageServiceIntegrationTest extends ServicelayerTransactionalTest
{
	private static final String ACTIVE_CATALOG_VERSION = "activeCatalogVersion";
	private static final String CONTENT_SLOT = "contentSlot";
	private static final String UID = "uid";

	@Resource
	private SessionService sessionService;
	@Resource
	private ModelService modelService;
	@Resource
	private CMSPageService cmsPageService;
	@Resource
	private CMSAdminSiteService cmsAdminSiteService;
	@Resource
	private CatalogVersionService catalogVersionService;
	@Resource
	private CMSAdminPageService cmsAdminPageService;
	@Resource
	private CMSAdminContentSlotService cmsAdminContentSlotService;

	private ContentSlotModel globalSlot;
	private ContentSlotModel regionSlot;
	private ContentSlotModel localSlot;
	private CatalogVersionModel globalOnlineCatalog;
	private CatalogVersionModel globalStagedCatalog;
	private CatalogVersionModel regionStagedCatalog;
	private CatalogVersionModel localStagedCatalog;
	private ContentPageModel globalHomepage;
	private ContentPageModel localUKHomepage;
	private ContentPageModel regionHomepage;
	private List<CatalogVersionModel> allCatalogVersions;

	@Before
	public void setUp() throws ImpExException
	{
		importCsv("/test/cmsMultiCountryTestData.csv", "UTF-8");

		globalOnlineCatalog = catalogVersionService.getCatalogVersion("MultiCountryTestContentCatalog", "OnlineVersion");
		globalStagedCatalog = catalogVersionService.getCatalogVersion("MultiCountryTestContentCatalog", "StagedVersion");
		regionStagedCatalog = catalogVersionService.getCatalogVersion("MultiCountryTestContentCatalog-region", "StagedVersion");
		localStagedCatalog = catalogVersionService.getCatalogVersion("MultiCountryTestContentCatalog-local", "StagedVersion");
		allCatalogVersions = Arrays.asList(globalOnlineCatalog, regionStagedCatalog, localStagedCatalog);

		globalSlot = cmsAdminContentSlotService.getContentSlotForIdAndCatalogVersions("FooterSlot",
				Arrays.asList(globalOnlineCatalog));
		regionSlot = cmsAdminContentSlotService.getContentSlotForIdAndCatalogVersions("FooterSlot-region",
				Arrays.asList(regionStagedCatalog));
		localSlot = cmsAdminContentSlotService.getContentSlotForIdAndCatalogVersions("FooterSlot-local",
				Arrays.asList(localStagedCatalog));

		final CMSSiteModel site = new CMSSiteModel();
		site.setContentCatalogs(Arrays.asList((ContentCatalogModel) globalOnlineCatalog.getCatalog(),
				(ContentCatalogModel) regionStagedCatalog.getCatalog(), (ContentCatalogModel) localStagedCatalog.getCatalog()));
		site.setActive(Boolean.TRUE);
		site.setUid("MultiCountrySite");
		modelService.save(site);
		cmsAdminSiteService.setActiveSite(site);
	}

	@Test
	public void shouldSortLocalBeforeRegionContentSlot()
	{
		final List<ContentSlotModel> results = cmsPageService
				.getSortedMultiCountryContentSlots(Arrays.asList(globalSlot, regionSlot, localSlot), allCatalogVersions);

		assertThat(results, containsInAnyOrder(localSlot, regionSlot));
	}

	@Test
	public void shouldSortContentSlotBeEmpty_NoLocalizationForRootSlot()
	{
		final List<ContentSlotModel> results = cmsPageService.getSortedMultiCountryContentSlots(Arrays.asList(globalSlot),
				Arrays.asList(globalOnlineCatalog));

		assertThat(results, empty());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void shouldGetContentSlotsForPage()
	{
		// This test should find all content slots defined for the page template and 1 content defined for the page
		sessionService.setAttribute(ACTIVE_CATALOG_VERSION, regionStagedCatalog.getPk());
		catalogVersionService.setSessionCatalogVersions(Arrays.asList(globalStagedCatalog, regionStagedCatalog));
		final AbstractPageModel regionPage = cmsAdminPageService.getPageForIdFromActiveCatalogVersion("TestHomePageRegionEU");

		final Collection<ContentSlotData> results = cmsPageService.getContentSlotsForPage(regionPage);

		final ContentSlotModel expectedOverrideSlot = cmsAdminContentSlotService
				.getContentSlotForIdAndCatalogVersions("Section1Slot-TestHomePageRegionEU", Arrays.asList(regionStagedCatalog));
		final ContentSlotModel expectedGlobalSlot = cmsAdminContentSlotService.getContentSlotForIdAndCatalogVersions("SiteLogoSlot",
				Arrays.asList(globalStagedCatalog));

		assertThat(results,
				hasItems(
						allOf(hasProperty(UID, equalTo(expectedOverrideSlot.getUid())),
								hasProperty(CONTENT_SLOT, equalTo(expectedOverrideSlot))),
						allOf(hasProperty(UID, equalTo(expectedGlobalSlot.getUid())),
								hasProperty(CONTENT_SLOT, equalTo(expectedGlobalSlot)))));
	}

	@Test
	public void shouldFindAllPages()
	{
		final SearchPageData pageableData = createPaginationData();
		catalogVersionService.setSessionCatalogVersions(Arrays.asList(globalStagedCatalog));

		final SearchPageData<AbstractPageModel> result = cmsPageService.findAllPages(AbstractPageModel._TYPECODE, pageableData);

		assertThat(result.getPagination().getHasNext(), equalTo(Boolean.TRUE));
		assertThat(result.getPagination().getTotalNumberOfResults(), greaterThanOrEqualTo(15l));
		assertThat(result.getPagination().getNumberOfPages(), greaterThanOrEqualTo(3));
	}

	protected SearchPageData createPaginationData()
	{
		final SearchPageData pageableData = new SearchPageData();
		final PaginationData paginationData = new PaginationData();
		paginationData.setCurrentPage(0);
		paginationData.setPageSize(5);
		paginationData.setNeedsTotal(true);
		pageableData.setPagination(paginationData);
		return pageableData;
	}
}
