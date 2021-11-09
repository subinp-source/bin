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

import de.hybris.platform.ruleengineservices.compiler.RuleCompilerProblem;
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterDefinitionData;
import de.hybris.platform.ruleengineservices.rule.services.RuleParameterFilterValueProvider;
import de.hybris.platform.ruleengineservices.rule.services.RuleParametersService;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleParameterTypeFormatter;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleParameterValueMapperException;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleParameterValueMapperStrategy;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.session.SessionService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;


public abstract class AbstractEditorViewModel<V extends Serializable> implements Serializable
{
	private static final Logger LOG = LoggerFactory.getLogger(AbstractEditorViewModel.class);

	private static final ParameterComparator PARAMETER_COMPARATOR = new ParameterComparator();

	private static final ValidationInfoComparator VALIDATION_INFO_COMPARATOR = new ValidationInfoComparator();

	private static final long serialVersionUID = 1L;

	protected static final String INITIAL_VALUE = "initialValue";
	protected static final String RULE_TYPE = "ruleType";
	protected static final String RULE_COMPILER_PROBLEMS = "ruleCompilerProblems";
	protected static final String READ_ONLY = "readOnly";
	protected static final String VALUE_CHANGED_EVENT = "onValueChanged";
	protected static final String BEGIN_VALUE_CHANGE_EVENT = "onBeginValueChange";

	public static final String UUID_PREFIX = "uuid: ";

	@WireVariable
	private transient RuleParametersService ruleParametersService;

	@WireVariable
	private transient RuleParameterTypeFormatter backofficeRuleParameterTypeFormatter;

	@WireVariable
	private transient RuleParameterValueMapperStrategy ruleParameterValueMapperStrategy;

	@WireVariable
	private transient I18NService i18NService;

	@WireVariable
	private transient SessionService sessionService;

	@WireVariable
	private transient RuleParameterFilterValueProvider ruleParameterFilterValueProvider;

	private V value;
	private Class<? extends AbstractRuleModel> ruleType;
	private Map<String, List<RuleCompilerProblem>> ruleCompilerProblems;

	private Component component;
	private Boolean readOnly;

	@Init
	public void init(@ContextParam(ContextType.EXECUTION) final Execution execution,
			@ContextParam(ContextType.COMPONENT) final Component component)
	{
		this.component = component;

		final Map<?, ?> args = execution.getArg();
		value = (V) args.get(INITIAL_VALUE);
		ruleType = (Class<? extends AbstractRuleModel>) args.get(RULE_TYPE);
		ruleCompilerProblems = (Map<String, List<RuleCompilerProblem>>) args.get(RULE_COMPILER_PROBLEMS);
		readOnly = (Boolean) args.get(READ_ONLY);

		if (ruleCompilerProblems == null)
		{
			ruleCompilerProblems = Collections.emptyMap();
		}

		try
		{
			loadData();
		}
		catch (final EditorException e)
		{
			throw new EditorRuntimeException(e);
		}
	}

	public void beginValueUpdate()
	{
		setReadOnly(Boolean.TRUE);
		Events.sendEvent(BEGIN_VALUE_CHANGE_EVENT, component, value);
	}

	public String join(final String... styleClasses)
	{
		return StringUtils.join(styleClasses, ' ');
	}

	protected abstract void loadData() throws EditorException;

