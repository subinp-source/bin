/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:33:00 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.ruleengineservices.rao;

import de.hybris.platform.ruleengineservices.rao.AbstractRuleActionRAO;
import de.hybris.platform.ruleengineservices.rao.DeliveryModeRAO;


import java.util.Objects;
public  class ShipmentRAO extends AbstractRuleActionRAO 
{

 

	/** <i>Generated property</i> for <code>ShipmentRAO.mode</code> property defined at extension <code>ruleengineservices</code>. */
		
	private DeliveryModeRAO mode;
	
	public ShipmentRAO()
	{
		// default constructor
	}
	
	public void setMode(final DeliveryModeRAO mode)
	{
		this.mode = mode;
	}

	public DeliveryModeRAO getMode() 
	{
		return mode;
	}
	

}