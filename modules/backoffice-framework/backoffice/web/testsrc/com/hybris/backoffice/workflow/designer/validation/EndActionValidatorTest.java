/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.validation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.Arrays;
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
public class EndActionValidatorTest
{

	@Mock
	NetworkEntityFinder nodeFinder;

	@InjectMocks
	EndActionValidator endActionValidator;

	@Test
	public void shouldNotCreateViolationWhenEndActionExist()
	{
		//given
		final List<Node> actions = new ArrayList<>(Collections.singletonList(createEndAction()));
		final NetworkChartContext context = mock(NetworkChartContext.class);
		given(nodeFinder.findActionNodes(context)).willReturn(actions);

		//when
		final WorkflowDesignerValidationResult result = endActionValidator.validate(context);

		//then
		assertThat(result.getViolations()).isEmpty();
	}

	@Test
	public void shouldNotCreateViolationWhenMoreThanOneEndActionExist()
	{
		//given
		final List<Node> actions = new ArrayList<>(Arrays.asList(createEndAction(), createEndAction()));
		final NetworkChartContext context = mock(NetworkChartContext.class);
		given(nodeFinder.findActionNodes(context)).willReturn(actions);

		//when
		final WorkflowDesignerValidationResult result = endActionValidator.validate(context);

		//then
		assertThat(result.getViolations()).isEmpty();
	}

	@Test
	public void shouldCreateViolationWhenEndActionNotExist()
	{
		//given
		final List actions = Collections.EMPTY_LIST;
		final NetworkChartContext context = mock(NetworkChartContext.class);
		given(nodeFinder.findActionNodes(context)).willReturn(actions);

		//when
		final WorkflowDesignerValidationResult result = endActionValidator.validate(context);

		//then
		assertThat(result.getViolations()).hasSize(1);
	}

	private Node createEndAction()
	{
		return new Node.Builder().withGroup(WorkflowDesignerGroup.END_ACTION.getValue()).build();
	}

}
