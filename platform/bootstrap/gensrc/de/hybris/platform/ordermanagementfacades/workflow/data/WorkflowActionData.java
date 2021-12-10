/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:33:00 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.ordermanagementfacades.workflow.data;

import java.io.Serializable;
import de.hybris.platform.ordermanagementfacades.workflow.data.WorkflowActionAttachmentItemData;
import de.hybris.platform.workflow.enums.WorkflowActionStatus;
import java.util.Date;
import java.util.List;


import java.util.Objects;
public  class WorkflowActionData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>WorkflowActionData.code</code> property defined at extension <code>ordermanagementfacades</code>. */
		
	private String code;

	/** <i>Generated property</i> for <code>WorkflowActionData.name</code> property defined at extension <code>ordermanagementfacades</code>. */
		
	private String name;

	/** <i>Generated property</i> for <code>WorkflowActionData.comment</code> property defined at extension <code>ordermanagementfacades</code>. */
		
	private String comment;

	/** <i>Generated property</i> for <code>WorkflowActionData.description</code> property defined at extension <code>ordermanagementfacades</code>. */
		
	private String description;

	/** <i>Generated property</i> for <code>WorkflowActionData.creationTime</code> property defined at extension <code>ordermanagementfacades</code>. */
		
	private Date creationTime;

	/** <i>Generated property</i> for <code>WorkflowActionData.workflowCode</code> property defined at extension <code>ordermanagementfacades</code>. */
		
	private String workflowCode;

	/** <i>Generated property</i> for <code>WorkflowActionData.status</code> property defined at extension <code>ordermanagementfacades</code>. */
		
	private WorkflowActionStatus status;

	/** <i>Generated property</i> for <code>WorkflowActionData.attachmentItems</code> property defined at extension <code>ordermanagementfacades</code>. */
		
	private List<WorkflowActionAttachmentItemData> attachmentItems;
	
	public WorkflowActionData()
	{
		// default constructor
	}
	
	public void setCode(final String code)
	{
		this.code = code;
	}

	public String getCode() 
	{
		return code;
	}
	
	public void setName(final String name)
	{
		this.name = name;
	}

	public String getName() 
	{
		return name;
	}
	
	public void setComment(final String comment)
	{
		this.comment = comment;
	}

	public String getComment() 
	{
		return comment;
	}
	
	public void setDescription(final String description)
	{
		this.description = description;
	}

	public String getDescription() 
	{
		return description;
	}
	
	public void setCreationTime(final Date creationTime)
	{
		this.creationTime = creationTime;
	}

	public Date getCreationTime() 
	{
		return creationTime;
	}
	
	public void setWorkflowCode(final String workflowCode)
	{
		this.workflowCode = workflowCode;
	}

	public String getWorkflowCode() 
	{
		return workflowCode;
	}
	
	public void setStatus(final WorkflowActionStatus status)
	{
		this.status = status;
	}

	public WorkflowActionStatus getStatus() 
	{
		return status;
	}
	
	public void setAttachmentItems(final List<WorkflowActionAttachmentItemData> attachmentItems)
	{
		this.attachmentItems = attachmentItems;
	}

	public List<WorkflowActionAttachmentItemData> getAttachmentItems() 
	{
		return attachmentItems;
	}
	

}