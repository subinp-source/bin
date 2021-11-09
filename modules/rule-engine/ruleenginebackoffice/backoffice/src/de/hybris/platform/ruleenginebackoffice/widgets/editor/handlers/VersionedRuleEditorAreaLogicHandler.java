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
package de.hybris.platform.ruleenginebackoffice.widgets.editor.handlers;

import com.hybris.cockpitng.core.events.CockpitEventQueue;
import com.hybris.cockpitng.core.events.impl.DefaultCockpitEvent;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectCRUDHandler;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectSavingException;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.widgets.baseeditorarea.DefaultEditorAreaLogicHandler;
import de.hybris.platform.ruleengineservices.enums.RuleStatus;
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;
import de.hybris.platform.ruleengineservices.rule.services.RuleService;
import org.springframework.beans.factory.annotation.Required;

import static java.util.Collections.singletonList;


/**
 * The {@link VersionedRuleEditorAreaLogicHandler} enriches the
 * {@link com.hybris.cockpitng.widgets.baseeditorarea.DefaultEditorAreaController} behaviour by hooking into extension
 * point {@link #performSave(WidgetInstanceManager, Object)}}.
 *
 * When a backoffice user modifies the "frozen" ({@link RuleStatus#PUBLISHED}, or {@link RuleStatus#INACTIVE})
 * {@link AbstractRuleModel} version and hit Save button, a new version of the rule is
 * being created and the newly created {@link AbstractRuleModel} version becomes an active object in the editor widget.
 * Additionally an event that is responsible for refreshing list view widget is being published
 */
public class VersionedRuleEditorAreaLogicHandler extends DefaultEditorAreaLogicHandler
{
	private RuleService ruleService;
	private CockpitEventQueue eventQueue;

	@Override
	public Object performSave(final WidgetInstanceManager widgetInstanceManager, final Object currentObject)
			throws ObjectSavingException
	{
		final Object saved = super.performSave(widgetInstanceManager, currentObject);
		if (isEditorWidgetReloadRequired(saved))
		{
			final AbstractRuleModel currentRule = (AbstractRuleModel) saved;
			final AbstractRuleModel latestRuleVersion = getRuleService().getRuleForCode(currentRule.getCode());
			notifyNewItemCreated(latestRuleVersion);
			return latestRuleVersion;
		}
		return saved;
	}

	/**
	 *
	 * @param saved
	 *           - saved object
	 * @return true if an object in the editor widget needs to be swapped by the newly created one otherwise false
	 */
	protected boolean isEditorWidgetReloadRequired(final Object saved)
	{
		return saved instanceof AbstractRuleModel && !RuleStatus.UNPUBLISHED.equals(((AbstractRuleModel) saved).getStatus());
	}

	/**
	 * Publish an event that allows to refresh other widgets in the backoffice
	 *
	 * @param rule
	 */
	protected void notifyNewItemCreated(final AbstractRuleModel rule)
	{
		final DefaultCockpitEvent event = new DefaultCockpitEvent(ObjectCRUDHandler.OBJECTS_UPDATED_EVENT, singletonList(rule),
				null);
		event.getContext().put("updatedObjectIsNew", Boolean.TRUE);
		getEventQueue().publishEvent(event);

	}

	protected RuleService getRuleService()
	{
		return ruleService;
	}

	@Required
	public void setRuleService(final RuleService ruleService)
	{
		this.ruleService = ruleService;
	}

	protected CockpitEventQueue getEventQueue()
	{
		return eventQueue;
	}

	@Required
	public void setEventQueue(final CockpitEventQueue eventQueue)
	{
		this.eventQueue = eventQueue;
	}

}
