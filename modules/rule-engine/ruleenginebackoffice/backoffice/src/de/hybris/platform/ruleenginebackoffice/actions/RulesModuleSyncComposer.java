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
import de.hybris.platform.ruleengine.dao.RulesModuleDao;
import de.hybris.platform.ruleengine.model.AbstractRulesModuleModel;
import de.hybris.platform.ruleengineservices.jobs.RuleEngineCronJobLauncher;
import de.hybris.platform.ruleengineservices.model.RuleEngineCronJobModel;
import de.hybris.platform.ruleengineservices.rule.services.RuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.event.CreateEvent;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;


/**
 * RulesModuleSyncComposer to invoke the action for rules module synchronisation
 */
public class RulesModuleSyncComposer<I> extends AbstractRuleCompilePublishComposer
{
	private static final Logger LOG = LoggerFactory.getLogger(RulesModuleSyncComposer.class);

	@Resource
	private transient RulesModuleDao rulesModuleDao;
	@Resource
	private transient RuleEngineCronJobLauncher ruleEngineCronJobLauncher;
	@Resource(name = "cockpitEventQueue")
	private transient CockpitEventQueue eventQueue;
	@Resource
	private transient RuleService ruleService;

	private transient InteractiveAction interactiveAction;
	private AbstractRulesModuleModel sourceModule;
	private Window window;
	private transient ActionContext<I> context;
	private Combobox envInput;
	private Button okBtn;
	private Vlayout ruleModuleSyncPanel;
	private Vlayout ruleModuleNoTargetPanel;

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
				final String targetModuleName = selectedComboItem.getValue();
				final AbstractRulesModuleModel selectedSourceModule = getSourceRulesModule();

				doSynchronizeModules(selectedSourceModule.getName(), targetModuleName);
			}
		}
		catch (final Exception exception)
		{
			LOG.error("Exception during rule compilation and publication.", exception);
			onException(exception);
		}
		getWindow().detach();
	}

	protected void doSynchronizeModules(final String sourceModuleName, final String targetModuleName)
	{
		final RuleEngineCronJobModel ruleEngineCronJob = getRuleEngineCronJobLauncher()
				.triggerSynchronizeModules(sourceModuleName, targetModuleName);
		onJobTriggered(ruleEngineCronJob);
		getEventQueue().publishEvent(new DefaultCockpitEvent("updateProcessForCronJob", ruleEngineCronJob.getJob().getCode(), null));
		
	}

	@Override
	protected void onSuccess(final String moduleName, final String previousModuleVersion, final String moduleVersion)
	{
		if (getEventQueue() != null)
		{
			final AbstractRulesModuleModel targetSourceRulesModule = getRulesModuleDao().findByName(moduleName);
			final DefaultCockpitEvent event = new DefaultCockpitEvent(ObjectCRUDHandler.OBJECTS_UPDATED_EVENT,
					targetSourceRulesModule, null);
			getEventQueue().publishEvent(event);
		}
	}

	/**
	 * returns the rules module to be synchronized by this action.
	 *
	 * @return the rules module to be used as a source for module synchronization
	 */
	protected AbstractRulesModuleModel getSourceRulesModule()
	{
		return sourceModule;
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
			final AbstractRulesModuleModel sourceRulesModule = getSourceRulesModule();
			final List<Comboitem> ruleModuleComboitems = getRulesModuleDao().findAll().stream()
					.filter(module -> !module.getName().equals(sourceRulesModule.getName()))
					.filter(module -> sourceRulesModule.getAllowedTargets().contains(module))
					.filter(module -> nonNull(module.getRuleType()) && module.getRuleType().equals(sourceRulesModule.getRuleType()))
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

			selectPanelToDisplay(ruleModuleComboitems);
		}
	}

	protected void selectPanelToDisplay(final List<Comboitem> ruleModuleComboitems)
	{
		if (ruleModuleComboitems.isEmpty())
		{
			getRuleModuleSyncPanel().setSclass(getRuleModuleSyncPanel().getSclass() + " hidden");
		}
		else
		{
			getRuleModuleNoTargetPanel().setSclass(getRuleModuleNoTargetPanel().getSclass() + "hidden");
		}
	}

	@ViewEvent(componentID = "cancelBtn", eventName = Events.ON_CLICK)
	public void closeDialog()
	{
		getWindow().detach();
	}

	@ViewEvent(componentID = "noTargetBtn", eventName = Events.ON_CLICK)
	public void closeNoTargetDialog()
	{
		closeDialog();
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

	protected Vlayout getRuleModuleSyncPanel()
	{
		return ruleModuleSyncPanel;
	}

	public void setRuleModuleSyncPanel(final Vlayout ruleModuleSyncPanel)
	{
		this.ruleModuleSyncPanel = ruleModuleSyncPanel;
	}

	protected Vlayout getRuleModuleNoTargetPanel()
	{
		return ruleModuleNoTargetPanel;
	}

	public void setRuleModuleNoTargetPanel(final Vlayout ruleModuleNoTargetPanel)
	{
		this.ruleModuleNoTargetPanel = ruleModuleNoTargetPanel;
	}
}
