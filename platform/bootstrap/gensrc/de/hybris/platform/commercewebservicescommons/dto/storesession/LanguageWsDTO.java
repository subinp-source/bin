/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:46 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicescommons.dto.storesession;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


import java.util.Objects;
/**
 * Representation of a Language
 */
@ApiModel(value="Language", description="Representation of a Language")
public  class LanguageWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** iso code of the language<br/><br/><i>Generated property</i> for <code>LanguageWsDTO.isocode</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="isocode", value="iso code of the language") 	
	private String isocode;

	/** name of the language<br/><br/><i>Generated property</i> for <code>LanguageWsDTO.name</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="name", value="name of the language") 	
	private String name;

	/** name the language in native form<br/><br/><i>Generated property</i> for <code>LanguageWsDTO.nativeName</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="nativeName", value="name the language in native form") 	
	private String nativeName;

	/** true/false indicator when the language is active<br/><br/><i>Generated property</i> for <code>LanguageWsDTO.active</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="active", value="true/false indicator when the language is active") 	
	private Boolean active;
	
	public LanguageWsDTO()
	{
		// default constructor
	}
	
	public void setIsocode(final String isocode)
	{
		this.isocode = isocode;
	}

	public String getIsocode() 
	{
		return isocode;
	}
	
	public void setName(final String name)
	{
		this.name = name;
	}

	public String getName() 
	{
		return name;
	}
	
	public void setNativeName(final String nativeName)
	{
		this.nativeName = nativeName;
	}

	public String getNativeName() 
	{
		return nativeName;
	}
	
	public void setActive(final Boolean active)
	{
		this.active = active;
	}

	public Boolean getActive() 
	{
		return active;
	}
	

}