/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.renderer;

import static com.hybris.backoffice.workflow.designer.renderer.ActionEndRenderer.VELOCITY_IE_TEMPLATE_LOCATION;
import static com.hybris.backoffice.workflow.designer.renderer.ActionEndRenderer.VELOCITY_TEMPLATE_LOCATION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;

import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.workflow.enums.WorkflowActionStatus;
import de.hybris.platform.workflow.enums.WorkflowActionType;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.backoffice.workflow.designer.handler.connection.WorkflowDesignerGroup;
import com.hybris.backoffice.workflow.designer.pojo.WorkflowActionInstance;
import com.hybris.backoffice.workflow.designer.pojo.WorkflowActionTemplate;
import com.hybris.backoffice.workflow.designer.pojo.WorkflowLink;
import com.hybris.backoffice.workflow.designer.services.NodeTypeService;
import com.hybris.cockpitng.components.visjs.network.data.Image;
import com.hybris.cockpitng.components.visjs.network.data.Node;


@RunWith(MockitoJUnitRunner.class)
public class ActionEndRendererTest
{
	private static final String GENERATED_ID = "generatedId";
	private static final String NODE_CODE = "nodeCode";
	private static final String NODE_DATA = "nodeData";

	@Mock
	KeyGenerator mockedKeyGenerator;
	@Mock
	WorkflowEntityImageCreator mockedWorkflowEntityImageCreator;
	@Mock
	NodeTypeService mockedNodeTypeService;

	@InjectMocks
	ActionEndRenderer actionEndRenderer;

	@Test
	public void shouldHandleEndActionModel()
	{
		// given
		final WorkflowActionTemplateModel endAction = prepareActionMockOfType(WorkflowActionType.END);
		final WorkflowActionTemplate workflowActionTemplate = new WorkflowActionTemplate(endAction);

		// when
		final boolean canHandle = actionEndRenderer.canHandle(workflowActionTemplate);

		// then
		assertThat(canHandle).isTrue();
	}

	@Test
	public void shouldNotHandleActionOfTypeDifferentThanEnd()
	{
		// given
		final WorkflowActionTemplateModel normalAction = prepareActionMockOfType(WorkflowActionType.NORMAL);
		final WorkflowActionTemplate workflowActionTemplate = new WorkflowActionTemplate(normalAction);

		// when
		final boolean canHandle = actionEndRenderer.canHandle(workflowActionTemplate);

		// then
		assertThat(canHandle).isFalse();
	}

	@Test
	public void shouldNotHandleOtherObjectsThanEndAction()
	{
		// given
		final WorkflowLink workflowLink = mock(WorkflowLink.class);

		// when
		final boolean canHandle = actionEndRenderer.canHandle(workflowLink);

		// then
		assertThat(canHandle).isFalse();
	}

	@Test
	public void shouldRenderEndAction()
	{
		// given
		final int visualizationX = 10;
		final int visualizationY = 20;

		final Image image = mock(Image.class);

		final WorkflowActionTemplateModel action = prepareActionMockOfType(WorkflowActionType.END);
		final WorkflowActionTemplate workflowActionTemplate = new WorkflowActionTemplate(action);
		given(action.getVisualisationX()).willReturn(visualizationX);
		given(action.getVisualisationY()).willReturn(visualizationY);

		given(mockedKeyGenerator.generateFor(action)).willReturn(GENERATED_ID);
		given(mockedWorkflowEntityImageCreator.createSvgImage(eq(action), eq(VELOCITY_TEMPLATE_LOCATION),
				eq(VELOCITY_IE_TEMPLATE_LOCATION), anyString())).willReturn(image);
		given(mockedNodeTypeService.generateNodeData(action)).willReturn(NODE_DATA);

		// when
		final Node node = actionEndRenderer.render(workflowActionTemplate);

		// then
		final Node expectedNode = new Node.Builder().withId(GENERATED_ID).withX(visualizationX).withY(visualizationY)
				.withData(NODE_DATA).withGroup(WorkflowDesignerGroup.END_ACTION.getValue()).withShape("image").withImage(image)
				.withTitle(String.format("[%s]", NODE_CODE)).build();
		assertThat(node).isEqualToComparingFieldByField(expectedNode);
	}

	private WorkflowActionTemplateModel prepareActionMockOfType(final WorkflowActionType type)
	{
		final WorkflowActionTemplateModel workflowActionTemplateModel = mock(WorkflowActionTemplateModel.class);
		given(workflowActionTemplateModel.getActionType()).willReturn(type);
		given(workflowActionTemplateModel.getCode()).willReturn(NODE_CODE);

		return workflowActionTemplateModel;
	}

	@Test
	public void shouldSpecificCssClassBeUsedForActionEndSVG()
	{
		// given
		final WorkflowActionModel model = mock(WorkflowActionModel.class);
		final WorkflowActionStatus status = WorkflowActionStatus.IN_PROGRESS;
		given(model.getStatus()).willReturn(status);

		// when
		actionEndRenderer.render(new WorkflowActionInstance(model));

		// then
		then(mockedWorkflowEntityImageCreator).should().createSvgImage(eq(model), eq(VELOCITY_TEMPLATE_LOCATION),
				eq(VELOCITY_IE_TEMPLATE_LOCATION), argThat(new ArgumentMatcher<>()
				{
					@Override
					public boolean matches(final Object o)
					{
						if (o instanceof String)
						{
							return StringUtils.equals((String) o, "in-progress");
						}

						return false;
					}
				}));
	}

}
