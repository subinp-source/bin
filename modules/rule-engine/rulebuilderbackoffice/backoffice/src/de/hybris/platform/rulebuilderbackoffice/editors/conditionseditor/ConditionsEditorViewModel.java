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
package de.hybris.platform.rulebuilderbackoffice.editors.conditionseditor;

import de.hybris.platform.rulebuilderbackoffice.editors.AbstractEditorViewModel;
import de.hybris.platform.rulebuilderbackoffice.editors.EditorException;
import de.hybris.platform.rulebuilderbackoffice.editors.EditorRuntimeException;
import de.hybris.platform.rulebuilderbackoffice.editors.ParameterModel;
import de.hybris.platform.rulebuilderbackoffice.editors.TreeListModel;
import de.hybris.platform.rulebuilderbackoffice.editors.TreeNodeModel;
import de.hybris.platform.ruleengineservices.RuleEngineServiceException;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionDefinitionCategoryData;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionDefinitionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import de.hybris.platform.ruleengineservices.rule.services.RuleConditionsRegistry;
import de.hybris.platform.ruleengineservices.rule.services.RuleConditionsService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.GroupsModel;
import org.zkoss.zul.SimpleGroupsModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;


@Init(superclass = true)
public class ConditionsEditorViewModel extends AbstractEditorViewModel<String>
{
	protected static final String CONDITION_BREADCRUMBS = "conditionBreadcrumbs";
	protected static final String EXCLUDED_CATEGORIES = "excluded_categories";
	protected static final String EXCLUDED_PRODUCTS = "excluded_products";
	protected static final String NOT_CONTAINS = "NOT_CONTAINS";
	protected static final String CATEGORIES_OPERATOR = "categories_operator";
	protected static final String EXCLUDED_CUSTOMERS = "excluded_customers";
	protected static final String EXCLUDED_CUSTOMER_GROUPS = "excluded_customer_groups";
	protected static final String CUSTOMER_GROUPS_OPERATOR = "customer_groups_operator";

	protected static final List<String> EXCLUDED_PARAM_NAMES = Arrays.asList(EXCLUDED_CATEGORIES, EXCLUDED_PRODUCTS,
			EXCLUDED_CUSTOMERS, EXCLUDED_CUSTOMER_GROUPS);

	@WireVariable
	private transient RuleConditionsRegistry ruleConditionsRegistry;

	@WireVariable
	private transient RuleConditionsService ruleConditionsService;

	private transient GroupsModel<ConditionDefinitionModel, Object, Object> conditionDefinitions;
	private TreeNodeModel<ConditionModel> conditions;
	private String conditionBreadcrumbs;

	protected RuleConditionsRegistry getRuleConditionsRegistry()
	{
		return ruleConditionsRegistry;
	}

	public void setRuleConditionsRegistry(final RuleConditionsRegistry ruleConditionsRegistry)
	{
		this.ruleConditionsRegistry = ruleConditionsRegistry;
	}

	protected RuleConditionsService getRuleConditionsService()
	{
		return ruleConditionsService;
	}

	public void setRuleConditionsService(final RuleConditionsService ruleConditionsService)
	{
		this.ruleConditionsService = ruleConditionsService;
	}

	public GroupsModel<ConditionDefinitionModel, Object, Object> getConditionDefinitions()
	{
		return conditionDefinitions;
	}

	public void setConditionDefinitions(final GroupsModel<ConditionDefinitionModel, Object, Object> conditionDefinitions)
	{
		this.conditionDefinitions = conditionDefinitions;
	}

	public TreeNodeModel<ConditionModel> getConditions()
	{
		return conditions;
	}

	public void setConditions(final TreeNodeModel<ConditionModel> conditions)
	{
		this.conditions = conditions;
	}

	public String getConditionBreadcrumbs()
	{
		return conditionBreadcrumbs;
	}

	public void setConditionBreadcrumbs(final String conditionBreadcrumbs)
	{
		this.conditionBreadcrumbs = conditionBreadcrumbs;
	}

