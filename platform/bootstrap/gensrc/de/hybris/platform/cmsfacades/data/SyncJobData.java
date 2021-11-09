/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:28 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.data;

import java.io.Serializable;
import java.util.Date;


import java.util.Objects;
public  class SyncJobData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>SyncJobData.syncStatus</code> property defined at extension <code>cmsfacades</code>. */
		
	private String syncStatus;

	/** <i>Generated property</i> for <code>SyncJobData.startDate</code> property defined at extension <code>cmsfacades</code>. */
		
	private Date startDate;

	/** <i>Generated property</i> for <code>SyncJobData.endDate</code> property defined at extension <code>cmsfacades</code>. */
		
	private Date endDate;

	/** <i>Generated property</i> for <code>SyncJobData.creationDate</code> property defined at extension <code>cmsfacades</code>. */
		
	private Date creationDate;

	/** <i>Generated property</i> for <code>SyncJobData.lastModifiedDate</code> property defined at extension <code>cmsfacades</code>. */
		
	private Date lastModifiedDate;

	/** <i>Generated property</i> for <code>SyncJobData.syncResult</code> property defined at extension <code>cmsfacades</code>. */
		
	private String syncResult;

	/** <i>Generated property</i> for <code>SyncJobData.sourceCatalogVersion</code> property defined at extension <code>cmsfacades</code>. */
		
	private String sourceCatalogVersion;

	/** <i>Generated property</i> for <code>SyncJobData.targetCatalogVersion</code> property defined at extension <code>cmsfacades</code>. */
		
	private String targetCatalogVersion;

	/** <i>Generated property</i> for <code>SyncJobData.code</code> property defined at extension <code>cmsfacades</code>. */
		
	private String code;
	
	public SyncJobData()
	{
		// default constructor
	}
	
	public void setSyncStatus(final String syncStatus)
	{
		this.syncStatus = syncStatus;
	}

	public String getSyncStatus() 
	{
		return syncStatus;
	}
	
	public void setStartDate(final Date startDate)
	{
		this.startDate = startDate;
	}

	public Date getStartDate() 
	{
		return startDate;
	}
	
	public void setEndDate(final Date endDate)
	{
		this.endDate = endDate;
	}

	public Date getEndDate() 
	{
		return endDate;
	}
	
	public void setCreationDate(final Date creationDate)
	{
		this.creationDate = creationDate;
	}

	public Date getCreationDate() 
	{
		return creationDate;
	}
	
	public void setLastModifiedDate(final Date lastModifiedDate)
	{
		this.lastModifiedDate = lastModifiedDate;
	}

	public Date getLastModifiedDate() 
	{
		return lastModifiedDate;
	}
	
	public void setSyncResult(final String syncResult)
	{
		this.syncResult = syncResult;
	}

	public String getSyncResult() 
	{
		return syncResult;
	}
	
	public void setSourceCatalogVersion(final String sourceCatalogVersion)
	{
		this.sourceCatalogVersion = sourceCatalogVersion;
	}

	public String getSourceCatalogVersion() 
	{
		return sourceCatalogVersion;
	}
	
	public void setTargetCatalogVersion(final String targetCatalogVersion)
	{
		this.targetCatalogVersion = targetCatalogVersion;
	}

	public String getTargetCatalogVersion() 
	{
		return targetCatalogVersion;
	}
	
	public void setCode(final String code)
	{
		this.code = code;
	}

	public String getCode() 
	{
		return code;
	}
	

}