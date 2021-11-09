/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.link.LinkModel;
import de.hybris.platform.jalo.link.Link;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowDecisionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.backoffice.workflow.designer.persistence.LinkAttributeAccessor;
import com.hybris.backoffice.workflow.designer.pojo.WorkflowActionTemplate;
import com.hybris.backoffice.workflow.designer.pojo.WorkflowPojoMapper;
import com.hybris.backoffice.workflow.designer.pojo.WorkflowTemplate;
import com.hybris.backoffice.workflow.designer.renderer.NetworkEntityRenderer;
import com.hybris.cockpitng.components.visjs.network.data.Edge;
import com.hybris.cockpitng.components.visjs.network.data.Network;
import com.hybris.cockpitng.components.visjs.network.data.Node;


@RunWith(MockitoJUnitRunner.class)
public class DefaultWorkflowNetworkEntitiesFactoryTest
{

	@InjectMocks
	private DefaultWorkflowNetworkEntitiesFactory factory;

	@Test
	public void shouldGenerateEmptyNetworkForEmptyWorkflow()
	{
		// given
		final WorkflowTemplateModel model = mock(WorkflowTemplateModel.class);
		given(model.getActions()).willReturn(Collections.emptyList());
		final WorkflowTemplate workflowTemplate = new WorkflowTemplate(model);

		// when
		final Network network = factory.generateNetwork(workflowTemplate);

		// then
		assertThat(network.getNodes()).isEmpty();
		assertThat(network.getEdges()).isEmpty();
	}

	@Test
	public void shouldGenerateSingleActionNode()
	{
		// given
		final WorkflowTemplateModel model = mock(WorkflowTemplateModel.class);
		final WorkflowTemplate workflowTemplate = new WorkflowTemplate(model);

		final WorkflowActionTemplateModel action = mockAction("myAction", "My Action");
		given(action.getIncomingLinkTemplates()).willReturn(Collections.emptyList());
		given(action.getDecisionTemplates()).willReturn(Collections.emptyList());
		given(model.getActions()).willReturn(List.of(action));

		final Node generatedNode = mock(Node.class);

		final NetworkEntityRenderer renderer = prepareRendererForNode(action, generatedNode);
		factory.setNetworkEntityRenderers(Set.of(renderer));

		// when
		final Network network = factory.generateNetwork(workflowTemplate);

		// then
		assertThat(network.getNodes()).containsOnly(generatedNode);
		assertThat(network.getEdges()).isEmpty();
	}

	@Test
	public void shouldGenerateSingleActionAndSingleDecisionNodes()
	{
		// given
		final WorkflowTemplateModel model = mock(WorkflowTemplateModel.class);
		final WorkflowTemplate workflowTemplate = new WorkflowTemplate(model);

		final WorkflowActionTemplateModel action = mockAction("myAction", "My Action");
		given(action.getIncomingLinkTemplates()).willReturn(Collections.emptyList());
		given(model.getActions()).willReturn(List.of(action));

		final WorkflowDecisionTemplateModel decision = mockDecision("myDecision", "My Decision");
		given(action.getDecisionTemplates()).willReturn(List.of(decision));

		final Node generatedActionNode = mock(Node.class, "actionNode");
		final Node generatedDecisionNode = mock(Node.class, "decisionNode");

		final NetworkEntityRenderer actionRenderer = prepareRendererForNode(action, generatedActionNode);
		final NetworkEntityRenderer decisionRenderer = prepareRendererForNode(decision, generatedDecisionNode);
		factory.setNetworkEntityRenderers(Set.of(actionRenderer, decisionRenderer));

		// when
		final Network network = factory.generateNetwork(workflowTemplate);

		// then
		assertThat(network.getNodes()).containsOnly(generatedActionNode, generatedDecisionNode);
		assertThat(network.getEdges()).containsOnly(new Edge.Builder(generatedActionNode, generatedDecisionNode).build());
	}

	@Test
	public void shouldGenerateAndNode() throws JaloSecurityException
	{
		// given
		final WorkflowTemplateModel model = mock(WorkflowTemplateModel.class);
		final WorkflowTemplate workflowTemplate = new WorkflowTemplate(model);
		final LinkModel andConnection = mock(LinkModel.class);
		prepareAndLink(andConnection);

		final WorkflowActionTemplateModel action = mockAction("myAction", "My Action");
		given(action.getDecisionTemplates()).willReturn(Collections.emptyList());
		given(action.getIncomingLinkTemplates()).willReturn(List.of(andConnection));
		given(model.getActions()).willReturn(List.of(action));

		final Node generatedActionNode = mock(Node.class, "actionNode");
		final Node generatedAndNode = mock(Node.class, "andNode");

		final NetworkEntityRenderer actionRenderer = prepareRendererForNode(action, generatedActionNode);
		final NetworkEntityRenderer andConnectionRenderer = prepareRendererForNode(andConnection, generatedAndNode);
		factory.setNetworkEntityRenderers(Set.of(actionRenderer, andConnectionRenderer));

		// when
		final Network network = factory.generateNetwork(workflowTemplate);

		// then
		assertThat(network.getNodes()).containsOnly(generatedActionNode, generatedAndNode);
	}

