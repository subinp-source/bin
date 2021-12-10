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
import java.io.InputStream;


import java.util.Objects;
public  class MediaFileDto  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>MediaFileDto.inputStream</code> property defined at extension <code>cmsfacades</code>. */
		
	private InputStream inputStream;

	/** <i>Generated property</i> for <code>MediaFileDto.mime</code> property defined at extension <code>cmsfacades</code>. */
		
	private String mime;

	/** <i>Generated property</i> for <code>MediaFileDto.name</code> property defined at extension <code>cmsfacades</code>. */
		
	private String name;

	/** <i>Generated property</i> for <code>MediaFileDto.size</code> property defined at extension <code>cmsfacades</code>. */
		
	private Long size;
	
	public MediaFileDto()
	{
		// default constructor
	}
	
	public void setInputStream(final InputStream inputStream)
	{
		this.inputStream = inputStream;
	}

	public InputStream getInputStream() 
	{
		return inputStream;
	}
	
	public void setMime(final String mime)
	{
		this.mime = mime;
	}

	public String getMime() 
	{
		return mime;
	}
	
	public void setName(final String name)
	{
		this.name = name;
	}

	public String getName() 
	{
		return name;
	}
	
	public void setSize(final Long size)
	{
		this.size = size;
	}

	public Long getSize() 
	{
		return size;
	}
	

}