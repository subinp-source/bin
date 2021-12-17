/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:45 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.dto;

import java.io.Serializable;


import java.util.Objects;
public  class RenderingPageValidationDto  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>RenderingPageValidationDto.pageTypeCode</code> property defined at extension <code>cmsfacades</code>. */
		
	private String pageTypeCode;

	/** <i>Generated property</i> for <code>RenderingPageValidationDto.code</code> property defined at extension <code>cmsfacades</code>. */
		
	private String code;

	/** <i>Generated property</i> for <code>RenderingPageValidationDto.pageLabelOrId</code> property defined at extension <code>cmsfacades</code>. */
		
	private String pageLabelOrId;
	
	public RenderingPageValidationDto()
	{
		// default constructor
	}
	
	public void setPageTypeCode(final String pageTypeCode)
	{
		this.pageTypeCode = pageTypeCode;
	}

	public String getPageTypeCode() 
	{
		return pageTypeCode;
	}
	
	public void setCode(final String code)
	{
		this.code = code;
	}

	public String getCode() 
	{
		return code;
	}
	
	public void setPageLabelOrId(final String pageLabelOrId)
	{
		this.pageLabelOrId = pageLabelOrId;
	}

	public String getPageLabelOrId() 
	{
		return pageLabelOrId;
	}
	

}