	private void prepareAndLink(final LinkModel andConnection) throws JaloSecurityException
	{
		final ItemModelContext context = mock(ItemModelContext.class);
		given(andConnection.getItemModelContext()).willReturn(context);
		final Link link = mock(Link.class);
		given(context.getSource()).willReturn(link);
		given(link.getAttribute(LinkAttributeAccessor.AND_CONNECTION_TEMPLATE_PROPERTY)).willReturn(true);
	}

	@Test
	public void shouldGenerateNoEdgesForSingleAction()
	{
		// given
		final WorkflowTemplateModel model = mock(WorkflowTemplateModel.class);
		final WorkflowTemplate workflowTemplate = new WorkflowTemplate(model);
		final WorkflowActionTemplateModel action = mockAction("myAction", "My Action");
		given(action.getDecisionTemplates()).willReturn(Collections.emptyList());
		given(model.getActions()).willReturn(List.of(action));

		final Node generatedActionNode = mock(Node.class, "actionNode");

		final NetworkEntityRenderer actionRenderer = prepareRendererForNode(action, generatedActionNode);
		factory.setNetworkEntityRenderers(Set.of(actionRenderer));

		// when
		final Network network = factory.generateNetwork(workflowTemplate);

		// then
		assertThat(network.getNodes()).isNotEmpty();
		assertThat(network.getEdges()).isEmpty();
	}

	@Test
	public void shouldGenerateEdgesForActionToDecisionToAction()
	{
		// given
		final WorkflowActionTemplateModel fromActionModel = mockAction("fromAction", "From Action");
		final WorkflowActionTemplate fromActionWorkflow = new WorkflowActionTemplate(fromActionModel);

		final WorkflowActionTemplateModel toActionModel = mockAction("toAction", "To Action");
		final WorkflowActionTemplate toActionWorkflow = new WorkflowActionTemplate(toActionModel);

		final WorkflowDecisionTemplateModel toDecisionModel = mockDecision("toDecision", "To Decision");

		final WorkflowTemplateModel model = mock(WorkflowTemplateModel.class);
		final WorkflowTemplate workflowTemplate = new WorkflowTemplate(model);

		given(fromActionModel.getDecisionTemplates()).willReturn(List.of(toDecisionModel));
		given(toActionModel.getDecisionTemplates()).willReturn(List.of(toDecisionModel));

		given(model.getActions()).willReturn(List.of(fromActionModel, toActionModel));

		final Node generatedFromActionNode = mock(Node.class, "fromActionNode");
		final Node generatedToActionNode = mock(Node.class, "toActionNode");
		final Node generatedDecisionNode = mock(Node.class, "decisionNode");

		final NetworkEntityRenderer actionRenderer = mock(NetworkEntityRenderer.class);
		given(actionRenderer.canHandle(fromActionWorkflow)).willReturn(true);
		given(actionRenderer.render(fromActionWorkflow)).willReturn(generatedFromActionNode);
		given(actionRenderer.canHandle(toActionWorkflow)).willReturn(true);
		given(actionRenderer.render(toActionWorkflow)).willReturn(generatedToActionNode);

		final NetworkEntityRenderer decisionRenderer = prepareRendererForNode(toDecisionModel, generatedDecisionNode);
		factory.setNetworkEntityRenderers(Set.of(actionRenderer, decisionRenderer));

		// when
		final Network network = factory.generateNetwork(workflowTemplate);

		// then
		assertThat(network.getNodes()).hasSize(3);
		final Edge fromActionToDecisionEdge = new Edge.Builder(generatedFromActionNode, generatedDecisionNode).build();
		final Edge fromDecisionToActionEdge = new Edge.Builder(generatedDecisionNode, generatedToActionNode).build();
		assertThat(network.getEdges()).containsOnly(fromActionToDecisionEdge, fromDecisionToActionEdge);
	}