	protected Map<String, ParameterModel> convertRuleParametersToParameters(final Map<String, RuleParameterData> ruleParameters,
			final Map<String, RuleParameterDefinitionData> ruleParameterDefinitions) throws EditorException
	{
		if (MapUtils.isEmpty(ruleParameters))
		{
			return Collections.emptyMap();
		}

		final List<ParameterModel> parameters = new ArrayList<>();

		for (final Entry<String, RuleParameterDefinitionData> entry : ruleParameterDefinitions.entrySet())
		{
			final String parameterId = entry.getKey();
			final RuleParameterDefinitionData ruleParameterDefinition = entry.getValue();

			RuleParameterData ruleParameter = ruleParameters.get(parameterId);
			if (ruleParameter == null)
			{
				ruleParameter = ruleParametersService.createParameterFromDefinition(ruleParameterDefinition);
			}

			final String type = getBackofficeRuleParameterTypeFormatter().formatParameterType(ruleParameterDefinition.getType());
			Object parameterValue;
			ValidationInfoModel paramValueMapperValidationInfo = null;

			try
			{
				parameterValue = getRuleParameterValueMapperStrategy().fromRuleParameter(ruleParameter.getValue(),
						ruleParameterDefinition.getType());
			}
			catch (final RuleParameterValueMapperException e)
			{
				LOG.error("RuleParameterValueMapperException caught: {}", e);

				paramValueMapperValidationInfo = new ValidationInfoModel();
				final ValidationInfoSeverity severity = ValidationInfoSeverity.ERROR;
				paramValueMapperValidationInfo.setSeverity(severity);
				paramValueMapperValidationInfo.setIconStyleClass(severity.getIconStyleClass());
				paramValueMapperValidationInfo.setStyleClass(severity.getStyleClass());
				paramValueMapperValidationInfo.setMessage(e.getMessage());
				parameterValue = null;
			}

			final ParameterModel parameter = new ParameterModel();
			parameter.setUuid(ruleParameter.getUuid());
			parameter.setId(parameterId);
			parameter.setName(ruleParameterDefinition.getName());
			parameter.setDescription(ruleParameterDefinition.getDescription());
			parameter.setPriority(ruleParameterDefinition.getPriority());
			parameter.setType(type);
			parameter.setValue((Serializable) parameterValue);
			parameter.setRequired(ruleParameterDefinition.getRequired().booleanValue());
			parameter.setFilters(ruleParameterDefinition.getFilters());
			parameter.setReadOnly(false);
			parameter.setDefaultEditor(ruleParameterDefinition.getDefaultEditor());
			parameter.setReadOnly(isReadOnly());

			final List<ValidationInfoModel> validationInfos = loadParameterValidationInfos(ruleParameter);

			if (nonNull(paramValueMapperValidationInfo))
			{
				validationInfos.add(paramValueMapperValidationInfo);
			}
			parameter.setValidationInfos(validationInfos);

			if (CollectionUtils.isEmpty(validationInfos))
			{
				parameter.setValid(true);
				parameter.setValidationIconStyleClass(ValidationInfoSeverity.NONE.getIconStyleClass());
				parameter.setValidationStyleClass(ValidationInfoSeverity.NONE.getStyleClass());
			}
			else
			{
				//sort validationInfos by severity
				Collections.sort(validationInfos, VALIDATION_INFO_COMPARATOR);
				parameter.setValid(false);
				final ValidationInfoModel validationInfo = validationInfos.get(0);
				parameter.setValidationIconStyleClass(validationInfo.getIconStyleClass());
				parameter.setValidationStyleClass(validationInfo.getStyleClass());
			}

			parameters.add(parameter);
		}

		Collections.sort(parameters, PARAMETER_COMPARATOR);

		final Map<String, ParameterModel> builderParameters = new LinkedHashMap<>();
		for (final ParameterModel parameter : parameters)
		{
			builderParameters.put(parameter.getId(), parameter);
			if (parameter.getValue() != null)
			{
				updateDependentParameters(parameter, parameters);
			}
		}

		return builderParameters;
	}

