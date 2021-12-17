/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:46 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.customercouponfacades.customercoupon.data;

import java.io.Serializable;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.customercouponfacades.customercoupon.data.CustomerCouponData;


import java.util.Objects;
public  class CustomerCouponNotificationData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>CustomerCouponNotificationData.coupon</code> property defined at extension <code>customercouponfacades</code>. */
		
	private CustomerCouponData coupon;

	/** <i>Generated property</i> for <code>CustomerCouponNotificationData.customer</code> property defined at extension <code>customercouponfacades</code>. */
		
	private CustomerData customer;

	/** <i>Generated property</i> for <code>CustomerCouponNotificationData.status</code> property defined at extension <code>customercouponfacades</code>. */
		
	private String status;
	
	public CustomerCouponNotificationData()
	{
		// default constructor
	}
	
	public void setCoupon(final CustomerCouponData coupon)
	{
		this.coupon = coupon;
	}

	public CustomerCouponData getCoupon() 
	{
		return coupon;
	}
	
	public void setCustomer(final CustomerData customer)
	{
		this.customer = customer;
	}

	public CustomerData getCustomer() 
	{
		return customer;
	}
	
	public void setStatus(final String status)
	{
		this.status = status;
	}

	public String getStatus() 
	{
		return status;
	}
	

}