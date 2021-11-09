/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.common.predicate;

import de.hybris.platform.workflow.model.AutomatedWorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowActionModel;

import java.util.Objects;
import java.util.function.Predicate;


/**
 * Predicate to verify if the provided workflow action model is of type {@link AutomatedWorkflowActionTemplateModel}.
 */
public class AutomatedWorkflowActionTypePredicate implements Predicate<WorkflowActionModel>
{

	@Override
	public boolean test(final WorkflowActionModel action)
	{
		if (Objects.nonNull(action) && Objects.nonNull(action.getTemplate()))
		{
			return AutomatedWorkflowActionTemplateModel.class.isAssignableFrom(action.getTemplate().getClass());
		}
		return false;
	}

}
