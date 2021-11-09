/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsocc.redirect.strategies.impl;

import de.hybris.platform.cms2.model.preview.PreviewDataModel;
import de.hybris.platform.cmsocc.redirect.strategies.PageRedirectStrategy;
import de.hybris.platform.cmsocc.redirect.suppliers.PageRedirectSupplier;

import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link PageRedirectStrategy}.
 */
public class DefaultPageRedirectStrategy implements PageRedirectStrategy
{
	private List<PageRedirectSupplier> pageRedirectSuppliers;

	@Override
	public boolean shouldRedirect(final HttpServletRequest request, final PreviewDataModel previewData)
	{
		if (Objects.nonNull(previewData.getPage()))
		{
			return getPageRedirectSuppliers().stream() //
					.filter(supplier -> supplier.getConstrainedBy().test(previewData.getPage())) //
					.findFirst().map(supplier -> supplier.shouldRedirect(request, previewData)) //
					.orElse(false);
		}
		return false;
	}

	@Override
	public String getRedirectUrl(final HttpServletRequest request, final PreviewDataModel previewData)
	{
		final PageRedirectSupplier foundSupplier = getPageRedirectSuppliers().stream() //
				.filter(supplier -> supplier.getConstrainedBy().test(previewData.getPage())) //
				.findFirst().orElseThrow(); //
		return foundSupplier.getRedirectUrl(request, previewData);

	}

	protected List<PageRedirectSupplier> getPageRedirectSuppliers()
	{
		return pageRedirectSuppliers;
	}

	@Required
	public void setPageRedirectSuppliers(final List<PageRedirectSupplier> pageRedirectSuppliers)
	{
		this.pageRedirectSuppliers = pageRedirectSuppliers;
	}
}
