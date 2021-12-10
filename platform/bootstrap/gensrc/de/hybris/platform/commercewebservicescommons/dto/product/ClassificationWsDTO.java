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
import de.hybris.platform.commercewebservicescommons.dto.product.FeatureWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Collection;


import java.util.Objects;
/**
 * Representation of a Classification
 */
@ApiModel(value="Classification", description="Representation of a Classification")
public  class ClassificationWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** Code of the classification<br/><br/><i>Generated property</i> for <code>ClassificationWsDTO.code</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="code", value="Code of the classification") 	
	private String code;

	/** Name of the classification<br/><br/><i>Generated property</i> for <code>ClassificationWsDTO.name</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="name", value="Name of the classification") 	
	private String name;

	/** List of features for given classification<br/><br/><i>Generated property</i> for <code>ClassificationWsDTO.features</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="features", value="List of features for given classification") 	
	private Collection<FeatureWsDTO> features;
	
	public ClassificationWsDTO()
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
	
	public void setFeatures(final Collection<FeatureWsDTO> features)
	{
		this.features = features;
	}

	public Collection<FeatureWsDTO> getFeatures() 
	{
		return features;
	}
	

}