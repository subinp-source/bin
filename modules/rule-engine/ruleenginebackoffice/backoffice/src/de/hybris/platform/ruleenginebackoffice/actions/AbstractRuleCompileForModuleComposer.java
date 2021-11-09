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
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.core.events.CockpitEventQueue;
import com.hybris.cockpitng.core.events.impl.DefaultCockpitEvent;
import de.hybris.platform.ruleengine.dao.RulesModuleDao;
import de.hybris.platform.ruleengine.enums.RuleType;
import de.hybris.platform.ruleengine.model.DroolsRuleModel;
import de.hybris.platform.ruleenginebackoffice.actions.RuleModuleSwappingScopeListener.ModuleSwappingData;
import de.hybris.platform.ruleengineservices.enums.RuleStatus;
import de.hybris.platform.ruleengineservices.jobs.RuleEngineCronJobLauncher;
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;
import de.hybris.platform.ruleengineservices.model.RuleEngineCronJobModel;
import de.hybris.platform.ruleengineservices.model.SourceRuleModel;
import de.hybris.platform.ruleengineservices.rule.services.RuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.event.CreateEvent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.google.common.collect.Maps.newHashMap;
import static java.util.Comparator.comparing;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;
import static org.assertj.core.util.Lists.newArrayList;


/**
 * AbstractRuleCompileForModuleComposer is the abstract class for the common functionality of the rule compile/publish
 * actions with the rule module chooser dialog.
 */
public abstract class AbstractRuleCompileForModuleComposer<I> extends AbstractRuleCompilePublishComposer
{
	private static final Logger LOG = LoggerFactory.getLogger(AbstractRuleCompileForModuleComposer.class);

	protected static final String DEFAULT_CONFIRM_PUBLISH_MESSAGE = "rule.compileandpublish.publish.confirm.message";
	protected static final String DEFAULT_MULTIPLEVERSIONS_CONFIRM_PUBLISH_MESSAGE =
			"rule.compileandpublish.publish.multipleversions.confirm.message";
	protected static final String DEFAULT_CONFIRM_PUBLISH_TITLE = "rule.compileandpublish.publish.confirm.title";
	protected static final String DEFAULT_CONFIRM_PUBLISH_OK = "rule.compileandpublish.publish.confirm.ok";
	protected static final String DEFAULT_CONFIRM_PUBLISH_CANCEL = "rule.compileandpublish.publish.confirm.cancel";
	protected static final String MESSAGEBOX_TEMPLATE_URL = "/cng/ruleengine/multiLineMessageBox.zul";

	@Resource
	private transient RulesModuleDao rulesModuleDao;

	@Resource
	private transient RuleEngineCronJobLauncher ruleEngineCronJobLauncher;

	@Resource(name = "cockpitEventQueue")
	private transient CockpitEventQueue eventQueue;

	@Resource
	private transient RuleService ruleService;

	private transient InteractiveAction interactiveAction;
	private List<SourceRuleModel> rulesToProcess;
	private Window window;
	private transient ActionContext<I> context;
	private Combobox envInput;
	private Button okBtn;

	/**
	 * called upon on click event of the corresponding OK button.
	 */
	@ViewEvent(componentID = "okBtn", eventName = Events.ON_CLICK)
	public void perform()
	{
		final Comboitem selectedComboItem = getEnvInput().getSelectedItem();
		if (nonNull(selectedComboItem))
		{
			final String moduleName = selectedComboItem.getValue();
			final List<SourceRuleModel> sourceRules = getRulesToProcess();

			final Map<String, List<SourceRuleModel>> collectVales =
					sourceRules.stream().collect(groupingBy(SourceRuleModel::getCode));

			if (collectVales.entrySet().stream().anyMatch(e -> e.getValue().size() > 1))
			{
				 final List<SourceRuleModel> maxVersionSourceRules = newArrayList(collectVales.entrySet().stream().collect(
						toMap(Map.Entry::getKey, e -> e.getValue().stream().max(comparing(AbstractRuleModel::getVersion)).get()))
						.values());

				 showConfirmationDialog(DEFAULT_MULTIPLEVERSIONS_CONFIRM_PUBLISH_MESSAGE,
						 clickEvent -> doOnConfirmationClickForMultiVersionRules(clickEvent, moduleName, maxVersionSourceRules));
			}
			else if (containsDeployedRules(moduleName, sourceRules))
			{
				showConfirmationDialog(DEFAULT_CONFIRM_PUBLISH_MESSAGE,
						clickEvent -> doOnConfirmationClickForDeployedRules(clickEvent, moduleName, sourceRules));
			}
			else
			{
				doCompileAndPublishRules(moduleName, sourceRules);
			}
		}

		getWindow().detach();
	}

