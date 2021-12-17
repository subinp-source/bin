/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:46 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.productconfig.facades.analytics;

import java.io.Serializable;
import de.hybris.platform.sap.productconfig.runtime.interf.analytics.model.AnalyticsDocument;
import java.util.List;


import java.util.Objects;
public  class AnalyticsPopulatorInput  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>AnalyticsPopulatorInput.document</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private AnalyticsDocument document;

	/** <i>Generated property</i> for <code>AnalyticsPopulatorInput.csticUiKeys</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private List<String> csticUiKeys;
	
	public AnalyticsPopulatorInput()
	{
		// default constructor
	}
	
	public void setDocument(final AnalyticsDocument document)
	{
		this.document = document;
	}

	public AnalyticsDocument getDocument() 
	{
		return document;
	}
	
	public void setCsticUiKeys(final List<String> csticUiKeys)
	{
		this.csticUiKeys = csticUiKeys;
	}

	public List<String> getCsticUiKeys() 
	{
		return csticUiKeys;
	}
	

}