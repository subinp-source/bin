/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import com.hybris.backoffice.widgets.networkchart.handler.ViewEventHandler;
import com.hybris.cockpitng.components.visjs.network.data.Edge;
import com.hybris.cockpitng.components.visjs.network.data.Edges;
import com.hybris.cockpitng.components.visjs.network.data.Node;
import com.hybris.cockpitng.components.visjs.network.data.Nodes;
import com.hybris.cockpitng.components.visjs.network.response.NetworkUpdates;
import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.engine.WidgetInstanceManager;


@RunWith(MockitoJUnitRunner.class)
public class ViewEventDetectingHandlerTest
{
	@Mock
	ViewEventHandler mockedDelegate;
	@Mock
	WorkflowDesignerDataManipulationListener mockedListener;

	@InjectMocks
	ViewEventDetectingHandler handler;

	@Test
	public void shouldNotifyAboutChangeOnClickNodeWhenNetworkUpdatesAreNotEmpty()
	{
		// given
		final Node node = mock(Node.class);
		final NetworkChartContext networkChartContext = prepareNetworkChartContext();
		final NetworkUpdates expectedNetworkUpdates = mock(NetworkUpdates.class);
		given(mockedDelegate.onClick(node, networkChartContext)).willReturn(expectedNetworkUpdates);

		// when
		final NetworkUpdates networkUpdates = handler.onClick(node, networkChartContext);

		// then
		assertThat(networkUpdates).isEqualTo(expectedNetworkUpdates);
		then(mockedDelegate).should().onClick(node, networkChartContext);
		then(mockedListener).should().onChange(networkChartContext.getWim().getModel());
	}

	@Test
	public void shouldNotNotifyAboutChangeOnClickNodeWhenNetworkUpdatesAreEmpty()
	{
		// given
		final Node node = mock(Node.class);
		final NetworkChartContext networkChartContext = prepareNetworkChartContext();
		final NetworkUpdates emptyNetworkUpdates = NetworkUpdates.EMPTY;
		given(mockedDelegate.onClick(node, networkChartContext)).willReturn(emptyNetworkUpdates);

		// when
		final NetworkUpdates networkUpdates = handler.onClick(node, networkChartContext);

		// then
		assertThat(networkUpdates).isEqualTo(emptyNetworkUpdates);
		then(mockedDelegate).should().onClick(node, networkChartContext);
		then(mockedListener).should(never()).onChange(networkChartContext.getWim().getModel());
	}

	@Test
	public void shouldNotifyAboutChangeOnDoubleClickNodeWhenNetworkUpdatesAreNotEmpty()
	{
		// given
		final Node node = mock(Node.class);
		final NetworkChartContext networkChartContext = prepareNetworkChartContext();
		final NetworkUpdates expectedNetworkUpdates = mock(NetworkUpdates.class);
		given(mockedDelegate.onDoubleClick(node, networkChartContext)).willReturn(expectedNetworkUpdates);

		// when
		final NetworkUpdates networkUpdates = handler.onDoubleClick(node, networkChartContext);

		// then
		assertThat(networkUpdates).isEqualTo(expectedNetworkUpdates);
		then(mockedDelegate).should().onDoubleClick(node, networkChartContext);
		then(mockedListener).should().onChange(networkChartContext.getWim().getModel());
	}

	@Test
	public void shouldNotNotifyAboutChangeOnDoubleClickNodeWhenNetworkUpdatesAreEmpty()
	{
		// given
		final Node node = mock(Node.class);
		final NetworkChartContext networkChartContext = prepareNetworkChartContext();
		final NetworkUpdates emptyNetworkUpdates = NetworkUpdates.EMPTY;
		given(mockedDelegate.onDoubleClick(node, networkChartContext)).willReturn(emptyNetworkUpdates);

		// when
		final NetworkUpdates networkUpdates = handler.onDoubleClick(node, networkChartContext);

		// then
		assertThat(networkUpdates).isEqualTo(emptyNetworkUpdates);
		then(mockedDelegate).should().onDoubleClick(node, networkChartContext);
		then(mockedListener).should(never()).onChange(networkChartContext.getWim().getModel());
	}

