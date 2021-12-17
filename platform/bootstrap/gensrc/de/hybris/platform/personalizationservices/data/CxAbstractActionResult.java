/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:46 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.personalizationservices.data;

import java.io.Serializable;


import java.util.Objects;
public  class CxAbstractActionResult  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>CxAbstractActionResult.actionCode</code> property defined at extension <code>personalizationservices</code>. */
		
	private String actionCode;

	/** <i>Generated property</i> for <code>CxAbstractActionResult.customizationCode</code> property defined at extension <code>personalizationservices</code>. */
		
	private String customizationCode;

	/** <i>Generated property</i> for <code>CxAbstractActionResult.variationCode</code> property defined at extension <code>personalizationservices</code>. */
		
	private String variationCode;

	/** <i>Generated property</i> for <code>CxAbstractActionResult.customizationName</code> property defined at extension <code>personalizationservices</code>. */
		
	private String customizationName;

	/** <i>Generated property</i> for <code>CxAbstractActionResult.variationName</code> property defined at extension <code>personalizationservices</code>. */
		
	private String variationName;
	
	public CxAbstractActionResult()
	{
		// default constructor
	}
	
	public void setActionCode(final String actionCode)
	{
		this.actionCode = actionCode;
	}

	public String getActionCode() 
	{
		return actionCode;
	}
	
	public void setCustomizationCode(final String customizationCode)
	{
		this.customizationCode = customizationCode;
	}

	public String getCustomizationCode() 
	{
		return customizationCode;
	}
	
	public void setVariationCode(final String variationCode)
	{
		this.variationCode = variationCode;
	}

	public String getVariationCode() 
	{
		return variationCode;
	}
	
	public void setCustomizationName(final String customizationName)
	{
		this.customizationName = customizationName;
	}

	public String getCustomizationName() 
	{
		return customizationName;
	}
	
	public void setVariationName(final String variationName)
	{
		this.variationName = variationName;
	}

	public String getVariationName() 
	{
		return variationName;
	}
	

}