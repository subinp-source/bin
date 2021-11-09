/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:27 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.ruleengineservices.rule.data;

import java.io.Serializable;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterDefinitionData;
import java.util.Map;


import java.util.Objects;
public abstract  class AbstractRuleDefinitionData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>AbstractRuleDefinitionData.id</code> property defined at extension <code>ruleengineservices</code>. */
		
	private String id;

	/** <i>Generated property</i> for <code>AbstractRuleDefinitionData.name</code> property defined at extension <code>ruleengineservices</code>. */
		
	private String name;

	/** <i>Generated property</i> for <code>AbstractRuleDefinitionData.priority</code> property defined at extension <code>ruleengineservices</code>. */
		
	private Integer priority;

	/** <i>Generated property</i> for <code>AbstractRuleDefinitionData.breadcrumb</code> property defined at extension <code>ruleengineservices</code>. */
		
	private String breadcrumb;

	/** <i>Generated property</i> for <code>AbstractRuleDefinitionData.translatorId</code> property defined at extension <code>ruleengineservices</code>. */
		
	private String translatorId;

	/** <i>Generated property</i> for <code>AbstractRuleDefinitionData.translatorParameters</code> property defined at extension <code>ruleengineservices</code>. */
		
	private Map<String,String> translatorParameters;

	/** <i>Generated property</i> for <code>AbstractRuleDefinitionData.parameters</code> property defined at extension <code>ruleengineservices</code>. */
		
	private Map<String,RuleParameterDefinitionData> parameters;
	
	public AbstractRuleDefinitionData()
	{
		// default constructor
	}
	
	public void setId(final String id)
	{
		this.id = id;
	}

	public String getId() 
	{
		return id;
	}
	
	public void setName(final String name)
	{
		this.name = name;
	}

	public String getName() 
	{
		return name;
	}
	
	public void setPriority(final Integer priority)
	{
		this.priority = priority;
	}

	public Integer getPriority() 
	{
		return priority;
	}
	
	public void setBreadcrumb(final String breadcrumb)
	{
		this.breadcrumb = breadcrumb;
	}

	public String getBreadcrumb() 
	{
		return breadcrumb;
	}
	
	public void setTranslatorId(final String translatorId)
	{
		this.translatorId = translatorId;
	}

	public String getTranslatorId() 
	{
		return translatorId;
	}
	
	public void setTranslatorParameters(final Map<String,String> translatorParameters)
	{
		this.translatorParameters = translatorParameters;
	}

	public Map<String,String> getTranslatorParameters() 
	{
		return translatorParameters;
	}
	
	public void setParameters(final Map<String,RuleParameterDefinitionData> parameters)
	{
		this.parameters = parameters;
	}

	public Map<String,RuleParameterDefinitionData> getParameters() 
	{
		return parameters;
	}
	

}