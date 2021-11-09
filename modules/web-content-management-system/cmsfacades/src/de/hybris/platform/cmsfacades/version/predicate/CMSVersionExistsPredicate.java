/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.version.predicate;

import de.hybris.platform.cms2.version.service.CMSVersionService;

import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Required;


/**
 * Predicate to test if a cms version exists.
 * <p>
 * Returns <tt>TRUE</tt> if the cms version exists; <tt>FALSE</tt> otherwise.
 * </p>
 */
public class CMSVersionExistsPredicate implements Predicate<String>
{
	private CMSVersionService cmsVersionService;

	@Override
	public boolean test(final String uid)
	{
		return getCmsVersionService().getVersionByUid(uid).isPresent();
	}

	protected CMSVersionService getCmsVersionService()
	{
		return cmsVersionService;
	}

	@Required
	public void setCmsVersionService(final CMSVersionService cmsVersionService)
	{
		this.cmsVersionService = cmsVersionService;
	}
}
