/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.handler.create;

import static com.hybris.backoffice.workflow.designer.handler.create.WorkflowDesignerCreateActionHandler.MODEL_OBJECT;
import static com.hybris.backoffice.workflow.designer.handler.create.WorkflowDesignerCreateActionHandler.SOCKET_OUT_ACTION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;

import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.workflow.enums.WorkflowActionType;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;

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
import com.hybris.backoffice.workflow.designer.form.WorkflowTemplateCreateActionForm;
import com.hybris.backoffice.workflow.designer.services.NodeTypeService;
import com.hybris.cockpitng.components.visjs.network.data.Node;
import com.hybris.cockpitng.config.jaxb.wizard.CustomType;
import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandlerAdapter;


@RunWith(MockitoJUnitRunner.class)
public class WorkflowDesignerCreateActionHandlerTest
{
	private static final CustomType ANY_CUSTOM_TYPE = null;
	private static final Map<String, String> ANY_MAP = null;
	@Mock
	ModelService mockedModelService;
	@Mock
	NodeTypeService mockedNodeTypeService;
	@Mock
	InitialElementLocationProvider mockedInitialElementLocationProvider;

	@InjectMocks
	WorkflowDesignerCreateActionHandler workflowDesignerCreateActionHandler;

	@Test
	public void shouldCreateAction()
	{
		// given
		final ArgumentCaptor<Object> socketOutValueCaptor = ArgumentCaptor.forClass(Object.class);
		final WorkflowTemplateCreateActionForm createActionForm = prepareWorkflowTemplateCreateActionForm();

		final WorkflowActionTemplateModel workflowActionTemplateModel = mock(WorkflowActionTemplateModel.class);
		final ElementLocation initialLocation = mock(ElementLocation.class);

		final FlowActionHandlerAdapter adapter = mock(FlowActionHandlerAdapter.class);
		final WidgetInstanceManager widgetInstanceManager = mock(WidgetInstanceManager.class);
		final WidgetModel widgetModel = mock(WidgetModel.class);

		given(adapter.getWidgetInstanceManager()).willReturn(widgetInstanceManager);
		given(widgetInstanceManager.getModel()).willReturn(widgetModel);
		given(widgetModel.getValue(MODEL_OBJECT, WorkflowTemplateCreateActionForm.class)).willReturn(createActionForm);
		willDoNothing().given(widgetInstanceManager).sendOutput(eq(SOCKET_OUT_ACTION), socketOutValueCaptor.capture());

		given(mockedModelService.create(WorkflowActionTemplateModel._TYPECODE)).willReturn(workflowActionTemplateModel);
		given(mockedInitialElementLocationProvider.provideLocation(any())).willReturn(initialLocation);

		// when
		workflowDesignerCreateActionHandler.perform(ANY_CUSTOM_TYPE, adapter, ANY_MAP);

		// then
		// workflow action template should be created properly based on the form object
		then(workflowActionTemplateModel).should().setCode(createActionForm.getCode());
		then(workflowActionTemplateModel).should().setActionType(createActionForm.getActionType());
		then(workflowActionTemplateModel).should().setPrincipalAssigned(createActionForm.getPrincipalAssigned());
		then(workflowActionTemplateModel).should().setName("englishName", Locale.ENGLISH);
		then(workflowActionTemplateModel).should().setName("germanName", Locale.GERMAN);

		// the action template should not be saved
		then(mockedModelService).should(never()).save(workflowActionTemplateModel);

		// the dto object should be sent containing model
		final Object sentDto = socketOutValueCaptor.getValue();
		assertThat(sentDto).isInstanceOf(ElementDto.class);
		assertThat(((ElementDto) sentDto).getOperation()).isEqualTo(Operation.CREATE);
		assertThat(((ElementDto) sentDto).getModel()).isEqualTo(workflowActionTemplateModel);

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
		final WorkflowTemplateCreateActionForm form = mock(WorkflowTemplateCreateActionForm.class);
		final Node node = mock(Node.class);

		given(adapter.getWidgetInstanceManager()).willReturn(widgetInstanceManager);
		given(widgetInstanceManager.getModel()).willReturn(widgetModel);
		given(widgetModel.getValue(MODEL_OBJECT, WorkflowTemplateCreateActionForm.class)).willReturn(form);
		given(widgetModel.getValue("ctx.node", Node.class)).willReturn(node);
		given(form.getCode()).willReturn("actionCode");

		final WorkflowActionTemplateModel actionModel = mock(WorkflowActionTemplateModel.class);
		given(mockedModelService.create(WorkflowActionTemplateModel._TYPECODE)).willReturn(actionModel);

		given(mockedNodeTypeService.hasCode(node, "actionCode")).willReturn(true);

		// when
		workflowDesignerCreateActionHandler.perform(ANY_CUSTOM_TYPE, adapter, ANY_MAP);

		// then
		then(widgetInstanceManager).should().sendOutput(eq(workflowDesignerCreateActionHandler.getSocketOutput()),
				any(ElementDto.class));
		then(adapter).should().done();
	}

