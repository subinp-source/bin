/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:33:00 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.ruleengine;

import java.io.Serializable;
import de.hybris.platform.ruleengineservices.rao.RuleEngineResultRAO;
import java.util.Set;


import java.util.Objects;
public  class RuleEvaluationResult  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>RuleEvaluationResult.evaluationFailed</code> property defined at extension <code>ruleengine</code>. */
		
	private boolean evaluationFailed;

	/** <i>Generated property</i> for <code>RuleEvaluationResult.facts</code> property defined at extension <code>ruleengine</code>. */
		
	private Set<Object> facts;

	/** <i>Generated property</i> for <code>RuleEvaluationResult.errorMessage</code> property defined at extension <code>ruleengine</code>. */
		
	private String errorMessage;

	/** contains the rule engine's native result object (e.g. for Drools an instance of ExecutionResults)<br/><br/><i>Generated property</i> for <code>RuleEvaluationResult.executionResult</code> property defined at extension <code>ruleengine</code>. */
		
	private Object executionResult;

	/** <i>Generated property</i> for <code>RuleEvaluationResult.result</code> property defined at extension <code>ruleengineservices</code>. */
		
	private RuleEngineResultRAO result;
	
	public RuleEvaluationResult()
	{
		// default constructor
	}
	
	public void setEvaluationFailed(final boolean evaluationFailed)
	{
		this.evaluationFailed = evaluationFailed;
	}

	public boolean isEvaluationFailed() 
	{
		return evaluationFailed;
	}
	
	public void setFacts(final Set<Object> facts)
	{
		this.facts = facts;
	}

	public Set<Object> getFacts() 
	{
		return facts;
	}
	
	public void setErrorMessage(final String errorMessage)
	{
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() 
	{
		return errorMessage;
	}
	
	public void setExecutionResult(final Object executionResult)
	{
		this.executionResult = executionResult;
	}

	public Object getExecutionResult() 
	{
		return executionResult;
	}
	
	public void setResult(final RuleEngineResultRAO result)
	{
		this.result = result;
	}

	public RuleEngineResultRAO getResult() 
	{
		return result;
	}
	

}