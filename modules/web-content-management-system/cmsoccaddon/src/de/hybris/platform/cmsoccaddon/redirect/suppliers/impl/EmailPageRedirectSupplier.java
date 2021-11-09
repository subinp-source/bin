/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsoccaddon.redirect.suppliers.impl;

import de.hybris.platform.cms2.model.preview.PreviewDataModel;
import de.hybris.platform.cmsoccaddon.constants.CmsoccaddonConstants;
import de.hybris.platform.cmsoccaddon.data.RequestParamData;
import de.hybris.platform.cmsoccaddon.redirect.suppliers.PageRedirectSupplier;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.MultiValueMap;


/**
 * Implementation of {@link PageRedirectSupplier} to handle {@code EmailPageModel}
 * <p>
 * Note that the email page type is defined in acceleratorservices and having a reference to the model in this extension
 * creates a circular dependency. <br>
 * Additionally, email pages are not supported in Spartacus/headless CMS flows. Therefore, this class is no
 * longer needed.
 *
 * @deprecated since 2005, no longer needed.
 */
@Deprecated(since = "2005", forRemoval = true)
public class EmailPageRedirectSupplier extends AbstractPageRedirectSupplier
{
	@Override
	public boolean shouldRedirect(final HttpServletRequest request, final PreviewDataModel previewData)
	{
		final String pageType = request.getParameter(CmsoccaddonConstants.PAGE_TYPE);
		return getTypeCodePredicate().negate().test(pageType);
	}

	@Override
	protected void populateParams(final PreviewDataModel previewData, final RequestParamData paramData)
	{
		final MultiValueMap<String, String> queryParams = (MultiValueMap<String, String>) paramData.getQueryParameters();
		if (Objects.nonNull(queryParams))
		{
			queryParams.set(CmsoccaddonConstants.PAGE_TYPE, "EmailPage");
			if (Objects.nonNull(previewData.getPage()))
			{
				paramData.getPathParameters().put(CmsoccaddonConstants.PAGE_ID, previewData.getPage().getUid());
			}
		}
	}

	@Override
	protected String getPreviewCode(final PreviewDataModel previewData)
	{
		throw new UnsupportedOperationException("Preview code is not supported for pages of type EmailPage");
	}
}
