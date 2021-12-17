/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:46 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.yprofile.dto.cookie;

import java.io.Serializable;


import java.util.Objects;
public  class ProfileConsentCookie  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>ProfileConsentCookie.templateCode</code> property defined at extension <code>profileservices</code>. */
		
	private String templateCode;

	/** <i>Generated property</i> for <code>ProfileConsentCookie.templateVersion</code> property defined at extension <code>profileservices</code>. */
		
	private String templateVersion;

	/** <i>Generated property</i> for <code>ProfileConsentCookie.consentState</code> property defined at extension <code>profileservices</code>. */
		
	private String consentState;
	
	public ProfileConsentCookie()
	{
		// default constructor
	}
	
	public void setTemplateCode(final String templateCode)
	{
		this.templateCode = templateCode;
	}

	public String getTemplateCode() 
	{
		return templateCode;
	}
	
	public void setTemplateVersion(final String templateVersion)
	{
		this.templateVersion = templateVersion;
	}

	public String getTemplateVersion() 
	{
		return templateVersion;
	}
	
	public void setConsentState(final String consentState)
	{
		this.consentState = consentState;
	}

	public String getConsentState() 
	{
		return consentState;
	}
	

}