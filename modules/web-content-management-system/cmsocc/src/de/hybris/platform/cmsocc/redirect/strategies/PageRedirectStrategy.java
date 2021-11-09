/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsocc.redirect.strategies;

import de.hybris.platform.cms2.model.preview.PreviewDataModel;

import javax.servlet.http.HttpServletRequest;

/**
 * Interface for strategy to determine whether to redirect the given request and also responsible for providing redirect url.
 */
public interface PageRedirectStrategy
{
	/**
	 * Verifies whether a request needs to be redirected or not.
	 *
	 * @param request
	 *           - the http servlet request
	 * @param previewData
	 *           - the preview data
	 * @return {@code true} when the request needs to be redirected, otherwise {@code false}
	 */
	boolean shouldRedirect(final HttpServletRequest request, final PreviewDataModel previewData);

	/**
	 * Constructs the redirect url given the http servlet request and the preview data.
	 *
	 * @param request
	 *           - the http servlet request
	 * @param previewData
	 *           - the preview data
	 * @return the redirect url
	 */
	String getRedirectUrl(final HttpServletRequest request, final PreviewDataModel previewData);
}
