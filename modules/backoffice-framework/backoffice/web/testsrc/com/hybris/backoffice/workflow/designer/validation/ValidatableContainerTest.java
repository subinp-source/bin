/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.validation;

import static com.hybris.backoffice.workflow.designer.validation.WorkflowDesignerValidatableContainer.VALIDATION_RESULT_MODEL_PATH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.util.List;
import java.util.Set;

import com.hybris.backoffice.workflow.designer.validation.Violation;
import com.hybris.backoffice.workflow.designer.validation.WorkflowDesignerValidatableContainer;
import com.hybris.backoffice.workflow.designer.validation.WorkflowDesignerValidationResult;
import org.junit.Test;
import org.zkoss.zk.ui.Component;

import com.hybris.cockpitng.components.validation.ValidationFocusTransferHandler;
import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.validation.model.ValidationInfo;
import com.hybris.cockpitng.validation.model.ValidationResult;
import com.hybris.cockpitng.validation.model.ValidationSeverity;


public class ValidatableContainerTest
{

	private static final WidgetModel ANY_MODEL = null;
	private static final String ANY_ROOT_PATH = null;
	private static final Component ANY_COMPONENT = null;
	private static final String ANY_PATH = null;

	@Test
	public void shouldReactOnlyOnValidationResultChangeInModel()
	{
		// given
		final WorkflowDesignerValidatableContainer validatableContainer = new WorkflowDesignerValidatableContainer(ANY_MODEL,
				ANY_ROOT_PATH, ANY_COMPONENT);

		// when
		final boolean result = validatableContainer.reactOnValidationChange(VALIDATION_RESULT_MODEL_PATH);

		// then
		assertThat(result).isTrue();
	}

	@Test
	public void shouldNotReactForOtherThanValidationResultChangeInModel()
	{
		// given
		final WorkflowDesignerValidatableContainer validatableContainer = new WorkflowDesignerValidatableContainer(ANY_MODEL,
				ANY_ROOT_PATH, ANY_COMPONENT);

		// when
		final boolean result = validatableContainer.reactOnValidationChange("otherThanValidationResult");

		// then
		assertThat(result).isFalse();
	}

	@Test
	public void shouldGetContainer()
	{
		// given
		final Component component = mock(Component.class);
		final WorkflowDesignerValidatableContainer validatableContainer = new WorkflowDesignerValidatableContainer(ANY_MODEL,
				ANY_ROOT_PATH, component);

		// when
		final Component result = validatableContainer.getContainer();

		// then
		assertThat(result).isSameAs(component);
	}

	@Test
	public void shouldNotSupportNavigationByInvalidProperty()
	{
		// given
		final WorkflowDesignerValidatableContainer validatableContainer = new WorkflowDesignerValidatableContainer(ANY_MODEL,
				ANY_ROOT_PATH, ANY_COMPONENT);

		// when
		final String currentObjectPath = validatableContainer.getCurrentObjectPath(ANY_PATH);

		// then
		assertThat(currentObjectPath).isEmpty();
	}

	@Test
	public void shouldNotSupportFocusTransfer()
	{
		// given
		final WorkflowDesignerValidatableContainer validatableContainer = new WorkflowDesignerValidatableContainer(ANY_MODEL,
				ANY_ROOT_PATH, ANY_COMPONENT);

		// when
		final ValidationFocusTransferHandler focusTransferHandler = validatableContainer.createFocusTransferHandler();

		// then
		assertThat(focusTransferHandler.focusValidationPath(ANY_COMPONENT, ANY_PATH)).isEqualTo(0);
	}

	@Test
	public void shouldGetCurrentValidationResult()
	{
		// given
		final WorkflowDesignerValidationResult workflowDesignerValidationResult = mock(WorkflowDesignerValidationResult.class);
		final Violation error = Violation.error("errorKey", "errorParam1");
		final Violation warn = Violation.warn("warnKey", "warnParam1");
		final Violation info = Violation.info("infoKey", "infoParam1");
		given(workflowDesignerValidationResult.getViolations()).willReturn(Set.of(error, warn, info));

		final WidgetModel widgetModel = mock(WidgetModel.class);
		given(widgetModel.getValue(VALIDATION_RESULT_MODEL_PATH, WorkflowDesignerValidationResult.class))
				.willReturn(workflowDesignerValidationResult);

		final WorkflowDesignerValidatableContainer validatableContainer = spy(
				new WorkflowDesignerValidatableContainer(widgetModel, ANY_ROOT_PATH, ANY_COMPONENT));
		given(validatableContainer.getValidationMessage(error)).willReturn("error localized message");
		given(validatableContainer.getValidationMessage(warn)).willReturn("warn localized message");
		given(validatableContainer.getValidationMessage(info)).willReturn("info localized message");

		// when
		final ValidationResult validationResult = validatableContainer.getCurrentValidationResult(ANY_PATH);

		// then
		assertThat(validationResult.getAll()).extracting(ValidationInfo::getValidationSeverity)
				.containsOnly(ValidationSeverity.ERROR, ValidationSeverity.WARN, ValidationSeverity.INFO);
		assertThat(validationResult.getAll()).extracting(ValidationInfo::getValidationMessage)
				.containsOnly("error localized message", "warn localized message", "info localized message");
	}
}
