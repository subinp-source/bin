/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;

import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;

import java.util.Optional;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import com.hybris.backoffice.workflow.designer.dto.ActionDto;
import com.hybris.backoffice.workflow.designer.dto.ElementDto;
import com.hybris.backoffice.workflow.designer.dto.ElementLocation;
import com.hybris.backoffice.workflow.designer.dto.Operation;
import com.hybris.backoffice.workflow.designer.handler.create.InitialElementLocationProvider;
import com.hybris.backoffice.workflow.designer.pojo.WorkflowActionTemplate;
import com.hybris.backoffice.workflow.designer.pojo.WorkflowEntity;
import com.hybris.backoffice.workflow.designer.pojo.WorkflowTemplate;
import com.hybris.backoffice.workflow.designer.services.ConnectionFinder;
import com.hybris.cockpitng.components.visjs.network.data.Network;
import com.hybris.cockpitng.components.visjs.network.data.Node;
import com.hybris.cockpitng.components.visjs.network.response.Action;
import com.hybris.cockpitng.components.visjs.network.response.NetworkUpdates;
import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.engine.WidgetInstanceManager;


@RunWith(MockitoJUnitRunner.class)
public class WorkflowDesignerNetworkPopulatorTest
{
	private static final NetworkChartContext ANY_NETWORK_CHART_CONTEXT = new NetworkChartContext(null);

	@Mock
	private ModelService mockedModelService;
	@Mock
	private WorkflowNetworkEntitiesFactory mockedWorkflowNetworkEntitiesFactory;
	@Mock
	private ConnectionFinder connectionFinder;
	@Mock
	private InitialElementLocationProvider initialElementLocationProvider;

	@InjectMocks
	private WorkflowDesignerNetworkPopulator populator;

	@Before
	public void setUp()
	{
		given(connectionFinder.findEdgesOfNode(any(), any())).willReturn(Set.of());
		given(initialElementLocationProvider.provideLocation(any())).willReturn(ElementLocation.zeroLocation());
	}

	@Test
	public void shouldCreateEmptyNetworkIfInitDataIsNotPresent()
	{
		// given
		final NetworkChartContext context = new NetworkChartContext(null);

		// when
		final Network network = populator.populate(context);

		// then
		assertThat(network).isNotNull();
		assertThat(network.getNodes()).isEmpty();
		assertThat(network.getEdges()).isEmpty();
	}

	@Test
	public void shouldCreateEmptyNetworkIfInitDataIsNotAWorkflowTemplate()
	{
		// given
		final NetworkChartContext context = new NetworkChartContext(null);
		context.setInitData(new Object());

		// when
		final Network network = populator.populate(context);

		// then
		assertThat(network).isNotNull();
		assertThat(network.getNodes()).isEmpty();
		assertThat(network.getEdges()).isEmpty();
	}

	@Test
	public void shouldCreateNetworkWithNodesAndEdges()
	{
		// given
		final WorkflowTemplateModel model = mock(WorkflowTemplateModel.class);
		final WorkflowTemplate workflowTemplate = new WorkflowTemplate(model);
		final NetworkChartContext context = new NetworkChartContext(null);
		context.setInitData(model);
		final Network network = mock(Network.class);

		given(mockedWorkflowNetworkEntitiesFactory.generateNetwork(workflowTemplate)).willReturn(network);

		// when
		final Network result = populator.populate(context);

		// then
		then(mockedModelService).should().refresh(model);
		assertThat(result).isSameAs(network);
	}

