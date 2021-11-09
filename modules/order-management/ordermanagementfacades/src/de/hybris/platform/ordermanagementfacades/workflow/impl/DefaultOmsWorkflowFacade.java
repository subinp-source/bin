/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 *
 */
package de.hybris.platform.ordermanagementfacades.workflow.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.ordermanagementfacades.workflow.OmsWorkflowFacade;
import de.hybris.platform.ordermanagementfacades.workflow.data.WorkflowActionData;
import de.hybris.platform.ordermanagementfacades.workflow.data.WorkflowCodesDataList;
import de.hybris.platform.ordermanagementfacades.workflow.data.WorkflowData;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.workflow.WorkflowActionService;
import de.hybris.platform.workflow.WorkflowProcessingService;
import de.hybris.platform.workflow.WorkflowService;
import de.hybris.platform.workflow.WorkflowTemplateService;
import de.hybris.platform.workflow.enums.WorkflowActionStatus;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;
import de.hybris.platform.workflow.model.WorkflowModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.StringUtils;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;


/**
 * Default implementation for {@link OmsWorkflowFacade}
 */
public class DefaultOmsWorkflowFacade implements OmsWorkflowFacade
{
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultOmsWorkflowFacade.class);

	private ModelService modelService;
	private WorkflowService workflowService;
	private WorkflowTemplateService workflowTemplateService;
	private WorkflowProcessingService workflowProcessingService;
	private WorkflowActionService workflowActionService;
	private UserService userService;
	private Converter<WorkflowModel, WorkflowData> workflowConverter;
	private Converter<WorkflowActionModel, WorkflowActionData> workflowActionConverter;

	@Override
	public WorkflowData startErrorRecoveryWorkflow(final ItemModel item, final String workflowName,
			final String workflowTemplateCode, final String userGroupUid, final String errorType, final String errorDescription)
	{
		validateParameterNotNullStandardMessage("item", item);
		validateParameterNotNullStandardMessage("workflowName", workflowName);
		validateParameterNotNullStandardMessage("workflowTemplateCode", workflowTemplateCode);
		validateParameterNotNullStandardMessage("errorType", errorType);
		validateParameterNotNullStandardMessage("errorDescription", errorDescription);

		final WorkflowModel workflowModel = createAndStartWorkflow(item, workflowName, workflowTemplateCode, userGroupUid);

		if (workflowModel != null)
		{
			workflowModel.getActions().stream().filter(action -> WorkflowActionStatus.IN_PROGRESS.equals(action.getStatus()))
					.findFirst().ifPresent(action -> {
				action.setComment(errorDescription);
				action.setDescription(errorType);
				getModelService().save(action);
			});
		}

		return getWorkflowConverter().convert(workflowModel);
	}

	@Override
	public WorkflowData startWorkflow(final ItemModel item, final String workflowName, final String workflowTemplateCode,
			final String userGroupUid)
	{
		validateParameterNotNullStandardMessage("item", item);
		validateParameterNotNullStandardMessage("workflowName", workflowName);
		validateParameterNotNullStandardMessage("workflowTemplateCode", workflowTemplateCode);

		final WorkflowModel workflowModel = createAndStartWorkflow(item, workflowName, workflowTemplateCode, userGroupUid);

		return getWorkflowConverter().convert(workflowModel);
	}

	@Override
	public List<WorkflowActionData> getWorkflowActions()
	{
		final List<WorkflowActionModel> workflowActionModelList = getWorkflowActionService()
				.getAllUserWorkflowActionsWithAttachments(emptyList(), singletonList(WorkflowActionStatus.IN_PROGRESS));
		return workflowActionModelList.stream().map(workflowAction -> getWorkflowActionConverter().convert(workflowAction))
				.collect(toList());
	}

	@Override
	public void decideAction(final String workflowCode, final String workflowDecisionCode)
	{
		validateParameterNotNullStandardMessage("workflowCode", workflowCode);
		validateParameterNotNullStandardMessage("workflowDecisionCode", workflowDecisionCode);

		final Optional<WorkflowActionModel> workflowActionOptional = getWorkflowService().getWorkflowForCode(workflowCode)
				.getActions().stream().filter(action -> WorkflowActionStatus.IN_PROGRESS.equals(action.getStatus())).findFirst();

		if (workflowActionOptional.isPresent())
		{
			final WorkflowActionModel workflowAction = workflowActionOptional.get();
			final Optional<WorkflowDecisionModel> workflowDecisionOptional = workflowAction.getDecisions().stream()
					.filter(decision -> workflowDecisionCode.equals(decision.getName())).findFirst();

			if (workflowDecisionOptional.isPresent())
			{
				workflowAction.setPrincipalAssigned(getUserService().getAdminUserGroup());
				getModelService().save(workflowAction);
				getWorkflowProcessingService().decideAction(workflowAction, workflowDecisionOptional.get());
			}
		}
	}

	@Override
	public void decideActions(final WorkflowCodesDataList workflowCodes, final String workflowDecisionCode)
	{
		validateParameterNotNullStandardMessage("workflowCodes", workflowCodes);
		validateParameterNotNullStandardMessage("workflowDecisionCode", workflowDecisionCode);

		if (workflowCodes.getCodes() != null)
		{
			workflowCodes.getCodes().forEach(workflowCode -> {
				try
				{
					this.decideAction(workflowCode, workflowDecisionCode);
				}
				catch (final UnknownIdentifierException | IllegalArgumentException e) //NOSONAR
				{
					LOGGER.error(e.getMessage());
				}
			});
		}
	}

	/**
	 * Creates and starts a workflow with the given template for the given {@link ItemModel}.
	 *
	 * @param item
	 * 		the {@link ItemModel} for which to start the workflow
	 * @param workflowName
	 * 		name given to the workflow at creation time
	 * @param workflowTemplateCode
	 * 		template code of the workflow to be started
	 * @param userGroupUid
	 * 		the uid of the group to be assigned to the workflow actions
	 * @return instance of created {@link WorkflowModel}
	 */
	protected WorkflowModel createAndStartWorkflow(final ItemModel item, final String workflowName,
			final String workflowTemplateCode, final String userGroupUid)
	{
		WorkflowModel resultWorkflow = null;

		try
		{
			final WorkflowTemplateModel workflowTemplate = getWorkflowTemplateService()
					.getWorkflowTemplateForCode(workflowTemplateCode);
			resultWorkflow = getWorkflowService()
					.createWorkflow(workflowTemplateCode + "_" + workflowName, workflowTemplate, singletonList(item),
							getUserService().getAdminUser());
			getWorkflowProcessingService().startWorkflow(resultWorkflow);
			resultWorkflow.getActions().forEach(action -> {
				action.setPrincipalAssigned(getUserGroupModel(userGroupUid));
				getModelService().save(action);
			});

		}
		catch (final UnknownIdentifierException | IllegalArgumentException e) //NOSONAR
		{
			LOGGER.error("No workflow template found. Cannot create workflow for code {}.", workflowTemplateCode);
		}

		return resultWorkflow;
	}

	/**
	 * Resolves the given group's uid to the {@link UserGroupModel}
	 *
	 * @param userGroupUid
	 * 		the group's uid. If empty or null the method will return admin user group
	 * @return instance of {@link UserGroupModel}
	 */
	protected UserGroupModel getUserGroupModel(final String userGroupUid)
	{
		final UserGroupModel result;

		if (StringUtils.isEmpty(userGroupUid))
		{
			result = getUserService().getAdminUserGroup();
		}
		else
		{
			result = getUserService().getUserGroupForUID(userGroupUid);
		}

		return result;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected WorkflowService getWorkflowService()
	{
		return workflowService;
	}

	@Required
	public void setWorkflowService(final WorkflowService workflowService)
	{
		this.workflowService = workflowService;
	}

	protected WorkflowTemplateService getWorkflowTemplateService()
	{
		return workflowTemplateService;
	}

	@Required
	public void setWorkflowTemplateService(final WorkflowTemplateService workflowTemplateService)
	{
		this.workflowTemplateService = workflowTemplateService;
	}

	protected WorkflowProcessingService getWorkflowProcessingService()
	{
		return workflowProcessingService;
	}

	@Required
	public void setWorkflowProcessingService(final WorkflowProcessingService workflowProcessingService)
	{
		this.workflowProcessingService = workflowProcessingService;
	}

	protected WorkflowActionService getWorkflowActionService()
	{
		return workflowActionService;
	}

	@Required
	public void setWorkflowActionService(final WorkflowActionService workflowActionService)
	{
		this.workflowActionService = workflowActionService;
	}

	protected UserService getUserService()
	{
		return userService;
	}

	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	protected Converter<WorkflowModel, WorkflowData> getWorkflowConverter()
	{
		return workflowConverter;
	}

	@Required
	public void setWorkflowConverter(final Converter<WorkflowModel, WorkflowData> workflowConverter)
	{
		this.workflowConverter = workflowConverter;
	}

	protected Converter<WorkflowActionModel, WorkflowActionData> getWorkflowActionConverter()
	{
		return workflowActionConverter;
	}

	@Required
	public void setWorkflowActionConverter(final Converter<WorkflowActionModel, WorkflowActionData> workflowActionConverter)
	{
		this.workflowActionConverter = workflowActionConverter;
	}
}