	/**
	 * @throws EditorRuntimeException
	 */
	@SuppressWarnings("squid:S1188")
	@Override
	public void loadData()
	{
		getSessionService().executeInLocalView(new SessionExecutionBody()
		{
			@Override
			public void executeWithoutResult()
			{
				getI18NService().setLocalizationFallbackEnabled(true);
				try
				{
					final Map<String, RuleConditionDefinitionData> ruleConditionDefinitions = getRuleConditionsRegistry()
							.getConditionDefinitionsForRuleTypeAsMap(getRuleType());
					loadConditions(ruleConditionDefinitions);
					loadConditionDefinitions(ruleConditionDefinitions);
				}
				catch (final EditorException e)
				{
					throw new EditorRuntimeException(e);
				}
				finally
				{
					getI18NService().setLocalizationFallbackEnabled(false);
				}
			}
		});
	}

	@Command
	@NotifyChange(CONDITION_BREADCRUMBS)
	public void dropCondition(@BindingParam("target") final Object target,
			@BindingParam("isDropPlaceholder") final boolean isDropPlaceholder, @BindingParam("source") final Object source)
			throws EditorException
	{
		if (!isReadOnly())
		{
			final TreeNodeModel<ConditionModel> targetNode;
			int targetIndex;

			if (target instanceof TreeNodeModel)
			{
				final TreeNodeModel<ConditionModel> treeNode = (TreeNodeModel<ConditionModel>) target;

				if (isDropPlaceholder)
				{
					targetNode = treeNode;
					targetIndex = targetNode.getChildren().size();
				}
				else
				{
					targetNode = treeNode.getParent();
					targetIndex = targetNode.indexOfChild(treeNode);
				}
			}
			else
			{
				throw new EditorException("Drag and drop source not supported");
			}

			handleSourceObject(isDropPlaceholder, source, targetNode, targetIndex);

			updateConditions();
		}
	}

	protected void handleSourceObject(final boolean isDropPlaceholder, final Object source,
			final TreeNodeModel<ConditionModel> targetNode, final int targetIndex) throws EditorException
	{
		if (source instanceof ConditionDefinitionModel)
		{
			final ConditionDefinitionModel conditionDefinition = (ConditionDefinitionModel) source;
			final TreeNodeModel<ConditionModel> treeNode = createTreeNodeFromConditionDefinition(
					conditionDefinition.getConditionDefinition(), targetNode);

			targetNode.addChild(targetIndex, treeNode);
		}
		else if (source instanceof TreeNodeModel)
		{
			final TreeNodeModel<ConditionModel> treeNode = (TreeNodeModel<ConditionModel>) source;
			final TreeNodeModel<ConditionModel> parentNode = treeNode.getParent();

			if (Objects.equals(targetNode, treeNode) || targetNode.hasParent(treeNode))
			{
				throw new EditorException("Drag and drop target not supported: cannot drop to a nested element");
			}

			int newTargetIndex = targetIndex;

			if (isDropPlaceholder && targetNode.equals(parentNode))
			{
				newTargetIndex--;
			}

			parentNode.removeChild(treeNode);
			targetNode.addChild(newTargetIndex, treeNode);
			treeNode.setParent(targetNode);
		}
		else
		{
			throw new EditorException("Drag and drop target not supported");
		}
	}

	@Command
	@NotifyChange(CONDITION_BREADCRUMBS)
	public void removeCondition(@BindingParam("treeNode") final TreeNodeModel<ConditionModel> treeNode) throws EditorException
	{
		if (!isReadOnly())
		{
			final TreeNodeModel<ConditionModel> parentNode = treeNode.getParent();
			parentNode.removeChild(treeNode);

			updateConditions();
		}
	}

	@Command
	@NotifyChange(CONDITION_BREADCRUMBS)
	public void changeTreeNodeParameter(@BindingParam("treeNode") final TreeNodeModel<ConditionModel> treeNode,
			@BindingParam("parameterId") final String parameterId, @BindingParam("parameterValue") final Serializable parameterValue)
			throws EditorException
	{
		final ConditionModel condition = treeNode.getData();
		final ParameterModel parameter = condition.getParameters().get(parameterId);
		parameter.setValue(parameterValue);

		updateDependentParametersForTreeNode(treeNode, parameter);
		updateExcludeParamsAndRecreateNode(treeNode, parameterId, parameterValue, condition);
		updateConditions();
	}

