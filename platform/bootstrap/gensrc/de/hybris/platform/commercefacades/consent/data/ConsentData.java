/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:46 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.consent.data;

import java.io.Serializable;
import java.util.Date;


import java.util.Objects;
public  class ConsentData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>ConsentData.code</code> property defined at extension <code>commercefacades</code>. */
		
	private String code;

	/** <i>Generated property</i> for <code>ConsentData.consentGivenDate</code> property defined at extension <code>commercefacades</code>. */
		
	private Date consentGivenDate;

	/** <i>Generated property</i> for <code>ConsentData.consentWithdrawnDate</code> property defined at extension <code>commercefacades</code>. */
		
	private Date consentWithdrawnDate;
	
	public ConsentData()
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