	@Test
	public void shouldNotifyAboutChangeOnClickEdgeWhenNetworkUpdatesAreNotEmpty()
	{
		// given
		final Edge edge = mock(Edge.class);
		final NetworkChartContext networkChartContext = prepareNetworkChartContext();
		final NetworkUpdates expectedNetworkUpdates = mock(NetworkUpdates.class);
		given(mockedDelegate.onClick(edge, networkChartContext)).willReturn(expectedNetworkUpdates);

		// when
		final NetworkUpdates networkUpdates = handler.onClick(edge, networkChartContext);

		// then
		assertThat(networkUpdates).isEqualTo(expectedNetworkUpdates);
		then(mockedDelegate).should().onClick(edge, networkChartContext);
		then(mockedListener).should().onChange(networkChartContext.getWim().getModel());
	}

	@Test
	public void shouldNotNotifyAboutChangeOnClickEdgeWhenNetworkUpdatesAreEmpty()
	{
		// given
		final Edge edge = mock(Edge.class);
		final NetworkChartContext networkChartContext = prepareNetworkChartContext();
		final NetworkUpdates emptyNetworkUpdates = NetworkUpdates.EMPTY;
		given(mockedDelegate.onClick(edge, networkChartContext)).willReturn(emptyNetworkUpdates);

		// when
		final NetworkUpdates networkUpdates = handler.onClick(edge, networkChartContext);

		// then
		assertThat(networkUpdates).isEqualTo(emptyNetworkUpdates);
		then(mockedDelegate).should().onClick(edge, networkChartContext);
		then(mockedListener).should(never()).onChange(networkChartContext.getWim().getModel());
	}

	@Test
	public void shouldNotifyAboutChangeOnDoubleClickEdgeWhenNetworkUpdatesAreNotEmpty()
	{
		// given
		final Edge edge = mock(Edge.class);
		final NetworkChartContext networkChartContext = prepareNetworkChartContext();
		final NetworkUpdates expectedNetworkUpdates = mock(NetworkUpdates.class);
		given(mockedDelegate.onDoubleClick(edge, networkChartContext)).willReturn(expectedNetworkUpdates);

		// when
		final NetworkUpdates networkUpdates = handler.onDoubleClick(edge, networkChartContext);

		// then
		assertThat(networkUpdates).isEqualTo(expectedNetworkUpdates);
		then(mockedDelegate).should().onDoubleClick(edge, networkChartContext);
		then(mockedListener).should().onChange(networkChartContext.getWim().getModel());
	}

	@Test
	public void shouldNotNotifyAboutChangeOnDoubleClickEdgeWhenNetworkUpdatesAreEmpty()
	{
		// given
		final Edge edge = mock(Edge.class);
		final NetworkChartContext networkChartContext = prepareNetworkChartContext();
		final NetworkUpdates emptyNetworkUpdates = NetworkUpdates.EMPTY;
		given(mockedDelegate.onDoubleClick(edge, networkChartContext)).willReturn(emptyNetworkUpdates);

		// when
		final NetworkUpdates networkUpdates = handler.onDoubleClick(edge, networkChartContext);

		// then
		assertThat(networkUpdates).isEqualTo(emptyNetworkUpdates);
		then(mockedDelegate).should().onDoubleClick(edge, networkChartContext);
		then(mockedListener).should(never()).onChange(networkChartContext.getWim().getModel());
	}

	@Test
	public void shouldNotifyAboutChangeOnSelectEdgeWhenNetworkUpdatesAreNotEmpty()
	{
		// given
		final Edge edge = mock(Edge.class);
		final NetworkChartContext networkChartContext = prepareNetworkChartContext();
		final NetworkUpdates expectedNetworkUpdates = mock(NetworkUpdates.class);
		given(mockedDelegate.onSelect(edge, networkChartContext)).willReturn(expectedNetworkUpdates);

		// when
		final NetworkUpdates networkUpdates = handler.onSelect(edge, networkChartContext);

		// then
		assertThat(networkUpdates).isEqualTo(expectedNetworkUpdates);
		then(mockedDelegate).should().onSelect(edge, networkChartContext);
		then(mockedListener).should().onChange(networkChartContext.getWim().getModel());
	}

	@Test
	public void shouldNotNotifyAboutChangeOnSelectEdgeWhenNetworkUpdatesAreEmpty()
	{
		// given
		final Edge edge = mock(Edge.class);
		final NetworkChartContext networkChartContext = prepareNetworkChartContext();
		final NetworkUpdates emptyNetworkUpdates = NetworkUpdates.EMPTY;
		given(mockedDelegate.onSelect(edge, networkChartContext)).willReturn(emptyNetworkUpdates);

		// when
		final NetworkUpdates networkUpdates = handler.onSelect(edge, networkChartContext);

		// then
		assertThat(networkUpdates).isEqualTo(emptyNetworkUpdates);
		then(mockedDelegate).should().onSelect(edge, networkChartContext);
		then(mockedListener).should(never()).onChange(networkChartContext.getWim().getModel());
	}

