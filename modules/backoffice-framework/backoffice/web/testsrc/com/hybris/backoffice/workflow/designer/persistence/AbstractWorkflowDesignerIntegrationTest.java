/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.persistence;

import static com.hybris.backoffice.widgets.networkchart.NetworkChartController.MODEL_INIT_DATA;
import static com.hybris.backoffice.widgets.networkchart.NetworkChartController.MODEL_NETWORK_EDGES;
import static com.hybris.backoffice.widgets.networkchart.NetworkChartController.MODEL_NETWORK_NODES;
import static com.hybris.backoffice.workflow.designer.WorkflowDesignerNetworkPopulator.MODEL_NEW_WORKFLOW_ITEMS_KEY;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.link.LinkModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.workflow.WorkflowTemplateService;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowDecisionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.assertj.core.api.iterable.Extractor;
import org.junit.Before;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import com.hybris.backoffice.workflow.designer.WorkflowNetworkEntitiesFactory;
import com.hybris.backoffice.workflow.designer.handler.DefaultWorkflowDesignerSaveHandler;
import com.hybris.backoffice.workflow.designer.pojo.WorkflowAction;
import com.hybris.backoffice.workflow.designer.pojo.WorkflowActionTemplate;
import com.hybris.backoffice.workflow.designer.pojo.WorkflowDecision;
import com.hybris.backoffice.workflow.designer.pojo.WorkflowDecisionTemplate;
import com.hybris.backoffice.workflow.designer.pojo.WorkflowLink;
import com.hybris.cockpitng.components.visjs.network.data.Edge;
import com.hybris.cockpitng.components.visjs.network.data.Node;
import com.hybris.cockpitng.core.impl.DefaultWidgetModel;
import com.hybris.cockpitng.core.model.ModelValueHandler;
import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.engine.WidgetInstanceManager;


public abstract class AbstractWorkflowDesignerIntegrationTest extends ServicelayerTransactionalTest
{
	protected static final Extractor<WorkflowActionTemplateModel, String> ACTION_CODE = WorkflowActionTemplateModel::getCode;
	protected static final Extractor<WorkflowActionTemplateModel, Collection<WorkflowDecisionTemplateModel>> ACTION_DECISIONS = WorkflowActionTemplateModel::getDecisionTemplates;
	protected static final Extractor<WorkflowActionTemplateModel, Collection<WorkflowDecisionTemplateModel>> ACTION_INCOMING_DECISIONS = WorkflowActionTemplateModel::getIncomingTemplateDecisions;
	protected static final Extractor<WorkflowActionTemplateModel, Collection<LinkModel>> ACTION_INCOMING_LINKS = WorkflowActionTemplateModel::getIncomingLinkTemplates;
	protected static final Extractor<WorkflowDecisionTemplateModel, String> DECISION_CODE = WorkflowDecisionTemplateModel::getCode;

	@Resource
	ModelService modelService;
	@Resource
	UserService userService;
	@Resource
	WorkflowTemplateService workflowTemplateService;

	WorkflowNetworkEntitiesFactory workflowNetworkEntitiesFactory;

	private Map<String, Object> realModel = new HashMap<>();

	private ApplicationContext applicationContext;

	@Before
	@Override
	public void prepareApplicationContextAndSession() throws Exception
	{
		super.prepareApplicationContextAndSession();


		final String[] contextPaths =
		{ "resources/test/workflow/workflow-designer-integration-test.xml",
				"web/webroot/WEB-INF/conf/workflow-designer/backoffice-workflow-designer-services-spring.xml",
				"web/webroot/WEB-INF/conf/workflow-designer/backoffice-workflow-designer-renderers-spring.xml",
				"web/webroot/WEB-INF/conf/workflow-designer/backoffice-workflow-designer-persisters-spring.xml" };

		final ApplicationContext applicationContext = new FileSystemXmlApplicationContext(
				Arrays.stream(contextPaths).map(this::resolveContextAbsolutePath).toArray(String[]::new),
				Registry.getApplicationContext());

		this.applicationContext = applicationContext;
		this.workflowNetworkEntitiesFactory = applicationContext.getBean("workflowNetworkEntitiesFactory",
				WorkflowNetworkEntitiesFactory.class);
	}

	private String resolveContextAbsolutePath(final String path)
	{
		return "file:" + Path.of(Utilities.getExtensionInfo("backoffice").getExtensionDirectory().getAbsolutePath())
				.resolve(Path.of(path)).toAbsolutePath().toString();
	}

	@Override
	protected ApplicationContext getApplicationContext()
	{
		return applicationContext;
	}

	protected void createEdge(final NetworkChartContext context, final Node from, final Node to)
	{
		final Edge edge = new Edge.Builder(from, to).build();
		context.getWim().getModel().getValue(MODEL_NETWORK_EDGES, Collection.class).add(edge);
	}

