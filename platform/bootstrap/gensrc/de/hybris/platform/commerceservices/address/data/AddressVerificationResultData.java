/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:46 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.address.data;

import java.io.Serializable;
import de.hybris.platform.core.model.user.AddressModel;
import java.util.List;


import java.util.Objects;
/**
 * POJO representation of an address verification result.
 */
public  class AddressVerificationResultData<DECISION, FIELD_ERROR>  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>AddressVerificationResultData<DECISION, FIELD_ERROR>.decision</code> property defined at extension <code>commerceservices</code>. */
		
	private DECISION decision;

	/** <i>Generated property</i> for <code>AddressVerificationResultData<DECISION, FIELD_ERROR>.fieldErrors</code> property defined at extension <code>commerceservices</code>. */
		
	private List<FIELD_ERROR> fieldErrors;

	/** <i>Generated property</i> for <code>AddressVerificationResultData<DECISION, FIELD_ERROR>.suggestedAddresses</code> property defined at extension <code>commerceservices</code>. */
		
	private List<AddressModel> suggestedAddresses;
	
	public AddressVerificationResultData()
	{
		// default constructor
	}
	
	public void setDecision(final DECISION decision)
	{
		this.decision = decision;
	}

	public DECISION getDecision() 
	{
		return decision;
	}
	
	public void setFieldErrors(final List<FIELD_ERROR> fieldErrors)
	{
		this.fieldErrors = fieldErrors;
	}

	public List<FIELD_ERROR> getFieldErrors() 
	{
		return fieldErrors;
	}
	
	public void setSuggestedAddresses(final List<AddressModel> suggestedAddresses)
	{
		this.suggestedAddresses = suggestedAddresses;
	}

	public List<AddressModel> getSuggestedAddresses() 
	{
		return suggestedAddresses;
	}
	

}