	@Test
	public void shouldUpdateNetworkChartUsingFactory()
	{
		// given
		final WorkflowActionTemplateModel modelHandledByFactory = mock(WorkflowActionTemplateModel.class);

		final ElementDto elementDto = mock(ActionDto.class);
		given(elementDto.getModel()).willReturn(modelHandledByFactory);

		final String nodeId = "nodeId";
		final Node generatedNode = new Node.Builder().withId(nodeId).build();
		given(mockedWorkflowNetworkEntitiesFactory.generateNode(any())).willAnswer(invocationOnMock -> {
			final WorkflowEntity workflowEntity = (WorkflowEntity) invocationOnMock.getArguments()[0];
			return workflowEntity.getModel() == modelHandledByFactory ? Optional.of(generatedNode) : Optional.empty();
		});


		final NetworkChartContext context = mock(NetworkChartContext.class);
		final WidgetInstanceManager wim = mock(WidgetInstanceManager.class);
		final WidgetModel widgetModel = mock(WidgetModel.class);
		given(wim.getModel()).willReturn(widgetModel);
		given(context.getWim()).willReturn(wim);

		// when
		final NetworkUpdates updates = populator.update(elementDto, context);

		// then
		assertThat(updates).isNotNull();
		assertThat(updates.getUpdates()).hasSize(1);
		assertThat(updates.getUpdates().get(0).getAction()).isEqualTo(Action.ADD);
		assertThat(updates.getUpdates().get(0).getEntity().getId()).isEqualTo(nodeId);
	}

	@Test
	public void shouldUpdateNodeWhenItExistsInsteadOfGeneratingNew()
	{
		// given
		final WorkflowActionTemplateModel itemModel = mock(WorkflowActionTemplateModel.class);
		final WorkflowEntity workflowEntity = new WorkflowActionTemplate(itemModel);

		final ElementDto elementDto = mock(ActionDto.class);
		final Node node = mock(Node.class);
		given(elementDto.getNode()).willReturn(node);
		given(elementDto.getModel()).willReturn(itemModel);
		given(elementDto.getOperation()).willReturn(Operation.EDIT);
		given(mockedWorkflowNetworkEntitiesFactory.generateNode(workflowEntity, node)).willReturn(Optional.of(node));

		// when
		final NetworkUpdates updates = populator.update(elementDto, ANY_NETWORK_CHART_CONTEXT);

		// then
		assertThat(updates).isNotNull();
		assertThat(updates.getUpdates()).hasSize(1);
		assertThat(updates.getUpdates().get(0).getAction()).isEqualTo(Action.UPDATE);
		then(mockedWorkflowNetworkEntitiesFactory).should().generateNode(workflowEntity, node);
		then(mockedWorkflowNetworkEntitiesFactory).should(never()).generateNode(workflowEntity);
	}

	@Test
	public void shouldUpdateNetworkChartWithEmptyUpdatesForUnhandledObject()
	{
		// given
		final Object unhandledObject = new Object();

		// when
		final NetworkUpdates updates = populator.update(unhandledObject, ANY_NETWORK_CHART_CONTEXT);

		// then
		assertThat(updates).isEqualTo(NetworkUpdates.EMPTY);
	}

	@Test
	public void shouldUseInitialElementLocationProvider()
	{
		// given
		final WorkflowActionTemplateModel itemModel = mock(WorkflowActionTemplateModel.class);
		final WorkflowEntity workflowEntity = new WorkflowActionTemplate(itemModel);
		final ElementDto elementDto = mock(ActionDto.class);
		given(elementDto.getModel()).willReturn(itemModel);
		given(elementDto.getOperation()).willReturn(Operation.CREATE);

		final Node generatedNode = new Node.Builder().withId("nodeId").build();
		given(mockedWorkflowNetworkEntitiesFactory.generateNode(workflowEntity)).willReturn(Optional.of(generatedNode));
		given(initialElementLocationProvider.provideLocation(any())).willReturn(ElementLocation.of(10, 15));

		// when
		final NetworkUpdates updates = populator.update(elementDto, ANY_NETWORK_CHART_CONTEXT);

		// then
		assertThat(updates).isNotNull();
		assertThat(updates.getUpdates()).hasSize(1);
		assertThat(updates.getUpdates().get(0).getAction()).isEqualTo(Action.ADD);
		final Node entity = (Node) updates.getUpdates().get(0).getEntity();
		assertThat(entity.getId()).isEqualTo("nodeId");
		assertThat(entity.getX()).isEqualTo(10);
		assertThat(entity.getY()).isEqualTo(15);
	}
}
