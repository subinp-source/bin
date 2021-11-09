/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.rendering.suppliers.page.impl;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.data.RestrictionData;
import de.hybris.platform.cms2.servicelayer.services.CMSContentPageService;
import de.hybris.platform.cms2.servicelayer.services.CMSPreviewService;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.cmsfacades.rendering.suppliers.page.RenderingPageModelSupplier;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Implementation of {@link RenderingPageModelSupplier} for Content page.
 */
public class RenderingContentPageModelSupplier implements RenderingPageModelSupplier
{
	private Predicate<String> constrainedBy;
	private CMSContentPageService cmsContentPageService;
	private CMSSiteService cmsSiteService;
	private CMSPreviewService cmsPreviewService;

	@Override
	public Predicate<String> getConstrainedBy()
	{
		return constrainedBy;
	}

	@Override
	public Optional<AbstractPageModel> getPageModel(final String pageLabelOrId)
	{
		return getContentPageModel(pageLabelOrId);
	}

	@Override
	public Optional<RestrictionData> getRestrictionData(final String qualifier)
	{
		return Optional.empty();
	}

	/**
	 * Returns {@link Optional} {@link AbstractPageModel} based on pageLabelOrId. Extracts the page in the following
	 * order: - by label or id. - current catalog home page. - by default label or id
	 *
	 * @param pageLabelOrId
	 *           the page label or id
	 * @return {@link Optional} {@link AbstractPageModel} page model
	 */
	protected Optional<AbstractPageModel> getContentPageModel(final String pageLabelOrId)
	{
		final Supplier<Optional<ContentPageModel>> page = getPageForLabelOrId(pageLabelOrId);
		final Supplier<Optional<ContentPageModel>> homePage = getCurrentCatalogHomePage();
		final Supplier<Optional<ContentPageModel>> startPage = getPageForLabelOrId(getStartPageLabelOrId());

		final ContentPageModel resultPage = page.get().orElse(homePage.get().orElse(startPage.get().orElse(null)));

		return Optional.ofNullable(resultPage);
	}

	/**
	 * Returns the {@link Optional} page by label or id
	 *
	 * @param pageLabelOrId
	 * @return the {@link Optional} {@link AbstractPageModel}
	 */
	protected Supplier<Optional<ContentPageModel>> getPageForLabelOrId(final String pageLabelOrId)
	{
		return () -> {
			if (!StringUtils.isBlank(pageLabelOrId))
			{
				try
				{
					return Optional.ofNullable(getCmsContentPageService().getPageForLabelOrIdAndMatchType(pageLabelOrId,
							getCmsPreviewService().getPagePreviewCriteria(), false));
				}
				catch (final CMSItemNotFoundException e)
				{
					return Optional.empty();
				}
			}
			else
			{
				return Optional.empty();
			}
		};
	}

	/**
	 * Returns default page label or id
	 *
	 * @return the page label or id
	 */
	protected String getStartPageLabelOrId()
	{
		final Optional<CMSSiteModel> currentSite = getCurrentSite();
		return currentSite.map(site -> getCmsSiteService().getStartPageLabelOrId(site)).orElse(null);
	}

	/**
	 * Returns a home page for the current catalog.
	 *
	 * @return {@link Optional} {@link AbstractPageModel}
	 */
	protected Supplier<Optional<ContentPageModel>> getCurrentCatalogHomePage()
	{
		return () -> Optional.ofNullable(getCmsContentPageService().getHomepage(getCmsPreviewService().getPagePreviewCriteria()));
	}

	/**
	 * Returns current site
	 *
	 * @return {@link Optional} {@link CMSSiteModel}
	 */
	protected Optional<CMSSiteModel> getCurrentSite()
	{
		return Optional.ofNullable(getCmsSiteService().getCurrentSite());
	}

	@Required
	public void setConstrainedBy(final Predicate<String> constrainedBy)
	{
		this.constrainedBy = constrainedBy;
	}

	protected CMSContentPageService getCmsContentPageService()
	{
		return cmsContentPageService;
	}

	@Required
	public void setCmsContentPageService(final CMSContentPageService cmsContentPageService)
	{
		this.cmsContentPageService = cmsContentPageService;
	}

	protected CMSSiteService getCmsSiteService()
	{
		return cmsSiteService;
	}

	@Required
	public void setCmsSiteService(final CMSSiteService cmsSiteService)
	{
		this.cmsSiteService = cmsSiteService;
	}

	protected CMSPreviewService getCmsPreviewService()
	{
		return cmsPreviewService;
	}

	@Required
	public void setCmsPreviewService(final CMSPreviewService cmsPreviewService)
	{
		this.cmsPreviewService = cmsPreviewService;
	}
}
