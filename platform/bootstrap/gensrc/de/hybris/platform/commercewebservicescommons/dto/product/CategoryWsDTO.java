/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:46 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicescommons.dto.product;

import java.io.Serializable;
import de.hybris.platform.commercewebservicescommons.dto.product.ImageWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


import java.util.Objects;
/**
 * Representation of a Category
 */
@ApiModel(value="Category", description="Representation of a Category")
public  class CategoryWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** Code of the category<br/><br/><i>Generated property</i> for <code>CategoryWsDTO.code</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="code", value="Code of the category") 	
	private String code;

	/** Name of the category<br/><br/><i>Generated property</i> for <code>CategoryWsDTO.name</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="name", value="Name of the category") 	
	private String name;

	/** URL of the category<br/><br/><i>Generated property</i> for <code>CategoryWsDTO.url</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="url", value="URL of the category") 	
	private String url;

	/** Category image<br/><br/><i>Generated property</i> for <code>CategoryWsDTO.image</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="image", value="Category image") 	
	private ImageWsDTO image;
	
	public CategoryWsDTO()
	{
		// default constructor
	}
	
	public void setCode(final String code)
	{
		this.code = code;
	}

	public String getCode() 
	{
		return code;
	}
	
	public void setName(final String name)
	{
		this.name = name;
	}

	public String getName() 
	{
		return name;
	}
	
	public void setUrl(final String url)
	{
		this.url = url;
	}

	public String getUrl() 
	{
		return url;
	}
	
	public void setImage(final ImageWsDTO image)
	{
		this.image = image;
	}

	public ImageWsDTO getImage() 
	{
		return image;
	}
	

}