/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.product.impl;


import de.hybris.platform.commercefacades.product.ImageFormatMapping;

import java.util.Map;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of ImageFormatMapping. Provides for a simple spring configured mapping.
 */
public class DefaultImageFormatMapping implements ImageFormatMapping
{
	private Map<String, String> mapping;

	protected Map<String, String> getMapping()
	{
		return mapping;
	}

	@Required
	public void setMapping(final Map<String, String> mapping)
	{
		this.mapping = mapping;
	}

	@Override
	public String getMediaFormatQualifierForImageFormat(final String imageFormat)
	{
		return getMapping().get(imageFormat);
	}
}
