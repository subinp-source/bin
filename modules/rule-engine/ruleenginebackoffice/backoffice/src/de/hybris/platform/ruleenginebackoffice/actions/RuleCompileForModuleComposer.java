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

import de.hybris.platform.ruleengine.enums.RuleType;
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;
import de.hybris.platform.ruleengineservices.model.SourceRuleModel;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.hybris.cockpitng.core.events.impl.DefaultCockpitEvent;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectCRUDHandler;


/**
 * RuleCompileForModuleComposer is responsible for handling of Rule Compile Action dialog box.
 */
public class RuleCompileForModuleComposer extends AbstractRuleCompileForModuleComposer<SourceRuleModel>
{
	@Override
	protected void onSuccess(final String moduleName, final String previousModuleVersion, final String moduleVersion)
	{
		final AbstractRuleModel ruleModel = getRuleToCompile();
		final List<AbstractRuleModel> affectedRules = getAffectedRules(singletonList(ruleModel));

		if (getEventQueue() != null)
		{
			final DefaultCockpitEvent event = new DefaultCockpitEvent(ObjectCRUDHandler.OBJECTS_UPDATED_EVENT, affectedRules, null);
			getEventQueue().publishEvent(event);
		}

		final List<AbstractRuleModel> deselectItems = new ArrayList(affectedRules);
		deselectItems.remove(ruleModel);

		getInteractiveAction().sendOutputDataToSocket("deselectItems", deselectItems);
		getInteractiveAction().sendOutputDataToSocket("selectItem", ruleModel);
	}

	@Override
	protected void doCompileAndPublishRules(final String moduleName, final List<SourceRuleModel> sourceRules)
	{
		super.doCompileAndPublishRules(moduleName, sourceRules);
		getInteractiveAction().sendOutputDataToSocket("selectItem", sourceRules.get(0));
	}

	@Override
	protected RuleType getRuleType()
	{
		return getRuleService().getEngineRuleTypeForRuleType(getContext().getData().getClass());
	}

	protected AbstractRuleModel getRuleToCompile()
	{
		if (CollectionUtils.isEmpty(getRulesToProcess()))
		{
			throw new IllegalStateException(
					"RuleCompileEnv action isn't initialized properly. No rule was selected for compilation.");
		}
		return getRulesToProcess().get(0);
	}

}
