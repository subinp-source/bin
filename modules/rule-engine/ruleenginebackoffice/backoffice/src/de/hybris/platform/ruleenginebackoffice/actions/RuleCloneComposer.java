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

import de.hybris.platform.ruleenginebackoffice.constants.RuleEngineBackofficeConstants.NotificationSource.Clone;
import de.hybris.platform.ruleenginebackoffice.constants.RuleEngineBackofficeConstants.NotificationSource.Clone.EventType;
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;
import de.hybris.platform.ruleengineservices.model.SourceRuleModel;
import de.hybris.platform.ruleengineservices.rule.services.RuleService;

import java.util.function.Predicate;

import javax.annotation.Resource;

import org.zkoss.zk.ui.event.CreateEvent;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.google.common.collect.Lists;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent.Level;
import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.core.events.CockpitEventQueue;
import com.hybris.cockpitng.core.events.impl.DefaultCockpitEvent;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectCRUDHandler;
import com.hybris.cockpitng.util.ViewAnnotationAwareComposer;
import com.hybris.cockpitng.util.notifications.NotificationService;


/**
 * RuleCloneComposer is responsible for handling the clone rules action.
 */
public class RuleCloneComposer extends ViewAnnotationAwareComposer
{
	private static final String CLONING_FAILED = "rule.clone.failed";
	private static final String INVALID_CODE = "rule.clone.invalid.code";
	private static final String SOURCE_RULES_NAVIGATION_NODE = "SourceRule";

	private transient InteractiveAction interactiveAction;
	private SourceRuleModel ruleToClone;
	private Window window;
	private transient ActionContext<AbstractRuleModel> context;
	private Textbox codeInput;
	private Button okBtn;

	@Resource(name = "ruleCodeValidator")
	private transient Predicate<String> ruleCodeValidator;

	@Resource
	private transient RuleService ruleService;

	@Resource(name = "cockpitEventQueue")
	private transient CockpitEventQueue eventQueue;

	@Resource
	private transient NotificationService notificationService;

	@ViewEvent(eventName = Events.ON_CREATE)
	public void onCreate(@SuppressWarnings(
	{ "unused", "squid:S1172" }) final CreateEvent event)
	{
		getCodeInput().setValue(ruleToClone.getCode());
	}

	@ViewEvent(componentID = "okBtn", eventName = Events.ON_CLICK)
	public void perform()
	{
		final String newCode = getCodeInput().getValue().trim();
		getCodeInput().setValue(newCode);
		if (ruleCodeValidator.test(newCode))
		{
			try
			{
				final AbstractRuleModel result = ruleService.cloneRule(newCode, ruleToClone);
				updateListView(result);
				replaceEditorElement(result);
				reportSuccess();
			}
			catch (final Exception exception)
			{
				reportError(exception);
			}
			finally
			{
				getWindow().detach();
			}
		}
		else
		{
			getInteractiveAction().showAlertDialog(context, CLONING_FAILED, INVALID_CODE);
		}
	}

	/**
	 * Updates editorArea widget content
	 *
	 * @param newItem
	 */
	protected void replaceEditorElement(final AbstractRuleModel newItem)
	{
		getInteractiveAction().sendOutputDataToSocket("newObject", newItem);
	}

	/**
	 * Deselects currently selected item on the widget list
	 */
	protected void deselectClonedSourceRule()
	{
		getInteractiveAction().sendOutputDataToSocket("deselectItems", Lists.newArrayList(ruleToClone));
	}


	/**
	 * Updates list view
	 *
	 * @param newlyCreatedItem
	 */
	protected void updateListView(final AbstractRuleModel newlyCreatedItem)
	{
		deselectClonedSourceRule();
		forceListViewReload(newlyCreatedItem);
	}

	/**
	 * Forces list view refresh due to a new item creation
	 *
	 * @param newlyCreatedItem
	 */
	protected void forceListViewReload(final AbstractRuleModel newlyCreatedItem)
	{
		final DefaultCockpitEvent event = new DefaultCockpitEvent(ObjectCRUDHandler.OBJECTS_UPDATED_EVENT,
				singletonList(newlyCreatedItem), null);
		event.getContext().put("updatedObjectIsNew", Boolean.TRUE);
		getEventQueue().publishEvent(event);
	}

	@ViewEvent(componentID = "cancelBtn", eventName = Events.ON_CLICK)
	public void closeDialog()
	{
		getWindow().detach();
	}

	protected void reportSuccess()
	{
		getNotificationService().notifyUser(Clone.MESSAGE_SOURCE, EventType.CLONE, Level.SUCCESS, ruleToClone.getCode());
	}

	protected void reportError(final Exception exception)
	{
		getNotificationService().notifyUser(Clone.MESSAGE_SOURCE, EventType.CLONE, Level.FAILURE, exception);
	}

	protected InteractiveAction getInteractiveAction()
	{
		return interactiveAction;
	}

	protected Textbox getCodeInput()
	{
		return codeInput;
	}

	protected Window getWindow()
	{
		return window;
	}

	protected Button getOkBtn()
	{
		return okBtn;
	}

	protected String getNavigationNode()
	{
		return SOURCE_RULES_NAVIGATION_NODE;
	}

	protected CockpitEventQueue getEventQueue()
	{
		return eventQueue;
	}

	public void setEventQueue(final CockpitEventQueue eventQueue)
	{
		this.eventQueue = eventQueue;
	}

	protected NotificationService getNotificationService()
	{
		return notificationService;
	}
}
