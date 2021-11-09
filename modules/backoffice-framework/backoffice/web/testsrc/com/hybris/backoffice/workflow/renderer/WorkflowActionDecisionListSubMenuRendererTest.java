/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.renderer;


import com.hybris.cockpitng.core.config.impl.jaxb.listview.ListColumn;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.testing.AbstractCockpitngUnitTest;
import com.hybris.cockpitng.testing.annotation.ExtensibleWidget;
import com.hybris.cockpitng.testing.util.CockpitTestUtil;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.zkoss.zul.Menupopup;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Predicate;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtensibleWidget(level = ExtensibleWidget.ALL)
@RunWith(MockitoJUnitRunner.class)
public class WorkflowActionDecisionListSubMenuRendererTest
		extends AbstractCockpitngUnitTest<WorkflowActionDecisionListSubMenuRenderer>
{
	@Spy
	@InjectMocks
	private WorkflowActionDecisionListSubMenuRenderer renderer;

	@Mock
	private Predicate predicate;

	@Mock
	private Menupopup menupopup;

	@Mock
	private ListColumn listColumn;

	@Mock
	private WorkflowActionModel workflowActionModel;

	@Mock
	private DataType dataType;

	@Mock
	private WidgetInstanceManager widgetInstanceManager;

	@Before
	public void setUp()
	{
		CockpitTestUtil.mockZkEnvironment();
	}

	@Test
	public void shouldFireComponentRendered()
	{
		// given
		when(workflowActionModel.getDecisions()).thenReturn(Collections.emptyList());
		when(predicate.negate()).thenReturn(predicate);
		when(Boolean.valueOf(predicate.test(workflowActionModel))).thenReturn(Boolean.FALSE);

		// when
		renderer.render(menupopup, listColumn, workflowActionModel, dataType, widgetInstanceManager);

		// then
		verify(renderer).fireComponentRendered(menupopup, listColumn, workflowActionModel);
	}

	@Test
	public void shouldNotRenderMenuItemWhenSubMenuIsEmpty()
	{
		// given
		when(workflowActionModel.getDecisions()).thenReturn(notEmptyDecisions());
		when(predicate.negate()).thenReturn(predicate);
		when(Boolean.valueOf(predicate.test(workflowActionModel))).thenReturn(Boolean.FALSE);
		renderer.setWorkflowActionDecisionMenuitemRenderer(noRenderingRenderer());


		// when
		renderer.render(menupopup, listColumn, workflowActionModel, dataType, widgetInstanceManager);

		// then
		verify(menupopup, never()).appendChild(any());
	}

	private Collection<WorkflowDecisionModel> notEmptyDecisions()
	{
		return Arrays.asList(mock(WorkflowDecisionModel.class), mock(WorkflowDecisionModel.class));
	}

	private WorkflowActionDecisionMenuitemRenderer noRenderingRenderer()
	{
		final WorkflowActionDecisionMenuitemRenderer workflowActionDecisionMenuitemRenderer = mock(
				WorkflowActionDecisionMenuitemRenderer.class);
		doNothing().when(workflowActionDecisionMenuitemRenderer).render(any(), any(), any(), any(), any());
		return workflowActionDecisionMenuitemRenderer;
	}
}
