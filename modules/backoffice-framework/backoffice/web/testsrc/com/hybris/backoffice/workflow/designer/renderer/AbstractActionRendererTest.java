/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.renderer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.hybris.backoffice.workflow.designer.pojo.WorkflowEntity;
import com.hybris.cockpitng.components.visjs.network.data.Node;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.testing.util.CockpitTestUtil;
import com.hybris.cockpitng.testing.util.LabelLookup;
import com.hybris.cockpitng.testing.util.LabelLookupFactory;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.workflow.enums.WorkflowActionStatus;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;

import java.util.Date;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class AbstractActionRendererTest
{

	@Mock
	LabelService labelService;

	@InjectMocks
	AbstractActionRenderer renderer = new TestActionRenderer();

	@Test
	public void shouldEmptyOptionalBeReturnedWhenStatusCannotBeRetrieved()
	{
		// given
		final WorkflowActionTemplateModel actionTemplateModel = mock(WorkflowActionTemplateModel.class);

		// when
		final Optional<String> status = renderer.getActionStatus(actionTemplateModel);

		// then
		assertThat(status).isEmpty();
	}

	@Test
	public void shouldStatusBeRetrieved()
	{
		// given
		final WorkflowActionModel actionModel = mock(WorkflowActionModel.class);
		final WorkflowActionStatus status = WorkflowActionStatus.IN_PROGRESS;
		given(actionModel.getStatus()).willReturn(status);

		// when
		final Optional<String> returnedStatus = renderer.getActionStatus(actionModel);

		// then
		assertThat(returnedStatus).isPresent();
		assertThat(returnedStatus.get()).isEqualTo("actionInProgress");
	}

	@Test
	public void shouldCreateTitleForWorkflowActionModel()
	{
		// given
		final WorkflowActionModel actionModel = mock(WorkflowActionModel.class);
		final PrincipalModel principalAssigned = mock(PrincipalModel.class);
		final Date activated = new Date();
		given(actionModel.getPrincipalAssigned()).willReturn(principalAssigned);
		given(actionModel.getModifiedtime()).willReturn(activated);
		given(labelService.getObjectLabel(principalAssigned)).willReturn("admin");
		given(labelService.getObjectLabel(activated)).willReturn("22.01.2019 11:12");

		final LabelLookupFactory labelLookup = LabelLookupFactory.createLabelLookup();
		labelLookup.registerLabel(AbstractActionRenderer.ASSIGNED_USER_LABEL, "Assigned user:");
		labelLookup.registerLabel(AbstractActionRenderer.LAST_UPDATE_LABEL, "Last update:");
		CockpitTestUtil.mockLabelLookup(labelLookup);

		// when
		final String title = renderer.getTitle(actionModel);

		// then
		assertThat(title).isEqualTo("<strong>Last update:</strong>: 22.01.2019 11:12<br><strong>Assigned user:</strong>: admin");
	}

	@Test
	public void shouldCreateTitleForWorkflowActionTemplateModel()
	{
		// given
		final WorkflowActionTemplateModel actionModel = mock(WorkflowActionTemplateModel.class);
		given(actionModel.getName()).willReturn("Start");

		// when
		final String title = renderer.getTitle(actionModel);

		// then
		assertThat(title).isEqualTo("Start");
	}

	static class TestActionRenderer extends AbstractActionRenderer
	{

		@Override
		public boolean canHandle(final WorkflowEntity workflowEntity) {
			return false;
		}

		@Override
		public Node render(final WorkflowEntity workflowEntity) {
			return null;
		}

		@Override
		public Node render(final WorkflowEntity workflowEntity, final Node node) {
			return null;
		}
	}

}


