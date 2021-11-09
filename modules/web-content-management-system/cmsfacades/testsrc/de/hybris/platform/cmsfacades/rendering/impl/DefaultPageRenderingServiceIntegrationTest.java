/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.rendering.impl;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cmsfacades.data.AbstractPageData;
import de.hybris.platform.cmsfacades.rendering.PageRenderingService;
import de.hybris.platform.core.servicelayer.data.PaginationData;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;

import java.util.Arrays;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class DefaultPageRenderingServiceIntegrationTest extends ServicelayerTransactionalTest
{
	@Resource
	private PageRenderingService cmsPageRenderingService;
	@Resource
	private CatalogVersionService catalogVersionService;

	private CatalogVersionModel globalStagedCatalog;

	@Before
	public void setUp() throws ImpExException
	{
		importCsv("/test/cmsMultiCountryTestData.csv", "UTF-8");

		globalStagedCatalog = catalogVersionService.getCatalogVersion("MultiCountryTestContentCatalog", "StagedVersion");
	}

	@Test
	public void shouldFindAllPagesForRendering()
	{
		final SearchPageData pageableData = createPaginationData();
		catalogVersionService.setSessionCatalogVersions(Arrays.asList(globalStagedCatalog));

		final SearchPageData<AbstractPageData> result = cmsPageRenderingService
				.findAllRenderingPageData(AbstractPageModel._TYPECODE, pageableData);

		assertThat(result.getResults(), hasItems( //
				allOf(hasProperty("uid", equalTo("TestCategoryPage")), //
						hasProperty("typeCode", equalTo("CategoryPage"))), //
				allOf(hasProperty("uid", equalTo("menAccessoriesCategoryPage")), //
						hasProperty("typeCode", equalTo("CategoryPage")), //
						hasProperty("otherProperties", hasEntry("categoryCodes", Arrays.asList("accessories")))), //
				allOf(hasProperty("uid", equalTo("winterHatProductPage")), //
						hasProperty("typeCode", equalTo("ProductPage")), //
						hasProperty("otherProperties", hasEntry("productCodes", Arrays.asList("hat", "parka")))) //
		));
	}

	protected SearchPageData createPaginationData()
	{
		final SearchPageData pageableData = new SearchPageData();
		final PaginationData paginationData = new PaginationData();
		paginationData.setCurrentPage(0);
		paginationData.setPageSize(10);
		paginationData.setNeedsTotal(true);
		pageableData.setPagination(paginationData);
		return pageableData;
	}

}
