/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.rendering.suppliers.page.impl;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.servicelayer.data.CMSDataFactory;
import de.hybris.platform.cms2.servicelayer.data.RestrictionData;
import de.hybris.platform.cms2.servicelayer.services.CMSPageService;
import de.hybris.platform.cms2.servicelayer.services.CMSPreviewService;
import de.hybris.platform.cmsfacades.rendering.suppliers.page.RenderingPageModelSupplier;
import org.springframework.beans.factory.annotation.Required;

import java.util.Optional;
import java.util.function.Predicate;


/**
 * Implementation of {@link RenderingPageModelSupplier} for Category page.
 */
public class RenderingCategoryPageModelSupplier implements RenderingPageModelSupplier
{
	private Predicate<String> constrainedBy;
	private CMSPageService cmsPageService;
	private CMSDataFactory cmsDataFactory;
	private CMSPreviewService cmsPreviewService;

	@Override
	public Predicate<String> getConstrainedBy()
	{
		return constrainedBy;
	}

	@Override
	public Optional<AbstractPageModel> getPageModel(String categoryCode)
	{
		try
		{
			return Optional.of(getCmsPageService().getPageForCategoryCode(categoryCode, getCmsPreviewService().getPagePreviewCriteria()));
		}
		catch (CMSItemNotFoundException e)
		{
			return Optional.empty();
		}
	}

	@Override
	public Optional<RestrictionData> getRestrictionData(String categoryCode)
	{
		return Optional.ofNullable(getCmsDataFactory().createRestrictionData(categoryCode, null, null));
	}

	@Required
	public void setConstrainedBy(Predicate<String> constrainedBy)
	{
		this.constrainedBy = constrainedBy;
	}

	protected CMSPageService getCmsPageService()
	{
		return cmsPageService;
	}

	@Required
	public void setCmsPageService(CMSPageService cmsPageService)
	{
		this.cmsPageService = cmsPageService;
	}

	protected CMSDataFactory getCmsDataFactory()
	{
		return cmsDataFactory;
	}

	@Required
	public void setCmsDataFactory(CMSDataFactory cmsDataFactory)
	{
		this.cmsDataFactory = cmsDataFactory;
	}

	protected CMSPreviewService getCmsPreviewService()
	{
		return cmsPreviewService;
	}

	@Required
	public void setCmsPreviewService(CMSPreviewService cmsPreviewService)
	{
		this.cmsPreviewService = cmsPreviewService;
	}
}
