/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:46 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicescommons.dto.consent;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;


import java.util.Objects;
/**
 * Representation of a Consent
 */
@ApiModel(value="Consent", description="Representation of a Consent")
public  class ConsentWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** Code of consent<br/><br/><i>Generated property</i> for <code>ConsentWsDTO.code</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="code", value="Code of consent") 	
	private String code;

	/** Date of consenting<br/><br/><i>Generated property</i> for <code>ConsentWsDTO.consentGivenDate</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="consentGivenDate", value="Date of consenting") 	
	private Date consentGivenDate;

	/** Consent withdrawn date<br/><br/><i>Generated property</i> for <code>ConsentWsDTO.consentWithdrawnDate</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="consentWithdrawnDate", value="Consent withdrawn date") 	
	private Date consentWithdrawnDate;
	
	public ConsentWsDTO()
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
	
	public void setConsentGivenDate(final Date consentGivenDate)
	{
		this.consentGivenDate = consentGivenDate;
	}

	public Date getConsentGivenDate() 
	{
		return consentGivenDate;
	}
	
	public void setConsentWithdrawnDate(final Date consentWithdrawnDate)
	{
		this.consentWithdrawnDate = consentWithdrawnDate;
	}

	public Date getConsentWithdrawnDate() 
	{
		return consentWithdrawnDate;
	}
	

}