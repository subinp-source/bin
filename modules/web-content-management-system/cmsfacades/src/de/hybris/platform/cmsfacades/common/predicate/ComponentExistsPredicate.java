/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.common.predicate;

import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminComponentService;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Required;


/**
 * Predicate to test if a given component uid maps to an existing component.
 * <p>
 * Returns <tt>TRUE</tt> if the component exists; <tt>FALSE</tt> otherwise.
 * </p>
 */
public class ComponentExistsPredicate implements Predicate<String>
{

	private CMSAdminComponentService cmsAdminComponentService;

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
			getCmsAdminComponentService().getCMSComponentForId(target);
		}
		catch (UnknownIdentifierException | AmbiguousIdentifierException e)
		{
			result = false;
		}
		return result;
	}

	protected CMSAdminComponentService getCmsAdminComponentService()
	{
		return cmsAdminComponentService;
	}

	@Required
	public void setCmsAdminComponentService(final CMSAdminComponentService cmsAdminComponentService)
	{
		this.cmsAdminComponentService = cmsAdminComponentService;
	}

}
