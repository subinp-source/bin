/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.util;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;

import org.junit.Assert;
import org.junit.Test;


/**
 * The integration test for {@link CommerceSearchUtils}.
 */
@UnitTest
public class CommerceSearchUtilsTest
{
	@Test
	public void shouldGetAllOnOnePagePageableData()
	{
		final PageableData pageableData = CommerceSearchUtils.getAllOnOnePagePageableData();
		Assert.assertNotNull("pageableData", pageableData);
		Assert.assertEquals("CurrentPage", 0, pageableData.getCurrentPage());
		Assert.assertEquals("PageSize", -1, pageableData.getPageSize());
		Assert.assertEquals("Sort", "asc", pageableData.getSort());
	}

	@Test
	public void shouldCreateEmptySearchPageData()
	{
		final SearchPageData searchPageData = CommerceSearchUtils.createEmptySearchPageData();
		Assert.assertNotNull("searchPageData", searchPageData);
		Assert.assertNotNull("Results", searchPageData.getResults());
		Assert.assertEquals("ResultsSize", 0, searchPageData.getResults().size());
		Assert.assertNotNull("Pagination", searchPageData.getPagination());
		Assert.assertEquals("Pagination result size", 0, searchPageData.getPagination().getTotalNumberOfResults());
		Assert.assertNotNull("Sorts", searchPageData.getSorts());
		Assert.assertEquals("Sort Size", 0, searchPageData.getSorts().size());
	}
}
