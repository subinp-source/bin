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
import de.hybris.platform.ruleengine.InitializeMode;
import java.util.Collection;
import java.util.Map;


import java.util.Objects;
/**
 * ExecutionContext is a ruleengine specific context object used during rule initialization
 */
public  class ExecutionContext  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** this map contains entries of rule code and referenced engine rule version<br/><br/><i>Generated property</i> for <code>ExecutionContext.ruleVersions</code> property defined at extension <code>ruleengine</code>. */
		
	private Map<String, Long> ruleVersions;

	/** mode of Rule Engine initialization<br/><br/><i>Generated property</i> for <code>ExecutionContext.initializeMode</code> property defined at extension <code>ruleengine</code>. */
		
	private InitializeMode initializeMode;

	/** <i>Generated property</i> for <code>ExecutionContext.modifiedRuleCodes</code> property defined at extension <code>ruleengine</code>. */
		
	private Collection<String> modifiedRuleCodes;
	
	public ExecutionContext()
	{
		// default constructor
	}
	
	public void setRuleVersions(final Map<String, Long> ruleVersions)
	{
		this.ruleVersions = ruleVersions;
	}

	public Map<String, Long> getRuleVersions() 
	{
		return ruleVersions;
	}
	
	public void setInitializeMode(final InitializeMode initializeMode)
	{
		this.initializeMode = initializeMode;
	}

	public InitializeMode getInitializeMode() 
	{
		return initializeMode;
	}
	
	public void setModifiedRuleCodes(final Collection<String> modifiedRuleCodes)
	{
		this.modifiedRuleCodes = modifiedRuleCodes;
	}

	public Collection<String> getModifiedRuleCodes() 
	{
		return modifiedRuleCodes;
	}
	

}