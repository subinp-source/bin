/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:28 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.dto;

import java.io.Serializable;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;


import java.util.Objects;
public  class ComponentTypeAndContentSlotValidationDto  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>ComponentTypeAndContentSlotValidationDto.componentType</code> property defined at extension <code>cmsfacades</code>. */
		
	private String componentType;

	/** <i>Generated property</i> for <code>ComponentTypeAndContentSlotValidationDto.contentSlot</code> property defined at extension <code>cmsfacades</code>. */
		
	private ContentSlotModel contentSlot;

	/** <i>Generated property</i> for <code>ComponentTypeAndContentSlotValidationDto.page</code> property defined at extension <code>cmsfacades</code>. */
		
	private AbstractPageModel page;
	
	public ComponentTypeAndContentSlotValidationDto()
	{
		// default constructor
	}
	
	public void setComponentType(final String componentType)
	{
		this.componentType = componentType;
	}

	public String getComponentType() 
	{
		return componentType;
	}
	
	public void setContentSlot(final ContentSlotModel contentSlot)
	{
		this.contentSlot = contentSlot;
	}

	public ContentSlotModel getContentSlot() 
	{
		return contentSlot;
	}
	
	public void setPage(final AbstractPageModel page)
	{
		this.page = page;
	}

	public AbstractPageModel getPage() 
	{
		return page;
	}
	

}