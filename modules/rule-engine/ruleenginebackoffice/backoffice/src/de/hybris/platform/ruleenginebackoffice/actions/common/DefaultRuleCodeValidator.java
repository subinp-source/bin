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
package de.hybris.platform.ruleenginebackoffice.actions.common;

import de.hybris.platform.ruleengineservices.rule.services.RuleService;

import java.util.Objects;
import java.util.function.Predicate;

import javax.annotation.Resource;


/**
 * The class encapsulates validation logic of a rule code to be used in rule creation/clone actions.
 *
 */
public class DefaultRuleCodeValidator implements Predicate<String>
{
	private static final int MIN_LENGTH_OF_RULE_CODE_TO_ENTER = 5;

	@Resource
	private RuleService ruleService;

	@Override
	public boolean test(final String ruleCode)
	{
		return ruleCode.length() >= MIN_LENGTH_OF_RULE_CODE_TO_ENTER && Objects.isNull(ruleService.getRuleForCode(ruleCode));
	}
}
