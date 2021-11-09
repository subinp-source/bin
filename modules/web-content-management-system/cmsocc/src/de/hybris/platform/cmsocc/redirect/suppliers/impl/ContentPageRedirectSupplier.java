/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsocc.redirect.suppliers.impl;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.model.preview.PreviewDataModel;
import de.hybris.platform.cmsocc.constants.CmsoccConstants;
import de.hybris.platform.cmsocc.data.RequestParamData;
import de.hybris.platform.cmsocc.redirect.suppliers.PageRedirectSupplier;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;


/**
 * Implementation of {@link PageRedirectSupplier} to handle {@link de.hybris.platform.cms2.model.pages.ContentPageModel}
 */
public class ContentPageRedirectSupplier extends AbstractPageRedirectSupplier
{
	@Override
	public boolean shouldRedirect(final HttpServletRequest request, final PreviewDataModel previewData)
	{
		final String pageType = request.getParameter(CmsoccConstants.PAGE_TYPE);
		final String pageLabelOrId = request.getParameter(CmsoccConstants.PAGE_LABEL_ID);

		//    REDIRECT (type IS NULL) OR (type=ContentPage && pageLabelOrId IS NULL)
		// NO REDIRECT (type=NonContentPage) OR (type=ContentPage && pageLabelOrId NON NULL)
		final boolean redirect = isNull(pageType) || getTypeCodePredicate().test(pageType) && isNull(pageLabelOrId);
		final boolean noRedirect = nonNull(pageType) && getTypeCodePredicate().negate().test(pageType) || getTypeCodePredicate().test(pageType) && nonNull(pageLabelOrId);

		return redirect && !noRedirect;
	}

	@Override
	public void populateParams(final PreviewDataModel previewData, final RequestParamData paramData)
	{
		final ContentPageModel page = (ContentPageModel) previewData.getPage();
		if (nonNull(page))
		{
			final MultiValueMap<String, String> queryParameters = (MultiValueMap<String, String>) paramData.getQueryParameters();
			queryParameters.set(CmsoccConstants.PAGE_LABEL_ID, page.getUid());
			queryParameters.set(CmsoccConstants.PAGE_TYPE, ContentPageModel._TYPECODE);
		}
	}

	@Override
	public String getRedirectUrl(final HttpServletRequest request, final PreviewDataModel previewData)
	{
		final RequestParamData paramData = buildRequestParamData(request, previewData);
		return UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString()) //
				.queryParams((MultiValueMap<String, String>) paramData.getQueryParameters()) //
				.build() //
				.toString();
	}

	@Override
	protected String getPreviewCode(final PreviewDataModel previewData)
	{
		throw new UnsupportedOperationException("Preview code is not supported for pages of type " + ContentPageModel._TYPECODE);
	}

}
