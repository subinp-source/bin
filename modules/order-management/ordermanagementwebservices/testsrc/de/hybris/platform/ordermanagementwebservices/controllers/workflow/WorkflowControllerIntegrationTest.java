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
package de.hybris.platform.ordermanagementwebservices.controllers.workflow;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.oauth2.constants.OAuth2Constants;
import de.hybris.platform.ordermanagementfacades.workflow.OmsWorkflowFacade;
import de.hybris.platform.ordermanagementwebservices.constants.OrdermanagementwebservicesConstants;
import de.hybris.platform.ordermanagementwebservices.dto.workflow.WorkflowActionListWsDto;
import de.hybris.platform.ordermanagementwebservices.dto.workflow.WorkflowCodesListWsDto;
import de.hybris.platform.ordermanagementwebservices.util.BaseOrderManagementWebservicesIntegrationTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.webservicescommons.testsupport.server.NeedsEmbeddedServer;
import de.hybris.platform.workflow.WorkflowService;
import de.hybris.platform.workflow.WorkflowTemplateService;
import de.hybris.platform.workflow.enums.WorkflowActionStatus;
import de.hybris.platform.workflow.model.WorkflowModel;

import javax.annotation.Resource;
import javax.ws.rs.core.Response;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.fest.util.Strings;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static de.hybris.platform.ordermanagementwebservices.constants.OrdermanagementwebservicesConstants.DEFAULT_ENCODING;
import static de.hybris.platform.webservicescommons.testsupport.client.WebservicesAssert.assertResponse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


@NeedsEmbeddedServer(webExtensions = { OrdermanagementwebservicesConstants.EXTENSIONNAME, OAuth2Constants.EXTENSIONNAME })
@IntegrationTest
public class WorkflowControllerIntegrationTest extends BaseOrderManagementWebservicesIntegrationTest
{
	protected static final String WORKFLOW_TEMPLATE_CODE = "TestWorkflowTemplate";
	protected static final String WORKFLOW_NAME = "workflowName";
	protected static final String WORKFLOW_ACTION_NAME = "Created";
	protected static final String DECISION_CODE = "decide";

	@Resource
	private OmsWorkflowFacade omsWorkflowFacade;
	@Resource
	private WorkflowService newestWorkflowService;
	@Resource
	private WorkflowTemplateService workflowTemplateService;
	@Resource
	private FlexibleSearchService flexibleSearchService;
	@Resource
	private UserService userService;
	@Resource
	private ModelService modelService;

	@Before
	public void setup()
	{
		try
		{
			importCsv("/test/AuthorizationGroups.csv", DEFAULT_ENCODING);
			importCsv("/test/WorkflowTestData.csv", DEFAULT_ENCODING);
		}
		catch (final ImpExException e)
		{
			e.printStackTrace();
		}
	}

	@After
	public void cleanUpData()
	{
		cleanUpModel("Workflow");
		cleanUpModel("WorkflowAction");
		cleanUpModel("WorkflowDecision");
	}

	@Test
	public void testPostDecideAction()
	{
		//given
		final Calendar earlyDate = Calendar.getInstance();
		earlyDate.add(Calendar.YEAR, -1);
		getOmsWorkflowFacade().startWorkflow(getUserService().getAdminUser(), WORKFLOW_NAME, WORKFLOW_TEMPLATE_CODE,
				getUserService().getAdminUserGroup().getUid());

		final List<WorkflowModel> workflows = getNewestWorkflowService()
				.getWorkflowsForTemplateAndUser(getWorkflowTemplateService().getWorkflowTemplateForCode(WORKFLOW_TEMPLATE_CODE),
						getUserService().getAdminUser());

		//when
		final Response result = postDecideWorkflowAction(workflows.get(0).getCode(), DECISION_CODE);

		//then
		assertResponse(Response.Status.OK, Optional.empty(), result);
	}

	@Test
	public void testPostDecideActions()
	{
		//given
		getOmsWorkflowFacade().startWorkflow(getUserService().getAdminUser(), WORKFLOW_NAME, WORKFLOW_TEMPLATE_CODE,
				getUserService().getAdminUserGroup().getUid());
		getOmsWorkflowFacade().startWorkflow(getUserService().getAdminUser(), WORKFLOW_NAME, WORKFLOW_TEMPLATE_CODE,
				getUserService().getAdminUserGroup().getUid());

		final List<WorkflowModel> workflows = getNewestWorkflowService()
				.getWorkflowsForTemplateAndUser(getWorkflowTemplateService().getWorkflowTemplateForCode(WORKFLOW_TEMPLATE_CODE),
						getUserService().getAdminUser());
		final List<String> workflowCodes = workflows.stream().map(WorkflowModel::getCode).collect(Collectors.toList());
		final WorkflowCodesListWsDto workflowCodesListWsDto = new WorkflowCodesListWsDto();
		workflowCodesListWsDto.setCodes(workflowCodes);

		//when
		final Response result = postDecideWorkflowActions(workflowCodesListWsDto, DECISION_CODE);

		//then
		assertResponse(Response.Status.OK, Optional.empty(), result);
	}

	@Test
	public void testGetWorkflowActions()
	{
		//given
		getOmsWorkflowFacade().startWorkflow(getUserService().getAdminUser(), WORKFLOW_NAME, WORKFLOW_TEMPLATE_CODE,
				getUserService().getAdminUserGroup().getUid());

		//when
		final WorkflowActionListWsDto workflowActionListWsDto = getWorkflowActions();

		//then
		assertNotNull(workflowActionListWsDto.getWorkflowActions());
		assertEquals(1, workflowActionListWsDto.getWorkflowActions().size());
		assertTrue(workflowActionListWsDto.getWorkflowActions().stream().noneMatch(action -> Strings.isEmpty(action.getCode())));
		assertTrue(workflowActionListWsDto.getWorkflowActions().stream()
				.anyMatch(action -> WORKFLOW_ACTION_NAME.equals(action.getName())));
	}

	/**
	 * Removes all models with the provided name
	 *
	 * @param modelName
	 * 		the models to remove
	 */
	protected void cleanUpModel(final String modelName)
	{
		try
		{
			SearchResult<FlexibleSearchQuery> result = getFlexibleSearchService().search("SELECT {pk} FROM {" + modelName + "}");
			if (result.getCount() != 0)
				getModelService().removeAll(result.getResult());
		}
		catch (final NullPointerException e)
		{
			//do nothing
		}
	}

	protected OmsWorkflowFacade getOmsWorkflowFacade()
	{
		return omsWorkflowFacade;
	}

	protected WorkflowService getNewestWorkflowService()
	{
		return newestWorkflowService;
	}

	protected UserService getUserService()
	{
		return userService;
	}

	protected WorkflowTemplateService getWorkflowTemplateService()
	{
		return workflowTemplateService;
	}

	protected FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}
}
