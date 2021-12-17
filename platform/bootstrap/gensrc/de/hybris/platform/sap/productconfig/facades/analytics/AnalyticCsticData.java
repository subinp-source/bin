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
import de.hybris.platform.sap.productconfig.facades.analytics.AnalyticCsticValueData;
import java.util.Map;


import java.util.Objects;
public  class AnalyticCsticData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>AnalyticCsticData.csticUiKey</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private String csticUiKey;

	/** <i>Generated property</i> for <code>AnalyticCsticData.analyticValues</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private Map<String,AnalyticCsticValueData> analyticValues;
	
	public AnalyticCsticData()
	{
		// default constructor
	}
	
	public void setCsticUiKey(final String csticUiKey)
	{
		this.csticUiKey = csticUiKey;
	}

	public String getCsticUiKey() 
	{
		return csticUiKey;
	}
	
	public void setAnalyticValues(final Map<String,AnalyticCsticValueData> analyticValues)
	{
		this.analyticValues = analyticValues;
	}

	public Map<String,AnalyticCsticValueData> getAnalyticValues() 
	{
		return analyticValues;
	}
	

}