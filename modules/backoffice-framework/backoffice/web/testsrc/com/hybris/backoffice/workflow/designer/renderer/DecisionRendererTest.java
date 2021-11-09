/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.renderer;

import static com.hybris.backoffice.workflow.designer.renderer.DecisionRenderer.VELOCITY_IE_TEMPLATE_LOCATION;
import static com.hybris.backoffice.workflow.designer.renderer.DecisionRenderer.VELOCITY_TEMPLATE_LOCATION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;

import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.workflow.model.WorkflowDecisionTemplateModel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.backoffice.workflow.designer.handler.connection.WorkflowDesignerGroup;
import com.hybris.backoffice.workflow.designer.pojo.WorkflowActionInstance;
import com.hybris.backoffice.workflow.designer.pojo.WorkflowDecisionTemplate;
import com.hybris.backoffice.workflow.designer.services.NodeTypeService;
import com.hybris.cockpitng.components.visjs.network.data.Image;
import com.hybris.cockpitng.components.visjs.network.data.Node;


@RunWith(MockitoJUnitRunner.class)
public class DecisionRendererTest
{
	private static final String MODEL_CODE = "workflow decision code";
	private static final String GENERATED_ID = "generatedId";
	private static final String NODE_DATA = "node data";
	@Mock
	KeyGenerator mockedKeyGenerator;
	@Mock
	WorkflowEntityImageCreator mockedWorkflowEntityImageCreator;
	@Mock
	NodeTypeService mockedNodeTypeService;

	@InjectMocks
	DecisionRenderer decisionRenderer;

	@Test
	public void shouldHandleDecisionModel()
	{
		// given
		final WorkflowDecisionTemplateModel model = mock(WorkflowDecisionTemplateModel.class);
		final WorkflowDecisionTemplate workflowDecisionTemplate = new WorkflowDecisionTemplate(model);

		// when
		final boolean result = decisionRenderer.canHandle(workflowDecisionTemplate);

		// then
		assertThat(result).isTrue();
	}

	@Test
	public void shouldNotHandleObjectsOtherThanDecisionModel()
	{
		// given
		final WorkflowActionInstance workflowActionInstance = mock(WorkflowActionInstance.class);

		// when
		final boolean result = decisionRenderer.canHandle(workflowActionInstance);

		// then
		assertThat(result).isFalse();
	}

	@Test
	public void shouldRenderDecisionDto()
	{
		// given
		final int visualizationX = 10;
		final int visualizationY = 20;
		final Image image = mock(Image.class);

		final WorkflowDecisionTemplateModel model = mock(WorkflowDecisionTemplateModel.class);
		final WorkflowDecisionTemplate decision = new WorkflowDecisionTemplate(model);
		given(model.getCode()).willReturn(MODEL_CODE);
		given(model.getVisualisationX()).willReturn(visualizationX);
		given(model.getVisualisationY()).willReturn(visualizationY);

		given(mockedKeyGenerator.generateFor(model)).willReturn(GENERATED_ID);
		given(mockedWorkflowEntityImageCreator.createSvgImage(eq(model), eq(VELOCITY_TEMPLATE_LOCATION),
				eq(VELOCITY_IE_TEMPLATE_LOCATION), anyString())).willReturn(image);
		given(mockedNodeTypeService.generateNodeData(model)).willReturn(NODE_DATA);

		// when
		final Node node = decisionRenderer.render(decision);

		// then
		final Node expectedNode = new Node.Builder().withId(GENERATED_ID).withX(visualizationX).withY(visualizationY)
				.withData(NODE_DATA).withGroup(WorkflowDesignerGroup.DECISION.getValue()).withImage(image)
				.withTitle(String.format("[%s]", MODEL_CODE)).build();
		assertThat(node).isEqualToComparingFieldByField(expectedNode);
	}

	@Test
	public void shouldNoSpecificCssClassBeUsedForDecisionSVG()
	{
		// given
		final WorkflowDecisionTemplateModel model = mock(WorkflowDecisionTemplateModel.class);

		// when
		decisionRenderer.render(new WorkflowDecisionTemplate(model));

		// then
		then(mockedWorkflowEntityImageCreator).should().createSvgImage(eq(model), eq(VELOCITY_TEMPLATE_LOCATION),
				eq(VELOCITY_IE_TEMPLATE_LOCATION), argThat(new ArgumentMatcher<>()
				{
					@Override
					public boolean matches(final Object o)
					{
						if (o instanceof String)
						{
							return ((String) o).isEmpty();
						}

						return false;
					}
				}));
	}

}
