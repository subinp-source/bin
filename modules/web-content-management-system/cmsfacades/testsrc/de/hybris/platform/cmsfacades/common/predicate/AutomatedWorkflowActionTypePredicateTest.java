/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.common.predicate;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.workflow.model.AutomatedWorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;

import org.junit.Test;


@UnitTest
public class AutomatedWorkflowActionTypePredicateTest
{
	private final AutomatedWorkflowActionTypePredicate predicate = new AutomatedWorkflowActionTypePredicate();
	private final WorkflowActionModel action = new WorkflowActionModel();

	@Test
	public void testIsAutomatedWorkflowActionType()
	{
		action.setTemplate(new AutomatedWorkflowActionTemplateModel());

		final boolean result = predicate.test(action);

		assertTrue(result);
	}

	@Test
	public void testIsNotAutomatedWorkflowActionType()
	{
		action.setTemplate(new WorkflowActionTemplateModel());

		final boolean result = predicate.test(action);

		assertFalse(result);
	}

	@Test
	public void testIsNotAutomatedWorkflowActionTypeWithoutAction()
	{
		final boolean result = predicate.test(null);

		assertFalse(result);
	}

	@Test
	public void testIsNotAutomatedWorkflowActionTypeWithoutActionTemplate()
	{
		action.setTemplate(null);

		final boolean result = predicate.test(null);

		assertFalse(result);
	}

}
