/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.workflow.populator;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cmsfacades.data.CMSWorkflowActionData;
import de.hybris.platform.cmsfacades.data.CMSWorkflowData;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.workflow.model.AutomatedWorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.base.Predicate;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class CMSWorkflowWithActionsDataPopulatorTest
{
	private final String WORKFLOW_CODE = "some code";
	private final String WORKFLOW_TEMPLATE_CODE = "some template code";

	@Mock
	private WorkflowModel workflowModel;

	@Mock
	private WorkflowActionModel workflowAction1;

	@Mock
	private WorkflowActionModel workflowAction2;

	@Mock
	private WorkflowTemplateModel workflowTemplate;

	@Mock
	private WorkflowActionTemplateModel actionTemplate;

	@Mock
	private AutomatedWorkflowActionTemplateModel automatedActionTemplate;

	@Mock
	private CMSWorkflowActionData workflowActionData1;

	@Mock
	private CMSWorkflowActionData workflowActionData2;

	@Mock
	private Converter<WorkflowActionModel, CMSWorkflowActionData> cmsWorkflowActionDataConverter;
	@Mock
	private Predicate<WorkflowActionModel> automatedWorkflowActionTypePredicate;
	@Mock
	private Predicate<WorkflowActionModel> negatePredicate;

	private CMSWorkflowData workflowData;

	@InjectMocks
	private CMSWorkflowWithActionsDataPopulator cmsWorkflowActionsDataPopulator;

	@Before
	public void setUp()
	{
		workflowData = new CMSWorkflowData();

		when(workflowModel.getCode()).thenReturn(WORKFLOW_CODE);
		when(workflowModel.getJob()).thenReturn(workflowTemplate);
		when(workflowModel.getActions()).thenReturn(Arrays.asList(workflowAction1, workflowAction2));
		when(workflowTemplate.getCode()).thenReturn(WORKFLOW_TEMPLATE_CODE);

		when(workflowAction1.getTemplate()).thenReturn(actionTemplate);
		when(workflowAction2.getTemplate()).thenReturn(actionTemplate);
		when(cmsWorkflowActionDataConverter.convert(workflowAction1)).thenReturn(workflowActionData1);
		when(cmsWorkflowActionDataConverter.convert(workflowAction2)).thenReturn(workflowActionData2);

		when(automatedWorkflowActionTypePredicate.negate()).thenReturn(negatePredicate);
	}

	@Test
	public void testPopulateAllFields()
	{
		// GIVEN
		when(workflowAction1.getTemplate()).thenReturn(actionTemplate);
		when(workflowAction2.getTemplate()).thenReturn(actionTemplate);
		when(negatePredicate.test(any())).thenReturn(true);

		// WHEN
		cmsWorkflowActionsDataPopulator.populate(workflowModel, workflowData);

		// THEN
		assertThat(workflowData.getWorkflowCode(), equalTo(WORKFLOW_CODE));
		assertThat(workflowData.getTemplateCode(), equalTo(WORKFLOW_TEMPLATE_CODE));
		assertThat(workflowData.getActions(), hasItems(workflowActionData1, workflowActionData2));
	}

	@Test
	public void testPopulateActionsWithNonAutomatedActions()
	{
		// GIVEN
		when(workflowAction1.getTemplate()).thenReturn(actionTemplate);
		when(workflowAction2.getTemplate()).thenReturn(automatedActionTemplate);
		when(negatePredicate.test(workflowAction1)).thenReturn(true);
		when(negatePredicate.test(workflowAction2)).thenReturn(false);

		// WHEN
		cmsWorkflowActionsDataPopulator.populate(workflowModel, workflowData);

		// THEN
		assertThat(workflowData.getWorkflowCode(), equalTo(WORKFLOW_CODE));
		assertThat(workflowData.getTemplateCode(), equalTo(WORKFLOW_TEMPLATE_CODE));
		assertThat(workflowData.getActions(), hasItems(workflowActionData1));
	}

}
