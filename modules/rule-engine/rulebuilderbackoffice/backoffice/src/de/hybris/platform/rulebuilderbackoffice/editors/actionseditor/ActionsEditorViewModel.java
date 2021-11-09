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
package de.hybris.platform.rulebuilderbackoffice.editors.actionseditor;

import de.hybris.platform.rulebuilderbackoffice.editors.AbstractEditorViewModel;
import de.hybris.platform.rulebuilderbackoffice.editors.EditorException;
import de.hybris.platform.rulebuilderbackoffice.editors.EditorRuntimeException;
import de.hybris.platform.rulebuilderbackoffice.editors.ParameterModel;
import de.hybris.platform.rulebuilderbackoffice.editors.TreeListModel;
import de.hybris.platform.rulebuilderbackoffice.editors.TreeNodeModel;
import de.hybris.platform.ruleengineservices.RuleEngineServiceException;
import de.hybris.platform.ruleengineservices.rule.data.RuleActionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleActionDefinitionCategoryData;
import de.hybris.platform.ruleengineservices.rule.data.RuleActionDefinitionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import de.hybris.platform.ruleengineservices.rule.services.RuleActionsRegistry;
import de.hybris.platform.ruleengineservices.rule.services.RuleActionsService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.GroupsModel;
import org.zkoss.zul.SimpleGroupsModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;


@Init(superclass = true)
public class ActionsEditorViewModel extends AbstractEditorViewModel<String>
{
	protected static final String ACTION_BREADCRUMBS = "actionBreadcrumbs";

	@WireVariable
	private transient RuleActionsRegistry ruleActionsRegistry;

	@WireVariable
	private transient RuleActionsService ruleActionsService;

	private transient GroupsModel<ActionDefinitionModel, Object, Object> actionDefinitions;
	private TreeNodeModel<ActionModel> actions;
	private String actionBreadcrumbs;

	protected RuleActionsRegistry getRuleActionsRegistry()
	{
		return ruleActionsRegistry;
	}

	public void setRuleActionsRegistry(final RuleActionsRegistry ruleActionsRegistry)
	{
		this.ruleActionsRegistry = ruleActionsRegistry;
	}

	protected RuleActionsService getRuleActionsService()
	{
		return ruleActionsService;
	}

	public void setRuleActionsService(final RuleActionsService ruleActionsService)
	{
		this.ruleActionsService = ruleActionsService;
	}

	public GroupsModel<ActionDefinitionModel, Object, Object> getActionDefinitions()
	{
		return actionDefinitions;
	}

	public void setActionDefinitions(final GroupsModel<ActionDefinitionModel, Object, Object> actionDefinitions)
	{
		this.actionDefinitions = actionDefinitions;
	}

	public TreeNodeModel<ActionModel> getActions()
	{
		return actions;
	}

	public void setActions(final TreeNodeModel<ActionModel> actions)
	{
		this.actions = actions;
	}

	public String getActionBreadcrumbs()
	{
		return actionBreadcrumbs;
	}

