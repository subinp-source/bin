/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import com.hybris.backoffice.widgets.networkchart.handler.DataManipulationHandler;
import com.hybris.cockpitng.components.visjs.network.data.Edge;
import com.hybris.cockpitng.components.visjs.network.data.EdgeUpdate;
import com.hybris.cockpitng.components.visjs.network.data.Edges;
import com.hybris.cockpitng.components.visjs.network.data.Node;
import com.hybris.cockpitng.components.visjs.network.data.Nodes;
import com.hybris.cockpitng.components.visjs.network.event.ClickOnAddNodeButtonEvent;
import com.hybris.cockpitng.components.visjs.network.response.NetworkUpdates;
import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.engine.WidgetInstanceManager;


@RunWith(MockitoJUnitRunner.class)
public class DataManipulationDetectingHandlerTest
{
	@Mock
	DataManipulationHandler mockedDelegate;
	@Mock
	WorkflowDesignerDataManipulationListener mockedListener;

	@InjectMocks
	DataManipulationDetectingHandler handler;

	@Test
	public void shouldNotifyWhenOnAddNodeWithNotEmptyUpdates()
	{
		// given
		final NetworkChartContext networkChartContext = prepareNetworkChartContext();
		final NetworkUpdates networkUpdates = mock(NetworkUpdates.class);
		final Node node = mock(Node.class);

		given(mockedDelegate.onAdd(node, networkChartContext)).willReturn(networkUpdates);

		// when
		final NetworkUpdates updates = handler.onAdd(node, networkChartContext);

		// then
		assertThat(updates).isEqualTo(networkUpdates);
		then(mockedListener).should().onChange(networkChartContext.getWim().getModel());
	}

	@Test
	public void shouldNotNotifyWhenOnAddNodeWithEmptyUpdates()
	{
		// given
		final NetworkChartContext networkChartContext = prepareNetworkChartContext();
		final NetworkUpdates emptyNetworkUpdates = NetworkUpdates.EMPTY;
		final Node node = mock(Node.class);

		given(mockedDelegate.onAdd(node, networkChartContext)).willReturn(emptyNetworkUpdates);

		// when
		handler.onAdd(node, networkChartContext);

		// then
		then(mockedListener).should(never()).onChange(any());
	}

	@Test
	public void shouldNotifyWhenOnAddEdgeWithNotEmptyUpdates()
	{
		// given
		final NetworkChartContext networkChartContext = prepareNetworkChartContext();
		final NetworkUpdates networkUpdates = mock(NetworkUpdates.class);
		final Edge edge = mock(Edge.class);

		given(mockedDelegate.onAdd(edge, networkChartContext)).willReturn(networkUpdates);

		// when
		final NetworkUpdates updates = handler.onAdd(edge, networkChartContext);

		// then
		assertThat(updates).isEqualTo(networkUpdates);
		then(mockedListener).should().onChange(networkChartContext.getWim().getModel());
	}

	@Test
	public void shouldNotNotifyWhenOnAddEdgeWithEmptyUpdates()
	{
		// given
		final NetworkChartContext networkChartContext = prepareNetworkChartContext();
		final NetworkUpdates emptyNetworkUpdates = NetworkUpdates.EMPTY;
		final Edge edge = mock(Edge.class);

		given(mockedDelegate.onAdd(edge, networkChartContext)).willReturn(emptyNetworkUpdates);

		// when
		handler.onAdd(edge, networkChartContext);

		// then
		then(mockedListener).should(never()).onChange(any());
	}

	@Test
	public void shouldNotifyWhenOnEditNodeWithNotEmptyUpdates()
	{
		// given
		final NetworkChartContext networkChartContext = prepareNetworkChartContext();
		final NetworkUpdates networkUpdates = mock(NetworkUpdates.class);
		final Node node = mock(Node.class);

		given(mockedDelegate.onEdit(node, networkChartContext)).willReturn(networkUpdates);

		// when
		final NetworkUpdates updates = handler.onEdit(node, networkChartContext);

		// then
		assertThat(updates).isEqualTo(networkUpdates);
		then(mockedListener).should().onChange(networkChartContext.getWim().getModel());
	}