	protected boolean containsDeployedRules(final String moduleName, final List<SourceRuleModel> sourceRules)
	{
		return sourceRules.stream().filter(r -> r.getStatus().equals(RuleStatus.PUBLISHED))
						.flatMap(r -> r.getEngineRules().stream())
						.filter(r -> r.getActive() && r.getCurrentVersion())
						.anyMatch(r -> ((DroolsRuleModel) r).getKieBase().getKieModule().getName().equals(moduleName));
	}

	protected void showConfirmationDialog(final String message, final EventListener<Messagebox.ClickEvent> listener)
	{
		final Map<String, String> params = newHashMap();
		params.put("width", getMessageboxWidth());
		final String origTemplate = Messagebox.getTemplate();
		try
		{
			Messagebox.setTemplate(MESSAGEBOX_TEMPLATE_URL);
			Messagebox.show(getContext().getLabel(message),
					getContext().getLabel(DEFAULT_CONFIRM_PUBLISH_TITLE), new Messagebox.Button[]
							{ Messagebox.Button.OK, Messagebox.Button.CANCEL }, new String[]
							{ getContext().getLabel(DEFAULT_CONFIRM_PUBLISH_OK),
									getContext().getLabel(DEFAULT_CONFIRM_PUBLISH_CANCEL) }, Messagebox.EXCLAMATION,
					Messagebox.Button.CANCEL, listener,
					params);
		}
		finally
		{
			Messagebox.setTemplate(origTemplate);
		}
	}

	protected String getMessageboxWidth()
	{
		return "600px";
	}

	protected void doOnConfirmationClickForMultiVersionRules(final Messagebox.ClickEvent clickEvent, final String moduleName,
			final List<SourceRuleModel> sourceRules)
	{
		if (Messagebox.ON_OK.equals(clickEvent.getName()))
		{
			if (containsDeployedRules(moduleName, sourceRules))
			{
				showConfirmationDialog(DEFAULT_CONFIRM_PUBLISH_MESSAGE,
						e -> doOnConfirmationClickForDeployedRules(e, moduleName, sourceRules));
			}
			else
			{
				doCompileAndPublishRules(moduleName, sourceRules);
			}
		}
		else if (Messagebox.ON_CANCEL.equals(clickEvent.getName()))
		{
			getWindow().detach();
		}
	}

	protected void doOnConfirmationClickForDeployedRules(final Messagebox.ClickEvent clickEvent, final String moduleName,
			final List<SourceRuleModel> sourceRules)
	{
		if (Messagebox.ON_OK.equals(clickEvent.getName()))
		{
			doCompileAndPublishRules(moduleName, sourceRules);
		}
		else if (Messagebox.ON_CANCEL.equals(clickEvent.getName()))
		{
			getWindow().detach();
		}
	}

	protected void doCompileAndPublishRules(final String moduleName, final List<SourceRuleModel> sourceRules)
	{
		if (registerModuleSwappingNotification(moduleName))
		{
			try
			{
				final RuleEngineCronJobModel ruleEngineCronJob = getRuleEngineCronJobLauncher()
						.triggerCompileAndPublish(sourceRules, moduleName, false);

				onJobTriggered(ruleEngineCronJob);
				getEventQueue()
						.publishEvent(new DefaultCockpitEvent("updateProcessForCronJob", ruleEngineCronJob.getJob().getCode(), null));
			}
			catch (final Exception exception)
			{
				LOG.error("Exception during rule compilation and publication.", exception);
				onException(exception);
			}
		}
	}

