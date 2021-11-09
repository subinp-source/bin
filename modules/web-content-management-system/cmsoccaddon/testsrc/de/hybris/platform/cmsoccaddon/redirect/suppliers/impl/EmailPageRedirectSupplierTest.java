/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsoccaddon.redirect.suppliers.impl;

import static de.hybris.platform.cmsoccaddon.constants.CmsoccaddonConstants.PAGE_ID;
import static de.hybris.platform.cmsoccaddon.constants.CmsoccaddonConstants.PAGE_TYPE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.preview.PreviewDataModel;
import de.hybris.platform.cmsoccaddon.data.RequestParamData;

import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;

import javax.servlet.http.HttpServletRequest;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.util.LinkedMultiValueMap;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class EmailPageRedirectSupplierTest
{
	public static String EMAIL_PAGE_UID = "EmailPageUid";

	@InjectMocks
	private EmailPageRedirectSupplier supplier;

	@Mock
	private Predicate<String> typeCodePredicate;
	@Mock
	private Predicate<String> typeCodeNegatedPredicate;

	@Mock
	private HttpServletRequest request;
	@Mock
	private PreviewDataModel previewData;
	@Mock
	private AbstractPageModel emailPageModel;

	@Before
	public void setup()
	{
		when(typeCodePredicate.test("EmailPage")).thenReturn(true);
		when(typeCodePredicate.negate()).thenReturn(typeCodeNegatedPredicate);
		when(typeCodeNegatedPredicate.test("EmailPage")).thenReturn(false);
		when(previewData.getPage()).thenReturn(emailPageModel);
		when(emailPageModel.getUid()).thenReturn(EMAIL_PAGE_UID);
	}

	@Test
	public void shouldRedirectWhenPageTypeIsNotProvided()
	{
		// GIVEN
		when(typeCodeNegatedPredicate.test(null)).thenReturn(true);

		// WHEN
		final boolean shouldRedirect = supplier.shouldRedirect(request, previewData);

		// THEN
		assertTrue(shouldRedirect);
	}

	@Test
	public void shouldNotRedirectWhenPageTypeIsProvided()
	{
		// GIVEN
		when(typeCodeNegatedPredicate.test(any())).thenReturn(false);

		// WHEN
		final boolean shouldRedirect = supplier.shouldRedirect(request, previewData);

		// THEN
		assertFalse(shouldRedirect);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void shouldThrowExceptionForPreviewCode()
	{
		// WHEN
		supplier.getPreviewCode(previewData);
	}

	@Test
	public void shouldPopulateAllParametersForRedirectUrl()
	{
		// GIVEN
		final RequestParamData paramData = createParamData();

		// WHEN
		supplier.populateParams(previewData, paramData);

		// THEN
		final List<String> pageTypes = paramData.getQueryParameters().get(PAGE_TYPE);
		assertThat(pageTypes, Matchers.containsInAnyOrder("EmailPage"));
		final String pageId = paramData.getPathParameters().get(PAGE_ID);
		assertEquals(pageId, EMAIL_PAGE_UID);
	}

	protected RequestParamData createParamData()
	{
		final RequestParamData paramData = new RequestParamData();
		paramData.setPathParameters(new HashMap<>());
		paramData.setQueryParameters(new LinkedMultiValueMap<>());
		return paramData;
	}
}
