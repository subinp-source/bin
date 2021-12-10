/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:33:00 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.payment.data;

import de.hybris.platform.acceleratorservices.payment.data.AbstractPaymentResult;


import java.util.Objects;
public  class CreateSubscriptionResult extends AbstractPaymentResult 
{

 

	/** <i>Generated property</i> for <code>CreateSubscriptionResult.authReplyData</code> property defined at extension <code>acceleratorservices</code>. */
		
	private AuthReplyData authReplyData;

	/** <i>Generated property</i> for <code>CreateSubscriptionResult.customerInfoData</code> property defined at extension <code>acceleratorservices</code>. */
		
	private CustomerInfoData customerInfoData;

	/** <i>Generated property</i> for <code>CreateSubscriptionResult.paymentInfoData</code> property defined at extension <code>acceleratorservices</code>. */
		
	private PaymentInfoData paymentInfoData;

	/** <i>Generated property</i> for <code>CreateSubscriptionResult.orderInfoData</code> property defined at extension <code>acceleratorservices</code>. */
		
	private OrderInfoData orderInfoData;

	/** <i>Generated property</i> for <code>CreateSubscriptionResult.signatureData</code> property defined at extension <code>acceleratorservices</code>. */
		
	private SignatureData signatureData;

	/** <i>Generated property</i> for <code>CreateSubscriptionResult.subscriptionInfoData</code> property defined at extension <code>acceleratorservices</code>. */
		
	private SubscriptionInfoData subscriptionInfoData;

	/** <i>Generated property</i> for <code>CreateSubscriptionResult.subscriptionSignatureData</code> property defined at extension <code>acceleratorservices</code>. */
		
	private SubscriptionSignatureData subscriptionSignatureData;
	
	public CreateSubscriptionResult()
	{
		// default constructor
	}
	
	public void setAuthReplyData(final AuthReplyData authReplyData)
	{
		this.authReplyData = authReplyData;
	}

	public AuthReplyData getAuthReplyData() 
	{
		return authReplyData;
	}
	
	public void setCustomerInfoData(final CustomerInfoData customerInfoData)
	{
		this.customerInfoData = customerInfoData;
	}

	public CustomerInfoData getCustomerInfoData() 
	{
		return customerInfoData;
	}
	
	public void setPaymentInfoData(final PaymentInfoData paymentInfoData)
	{
		this.paymentInfoData = paymentInfoData;
	}

	public PaymentInfoData getPaymentInfoData() 
	{
		return paymentInfoData;
	}
	
	public void setOrderInfoData(final OrderInfoData orderInfoData)
	{
		this.orderInfoData = orderInfoData;
	}

	public OrderInfoData getOrderInfoData() 
	{
		return orderInfoData;
	}
	
	public void setSignatureData(final SignatureData signatureData)
	{
		this.signatureData = signatureData;
	}

	public SignatureData getSignatureData() 
	{
		return signatureData;
	}
	
	public void setSubscriptionInfoData(final SubscriptionInfoData subscriptionInfoData)
	{
		this.subscriptionInfoData = subscriptionInfoData;
	}

	public SubscriptionInfoData getSubscriptionInfoData() 
	{
		return subscriptionInfoData;
	}
	
	public void setSubscriptionSignatureData(final SubscriptionSignatureData subscriptionSignatureData)
	{
		this.subscriptionSignatureData = subscriptionSignatureData;
	}

	public SubscriptionSignatureData getSubscriptionSignatureData() 
	{
		return subscriptionSignatureData;
	}
	

}