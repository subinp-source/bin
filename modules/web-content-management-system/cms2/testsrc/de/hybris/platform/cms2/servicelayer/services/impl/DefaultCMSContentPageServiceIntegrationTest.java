/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cms2.servicelayer.services.impl;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.CMSContentPageService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminPageService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class DefaultCMSContentPageServiceIntegrationTest extends ServicelayerTransactionalTest
{
	@Resource
	private CMSContentPageService cmsContentPageService;
	@Resource
	private ModelService modelService;
	@Resource
	private CMSAdminSiteService cmsAdminSiteService;
	@Resource
	private CatalogVersionService catalogVersionService;
	@Resource
	private CMSAdminPageService cmsAdminPageService;

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
		catalogVersionService.setSessionCatalogVersions(allCatalogVersions);

		final CMSSiteModel site = new CMSSiteModel();
		site.setContentCatalogs(Arrays.asList((ContentCatalogModel) globalOnlineCatalog.getCatalog(),
				(ContentCatalogModel) regionStagedCatalog.getCatalog(), (ContentCatalogModel) localStagedCatalog.getCatalog()));
		site.setActive(Boolean.TRUE);
		site.setUid("MultiCountrySite");
		modelService.save(site);
		cmsAdminSiteService.setActiveSite(site);

		globalHomepage = (ContentPageModel) cmsAdminPageService.getPageForId("TestHomePageGlobal", allCatalogVersions);
		localUKHomepage = (ContentPageModel) cmsAdminPageService.getPageForId("TestHomePageLocalUK", allCatalogVersions);
		regionHomepage = (ContentPageModel) cmsAdminPageService.getPageForId("TestHomePageRegionEU", allCatalogVersions);
	}

	@Test
	public void shouldGetHomepageUKWhenUKHasHomePageAndLabelIsDifferentFromParentCatalogHomePage()
	{
		final ContentPageModel homepage = cmsContentPageService.getHomepage();

		assertThat(homepage, equalTo(localUKHomepage));
	}

	@Test
	public void shouldGetHomepageEUWhenEUHasHomePageAndLabelIsSameAsParentCatalogHomePage()
	{
		catalogVersionService.setSessionCatalogVersions(Arrays.asList(globalOnlineCatalog, regionStagedCatalog));

		final ContentPageModel homepage = cmsContentPageService.getHomepage();

		assertThat(homepage, equalTo(regionHomepage));
	}

	@Test
	public void shouldGetHomepageEUWhenUKHasNoHomePage()
	{
		localUKHomepage.setHomepage(false);
		modelService.save(localUKHomepage);

		final ContentPageModel homepage = cmsContentPageService.getHomepage();

		assertThat(homepage, equalTo(regionHomepage));
	}

	@Test
	public void shouldGetHomepageGlobalWhenUKHasNoHomePageButHasPageWithSameLabelAsParentCatalogHomePage()
	{
		localUKHomepage.setHomepage(false);
		localUKHomepage.setLabel("TestHomePageGlobal");
		regionHomepage.setHomepage(false);
		modelService.saveAll(regionHomepage, localUKHomepage);

		final ContentPageModel homepage = cmsContentPageService.getHomepage();

		assertThat(homepage, equalTo(globalHomepage));
	}

	@Test
	public void testFindPageByExactLabelMatch() throws CMSItemNotFoundException
	{
		final ContentPageModel result = cmsContentPageService.getPageForLabelOrIdAndMatchType("/faq/default", true);

		assertThat(result.getUid(), equalTo("testFaqPage1"));
	}

	@Test
	public void testFindPageByBestLabelMatch_WithTrailingSlash() throws CMSItemNotFoundException
	{
		final ContentPageModel result = cmsContentPageService.getPageForLabelOrIdAndMatchType("/faq/default/", false);

		assertThat(result.getUid(), equalTo("testFaqPage2"));
	}

	@Test
	public void testFindPageByBestLabelMatch_IgnoreTrailingSlash() throws CMSItemNotFoundException
	{
		final ContentPageModel result = cmsContentPageService.getPageForLabelOrIdAndMatchType("/faq/default//", false);

		assertThat(result.getUid(), equalTo("testFaqPage2"));
	}

	@Test
	public void testFindPageByBestLabelMatch_MostMatchToParent() throws CMSItemNotFoundException
	{
		final ContentPageModel result = cmsContentPageService.getPageForLabelOrIdAndMatchType("/faq/default/edit", false);

		assertThat(result.getUid(), equalTo("testFaqPage2"));
	}

	@Test
	public void testFindPageByBestLabelMatch_IgnoreTrailingSlash_NextLevel() throws CMSItemNotFoundException
	{
		final ContentPageModel result = cmsContentPageService.getPageForLabelOrIdAndMatchType("/faq/default/create/", false);

		assertThat(result.getUid(), equalTo("testFaqPage3"));
	}

	@Test
	public void testFindPageByBestLabelMatch_MostMatchToParent_NextLevel() throws CMSItemNotFoundException
	{
		final ContentPageModel result = cmsContentPageService.getPageForLabelOrIdAndMatchType("/faq/default/create/edit", false);

		assertThat(result.getUid(), equalTo("testFaqPage3"));
	}

	@Test
	public void testFindPageByBestLabelMatch_NotStartingWithSlash() throws CMSItemNotFoundException
	{
		final ContentPageModel result = cmsContentPageService.getPageForLabelOrIdAndMatchType("faq/default/create", false);

		assertThat(result.getUid(), equalTo("testFaqPage3"));
	}

	@Test
	public void testFindPageByExactLabelMatch_StartingWithSlash() throws CMSItemNotFoundException
	{
		final ContentPageModel result = cmsContentPageService.getPageForLabelOrIdAndMatchType("/faq", false);

		assertThat(result.getUid(), equalTo("testFaqPage6"));
	}
}
