/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:33:00 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.eventtracking.model.events;

import java.io.Serializable;

import de.hybris.eventtracking.model.events.AbstractProductAwareTrackingEvent;

public  class AbstractProductAndCartAwareTrackingEvent extends AbstractProductAwareTrackingEvent {


	/** <i>Generated property</i> for <code>AbstractProductAndCartAwareTrackingEvent.cartId</code> property defined at extension <code>eventtrackingmodel</code>. */
		
	private String cartId;
	
	public AbstractProductAndCartAwareTrackingEvent()
	{
		super();
	}

	public AbstractProductAndCartAwareTrackingEvent(final Serializable source)
	{
		super(source);
	}
	
	public void setCartId(final String cartId)
	{
		this.cartId = cartId;
	}

	public String getCartId() 
	{
		return cartId;
	}
	


}