	protected void updateExcludeParamsAndRecreateNode(final TreeNodeModel<ConditionModel> treeNode, final String parameterId,
			final Object parameterValue, final ConditionModel condition)
	{
		if (parameterId.equals(CATEGORIES_OPERATOR) || parameterId.equals(CUSTOMER_GROUPS_OPERATOR))
		{
			if (NOT_CONTAINS.equals(parameterValue.toString()))
			{
				disableExcludeParameters(condition.getParameters());
			}
			else
			{
				enableExcludeParameters(condition.getParameters());
			}
			recreateTreeNode(treeNode);
		}
	}

	protected void loadConditionDefinitions(final Map<String, RuleConditionDefinitionData> ruleConditionDefinitions)
	{
		final Map<ConditionDefinitionGroupModel, List<ConditionDefinitionModel>> conditionDefinitionsMap = new TreeMap<>();

		for (final RuleConditionDefinitionData ruleConditionDefinition : ruleConditionDefinitions.values())
		{
			final List<RuleConditionDefinitionCategoryData> categories = ruleConditionDefinition.getCategories();
			final ConditionDefinitionModel conditionDefinition = new ConditionDefinitionModel();
			conditionDefinition.setConditionDefinition(ruleConditionDefinition);

			for (final RuleConditionDefinitionCategoryData category : categories)
			{
				final ConditionDefinitionGroupModel conditionDefinitionGroup = new ConditionDefinitionGroupModel();
				conditionDefinitionGroup.setCategory(category);

				List<ConditionDefinitionModel> groupConditionDefinitions = conditionDefinitionsMap.get(conditionDefinitionGroup);

				if (groupConditionDefinitions == null)
				{
					groupConditionDefinitions = new ArrayList<>();

					conditionDefinitionsMap.put(conditionDefinitionGroup, groupConditionDefinitions);
				}

				groupConditionDefinitions.add(conditionDefinition);
			}
		}

		setConditionDefinitions(fillGroupsModel(conditionDefinitionsMap));
	}

	protected GroupsModel<ConditionDefinitionModel, Object, Object> fillGroupsModel(
			final Map<ConditionDefinitionGroupModel, List<ConditionDefinitionModel>> models)
	{
		final Set<Map.Entry<ConditionDefinitionGroupModel, List<ConditionDefinitionModel>>> entries = models.entrySet();

		final List<List<ConditionDefinitionModel>> data = new ArrayList<>();
		final List<ConditionDefinitionGroupModel> heads = new ArrayList<>();

		for (final Map.Entry<ConditionDefinitionGroupModel, List<ConditionDefinitionModel>> entry : entries)
		{
			heads.add(entry.getKey());

			final List<ConditionDefinitionModel> conditionDefinitionModels = new ArrayList<>(entry.getValue());
			Collections.sort(conditionDefinitionModels);

			data.add(conditionDefinitionModels);
		}

		return new SimpleGroupsModel<ConditionDefinitionModel, ConditionDefinitionGroupModel, Object, Object>(data, heads);
	}

	protected void loadConditions(final Map<String, RuleConditionDefinitionData> ruleConditionDefinitions) throws EditorException
	{
		try
		{
			final List<RuleConditionData> ruleConditions = getRuleConditionsService().convertConditionsFromString(getValue(),
					ruleConditionDefinitions);
			setConditionBreadcrumbs(getRuleConditionsService().buildStyledConditionBreadcrumbs(ruleConditions,
					ruleConditionDefinitions));
			setConditions(convertConditionsToTree(ruleConditions, ruleConditionDefinitions));
		}
		catch (final RuleEngineServiceException e)
		{
			throw new EditorException("Error loading conditions", e);
		}
	}

