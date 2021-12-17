/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:45 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.cartfileupload.data;

import java.io.Serializable;
import  de.hybris.platform.commerceservices.order.CommerceCartModification;
import java.util.List;


import java.util.Objects;
public  class SavedCartFileUploadReportData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>SavedCartFileUploadReportData.errorModificationList</code> property defined at extension <code>acceleratorservices</code>. */
		
	private List<CommerceCartModification> errorModificationList;

	/** <i>Generated property</i> for <code>SavedCartFileUploadReportData.successCount</code> property defined at extension <code>acceleratorservices</code>. */
		
	private Integer successCount;

	/** <i>Generated property</i> for <code>SavedCartFileUploadReportData.partialImportCount</code> property defined at extension <code>acceleratorservices</code>. */
		
	private Integer partialImportCount;

	/** <i>Generated property</i> for <code>SavedCartFileUploadReportData.failureCount</code> property defined at extension <code>acceleratorservices</code>. */
		
	private Integer failureCount;
	
	public SavedCartFileUploadReportData()
	{
		// default constructor
	}
	
	public void setErrorModificationList(final List<CommerceCartModification> errorModificationList)
	{
		this.errorModificationList = errorModificationList;
	}

	public List<CommerceCartModification> getErrorModificationList() 
	{
		return errorModificationList;
	}
	
	public void setSuccessCount(final Integer successCount)
	{
		this.successCount = successCount;
	}

	public Integer getSuccessCount() 
	{
		return successCount;
	}
	
	public void setPartialImportCount(final Integer partialImportCount)
	{
		this.partialImportCount = partialImportCount;
	}

	public Integer getPartialImportCount() 
	{
		return partialImportCount;
	}
	
	public void setFailureCount(final Integer failureCount)
	{
		this.failureCount = failureCount;
	}

	public Integer getFailureCount() 
	{
		return failureCount;
	}
	

}