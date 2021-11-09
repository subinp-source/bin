/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorcms.component.cache.impl;

import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;


public class CurrentCategoryCmsCacheKeyProvider extends DefaultCmsCacheKeyProvider
{
	@Override
	public StringBuilder getKeyInternal(final HttpServletRequest request, final SimpleCMSComponentModel component)
	{
		final StringBuilder buffer = new StringBuilder(super.getKeyInternal(request, component));
		final String currentCategory = getRequestContextData(request).getCategory().getPk().getLongValueAsString();
		if (!StringUtils.isEmpty(currentCategory))
		{
			buffer.append(currentCategory);
		}
		return buffer;
	}
}
