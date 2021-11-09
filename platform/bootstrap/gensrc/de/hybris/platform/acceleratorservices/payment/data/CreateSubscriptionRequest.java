/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:27 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.payment.data;

import de.hybris.platform.acceleratorservices.payment.data.HostedOrderPageRequest;


import java.util.Objects;
public  class CreateSubscriptionRequest extends HostedOrderPageRequest 
{

 

	/** <i>Generated property</i> for <code>CreateSubscriptionRequest.customerBillToData</code> property defined at extension <code>acceleratorservices</code>. */
		
	private CustomerBillToData customerBillToData;

	/** <i>Generated property</i> for <code>CreateSubscriptionRequest.customerShipToData</code> property defined at extension <code>acceleratorservices</code>. */
		
	private CustomerShipToData customerShipToData;

	/** <i>Generated property</i> for <code>CreateSubscriptionRequest.paymentInfoData</code> property defined at extension <code>acceleratorservices</code>. */
		
	private PaymentInfoData paymentInfoData;

	/** <i>Generated property</i> for <code>CreateSubscriptionRequest.orderInfoData</code> property defined at extension <code>acceleratorservices</code>. */
		
	private OrderInfoData orderInfoData;

	/** <i>Generated property</i> for <code>CreateSubscriptionRequest.orderPageAppearanceData</code> property defined at extension <code>acceleratorservices</code>. */
		
	private OrderPageAppearanceData orderPageAppearanceData;

	/** <i>Generated property</i> for <code>CreateSubscriptionRequest.orderPageConfirmationData</code> property defined at extension <code>acceleratorservices</code>. */
		
	private OrderPageConfirmationData orderPageConfirmationData;

	/** <i>Generated property</i> for <code>CreateSubscriptionRequest.signatureData</code> property defined at extension <code>acceleratorservices</code>. */
		
	private SignatureData signatureData;

	/** <i>Generated property</i> for <code>CreateSubscriptionRequest.subscriptionSignatureData</code> property defined at extension <code>acceleratorservices</code>. */
		
	private SubscriptionSignatureData subscriptionSignatureData;
	
	public CreateSubscriptionRequest()
	{
		// default constructor
	}
	
	public void setCustomerBillToData(final CustomerBillToData customerBillToData)
	{
		this.customerBillToData = customerBillToData;
	}

	public CustomerBillToData getCustomerBillToData() 
	{
		return customerBillToData;
	}
	
	public void setCustomerShipToData(final CustomerShipToData customerShipToData)
	{
		this.customerShipToData = customerShipToData;
	}

	public CustomerShipToData getCustomerShipToData() 
	{
		return customerShipToData;
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
	
	public void setOrderPageAppearanceData(final OrderPageAppearanceData orderPageAppearanceData)
	{
		this.orderPageAppearanceData = orderPageAppearanceData;
	}

	public OrderPageAppearanceData getOrderPageAppearanceData() 
	{
		return orderPageAppearanceData;
	}
	
	public void setOrderPageConfirmationData(final OrderPageConfirmationData orderPageConfirmationData)
	{
		this.orderPageConfirmationData = orderPageConfirmationData;
	}

	public OrderPageConfirmationData getOrderPageConfirmationData() 
	{
		return orderPageConfirmationData;
	}
	
	public void setSignatureData(final SignatureData signatureData)
	{
		this.signatureData = signatureData;
	}

	public SignatureData getSignatureData() 
	{
		return signatureData;
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