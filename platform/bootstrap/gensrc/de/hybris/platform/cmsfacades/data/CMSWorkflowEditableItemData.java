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


import java.util.Objects;
/**
 * Contains information about whether the item from a workflow is editable by session user or not.
 */
public  class CMSWorkflowEditableItemData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>CMSWorkflowEditableItemData.uid</code> property defined at extension <code>cmsfacades</code>. */
		
	private String uid;

	/** <i>Generated property</i> for <code>CMSWorkflowEditableItemData.uuid</code> property defined at extension <code>cmsfacades</code>. */
		
	private String uuid;

	/** Indicates whether the session user can edit the item from a workflow or not.<br/><br/><i>Generated property</i> for <code>CMSWorkflowEditableItemData.editableByUser</code> property defined at extension <code>cmsfacades</code>. */
		
	private boolean editableByUser;

	/** 
				Contains the workflow code where item can be edited. It either contains the code of the oldest workflow that contains item or null if there is no workflow.
			<br/><br/><i>Generated property</i> for <code>CMSWorkflowEditableItemData.editableInWorkflow</code> property defined at extension <code>cmsfacades</code>. */
		
	private String editableInWorkflow;
	
	public CMSWorkflowEditableItemData()
	{
		// default constructor
	}
	
	public void setUid(final String uid)
	{
		this.uid = uid;
	}

	public String getUid() 
	{
		return uid;
	}
	
	public void setUuid(final String uuid)
	{
		this.uuid = uuid;
	}

	public String getUuid() 
	{
		return uuid;
	}
	
	public void setEditableByUser(final boolean editableByUser)
	{
		this.editableByUser = editableByUser;
	}

	public boolean isEditableByUser() 
	{
		return editableByUser;
	}
	
	public void setEditableInWorkflow(final String editableInWorkflow)
	{
		this.editableInWorkflow = editableInWorkflow;
	}

	public String getEditableInWorkflow() 
	{
		return editableInWorkflow;
	}
	

}