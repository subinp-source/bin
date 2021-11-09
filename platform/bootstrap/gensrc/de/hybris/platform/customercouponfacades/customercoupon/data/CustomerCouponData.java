/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:28 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.customercouponfacades.customercoupon.data;

import de.hybris.platform.commercefacades.coupon.data.CouponData;


import java.util.Objects;
public  class CustomerCouponData extends CouponData 
{

 

	/** <i>Generated property</i> for <code>CustomerCouponData.status</code> property defined at extension <code>customercouponfacades</code>. */
		
	private String status;

	/** <i>Generated property</i> for <code>CustomerCouponData.description</code> property defined at extension <code>customercouponfacades</code>. */
		
	private String description;

	/** <i>Generated property</i> for <code>CustomerCouponData.notificationOn</code> property defined at extension <code>customercouponfacades</code>. */
		
	private boolean notificationOn;

	/** <i>Generated property</i> for <code>CustomerCouponData.productUrl</code> property defined at extension <code>customercouponfacades</code>. */
		
	private String productUrl;

	/** <i>Generated property</i> for <code>CustomerCouponData.solrFacets</code> property defined at extension <code>customercouponfacades</code>. */
		
	private String solrFacets;

	/** <i>Generated property</i> for <code>CustomerCouponData.allProductsApplicable</code> property defined at extension <code>customercouponfacades</code>. */
		
	private boolean allProductsApplicable;
	
	public CustomerCouponData()
	{
		// default constructor
	}
	
	public void setStatus(final String status)
	{
		this.status = status;
	}

	public String getStatus() 
	{
		return status;
	}
	
	public void setDescription(final String description)
	{
		this.description = description;
	}

	public String getDescription() 
	{
		return description;
	}
	
	public void setNotificationOn(final boolean notificationOn)
	{
		this.notificationOn = notificationOn;
	}

	public boolean isNotificationOn() 
	{
		return notificationOn;
	}
	
	public void setProductUrl(final String productUrl)
	{
		this.productUrl = productUrl;
	}

	public String getProductUrl() 
	{
		return productUrl;
	}
	
	/**
	 * @deprecated true
	 */
	@Deprecated(forRemoval = true)
	public void setSolrFacets(final String solrFacets)
	{
		this.solrFacets = solrFacets;
	}

	/**
	 * @deprecated true
	 */
	@Deprecated(forRemoval = true)
	public String getSolrFacets() 
	{
		return solrFacets;
	}
	
	public void setAllProductsApplicable(final boolean allProductsApplicable)
	{
		this.allProductsApplicable = allProductsApplicable;
	}

	public boolean isAllProductsApplicable() 
	{
		return allProductsApplicable;
	}
	

}