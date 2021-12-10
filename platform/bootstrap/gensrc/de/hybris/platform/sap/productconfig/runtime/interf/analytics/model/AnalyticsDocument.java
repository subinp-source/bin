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
import de.hybris.platform.sap.productconfig.runtime.interf.analytics.model.AnalyticsContextEntry;
import de.hybris.platform.sap.productconfig.runtime.interf.analytics.model.AnalyticsItem;
import java.util.List;


import java.util.Objects;
public  class AnalyticsDocument  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>AnalyticsDocument.rootProduct</code> property defined at extension <code>sapproductconfigruntimeinterface</code>. */
		
	private String rootProduct;

	/** <i>Generated property</i> for <code>AnalyticsDocument.contextAttributes</code> property defined at extension <code>sapproductconfigruntimeinterface</code>. */
		
	private List<AnalyticsContextEntry> contextAttributes;

	/** <i>Generated property</i> for <code>AnalyticsDocument.rootItem</code> property defined at extension <code>sapproductconfigruntimeinterface</code>. */
		
	private AnalyticsItem rootItem;
	
	public AnalyticsDocument()
	{
		// default constructor
	}
	
	public void setRootProduct(final String rootProduct)
	{
		this.rootProduct = rootProduct;
	}

	public String getRootProduct() 
	{
		return rootProduct;
	}
	
	public void setContextAttributes(final List<AnalyticsContextEntry> contextAttributes)
	{
		this.contextAttributes = contextAttributes;
	}

	public List<AnalyticsContextEntry> getContextAttributes() 
	{
		return contextAttributes;
	}
	
	public void setRootItem(final AnalyticsItem rootItem)
	{
		this.rootItem = rootItem;
	}

	public AnalyticsItem getRootItem() 
	{
		return rootItem;
	}
	

}