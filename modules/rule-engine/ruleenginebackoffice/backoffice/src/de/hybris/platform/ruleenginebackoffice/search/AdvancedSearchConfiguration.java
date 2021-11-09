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
package de.hybris.platform.ruleenginebackoffice.search;

import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import org.springframework.beans.factory.annotation.Required;

import java.util.Collection;
import java.util.Map;


/**
 * Rule engine specific configuration for the advanced search widget
 */
public class AdvancedSearchConfiguration
{
	private Collection<String> applicableTypeCodes;

	private Map<ValueComparisonOperator, ValueComparisonOperator> operatorReplacements;

	public Collection<String> getApplicableTypeCodes()
	{
		return applicableTypeCodes;
	}

	@Required
	public void setApplicableTypeCodes(final Collection<String> applicableTypeCodes)
	{
		this.applicableTypeCodes = applicableTypeCodes;
	}

	public Map<ValueComparisonOperator, ValueComparisonOperator> getOperatorReplacements()
	{
		return operatorReplacements;
	}

	@Required
	public void setOperatorReplacements(final Map<ValueComparisonOperator, ValueComparisonOperator> operatorReplacements)
	{
		this.operatorReplacements = operatorReplacements;
	}
}
