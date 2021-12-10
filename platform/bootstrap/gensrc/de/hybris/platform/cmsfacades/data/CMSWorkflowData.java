/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:33:00 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.data;

import java.util.List;


import java.util.Objects;
public  class CMSWorkflowData extends CMSCreateVersionData 
{

 

	/** <i>Generated property</i> for <code>CMSWorkflowData.workflowCode</code> property defined at extension <code>cmsfacades</code>. */
		
	private String workflowCode;

	/** <i>Generated property</i> for <code>CMSWorkflowData.templateCode</code> property defined at extension <code>cmsfacades</code>. */
		
	private String templateCode;

	/** <i>Generated property</i> for <code>CMSWorkflowData.description</code> property defined at extension <code>cmsfacades</code>. */
		
	private String description;

	/** <i>Generated property</i> for <code>CMSWorkflowData.attachments</code> property defined at extension <code>cmsfacades</code>. */
		
	private List<String> attachments;

	/** <i>Generated property</i> for <code>CMSWorkflowData.status</code> property defined at extension <code>cmsfacades</code>. */
		
	private String status;

	/** <i>Generated property</i> for <code>CMSWorkflowData.statuses</code> property defined at extension <code>cmsfacades</code>. */
		
	private List<String> statuses;

	/** <i>Generated property</i> for <code>CMSWorkflowData.isAvailableForCurrentPrincipal</code> property defined at extension <code>cmsfacades</code>. */
		
	private Boolean isAvailableForCurrentPrincipal;

	/** <i>Generated property</i> for <code>CMSWorkflowData.originalWorkflowCode</code> property defined at extension <code>cmsfacades</code>. */
		
	private String originalWorkflowCode;

	/** <i>Generated property</i> for <code>CMSWorkflowData.actions</code> property defined at extension <code>cmsfacades</code>. */
		
	private List<CMSWorkflowActionData> actions;

	/** <i>Generated property</i> for <code>CMSWorkflowData.canModifyItemInWorkflow</code> property defined at extension <code>cmsfacades</code>. */
		
	private Boolean canModifyItemInWorkflow;
	
	public CMSWorkflowData()
	{
		// default constructor
	}
	
	public void setWorkflowCode(final String workflowCode)
	{
		this.workflowCode = workflowCode;
	}

	public String getWorkflowCode() 
	{
		return workflowCode;
	}
	
	public void setTemplateCode(final String templateCode)
	{
		this.templateCode = templateCode;
	}

	public String getTemplateCode() 
	{
		return templateCode;
	}
	
	public void setDescription(final String description)
	{
		this.description = description;
	}

	public String getDescription() 
	{
		return description;
	}
	
	public void setAttachments(final List<String> attachments)
	{
		this.attachments = attachments;
	}

	public List<String> getAttachments() 
	{
		return attachments;
	}
	
	public void setStatus(final String status)
	{
		this.status = status;
	}

	public String getStatus() 
	{
		return status;
	}
	
	public void setStatuses(final List<String> statuses)
	{
		this.statuses = statuses;
	}

	public List<String> getStatuses() 
	{
		return statuses;
	}
	
	public void setIsAvailableForCurrentPrincipal(final Boolean isAvailableForCurrentPrincipal)
	{
		this.isAvailableForCurrentPrincipal = isAvailableForCurrentPrincipal;
	}

	public Boolean getIsAvailableForCurrentPrincipal() 
	{
		return isAvailableForCurrentPrincipal;
	}
	
	public void setOriginalWorkflowCode(final String originalWorkflowCode)
	{
		this.originalWorkflowCode = originalWorkflowCode;
	}

	public String getOriginalWorkflowCode() 
	{
		return originalWorkflowCode;
	}
	
	public void setActions(final List<CMSWorkflowActionData> actions)
	{
		this.actions = actions;
	}

	public List<CMSWorkflowActionData> getActions() 
	{
		return actions;
	}
	
	public void setCanModifyItemInWorkflow(final Boolean canModifyItemInWorkflow)
	{
		this.canModifyItemInWorkflow = canModifyItemInWorkflow;
	}

	public Boolean getCanModifyItemInWorkflow() 
	{
		return canModifyItemInWorkflow;
	}
	

}