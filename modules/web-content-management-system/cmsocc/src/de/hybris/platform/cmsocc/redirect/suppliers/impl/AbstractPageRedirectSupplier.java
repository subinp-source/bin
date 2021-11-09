/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsocc.redirect.suppliers.impl;

import de.hybris.platform.cms2.model.preview.PreviewDataModel;
import de.hybris.platform.cmsocc.constants.CmsoccConstants;
import de.hybris.platform.cmsocc.data.RequestParamData;
import de.hybris.platform.cmsocc.redirect.suppliers.PageRedirectSupplier;
import de.hybris.platform.core.model.ItemModel;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;


/**
 * Abstract implementation of {@link PageRedirectSupplier} providing common functionalities for other supplier
 * implementations.
 */
public abstract class AbstractPageRedirectSupplier implements PageRedirectSupplier
{
	private Predicate<ItemModel> pagePredicate;
	private Predicate<String> typeCodePredicate;

	/**
	 * Populates the request parameters to make the request to redirect.
	 *
	 * @param previewData
	 *           - the preview data
	 * @param paramData
	 *           - the request path and query parameters
	 */
	protected abstract void populateParams(PreviewDataModel previewData, RequestParamData paramData);

	/**
	 * Retrieves the category or product code from preview data.
	 *
	 * @param previewData
	 *           - the preview data
	 * @return the category code; can be {@code NULL}
	 */
	protected abstract String getPreviewCode(final PreviewDataModel previewData);

	@Override
	public Predicate<ItemModel> getConstrainedBy()
	{
		return getPagePredicate();
	}

	@Override
	public boolean shouldRedirect(final HttpServletRequest request, final PreviewDataModel previewData)
	{
		final String pageType = request.getParameter(CmsoccConstants.PAGE_TYPE);
		final String code = request.getParameter(CmsoccConstants.CODE);

		if (getTypeCodePredicate().negate().test(pageType))
		{
			return true;
		}
		else if (Objects.isNull(code))
		{
			return Objects.isNull(pageType);
		}
		else
		{
			return !code.equals(getPreviewCode(previewData));
		}
	}

	/**
	 * Creates a {@link RequestParamData} object to store query and path parameters information which are used to build
	 * the redirect URL.
	 *
	 * @param request
	 *           - the http request
	 * @param previewData
	 *           - the preview data
	 * @return a {@code RequestParamData} object
	 */
	protected RequestParamData buildRequestParamData(final HttpServletRequest request, final PreviewDataModel previewData)
	{
		final MultiValueMap<String, String> queryParams = request.getParameterMap().entrySet().stream() //
				.collect(Collectors.toMap( //
						Map.Entry::getKey, //
						entry -> Arrays.asList(entry.getValue()), //
						(v1, v2) -> v2, //
						LinkedMultiValueMap::new));

		final RequestParamData paramData = new RequestParamData();
		paramData.setQueryParameters(queryParams);
		paramData.setPathParameters(new HashMap<>());
		populateParams(previewData, paramData);

		return paramData;
	}
	
	@Override
	public String getRedirectUrl(final HttpServletRequest request, final PreviewDataModel previewData)
	{
		final RequestParamData paramData = buildRequestParamData(request, previewData);
		final UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());

		if (!paramData.getPathParameters().isEmpty() && request.getRequestURL().toString().endsWith("/pages"))
		{
			uriBuilder.pathSegment(paramData.getPathParameters().get(CmsoccConstants.PAGE_ID));
		}

		return uriBuilder //
				.queryParams((MultiValueMap<String, String>) paramData.getQueryParameters()) //
				.build() //
				.toString();
	}

	protected Predicate<ItemModel> getPagePredicate()
	{
		return pagePredicate;
	}

	@Required
	public void setPagePredicate(final Predicate<ItemModel> pagePredicate)
	{
		this.pagePredicate = pagePredicate;
	}

	protected Predicate<String> getTypeCodePredicate()
	{
		return typeCodePredicate;
	}

	@Required
	public void setTypeCodePredicate(final Predicate<String> typeCodePredicate)
	{
		this.typeCodePredicate = typeCodePredicate;
	}

}
