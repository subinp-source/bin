/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:28 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.productconfig.frontend;

import java.io.Serializable;
import de.hybris.platform.sap.productconfig.facades.PricingData;
import de.hybris.platform.sap.productconfig.facades.overview.CharacteristicGroup;
import de.hybris.platform.sap.productconfig.frontend.CPQOverviewActionType;
import de.hybris.platform.sap.productconfig.frontend.FilterData;
import de.hybris.platform.sap.productconfig.frontend.OverviewMode;
import java.util.List;


import java.util.Objects;
public  class OverviewUiData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>OverviewUiData.sourceDocumentId</code> property defined at extension <code>ysapproductconfigaddon</code>. */
		
	private String sourceDocumentId;

	/** <i>Generated property</i> for <code>OverviewUiData.configId</code> property defined at extension <code>ysapproductconfigaddon</code>. */
		
	private String configId;

	/** <i>Generated property</i> for <code>OverviewUiData.productCode</code> property defined at extension <code>ysapproductconfigaddon</code>. */
		
	private String productCode;

	/** <i>Generated property</i> for <code>OverviewUiData.quantity</code> property defined at extension <code>ysapproductconfigaddon</code>. */
		
	private long quantity;

	/** <i>Generated property</i> for <code>OverviewUiData.cartEntryNumber</code> property defined at extension <code>ysapproductconfigaddon</code>. */
		
	private Integer cartEntryNumber;

	/** <i>Generated property</i> for <code>OverviewUiData.abstractOrderCode</code> property defined at extension <code>ysapproductconfigaddon</code>. */
		
	private String abstractOrderCode;

	/** <i>Generated property</i> for <code>OverviewUiData.abstractOrderEntryNumber</code> property defined at extension <code>ysapproductconfigaddon</code>. */
		
	private Integer abstractOrderEntryNumber;

	/** <i>Generated property</i> for <code>OverviewUiData.overviewMode</code> property defined at extension <code>ysapproductconfigaddon</code>. */
		
	private OverviewMode overviewMode;

	/** <i>Generated property</i> for <code>OverviewUiData.cpqAction</code> property defined at extension <code>ysapproductconfigaddon</code>. */
		
	private CPQOverviewActionType cpqAction;

	/** <i>Generated property</i> for <code>OverviewUiData.groups</code> property defined at extension <code>ysapproductconfigaddon</code>. */
		
	private List<CharacteristicGroup> groups;

	/** <i>Generated property</i> for <code>OverviewUiData.pricing</code> property defined at extension <code>ysapproductconfigaddon</code>. */
		
	private PricingData pricing;

	/** <i>Generated property</i> for <code>OverviewUiData.csticFilterList</code> property defined at extension <code>ysapproductconfigaddon</code>. */
		
	private List<FilterData> csticFilterList;

	/** <i>Generated property</i> for <code>OverviewUiData.groupFilterList</code> property defined at extension <code>ysapproductconfigaddon</code>. */
		
	private List<FilterData> groupFilterList;
	
	public OverviewUiData()
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
	
	public void setConfigId(final String configId)
	{
		this.configId = configId;
	}

	public String getConfigId() 
	{
		return configId;
	}
	
	public void setProductCode(final String productCode)
	{
		this.productCode = productCode;
	}

	public String getProductCode() 
	{
		return productCode;
	}
	
	public void setQuantity(final long quantity)
	{
		this.quantity = quantity;
	}

	public long getQuantity() 
	{
		return quantity;
	}
	
	public void setCartEntryNumber(final Integer cartEntryNumber)
	{
		this.cartEntryNumber = cartEntryNumber;
	}

	public Integer getCartEntryNumber() 
	{
		return cartEntryNumber;
	}
	
	public void setAbstractOrderCode(final String abstractOrderCode)
	{
		this.abstractOrderCode = abstractOrderCode;
	}

	public String getAbstractOrderCode() 
	{
		return abstractOrderCode;
	}
	
	public void setAbstractOrderEntryNumber(final Integer abstractOrderEntryNumber)
	{
		this.abstractOrderEntryNumber = abstractOrderEntryNumber;
	}

	public Integer getAbstractOrderEntryNumber() 
	{
		return abstractOrderEntryNumber;
	}
	
	public void setOverviewMode(final OverviewMode overviewMode)
	{
		this.overviewMode = overviewMode;
	}

	public OverviewMode getOverviewMode() 
	{
		return overviewMode;
	}
	
	public void setCpqAction(final CPQOverviewActionType cpqAction)
	{
		this.cpqAction = cpqAction;
	}

	public CPQOverviewActionType getCpqAction() 
	{
		return cpqAction;
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
	
	public void setCsticFilterList(final List<FilterData> csticFilterList)
	{
		this.csticFilterList = csticFilterList;
	}

	public List<FilterData> getCsticFilterList() 
	{
		return csticFilterList;
	}
	
	public void setGroupFilterList(final List<FilterData> groupFilterList)
	{
		this.groupFilterList = groupFilterList;
	}

	public List<FilterData> getGroupFilterList() 
	{
		return groupFilterList;
	}
	

}