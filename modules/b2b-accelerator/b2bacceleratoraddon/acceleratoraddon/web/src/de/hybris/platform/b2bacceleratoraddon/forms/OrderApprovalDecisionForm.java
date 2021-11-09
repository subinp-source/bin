/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bacceleratoraddon.forms;

/**
 * Pojo for 'order approval decision' form.
 */
public class OrderApprovalDecisionForm
{
	private String workFlowActionCode;
	private String approverSelectedDecision;
	private String comments;

	public String getWorkFlowActionCode()
	{
		return workFlowActionCode;
	}

	public void setWorkFlowActionCode(final String workFlowActionCode)
	{
		this.workFlowActionCode = workFlowActionCode;
	}

	public String getApproverSelectedDecision()
	{
		return approverSelectedDecision;
	}

	public void setApproverSelectedDecision(final String approverSelectedDecision)
	{
		this.approverSelectedDecision = approverSelectedDecision;
	}

	public String getComments()
	{
		return comments;
	}

	public void setComments(final String comments)
	{
		this.comments = comments;
	}
}
