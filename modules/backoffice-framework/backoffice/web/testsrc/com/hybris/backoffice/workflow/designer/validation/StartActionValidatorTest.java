/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.validation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import com.hybris.backoffice.workflow.designer.handler.connection.WorkflowDesignerGroup;
import com.hybris.backoffice.workflow.designer.services.NetworkEntityFinder;
import com.hybris.cockpitng.components.visjs.network.data.Node;


@RunWith(MockitoJUnitRunner.class)
public class StartActionValidatorTest
{

	@Mock
	NetworkEntityFinder networkEntityFinder;

	@InjectMocks
	StartActionValidator startActionValidator;

	@Test
	public void shouldNotCreateViolationWhenStartActionExist()
	{
		//given
		final List<Node> actions = List.of(createStartAction());
		final NetworkChartContext context = mock(NetworkChartContext.class);
		given(networkEntityFinder.findActionNodes(context)).willReturn(actions);

		//when
		final WorkflowDesignerValidationResult result = startActionValidator.validate(context);

		//then
		assertThat(result.getViolations()).isEmpty();
	}

	@Test
	public void shouldNotCreateViolationWhenMoreThanOneStartActionExist()
	{
		//given
		final List<Node> actions = List.of(createStartAction(), createStartAction());
		final NetworkChartContext context = mock(NetworkChartContext.class);
		given(networkEntityFinder.findActionNodes(context)).willReturn(actions);

		//when
		final WorkflowDesignerValidationResult result = startActionValidator.validate(context);

		//then
		assertThat(result.getViolations()).isEmpty();
	}

	@Test
	public void shouldCreateViolationWhenStartActionNotExist()
	{
		//given
		final List<Node> actions = Collections.EMPTY_LIST;
		final NetworkChartContext context = mock(NetworkChartContext.class);
		given(networkEntityFinder.findActionNodes(context)).willReturn(actions);

		//when
		final WorkflowDesignerValidationResult result = startActionValidator.validate(context);

		//then
		assertThat(result.getViolations()).hasSize(1);
	}

	private Node createStartAction()
	{
		return new Node.Builder().withGroup(WorkflowDesignerGroup.START_ACTION.getValue()).build();
	}
}
