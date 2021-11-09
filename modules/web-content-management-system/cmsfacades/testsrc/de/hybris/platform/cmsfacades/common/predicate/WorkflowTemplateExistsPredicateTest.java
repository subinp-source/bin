/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.common.predicate;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.workflow.WorkflowTemplateService;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class WorkflowTemplateExistsPredicateTest
{
	private final String TEMPLATE_CODE = "some template code";

	@Mock
	private WorkflowTemplateModel workflowTemplateModel;

	@Mock
	private WorkflowTemplateService workflowTemplateService;

	@InjectMocks
	private WorkflowTemplateExistsPredicate workflowTemplateExistsPredicate;

	@Before
	public void setUp()
	{
		when(workflowTemplateService.getWorkflowTemplateForCode(TEMPLATE_CODE)).thenReturn(workflowTemplateModel);
	}

	@Test
	public void givenAmbiguousWorkflowTemplateCode_WhenTestIsCalled_ThenItReturnsFalse()
	{
		// GIVEN
		when(workflowTemplateService.getWorkflowTemplateForCode(TEMPLATE_CODE))
				.thenThrow(new AmbiguousIdentifierException("Ambiguous code"));

		// WHEN
		final boolean result = workflowTemplateExistsPredicate.test(TEMPLATE_CODE);

		// THEN
		assertFalse(result);
	}

	@Test
	public void givenUnknownWorkflowTemplateCode_WhenTestIsCalled_ThenItReturnsFalse()
	{
		// GIVEN
		when(workflowTemplateService.getWorkflowTemplateForCode(TEMPLATE_CODE))
				.thenThrow(new UnknownIdentifierException("Ambiguous code"));

		// WHEN
		final boolean result = workflowTemplateExistsPredicate.test(TEMPLATE_CODE);

		// THEN
		assertFalse(result);
	}

	@Test
	public void givenValidWorkflowTemplateCode_WhenTestIsCalled_ThenItReturnsTrue()
	{
		// WHEN
		final boolean result = workflowTemplateExistsPredicate.test(TEMPLATE_CODE);

		// THEN
		assertTrue(result);
	}
}
