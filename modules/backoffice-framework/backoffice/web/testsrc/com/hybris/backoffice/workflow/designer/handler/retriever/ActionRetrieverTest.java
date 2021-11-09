/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.handler.retriever;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;

import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;

import java.util.Optional;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import com.hybris.backoffice.workflow.designer.handler.connection.WorkflowDesignerGroup;
import com.hybris.backoffice.workflow.designer.services.NodeTypeService;
import com.hybris.backoffice.workflow.designer.services.WorkflowModelFinder;
import com.hybris.cockpitng.components.visjs.network.data.Node;


@RunWith(MockitoJUnitRunner.class)
public class ActionRetrieverTest
{
	@Mock
	NodeTypeService mockedNodeTypeService;

	@Mock
	WorkflowModelFinder mockedWorkflowModelFinder;

	@InjectMocks
	ActionRetriever retriever;

	@Test
	public void shouldRetrieverBeIgnoredWhenNodeIsNotAnActionGroup()
	{
		// given
		final Node node = mock(Node.class);
		given(node.getGroup()).willReturn(WorkflowDesignerGroup.DECISION.getValue());

		// when
		final Optional<WorkflowActionTemplateModel> action = retriever.retrieve(node, null);

		// then
		assertThat(action).isEmpty();
	}

	@Test
	public void shouldRetrieveNewlyCreatedAction()
	{
		// given
		final Node node = mock(Node.class);
		given(node.getGroup()).willReturn(WorkflowDesignerGroup.ACTION.getValue());

		final NetworkChartContext context = mock(NetworkChartContext.class);

		final WorkflowActionTemplateModel action = mock(WorkflowActionTemplateModel.class);

		given(mockedWorkflowModelFinder.findNewWorkflowAction(context, node)).willReturn(Optional.of(action));

		// when
		final Optional<WorkflowActionTemplateModel> optModel = retriever.retrieve(node, context);

		// then
		assertThat(optModel).isPresent().hasValue(action);
		then(mockedWorkflowModelFinder).should(never()).findWorkflowActionsFromWorkflowTemplateModel(any());
	}

	@Test
	public void shouldRetrieveExistingAction()
	{
		// given
		final Node node = mock(Node.class);
		given(node.getGroup()).willReturn(WorkflowDesignerGroup.ACTION.getValue());

		final NetworkChartContext context = mock(NetworkChartContext.class);

		final WorkflowActionTemplateModel action = mock(WorkflowActionTemplateModel.class);

		given(mockedWorkflowModelFinder.findNewWorkflowAction(context, node)).willReturn(Optional.empty());
		given(mockedWorkflowModelFinder.findWorkflowActionsFromWorkflowTemplateModel(context)).willReturn(Set.of(action));
		given(mockedNodeTypeService.isSameAction(action, node)).willReturn(true);

		// when
		final Optional<WorkflowActionTemplateModel> optModel = retriever.retrieve(node, context);

		// then
		assertThat(optModel).isPresent().hasValue(action);
	}
}
