/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:33:00 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.storefront.data;

import java.io.Serializable;


import java.util.Objects;
public  class JavaScriptVariableData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>JavaScriptVariableData.qualifier</code> property defined at extension <code>acceleratorservices</code>. */
		
	private String qualifier;

	/** <i>Generated property</i> for <code>JavaScriptVariableData.value</code> property defined at extension <code>acceleratorservices</code>. */
		
	private String value;
	
	public JavaScriptVariableData()
	{
		// default constructor
	}
	
	public void setQualifier(final String qualifier)
	{
		this.qualifier = qualifier;
	}

	public String getQualifier() 
	{
		return qualifier;
	}
	
	public void setValue(final String value)
	{
		this.value = value;
	}

	public String getValue() 
	{
		return value;
	}
	

}