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

import de.hybris.platform.ruleenginebackoffice.constants.RuleEngineBackofficeConstants.NotificationSource.CreateFromTemplate;
import de.hybris.platform.ruleenginebackoffice.constants.RuleEngineBackofficeConstants.NotificationSource.CreateFromTemplate.EventType;
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;
import de.hybris.platform.ruleengineservices.model.AbstractRuleTemplateModel;
import de.hybris.platform.ruleengineservices.rule.services.RuleService;

import java.util.function.Predicate;

import javax.annotation.Resource;

import org.zkoss.zk.ui.event.CreateEvent;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.hybris.backoffice.navigation.TreeNodeSelector;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent.Level;
import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.core.events.CockpitEventQueue;
import com.hybris.cockpitng.util.ViewAnnotationAwareComposer;
import com.hybris.cockpitng.util.notifications.NotificationService;


/**
 * RuleCreateFromTemplateComposer is responsible for handling the create rule from template action.
 */
public class RuleCreateFromTemplateComposer extends ViewAnnotationAwareComposer
{
	private static final String INVALID_CODE = "rule.createfromtemplate.invalid.code";
	private static final String CREATION_FAILED = "rule.createfromtemplate.failed";
	private static final String SOURCE_RULES_NAVIGATION_NODE = "hmc_sourcerules";

	private transient InteractiveAction interactiveAction;
	private AbstractRuleTemplateModel ruleTemplate;
	private Window window;
	private transient ActionContext<AbstractRuleTemplateModel> context;
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
	public void onCreate(@SuppressWarnings("unused") final CreateEvent event)
	{
		getCodeInput().setValue(ruleTemplate.getCode());
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
				final AbstractRuleModel result = ruleService.createRuleFromTemplate(newCode, ruleTemplate);
				navigateToTreeNode();
				reportSuccess(result);
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
			getInteractiveAction().showAlertDialog(context, CREATION_FAILED, INVALID_CODE);
		}
	}

	protected void navigateToTreeNode()
	{
		getInteractiveAction().sendOutputDataToSocket("nodeToNavigate", new TreeNodeSelector(SOURCE_RULES_NAVIGATION_NODE, true));
	}

	@ViewEvent(componentID = "cancelBtn", eventName = Events.ON_CLICK)
	public void closeDialog()
	{
		getWindow().detach();
	}

	protected void reportSuccess(final AbstractRuleModel result)
	{
		getNotificationService().notifyUser(CreateFromTemplate.MESSAGE_SOURCE, EventType.CREATE, Level.SUCCESS, result.getCode());
		getInteractiveAction().sendOutputDataToSocket("newObject", result);
	}

	protected void reportError(final Object exception)
	{
		getNotificationService().notifyUser(CreateFromTemplate.MESSAGE_SOURCE, EventType.CREATE, Level.FAILURE, exception);
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
