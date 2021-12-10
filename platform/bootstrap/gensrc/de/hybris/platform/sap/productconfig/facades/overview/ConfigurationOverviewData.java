/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:32:59 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.productconfig.facades.overview;

import java.io.Serializable;
import de.hybris.platform.sap.productconfig.facades.PricingData;
import de.hybris.platform.sap.productconfig.facades.ProductConfigMessageData;
import de.hybris.platform.sap.productconfig.facades.overview.CharacteristicGroup;
import de.hybris.platform.sap.productconfig.facades.overview.FilterEnum;
import java.util.List;
import java.util.Set;


import java.util.Objects;
public  class ConfigurationOverviewData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>ConfigurationOverviewData.sourceDocumentId</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private String sourceDocumentId;

	/** <i>Generated property</i> for <code>ConfigurationOverviewData.id</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private String id;

	/** <i>Generated property</i> for <code>ConfigurationOverviewData.productCode</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private String productCode;

	/** <i>Generated property</i> for <code>ConfigurationOverviewData.messages</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private List<ProductConfigMessageData> messages;

	/** <i>Generated property</i> for <code>ConfigurationOverviewData.appliedCsticFilters</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private List<FilterEnum> appliedCsticFilters;

	/** <i>Generated property</i> for <code>ConfigurationOverviewData.appliedGroupFilters</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private Set<String> appliedGroupFilters;

	/** <i>Generated property</i> for <code>ConfigurationOverviewData.groups</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private List<CharacteristicGroup> groups;

	/** <i>Generated property</i> for <code>ConfigurationOverviewData.pricing</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private PricingData pricing;
	
	public ConfigurationOverviewData()
	{
		// default constructor
	}
	
	public void setSourceDocumentId(final String sourceDocumentId)
	{
		this.sourceDocumentId = sourceDocumentId;
	}

	public String getSourceDocumentId() 
	{
		return sourceDocumentId;
	}
	
	public void setId(final String id)
	{
		this.id = id;
	}

	public String getId() 
	{
		return id;
	}
	
	public void setProductCode(final String productCode)
	{
		this.productCode = productCode;
	}

	public String getProductCode() 
	{
		return productCode;
	}
	
	public void setMessages(final List<ProductConfigMessageData> messages)
	{
		this.messages = messages;
	}

	public List<ProductConfigMessageData> getMessages() 
	{
		return messages;
	}
	
	public void setAppliedCsticFilters(final List<FilterEnum> appliedCsticFilters)
	{
		this.appliedCsticFilters = appliedCsticFilters;
	}

	public List<FilterEnum> getAppliedCsticFilters() 
	{
		return appliedCsticFilters;
	}
	
	public void setAppliedGroupFilters(final Set<String> appliedGroupFilters)
	{
		this.appliedGroupFilters = appliedGroupFilters;
	}

	public Set<String> getAppliedGroupFilters() 
	{
		return appliedGroupFilters;
	}
	
	public void setGroups(final List<CharacteristicGroup> groups)
	{
		this.groups = groups;
	}

	public List<CharacteristicGroup> getGroups() 
	{
		return groups;
	}
	
	public void setPricing(final PricingData pricing)
	{
		this.pricing = pricing;
	}

	public PricingData getPricing() 
	{
		return pricing;
	}
	

}