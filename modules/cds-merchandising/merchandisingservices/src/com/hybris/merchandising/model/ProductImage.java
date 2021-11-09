/**
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.merchandising.model;

public class ProductImage
{
	private String mainImage;
	private String thumbnailImage;

	/**
	 * Gets the main image URL.
	 * @return the main image URL - this is a large image.
	 */
	public String getMainImage()
	{
		return mainImage;
	}

	/**
	 * Sets the main image URL.
	 * @param mainImage the URL for the main image to show.
	 */
	public void setMainImage(final String mainImage)
	{
		this.mainImage = mainImage;
	}

	/**
	 * Gets the thumbnail image URL.
	 * @return the thumbnail image URL.
	 */
	public String getThumbnailImage()
	{
		return thumbnailImage;
	}

	/**
	 * Sets the thumbnail image URL.
	 * @param thumbnailImage the thumbnail image URL to show.
	 */
	public void setThumbnailImage(final String thumbnailImage)
	{
		this.thumbnailImage = thumbnailImage;
	}

}
