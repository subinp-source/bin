/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.pages.service.impl;

import de.hybris.platform.cmsfacades.data.AbstractPageData;
import de.hybris.platform.cmsfacades.pages.service.PageTypeMapping;


/**
 * Default implementation of <code>PageTypeMapping</code>.
 * 
 * @deprecated since 6.6
 */
@Deprecated(since = "6.6", forRemoval = true)
public class DefaultPageTypeMapping implements PageTypeMapping
{
	private String typecode;
	private Class<? extends AbstractPageData> typedata;

	@Override
	public String getTypecode()
	{
		return typecode;
	}

	@Override
	public void setTypecode(final String typecode)
	{
		this.typecode = typecode;
	}

	@Override
	public Class<? extends AbstractPageData> getTypedata()
	{
		return typedata;
	}

	@Override
	public void setTypedata(final Class<? extends AbstractPageData> typedata)
	{
		this.typedata = typedata;
	}

}