	protected TreeNodeModel<ConditionModel> convertConditionsToTree(final List<RuleConditionData> ruleConditions,
			final Map<String, RuleConditionDefinitionData> ruleConditionDefinitions) throws EditorException
	{
		final TreeNodeModel<ConditionModel> rootNode = new TreeNodeModel<>();
		rootNode.setAllowsChildren(Boolean.TRUE);
		rootNode.setExpanded(Boolean.TRUE);

		final TreeListModel<ConditionModel> childConditions = new TreeListModel<>();
		childConditions.setTreeNode(rootNode);

		rootNode.setChildren(childConditions);

		for (final RuleConditionData ruleCondition : ruleConditions)
		{
			childConditions.add(convertConditionToTreeNode(ruleCondition, ruleConditionDefinitions, rootNode));
		}

		return rootNode;
	}

	protected TreeNodeModel<ConditionModel> convertConditionToTreeNode(final RuleConditionData ruleCondition,
			final Map<String, RuleConditionDefinitionData> ruleConditionDefinitions, final TreeNodeModel<ConditionModel> parentNode)
			throws EditorException
	{
		final RuleConditionDefinitionData ruleConditionDefinition = ruleConditionDefinitions.get(ruleCondition.getDefinitionId());
		if (ruleConditionDefinition == null)
		{
			throw new EditorException("No condition definition found for id " + ruleCondition.getDefinitionId());
		}

		final TreeNodeModel<ConditionModel> treeNode = createTreeNodeFromCondition(ruleCondition, ruleConditionDefinition,
				parentNode);

		if (BooleanUtils.isTrue(ruleConditionDefinition.getAllowsChildren())
				&& CollectionUtils.isNotEmpty(ruleCondition.getChildren()))
		{
			for (final RuleConditionData childRuleCondition : ruleCondition.getChildren())
			{
				treeNode.addChild(convertConditionToTreeNode(childRuleCondition, ruleConditionDefinitions, treeNode));
			}
		}

		return treeNode;
	}

	protected TreeNodeModel<ConditionModel> createTreeNodeFromCondition(final RuleConditionData ruleCondition,
			final RuleConditionDefinitionData ruleConditionDefinition, final TreeNodeModel<ConditionModel> parentNode)
			throws EditorException
	{
		final Map<String, ParameterModel> parameters = convertRuleParametersToParameters(ruleCondition.getParameters(),
				ruleConditionDefinition.getParameters());

		final Optional<String> categoriesOperatorValue = parameters.get(CATEGORIES_OPERATOR) == null ? Optional.empty() : Optional
				.of(parameters.get(CATEGORIES_OPERATOR).getValue().toString());
		final Optional<String> customerGroupOperatorValue = parameters.get(CUSTOMER_GROUPS_OPERATOR) == null ? Optional.empty()
				: Optional.of(parameters.get(CUSTOMER_GROUPS_OPERATOR).getValue().toString());

		if ((categoriesOperatorValue.isPresent() && NOT_CONTAINS.equals(categoriesOperatorValue.get()))
				|| (customerGroupOperatorValue.isPresent() && NOT_CONTAINS.equals(customerGroupOperatorValue.get())))
		{
			disableExcludeParameters(parameters);
		}

		final ConditionModel condition = new ConditionModel();
		condition.setConditionDefinition(ruleConditionDefinition);
		condition.setParameters(parameters);
		final Boolean expanded = parameters.values().stream()
				.filter(parameter -> CollectionUtils.isNotEmpty(parameter.getValidationInfos())).count() > 0 ? Boolean.TRUE
				: Boolean.FALSE;
		condition.setExpanded(expanded);

		final TreeNodeModel<ConditionModel> treeNode = new TreeNodeModel<>();
		treeNode.setParent(parentNode);
		treeNode.setAllowsChildren(ruleConditionDefinition.getAllowsChildren());
		treeNode.setExpanded(Boolean.TRUE);
		treeNode.setData(condition);

		if (BooleanUtils.isTrue(ruleConditionDefinition.getAllowsChildren()))
		{
			final TreeListModel<ConditionModel> treeList = new TreeListModel<>();
			treeList.setTreeNode(treeNode);

			treeNode.setChildren(treeList);
		}

		return treeNode;
	}

