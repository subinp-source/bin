/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.notificationfacades.url;

import de.hybris.platform.commerceservices.url.UrlResolver;
import de.hybris.platform.core.model.ItemModel;

import org.apache.commons.lang3.StringUtils;


/**
 * Abstract site message URL resolver to provide default URL.
 */
public abstract class SiteMessageUrlResolver<T extends ItemModel> implements UrlResolver<T>
{

	private String defaultUrl = StringUtils.EMPTY;

	protected String getDefaultUrl()
	{
		return defaultUrl;
	}

	public void setDefaultUrl(final String defaultUrl)
	{
		this.defaultUrl = defaultUrl;
	}

}
