/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.cmsitems.populators;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.commercefacades.basesite.data.BaseSiteData;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class CMSSitePopulatorTest
{

	public static final String ELECTRONICS_PATTERN = "(?i)^https?://electronics\\.[^/]+(|/.*|\\?.*)$";
	public static final String REST_PATTERN = "(?i)^https?://localhost(:[\\d]+)?/rest/.*$";
	public static final List<String> URL_PATTERNS = Collections.unmodifiableList(Arrays.asList(ELECTRONICS_PATTERN, REST_PATTERN));
	public static final String DEFAULT_PREVIEW_CATALOG_ID = "catalogId";
	public static final String DEFAULT_PREVIEW_CATEGORY_CODE = "categoryCode";
	public static final String DEFAULT_PREVIEW_PRODUCT_CODE = "productCode";

	private final CMSSitePopulator cmsSitePopulator = new CMSSitePopulator();

	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	private CMSSiteModel cmsSiteModel;
	private BaseSiteData baseSiteData;

	@Before
	public void setUp()
	{
		baseSiteData = new BaseSiteData();
		when(cmsSiteModel.getUrlPatterns()).thenReturn(URL_PATTERNS);
		when(cmsSiteModel.getDefaultPreviewCatalog().getId()).thenReturn(DEFAULT_PREVIEW_CATALOG_ID);
		when(cmsSiteModel.getDefaultPreviewCategory().getCode()).thenReturn(DEFAULT_PREVIEW_CATEGORY_CODE);
		when(cmsSiteModel.getDefaultPreviewProduct().getCode()).thenReturn(DEFAULT_PREVIEW_PRODUCT_CODE);
	}

	@Test
	public void populate()
	{
		cmsSitePopulator.populate(cmsSiteModel, baseSiteData);
		final List<String> urlPatterns = baseSiteData.getUrlPatterns();
		assertThat(urlPatterns, is(URL_PATTERNS));
		assertThat(urlPatterns, contains(ELECTRONICS_PATTERN, REST_PATTERN));
		assertEquals(DEFAULT_PREVIEW_CATALOG_ID, baseSiteData.getDefaultPreviewCatalogId());
		assertEquals(DEFAULT_PREVIEW_CATEGORY_CODE, baseSiteData.getDefaultPreviewCategoryCode());
		assertEquals(DEFAULT_PREVIEW_PRODUCT_CODE, baseSiteData.getDefaultPreviewProductCode());
	}
}
