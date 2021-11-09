/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.common.predicate;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.servicelayer.services.CMSContentPageService;

import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Required;


/**
 * Predicate to check existence of label or id for a ContentPage.
 * <p>
 * Returns <tt>TRUE</tt> if the given label or id exists; <tt>FALSE</tt> otherwise.
 * </p>
 */
public class ContentPageLabelOrIdExistsPredicate implements Predicate<String>
{
	private CMSContentPageService cmsContentPageService;

	@Override
	public boolean test(final String pageLabelOrId)
	{
		try
		{
			getCmsContentPageService().getPageForLabelOrIdAndMatchType(pageLabelOrId, false);
			return true;
		}
		catch (final CMSItemNotFoundException e)
		{
			return false;
		}
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
}
