/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.handler.create;

import static com.hybris.backoffice.workflow.designer.handler.create.WorkflowDesignerCreateDecisionHandler.MODEL_OBJECT;
import static com.hybris.backoffice.workflow.designer.handler.create.WorkflowDesignerCreateDecisionHandler.SOCKET_OUT_DECISION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;

import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.workflow.model.WorkflowDecisionTemplateModel;

import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.backoffice.workflow.designer.dto.ElementDto;
import com.hybris.backoffice.workflow.designer.dto.ElementLocation;
import com.hybris.backoffice.workflow.designer.dto.Operation;
import com.hybris.backoffice.workflow.designer.form.WorkflowTemplateCreateDecisionForm;
import com.hybris.backoffice.workflow.designer.services.NodeTypeService;
import com.hybris.cockpitng.components.visjs.network.data.Node;
import com.hybris.cockpitng.config.jaxb.wizard.CustomType;
import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandlerAdapter;


@RunWith(MockitoJUnitRunner.class)
public class WorkflowDesignerCreateDecisionHandlerTest
{
	private static final Map<String, String> ANY_MAP = null;
	private static final CustomType ANY_CUSTOM_TYPE = null;
	@Mock
	ModelService mockedModelService;
	@Mock
	InitialElementLocationProvider mockedInitialElementLocationProvider;
	@Mock
	NodeTypeService mockedNodeTypeService;

	@InjectMocks
	WorkflowDesignerCreateDecisionHandler workflowDesignerCreateDecisionHandler;

	@Test
	public void shouldCreateDecision()
	{
		// given
		final ArgumentCaptor<Object> socketOutValueCaptor = ArgumentCaptor.forClass(Object.class);
		final WorkflowTemplateCreateDecisionForm createDecisionForm = prepareWorkflowTemplateCreateDecisionForm();

		final WorkflowDecisionTemplateModel workflowDecisionTemplateModel = mock(WorkflowDecisionTemplateModel.class);
		final ElementLocation initialLocation = ElementLocation.of(10, 20);

		final FlowActionHandlerAdapter adapter = Mockito.mock(FlowActionHandlerAdapter.class);
		final WidgetInstanceManager widgetInstanceManager = mock(WidgetInstanceManager.class);
		final WidgetModel widgetModel = mock(WidgetModel.class);

		given(adapter.getWidgetInstanceManager()).willReturn(widgetInstanceManager);
		given(widgetInstanceManager.getModel()).willReturn(widgetModel);
		given(widgetModel.getValue(MODEL_OBJECT, WorkflowTemplateCreateDecisionForm.class)).willReturn(createDecisionForm);

		given(mockedModelService.create(WorkflowDecisionTemplateModel._TYPECODE)).willReturn(workflowDecisionTemplateModel);
		given(mockedInitialElementLocationProvider.provideLocation(any())).willReturn(initialLocation);
		willDoNothing().given(widgetInstanceManager).sendOutput(eq(SOCKET_OUT_DECISION), socketOutValueCaptor.capture());

		// when
		workflowDesignerCreateDecisionHandler.perform(ANY_CUSTOM_TYPE, adapter, ANY_MAP);

		// then
		// workflow decision template should be created properly based on the form object
		then(workflowDecisionTemplateModel).should().setCode(createDecisionForm.getCode());
		then(workflowDecisionTemplateModel).should().setName("englishName", Locale.ENGLISH);
		then(workflowDecisionTemplateModel).should().setName("germanName", Locale.GERMAN);

		// the decision template should not be saved
		then(mockedModelService).should(never()).save(workflowDecisionTemplateModel);

		// the dto object should be sent containing model
		final Object sentDto = socketOutValueCaptor.getValue();
		assertThat(sentDto).isInstanceOf(ElementDto.class);
		assertThat(((ElementDto) sentDto).getOperation()).isEqualTo(Operation.CREATE);
		assertThat(((ElementDto) sentDto).getModel()).isEqualTo(workflowDecisionTemplateModel);

		// the wizard should be closed
		then(adapter).should().done();
	}

