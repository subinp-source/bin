/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsocc.redirect.suppliers.impl;

import static de.hybris.platform.cmsocc.constants.CmsoccConstants.CODE;
import static de.hybris.platform.cmsocc.constants.CmsoccConstants.PAGE_LABEL_ID;
import static de.hybris.platform.cmsocc.constants.CmsoccConstants.PAGE_TYPE;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.model.preview.PreviewDataModel;
import de.hybris.platform.cmsocc.data.RequestParamData;

import java.util.Collections;
import java.util.function.Predicate;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.util.LinkedMultiValueMap;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class ContentPageRedirectSupplierTest
{
	private static final String CONTENT_PAGE_ID_VALUE = "mockContentPage";
	private static final String PAGE_TYPE_VALUE = "pageType";

	@InjectMocks
	private ContentPageRedirectSupplier supplier;
	@Mock
	private Predicate<String> typeCodePredicate;
	@Mock
	private Predicate<String> revertTypeCodePredicate;

	@Mock
	private HttpServletRequest request;
	@Mock
	private PreviewDataModel previewData;
	@Mock
	private ContentPageModel contentPage;

	@Before
	public void setup()
	{
		when(previewData.getPage()).thenReturn(contentPage);
		when(contentPage.getUid()).thenReturn(CONTENT_PAGE_ID_VALUE);
		initTypeCodePredicate(true);
	}

	@Test
	public void shouldRedirectWhenPageTypeIsNull()
	{
		// GIVEN
		when(request.getParameter(PAGE_TYPE)).thenReturn(null);
		when(request.getParameter(PAGE_LABEL_ID)).thenReturn(null);

		// WHEN
		final boolean value = supplier.shouldRedirect(request, previewData);

		// THEN
		assertTrue(value);
	}

	@Test
	public void shouldRedirectWhenPageTypeEqualsContentPageAndPageIdFromUrlIsNotProvided()
	{
		// GIVEN
		when(request.getParameter(PAGE_TYPE)).thenReturn(PAGE_TYPE_VALUE);
		initTypeCodePredicate(true);

		// WHEN
		final boolean value = supplier.shouldRedirect(request, previewData);

		// THEN
		assertTrue(value);
	}

	@Test
	public void shouldNotRedirectWhenPageTypeIsNotContentPage()
	{
		// GIVEN
		when(request.getParameter(PAGE_TYPE)).thenReturn(PAGE_TYPE_VALUE);
		initTypeCodePredicate(false);

		// WHEN
		final boolean value = supplier.shouldRedirect(request, previewData);

		// THEN
		assertFalse(value);
	}

	@Test
	public void shouldNotRedirectWhenPageTypeEqualsContentPageAndPageIdInUrlIsProvided()
	{
		// GIVEN
		when(request.getParameter(PAGE_TYPE)).thenReturn(PAGE_TYPE_VALUE);
		when(request.getParameter(PAGE_LABEL_ID)).thenReturn(CONTENT_PAGE_ID_VALUE);
		initTypeCodePredicate(true);

		// WHEN
		final boolean value = supplier.shouldRedirect(request, previewData);

		// THEN
		assertFalse(value);
	}

	@Test
	public void shouldPopulateAllParams()
	{
		final RequestParamData paramData = new RequestParamData();
		paramData.setPathParameters(Collections.emptyMap());
		paramData.setQueryParameters(new LinkedMultiValueMap<>());

		// WHEN
		supplier.populateParams(previewData, paramData);

		// THEN
		assertThat(paramData.getQueryParameters().get(PAGE_LABEL_ID), hasItem(CONTENT_PAGE_ID_VALUE));
		assertThat(paramData.getQueryParameters().get(PAGE_TYPE), hasItem(ContentPageModel._TYPECODE));
		assertThat(paramData.getQueryParameters().get(CODE), nullValue());
		assertThat(paramData.getPathParameters().entrySet(), is(empty()));
	}

	protected void initTypeCodePredicate(final boolean theSame)
	{
		when(typeCodePredicate.test(PAGE_TYPE_VALUE)).thenReturn(theSame);
		when(typeCodePredicate.negate()).thenReturn(revertTypeCodePredicate);
		when(revertTypeCodePredicate.test(PAGE_TYPE_VALUE)).thenReturn(!theSame);
	}
}
