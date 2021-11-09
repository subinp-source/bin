/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 */
package de.hybris.platform.couponservices.rao;

import java.util.Objects;

public class CouponRAO  implements java.io.Serializable 
{

	/** <i>Generated property</i> for <code>CouponRAO.couponId</code> property defined at extension <code>couponservices</code>. */
	private String couponId;
	/** <i>Generated property</i> for <code>CouponRAO.couponCode</code> property defined at extension <code>couponservices</code>. */
	private String couponCode;
		
	public CouponRAO()
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
		
		
	public void setCouponCode(final String couponCode)
	{
		this.couponCode = couponCode;
	}
	
		
	public String getCouponCode() 
	{
		return couponCode;
	}
		
	

		@Override
	public boolean equals(final Object o)
	{
		
	if (o == null) return false;
	if (o == this) return true;

	if (getClass() != o.getClass()) return false;

	final CouponRAO other = (CouponRAO) o;
	return	Objects.equals(getCouponId(), other.getCouponId())
 &&  Objects.equals(getCouponCode(), other.getCouponCode())
  ;
	}

	@Override
	public int hashCode()
	{
		int result = 1;
		Object attribute;

				attribute = couponId;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());
				attribute = couponCode;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());
		
		return result;
	}
	}