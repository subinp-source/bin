/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsocc.redirect.suppliers;

import de.hybris.platform.cms2.model.preview.PreviewDataModel;
import de.hybris.platform.core.model.ItemModel;

import java.util.function.Predicate;

import javax.servlet.http.HttpServletRequest;


/**
 * Interface responsible for indicating whether to redirect the request or not and providing the redirect URL if
 * applicable.
 */
public interface PageRedirectSupplier
{
	/**
	 * Predicate to test if a given page model matches the page type supplier.
	 *
	 * @return {@code true} if the supplier exists; {@code false} otherwise.
	 */
	Predicate<ItemModel> getConstrainedBy();

	/**
	 * Determines whether to redirect the request depending upon the logic in supplier.
	 *
	 * @param request
	 *           - the http request
	 * @param previewData
	 *           - the preview data
	 * @return {@code true} if it needs to redirect the request; {@code false} otherwise.
	 */
	boolean shouldRedirect(HttpServletRequest request, PreviewDataModel previewData);

	/**
	 * Constructs the redirect url given the http servlet request and the preview data.
	 *
	 * @param request
	 *           - the http servlet request
	 * @param previewData
	 *           - the preview data
	 * @return the redirect url
	 */
	String getRedirectUrl(HttpServletRequest request, PreviewDataModel previewData);
}
