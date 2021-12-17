/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:45 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.personalizationservices.voters;

import java.io.Serializable;


import java.util.Objects;
/**
 * Vote from interceptors used for defining what should be done with given request.
 */
public  class CxOccInterceptorVote  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>CxOccInterceptorVote.vote</code> property defined at extension <code>personalizationservices</code>. */
		
	private boolean vote;

	/** <i>Generated property</i> for <code>CxOccInterceptorVote.conclusive</code> property defined at extension <code>personalizationservices</code>. */
		
	private boolean conclusive;
	
	public CxOccInterceptorVote()
	{
		// default constructor
	}
	
	public void setVote(final boolean vote)
	{
		this.vote = vote;
	}

	public boolean isVote() 
	{
		return vote;
	}
	
	public void setConclusive(final boolean conclusive)
	{
		this.conclusive = conclusive;
	}

	public boolean isConclusive() 
	{
		return conclusive;
	}
	

}