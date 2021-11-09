/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.handler.connection.validator;

import static com.hybris.backoffice.workflow.designer.handler.connection.validator.WorkflowConnectionValidationResult.ofViolations;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;


public class WorkflowConnectionValidatorTest
{
	public static final ValidationContext ANY_VALIDATION_CONTEXT = null;
	WorkflowConnectionValidator validator;

	@Before
	public void setUp()
	{
		validator = new WorkflowConnectionValidator();
	}

	@Test
	public void shouldConcatPartialValidatorsResults()
	{
		// given
		final Violation firstViolation = Violation.create("silent");
		final Violation secondViolation = Violation.create("notifiable");

		final WorkflowConnectionPartialValidator firstValidator = mock(WorkflowConnectionPartialValidator.class);
		given(firstValidator.validate(ANY_VALIDATION_CONTEXT)).willReturn(ofViolations(firstViolation));

		final WorkflowConnectionPartialValidator secondValidator = mock(WorkflowConnectionPartialValidator.class);
		given(secondValidator.validate(ANY_VALIDATION_CONTEXT)).willReturn(ofViolations(secondViolation));

		validator.setPartialValidators(Arrays.asList(firstValidator, secondValidator));

		// when
		final WorkflowConnectionValidationResult result = validator.validate(ANY_VALIDATION_CONTEXT);

		// then
		assertThat(result).isNotNull();
		assertThat(result.getViolations()).containsOnly(firstViolation, secondViolation);
	}

	@Test
	public void shouldReturnEmptyValidationResultWhenNoPartialValidatorIsAttached()
	{
		// given
		// validator without partial validators

		// when
		final WorkflowConnectionValidationResult result = validator.validate(ANY_VALIDATION_CONTEXT);

		// then
		assertThat(result).isEqualTo(WorkflowConnectionValidationResult.EMPTY);
	}

	@Test
	public void shouldSortPartialValidatorsByOrder()
	{
		// given
		final WorkflowConnectionPartialValidator moreImportantValidator = createPartialValidatorWithOrder(0);
		final WorkflowConnectionPartialValidator lessImportantValidator = createPartialValidatorWithOrder(100);

		validator.setPartialValidators(Arrays.asList(lessImportantValidator, moreImportantValidator));

		// when
		validator.validate(ANY_VALIDATION_CONTEXT);

		// then
		final InOrder inOrder = inOrder(moreImportantValidator, lessImportantValidator);
		inOrder.verify(moreImportantValidator).validate(ANY_VALIDATION_CONTEXT);
		inOrder.verify(lessImportantValidator).validate(ANY_VALIDATION_CONTEXT);
	}

	private WorkflowConnectionPartialValidator createPartialValidatorWithOrder(final int order)
	{
		final WorkflowConnectionPartialValidator validator = mock(WorkflowConnectionPartialValidator.class);
		given(validator.getOrder()).willReturn(order);
		given(validator.validate(any())).willReturn(WorkflowConnectionValidationResult.EMPTY);
		return validator;
	}
}
