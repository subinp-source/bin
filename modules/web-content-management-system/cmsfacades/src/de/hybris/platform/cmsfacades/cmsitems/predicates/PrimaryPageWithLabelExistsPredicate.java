/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.cmsitems.predicates;

import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.servicelayer.services.CMSContentPageService;
import de.hybris.platform.cmsfacades.cmsitems.OriginalClonedItemProvider;
import de.hybris.platform.cmsfacades.pages.service.PageVariationResolver;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Required;


/**
 * Predicate to test if a given page label maps to an existing primary page.
 * <p>
 * Returns <tt>TRUE</tt> if the page exists; <tt>FALSE</tt> otherwise.
 * </p>
 */
public class PrimaryPageWithLabelExistsPredicate implements Predicate<String>
{
	private PageVariationResolver<ContentPageModel> pageVariationResolver;
	private OriginalClonedItemProvider originalClonedItemProvider;
	private CMSContentPageService contentPageService;

	@Override
	public boolean test(final String label)
	{
		final ContentPageModel originalItemModel = (ContentPageModel) getOriginalClonedItemProvider().getCurrentItem();

		final List<String> labels = getContentPageService().findLabelVariations(label, true);

		return getPageVariationResolver().findPagesByType(ContentPageModel._TYPECODE, Boolean.TRUE).stream() //
				.filter(defaultPage -> labels.contains(defaultPage.getLabel())) //
				.filter(defaultPage -> Objects.nonNull(defaultPage.getDefaultPage()) && defaultPage.getDefaultPage()) //
				.anyMatch(defaultPage -> originalItemModel == null || !defaultPage.getUid().equals(originalItemModel.getUid()));
	}

	protected PageVariationResolver<ContentPageModel> getPageVariationResolver()
	{
		return pageVariationResolver;
	}

	@Required
	public void setPageVariationResolver(final PageVariationResolver<ContentPageModel> pageVariationResolver)
	{
		this.pageVariationResolver = pageVariationResolver;
	}

	protected OriginalClonedItemProvider getOriginalClonedItemProvider()
	{
		return originalClonedItemProvider;
	}

	@Required
	public void setOriginalClonedItemProvider(final OriginalClonedItemProvider originalClonedItemProvider)
	{
		this.originalClonedItemProvider = originalClonedItemProvider;
	}

	protected CMSContentPageService getContentPageService()
	{
		return contentPageService;
	}

	@Required
	public void setContentPageService(final CMSContentPageService contentPageService)
	{
		this.contentPageService = contentPageService;
	}
}
