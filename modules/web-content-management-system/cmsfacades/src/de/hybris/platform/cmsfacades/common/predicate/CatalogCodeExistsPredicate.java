/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.common.predicate;

import de.hybris.platform.catalog.CatalogService;
import org.springframework.beans.factory.annotation.Required;

import java.util.function.Predicate;


/**
 * Predicate to check existence of catalog code.
 * <p>
 * Returns <tt>TRUE</tt> if the given catalog code exists; <tt>FALSE</tt> otherwise.
 * </p>
 */
public class CatalogCodeExistsPredicate implements Predicate<String>
{
	private CatalogService catalogService;

	@Override
	@SuppressWarnings("squid:S1166")
	public boolean test(String catalogCode)
	{
		try
		{
			getCatalogService().getCatalogForId(catalogCode);
			return true;
		}
		catch (RuntimeException e)
		{
			return false;
		}
	}

	protected CatalogService getCatalogService()
	{
		return catalogService;
	}

	@Required
	public void setCatalogService(CatalogService catalogService)
	{
		this.catalogService = catalogService;
	}
}
