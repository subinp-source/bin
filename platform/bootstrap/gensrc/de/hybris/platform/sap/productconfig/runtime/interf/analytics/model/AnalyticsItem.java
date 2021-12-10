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
import de.hybris.platform.sap.productconfig.runtime.interf.analytics.model.AnalyticsCharacteristic;
import java.util.List;


import java.util.Objects;
public  class AnalyticsItem  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>AnalyticsItem.productId</code> property defined at extension <code>sapproductconfigruntimeinterface</code>. */
		
	private String productId;

	/** <i>Generated property</i> for <code>AnalyticsItem.characteristics</code> property defined at extension <code>sapproductconfigruntimeinterface</code>. */
		
	private List<AnalyticsCharacteristic> characteristics;

	/** <i>Generated property</i> for <code>AnalyticsItem.aggregationUnit</code> property defined at extension <code>sapproductconfigruntimeinterface</code>. */
		
	private String aggregationUnit;
	
	public AnalyticsItem()
	{
		// default constructor
	}
	
	public void setProductId(final String productId)
	{
		this.productId = productId;
	}

	public String getProductId() 
	{
		return productId;
	}
	
	public void setCharacteristics(final List<AnalyticsCharacteristic> characteristics)
	{
		this.characteristics = characteristics;
	}

	public List<AnalyticsCharacteristic> getCharacteristics() 
	{
		return characteristics;
	}
	
	public void setAggregationUnit(final String aggregationUnit)
	{
		this.aggregationUnit = aggregationUnit;
	}

	public String getAggregationUnit() 
	{
		return aggregationUnit;
	}
	

}