	@Test
	public void shouldNotifyAboutChangeOnSelectNodeWhenNetworkUpdatesAreNotEmpty()
	{
		// given
		final Node node = mock(Node.class);
		final NetworkChartContext networkChartContext = prepareNetworkChartContext();
		final NetworkUpdates expectedNetworkUpdates = mock(NetworkUpdates.class);
		given(mockedDelegate.onSelect(node, networkChartContext)).willReturn(expectedNetworkUpdates);

		// when
		final NetworkUpdates networkUpdates = handler.onSelect(node, networkChartContext);

		// then
		assertThat(networkUpdates).isEqualTo(expectedNetworkUpdates);
		then(mockedDelegate).should().onSelect(node, networkChartContext);
		then(mockedListener).should().onChange(networkChartContext.getWim().getModel());
	}

	@Test
	public void shouldNotNotifyAboutChangeOnSelectNodeWhenNetworkUpdatesAreEmpty()
	{
		// given
		final Node node = mock(Node.class);
		final NetworkChartContext networkChartContext = prepareNetworkChartContext();
		final NetworkUpdates emptyNetworkUpdates = NetworkUpdates.EMPTY;
		given(mockedDelegate.onSelect(node, networkChartContext)).willReturn(emptyNetworkUpdates);

		// when
		final NetworkUpdates networkUpdates = handler.onSelect(node, networkChartContext);

		// then
		assertThat(networkUpdates).isEqualTo(emptyNetworkUpdates);
		then(mockedDelegate).should().onSelect(node, networkChartContext);
		then(mockedListener).should(never()).onChange(networkChartContext.getWim().getModel());
	}

	@Test
	public void shouldNotifyAboutChangeOnDeselectEdgesWhenNetworkUpdatesAreNotEmpty()
	{
		// given
		final Edges edges = mock(Edges.class);
		final NetworkChartContext networkChartContext = prepareNetworkChartContext();
		final NetworkUpdates expectedNetworkUpdates = mock(NetworkUpdates.class);
		given(mockedDelegate.onDeselect(edges, networkChartContext)).willReturn(expectedNetworkUpdates);

		// when
		final NetworkUpdates networkUpdates = handler.onDeselect(edges, networkChartContext);

		// then
		assertThat(networkUpdates).isEqualTo(expectedNetworkUpdates);
		then(mockedDelegate).should().onDeselect(edges, networkChartContext);
		then(mockedListener).should().onChange(networkChartContext.getWim().getModel());
	}

	@Test
	public void shouldNotNotifyAboutChangeOnDeselectEdgesWhenNetworkUpdatesAreEmpty()
	{
		// given
		final Edges edges = mock(Edges.class);
		final NetworkChartContext networkChartContext = prepareNetworkChartContext();
		final NetworkUpdates emptyNetworkUpdates = NetworkUpdates.EMPTY;
		given(mockedDelegate.onDeselect(edges, networkChartContext)).willReturn(emptyNetworkUpdates);

		// when
		final NetworkUpdates networkUpdates = handler.onDeselect(edges, networkChartContext);

		// then
		assertThat(networkUpdates).isEqualTo(emptyNetworkUpdates);
		then(mockedDelegate).should().onDeselect(edges, networkChartContext);
		then(mockedListener).should(never()).onChange(networkChartContext.getWim().getModel());
	}

	@Test
	public void shouldNotifyAboutChangeOnDeselectNodeWhenNetworkUpdatesAreNotEmpty()
	{
		// given
		final Nodes nodes = mock(Nodes.class);
		final NetworkChartContext networkChartContext = prepareNetworkChartContext();
		final NetworkUpdates expectedNetworkUpdates = mock(NetworkUpdates.class);
		given(mockedDelegate.onDeselect(nodes, networkChartContext)).willReturn(expectedNetworkUpdates);

		// when
		final NetworkUpdates networkUpdates = handler.onDeselect(nodes, networkChartContext);

		// then
		assertThat(networkUpdates).isEqualTo(expectedNetworkUpdates);
		then(mockedDelegate).should().onDeselect(nodes, networkChartContext);
		then(mockedListener).should().onChange(networkChartContext.getWim().getModel());
	}

