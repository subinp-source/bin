/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:46 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.yprofile.dto;

import java.io.Serializable;


import java.util.Objects;
public  class PaymentInfo  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>PaymentInfo.paymentType</code> property defined at extension <code>profileservices</code>. */
		
	private String paymentType;

	/** <i>Generated property</i> for <code>PaymentInfo.status</code> property defined at extension <code>profileservices</code>. */
		
	private String status;

	/** <i>Generated property</i> for <code>PaymentInfo.address</code> property defined at extension <code>profileservices</code>. */
		
	private Address address;
	
	public PaymentInfo()
	{
		// default constructor
	}
	
	public void setPaymentType(final String paymentType)
	{
		this.paymentType = paymentType;
	}

	public String getPaymentType() 
	{
		return paymentType;
	}
	
	public void setStatus(final String status)
	{
		this.status = status;
	}

	public String getStatus() 
	{
		return status;
	}
	
	public void setAddress(final Address address)
	{
		this.address = address;
	}

	public Address getAddress() 
	{
		return address;
	}
	

}