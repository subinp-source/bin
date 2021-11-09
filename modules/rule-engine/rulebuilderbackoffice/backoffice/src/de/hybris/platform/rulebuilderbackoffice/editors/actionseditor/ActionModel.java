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
package de.hybris.platform.rulebuilderbackoffice.editors.actionseditor;

import de.hybris.platform.rulebuilderbackoffice.editors.ParameterModel;
import de.hybris.platform.rulebuilderbackoffice.editors.RuleItemModel;
import de.hybris.platform.ruleengineservices.rule.data.RuleActionDefinitionData;

import java.util.LinkedHashMap;
import java.util.Map;


public class ActionModel implements RuleItemModel
{
	private static final long serialVersionUID = 1L;

	private RuleActionDefinitionData actionDefinition;
	private Map<String, ParameterModel> parameters;
	private Boolean expanded;

	public ActionModel()
	{
		parameters = new LinkedHashMap<>();
		expanded = Boolean.FALSE;
	}

	public RuleActionDefinitionData getActionDefinition()
	{
		return actionDefinition;
	}

	public void setActionDefinition(final RuleActionDefinitionData actionDefinition)
	{
		this.actionDefinition = actionDefinition;
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
