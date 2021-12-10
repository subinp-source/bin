/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:33:00 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.dto;

import java.io.Serializable;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;


import java.util.Objects;
public  class ComponentAndContentSlotValidationDto  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>ComponentAndContentSlotValidationDto.component</code> property defined at extension <code>cmsfacades</code>. */
		
	private AbstractCMSComponentModel component;

	/** <i>Generated property</i> for <code>ComponentAndContentSlotValidationDto.contentSlot</code> property defined at extension <code>cmsfacades</code>. */
		
	private ContentSlotModel contentSlot;
	
	public ComponentAndContentSlotValidationDto()
	{
		// default constructor
	}
	
	public void setComponent(final AbstractCMSComponentModel component)
	{
		this.component = component;
	}

	public AbstractCMSComponentModel getComponent() 
	{
		return component;
	}
	
	public void setContentSlot(final ContentSlotModel contentSlot)
	{
		this.contentSlot = contentSlot;
	}

	public ContentSlotModel getContentSlot() 
	{
		return contentSlot;
	}
	

}