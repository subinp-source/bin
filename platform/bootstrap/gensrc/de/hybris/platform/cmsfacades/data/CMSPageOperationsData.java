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
import de.hybris.platform.cmsfacades.enums.CMSPageOperation;


import java.util.Objects;
public  class CMSPageOperationsData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>CMSPageOperationsData.operation</code> property defined at extension <code>cmsfacades</code>. */
		
	private CMSPageOperation operation;

	/** <i>Generated property</i> for <code>CMSPageOperationsData.catalogId</code> property defined at extension <code>cmsfacades</code>. */
		
	private String catalogId;

	/** <i>Generated property</i> for <code>CMSPageOperationsData.sourceCatalogVersion</code> property defined at extension <code>cmsfacades</code>. */
		
	private String sourceCatalogVersion;

	/** <i>Generated property</i> for <code>CMSPageOperationsData.targetCatalogVersion</code> property defined at extension <code>cmsfacades</code>. */
		
	private String targetCatalogVersion;
	
	public CMSPageOperationsData()
	{
		// default constructor
	}
	
	public void setOperation(final CMSPageOperation operation)
	{
		this.operation = operation;
	}

	public CMSPageOperation getOperation() 
	{
		return operation;
	}
	
	public void setCatalogId(final String catalogId)
	{
		this.catalogId = catalogId;
	}

	public String getCatalogId() 
	{
		return catalogId;
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
	

}