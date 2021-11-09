/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:27 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.couponservices.service.data;

import java.io.Serializable;
import de.hybris.platform.commercefacades.coupon.data.CouponData;


import java.util.Objects;
public  class CouponResponse  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>CouponResponse.couponId</code> property defined at extension <code>couponservices</code>. */
		
	private String couponId;

	/** <i>Generated property</i> for <code>CouponResponse.success</code> property defined at extension <code>couponservices</code>. */
		
	private Boolean success;

	/** <i>Generated property</i> for <code>CouponResponse.message</code> property defined at extension <code>couponservices</code>. */
		
	private String message;

	/** <i>Generated property</i> for <code>CouponResponse.couponData</code> property defined at extension <code>couponfacades</code>. */
		
	private CouponData couponData;
	
	public CouponResponse()
	{
		// default constructor
	}
	
	public void setCouponId(final String couponId)
	{
		this.couponId = couponId;
	}

	public String getCouponId() 
	{
		return couponId;
	}
	
	public void setSuccess(final Boolean success)
	{
		this.success = success;
	}

	public Boolean getSuccess() 
	{
		return success;
	}
	
	public void setMessage(final String message)
	{
		this.message = message;
	}

	public String getMessage() 
	{
		return message;
	}
	
	public void setCouponData(final CouponData couponData)
	{
		this.couponData = couponData;
	}

	public CouponData getCouponData() 
	{
		return couponData;
	}
	

}