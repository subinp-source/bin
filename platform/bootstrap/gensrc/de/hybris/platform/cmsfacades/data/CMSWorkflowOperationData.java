/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:28 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.data;

import de.hybris.platform.cmsfacades.enums.CMSWorkflowOperation;


import java.util.Objects;
public  class CMSWorkflowOperationData extends CMSCreateVersionData 
{

 

	/** <i>Generated property</i> for <code>CMSWorkflowOperationData.operation</code> property defined at extension <code>cmsfacades</code>. */
		
	private CMSWorkflowOperation operation;

	/** <i>Generated property</i> for <code>CMSWorkflowOperationData.actionCode</code> property defined at extension <code>cmsfacades</code>. */
		
	private String actionCode;

	/** <i>Generated property</i> for <code>CMSWorkflowOperationData.decisionCode</code> property defined at extension <code>cmsfacades</code>. */
		
	private String decisionCode;

	/** <i>Generated property</i> for <code>CMSWorkflowOperationData.comment</code> property defined at extension <code>cmsfacades</code>. */
		
	private String comment;
	
	public CMSWorkflowOperationData()
	{
		// default constructor
	}
	
	public void setOperation(final CMSWorkflowOperation operation)
	{
		this.operation = operation;
	}

	public CMSWorkflowOperation getOperation() 
	{
		return operation;
	}
	
	public void setActionCode(final String actionCode)
	{
		this.actionCode = actionCode;
	}

	public String getActionCode() 
	{
		return actionCode;
	}
	
	public void setDecisionCode(final String decisionCode)
	{
		this.decisionCode = decisionCode;
	}

	public String getDecisionCode() 
	{
		return decisionCode;
	}
	
	public void setComment(final String comment)
	{
		this.comment = comment;
	}

	public String getComment() 
	{
		return comment;
	}
	

}