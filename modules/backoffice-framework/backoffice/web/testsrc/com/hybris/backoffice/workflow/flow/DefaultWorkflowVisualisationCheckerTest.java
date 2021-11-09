/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.flow;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import com.hybris.backoffice.workflow.designer.WorkflowDesignerModelKey;
import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.engine.WidgetInstanceManager;


@RunWith(MockitoJUnitRunner.class)
public class DefaultWorkflowVisualisationCheckerTest
{

	private final DefaultWorkflowVisualisationChecker checker = new DefaultWorkflowVisualisationChecker();

	@Test
	public void shouldVisualisationBeSet()
	{
		// given
		final NetworkChartContext context = mockContext(true);

		// when
		final boolean output = checker.isVisualisationSet(context);

		// then
		assertThat(output).isTrue();
	}

	@Test
	public void shouldVisualisationNotBeSet()
	{
		// given
		final NetworkChartContext context = mockContext(false);

		// when
		final boolean output = checker.isVisualisationSet(context);

		// then
		assertThat(output).isFalse();
	}

	private NetworkChartContext mockContext(final boolean isVisualisationSet)
	{
		final NetworkChartContext context = mock(NetworkChartContext.class);
		final WidgetInstanceManager wim = mock(WidgetInstanceManager.class);
		final WidgetModel widgetModel = mock(WidgetModel.class);
		given(context.getWim()).willReturn(wim);
		given(wim.getModel()).willReturn(widgetModel);
		given(widgetModel.getValue(WorkflowDesignerModelKey.KEY_IS_VISUALISATION_SET, Boolean.class))
				.willReturn(isVisualisationSet);
		return context;
	}

}
