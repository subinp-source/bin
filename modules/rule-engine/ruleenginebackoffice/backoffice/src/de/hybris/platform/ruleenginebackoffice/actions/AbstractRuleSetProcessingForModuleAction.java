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
package de.hybris.platform.ruleenginebackoffice.actions;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import de.hybris.platform.ruleengine.enums.RuleType;
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;
import de.hybris.platform.ruleengineservices.rule.services.RuleService;
import org.apache.commons.collections.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections.CollectionUtils.isNotEmpty;


/**
 * AbstractRuleSetProcessingForModuleAction is the abstract class for the common functionality of group rule processing
 * actions.
 */
public abstract class AbstractRuleSetProcessingForModuleAction<O>
		extends AbstractRuleProcessingForModuleAction<Set<AbstractRuleModel>, O>
{
	@Resource
	private RuleService ruleService;

	protected abstract String getDifferentRuleTypesAlertTitle();

	protected abstract String getDifferentRuleTypesAlertMessage();

	@Override
	public boolean canPerform(final ActionContext<Set<AbstractRuleModel>> context)
	{
		return isNotEmpty(getSelectedRules(context));
	}

	@Override
	public ActionResult<O> perform(final ActionContext<Set<AbstractRuleModel>> context)
	{
		final List<AbstractRuleModel> rulesToProcess = getRulesToProcess(context);
		if (differentRuleTypes(rulesToProcess))
		{
			showAlertDialog(context, getDifferentRuleTypesAlertTitle(), getDifferentRuleTypesAlertMessage());
			return new ActionResult<>(ActionResult.SUCCESS);
		}
		return super.perform(context);
	}

	protected boolean differentRuleTypes(final List<AbstractRuleModel> rulesToCompile)
	{
		if (CollectionUtils.isNotEmpty(rulesToCompile))
		{
			final RuleType firstEntryRuleType = getRuleType(rulesToCompile.get(0));
			return rulesToCompile.stream().anyMatch(rule -> !getRuleType(rule).equals(firstEntryRuleType));
		}
		return false;
	}

	protected RuleType getRuleType(final AbstractRuleModel rule)
	{
		return getRuleService().getEngineRuleTypeForRuleType(rule.getClass());
	}

	/**
	 * retrieves the (non-empty) list of selected rules from the UI context (used inside
	 * {@link #getRulesToProcess(ActionContext)})
	 *
	 * @return the selected (non-empty) rules
	 */
	protected <T extends AbstractRuleModel> List<T> getSelectedRules(final ActionContext<Set<AbstractRuleModel>> context)
	{
		Set<AbstractRuleModel> selectedRules = context.getData();
		if (isNotEmpty(selectedRules))
		{
			selectedRules = filterByType(selectedRules);
			return (List<T>) selectedRules.stream().filter(filterSelectedRules().and(filterSelectedRulesByStatus()))
					.collect(toList());
		}
		return emptyList();
	}

	protected Set<AbstractRuleModel> filterByType(final Set<AbstractRuleModel> rules)
	{
		final Set<AbstractRuleModel> result = new HashSet<>();
		for (final Object entry : rules)
		{
			if (entry instanceof AbstractRuleModel)
			{
				result.add((AbstractRuleModel) entry);
			}
		}
		return result;
	}

	/**
	 * returns a filter (used inside of {@link #getSelectedRules(ActionContext)})
	 */
	protected Predicate<AbstractRuleModel> filterSelectedRules()
	{
		return r -> true;
	}

	/**
	 * returns a filter (used inside of {@link #getSelectedRules(ActionContext)}) that is responsible to filter out rules
	 * based on their status
	 */
	protected Predicate<AbstractRuleModel> filterSelectedRulesByStatus()
	{
		return r -> true;
	}

	protected RuleService getRuleService()
	{
		return ruleService;
	}
}
