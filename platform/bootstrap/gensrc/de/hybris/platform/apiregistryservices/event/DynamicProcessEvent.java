/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:33:00 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.event;

import java.io.Serializable;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;

import de.hybris.platform.processengine.model.BusinessProcessModel;

public  class DynamicProcessEvent  extends AbstractEvent {


	/** <i>Generated property</i> for <code>DynamicProcessEvent.businessEvent</code> property defined at extension <code>apiregistryservices</code>. */
		
	private String businessEvent;

	/** <i>Generated property</i> for <code>DynamicProcessEvent.businessProcess</code> property defined at extension <code>apiregistryservices</code>. */
		
	private BusinessProcessModel businessProcess;
	
	public DynamicProcessEvent()
	{
		super();
	}

	public DynamicProcessEvent(final Serializable source)
	{
		super(source);
	}
	
	public void setBusinessEvent(final String businessEvent)
	{
		this.businessEvent = businessEvent;
	}

	public String getBusinessEvent() 
	{
		return businessEvent;
	}
	
	public void setBusinessProcess(final BusinessProcessModel businessProcess)
	{
		this.businessProcess = businessProcess;
	}

	public BusinessProcessModel getBusinessProcess() 
	{
		return businessProcess;
	}
	


}
