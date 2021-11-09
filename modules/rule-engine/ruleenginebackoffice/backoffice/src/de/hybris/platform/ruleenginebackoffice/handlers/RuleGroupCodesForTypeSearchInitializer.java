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
package de.hybris.platform.ruleenginebackoffice.handlers;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Objects.nonNull;
import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

import de.hybris.platform.ruleengine.enums.RuleType;
import de.hybris.platform.ruleengineservices.model.RuleGroupModel;
import de.hybris.platform.ruleengineservices.rule.dao.RuleGroupDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Required;

import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.backoffice.widgets.advancedsearch.AdvancedSearchInitializer;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchData;
import com.hybris.backoffice.widgets.advancedsearch.impl.SearchConditionData;
import com.hybris.backoffice.widgets.advancedsearch.impl.SearchConditionDataList;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.advancedsearch.FieldType;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;


/**
 * Advanced search initializer for promotion rule groups
 */
public class RuleGroupCodesForTypeSearchInitializer implements AdvancedSearchInitializer
{
	private static final String CODE = "code";

	private RuleGroupDao ruleGroupDao;
	private RuleType ruleType;

	@Override
	public void addSearchDataConditions(final AdvancedSearchData searchData, final Optional<NavigationNode> navigationNode)
	{
		if (nonNull(searchData))
		{
			removeExistingGroupCodeCondition(searchData);
			searchData.addConditionList(ValueComparisonOperator.OR, createGroupCodeSearchConditions());
		}
	}

	protected void removeExistingGroupCodeCondition(final AdvancedSearchData searchData)
	{
		final List<SearchConditionData> conditions = searchData.getConditions(CODE);
		if (isNotEmpty(conditions))
		{
			conditions.clear();
		}
	}

	protected List<SearchConditionData> createGroupCodeSearchConditions()
	{
		return newArrayList(createGroupCodeConditionsList());
	}

	protected SearchConditionDataList createGroupCodeConditionsList()
	{
		final FieldType groupCode = createCodeField();
		final List groupCodeConditionsList = new ArrayList();
		final List<RuleGroupModel> ruleGroups = new ArrayList(ruleGroupDao.findRuleGroupOfType(ruleType));
		ruleGroups.addAll(ruleGroupDao.findAllNotReferredRuleGroups());
		ruleGroups.stream().forEach(rg -> groupCodeConditionsList.add(createCondition(groupCode, rg.getCode())));
		return SearchConditionDataList.or(groupCodeConditionsList);
	}

	protected FieldType createCodeField()
	{
		final FieldType groupCode = new FieldType();
		groupCode.setDisabled(Boolean.FALSE);
		groupCode.setSelected(Boolean.TRUE);
		groupCode.setName(CODE);
		return groupCode;
	}

	protected SearchConditionData createCondition(final FieldType codeFieldType, final String code)
	{
		return new SearchConditionData(codeFieldType, code, ValueComparisonOperator.EQUALS);
	}

	public RuleGroupDao getRuleGroupDao()
	{
		return ruleGroupDao;
	}

	@Required
	public void setRuleGroupDao(final RuleGroupDao ruleGroupDao)
	{
		this.ruleGroupDao = ruleGroupDao;
	}

	public RuleType getRuleType()
	{
		return ruleType;
	}

	@Required
	public void setRuleType(final RuleType ruleType)
	{
		this.ruleType = ruleType;
	}
}
