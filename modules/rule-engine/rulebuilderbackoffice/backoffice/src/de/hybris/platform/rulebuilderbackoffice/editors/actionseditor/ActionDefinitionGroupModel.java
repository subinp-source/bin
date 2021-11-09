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

import de.hybris.platform.ruleengineservices.rule.data.RuleActionDefinitionCategoryData;

import java.io.Serializable;
import java.util.Objects;

import org.apache.commons.lang.math.NumberUtils;


public class ActionDefinitionGroupModel implements Comparable<ActionDefinitionGroupModel>, Serializable
{
	private static final long serialVersionUID = 1L;

	private RuleActionDefinitionCategoryData category;

	public RuleActionDefinitionCategoryData getCategory()
	{
		return category;
	}

	public void setCategory(final RuleActionDefinitionCategoryData category)
	{
		this.category = category;
	}

	@Override
	public int compareTo(final ActionDefinitionGroupModel other)
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

		if (!(obj.getClass().equals(ActionDefinitionGroupModel.class)))
		{
			return false;
		}

		return Objects.equals(category.getId(), ((ActionDefinitionGroupModel) obj).getCategory().getId());
	}
}