	protected TreeNodeModel<ConditionModel> createTreeNodeFromConditionDefinition(
			final RuleConditionDefinitionData ruleConditionDefinition, final TreeNodeModel<ConditionModel> parentNode)
			throws EditorException
	{
		final RuleConditionData ruleCondition = getRuleConditionsService().createConditionFromDefinition(ruleConditionDefinition);
		return createTreeNodeFromCondition(ruleCondition, ruleConditionDefinition, parentNode);
	}

	protected void updateConditions() throws EditorException
	{
		try
		{
			beginValueUpdate();
			final Map<String, RuleConditionDefinitionData> ruleConditionDefinitions = getRuleConditionsRegistry()
					.getConditionDefinitionsForRuleTypeAsMap(getRuleType());

			final List<RuleConditionData> ruleConditions = convertTreeToRuleConditions(getConditions());
			setConditionBreadcrumbs(getRuleConditionsService().buildStyledConditionBreadcrumbs(ruleConditions,
					ruleConditionDefinitions));
			setValue(getRuleConditionsService().convertConditionsToString(ruleConditions, ruleConditionDefinitions));
		}
		catch (final RuleEngineServiceException e)
		{
			throw new EditorException("Error updating conditions", e);
		}
	}

	protected List<RuleConditionData> convertTreeToRuleConditions(final TreeNodeModel<ConditionModel> rootNode)
			throws EditorException
	{
		final List<RuleConditionData> ruleConditions = new ArrayList<>();

		for (final TreeNodeModel<ConditionModel> treeNode : rootNode.getChildren())
		{
			final RuleConditionData ruleCondition = convertTreeNodeToRuleCondition(treeNode);
			ruleConditions.add(ruleCondition);
		}

		return ruleConditions;
	}

	protected RuleConditionData convertTreeNodeToRuleCondition(final TreeNodeModel<ConditionModel> treeNode)
			throws EditorException
	{
		final ConditionModel condition = treeNode.getData();
		final RuleConditionDefinitionData ruleConditionDefinition = condition.getConditionDefinition();
		final RuleConditionData ruleCondition = createRuleConditionFromTreeNode(treeNode);

		if (BooleanUtils.isTrue(ruleConditionDefinition.getAllowsChildren()) && CollectionUtils.isNotEmpty(treeNode.getChildren()))
		{
			for (final TreeNodeModel<ConditionModel> childNode : treeNode.getChildren())
			{
				final RuleConditionData childRuleCondition = convertTreeNodeToRuleCondition(childNode);
				ruleCondition.getChildren().add(childRuleCondition);
			}
		}

		return ruleCondition;
	}

	protected RuleConditionData createRuleConditionFromTreeNode(final TreeNodeModel<ConditionModel> treeNode)
			throws EditorException
	{
		final ConditionModel condition = treeNode.getData();
		final RuleConditionDefinitionData ruleConditionDefinition = condition.getConditionDefinition();
		final Map<String, RuleParameterData> parameters = convertParametersToRuleParameters(condition.getParameters(),
				ruleConditionDefinition.getParameters());

		final RuleConditionData ruleCondition = new RuleConditionData();
		ruleCondition.setDefinitionId(ruleConditionDefinition.getId());
		ruleCondition.setParameters(parameters);
		ruleCondition.setChildren(new ArrayList<>());

		return ruleCondition;
	}

	protected void disableExcludeParameters(final Map<String, ParameterModel> parameters)
	{
		EXCLUDED_PARAM_NAMES.stream().forEach(excludeParam -> {
			if (parameters.get(excludeParam) != null)
			{
				parameters.get(excludeParam).setReadOnly(true);
				parameters.get(excludeParam).setValue(null);
			}
		});
	}

	protected void enableExcludeParameters(final Map<String, ParameterModel> parameters)
	{
		EXCLUDED_PARAM_NAMES.stream().forEach(excludeParam -> {
			if (parameters.get(excludeParam) != null)
			{
				parameters.get(excludeParam).setReadOnly(false);
			}
		});
	}
}
