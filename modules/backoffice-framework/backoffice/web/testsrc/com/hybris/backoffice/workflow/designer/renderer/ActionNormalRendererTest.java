/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.renderer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.workflow.enums.WorkflowActionType;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.backoffice.workflow.designer.handler.connection.WorkflowDesignerGroup;
import com.hybris.backoffice.workflow.designer.pojo.WorkflowActionTemplate;
import com.hybris.backoffice.workflow.designer.pojo.WorkflowDecisionTemplate;
import com.hybris.backoffice.workflow.designer.services.NodeTypeService;
import com.hybris.cockpitng.components.visjs.network.data.ChosenNode;
import com.hybris.cockpitng.components.visjs.network.data.Node;
import com.hybris.cockpitng.components.visjs.network.data.WidthConstraint;


@RunWith(MockitoJUnitRunner.class)
public class ActionNormalRendererTest
{
	private static final String GENERATED_ID = "generatedId";
	private static final String NODE_CODE = "nodeCode";
	private static final String NODE_NAME = "nodeName";
	private static final String NODE_DATA = "node data";

	@Mock
	KeyGenerator mockedKeyGenerator;
	@Mock
	NodeLabelMapper mockedNodeLabelMapper;
	@Mock
	NodeTypeService mockedNodeTypeService;

	@InjectMocks
	ActionNormalRenderer actionNormalRenderer;

	@Before
	public void setUp()
	{
		given(mockedNodeLabelMapper.apply(any())).willAnswer(a -> a.getArguments()[0]);
	}

	@Test
	public void shouldHandleNormalActionModel()
	{
		// given
		final WorkflowActionTemplateModel normalAction = prepareActionMockOfType(WorkflowActionType.NORMAL);
		final WorkflowActionTemplate workflowActionTemplate = new WorkflowActionTemplate(normalAction);

		// when
		final boolean canHandle = actionNormalRenderer.canHandle(workflowActionTemplate);

		// then
		assertThat(canHandle).isTrue();
	}

	@Test
	public void shouldNotHandleActionOfTypeDifferentThanNormal()
	{
		// given
		final WorkflowActionTemplateModel endAction = prepareActionMockOfType(WorkflowActionType.END);
		final WorkflowActionTemplate workflowActionTemplate = new WorkflowActionTemplate(endAction);

		// when
		final boolean canHandle = actionNormalRenderer.canHandle(workflowActionTemplate);

		// then
		assertThat(canHandle).isFalse();
	}

	@Test
	public void shouldNotHandleOtherObjectsThanNormalAction()
	{
		// given
		final WorkflowDecisionTemplate workflowDecisionTemplate = mock(WorkflowDecisionTemplate.class);

		// when
		final boolean canHandle = actionNormalRenderer.canHandle(workflowDecisionTemplate);

		// then
		assertThat(canHandle).isFalse();
	}

	@Test
	public void shouldRenderNormalAction()
	{
		// given
		final int visualizationX = 10;
		final int visualizationY = 20;

		final WorkflowActionTemplateModel action = prepareActionMockOfType(WorkflowActionType.END);
		final WorkflowActionTemplate workflowActionTemplate = new WorkflowActionTemplate(action);
		given(action.getVisualisationX()).willReturn(visualizationX);
		given(action.getVisualisationY()).willReturn(visualizationY);

		given(mockedKeyGenerator.generateFor(action)).willReturn(GENERATED_ID);
		given(mockedNodeTypeService.generateNodeData(action)).willReturn(NODE_DATA);

		// when
		final Node node = actionNormalRenderer.render(workflowActionTemplate);

		// then
		final WidthConstraint expectedWidth = new WidthConstraint.Builder().withMinimum(100).build();
		final ChosenNode expectedChosen = new ChosenNode.Builder().withNode(String.valueOf(true)).build();
		final Node expectedNode = new Node.Builder().withId(GENERATED_ID).withX(visualizationX).withY(visualizationY)
				.withLabel(NODE_NAME).withData(NODE_DATA).withGroup(WorkflowDesignerGroup.ACTION.getValue()).withTitle(NODE_NAME)
				.withWidthConstraint(expectedWidth).build();
		assertThat(node).isEqualToIgnoringGivenFields(expectedNode, "widthConstraint", "chosen");
		assertThat(node.getWidthConstraint()).isEqualToComparingFieldByField(expectedWidth);
		assertThat(node.getChosen()).isEqualToComparingFieldByField(expectedChosen);
	}

	private WorkflowActionTemplateModel prepareActionMockOfType(final WorkflowActionType type)
	{
		final WorkflowActionTemplateModel workflowActionTemplateModel = mock(WorkflowActionTemplateModel.class);
		given(workflowActionTemplateModel.getActionType()).willReturn(type);
		given(workflowActionTemplateModel.getCode()).willReturn(NODE_CODE);
		given(workflowActionTemplateModel.getName()).willReturn(NODE_NAME);

		return workflowActionTemplateModel;
	}
}
