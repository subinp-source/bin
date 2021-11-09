/*
* ----------------------------------------------------------------
* --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
* --- Generated at 08-Nov-2021, 4:51:28 PM
* ----------------------------------------------------------------
*
* Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
*/
package de.hybris.platform.ruleengineservices.rao;

import java.io.Serializable;
import java.util.Objects;
import de.hybris.platform.ruleengineservices.rao.AbstractActionedRAO;
import de.hybris.platform.ruleengineservices.rao.OrderEntryConsumedRAO;
import java.util.Map;
import java.util.Set;

public  class AbstractRuleActionRAO  implements Serializable 
{

	/** <i>Generated property</i> for <code>AbstractRuleActionRAO.firedRuleCode</code> property defined at extension <code>ruleengineservices</code>. */
	private String firedRuleCode;
	/** <i>Generated property</i> for <code>AbstractRuleActionRAO.moduleName</code> property defined at extension <code>ruleengineservices</code>. */
	private String moduleName;
	/** <i>Generated property</i> for <code>AbstractRuleActionRAO.actionStrategyKey</code> property defined at extension <code>ruleengineservices</code>. */
	private String actionStrategyKey;
	/** <i>Generated property</i> for <code>AbstractRuleActionRAO.appliedToObject</code> property defined at extension <code>ruleengineservices</code>. */
	private AbstractActionedRAO appliedToObject;
	/** <i>Generated property</i> for <code>AbstractRuleActionRAO.consumedEntries</code> property defined at extension <code>ruleengineservices</code>. */
	private Set<OrderEntryConsumedRAO> consumedEntries;
	/** <i>Generated property</i> for <code>AbstractRuleActionRAO.metadata</code> property defined at extension <code>ruleengineservices</code>. */
	private Map<String,String> metadata;
		
	public AbstractRuleActionRAO()
	{
		// default constructor
	}
	
		public void setFiredRuleCode(final String firedRuleCode)
	{
		this.firedRuleCode = firedRuleCode;
	}
		public String getFiredRuleCode() 
	{
		return firedRuleCode;
	}
		
		public void setModuleName(final String moduleName)
	{
		this.moduleName = moduleName;
	}
		public String getModuleName() 
	{
		return moduleName;
	}
		
		public void setActionStrategyKey(final String actionStrategyKey)
	{
		this.actionStrategyKey = actionStrategyKey;
	}
		public String getActionStrategyKey() 
	{
		return actionStrategyKey;
	}
		
		public void setAppliedToObject(final AbstractActionedRAO appliedToObject)
	{
		this.appliedToObject = appliedToObject;
	}
		public AbstractActionedRAO getAppliedToObject() 
	{
		return appliedToObject;
	}
		
		public void setConsumedEntries(final Set<OrderEntryConsumedRAO> consumedEntries)
	{
		this.consumedEntries = consumedEntries;
	}
		public Set<OrderEntryConsumedRAO> getConsumedEntries() 
	{
		return consumedEntries;
	}
		
		public void setMetadata(final Map<String,String> metadata)
	{
		this.metadata = metadata;
	}
		public Map<String,String> getMetadata() 
	{
		return metadata;
	}
		
	
	}
