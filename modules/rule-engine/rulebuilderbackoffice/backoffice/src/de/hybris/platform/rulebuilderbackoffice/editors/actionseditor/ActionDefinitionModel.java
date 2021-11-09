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

import de.hybris.platform.ruleengineservices.rule.data.RuleActionDefinitionData;

import java.io.Serializable;

import org.apache.commons.lang.math.NumberUtils;


import static java.util.Objects.hash;


public class ActionDefinitionModel implements Comparable<ActionDefinitionModel>, Serializable
{
	private static final long serialVersionUID = 1L;

	private RuleActionDefinitionData actionDefinition;

	public RuleActionDefinitionData getActionDefinition()
	{
		return actionDefinition;
	}

	public void setActionDefinition(final RuleActionDefinitionData conditionDefinition)
	{
		this.actionDefinition = conditionDefinition;
	}

	@Override
	public int compareTo(final ActionDefinitionModel other)
	{
		final Integer thisPriority = this.getActionDefinition().getPriority() == null ? NumberUtils.INTEGER_ZERO
				: this.getActionDefinition().getPriority();
		final Integer otherPriority = other.getActionDefinition().getPriority() == null ? NumberUtils.INTEGER_ZERO
				: other.getActionDefinition().getPriority();

		return otherPriority.compareTo(thisPriority);
	}

	@Override
	public int hashCode()
	{
		return hash(actionDefinition);
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		final ActionDefinitionModel other = (ActionDefinitionModel) obj;
		if (actionDefinition == null)
		{
			if (other.actionDefinition != null)
			{
				return false;
			}
		}
		else if (!actionDefinition.equals(other.actionDefinition))
		{
			return false;
		}
		return true;
	}


}
