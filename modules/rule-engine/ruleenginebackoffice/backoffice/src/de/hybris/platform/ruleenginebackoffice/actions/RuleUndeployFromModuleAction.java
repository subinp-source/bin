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

import static java.util.Collections.singletonList;
import static java.util.Objects.nonNull;
import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

import de.hybris.platform.ruleengine.model.DroolsRuleModel;
import de.hybris.platform.ruleengine.util.EngineRulesRepository;
import de.hybris.platform.ruleengine.util.RuleMappings;
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;
import de.hybris.platform.ruleengineservices.model.SourceRuleModel;

import com.hybris.cockpitng.actions.ActionContext;

import java.util.List;

import javax.annotation.Resource;


/**
 * Action to undeploy a single rule for a user-specified environment.
 */
public class RuleUndeployFromModuleAction extends AbstractRuleProcessingForModuleAction<AbstractRuleModel, Object>
{
	private static final String DEFAULT_DIALOG_TEMPLATE = "/ruleundeployfrommodule.zul";
	private static final String TITLE_RULECOMPILEACTION = "title.ruleundeployfrommoduleaction";

	@Resource
	private EngineRulesRepository engineRulesRepository;

	@Override
	protected String getDialogTemplate(final ActionContext<AbstractRuleModel> context)
	{
		return DEFAULT_DIALOG_TEMPLATE;
	}

	@Override
	protected String getDialogTitle(final ActionContext<AbstractRuleModel> context)
	{
		return TITLE_RULECOMPILEACTION;
	}

	@Override
	protected List<AbstractRuleModel> getRulesToProcess(final ActionContext<AbstractRuleModel> context)
	{
		return singletonList(context.getData());
	}

	@Override
	public boolean canPerform(final ActionContext context)
	{
		boolean canUndeploy = false;
		if (context.getData() instanceof AbstractRuleModel)
		{
			final AbstractRuleModel data = (AbstractRuleModel) context.getData();
			canUndeploy = nonNull(data);
			if (canUndeploy && data instanceof SourceRuleModel)
			{
				final SourceRuleModel sourceRule = (SourceRuleModel) data;
				canUndeploy = isNotEmpty(sourceRule.getEngineRules()) && sourceRule.getEngineRules().stream()
						.anyMatch(r -> engineRulesRepository.checkEngineRuleDeployedForModule(r, RuleMappings.moduleName((DroolsRuleModel) r)));
			}
		}
		return canUndeploy;
	}

}
