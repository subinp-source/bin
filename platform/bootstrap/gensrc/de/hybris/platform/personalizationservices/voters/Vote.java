/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:27 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.personalizationservices.voters;

import java.io.Serializable;
import de.hybris.platform.personalizationservices.RecalculateAction;
import java.util.Set;


import java.util.Objects;
/**
 * Vote used for defining what should be done with given request.
 */
public  class Vote  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>Vote.recalculateActions</code> property defined at extension <code>personalizationservices</code>. */
		
	private Set<RecalculateAction> recalculateActions;

	/** <i>Generated property</i> for <code>Vote.conclusive</code> property defined at extension <code>personalizationservices</code>. */
		
	private boolean conclusive;
	
	public Vote()
	{
		// default constructor
	}
	
	public void setRecalculateActions(final Set<RecalculateAction> recalculateActions)
	{
		this.recalculateActions = recalculateActions;
	}

	public Set<RecalculateAction> getRecalculateActions() 
	{
		return recalculateActions;
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