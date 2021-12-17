/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:46 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.data;

import java.io.Serializable;
import java.util.Map;


import java.util.Objects;
public  class MediaContainerData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>MediaContainerData.formatMediaCodeMap</code> property defined at extension <code>cmsfacades</code>. */
		
	private Map<String, String> formatMediaCodeMap;

	/** <i>Generated property</i> for <code>MediaContainerData.qualifier</code> property defined at extension <code>cmsfacades</code>. */
		
	private String qualifier;

	/** <i>Generated property</i> for <code>MediaContainerData.catalogVersion</code> property defined at extension <code>cmsfacades</code>. */
		
	private String catalogVersion;

	/** <i>Generated property</i> for <code>MediaContainerData.thumbnailUrl</code> property defined at extension <code>cmsfacades</code>. */
		
	private String thumbnailUrl;

	/** <i>Generated property</i> for <code>MediaContainerData.mediaContainerUuid</code> property defined at extension <code>cmsfacades</code>. */
		
	private String mediaContainerUuid;
	
	public MediaContainerData()
	{
		// default constructor
	}
	
	public void setFormatMediaCodeMap(final Map<String, String> formatMediaCodeMap)
	{
		this.formatMediaCodeMap = formatMediaCodeMap;
	}

	public Map<String, String> getFormatMediaCodeMap() 
	{
		return formatMediaCodeMap;
	}
	
	public void setQualifier(final String qualifier)
	{
		this.qualifier = qualifier;
	}

	public String getQualifier() 
	{
		return qualifier;
	}
	
	public void setCatalogVersion(final String catalogVersion)
	{
		this.catalogVersion = catalogVersion;
	}

	public String getCatalogVersion() 
	{
		return catalogVersion;
	}
	
	public void setThumbnailUrl(final String thumbnailUrl)
	{
		this.thumbnailUrl = thumbnailUrl;
	}

	public String getThumbnailUrl() 
	{
		return thumbnailUrl;
	}
	
	public void setMediaContainerUuid(final String mediaContainerUuid)
	{
		this.mediaContainerUuid = mediaContainerUuid;
	}

	public String getMediaContainerUuid() 
	{
		return mediaContainerUuid;
	}
	

}