	protected boolean registerModuleSwappingNotification(final String moduleName)
	{
		final Object swappingAttr = session.getAttribute(RuleModuleSwappingScopeListener.SWAPPING_ATTR);
		if (swappingAttr instanceof ModuleSwappingData)
		{
			return false;
		}
		session.removeAttribute(RuleModuleSwappingScopeListener.SWAPPING_ATTR);

		final String swappingId = UUID.randomUUID().toString();
		final RuleModuleSwappingScopeListener swappingScopeListener = (RuleModuleSwappingScopeListener) session
				.getAttribute(RuleModuleSwappingScopeListener.MODULE_SWAPPING_SCOPE_LISTENER_ATTR);
		if (nonNull(swappingScopeListener))
		{
			swappingScopeListener.setSwappingId(swappingId);
		}
		else
		{
			final RuleModuleSwappingScopeListener scopeListener = new RuleModuleSwappingScopeListener(session, swappingId);
			session.setAttribute(RuleModuleSwappingScopeListener.MODULE_SWAPPING_SCOPE_LISTENER_ATTR, scopeListener);
			session.addScopeListener(scopeListener);
		}
		session.setAttribute(RuleModuleSwappingScopeListener.SWAPPING_ATTR, swappingId);
		return true;
	}


	/**
	 * the rule type used for (sub)selecting which environments / rule modules are displayed.
	 *
	 * @return the rule type of the selected rule(s)
	 */
	protected abstract RuleType getRuleType();

	/**
	 * returns the rule to be compiled by this action.
	 *
	 * @return the list of rules that will be compiled by this action
	 */
	protected List<SourceRuleModel> getRulesToProcess()
	{
		return rulesToProcess;
	}

	/**
	 * returns reference to the action instance.
	 *
	 * @return the action instance
	 */
	protected InteractiveAction getInteractiveAction()
	{
		return interactiveAction;
	}

	/**
	 * @param event
	 * 		not used in this call
	 */
	@ViewEvent(eventName = Events.ON_CREATE)
	public void onCreate(final CreateEvent event)
	{
		initEnvironmentOptionsCombo();
	}

	/**
	 * creates the combo boxes for the dialog box.
	 */
	protected void initEnvironmentOptionsCombo()
	{
		if (getEnvInput().getItemCount() <= 1)
		{
			final RuleType ruleType = getRuleType();
			final List<Comboitem> ruleModuleComboitems = getRulesModuleDao().findAll().stream()
					.filter(module -> nonNull(module.getRuleType()) && module.getRuleType().equals(ruleType))
					.map(abstractRulesModule ->
					{
						final Comboitem comboitem = new Comboitem(abstractRulesModule.getName());
						comboitem.setValue(abstractRulesModule.getName());
						return comboitem;
					}).collect(Collectors.toList());
			ruleModuleComboitems.forEach(comboitem -> getEnvInput().appendChild(comboitem));
			if (ruleModuleComboitems.size() == 1)
			{
				getEnvInput().setSelectedItem(ruleModuleComboitems.get(0));
				getEnvInput().setDisabled(true);
				changeEnv();
			}
		}
	}


	@ViewEvent(componentID = "cancelBtn", eventName = Events.ON_CLICK)
	public void closeDialog()
	{
		getWindow().detach();
	}

	@ViewEvent(componentID = "envInput", eventName = Events.ON_CHANGE)
	public void changeEnv()
	{
		getOkBtn().setDisabled(getEnvInput().getSelectedIndex() == 0);
	}

	protected RulesModuleDao getRulesModuleDao()
	{
		return rulesModuleDao;
	}

	protected RuleEngineCronJobLauncher getRuleEngineCronJobLauncher()
	{
		return ruleEngineCronJobLauncher;
	}

	protected CockpitEventQueue getEventQueue()
	{
		return eventQueue;
	}

	protected RuleService getRuleService()
	{
		return ruleService;
	}

	protected ActionContext<I> getContext()
	{
		return context;
	}

	protected Combobox getEnvInput()
	{
		return envInput;
	}

	protected Window getWindow()
	{
		return window;
	}

	protected Button getOkBtn()
	{
		return okBtn;
	}
}
