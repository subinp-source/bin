/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:45 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.ordermanagementfacades.workflow.data;

import java.io.Serializable;
import de.hybris.platform.ordermanagementfacades.workflow.data.WorkflowActionData;
import java.util.List;


import java.util.Objects;
public  class WorkflowData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>WorkflowData.code</code> property defined at extension <code>ordermanagementfacades</code>. */
		
	private String code;

	/** <i>Generated property</i> for <code>WorkflowData.name</code> property defined at extension <code>ordermanagementfacades</code>. */
		
	private String name;

	/** <i>Generated property</i> for <code>WorkflowData.description</code> property defined at extension <code>ordermanagementfacades</code>. */
		
	private String description;

	/** <i>Generated property</i> for <code>WorkflowData.actions</code> property defined at extension <code>ordermanagementfacades</code>. */
		
	private List<WorkflowActionData> actions;
	
	public WorkflowData()
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
	
	public void setDescription(final String description)
	{
		this.description = description;
	}

	public String getDescription() 
	{
		return description;
	}
	
	public void setActions(final List<WorkflowActionData> actions)
	{
		this.actions = actions;
	}

	public List<WorkflowActionData> getActions() 
	{
		return actions;
	}
	

}