	@SuppressWarnings("squid:RedundantThrowsDeclarationCheck")
	protected List<ValidationInfoModel> loadParameterValidationInfos(final RuleParameterData ruleParameter) throws EditorException
	{
		final List<RuleCompilerProblem> compilerProblems = ruleCompilerProblems.get(ruleParameter.getUuid());

		final List<ValidationInfoModel> validationInfos = new ArrayList<>();

		if (CollectionUtils.isEmpty(compilerProblems))
		{
			return validationInfos;
		}

		for (final RuleCompilerProblem compilerProblem : compilerProblems)
		{
			ValidationInfoSeverity severity;

			switch (compilerProblem.getSeverity())
			{
				case ERROR:
					severity = ValidationInfoSeverity.ERROR;
					break;

				case WARNING:
					severity = ValidationInfoSeverity.WARN;
					break;

				default:
					severity = ValidationInfoSeverity.NONE;
					break;
			}

			final ValidationInfoModel validationInfo = new ValidationInfoModel();
			validationInfo.setSeverity(severity);
			validationInfo.setIconStyleClass(severity.getIconStyleClass());
			validationInfo.setStyleClass(severity.getStyleClass());
			validationInfo.setMessage(compilerProblem.getMessage());

			validationInfos.add(validationInfo);
		}

		return validationInfos;
	}

	protected Map<String, RuleParameterData> convertParametersToRuleParameters(final Map<String, ParameterModel> parameters,
			final Map<String, RuleParameterDefinitionData> ruleParameterDefinitions) throws EditorException
	{
		final Map<String, RuleParameterData> ruleParameters = new HashMap<>();

		if (MapUtils.isEmpty(parameters))
		{
			return ruleParameters;
		}

		try
		{
			for (final Entry<String, RuleParameterDefinitionData> entry : ruleParameterDefinitions.entrySet())
			{
				final String parameterId = entry.getKey();
				final RuleParameterDefinitionData ruleParameterDefinition = entry.getValue();

				final RuleParameterData ruleParameter;
				final ParameterModel parameter = parameters.get(parameterId);

				if (parameter != null)
				{
					final Object paramaterValue = getRuleParameterValueMapperStrategy().toRuleParameter(parameter.getValue(),
							ruleParameterDefinition.getType());

					ruleParameter = new RuleParameterData();
					ruleParameter.setUuid(parameter.getUuid());
					ruleParameter.setType(ruleParameterDefinition.getType());
					ruleParameter.setValue(paramaterValue);
				}
				else
				{
					ruleParameter = ruleParametersService.createParameterFromDefinition(ruleParameterDefinition);
				}

				ruleParameters.put(parameterId, ruleParameter);
			}
		}
		catch (final RuleParameterValueMapperException e)
		{
			throw new EditorException(e);
		}

		return ruleParameters;
	}

	protected <T extends RuleItemModel> void updateDependentParametersForTreeNode(final TreeNodeModel<T> treeNode,
			final ParameterModel masterParameter)
	{
		final Collection<ParameterModel> treeNodeParameters = treeNode.getData().getParameters().values();
		final List<ParameterModel> dependentParameters = updateDependentParameters(masterParameter, treeNodeParameters);
		if (CollectionUtils.isNotEmpty(dependentParameters))
		{
			recreateTreeNode(treeNode);
		}
	}

	protected List<ParameterModel> updateDependentParameters(final ParameterModel masterParameter,
			final Collection<ParameterModel> treeNodeParameters)
	{
		final List<ParameterModel> dependentParameters = getDependentParameters(masterParameter, treeNodeParameters);
		dependentParameters.forEach(dependentParameter -> {
			final HashMap<String, Serializable> customAttributes = new HashMap<>();
			if (MapUtils.isNotEmpty(dependentParameter.getCustomAttributes()))
			{
				customAttributes.putAll(dependentParameter.getCustomAttributes());
			}
			if (masterParameter.getValue() != null)
			{
				getFiltersForMasterFieldFilter(masterParameter, dependentParameter).forEach(
						filter -> customAttributes.put(getCustomAttributeName(filter),
								(Serializable) getRuleParameterFilterValueProvider().evaluate(filter.getValue(),
										masterParameter.getValue())));
			}
			else
			{
				getFiltersForMasterFieldFilter(masterParameter, dependentParameter).forEach(
						filter -> customAttributes.remove(getCustomAttributeName(filter)));
			}
			dependentParameter.setCustomAttributes(customAttributes);
		});
		return dependentParameters;
	}

