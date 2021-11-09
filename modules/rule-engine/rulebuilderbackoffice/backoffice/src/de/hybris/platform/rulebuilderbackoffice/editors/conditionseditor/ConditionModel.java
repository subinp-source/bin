/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.rulebuilderbackoffice.editors.conditionseditor;

import de.hybris.platform.rulebuilderbackoffice.editors.ParameterModel;
import de.hybris.platform.rulebuilderbackoffice.editors.RuleItemModel;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionDefinitionData;

import java.util.LinkedHashMap;
import java.util.Map;


public class ConditionModel implements RuleItemModel
{
	private static final long serialVersionUID = 1L;

	private RuleConditionDefinitionData conditionDefinition;
	private Map<String, ParameterModel> parameters;
	private Boolean expanded;

	public ConditionModel()
	{
		parameters = new LinkedHashMap<>();
		expanded = Boolean.FALSE;
	}

	public RuleConditionDefinitionData getConditionDefinition()
	{
		return conditionDefinition;
	}

	public void setConditionDefinition(final RuleConditionDefinitionData conditionDefinition)
	{
		this.conditionDefinition = conditionDefinition;
	}

	@Override
	public Map<String, ParameterModel> getParameters()
	{
		return parameters;
	}

	public void setParameters(final Map<String, ParameterModel> parameters)
	{
		this.parameters = parameters;
	}

	public Boolean getExpanded()
	{
		return expanded;
	}

	public void setExpanded(final Boolean expanded)
	{
		this.expanded = expanded;
	}
}
