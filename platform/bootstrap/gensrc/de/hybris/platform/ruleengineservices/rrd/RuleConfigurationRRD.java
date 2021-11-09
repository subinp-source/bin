/*
* ----------------------------------------------------------------
* --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
* --- Generated at 08-Nov-2021, 4:51:28 PM
* ----------------------------------------------------------------
*
* Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
*/
package de.hybris.platform.ruleengineservices.rrd;

import java.io.Serializable;
import java.util.Objects;

/**
* Represents a Rule's configuration at rule evaluation time (gets inserted as one fact per rule, the rule being identified by the ruleCode)
*
* @deprecated not used anymore, instead rules are tracked via RuleAndRuleGroupExecutionTracker and its agenda filter
*/
@Deprecated(forRemoval = true)
public  class RuleConfigurationRRD  implements Serializable 
{

	/** <i>Generated property</i> for <code>RuleConfigurationRRD.ruleCode</code> property defined at extension <code>ruleengineservices</code>. */
	private String ruleCode;
	/** <i>Generated property</i> for <code>RuleConfigurationRRD.maxAllowedRuns</code> property defined at extension <code>ruleengineservices</code>. */
	private Integer maxAllowedRuns;
	/** <i>Generated property</i> for <code>RuleConfigurationRRD.currentRuns</code> property defined at extension <code>ruleengineservices</code>. */
	private Integer currentRuns;
	/** <i>Generated property</i> for <code>RuleConfigurationRRD.ruleGroupCode</code> property defined at extension <code>ruleengineservices</code>. */
	private String ruleGroupCode;
	/** indicates if at least one of a Rule Action executed successfully<br/><br/><i>Generated property</i> for <code>RuleConfigurationRRD.actionExecutionStarted</code> property defined at extension <code>ruleengineservices</code>. */
	private boolean actionExecutionStarted;
		
	public RuleConfigurationRRD()
	{
		// default constructor
	}
	
		public void setRuleCode(final String ruleCode)
	{
		this.ruleCode = ruleCode;
	}
		public String getRuleCode() 
	{
		return ruleCode;
	}
		
		public void setMaxAllowedRuns(final Integer maxAllowedRuns)
	{
		this.maxAllowedRuns = maxAllowedRuns;
	}
		public Integer getMaxAllowedRuns() 
	{
		return maxAllowedRuns;
	}
		
		public void setCurrentRuns(final Integer currentRuns)
	{
		this.currentRuns = currentRuns;
	}
		public Integer getCurrentRuns() 
	{
		return currentRuns;
	}
		
		public void setRuleGroupCode(final String ruleGroupCode)
	{
		this.ruleGroupCode = ruleGroupCode;
	}
		public String getRuleGroupCode() 
	{
		return ruleGroupCode;
	}
		
		public void setActionExecutionStarted(final boolean actionExecutionStarted)
	{
		this.actionExecutionStarted = actionExecutionStarted;
	}
		public boolean isActionExecutionStarted() 
	{
		return actionExecutionStarted;
	}
		
	
		@Override
	public boolean equals(final Object o)
	{
		
		if (o == null) return false;
		if (o == this) return true;

		if (getClass() != o.getClass()) return false;

		final RuleConfigurationRRD other = (RuleConfigurationRRD) o;
		return				Objects.equals(getRuleCode(), other.getRuleCode())
  ;
	}

	@Override
	public int hashCode()
	{
		int result = 1;
		Object attribute;

				attribute = ruleCode;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());
		
		return result;
	}
	}