	@Test
	public void shouldEditAction()
	{
		// given
		final FlowActionHandlerAdapter adapter = Mockito.mock(FlowActionHandlerAdapter.class);
		final WidgetInstanceManager widgetInstanceManager = mock(WidgetInstanceManager.class);
		final WidgetModel widgetModel = mock(WidgetModel.class);
		final WorkflowTemplateCreateDecisionForm form = mock(WorkflowTemplateCreateDecisionForm.class);
		final Node node = mock(Node.class);

		given(adapter.getWidgetInstanceManager()).willReturn(widgetInstanceManager);
		given(widgetInstanceManager.getModel()).willReturn(widgetModel);
		given(widgetModel.getValue(WorkflowDesignerCreateActionHandler.MODEL_OBJECT, WorkflowTemplateCreateDecisionForm.class))
				.willReturn(form);
		given(widgetModel.getValue("ctx.node", Node.class)).willReturn(node);
		given(form.getCode()).willReturn("decisionNode");

		final WorkflowDecisionTemplateModel decisionModel = mock(WorkflowDecisionTemplateModel.class);
		given(mockedModelService.create(WorkflowDecisionTemplateModel._TYPECODE)).willReturn(decisionModel);

		given(mockedNodeTypeService.hasCode(node, "decisionNode")).willReturn(true);

		// when
		workflowDesignerCreateDecisionHandler.perform(ANY_CUSTOM_TYPE, adapter, ANY_MAP);

		// then
		then(widgetInstanceManager).should().sendOutput(eq(workflowDesignerCreateDecisionHandler.getSocketOutput()),
				any(ElementDto.class));
		then(adapter).should().done();
	}

	@Test
	public void shouldCreateDecisionIfItsNotInModel()
	{
		// given
		final WorkflowTemplateCreateDecisionForm form = mock(WorkflowTemplateCreateDecisionForm.class);
		final Node node = mock(Node.class);
		final WidgetModel widgetModel = mock(WidgetModel.class);

		given(form.getName()).willReturn(Map.of());
		given(mockedModelService.create(WorkflowDecisionTemplateModel._TYPECODE))
				.willReturn(mock(WorkflowDecisionTemplateModel.class));

		// when
		workflowDesignerCreateDecisionHandler.retrieveOrCreateModelInstance(form, node, widgetModel);

		// then
		then(mockedModelService).should().create(WorkflowDecisionTemplateModel._TYPECODE);
	}

	@Test
	public void shouldRetrieveDecisionIfItsInModel()
	{
		// given
		final WorkflowTemplateCreateDecisionForm form = mock(WorkflowTemplateCreateDecisionForm.class);
		final Node node = mock(Node.class);
		final WidgetModel widgetModel = mock(WidgetModel.class);
		final WorkflowDecisionTemplateModel decision = mock(WorkflowDecisionTemplateModel.class);
		final String code = "someDecision";
		given(decision.getCode()).willReturn(code);
		given(node.getData()).willReturn(code);
		given(widgetModel.getValue(anyString(), any())).willReturn(decision);
		given(form.getName()).willReturn(Map.of());
		given(mockedNodeTypeService.isSameDecision(decision, node)).willReturn(true);

		// when
		final WorkflowDecisionTemplateModel newModel = workflowDesignerCreateDecisionHandler.retrieveOrCreateModelInstance(form,
				node, widgetModel);

		// then
		then(mockedModelService).should(never()).create(WorkflowDecisionTemplateModel._TYPECODE);
		assertThat(newModel).isNotNull();
	}

	@Test
	public void shouldCompareCodeUniqueness()
	{
		// given
		final String code = "someDecision";

		final Node node = mock(Node.class);
		given(node.getData()).willReturn(code);

		final WorkflowDecisionTemplateModel decision = mock(WorkflowDecisionTemplateModel.class);
		given(decision.getCode()).willReturn(code);

		final WorkflowTemplateCreateDecisionForm form = prepareWorkflowTemplateCreateDecisionForm();
		final WidgetModel widgetModel = mock(WidgetModel.class);
		given(widgetModel.getValue(anyString(), any())).willReturn(Set.of(node));

		given(mockedNodeTypeService.hasCode(node, code)).willReturn(true);

		// when
		final boolean codeUnique = workflowDesignerCreateDecisionHandler.isCodeUnique(form, widgetModel);

		// then
		assertThat(codeUnique).isTrue();
	}

	private WorkflowTemplateCreateDecisionForm prepareWorkflowTemplateCreateDecisionForm()
	{
		final WorkflowTemplateCreateDecisionForm createDecisionForm = new WorkflowTemplateCreateDecisionForm();
		createDecisionForm.setCode("decisionCode");
		createDecisionForm.setName(Map.ofEntries(Map.entry(Locale.ENGLISH, "englishName"), Map.entry(Locale.GERMAN, "germanName")));
		return createDecisionForm;
	}
}
