/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:28 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.dto;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


import java.util.Objects;
@JsonIgnoreProperties(ignoreUnknown = true)
public  class RegisteredDestinationData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>RegisteredDestinationData.identifier</code> property defined at extension <code>apiregistryservices</code>. */
	@JsonProperty("identifier") 	
	private String identifier;

	/** <i>Generated property</i> for <code>RegisteredDestinationData.targetId</code> property defined at extension <code>apiregistryservices</code>. */
	@JsonProperty("id") 	
	private String targetId;
	
	public RegisteredDestinationData()
	{
		// default constructor
	}
	
@JsonProperty("identifier") 	public void setIdentifier(final String identifier)
	{
		this.identifier = identifier;
	}

@JsonProperty("identifier") 	public String getIdentifier() 
	{
		return identifier;
	}
	
@JsonProperty("id") 	public void setTargetId(final String targetId)
	{
		this.targetId = targetId;
	}

@JsonProperty("id") 	public String getTargetId() 
	{
		return targetId;
	}
	

}