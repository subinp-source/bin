/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:33:00 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.productconfig.runtime.interf.analytics.model;

import java.io.Serializable;
import de.hybris.platform.sap.productconfig.runtime.interf.analytics.model.AnalyticsPossibleValue;
import de.hybris.platform.sap.productconfig.runtime.interf.analytics.model.AnalyticsValue;
import java.util.List;


import java.util.Objects;
public  class AnalyticsCharacteristic  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>AnalyticsCharacteristic.id</code> property defined at extension <code>sapproductconfigruntimeinterface</code>. */
		
	private String id;

	/** <i>Generated property</i> for <code>AnalyticsCharacteristic.possibleValues</code> property defined at extension <code>sapproductconfigruntimeinterface</code>. */
		
	private List<AnalyticsPossibleValue> possibleValues;

	/** <i>Generated property</i> for <code>AnalyticsCharacteristic.values</code> property defined at extension <code>sapproductconfigruntimeinterface</code>. */
		
	private List<AnalyticsValue> values;
	
	public AnalyticsCharacteristic()
	{
		// default constructor
	}
	
	public void setId(final String id)
	{
		this.id = id;
	}

	public String getId() 
	{
		return id;
	}
	
	public void setPossibleValues(final List<AnalyticsPossibleValue> possibleValues)
	{
		this.possibleValues = possibleValues;
	}

	public List<AnalyticsPossibleValue> getPossibleValues() 
	{
		return possibleValues;
	}
	
	public void setValues(final List<AnalyticsValue> values)
	{
		this.values = values;
	}

	public List<AnalyticsValue> getValues() 
	{
		return values;
	}
	

}