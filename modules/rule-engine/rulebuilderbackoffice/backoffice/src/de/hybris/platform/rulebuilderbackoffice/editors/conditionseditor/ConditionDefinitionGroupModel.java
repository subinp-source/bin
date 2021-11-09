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

import de.hybris.platform.ruleengineservices.rule.data.RuleConditionDefinitionCategoryData;

import java.io.Serializable;
import java.util.Objects;

import org.apache.commons.lang.math.NumberUtils;


public class ConditionDefinitionGroupModel implements Comparable<ConditionDefinitionGroupModel>, Serializable
{
	private static final long serialVersionUID = 1L;

	private RuleConditionDefinitionCategoryData category;

	public RuleConditionDefinitionCategoryData getCategory()
	{
		return category;
	}

	public void setCategory(final RuleConditionDefinitionCategoryData category)
	{
		this.category = category;
	}

	@Override
	public int compareTo(final ConditionDefinitionGroupModel other)
	{
		final Integer thisPriority = this.getCategory().getPriority() == null ? NumberUtils.INTEGER_ZERO
				: this.getCategory().getPriority();
		final Integer otherPriority = other.getCategory().getPriority() == null ? NumberUtils.INTEGER_ZERO
				: other.getCategory().getPriority();

		return otherPriority.compareTo(thisPriority);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(category.getId());
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

		if (!(obj.getClass().equals(ConditionDefinitionGroupModel.class)))
		{
			return false;
		}

		return Objects.equals(category.getId(), ((ConditionDefinitionGroupModel) obj).getCategory().getId());
	}
}
