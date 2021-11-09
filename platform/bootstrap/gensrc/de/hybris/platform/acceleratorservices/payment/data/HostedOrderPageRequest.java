/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:28 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.payment.data;

import java.io.Serializable;


import java.util.Objects;
public  class HostedOrderPageRequest  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>HostedOrderPageRequest.requestId</code> property defined at extension <code>acceleratorservices</code>. */
		
	private String requestId;

	/** <i>Generated property</i> for <code>HostedOrderPageRequest.requestUrl</code> property defined at extension <code>acceleratorservices</code>. */
		
	private String requestUrl;

	/** <i>Generated property</i> for <code>HostedOrderPageRequest.responseUrl</code> property defined at extension <code>acceleratorservices</code>. */
		
	private String responseUrl;

	/** <i>Generated property</i> for <code>HostedOrderPageRequest.siteName</code> property defined at extension <code>acceleratorservices</code>. */
		
	private String siteName;
	
	public HostedOrderPageRequest()
	{
		// default constructor
	}
	
	public void setRequestId(final String requestId)
	{
		this.requestId = requestId;
	}

	public String getRequestId() 
	{
		return requestId;
	}
	
	public void setRequestUrl(final String requestUrl)
	{
		this.requestUrl = requestUrl;
	}

	public String getRequestUrl() 
	{
		return requestUrl;
	}
	
	public void setResponseUrl(final String responseUrl)
	{
		this.responseUrl = responseUrl;
	}

	public String getResponseUrl() 
	{
		return responseUrl;
	}
	
	public void setSiteName(final String siteName)
	{
		this.siteName = siteName;
	}

	public String getSiteName() 
	{
		return siteName;
	}
	

}