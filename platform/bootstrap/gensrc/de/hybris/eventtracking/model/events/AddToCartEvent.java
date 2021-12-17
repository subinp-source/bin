/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:46 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.eventtracking.model.events;

import java.io.Serializable;

import de.hybris.eventtracking.model.events.AbstractProductAndCartAwareTrackingEvent;

public  class AddToCartEvent extends AbstractProductAndCartAwareTrackingEvent {


	/** <i>Generated property</i> for <code>AddToCartEvent.quantity</code> property defined at extension <code>eventtrackingmodel</code>. */
		
	private String quantity;
	
	public AddToCartEvent()
	{
		super();
	}

	public AddToCartEvent(final Serializable source)
	{
		super(source);
	}
	
	public void setQuantity(final String quantity)
	{
		this.quantity = quantity;
	}

	public String getQuantity() 
	{
		return quantity;
	}
	


}
