/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:45 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.productconfig.runtime.interf.analytics.model;

import java.io.Serializable;
import de.hybris.platform.sap.productconfig.runtime.interf.analytics.model.AnalyticsPopularityIndicator;
import java.util.List;


import java.util.Objects;
public  class AnalyticsPossibleValue  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>AnalyticsPossibleValue.value</code> property defined at extension <code>sapproductconfigruntimeinterface</code>. */
		
	private String value;

	/** <i>Generated property</i> for <code>AnalyticsPossibleValue.popularityIndicators</code> property defined at extension <code>sapproductconfigruntimeinterface</code>. */
		
	private List<AnalyticsPopularityIndicator> popularityIndicators;
	
	public AnalyticsPossibleValue()
	{
		// default constructor
	}
	
	public void setValue(final String value)
	{
		this.value = value;
	}

	public String getValue() 
	{
		return value;
	}
	
	public void setPopularityIndicators(final List<AnalyticsPopularityIndicator> popularityIndicators)
	{
		this.popularityIndicators = popularityIndicators;
	}

	public List<AnalyticsPopularityIndicator> getPopularityIndicators() 
	{
		return popularityIndicators;
	}
	

}