	@Test
	public void shouldNotNotifyWhenOnEditNodeWithEmptyUpdates()
	{
		// given
		final NetworkChartContext networkChartContext = prepareNetworkChartContext();
		final NetworkUpdates emptyNetworkUpdates = NetworkUpdates.EMPTY;
		final Node node = mock(Node.class);

		given(mockedDelegate.onEdit(node, networkChartContext)).willReturn(emptyNetworkUpdates);

		// when
		handler.onEdit(node, networkChartContext);

		// then
		then(mockedListener).should(never()).onChange(any());
	}

	@Test
	public void shouldNotifyWhenOnEditEdgeWithNotEmptyUpdates()
	{
		// given
		final NetworkChartContext networkChartContext = prepareNetworkChartContext();
		final NetworkUpdates networkUpdates = mock(NetworkUpdates.class);
		final EdgeUpdate edge = mock(EdgeUpdate.class);

		given(mockedDelegate.onEdit(edge, networkChartContext)).willReturn(networkUpdates);

		// when
		final NetworkUpdates updates = handler.onEdit(edge, networkChartContext);

		// then
		assertThat(updates).isEqualTo(networkUpdates);
		then(mockedListener).should().onChange(networkChartContext.getWim().getModel());
	}

	@Test
	public void shouldNotNotifyWhenOnEditEdgeWithEmptyUpdates()
	{
		// given
		final NetworkChartContext networkChartContext = prepareNetworkChartContext();
		final NetworkUpdates emptyNetworkUpdates = NetworkUpdates.EMPTY;
		final EdgeUpdate edge = mock(EdgeUpdate.class);

		given(mockedDelegate.onEdit(edge, networkChartContext)).willReturn(emptyNetworkUpdates);

		// when
		handler.onEdit(edge, networkChartContext);

		// then
		then(mockedListener).should(never()).onChange(any());
	}

	@Test
	public void shouldNotifyWhenOnRemoveNodeWithNotEmptyUpdates()
	{
		// given
		final NetworkChartContext networkChartContext = prepareNetworkChartContext();
		final NetworkUpdates networkUpdates = mock(NetworkUpdates.class);
		final Nodes nodes = mock(Nodes.class);

		given(mockedDelegate.onRemove(nodes, networkChartContext)).willReturn(networkUpdates);

		// when
		final NetworkUpdates updates = handler.onRemove(nodes, networkChartContext);

		// then
		assertThat(updates).isEqualTo(networkUpdates);
		then(mockedListener).should().onChange(networkChartContext.getWim().getModel());
	}

	@Test
	public void shouldNotNotifyWhenOnRemoveNodeWithEmptyUpdates()
	{
		// given
		final NetworkChartContext networkChartContext = prepareNetworkChartContext();
		final NetworkUpdates emptyNetworkUpdates = NetworkUpdates.EMPTY;
		final Nodes nodes = mock(Nodes.class);

		given(mockedDelegate.onRemove(nodes, networkChartContext)).willReturn(emptyNetworkUpdates);

		// when
		handler.onRemove(nodes, networkChartContext);

		// then
		then(mockedListener).should(never()).onChange(any());
	}

	@Test
	public void shouldNotifyWhenOnRemoveEdgeWithNotEmptyUpdates()
	{
		// given
		final NetworkChartContext networkChartContext = prepareNetworkChartContext();
		final NetworkUpdates networkUpdates = mock(NetworkUpdates.class);
		final Edges edges = mock(Edges.class);

		given(mockedDelegate.onRemove(edges, networkChartContext)).willReturn(networkUpdates);

		// when
		final NetworkUpdates updates = handler.onRemove(edges, networkChartContext);

		// then
		assertThat(updates).isEqualTo(networkUpdates);
		then(mockedListener).should().onChange(networkChartContext.getWim().getModel());
	}