	@Test
	public void shouldNotNotifyAboutChangeOnDeselectNodesWhenNetworkUpdatesAreEmpty()
	{
		// given
		final Nodes nodes = mock(Nodes.class);
		final NetworkChartContext networkChartContext = prepareNetworkChartContext();
		final NetworkUpdates emptyNetworkUpdates = NetworkUpdates.EMPTY;
		given(mockedDelegate.onDeselect(nodes, networkChartContext)).willReturn(emptyNetworkUpdates);

		// when
		final NetworkUpdates networkUpdates = handler.onDeselect(nodes, networkChartContext);

		// then
		assertThat(networkUpdates).isEqualTo(emptyNetworkUpdates);
		then(mockedDelegate).should().onDeselect(nodes, networkChartContext);
		then(mockedListener).should(never()).onChange(networkChartContext.getWim().getModel());
	}

	@Test
	public void shouldNotifyAboutChangeOnHoverEdgeWhenNetworkUpdatesAreNotEmpty()
	{
		// given
		final Edge edge = mock(Edge.class);
		final NetworkChartContext networkChartContext = prepareNetworkChartContext();
		final NetworkUpdates expectedNetworkUpdates = mock(NetworkUpdates.class);
		given(mockedDelegate.onHover(edge, networkChartContext)).willReturn(expectedNetworkUpdates);

		// when
		final NetworkUpdates networkUpdates = handler.onHover(edge, networkChartContext);

		// then
		assertThat(networkUpdates).isEqualTo(expectedNetworkUpdates);
		then(mockedDelegate).should().onHover(edge, networkChartContext);
		then(mockedListener).should().onChange(networkChartContext.getWim().getModel());
	}

	@Test
	public void shouldNotNotifyAboutChangeOnHoverEdgeWhenNetworkUpdatesAreEmpty()
	{
		// given
		final Edge edge = mock(Edge.class);
		final NetworkChartContext networkChartContext = prepareNetworkChartContext();
		final NetworkUpdates emptyNetworkUpdates = NetworkUpdates.EMPTY;
		given(mockedDelegate.onHover(edge, networkChartContext)).willReturn(emptyNetworkUpdates);

		// when
		final NetworkUpdates networkUpdates = handler.onHover(edge, networkChartContext);

		// then
		assertThat(networkUpdates).isEqualTo(emptyNetworkUpdates);
		then(mockedDelegate).should().onHover(edge, networkChartContext);
		then(mockedListener).should(never()).onChange(networkChartContext.getWim().getModel());
	}

	@Test
	public void shouldNotifyAboutChangeOnHoverNodeWhenNetworkUpdatesAreNotEmpty()
	{
		// given
		final Node node = mock(Node.class);
		final NetworkChartContext networkChartContext = prepareNetworkChartContext();
		final NetworkUpdates expectedNetworkUpdates = mock(NetworkUpdates.class);
		given(mockedDelegate.onHover(node, networkChartContext)).willReturn(expectedNetworkUpdates);

		// when
		final NetworkUpdates networkUpdates = handler.onHover(node, networkChartContext);

		// then
		assertThat(networkUpdates).isEqualTo(expectedNetworkUpdates);
		then(mockedDelegate).should().onHover(node, networkChartContext);
		then(mockedListener).should().onChange(networkChartContext.getWim().getModel());
	}

	@Test
	public void shouldNotNotifyAboutChangeOnHoverNodeWhenNetworkUpdatesAreEmpty()
	{
		// given
		final Node node = mock(Node.class);
		final NetworkChartContext networkChartContext = prepareNetworkChartContext();
		final NetworkUpdates emptyNetworkUpdates = NetworkUpdates.EMPTY;
		given(mockedDelegate.onHover(node, networkChartContext)).willReturn(emptyNetworkUpdates);

		// when
		final NetworkUpdates networkUpdates = handler.onHover(node, networkChartContext);

		// then
		assertThat(networkUpdates).isEqualTo(emptyNetworkUpdates);
		then(mockedDelegate).should().onHover(node, networkChartContext);
		then(mockedListener).should(never()).onChange(networkChartContext.getWim().getModel());
	}

	@Test
	public void shouldNotifyAboutChangeOnBlurEdgeWhenNetworkUpdatesAreNotEmpty()
	{
		// given
		final Edge edge = mock(Edge.class);
		final NetworkChartContext networkChartContext = prepareNetworkChartContext();
		final NetworkUpdates expectedNetworkUpdates = mock(NetworkUpdates.class);
		given(mockedDelegate.onBlur(edge, networkChartContext)).willReturn(expectedNetworkUpdates);

		// when
		final NetworkUpdates networkUpdates = handler.onBlur(edge, networkChartContext);

		// then
		assertThat(networkUpdates).isEqualTo(expectedNetworkUpdates);
		then(mockedDelegate).should().onBlur(edge, networkChartContext);
		then(mockedListener).should().onChange(networkChartContext.getWim().getModel());
	}

