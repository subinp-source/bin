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
import com.hybris.cockpitng.dataaccess.facades.object.ObjectCRUDHandler;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacade;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectNotFoundException;
import de.hybris.platform.ruleengine.model.DroolsRuleModel;
import de.hybris.platform.ruleengine.util.EngineRulesRepository;
import de.hybris.platform.ruleengine.util.RuleMappings;
import de.hybris.platform.ruleengineservices.enums.RuleStatus;
import de.hybris.platform.ruleengineservices.jobs.RuleEngineCronJobLauncher;
import de.hybris.platform.ruleengineservices.model.RuleEngineCronJobModel;
import de.hybris.platform.ruleengineservices.model.SourceRuleModel;
import de.hybris.platform.ruleengineservices.rule.services.RuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.event.CreateEvent;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Window;

import javax.annotation.Resource;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static de.hybris.platform.ruleengine.util.RuleMappings.moduleName;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;


/**
 * AbstractRuleUndeployFromModuleComposer is the abstract class for the common functionality of Rule Undeploy Action
 * dialog box.
 */
public abstract class AbstractRuleUndeployFromModuleComposer<I> extends AbstractRuleCompilePublishComposer
{
	private static final Logger LOG = LoggerFactory.getLogger(AbstractRuleUndeployFromModuleComposer.class);

	@Resource
	private transient RuleEngineCronJobLauncher ruleEngineCronJobLauncher;

	@Resource(name = "cockpitEventQueue")
	private transient CockpitEventQueue eventQueue;

	@Resource
	private transient RuleService ruleService;

	@Resource
	private transient ObjectFacade objectFacade;

	@Resource
	private transient EngineRulesRepository engineRulesRepository;

	private transient InteractiveAction interactiveAction;
	private List<SourceRuleModel> rulesToProcess;
	private Window window;
	private transient ActionContext<I> context;
	private Combobox envInput;
	private Button okBtn;

	/**
	 * creates the combo boxes for the dialog box.
	 */
	protected void initEnvironmentOptionsCombo()
	{
		if (getEnvInput().getItemCount() <= 1)
		{
			final List<Comboitem> ruleModuleComboitems = getRulesToUndeploy().stream()
					.filter(r -> RuleStatus.PUBLISHED.equals(r.getStatus())).flatMap(r -> r.getEngineRules().stream())
					.filter(r -> engineRulesRepository.checkEngineRuleDeployedForModule(r, moduleName((DroolsRuleModel) r)))
					.filter(DroolsRuleModel.class::isInstance).map(DroolsRuleModel.class::cast).map(RuleMappings::moduleName)
					.distinct().map(this::createComboitem).collect(toList());

			ruleModuleComboitems.forEach(getEnvInput()::appendChild);
			if (ruleModuleComboitems.size() == 1)
			{
				getEnvInput().setSelectedItem(ruleModuleComboitems.get(0));
				getEnvInput().setDisabled(true);
				changeEnv();
			}
		}
	}

	/**
	 * the method invoked upon success
	 */
	@Override
	protected void onSuccess(final String moduleName, final String previousModuleVersion, final String moduleVersion)
	{
		if (getEventQueue() != null)
		{
			final List<SourceRuleModel> rulesToUndeploy = getRulesToUndeploy();
			final DefaultCockpitEvent event = new DefaultCockpitEvent(ObjectCRUDHandler.OBJECTS_UPDATED_EVENT, rulesToUndeploy,
					null);
			getEventQueue().publishEvent(event);
		}

		refreshWidget();
	}

	/**
	 * Takes care of refreshing widget, if required
	 */
	protected abstract void refreshWidget();

	/**
	 * called upon on click event of the corresponding OK button.
	 */
	@ViewEvent(componentID = "okBtn", eventName = Events.ON_CLICK)
	public void perform()
	{
		try
		{
			final Comboitem selectedComboItem = getEnvInput().getSelectedItem();
			if (nonNull(selectedComboItem))
			{
				final String moduleName = selectedComboItem.getValue();
				undeployRules(moduleName);
			}
		}
		catch (final Exception exception)
		{
			LOG.error("Exception during rule undeployment.", exception);
			onException(exception);
		}
		getWindow().detach();
	}


	/**
	 * Undeploys rule from a module with given name
	 *
	 * @param moduleName
	 * 		kie module name
	 */
	protected void undeployRules(final String moduleName)
	{
		final List<SourceRuleModel> rulesToUndeploy = getRulesToUndeploy();
		final RuleEngineCronJobModel ruleEngineCronJob = getRuleEngineCronJobLauncher().triggerUndeployRules(rulesToUndeploy, moduleName);
		onJobTriggered(ruleEngineCronJob);
		getEventQueue().publishEvent(new DefaultCockpitEvent("updateProcessForCronJob", ruleEngineCronJob.getJob().getCode(), null));
	}

	/**
	 * Returns the rule to be compiled by this action. The returned rule is refreshed every time the object is provided
	 * due to possibility to have state changed by the bulk publish action.
	 *
	 * @return the list of rules that will be compiled by this action
	 */
	protected List<SourceRuleModel> getRulesToUndeploy()
	{
		try
		{
			return reload(getRulesToProcess());
		}
		catch (final ObjectNotFoundException e)
		{
			LOG.error("Attempt to reload current object failed", e);
			//it shouldn't happen in real life scenario, but in case it does let it fail as
			//it's hard to recover from this scenario
			throw new IllegalArgumentException(e);
		}
	}

	protected <T> List<T> reload(final List<T> list) throws ObjectNotFoundException
	{
		final List<T> ruleToUndeploy = newArrayList();
		for (final T item : list)
		{
			ruleToUndeploy.add(getObjectFacade().reload(item));
		}
		return ruleToUndeploy;
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
	 * Creates comboitem using name as key and value
	 *
	 * @param name
	 * @return comboitem instance
	 */
	protected Comboitem createComboitem(final String name)
	{
		final Comboitem comboitem = new Comboitem(name);
		comboitem.setValue(name);
		return comboitem;
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

	protected ObjectFacade getObjectFacade()
	{
		return objectFacade;
	}

	protected List<SourceRuleModel> getRulesToProcess()
	{
		return rulesToProcess;
	}
}