	@Test
	public void shouldNotNotifyWhenOnRemoveEdgeWithEmptyUpdates()
	{
		// given
		final NetworkChartContext networkChartContext = prepareNetworkChartContext();
		final NetworkUpdates emptyNetworkUpdates = NetworkUpdates.EMPTY;
		final Edges edges = mock(Edges.class);

		given(mockedDelegate.onRemove(edges, networkChartContext)).willReturn(emptyNetworkUpdates);

		// when
		handler.onRemove(edges, networkChartContext);

		// then
		then(mockedListener).should(never()).onChange(any());
	}

	@Test
	public void shouldNotifyWhenOnAddNodeButtonClickEdgeWithNotEmptyUpdates()
	{
		// given
		final NetworkChartContext networkChartContext = prepareNetworkChartContext();
		final NetworkUpdates networkUpdates = mock(NetworkUpdates.class);
		final ClickOnAddNodeButtonEvent event = mock(ClickOnAddNodeButtonEvent.class);

		given(mockedDelegate.onAddNodeButtonClick(event, networkChartContext)).willReturn(networkUpdates);

		// when
		final NetworkUpdates updates = handler.onAddNodeButtonClick(event, networkChartContext);

		// then
		assertThat(updates).isEqualTo(networkUpdates);
		then(mockedListener).should().onChange(networkChartContext.getWim().getModel());
	}

	@Test
	public void shouldNotNotifyWhenOnAddNodeButtonClickEdgeWithEmptyUpdates()
	{
		// given
		final NetworkChartContext networkChartContext = prepareNetworkChartContext();
		final NetworkUpdates emptyNetworkUpdates = NetworkUpdates.EMPTY;
		final ClickOnAddNodeButtonEvent event = mock(ClickOnAddNodeButtonEvent.class);

		given(mockedDelegate.onAddNodeButtonClick(event, networkChartContext)).willReturn(emptyNetworkUpdates);

		// when
		handler.onAddNodeButtonClick(event, networkChartContext);

		// then
		then(mockedListener).should(never()).onChange(any());
	}

	@Test
	public void shouldNotifyWhenOnSaveIsCalled()
	{
		// given
		final NetworkChartContext networkChartContext = prepareNetworkChartContext();
		final NetworkUpdates emptyNetworkUpdates = NetworkUpdates.EMPTY;
		given(mockedDelegate.onSave(networkChartContext)).willReturn(emptyNetworkUpdates);

		// when
		final NetworkUpdates updates = handler.onSave(networkChartContext);

		// then
		assertThat(updates).isEqualTo(emptyNetworkUpdates);
		then(mockedListener).should().onNew(networkChartContext.getWim().getModel());
	}

	@Test
	public void shouldNotifyWhenOnRefreshIsCalled()
	{
		// given
		final NetworkChartContext networkChartContext = prepareNetworkChartContext();
		final NetworkUpdates emptyNetworkUpdates = NetworkUpdates.EMPTY;
		given(mockedDelegate.onRefresh(networkChartContext)).willReturn(emptyNetworkUpdates);

		// when
		final NetworkUpdates updates = handler.onRefresh(networkChartContext);

		// then
		assertThat(updates).isEqualTo(emptyNetworkUpdates);
		then(mockedListener).should().onNew(networkChartContext.getWim().getModel());
	}

	@Test
	public void shouldNotifyWhenOnCancelIsCalled()
	{
		// given
		final NetworkChartContext networkChartContext = prepareNetworkChartContext();
		final NetworkUpdates emptyNetworkUpdates = NetworkUpdates.EMPTY;
		given(mockedDelegate.onCancel(networkChartContext)).willReturn(emptyNetworkUpdates);

		// when
		final NetworkUpdates updates = handler.onCancel(networkChartContext);

		// then
		assertThat(updates).isEqualTo(emptyNetworkUpdates);
		then(mockedListener).should().onNew(networkChartContext.getWim().getModel());
	}

	private NetworkChartContext prepareNetworkChartContext()
	{
		final WidgetInstanceManager widgetInstanceManager = mock(WidgetInstanceManager.class);
		given(widgetInstanceManager.getModel()).willReturn(mock(WidgetModel.class));
		final NetworkChartContext networkChartContext = mock(NetworkChartContext.class);
		given(networkChartContext.getWim()).willReturn(widgetInstanceManager);
		return networkChartContext;
	}
}
