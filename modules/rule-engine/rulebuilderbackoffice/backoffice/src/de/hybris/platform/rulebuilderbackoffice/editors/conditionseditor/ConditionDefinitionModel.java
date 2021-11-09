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

import de.hybris.platform.ruleengineservices.rule.data.RuleConditionDefinitionData;

import java.io.Serializable;

import org.apache.commons.lang.math.NumberUtils;


import static java.util.Objects.hash;


public class ConditionDefinitionModel implements Comparable<ConditionDefinitionModel>, Serializable
{
	private static final long serialVersionUID = 1L;

	private RuleConditionDefinitionData conditionDefinition;

	public RuleConditionDefinitionData getConditionDefinition()
	{
		return conditionDefinition;
	}

	public void setConditionDefinition(final RuleConditionDefinitionData conditionDefinition)
	{
		this.conditionDefinition = conditionDefinition;
	}

	@Override
	public int compareTo(final ConditionDefinitionModel other)
	{
		final Integer thisPriority = this.getConditionDefinition().getPriority() == null ? NumberUtils.INTEGER_ZERO
				: this.getConditionDefinition().getPriority();
		final Integer otherPriority = other.getConditionDefinition().getPriority() == null ? NumberUtils.INTEGER_ZERO
				: other.getConditionDefinition().getPriority();

		return otherPriority.compareTo(thisPriority);
	}

	@Override
	public int hashCode()
	{
		return hash(conditionDefinition);
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
		final ConditionDefinitionModel other = (ConditionDefinitionModel) obj;
		if (conditionDefinition == null)
		{
			if (other.conditionDefinition != null)
			{
				return false;
			}
		}
		else if (!conditionDefinition.equals(other.conditionDefinition))
		{
			return false;
		}
		return true;
	}


}
