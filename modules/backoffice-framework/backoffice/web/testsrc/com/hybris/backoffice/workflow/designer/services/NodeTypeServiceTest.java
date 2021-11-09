/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.services;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import de.hybris.platform.core.PK;
import com.hybris.backoffice.workflow.designer.handler.connection.WorkflowDesignerGroup;
import com.hybris.backoffice.workflow.designer.services.NodeTypeService;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowDecisionTemplateModel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.cockpitng.components.visjs.network.data.Node;


@RunWith(MockitoJUnitRunner.class)
public class NodeTypeServiceTest
{

	@InjectMocks
	private NodeTypeService nodeTypeService;

	@Test
	public void shouldRecognizeNormalAction()
	{
		// given
		final Node node = new Node.Builder().withGroup(WorkflowDesignerGroup.ACTION.getValue()).build();

		// when/then
		assertThat(nodeTypeService.isAction(node)).isTrue();
		assertThat(nodeTypeService.isDecision(node)).isFalse();
		assertThat(nodeTypeService.isAnd(node)).isFalse();
	}

	@Test
	public void shouldRecognizeDecision()
	{
		// given
		final Node node = new Node.Builder().withGroup(WorkflowDesignerGroup.DECISION.getValue()).build();

		// when/then
		assertThat(nodeTypeService.isAction(node)).isFalse();
		assertThat(nodeTypeService.isDecision(node)).isTrue();
		assertThat(nodeTypeService.isAnd(node)).isFalse();
	}

	@Test
	public void shouldRecognizeAndNode()
	{
		// given
		final Node node = new Node.Builder().withGroup(WorkflowDesignerGroup.AND.getValue()).build();

		// when/then
		assertThat(nodeTypeService.isAction(node)).isFalse();
		assertThat(nodeTypeService.isDecision(node)).isFalse();
		assertThat(nodeTypeService.isAnd(node)).isTrue();
	}

	@Test
	public void shouldRecognizeSameUnsavedAction()
	{
		// given
		final String actionCode = "actionCode";
		final WorkflowActionTemplateModel actionModel = mock(WorkflowActionTemplateModel.class);
		final String nodeData = "action:unsaved[code=actionCode]";
		final Node node = new Node.Builder().withGroup(WorkflowDesignerGroup.ACTION.getValue()).withData(nodeData).build();
		given(actionModel.getCode()).willReturn(actionCode);

		// when/then
		assertThat(nodeTypeService.isSameAction(actionModel, node)).isTrue();
	}

	@Test
	public void shouldRecognizeSameSavedActon()
	{
		// given
		final String actionCode = "actionCode";
		final WorkflowActionTemplateModel actionModel = mock(WorkflowActionTemplateModel.class);
		given(actionModel.getPk()).willReturn(PK.fromLong(1L));

		final String nodeData = "action:saved[pk=1,code=actionCode]";
		final Node node = new Node.Builder().withGroup(WorkflowDesignerGroup.ACTION.getValue()).withData(nodeData).build();
		given(actionModel.getCode()).willReturn(actionCode);

		// when/then
		assertThat(nodeTypeService.isSameAction(actionModel, node)).isTrue();
	}

	@Test
	public void shouldRecognizeSameUnsavedDecision()
	{
		// given
		final String decisionCode = "decisionCode";
		final WorkflowDecisionTemplateModel decisionModel = mock(WorkflowDecisionTemplateModel.class);
		final String nodeData = "decision:unsaved[code=decisionCode]";
		final Node node = new Node.Builder().withGroup(WorkflowDesignerGroup.DECISION.getValue()).withData(nodeData).build();
		given(decisionModel.getCode()).willReturn(decisionCode);

		// when/then
		assertThat(nodeTypeService.isSameDecision(decisionModel, node)).isTrue();
	}

