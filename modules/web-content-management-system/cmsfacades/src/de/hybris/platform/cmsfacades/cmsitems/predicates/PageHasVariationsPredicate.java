/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.cmsitems.predicates;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cmsfacades.pages.PageFacade;
import de.hybris.platform.cmsfacades.pages.service.PageVariationResolver;
import de.hybris.platform.cmsfacades.pages.service.PageVariationResolverType;
import de.hybris.platform.cmsfacades.pages.service.PageVariationResolverTypeRegistry;

import java.util.Optional;
import java.util.function.Predicate;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Predicate to identify if the page has variations associated with it.
 *
 * <p>
 * Returns <tt>TRUE</tt> if the page has variations; <tt>FALSE</tt> otherwise.
 * </p>
 */
public class PageHasVariationsPredicate implements Predicate<AbstractPageModel>
{
	private PageVariationResolverTypeRegistry pageVariationResolverTypeRegistry;

	@Override
	public boolean test(final AbstractPageModel page)
	{
		return CollectionUtils.isNotEmpty(getPageVariationResolver(page.getItemtype()).findVariationPages(page));
	}

	protected PageVariationResolver<AbstractPageModel> getPageVariationResolver(final String typeCode)
	{
		final Optional<PageVariationResolverType> optional = getPageVariationResolverTypeRegistry().getPageVariationResolverType(typeCode);
		return optional.isPresent() ? optional.get().getResolver() : null;
	}

	protected PageVariationResolverTypeRegistry getPageVariationResolverTypeRegistry()
	{
		return pageVariationResolverTypeRegistry;
	}

	@Required
	public void setPageVariationResolverTypeRegistry(final PageVariationResolverTypeRegistry pageVariationResolverTypeRegistry)
	{
		this.pageVariationResolverTypeRegistry = pageVariationResolverTypeRegistry;
	}
}
