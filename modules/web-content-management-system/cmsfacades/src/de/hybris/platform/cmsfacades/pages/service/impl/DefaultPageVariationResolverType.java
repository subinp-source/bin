/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.pages.service.impl;

import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cmsfacades.pages.service.PageVariationResolver;
import de.hybris.platform.cmsfacades.pages.service.PageVariationResolverType;


/**
 * Default implementation of <code>PageVariationResolverType</code>.
 */
public class DefaultPageVariationResolverType implements PageVariationResolverType
{
	private String typecode;
	private PageVariationResolver<AbstractPageModel> resolver;

	@Override
	public String getTypecode()
	{
		return typecode;
	}

	public void setTypecode(final String typecode)
	{
		this.typecode = typecode;
	}

	@Override
	public PageVariationResolver<AbstractPageModel> getResolver()
	{
		return resolver;
	}

	public void setResolver(final PageVariationResolver<AbstractPageModel> resolver)
	{
		this.resolver = resolver;
	}

}
