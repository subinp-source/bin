/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:46 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.ruleengineservices.compiler;

import de.hybris.platform.ruleengineservices.compiler.AbstractRuleIrAttributeCondition;


import java.util.Objects;
public  class RuleIrAttributeRelCondition extends AbstractRuleIrAttributeCondition 
{

 

	/** <i>Generated property</i> for <code>RuleIrAttributeRelCondition.targetVariable</code> property defined at extension <code>ruleengineservices</code>. */
		
	private String targetVariable;

	/** <i>Generated property</i> for <code>RuleIrAttributeRelCondition.targetAttribute</code> property defined at extension <code>ruleengineservices</code>. */
		
	private String targetAttribute;
	
	public RuleIrAttributeRelCondition()
	{
		// default constructor
	}
	
	public void setTargetVariable(final String targetVariable)
	{
		this.targetVariable = targetVariable;
	}

	public String getTargetVariable() 
	{
		return targetVariable;
	}
	
	public void setTargetAttribute(final String targetAttribute)
	{
		this.targetAttribute = targetAttribute;
	}

	public String getTargetAttribute() 
	{
		return targetAttribute;
	}
	

}