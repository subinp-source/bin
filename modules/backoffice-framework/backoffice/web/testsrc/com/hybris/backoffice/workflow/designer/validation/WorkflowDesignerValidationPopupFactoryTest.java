/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.validation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

import com.hybris.backoffice.workflow.designer.validation.Violation;
import com.hybris.backoffice.workflow.designer.validation.WorkflowDesignerValidatableContainer;
import com.hybris.backoffice.workflow.designer.validation.WorkflowDesignerValidationPopupFactory;
import com.hybris.backoffice.workflow.designer.validation.WorkflowDesignerValidationResult;
import com.hybris.backoffice.workflow.designer.validation.WorkflowDesignerValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Window;

import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.testing.util.CockpitTestUtil;
import com.hybris.cockpitng.widgets.configurableflow.validation.ConfigurableFlowValidationRenderer;
import com.hybris.cockpitng.widgets.configurableflow.validation.ConfigurableFlowValidationResultsPopup;


@RunWith(MockitoJUnitRunner.class)
public class WorkflowDesignerValidationPopupFactoryTest
{
	private static final WorkflowDesignerValidationResult EMPTY_VALIDATION_RESULT = new WorkflowDesignerValidationResult();
	@Mock
	ConfigurableFlowValidationRenderer mockedValidationRenderer;
	@Mock
	WorkflowDesignerValidator mockedValidator;

	@InjectMocks
	WorkflowDesignerValidationPopupFactory factory;

	@Before
	public void setUp() throws Exception
	{
		CockpitTestUtil.mockZkEnvironment();
	}

	@Test
	public void shouldNotCreateValidationPopUpIfThereAreNoViolations()
	{
		// given
		final NetworkChartContext context = mock(NetworkChartContext.class);
		final Button saveButton = mock(Button.class);
		final Runnable saveOperation = mock(Runnable.class);

		given(mockedValidator.validate(context)).willReturn(EMPTY_VALIDATION_RESULT);

		// when
		final Optional<Window> result = factory.createValidationPopup(context, saveButton, saveOperation);

		// then
		assertThat(result).isEmpty();
		verifyZeroInteractions(saveOperation);
	}

	@Test
	public void shouldCreateNewValidationPopUpIfThereAreViolations()
	{
		// given
		final WidgetModel model = mock(WidgetModel.class);

		final WidgetInstanceManager wim = mock(WidgetInstanceManager.class);
		given(wim.getModel()).willReturn(model);

		final NetworkChartContext context = mock(NetworkChartContext.class);
		given(context.getWim()).willReturn(wim);

		final Div buttonContainer = new Div();

		// button container already contains popup 
		final ConfigurableFlowValidationResultsPopup previousPopUp = createPopUp();
		final ConfigurableFlowValidationResultsPopup newPopUp = createPopUp();
		buttonContainer.getChildren().add(previousPopUp);

		final Button saveButton = new Button();
		saveButton.setParent(buttonContainer);

		final Runnable saveOperation = mock(Runnable.class);

		final Violation violation = mock(Violation.class);
		given(violation.getLevel()).willReturn(Violation.Level.ERROR);
		given(mockedValidator.validate(context)).willReturn(new WorkflowDesignerValidationResult(List.of(violation)));

		given(mockedValidationRenderer.createValidationViolationsPopup(any(WorkflowDesignerValidatableContainer.class),
				any(BiConsumer.class))).willReturn(newPopUp);

		// when
		final Optional<Window> result = factory.createValidationPopup(context, saveButton, saveOperation);

		// then
		assertThat(result).isPresent();

		assertThat(findPopUp(buttonContainer)).isPresent()
				.hasValueSatisfying(component -> assertThat(component).isEqualTo(newPopUp));
		verifyZeroInteractions(saveOperation);
	}

	private Optional<Component> findPopUp(final Div buttonContainer)
	{
		return CockpitTestUtil.findChild(buttonContainer, ConfigurableFlowValidationResultsPopup.class::isInstance);
	}

	private ConfigurableFlowValidationResultsPopup createPopUp()
	{
		return new ConfigurableFlowValidationResultsPopup(null, new Listbox(), null);
	}
}
