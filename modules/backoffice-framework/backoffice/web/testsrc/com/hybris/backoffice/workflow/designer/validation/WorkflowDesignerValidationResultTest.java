/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.validation;

import static org.assertj.core.api.Assertions.assertThat;

import com.hybris.backoffice.workflow.designer.validation.Violation;
import com.hybris.backoffice.workflow.designer.validation.WorkflowDesignerValidationResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class WorkflowDesignerValidationResultTest
{

	@Test
	public void shouldAddViolationsWithDifferentParams()
	{
		//given
		final Violation warn1 = Violation.warn("messageKey1", "param1");
		final Violation warn2 = Violation.warn("messageKey1", "param2");
		final WorkflowDesignerValidationResult validationResult1 = createWorkflowDesignerValidationResult(warn1);
		final WorkflowDesignerValidationResult validationResult2 = createWorkflowDesignerValidationResult(warn2);

		//when
		final WorkflowDesignerValidationResult result = WorkflowDesignerValidationResult.combine(validationResult1,
				validationResult2);

		//then
		assertThat(result.getViolations()).hasSize(2);
	}

	@Test
	public void shouldAddOnlyOneViolationIfParamsIsTheSame()
	{
		//given
		final Violation warn1 = Violation.warn("messageKey1", "param1");
		final Violation warn2 = Violation.warn("messageKey1", "param1");
		final WorkflowDesignerValidationResult validationResult1 = createWorkflowDesignerValidationResult(warn1);
		final WorkflowDesignerValidationResult validationResult2 = createWorkflowDesignerValidationResult(warn2);

		//when
		final WorkflowDesignerValidationResult result = WorkflowDesignerValidationResult.combine(validationResult1,
				validationResult2);

		//then
		assertThat(result.getViolations()).hasSize(1);
	}

	@Test
	public void shouldAddOnlyOneViolationIfMessageKeyIsTheSame()
	{
		//given
		final Violation warn1 = Violation.warn("messageKey1");
		final Violation warn2 = Violation.warn("messageKey1");
		final WorkflowDesignerValidationResult validationResult1 = createWorkflowDesignerValidationResult(warn1);
		final WorkflowDesignerValidationResult validationResult2 = createWorkflowDesignerValidationResult(warn2);

		//when
		final WorkflowDesignerValidationResult result = WorkflowDesignerValidationResult.combine(validationResult1,
				validationResult2);

		//then
		assertThat(result.getViolations()).hasSize(1);
	}

	@Test
	public void shouldAddViolationsWithTheSameMessageKey()
	{
		//given
		final Violation warn1 = Violation.warn("messageKey1");
		final Violation warn2 = Violation.warn("messageKey2");
		final WorkflowDesignerValidationResult validationResult1 = createWorkflowDesignerValidationResult(warn1);
		final WorkflowDesignerValidationResult validationResult2 = createWorkflowDesignerValidationResult(warn2);

		//when
		final WorkflowDesignerValidationResult result = WorkflowDesignerValidationResult.combine(validationResult1,
				validationResult2);

		//then
		assertThat(result.getViolations()).hasSize(2);
	}

	private WorkflowDesignerValidationResult createWorkflowDesignerValidationResult(final Violation... violations)
	{
		final WorkflowDesignerValidationResult validationResult = new WorkflowDesignerValidationResult();
		for (final Violation violation : violations)
		{
			validationResult.addViolation(violation);
		}
		return validationResult;
	}
}
