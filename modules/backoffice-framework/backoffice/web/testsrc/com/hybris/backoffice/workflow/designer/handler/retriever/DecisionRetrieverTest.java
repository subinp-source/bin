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

import de.hybris.platform.workflow.model.WorkflowDecisionTemplateModel;

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
public class DecisionRetrieverTest
{
	@Mock
	NodeTypeService mockedNodeTypeService;

	@Mock
	WorkflowModelFinder mockedWorkflowModelFinder;

	@InjectMocks
	DecisionRetriever retriever;

	@Test
	public void shouldRetrieverBeIgnoredWhenNodeIsNotAnDecisionGroup()
	{
		// given
		final Node node = mock(Node.class);
		given(node.getGroup()).willReturn(WorkflowDesignerGroup.ACTION.getValue());

		// when
		final Optional<WorkflowDecisionTemplateModel> decision = retriever.retrieve(node, null);

		// then
		assertThat(decision).isEmpty();
	}

	@Test
	public void shouldRetrieveExistingDecision()
	{
		// given
		final Node node = mock(Node.class);
		given(node.getGroup()).willReturn(WorkflowDesignerGroup.DECISION.getValue());

		final NetworkChartContext context = mock(NetworkChartContext.class);

		final WorkflowDecisionTemplateModel decision = mock(WorkflowDecisionTemplateModel.class);

		given(mockedWorkflowModelFinder.findNewWorkflowDecision(context, node)).willReturn(Optional.empty());
		given(mockedWorkflowModelFinder.findWorkflowDecisionsFromWorkflowTemplateModel(context)).willReturn(Set.of(decision));
		given(mockedNodeTypeService.isSameDecision(decision, node)).willReturn(true);

		// when
		final Optional<WorkflowDecisionTemplateModel> optModel = retriever.retrieve(node, context);

		// then
		assertThat(optModel).isPresent().hasValue(decision);
	}

	@Test
	public void shouldRetrieveNewlyCreatedDecision()
	{
		// given
		final Node node = mock(Node.class);
		given(node.getGroup()).willReturn(WorkflowDesignerGroup.DECISION.getValue());
		final NetworkChartContext context = mock(NetworkChartContext.class);
		final WorkflowDecisionTemplateModel decision = mock(WorkflowDecisionTemplateModel.class);

		given(mockedWorkflowModelFinder.findNewWorkflowDecision(context, node)).willReturn(Optional.of(decision));

		// when
		final Optional<WorkflowDecisionTemplateModel> optModel = retriever.retrieve(node, context);

		// then
		assertThat(optModel).isPresent().hasValue(decision);
		then(mockedWorkflowModelFinder).should(never()).findWorkflowDecisionsFromWorkflowTemplateModel(any());
	}
}
