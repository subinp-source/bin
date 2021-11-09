/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.common.predicate;

import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminContentSlotService;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Required;


/**
 * Predicate to test if a given content slot uid maps to an existing content slot.
 * <p>
 * Returns <tt>TRUE</tt> if the content slot exists; <tt>FALSE</tt> otherwise.
 * </p>
 */
public class ContentSlotExistsPredicate implements Predicate<String>
{

	private CMSAdminContentSlotService cmsAdminContentSlotService;

	/**
	 * Suppress sonar warning (squid:S1166 | Exception handlers should preserve the original exception) : The exception
	 * is correctly handled in the catch clause.
	 */
	@SuppressWarnings("squid:S1166")
	@Override
	public boolean test(final String target)
	{
		boolean result = true;
		try
		{
			getCmsAdminContentSlotService().getContentSlotForId(target);
		}
		catch (UnknownIdentifierException | AmbiguousIdentifierException e)
		{
			result = false;
		}
		return result;
	}

	protected CMSAdminContentSlotService getCmsAdminContentSlotService()
	{
		return cmsAdminContentSlotService;
	}

	@Required
	public void setCmsAdminContentSlotService(final CMSAdminContentSlotService cmsAdminContentSlotService)
	{
		this.cmsAdminContentSlotService = cmsAdminContentSlotService;
	}

}