	@Test
	public void shouldGenerateEdgesForAndNode()
	{
		// given
		final WorkflowTemplateModel model = mock(WorkflowTemplateModel.class);
		final WorkflowTemplate workflowTemplate = new WorkflowTemplate(model);
		final WorkflowActionTemplateModel fromActionModel = mockAction("fromAction", "From Action");
		final WorkflowActionTemplate fromActionWorkflow = new WorkflowActionTemplate(fromActionModel);
		final WorkflowActionTemplateModel toActionModel = mockAction("toAction", "To Action");
		final WorkflowActionTemplate toActionWorkflow = new WorkflowActionTemplate(toActionModel);

		final LinkModel andConnection = mock(LinkModel.class, "andConnection");

		final WorkflowDecisionTemplateModel toDecision = mockDecision("toDecision", "To Decision");
		given(fromActionModel.getDecisionTemplates()).willReturn(List.of(toDecision));

		given(toActionModel.getDecisionTemplates()).willReturn(List.of(toDecision));
		given(toActionModel.getIncomingLinkTemplates()).willReturn(List.of(andConnection));

		given(model.getActions()).willReturn(List.of(fromActionModel, toActionModel));

		final Node generatedFromActionNode = mock(Node.class, "fromActionNode");
		final Node generatedToActionNode = mock(Node.class, "toActionNode");
		final Node generatedAndNode = mock(Node.class, "andNode");
		final Node generatedDecisionNode = mock(Node.class, "decisionNode");

		final NetworkEntityRenderer actionRenderer = mock(NetworkEntityRenderer.class);
		given(actionRenderer.canHandle(fromActionWorkflow)).willReturn(true);
		given(actionRenderer.render(fromActionWorkflow)).willReturn(generatedFromActionNode);
		given(actionRenderer.canHandle(toActionWorkflow)).willReturn(true);
		given(actionRenderer.render(toActionWorkflow)).willReturn(generatedToActionNode);

		final NetworkEntityRenderer decisionRenderer = prepareRendererForNode(toDecision, generatedDecisionNode);
		final NetworkEntityRenderer andConnectionRenderer = prepareRendererForNode(andConnection, generatedAndNode);
		factory.setNetworkEntityRenderers(Set.of(actionRenderer, decisionRenderer, andConnectionRenderer));

		// when
		final Network network = factory.generateNetwork(workflowTemplate);

		// then
		assertThat(network.getNodes()).hasSize(4);
		final Edge fromActionToDecisionEdge = new Edge.Builder(generatedFromActionNode, generatedDecisionNode).build();
		final Edge fromDecisionToAndEdge = new Edge.Builder(generatedDecisionNode, generatedAndNode).build();
		final Edge fromAndToActionEdge = new Edge.Builder(generatedAndNode, generatedToActionNode).build();
		assertThat(network.getEdges()).containsOnly(fromActionToDecisionEdge, fromDecisionToAndEdge, fromAndToActionEdge);
	}

	@Test
	public void shouldReuseAlreadyGeneratedAndNodeInsteadOfCreatingNewOne()
	{
		// given
		final WorkflowTemplateModel model = mock(WorkflowTemplateModel.class);
		final WorkflowTemplate workflowTemplate = new WorkflowTemplate(model);

		final LinkModel andConnectionFromFirstDecision = mock(LinkModel.class, "andConnectionFromFirstDecision");
		final LinkModel andConnectionFromSecondDecision = mock(LinkModel.class, "andConnectionFromSecondDecision");

		final WorkflowActionTemplateModel actionWithTwoAndLinksModel = mockAction("actionWithTwoAndLinks",
				"Action with two and links");
		final WorkflowActionTemplate actionWithTwoAndLinks = new WorkflowActionTemplate(actionWithTwoAndLinksModel);
		given(actionWithTwoAndLinksModel.getIncomingLinkTemplates())
				.willReturn(List.of(andConnectionFromFirstDecision, andConnectionFromSecondDecision));

		given(model.getActions()).willReturn(List.of(actionWithTwoAndLinksModel));

		final Node generatedActionNode = mock(Node.class, "actionNode");
		final Node firstAndNode = mock(Node.class, "firstAndNode");
		final Node secondAndNode = mock(Node.class, "secondAndNode");

		final NetworkEntityRenderer actionRenderer = mock(NetworkEntityRenderer.class);
		given(actionRenderer.canHandle(actionWithTwoAndLinks)).willReturn(true);
		given(actionRenderer.render(actionWithTwoAndLinks)).willReturn(generatedActionNode);

		final NetworkEntityRenderer firstAndConnectionRenderer = prepareRendererForNode(andConnectionFromFirstDecision,
				firstAndNode);
		final NetworkEntityRenderer secondAndConnectionRenderer = prepareRendererForNode(andConnectionFromFirstDecision,
				firstAndNode);

		factory.setNetworkEntityRenderers(Set.of(actionRenderer, firstAndConnectionRenderer, secondAndConnectionRenderer));

		// when
		final Network network = factory.generateNetwork(workflowTemplate);

		// then:
		assertThat(network.getNodes()).doesNotHaveDuplicates().hasSize(2).contains(generatedActionNode, firstAndNode)
				.doesNotContain(secondAndNode);
	}

	private NetworkEntityRenderer prepareRendererForNode(final ItemModel itemModel, final Node node)
	{
		final NetworkEntityRenderer renderer = mock(NetworkEntityRenderer.class);
		WorkflowPojoMapper.mapItemToWorkflowEntity(itemModel).ifPresent(entity -> {
			given(renderer.canHandle(entity)).willReturn(true);
			given(renderer.render(entity)).willReturn(node);
		});
		return renderer;
	}

	private WorkflowDecisionTemplateModel mockDecision(final String code, final String name)
	{
		final WorkflowDecisionTemplateModel decision = mock(WorkflowDecisionTemplateModel.class);
		given(decision.getCode()).willReturn(code);
		given(decision.getName()).willReturn(name);
		return decision;
	}

	private WorkflowActionTemplateModel mockAction(final String code, final String name)
	{
		final WorkflowActionTemplateModel action = mock(WorkflowActionTemplateModel.class);
		given(action.getCode()).willReturn(code);
		given(action.getName()).willReturn(name);
		return action;
	}

}
