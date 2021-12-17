/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:46 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.payment.data;

import de.hybris.platform.acceleratorservices.payment.data.HostedOrderPageResult;


import java.util.Objects;
public  class AbstractPaymentResult extends HostedOrderPageResult 
{

 

	/** <i>Generated property</i> for <code>AbstractPaymentResult.decision</code> property defined at extension <code>acceleratorservices</code>. */
		
	private String decision;

	/** <i>Generated property</i> for <code>AbstractPaymentResult.decisionPublicSignature</code> property defined at extension <code>acceleratorservices</code>. */
		
	private String decisionPublicSignature;

	/** <i>Generated property</i> for <code>AbstractPaymentResult.transactionSignatureVerified</code> property defined at extension <code>acceleratorservices</code>. */
		
	private Boolean transactionSignatureVerified;
	
	public AbstractPaymentResult()
	{
		// default constructor
	}
	
	public void setDecision(final String decision)
	{
		this.decision = decision;
	}

	public String getDecision() 
	{
		return decision;
	}
	
	public void setDecisionPublicSignature(final String decisionPublicSignature)
	{
		this.decisionPublicSignature = decisionPublicSignature;
	}

	public String getDecisionPublicSignature() 
	{
		return decisionPublicSignature;
	}
	
	public void setTransactionSignatureVerified(final Boolean transactionSignatureVerified)
	{
		this.transactionSignatureVerified = transactionSignatureVerified;
	}

	public Boolean getTransactionSignatureVerified() 
	{
		return transactionSignatureVerified;
	}
	

}