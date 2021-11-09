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
package de.hybris.platform.ruleenginebackoffice.widgets.listeners;

import static de.hybris.platform.ruleenginebackoffice.actions.RuleModuleSwappingScopeListener.SWAPPING_ATTR;

import de.hybris.platform.ruleengine.event.RuleEngineModuleSwapCompletedEvent;
import de.hybris.platform.ruleenginebackoffice.actions.RuleModuleSwappingScopeListener.ModuleSwappingData;

import com.hybris.cockpitng.annotations.GlobalCockpitEvent;
import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;

import java.util.Objects;


public class RuleEngineEventListenerWidgetController extends DefaultWidgetController
{
	@GlobalCockpitEvent(eventName = "de.hybris.platform.ruleengine.event.RuleEngineModuleSwapCompletedEvent", scope = CockpitEvent.APPLICATION)
	public void handleSwapCompleted(final CockpitEvent event)
	{
		final String swappingId = (String) session.getAttribute(SWAPPING_ATTR);
		if (Objects.nonNull(swappingId))
		{
			ModuleSwappingData swappingData;
			final RuleEngineModuleSwapCompletedEvent ruleEngineModuleSwapCompletedEvent = (RuleEngineModuleSwapCompletedEvent) event
					.getData();
			if (ruleEngineModuleSwapCompletedEvent.isFailed())
			{
				swappingData = new ModuleSwappingData(swappingId, ruleEngineModuleSwapCompletedEvent.getFailureReason());
			}
			else
			{
				swappingData = new ModuleSwappingData(swappingId, ruleEngineModuleSwapCompletedEvent.getRulesModuleName(),
						ruleEngineModuleSwapCompletedEvent.getPreviousRulesModuleVersion(),
						ruleEngineModuleSwapCompletedEvent.getRulesModuleVersion());
			}
			session.setAttribute(SWAPPING_ATTR, swappingData);
		}

	}
}
