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
import de.hybris.platform.cmsfacades.data.AbstractPageData;


import java.util.Objects;
/**
 * @deprecated no longer needed
 */
@Deprecated(since = "6.6", forRemoval = true)
public  class UpdatePageValidationDto  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>UpdatePageValidationDto.originalUid</code> property defined at extension <code>cmsfacades</code>. */
		
	private String originalUid;

	/** <i>Generated property</i> for <code>UpdatePageValidationDto.page</code> property defined at extension <code>cmsfacades</code>. */
		
	private AbstractPageData page;
	
	public UpdatePageValidationDto()
	{
		// default constructor
	}
	
	public void setOriginalUid(final String originalUid)
	{
		this.originalUid = originalUid;
	}

	public String getOriginalUid() 
	{
		return originalUid;
	}
	
	public void setPage(final AbstractPageData page)
	{
		this.page = page;
	}

	public AbstractPageData getPage() 
	{
		return page;
	}
	

}