	public void setActionBreadcrumbs(final String actionBreadcrumbs)
	{
		this.actionBreadcrumbs = actionBreadcrumbs;
	}

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) final Component view)
	{
		Selectors.wireComponents(view, this, false);
		Selectors.wireEventListeners(view, this);
	}

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
					final Map<String, RuleActionDefinitionData> ruleActionDefinitions = getRuleActionsRegistry()
							.getActionDefinitionsForRuleTypeAsMap(getRuleType());
					loadActionDefinitions(ruleActionDefinitions);
					loadActions(ruleActionDefinitions);
				}
				catch (final EditorException e)
				{
					throw new EditorRuntimeException(e);
				}
			}
		});
	}

	@Command
	@NotifyChange(ACTION_BREADCRUMBS)
	public void dropAction(@BindingParam("target") final Object target,
			@BindingParam("isDropPlaceholder") final boolean isDropPlaceholder, @BindingParam("source") final Object source)
			throws EditorException
	{
		if (!isReadOnly())
		{
			final TreeNodeModel<ActionModel> targetNode;
			int targetIndex;

			if (target instanceof TreeNodeModel)
			{
				final TreeNodeModel<ActionModel> treeNode = (TreeNodeModel<ActionModel>) target;

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

			updateActions();
		}
	}

	protected void handleSourceObject(final boolean isDropPlaceholder, final Object source,
			final TreeNodeModel<ActionModel> targetNode, final int targetIndex) throws EditorException
	{
		if (source instanceof ActionDefinitionModel)
		{
			final ActionDefinitionModel actionDefinition = (ActionDefinitionModel) source;
			final TreeNodeModel<ActionModel> treeNode = createTreeNodeFromActionDefinition(actionDefinition.getActionDefinition(),
					targetNode);

			targetNode.addChild(targetIndex, treeNode);
		}
		else if (source instanceof TreeNodeModel)
		{
			handleSourceTreeNode(isDropPlaceholder, source, targetNode, targetIndex);
		}
		else
		{
			throw new EditorException("Drag and drop target not supported");
		}
	}


	protected void handleSourceTreeNode(final boolean isDropPlaceholder, final Object source,
			final TreeNodeModel<ActionModel> targetNode, final int targetIndex) throws EditorException
	{
		final TreeNodeModel<ActionModel> treeNode = (TreeNodeModel<ActionModel>) source;
		final TreeNodeModel<ActionModel> parentTreeNode = treeNode.getParent();

		if (Objects.equals(targetNode, treeNode) || targetNode.hasParent(treeNode))
		{
			throw new EditorException("Drag and drop target not supported: cannot drop to a nested element");
		}

		int newTargetIndex = targetIndex;

		if (isDropPlaceholder && targetNode.equals(parentTreeNode))
		{
			newTargetIndex--;
		}

		parentTreeNode.removeChild(treeNode);
		targetNode.addChild(newTargetIndex, treeNode);
		treeNode.setParent(targetNode);
	}

	@Command
	@NotifyChange(ACTION_BREADCRUMBS)
	public void removeAction(@BindingParam("treeNode") final TreeNodeModel<ActionModel> treeNode) throws EditorException
	{
		if (!isReadOnly())
		{
			final TreeNodeModel<ActionModel> parentNode = treeNode.getParent();
			parentNode.removeChild(treeNode);

			updateActions();
		}
	}

	@Command
	@NotifyChange(ACTION_BREADCRUMBS)
	public void changeTreeNodeParameter(@BindingParam("treeNode") final TreeNodeModel<ActionModel> treeNode,
			@BindingParam("parameterId") final String parameterId, @BindingParam("parameterValue") final Serializable parameterValue)
			throws EditorException
	{
		final ActionModel action = treeNode.getData();
		final ParameterModel parameter = action.getParameters().get(parameterId);
		parameter.setValue(parameterValue);

		updateDependentParametersForTreeNode(treeNode, parameter);
		updateActions();
	}

	protected void loadActionDefinitions(final Map<String, RuleActionDefinitionData> ruleActionDefinitions)
	{
		final Map<ActionDefinitionGroupModel, List<ActionDefinitionModel>> actionDefinitionMap = new TreeMap<>();

		for (final RuleActionDefinitionData ruleActionDefinition : ruleActionDefinitions.values())
		{
			final List<RuleActionDefinitionCategoryData> categories = ruleActionDefinition.getCategories();
			final ActionDefinitionModel actionDefinition = new ActionDefinitionModel();
			actionDefinition.setActionDefinition(ruleActionDefinition);

			for (final RuleActionDefinitionCategoryData category : categories)
			{
				final ActionDefinitionGroupModel actionDefinitionGroup = new ActionDefinitionGroupModel();
				actionDefinitionGroup.setCategory(category);

				List<ActionDefinitionModel> groupActionDefinitions = actionDefinitionMap.get(actionDefinitionGroup);

				if (groupActionDefinitions == null)
				{
					groupActionDefinitions = new ArrayList<>();

					actionDefinitionMap.put(actionDefinitionGroup, groupActionDefinitions);
				}

				groupActionDefinitions.add(actionDefinition);
			}
		}

		setActionDefinitions(fillGroupsModel(actionDefinitionMap));
	}

	protected GroupsModel<ActionDefinitionModel, Object, Object> fillGroupsModel(
			final Map<ActionDefinitionGroupModel, List<ActionDefinitionModel>> models)
	{
		final Set<Map.Entry<ActionDefinitionGroupModel, List<ActionDefinitionModel>>> entries = models.entrySet();

		final List<List<ActionDefinitionModel>> data = new ArrayList<>();
		final List<ActionDefinitionGroupModel> heads = new ArrayList<>();

		for (final Map.Entry<ActionDefinitionGroupModel, List<ActionDefinitionModel>> entry : entries)
		{
			heads.add(entry.getKey());

			final List<ActionDefinitionModel> actionDefinitionList = new ArrayList<>(entry.getValue());
			Collections.sort(actionDefinitionList);

			data.add(actionDefinitionList);
		}

		return new SimpleGroupsModel<ActionDefinitionModel, ActionDefinitionGroupModel, Object, Object>(data, heads);
	}

	protected void loadActions(final Map<String, RuleActionDefinitionData> ruleActionDefinitions) throws EditorException
	{
		try
		{
			final List<RuleActionData> ruleActions = getRuleActionsService().convertActionsFromString(getValue(),
					ruleActionDefinitions);
			setActionBreadcrumbs(getRuleActionsService().buildStyledActionBreadcrumbs(ruleActions, ruleActionDefinitions));
			setActions(convertActionsToTree(ruleActions, ruleActionDefinitions));
		}
		catch (final RuleEngineServiceException e)
		{
			throw new EditorException("Error loading actions", e);
		}
	}

	protected TreeNodeModel<ActionModel> convertActionsToTree(final List<RuleActionData> ruleActions,
			final Map<String, RuleActionDefinitionData> ruleActionDefinitions) throws EditorException
	{
		final TreeNodeModel<ActionModel> rootNode = new TreeNodeModel<>();
		rootNode.setAllowsChildren(Boolean.TRUE);
		rootNode.setExpanded(Boolean.TRUE);

		final TreeListModel<ActionModel> action = new TreeListModel<>();
		action.setTreeNode(rootNode);

		rootNode.setChildren(action);

		for (final RuleActionData ruleAction : ruleActions)
		{
			action.add(convertActionToTreeNode(ruleAction, ruleActionDefinitions, rootNode));
		}

		return rootNode;
	}

	protected TreeNodeModel<ActionModel> convertActionToTreeNode(final RuleActionData ruleAction,
			final Map<String, RuleActionDefinitionData> ruleActionDefinitions, final TreeNodeModel<ActionModel> parentNode)
			throws EditorException
	{
		final RuleActionDefinitionData ruleActionDefinition = ruleActionDefinitions.get(ruleAction.getDefinitionId());
		if (ruleActionDefinition == null)
		{
			throw new EditorException("No action definition found for id " + ruleAction.getDefinitionId());
		}

		return createTreeNodeFromAction(ruleAction, ruleActionDefinition, parentNode);
	}

	protected TreeNodeModel<ActionModel> createTreeNodeFromAction(final RuleActionData ruleAction,
			final RuleActionDefinitionData ruleActionDefinition, final TreeNodeModel<ActionModel> parentNode) throws EditorException
	{
		final Map<String, ParameterModel> parameters = convertRuleParametersToParameters(ruleAction.getParameters(),
				ruleActionDefinition.getParameters());

		final ActionModel action = new ActionModel();
		action.setActionDefinition(ruleActionDefinition);
		action.setParameters(parameters);
		action.setExpanded(Boolean.FALSE);

		final TreeNodeModel<ActionModel> treeNode = new TreeNodeModel<>();
		treeNode.setParent(parentNode);
		treeNode.setAllowsChildren(Boolean.FALSE);
		treeNode.setExpanded(Boolean.FALSE);
		treeNode.setData(action);

		return treeNode;
	}

	protected TreeNodeModel<ActionModel> createTreeNodeFromActionDefinition(final RuleActionDefinitionData ruleActionDefinition,
			final TreeNodeModel<ActionModel> parentNode) throws EditorException
	{
		final RuleActionData action = getRuleActionsService().createActionFromDefinition(ruleActionDefinition);
		return createTreeNodeFromAction(action, ruleActionDefinition, parentNode);
	}

	protected void updateActions() throws EditorException
	{
		try
		{
			beginValueUpdate();
			final Map<String, RuleActionDefinitionData> ruleActionDefinitions = getRuleActionsRegistry()
					.getActionDefinitionsForRuleTypeAsMap(getRuleType());

			final List<RuleActionData> ruleActions = convertTreeToRuleActions(getActions());
			setActionBreadcrumbs(getRuleActionsService().buildStyledActionBreadcrumbs(ruleActions, ruleActionDefinitions));
			setValue(getRuleActionsService().convertActionsToString(ruleActions, ruleActionDefinitions));
		}
		catch (final RuleEngineServiceException e)
		{
			throw new EditorException("Error updating actions", e);
		}
	}

	protected List<RuleActionData> convertTreeToRuleActions(final TreeNodeModel<ActionModel> rootNode) throws EditorException
	{
		final List<RuleActionData> ruleActions = new ArrayList<>();

		for (final TreeNodeModel<ActionModel> treeNode : rootNode.getChildren())
		{
			final RuleActionData ruleAction = convertTreeNodeToRuleAction(treeNode);
			ruleActions.add(ruleAction);
		}

		return ruleActions;
	}

	protected RuleActionData convertTreeNodeToRuleAction(final TreeNodeModel<ActionModel> treeNode) throws EditorException
	{
		return createRuleActionFromTreeNode(treeNode);
	}

	protected RuleActionData createRuleActionFromTreeNode(final TreeNodeModel<ActionModel> treeNode) throws EditorException
	{
		final ActionModel actionModel = treeNode.getData();
		final RuleActionDefinitionData ruleActionDefinition = actionModel.getActionDefinition();
		final Map<String, RuleParameterData> parameters = convertParametersToRuleParameters(actionModel.getParameters(),
				ruleActionDefinition.getParameters());

		final RuleActionData ruleAction = new RuleActionData();
		ruleAction.setDefinitionId(ruleActionDefinition.getId());
		ruleAction.setParameters(parameters);

		return ruleAction;
	}
}
