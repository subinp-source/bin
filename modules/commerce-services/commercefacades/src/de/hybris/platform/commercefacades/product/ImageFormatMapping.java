/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.product;


/**
 * Image format mapping to media format qualifier.
 */
public interface ImageFormatMapping
{
	/**
	 * Get the media format qualifier for an image format. The image format is a useful frontend qualifier, e.g.
	 * "thumbnail"
	 * 
	 * @param imageFormat
	 *           the image format
	 * @return the media format qualifier
	 */
	String getMediaFormatQualifierForImageFormat(String imageFormat);
}
