/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:33:00 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicescommons.dto.order;

import java.io.Serializable;
import de.hybris.platform.commercewebservicescommons.dto.order.CardTypeWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.user.AddressWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


import java.util.Objects;
/**
 * Representation of a Payment Details
 */
@ApiModel(value="PaymentDetails", description="Representation of a Payment Details")
public  class PaymentDetailsWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** Unique identifier of payment detail<br/><br/><i>Generated property</i> for <code>PaymentDetailsWsDTO.id</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="id", value="Unique identifier of payment detail") 	
	private String id;

	/** Name of account holder<br/><br/><i>Generated property</i> for <code>PaymentDetailsWsDTO.accountHolderName</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="accountHolderName", value="Name of account holder") 	
	private String accountHolderName;

	/** Type of payment card<br/><br/><i>Generated property</i> for <code>PaymentDetailsWsDTO.cardType</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="cardType", value="Type of payment card") 	
	private CardTypeWsDTO cardType;

	/** Payment card number<br/><br/><i>Generated property</i> for <code>PaymentDetailsWsDTO.cardNumber</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="cardNumber", value="Payment card number") 	
	private String cardNumber;

	/** Start month from which payment is valid<br/><br/><i>Generated property</i> for <code>PaymentDetailsWsDTO.startMonth</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="startMonth", value="Start month from which payment is valid") 	
	private String startMonth;

	/** Start year from which payment is valid<br/><br/><i>Generated property</i> for <code>PaymentDetailsWsDTO.startYear</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="startYear", value="Start year from which payment is valid") 	
	private String startYear;

	/** Month of expiration of payment<br/><br/><i>Generated property</i> for <code>PaymentDetailsWsDTO.expiryMonth</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="expiryMonth", value="Month of expiration of payment") 	
	private String expiryMonth;

	/** Year of expiration of payment<br/><br/><i>Generated property</i> for <code>PaymentDetailsWsDTO.expiryYear</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="expiryYear", value="Year of expiration of payment") 	
	private String expiryYear;

	/** Issue number<br/><br/><i>Generated property</i> for <code>PaymentDetailsWsDTO.issueNumber</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="issueNumber", value="Issue number") 	
	private String issueNumber;

	/** Identifier of subscription<br/><br/><i>Generated property</i> for <code>PaymentDetailsWsDTO.subscriptionId</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="subscriptionId", value="Identifier of subscription") 	
	private String subscriptionId;

	/** Flag to mark if payment is saved one<br/><br/><i>Generated property</i> for <code>PaymentDetailsWsDTO.saved</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="saved", value="Flag to mark if payment is saved one") 	
	private Boolean saved;

	/** Flag to mark if payment the default one<br/><br/><i>Generated property</i> for <code>PaymentDetailsWsDTO.defaultPayment</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="defaultPayment", value="Flag to mark if payment the default one") 	
	private Boolean defaultPayment;

	/** Address details to be considered as billing address<br/><br/><i>Generated property</i> for <code>PaymentDetailsWsDTO.billingAddress</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="billingAddress", value="Address details to be considered as billing address") 	
	private AddressWsDTO billingAddress;
	
	public PaymentDetailsWsDTO()
	{
		// default constructor
	}
	
	public void setId(final String id)
	{
		this.id = id;
	}

	public String getId() 
	{
		return id;
	}
	
	public void setAccountHolderName(final String accountHolderName)
	{
		this.accountHolderName = accountHolderName;
	}

	public String getAccountHolderName() 
	{
		return accountHolderName;
	}
	
	public void setCardType(final CardTypeWsDTO cardType)
	{
		this.cardType = cardType;
	}

	public CardTypeWsDTO getCardType() 
	{
		return cardType;
	}
	
	public void setCardNumber(final String cardNumber)
	{
		this.cardNumber = cardNumber;
	}

	public String getCardNumber() 
	{
		return cardNumber;
	}
	
	public void setStartMonth(final String startMonth)
	{
		this.startMonth = startMonth;
	}

	public String getStartMonth() 
	{
		return startMonth;
	}
	
	public void setStartYear(final String startYear)
	{
		this.startYear = startYear;
	}

	public String getStartYear() 
	{
		return startYear;
	}
	
	public void setExpiryMonth(final String expiryMonth)
	{
		this.expiryMonth = expiryMonth;
	}

	public String getExpiryMonth() 
	{
		return expiryMonth;
	}
	
	public void setExpiryYear(final String expiryYear)
	{
		this.expiryYear = expiryYear;
	}

	public String getExpiryYear() 
	{
		return expiryYear;
	}
	
	public void setIssueNumber(final String issueNumber)
	{
		this.issueNumber = issueNumber;
	}

	public String getIssueNumber() 
	{
		return issueNumber;
	}
	
	public void setSubscriptionId(final String subscriptionId)
	{
		this.subscriptionId = subscriptionId;
	}

	public String getSubscriptionId() 
	{
		return subscriptionId;
	}
	
	public void setSaved(final Boolean saved)
	{
		this.saved = saved;
	}

	public Boolean getSaved() 
	{
		return saved;
	}
	
	public void setDefaultPayment(final Boolean defaultPayment)
	{
		this.defaultPayment = defaultPayment;
	}

	public Boolean getDefaultPayment() 
	{
		return defaultPayment;
	}
	
	public void setBillingAddress(final AddressWsDTO billingAddress)
	{
		this.billingAddress = billingAddress;
	}

	public AddressWsDTO getBillingAddress() 
	{
		return billingAddress;
	}
	

}