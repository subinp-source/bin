/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:33:00 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicescommons.dto.product;

import java.io.Serializable;
import de.hybris.platform.commercewebservicescommons.dto.product.ImageWsDTOType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


import java.util.Objects;
/**
 * Representation of an Image
 */
@ApiModel(value="Image", description="Representation of an Image")
public  class ImageWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** Type of the image, can be PRIMARY or GALLERY<br/><br/><i>Generated property</i> for <code>ImageWsDTO.imageType</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="imageType", value="Type of the image, can be PRIMARY or GALLERY") 	
	private ImageWsDTOType imageType;

	/** Format of the image, can be zoom, product, thumbnail, store, cartIcon, etc.<br/><br/><i>Generated property</i> for <code>ImageWsDTO.format</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="format", value="Format of the image, can be zoom, product, thumbnail, store, cartIcon, etc.") 	
	private String format;

	/** URL address of the image<br/><br/><i>Generated property</i> for <code>ImageWsDTO.url</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="url", value="URL address of the image") 	
	private String url;

	/** Tooltip content which is visible while image mouse hovering<br/><br/><i>Generated property</i> for <code>ImageWsDTO.altText</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="altText", value="Tooltip content which is visible while image mouse hovering") 	
	private String altText;

	/** Index of the image while displayed in gallery<br/><br/><i>Generated property</i> for <code>ImageWsDTO.galleryIndex</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="galleryIndex", value="Index of the image while displayed in gallery") 	
	private Integer galleryIndex;
	
	public ImageWsDTO()
	{
		// default constructor
	}
	
	public void setImageType(final ImageWsDTOType imageType)
	{
		this.imageType = imageType;
	}

	public ImageWsDTOType getImageType() 
	{
		return imageType;
	}
	
	public void setFormat(final String format)
	{
		this.format = format;
	}

	public String getFormat() 
	{
		return format;
	}
	
	public void setUrl(final String url)
	{
		this.url = url;
	}

	public String getUrl() 
	{
		return url;
	}
	
	public void setAltText(final String altText)
	{
		this.altText = altText;
	}

	public String getAltText() 
	{
		return altText;
	}
	
	public void setGalleryIndex(final Integer galleryIndex)
	{
		this.galleryIndex = galleryIndex;
	}

	public Integer getGalleryIndex() 
	{
		return galleryIndex;
	}
	

}