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
package de.hybris.platform.rulebuilderbackoffice.editors;

import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.AbstractSection;
import com.hybris.cockpitng.core.model.ModelObserver;
import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.core.util.impl.TypedSettingsMap;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.widgets.editorarea.renderer.EditorAreaRendererUtils;
import com.hybris.cockpitng.widgets.editorarea.renderer.impl.AbstractEditorAreaSectionRenderer;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerProblem;
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;
import de.hybris.platform.ruleengineservices.model.AbstractRuleTemplateModel;
import de.hybris.platform.ruleengineservices.model.SourceRuleTemplateModel;
import de.hybris.platform.ruleengineservices.rule.services.RuleService;
import de.hybris.platform.servicelayer.model.ModelService;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.impl.AbstractExecution;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static com.hybris.cockpitng.widgets.baseeditorarea.DefaultEditorAreaController.MODEL_VALUE_CHANGED;
import static de.hybris.platform.rulebuilderbackoffice.editors.AbstractEditorViewModel.BEGIN_VALUE_CHANGE_EVENT;
import static de.hybris.platform.rulebuilderbackoffice.editors.AbstractEditorViewModel.VALUE_CHANGED_EVENT;
import static java.util.Optional.ofNullable;


public abstract class AbstractEditorSectionRenderer extends AbstractEditorAreaSectionRenderer<Object>
{
	protected static final String READ_ONLY = "readOnly";
	protected static final String INITIAL_VALUE = "initialValue";
	protected static final String RULE_TYPE = "ruleType";
	protected static final String RULE_COMPILER_PROBLEMS = "ruleCompilerProblems";

	protected static final String CURRENT_OBJECT_ATTRIBUTE = "currentObject";

	protected static final String EDITOR_VIEW_MODEL_CLASS = "editorViewModelClass";

	private ModelService modelService;
	private RuleService ruleService;
	private String editorViewModelClass;

	protected abstract String getEditorId();

	protected abstract String getAttribute();

	public ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	public RuleService getRuleService()
	{
		return ruleService;
	}

	@Required
	public void setRuleService(final RuleService ruleService)
	{
		this.ruleService = ruleService;
	}

	protected String getEditorViewModelClass()
	{
		return editorViewModelClass;
	}

	@Required
	public void setEditorViewModelClass(final String editorViewModelClass)
	{
		this.editorViewModelClass = editorViewModelClass;
	}

	protected void setCurrentPageForRoot(final Component comp, final Page page)
	{
		if (comp.getParent() == null)
		{
			comp.setPage(page);
		}
		else
		{
			setCurrentPageForRoot(comp.getParent(), page);
		}
	}

	@Override
	public void render(final Component parent, final AbstractSection section, final Object model, final DataType dataType,
			final WidgetInstanceManager widgetInstanceManager)
	{
		final TypedSettingsMap widgetSettings = widgetInstanceManager.getWidgetSettings();
		supplementWidgetSettings(widgetSettings);
		final Map<Object, Object> parameters = new HashMap<>();
		fillParameters(model, widgetInstanceManager, parameters);

		final String editorZul = "/cng/editors/" + getEditorId() + ".zul";
		final Execution currentExecution = Executions.getCurrent();
		// the conditional block below is a workaround for BOX-326:
		if (parent.getPage() == null && currentExecution instanceof AbstractExecution)
		{
			final Page currentPage = ((AbstractExecution) currentExecution).getCurrentPage();
			setCurrentPageForRoot(parent, currentPage);
		}
		final Component component = currentExecution.createComponents(editorZul, parent, parameters);

		// handles begin value change
		component.addEventListener(BEGIN_VALUE_CHANGE_EVENT,
				event -> widgetInstanceManager.getModel().setValue(MODEL_VALUE_CHANGED, Boolean.FALSE));

		// handles value change
		component.addEventListener(VALUE_CHANGED_EVENT, new EventListener<Event>()
		{
			@Override
			public void onEvent(final Event event)
			{
				final Object itemModel = widgetInstanceManager.getModel().getValue(CURRENT_OBJECT_ATTRIBUTE, Object.class);
				modelService.setAttributeValue(itemModel, getAttribute(), event.getData());
				widgetInstanceManager.getModel().setValue(MODEL_VALUE_CHANGED, Boolean.TRUE);
			}
		});

		// handles cancel/refresh
		EditorAreaRendererUtils.setAfterCancelListener(widgetInstanceManager.getModel(), getAttribute(), event -> {
			parent.getChildren().remove(component);

			final WidgetModel widgetModel = widgetInstanceManager.getModel();
			final Object newModel = widgetModel.getValue(CURRENT_OBJECT_ATTRIBUTE, Object.class);
			widgetModel.remove(RULE_COMPILER_PROBLEMS);

			render(parent, section, newModel, dataType, widgetInstanceManager);
		}, false);

		// removes attributes from the widget model
		widgetInstanceManager.getModel().addObserver(CURRENT_OBJECT, new ModelObserver()
		{
			@Override
			public void modelChanged()
			{
				final WidgetModel widgetModel = widgetInstanceManager.getModel();

				final Object oldModel = model;
				final Object newModel = widgetModel.getValue(CURRENT_OBJECT_ATTRIBUTE, Object.class);

				final Function<Object, PK> toPk = m -> ((ItemModel) m).getPk();
				final Optional<PK> oldModelPk = ofNullable(oldModel).map(toPk);
				final Optional<PK> newModelPk = ofNullable(newModel).map(toPk);

				if (!oldModelPk.equals(newModelPk))
				{
					widgetModel.remove(RULE_COMPILER_PROBLEMS);
				}

				widgetModel.removeObserver(this);
			}
		});
	}

	protected void supplementWidgetSettings(final TypedSettingsMap widgetSettings)
	{
		widgetSettings.put(EDITOR_VIEW_MODEL_CLASS, getEditorViewModelClass());
	}

	protected void fillParameters(final Object model, final WidgetInstanceManager widgetInstanceManager,
			final Map<Object, Object> parameters)
	{
		final Object initialValue = modelService.getAttributeValue(model, getAttribute());
		final Class<?> ruleType;
		final Map<String, List<RuleCompilerProblem>> ruleCompilerProblems;

		if (model instanceof AbstractRuleModel)
		{
			ruleType = ((AbstractRuleModel) model).getClass();
			ruleCompilerProblems = widgetInstanceManager.getModel().getValue(RULE_COMPILER_PROBLEMS, Map.class);
		}
		else if (model instanceof AbstractRuleTemplateModel)
		{
			ruleType = ruleService.getRuleTypeFromTemplate(((SourceRuleTemplateModel) model).getClass());
			ruleCompilerProblems = Collections.emptyMap();
		}
		else
		{
			throw new EditorRuntimeException("Rule type not supported: " + model.getClass().getName());
		}

		parameters.put(INITIAL_VALUE, initialValue);
		parameters.put(RULE_TYPE, ruleType);
		parameters.put(RULE_COMPILER_PROBLEMS, ruleCompilerProblems);
		parameters.put(READ_ONLY,
				Boolean.valueOf(!canChangeProperty(new DataAttribute.Builder(getAttribute()).writable(true).build(), model)));
	}
}