	@Test
	public void shouldRecognizeSameSavedDecision()
	{
		// given
		final String decisionCode = "decisionCode";
		final WorkflowDecisionTemplateModel decisionModel = mock(WorkflowDecisionTemplateModel.class);
		given(decisionModel.getPk()).willReturn(PK.fromLong(1L));

		final String nodeData = "decision:saved[pk=1,code=decisionCode]";
		final Node node = new Node.Builder().withGroup(WorkflowDesignerGroup.DECISION.getValue()).withData(nodeData).build();
		given(decisionModel.getCode()).willReturn(decisionCode);

		// when/then
		assertThat(nodeTypeService.isSameDecision(decisionModel, node)).isTrue();
	}

	@Test
	public void shouldCompareCodeFromSavedDecisionNode()
	{
		// given
		final Node node = new Node.Builder().withData("decision:saved[pk=1,code=decisionCode]").build();

		// when
		final boolean result = nodeTypeService.hasCode(node, "decisionCode");

		// then
		assertThat(result).isTrue();
	}

	@Test
	public void shouldCompareCodeFromUnsavedDecisionNode()
	{
		// given
		final Node node = new Node.Builder().withData("decision:unsaved[code=decisionCode]").build();

		// when
		final boolean result = nodeTypeService.hasCode(node, "decisionCode");

		// then
		assertThat(result).isTrue();
	}

	@Test
	public void shouldCompareNegativelyCodeFromUnsavedDecisionNode()
	{
		// given
		final Node node = new Node.Builder().withData("decision:unsaved[code=decisionCode]").build();

		// when
		final boolean result = nodeTypeService.hasCode(node, "otherDecisionCode");

		// then
		assertThat(result).isFalse();
	}

	@Test
	public void shouldCompareNegativelyCodeFromSavedDecisionNode()
	{
		// given
		final Node node = new Node.Builder().withData("decision:saved[pk=1,code=decisionCode]").build();

		// when
		final boolean result = nodeTypeService.hasCode(node, "otherDecisionCode");

		// then
		assertThat(result).isFalse();
	}

	@Test
	public void shouldCompareCodeFromSavedActionNode()
	{
		// given
		final Node node = new Node.Builder().withData("action:saved[pk=1,code=actionCode]").build();

		// when
		final boolean result = nodeTypeService.hasCode(node, "actionCode");

		// then
		assertThat(result).isTrue();
	}

	@Test
	public void shouldCompareCodeFromUnsavedActionNode()
	{
		// given
		final Node node = new Node.Builder().withData("action:unsaved[code=actionCode]").build();

		// when
		final boolean result = nodeTypeService.hasCode(node, "actionCode");

		// then
		assertThat(result).isTrue();
	}

	@Test
	public void shouldCompareNegativelyCodeFromUnsavedActionNode()
	{
		// given
		final Node node = new Node.Builder().withData("action:unsaved[code=actionCode]").build();

		// when
		final boolean result = nodeTypeService.hasCode(node, "otherActionCode");

		// then
		assertThat(result).isFalse();
	}

	@Test
	public void shouldCompareNegativelyCodeFromSavedActionNode()
	{
		// given
		final Node node = new Node.Builder().withData("action:saved[pk=1,code=actionCode]").build();

		// when
		final boolean result = nodeTypeService.hasCode(node, "otherActionCode");

		// then
		assertThat(result).isFalse();
	}

	@Test
	public void shouldCompareNegativelyCodeFromInvalidNode()
	{
		// given
		final Node node = new Node.Builder().withData("invalid[actionCode]Data").build();

		// when
		final boolean result = nodeTypeService.hasCode(node, "actionCode");

		// then
		assertThat(result).isFalse();
	}

	@Test
	public void shouldCompareCodeWithCodeFromNode()
	{
		// given
		final Node node = new Node.Builder().withData("action:unsaved[code=code=13]").build();

		// when
		final boolean result = nodeTypeService.hasCode(node, "code=13");

		// then
		assertThat(result).isFalse();
	}

	@Test
	public void shouldGetNodeName()
	{
		assertThat(nodeTypeService.getNodeName(new Node.Builder().withLabel("label").build())).isEqualTo("label");
		assertThat(nodeTypeService.getNodeName(new Node.Builder().withTitle("title").build())).isEqualTo("title");
		assertThat(nodeTypeService.getNodeName(new Node.Builder().withLabel("label").withTitle("title").build()))
				.isEqualTo("label");
	}
}