	protected List<ParameterModel> getDependentParameters(final ParameterModel masterParameter,
			final Collection<ParameterModel> treeNodeParameters)
	{
		return treeNodeParameters.stream().filter(p -> parameterHasFilter(p, masterParameter.getId())).collect(Collectors.toList());
	}

	protected boolean parameterHasFilter(final ParameterModel parameter, final String masterParameterId)
	{
		return parameter.getFilters().values().stream()
				.anyMatch(v -> getRuleParameterFilterValueProvider().getParameterId(v).equals(masterParameterId));
	}

	protected String getCustomAttributeName(final Entry<String, String> filter)
	{
		return "referenceSearchCondition_" + filter.getKey();
	}

	protected List<Entry<String, String>> getFiltersForMasterFieldFilter(final ParameterModel masterParameter,
			final ParameterModel dependentParameter)
	{
		return dependentParameter.getFilters().entrySet().stream()
				.filter(e -> getRuleParameterFilterValueProvider().getParameterId(e.getValue()).equals(masterParameter.getId()))
				.collect(Collectors.toList());
	}

	protected <T extends RuleItemModel> void recreateTreeNode(final TreeNodeModel<T> treeNode)
	{
		final TreeNodeModel<T> updatedChildNode = treeNode;
		final TreeNodeModel<T> parentNode = treeNode.getParent();
		final int updatedNodeIndex = parentNode.indexOfChild(treeNode);
		parentNode.removeChild(treeNode);
		parentNode.addChild(updatedNodeIndex, updatedChildNode);
	}

	protected RuleParametersService getRuleParametersService()
	{
		return ruleParametersService;
	}

	public void setRuleParametersService(final RuleParametersService ruleParametersService)
	{
		this.ruleParametersService = ruleParametersService;
	}

	public RuleParameterTypeFormatter getBackofficeRuleParameterTypeFormatter()
	{
		return backofficeRuleParameterTypeFormatter;
	}

	public void setBackofficeRuleParameterTypeFormatter(final RuleParameterTypeFormatter backofficeRuleParameterTypeFormatter)
	{
		this.backofficeRuleParameterTypeFormatter = backofficeRuleParameterTypeFormatter;
	}

	protected RuleParameterValueMapperStrategy getRuleParameterValueMapperStrategy()
	{
		return ruleParameterValueMapperStrategy;
	}

	public void setRuleParameterValueMapperStrategy(final RuleParameterValueMapperStrategy ruleParameterValueMapperStrategy)
	{
		this.ruleParameterValueMapperStrategy = ruleParameterValueMapperStrategy;
	}

	public RuleParameterFilterValueProvider getRuleParameterFilterValueProvider()
	{
		return ruleParameterFilterValueProvider;
	}

	public void setRuleParameterFilterValueProvider(final RuleParameterFilterValueProvider ruleParameterFilterValueProvider)
	{
		this.ruleParameterFilterValueProvider = ruleParameterFilterValueProvider;
	}

	protected I18NService getI18NService()
	{
		return i18NService;
	}

	public void setI18NService(final I18NService i18NService)
	{
		this.i18NService = i18NService;
	}

	protected SessionService getSessionService()
	{
		return sessionService;
	}

	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	public V getValue()
	{
		return value;
	}

	public boolean isReadOnly()
	{
		return readOnly.booleanValue();
	}

	protected void setReadOnly(final Boolean value)
	{
		readOnly = value;
	}

	public void setValue(final V value)
	{
		this.value = value;
		Events.sendEvent(VALUE_CHANGED_EVENT, component, value);
		setReadOnly(Boolean.FALSE);
	}

	public Class<? extends AbstractRuleModel> getRuleType()
	{
		return ruleType;
	}

	public Map<String, List<RuleCompilerProblem>> getRuleCompilerProblems()
	{
		return ruleCompilerProblems;
	}

	protected Component getComponent()
	{
		return component;
	}
}
