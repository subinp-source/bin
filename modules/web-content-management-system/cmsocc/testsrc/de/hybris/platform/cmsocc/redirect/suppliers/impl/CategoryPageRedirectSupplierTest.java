/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsocc.redirect.suppliers.impl;

import static de.hybris.platform.cmsocc.constants.CmsoccConstants.CODE;
import static de.hybris.platform.cmsocc.constants.CmsoccConstants.PAGE_ID;
import static de.hybris.platform.cmsocc.constants.CmsoccConstants.PAGE_LABEL_ID;
import static de.hybris.platform.cmsocc.constants.CmsoccConstants.PAGE_TYPE;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.CategoryPageModel;
import de.hybris.platform.cms2.model.preview.PreviewDataModel;
import de.hybris.platform.cmsocc.data.RequestParamData;

import java.util.Collections;
import java.util.HashMap;
import java.util.function.Predicate;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class CategoryPageRedirectSupplierTest
{
	private static final String CATEGORY_PAGE_ID = "mockCategoryPage";
	private static final String CATEGORY_CODE = "1234";
	private static final String SOME_CATEGORY_CODE = "9999";

	@InjectMocks
	private CategoryPageRedirectSupplier supplier;

	@Mock
	private Predicate<String> typeCodePredicate;
	@Mock
	private Predicate<String> typeCodeNegatedPredicate;
	@Mock
	private HttpServletRequest request;
	@Mock
	private PreviewDataModel previewData;
	@Mock
	private CategoryPageModel categoryPage;
	@Mock
	private CategoryModel category;

	@Before
	public void setup()
	{
		when(previewData.getPage()).thenReturn(categoryPage);
		when(previewData.getPreviewCategory()).thenReturn(category);

		when(categoryPage.getUid()).thenReturn(CATEGORY_PAGE_ID);

		when(request.getParameter(PAGE_TYPE)).thenReturn(null);
		when(request.getParameter(CODE)).thenReturn(null);

		when(typeCodePredicate.test(CategoryPageModel._TYPECODE)).thenReturn(true);
		when(typeCodePredicate.negate()).thenReturn(typeCodeNegatedPredicate);
		when(typeCodeNegatedPredicate.test(CategoryPageModel._TYPECODE)).thenReturn(false);

		when(category.getCode()).thenReturn(CATEGORY_CODE);
	}


	@Test
	public void shouldRedirectWhenPageTypeAndCodeAreNull()
	{
		// WHEN
		final boolean value = supplier.shouldRedirect(request, previewData);

		// THEN
		assertTrue(value);
	}

	@Test
	public void shouldNotRedirectForCategoryPageAndCodeEqualsPreviewCategoryCode()
	{
		// GIVEN
		when(request.getParameter(PAGE_TYPE)).thenReturn(CategoryPageModel._TYPECODE);
		when(request.getParameter(CODE)).thenReturn(CATEGORY_CODE);

		// WHEN
		final boolean value = supplier.shouldRedirect(request, previewData);

		// THEN
		assertFalse(value);
	}

	@Test
	public void shouldRedirectForNonCategoryPage()
	{
		// GIVEN
		when(request.getParameter(PAGE_TYPE)).thenReturn(AbstractPageModel._TYPECODE);
		when(typeCodePredicate.test(AbstractPageModel._TYPECODE)).thenReturn(false);
		when(typeCodePredicate.negate()).thenReturn(typeCodeNegatedPredicate);
		when(typeCodeNegatedPredicate.test(AbstractPageModel._TYPECODE)).thenReturn(true);

		// WHEN
		final boolean value = supplier.shouldRedirect(request, previewData);

		// THEN
		assertTrue(value);
	}

	@Test
	public void shouldRedirectForCategoryPageAndCodeNotEqualsPreviewCategoryCode()
	{
		// GIVEN
		when(request.getParameter(PAGE_TYPE)).thenReturn(CategoryPageModel._TYPECODE);
		when(request.getParameter(CODE)).thenReturn(SOME_CATEGORY_CODE);

		// WHEN
		final boolean value = supplier.shouldRedirect(request, previewData);

		// THEN
		assertTrue(value);
	}

	@Test
	public void shouldPopulateAllParams()
	{
		final RequestParamData paramData = createParamData();
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.put(PAGE_LABEL_ID, Collections.singletonList(CATEGORY_PAGE_ID));
		paramData.setQueryParameters(params);

		// WHEN
		supplier.populateParams(previewData, paramData);

		// THEN
		assertNull(paramData.getQueryParameters().get(PAGE_LABEL_ID));
		assertThat(paramData.getQueryParameters().get(CODE), hasItem(CATEGORY_CODE));
		assertThat(paramData.getQueryParameters().get(PAGE_TYPE), hasItem(CategoryPageModel._TYPECODE));
		assertThat(paramData.getPathParameters().entrySet(), is(empty()));
	}

	@Test
	public void shouldPopulatePathParamsWhenCategoryCodeIsNull()
	{
		final RequestParamData paramData = createParamData();
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.put(PAGE_LABEL_ID, Collections.singletonList(CATEGORY_PAGE_ID));
		paramData.setQueryParameters(params);

		when(previewData.getPreviewCategory()).thenReturn(null);
		when(previewData.getPage()).thenReturn(categoryPage);

		// WHEN
		supplier.populateParams(previewData, paramData);

		// THEN
		assertNull(paramData.getQueryParameters().get(PAGE_LABEL_ID));
		assertNull(paramData.getQueryParameters().get(CODE));
		assertThat(paramData.getQueryParameters().get(PAGE_TYPE), hasItem(CategoryPageModel._TYPECODE));
		assertThat(paramData.getPathParameters().get(PAGE_ID), equalTo(CATEGORY_PAGE_ID));
	}

	public void shouldNotPopulatePathParamsWhenCategoryCodeIsNullAndPreviewPageIdIsNull()
	{
		final RequestParamData paramData = createParamData();
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.put(PAGE_LABEL_ID, Collections.singletonList(CATEGORY_PAGE_ID));
		paramData.setQueryParameters(params);

		when(previewData.getPreviewCategory()).thenReturn(null);
		when(previewData.getPage()).thenReturn(null);

		// WHEN
		supplier.populateParams(previewData, paramData);

		// THEN
		assertNull(paramData.getQueryParameters().get(PAGE_LABEL_ID));
		assertNull(paramData.getQueryParameters().get(CODE));
		assertThat(paramData.getQueryParameters().get(PAGE_TYPE), hasItem(CategoryPageModel._TYPECODE));
		assertThat(paramData.getPathParameters().entrySet(), is(empty()));
	}

	@Test
	public void shouldRedirectUrlWithQueryParams()
	{
		when(request.getRequestURL()).thenReturn(new StringBuffer("https://mock-domain.com/find/pages"));

		final String redirectUrl = supplier.getRedirectUrl(request, previewData);

		assertTrue(redirectUrl.contains(PAGE_TYPE + "=" + CategoryPageModel._TYPECODE));
		assertTrue(redirectUrl.contains(CODE + "=" + CATEGORY_CODE));
	}

	@Test
	public void shouldRedirectUrlWithPathParam()
	{
		when(request.getRequestURL()).thenReturn(new StringBuffer("https://mock-domain.com/find/pages"));
		when(previewData.getPreviewCategory()).thenReturn(null);
		when(previewData.getPage()).thenReturn(categoryPage);

		final String redirectUrl = supplier.getRedirectUrl(request, previewData);

		assertTrue(redirectUrl.contains("/" + CATEGORY_PAGE_ID));
		assertTrue(redirectUrl.contains(PAGE_TYPE + "=" + CategoryPageModel._TYPECODE));
	}

	@Test
	public void shouldRedirectUrlWithoutPathParamForNonPagesEndpoint()
	{
		when(request.getRequestURL()).thenReturn(new StringBuffer("https://mock-domain.com/find/mypages"));
		when(previewData.getPreviewCategory()).thenReturn(null);
		when(previewData.getPage()).thenReturn(categoryPage);

		final String redirectUrl = supplier.getRedirectUrl(request, previewData);

		assertFalse(redirectUrl.contains("/" + CATEGORY_PAGE_ID));
		assertTrue(redirectUrl.contains(PAGE_TYPE + "=" + CategoryPageModel._TYPECODE));
	}

	protected RequestParamData createParamData()
	{
		final RequestParamData paramData = new RequestParamData();
		paramData.setPathParameters(new HashMap<>());
		paramData.setQueryParameters(new LinkedMultiValueMap<>());
		return paramData;
	}
}
