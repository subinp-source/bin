/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.flow;

import com.hybris.cockpitng.components.visjs.network.data.Node;
import org.junit.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


public class WorkflowDesignerToShowFlowNetworkNodeDecoratorTest
{

	private final WorkflowDesignerToShowFlowNetworkNodeDecorator decorator = new WorkflowDesignerToShowFlowNetworkNodeDecorator();

	@Test
	public void shouldDecorateNode()
	{
		// given
		final String group = "group999";
		final String id = "newId";
		final int level = 555;

		final Node nodeToDecorate = new Node.Builder().withId("someId").withLevel(999).withGroup(group).build();
		final Map<String, Object> ctx = Map.of("id", id, "level", level);

		// when
		final Node decoratedNode = decorator.decorate(nodeToDecorate, ctx);

		// then
		assertThat(decoratedNode).extracting("id", "level", "group").contains(id, level, group);
	}

	@Test
	public void shouldNodeNotBeDecoratedWhenCtxLacksOfNecessaryData()
	{
		// given
		final Node nodeToDecorate = new Node.Builder().withId("someId").withLevel(999).withGroup("someGroup").build();
		final Map<String, Object> ctx = Map.of();

		// when
		final Node decoratedNode = decorator.decorate(nodeToDecorate, ctx);

		// then
		assertThat(decoratedNode).isEqualTo(nodeToDecorate);
	}

}
