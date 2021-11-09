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

import static java.util.stream.Collectors.toList;

import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.ruleengine.ResultItem;
import de.hybris.platform.ruleengine.event.RuleEngineModuleSwapCompletedEvent;
import de.hybris.platform.ruleengine.event.RuleUpdatedEvent;
import de.hybris.platform.ruleenginebackoffice.constants.RuleEngineBackofficeConstants.NotificationSource;
import de.hybris.platform.ruleenginebackoffice.constants.RuleEngineBackofficeConstants.NotificationSource.EventType;
import de.hybris.platform.ruleenginebackoffice.util.ComponentUtils;
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;
import de.hybris.platform.ruleengineservices.rule.services.RuleService;

import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.zkoss.zk.ui.Component;

import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent.Level;
import com.hybris.cockpitng.annotations.GlobalCockpitEvent;
import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.util.ViewAnnotationAwareComposer;
import com.hybris.cockpitng.util.notifications.NotificationService;


/**
 * Abstract class for the rule compile/publish composers with the global event listener binding
 */
public abstract class AbstractRuleCompilePublishComposer extends ViewAnnotationAwareComposer
{
	@Resource
	private transient ComponentUtils componentUtils;

	@Resource
	private transient RuleService ruleService;

	@Resource
	private transient NotificationService notificationService;

	@Override
	public void doAfterCompose(final Component component) throws Exception
	{
		super.doAfterCompose(component);
		componentUtils.setupGlobalEventListeners(this, component);
	}

	@GlobalCockpitEvent(eventName = "de.hybris.platform.ruleengine.event.RuleEngineModuleSwapCompletedEvent", scope = CockpitEvent.APPLICATION)
	public void handleRuleEngineModuleSwapCompletedEvent(final CockpitEvent event)
	{
		final RuleEngineModuleSwapCompletedEvent swapCompletedEvent = (RuleEngineModuleSwapCompletedEvent) event.getData();

		if (swapCompletedEvent.isFailed())
		{
			onPublishingError(swapCompletedEvent.getFailureReason(), swapCompletedEvent.getResults());
		}
		else
		{
			onSuccess(swapCompletedEvent.getRulesModuleName(), swapCompletedEvent.getPreviousRulesModuleVersion(),
					swapCompletedEvent.getRulesModuleVersion());
		}

		componentUtils.removeGlobalEventListeners(_applied);
	}

	@GlobalCockpitEvent(eventName = "de.hybris.platform.ruleengine.event.RuleUpdatedEvent", scope = CockpitEvent.APPLICATION)
	public void handleRuleUpdatedEvent(final CockpitEvent event)
	{
		final RuleUpdatedEvent ruleUpdatedEvent = (RuleUpdatedEvent) event.getData();

		onRuleUpdate(ruleUpdatedEvent.getRuleCode());

		componentUtils.removeGlobalEventListeners(_applied);
	}

	/**
	 * the method invoked upon publishing success
	 *
	 * @param moduleName
	 *           name of the published module
	 * @param previousModuleVersion
	 *           previous version of the module
	 * @param moduleVersion
	 *           current version of the module
	 */
	protected abstract void onSuccess(final String moduleName, final String previousModuleVersion, final String moduleVersion);

	/**
	 * the method invoked upon publishing errors
	 *
	 * @param failureReason
	 *           failure reason message
	 * @param results
	 *           collection of publishing results
	 */
	protected void onPublishingError(final String failureReason, final Collection<ResultItem> results)
	{
		//empty
	}

	/**
	 * the method invoked upon rule update
	 *
	 * @param ruleCode
	 *           code of the updated rule
	 */
	protected void onRuleUpdate(final String ruleCode)
	{
		// empty
	}

	/**
	 * the method invoked upon exception
	 *
	 * @param exception
	 *           the exception encountered
	 */
	protected void onException(final Exception exception)
	{
		getNotificationService().notifyUser(NotificationSource.MESSAGE_SOURCE, EventType.EXCEPTION, Level.FAILURE, exception);
	}

	/**
	 * the method invoked upon job being triggered
	 *
	 * @param cronJob
	 *           the cron job to be used in notification
	 */
	protected void onJobTriggered(final CronJobModel cronJob)
	{
		getNotificationService().notifyUser(NotificationSource.MESSAGE_SOURCE, EventType.TRIGGER, Level.SUCCESS, cronJob.getCode());
	}

	/**
	 * Provides a list of all the related rules for the provided rule set. This method is intended to be used to identify
	 * all rules whose state might have been impacted by an action (such as publish/undeploy) triggered for one of the rules
	 *
	 * @param rules
	 *           collection of rules
	 * @return list of all of the rules that are in a relationship to the provided rule set
	 */
	protected <T extends AbstractRuleModel> List<T> getAffectedRules(final List<T> rules)
	{
		return rules.stream().flatMap(r -> ruleService.<T> getAllRulesForCode(r.getCode()).stream()).collect(toList());
	}

	protected NotificationService getNotificationService()
	{
		return notificationService;
	}
}