	protected Node createAndNode(final NetworkChartContext context)
	{
		final LinkModel linkModel = modelService.create(LinkModel._TYPECODE);
		linkModel.setSource(modelService.create(WorkflowActionTemplateModel._TYPECODE));
		final WorkflowLink workflowLink = WorkflowLink.ofUnsavedModel(linkModel, true);
		context.getWim().getModel().getValue(MODEL_NEW_WORKFLOW_ITEMS_KEY, Collection.class).add(linkModel);
		final Node node = workflowNetworkEntitiesFactory.generateNode(workflowLink).get();
		context.getWim().getModel().getValue(MODEL_NETWORK_NODES, Collection.class).add(node);
		return node;
	}

	protected Node createNewActionNode(final NetworkChartContext context, final String code)
	{
		final WorkflowActionTemplateModel action = modelService.create(WorkflowActionTemplateModel._TYPECODE);
		final WorkflowActionTemplate workflowActionTemplate = new WorkflowActionTemplate(action);
		action.setCode(code);
		action.setPrincipalAssigned(userService.getAdminUser());
		context.getWim().getModel().getValue(MODEL_NEW_WORKFLOW_ITEMS_KEY, Collection.class).add(action);
		final Node node = workflowNetworkEntitiesFactory.generateNode(workflowActionTemplate).get();
		context.getWim().getModel().getValue(MODEL_NETWORK_NODES, Collection.class).add(node);
		return node;
	}

	protected Node createActionNode(final NetworkChartContext context, final WorkflowAction action)
	{
		final Node node = workflowNetworkEntitiesFactory.generateNode(action).get();
		context.getWim().getModel().getValue(MODEL_NETWORK_NODES, Collection.class).add(node);
		return node;
	}

	protected Node createNewDecisionNode(final NetworkChartContext context, final String code)
	{
		final WorkflowDecisionTemplateModel decision = modelService.create(WorkflowDecisionTemplateModel._TYPECODE);
		final WorkflowDecisionTemplate workflowDecisionTemplate = new WorkflowDecisionTemplate(decision);
		decision.setCode(code);
		context.getWim().getModel().getValue(MODEL_NEW_WORKFLOW_ITEMS_KEY, Collection.class).add(decision);
		final Node node = workflowNetworkEntitiesFactory.generateNode(workflowDecisionTemplate).get();
		context.getWim().getModel().getValue(MODEL_NETWORK_NODES, Collection.class).add(node);
		appendExistingDecisions(context, workflowDecisionTemplate);
		return node;
	}

	protected Node createDecisionNode(final NetworkChartContext context, final WorkflowDecision decision)
	{
		final Node node = workflowNetworkEntitiesFactory.generateNode(decision).get();
		context.getWim().getModel().getValue(MODEL_NETWORK_NODES, Collection.class).add(node);
		appendExistingDecisions(context, decision);
		return node;
	}

	private void appendExistingDecisions(final NetworkChartContext context, final WorkflowDecision decision)
	{
		Set existingDecisions = context.getWim().getModel().getValue(DefaultWorkflowDesignerSaveHandler.EXISTING_DECISIONS,
				Set.class);
		if (existingDecisions == null)
		{
			existingDecisions = new HashSet();
		}
		existingDecisions.add(decision.getModel());
		context.getWim().getModel().setValue(DefaultWorkflowDesignerSaveHandler.EXISTING_DECISIONS, existingDecisions);
	}

	protected NetworkChartContext prepareContext(final WorkflowTemplateModel workflowTemplateModel)
	{
		final NetworkChartContext context = mock(NetworkChartContext.class);
		final WidgetInstanceManager wim = mock(WidgetInstanceManager.class);
		final WidgetModel model = new DefaultWidgetModel(realModel, new ModelValueHandler()
		{
			@Override
			public void setValue(final Object model, final String expression, final Object value)
			{
				realModel.put(expression, value);
			}

			@Override
			public Object getValue(final Object model, final String expression, final boolean useSessionLanguageForLocalized)
			{
				return realModel.get(expression);
			}

			@Override
			public Object getValue(final Object model, final String expression)
			{
				return realModel.get(expression);
			}

			@Override
			public <T> Class<T> getValueType(final Object model, final String expression)
			{
				return (Class<T>) model.getClass();
			}
		});
		given(context.getWim()).willReturn(wim);
		given(wim.getModel()).willReturn(model);
		model.setValue(MODEL_NETWORK_NODES, new ArrayList<>());
		model.setValue(MODEL_NETWORK_EDGES, new ArrayList<>());
		model.setValue(MODEL_NEW_WORKFLOW_ITEMS_KEY, new ArrayList<>());
		model.setValue(MODEL_INIT_DATA, workflowTemplateModel);
		return context;
	}
}
