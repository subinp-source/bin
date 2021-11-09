/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.rendering.suppliers.page.impl;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.data.PagePreviewCriteriaData;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.CMSContentPageService;
import de.hybris.platform.cms2.servicelayer.services.CMSPreviewService;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class RenderingContentPageModelSupplierTest
{
	private final String LABEL_OR_ID = "LabelOrId";

	@Mock
	private CMSContentPageService cmsContentPageService;
	@Mock
	private CMSSiteService cmsSiteService;
	@Mock
	private ContentPageModel contentPageModel;
	@Mock
	private ContentPageModel homePageModel;
	@Mock
	private PagePreviewCriteriaData pagePreviewCriteriaData;
	@Mock
	private ContentPageModel defaultPageModel;
	@Mock
	private CMSSiteModel cmsSiteModel;
	@Mock
	private CMSPreviewService cmsPreviewService;

	@InjectMocks
	private RenderingContentPageModelSupplier supplier;

	@Before
	public void setUp()
	{
		when(cmsPreviewService.getPagePreviewCriteria()).thenReturn(pagePreviewCriteriaData);
	}

	@Test
	public void shouldReturnPageByLabelOrId() throws CMSItemNotFoundException
	{
		// GIVEN
		when(cmsContentPageService.getPageForLabelOrIdAndMatchType(LABEL_OR_ID, pagePreviewCriteriaData, false))
				.thenReturn(contentPageModel);

		// WHEN
		final Optional<AbstractPageModel> result = supplier.getPageModel(LABEL_OR_ID);

		// THEN
		assertTrue(result.isPresent());
		assertThat(result.get(), is(contentPageModel));
	}

	@Test
	public void shouldReturnHomePageIfLabelOrIdNotFound() throws CMSItemNotFoundException
	{
		// GIVEN
		when(cmsContentPageService.getPageForLabelOrIdAndMatchType(LABEL_OR_ID, pagePreviewCriteriaData, false))
				.thenThrow(new CMSItemNotFoundException("")).thenThrow(new CMSItemNotFoundException(""));
		when(cmsContentPageService.getHomepage(pagePreviewCriteriaData)).thenReturn(homePageModel);

		// WHEN
		final Optional<AbstractPageModel> result = supplier.getPageModel(LABEL_OR_ID);

		// THEN
		assertTrue(result.isPresent());
		assertThat(result.get(), is(homePageModel));
	}

	@Test
	public void shouldReturnPageByDefaultLabelOrIdLabelOrIdNotFoundAndIfHomePageNotFound() throws CMSItemNotFoundException
	{
		// GIVEN
		when(cmsContentPageService.getPageForLabelOrIdAndMatchType(LABEL_OR_ID, pagePreviewCriteriaData, false))
				.thenThrow(new CMSItemNotFoundException("")).thenReturn(defaultPageModel);
		when(cmsContentPageService.getHomepage(pagePreviewCriteriaData)).thenReturn(null);
		when(cmsSiteService.getCurrentSite()).thenReturn(cmsSiteModel);
		when(cmsSiteService.getStartPageLabelOrId(cmsSiteModel)).thenReturn(LABEL_OR_ID);

		// WHEN
		final Optional<AbstractPageModel> result = supplier.getPageModel(LABEL_OR_ID);

		// THEN
		assertTrue(result.isPresent());
		assertThat(result.get(), is(defaultPageModel));
	}

	@Test
	public void shouldReturnEmptyIfPageDoesNotExists() throws CMSItemNotFoundException
	{
		// GIVEN
		when(cmsContentPageService.getPageForLabelOrIdAndMatchType(LABEL_OR_ID, true)).thenThrow(new CMSItemNotFoundException(""));
		when(cmsContentPageService.getHomepage(pagePreviewCriteriaData)).thenReturn(null);

		// WHEN
		final Optional<AbstractPageModel> result = supplier.getPageModel(LABEL_OR_ID);

		// THEN
		assertFalse(result.isPresent());
	}
}
