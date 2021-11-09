/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:28 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.deeplink.pojo;

import java.io.Serializable;
import de.hybris.platform.deeplink.model.rules.DeeplinkUrlModel;


import java.util.Objects;
public  class DeeplinkUrlInfo  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>DeeplinkUrlInfo.contextObject</code> property defined at extension <code>basecommerce</code>. */
		
	private Object contextObject;

	/** <i>Generated property</i> for <code>DeeplinkUrlInfo.deeplinkUrl</code> property defined at extension <code>basecommerce</code>. */
		
	private DeeplinkUrlModel deeplinkUrl;
	
	public DeeplinkUrlInfo()
	{
		// default constructor
	}
	
	public void setContextObject(final Object contextObject)
	{
		this.contextObject = contextObject;
	}

	public Object getContextObject() 
	{
		return contextObject;
	}
	
	public void setDeeplinkUrl(final DeeplinkUrlModel deeplinkUrl)
	{
		this.deeplinkUrl = deeplinkUrl;
	}

	public DeeplinkUrlModel getDeeplinkUrl() 
	{
		return deeplinkUrl;
	}
	

}