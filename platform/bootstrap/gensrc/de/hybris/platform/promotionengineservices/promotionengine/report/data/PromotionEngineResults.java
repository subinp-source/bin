/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:33:00 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.promotionengineservices.promotionengine.report.data;

import java.io.Serializable;
import de.hybris.platform.promotionengineservices.promotionengine.report.data.OrderEntryLevelPromotionEngineResults;
import de.hybris.platform.promotionengineservices.promotionengine.report.data.OrderLevelPromotionEngineResults;
import java.util.List;


import java.util.Objects;
public  class PromotionEngineResults  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>PromotionEngineResults.orderLevelPromotionEngineResults</code> property defined at extension <code>promotionengineservices</code>. */
		
	private OrderLevelPromotionEngineResults orderLevelPromotionEngineResults;

	/** <i>Generated property</i> for <code>PromotionEngineResults.orderEntryLevelPromotionEngineResults</code> property defined at extension <code>promotionengineservices</code>. */
		
	private List<OrderEntryLevelPromotionEngineResults> orderEntryLevelPromotionEngineResults;
	
	public PromotionEngineResults()
	{
		// default constructor
	}
	
	public void setOrderLevelPromotionEngineResults(final OrderLevelPromotionEngineResults orderLevelPromotionEngineResults)
	{
		this.orderLevelPromotionEngineResults = orderLevelPromotionEngineResults;
	}

	public OrderLevelPromotionEngineResults getOrderLevelPromotionEngineResults() 
	{
		return orderLevelPromotionEngineResults;
	}
	
	public void setOrderEntryLevelPromotionEngineResults(final List<OrderEntryLevelPromotionEngineResults> orderEntryLevelPromotionEngineResults)
	{
		this.orderEntryLevelPromotionEngineResults = orderEntryLevelPromotionEngineResults;
	}

	public List<OrderEntryLevelPromotionEngineResults> getOrderEntryLevelPromotionEngineResults() 
	{
		return orderEntryLevelPromotionEngineResults;
	}
	

}