	@Test
	public void shouldCreateActionIfItsNotInModel()
	{
		// given
		final WorkflowTemplateCreateActionForm form = mock(WorkflowTemplateCreateActionForm.class);
		final Node node = mock(Node.class);
		final WidgetModel widgetModel = mock(WidgetModel.class);

		given(form.getName()).willReturn(Map.of());
		given(mockedModelService.create(WorkflowActionTemplateModel._TYPECODE)).willReturn(mock(WorkflowActionTemplateModel.class));

		// when
		workflowDesignerCreateActionHandler.retrieveOrCreateModelInstance(form, node, widgetModel);

		// then
		then(mockedModelService).should().create(WorkflowActionTemplateModel._TYPECODE);
	}

	@Test
	public void shouldRetrieveActionIfItsInModel()
	{
		// given
		final WorkflowTemplateCreateActionForm form = mock(WorkflowTemplateCreateActionForm.class);
		final Node node = mock(Node.class);
		final WidgetModel widgetModel = mock(WidgetModel.class);
		final WorkflowActionTemplateModel action = mock(WorkflowActionTemplateModel.class);
		final String code = "someAction";
		given(action.getCode()).willReturn(code);
		given(node.getData()).willReturn(code);
		given(widgetModel.getValue(anyString(), any())).willReturn(action);
		given(form.getName()).willReturn(Map.of());
		given(form.getDescription()).willReturn(Map.of());
		given(mockedNodeTypeService.isSameAction(action, node)).willReturn(true);

		// when
		final WorkflowActionTemplateModel newModel = workflowDesignerCreateActionHandler.retrieveOrCreateModelInstance(form, node,
				widgetModel);

		// then
		then(mockedModelService).should(never()).create(WorkflowActionTemplateModel._TYPECODE);
		assertThat(newModel).isNotNull();
	}

	@Test
	public void shouldNotSetNameAndDescriptionIfMapIsNull()
	{
		// given
		final WorkflowTemplateCreateActionForm form = mock(WorkflowTemplateCreateActionForm.class);
		final Node node = mock(Node.class);
		final WidgetModel widgetModel = mock(WidgetModel.class);

		given(form.getName()).willReturn(null);
		given(form.getDescription()).willReturn(null);
		given(mockedModelService.create(WorkflowActionTemplateModel._TYPECODE)).willReturn(mock(WorkflowActionTemplateModel.class));

		// when
		final WorkflowActionTemplateModel result = workflowDesignerCreateActionHandler.retrieveOrCreateModelInstance(form, node,
				widgetModel);

		// then
		assertThat(result.getName()).isEqualTo(null);
		assertThat(result.getDescription()).isEqualTo(null);
	}

	@Test
	public void shouldCompareCodeUniqueness()
	{
		// given
		final String code = "someAction";

		final Node node = mock(Node.class);
		given(node.getData()).willReturn(code);

		final WorkflowActionTemplateModel decision = mock(WorkflowActionTemplateModel.class);
		given(decision.getCode()).willReturn(code);

		final WorkflowTemplateCreateActionForm form = prepareWorkflowTemplateCreateActionForm();
		final WidgetModel widgetModel = mock(WidgetModel.class);
		given(widgetModel.getValue(anyString(), any())).willReturn(Set.of(node));

		given(mockedNodeTypeService.hasCode(node, code)).willReturn(true);

		// when
		final boolean codeUnique = workflowDesignerCreateActionHandler.isCodeUnique(form, widgetModel);

		// then
		assertThat(codeUnique).isTrue();
	}

	private WorkflowTemplateCreateActionForm prepareWorkflowTemplateCreateActionForm()
	{
		final WorkflowTemplateCreateActionForm createActionForm = new WorkflowTemplateCreateActionForm();
		createActionForm.setActionType(WorkflowActionType.START);
		createActionForm.setCode("actionCode");
		createActionForm.setPrincipalAssigned(mock(PrincipalModel.class));
		createActionForm.setName(Map.ofEntries(Map.entry(Locale.ENGLISH, "englishName"), Map.entry(Locale.GERMAN, "germanName")));
		return createActionForm;
	}
}
