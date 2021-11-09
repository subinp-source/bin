/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:27 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.customercouponfacades.customercoupon.data;

import java.io.Serializable;
import de.hybris.platform.core.servicelayer.data.PaginationData;
import de.hybris.platform.core.servicelayer.data.SortData;
import de.hybris.platform.customercouponfacades.customercoupon.data.CustomerCouponData;
import java.util.List;


import java.util.Objects;
public  class CustomerCouponSearchPageData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>CustomerCouponSearchPageData.coupons</code> property defined at extension <code>customercouponfacades</code>. */
		
	private List<CustomerCouponData> coupons;

	/** <i>Generated property</i> for <code>CustomerCouponSearchPageData.sorts</code> property defined at extension <code>customercouponfacades</code>. */
		
	private List<SortData> sorts;

	/** <i>Generated property</i> for <code>CustomerCouponSearchPageData.pagination</code> property defined at extension <code>customercouponfacades</code>. */
		
	private PaginationData pagination;
	
	public CustomerCouponSearchPageData()
	{
		// default constructor
	}
	
	public void setCoupons(final List<CustomerCouponData> coupons)
	{
		this.coupons = coupons;
	}

	public List<CustomerCouponData> getCoupons() 
	{
		return coupons;
	}
	
	public void setSorts(final List<SortData> sorts)
	{
		this.sorts = sorts;
	}

	public List<SortData> getSorts() 
	{
		return sorts;
	}
	
	public void setPagination(final PaginationData pagination)
	{
		this.pagination = pagination;
	}

	public PaginationData getPagination() 
	{
		return pagination;
	}
	

}