	@Test
	public void shouldNotNotifyAboutChangeOnBlurEdgeWhenNetworkUpdatesAreEmpty()
	{
		// given
		final Edge edge = mock(Edge.class);
		final NetworkChartContext networkChartContext = prepareNetworkChartContext();
		final NetworkUpdates emptyNetworkUpdates = NetworkUpdates.EMPTY;
		given(mockedDelegate.onBlur(edge, networkChartContext)).willReturn(emptyNetworkUpdates);

		// when
		final NetworkUpdates networkUpdates = handler.onBlur(edge, networkChartContext);

		// then
		assertThat(networkUpdates).isEqualTo(emptyNetworkUpdates);
		then(mockedDelegate).should().onBlur(edge, networkChartContext);
		then(mockedListener).should(never()).onChange(networkChartContext.getWim().getModel());
	}

	@Test
	public void shouldNotifyAboutChangeOnBlurNodeWhenNetworkUpdatesAreNotEmpty()
	{
		// given
		final Node node = mock(Node.class);
		final NetworkChartContext networkChartContext = prepareNetworkChartContext();
		final NetworkUpdates expectedNetworkUpdates = mock(NetworkUpdates.class);
		given(mockedDelegate.onBlur(node, networkChartContext)).willReturn(expectedNetworkUpdates);

		// when
		final NetworkUpdates networkUpdates = handler.onBlur(node, networkChartContext);

		// then
		assertThat(networkUpdates).isEqualTo(expectedNetworkUpdates);
		then(mockedDelegate).should().onBlur(node, networkChartContext);
		then(mockedListener).should().onChange(networkChartContext.getWim().getModel());
	}

	@Test
	public void shouldNotNotifyAboutChangeOnBlurNodeWhenNetworkUpdatesAreEmpty()
	{
		// given
		final Node node = mock(Node.class);
		final NetworkChartContext networkChartContext = prepareNetworkChartContext();
		final NetworkUpdates emptyNetworkUpdates = NetworkUpdates.EMPTY;
		given(mockedDelegate.onBlur(node, networkChartContext)).willReturn(emptyNetworkUpdates);

		// when
		final NetworkUpdates networkUpdates = handler.onBlur(node, networkChartContext);

		// then
		assertThat(networkUpdates).isEqualTo(emptyNetworkUpdates);
		then(mockedDelegate).should().onBlur(node, networkChartContext);
		then(mockedListener).should(never()).onChange(networkChartContext.getWim().getModel());
	}

	@Test
	public void shouldNotifyAboutChangeOnDragEndNodeWhenNetworkUpdatesAreNotEmpty()
	{
		// given
		final Node node = mock(Node.class);
		final NetworkChartContext networkChartContext = prepareNetworkChartContext();
		final NetworkUpdates expectedNetworkUpdates = mock(NetworkUpdates.class);
		given(mockedDelegate.onDragEnd(node, networkChartContext)).willReturn(expectedNetworkUpdates);

		// when
		final NetworkUpdates networkUpdates = handler.onDragEnd(node, networkChartContext);

		// then
		assertThat(networkUpdates).isEqualTo(expectedNetworkUpdates);
		then(mockedDelegate).should().onDragEnd(node, networkChartContext);
		then(mockedListener).should().onChange(networkChartContext.getWim().getModel());
	}

	@Test
	public void shouldNotNotifyAboutChangeOnDragEndNodeWhenNetworkUpdatesAreEmpty()
	{
		// given
		final Node node = mock(Node.class);
		final NetworkChartContext networkChartContext = prepareNetworkChartContext();
		final NetworkUpdates emptyNetworkUpdates = NetworkUpdates.EMPTY;
		given(mockedDelegate.onDragEnd(node, networkChartContext)).willReturn(emptyNetworkUpdates);

		// when
		final NetworkUpdates networkUpdates = handler.onDragEnd(node, networkChartContext);

		// then
		assertThat(networkUpdates).isEqualTo(emptyNetworkUpdates);
		then(mockedDelegate).should().onDragEnd(node, networkChartContext);
		then(mockedListener).should(never()).onChange(networkChartContext.getWim().getModel());
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
