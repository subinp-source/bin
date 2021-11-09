/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.basesites;

import de.hybris.platform.commercefacades.basesite.data.BaseSiteData;

import java.util.List;


/**
 * Facade for management of base sites - Its main purpose is to retrieve base sites information using existing services.
 */
public interface BaseSiteFacade
{
	/**
	 * Returns list of base site DTOs
	 *
	 * @return all base sites
	 */
	List<BaseSiteData> getAllBaseSites();
}
