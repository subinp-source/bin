/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorcms.component.cache;

import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.regioncache.key.CacheKey;

import javax.servlet.http.HttpServletRequest;


public interface CmsCacheKeyProvider<C extends AbstractCMSComponentModel>
{
	CacheKey getKey(HttpServletRequest request, C component);
}
