/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.common.predicate;

import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.workflow.WorkflowTemplateService;

import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;


/**
 * Predicate to test if a given template code maps to a valid workflow template.
 * <p>
 * Returns <tt>TRUE</tt> if the template exists; <tt>FALSE</tt> otherwise.
 * </p>
 */
public class WorkflowTemplateExistsPredicate implements Predicate<String>
{
	private static final Logger LOG = LoggerFactory.getLogger(WorkflowTemplateExistsPredicate.class);

	private WorkflowTemplateService workflowTemplateService;

	@Override
	public boolean test(final String templateCode)
	{
		boolean result;
		try
		{
			getWorkflowTemplateService().getWorkflowTemplateForCode(templateCode);
			result = true;
		}
		catch (final UnknownIdentifierException | AmbiguousIdentifierException e)
		{
			LOG.debug("Unable to find the workflow template", e);
			result = false;
		}

		return result;
	}

	protected WorkflowTemplateService getWorkflowTemplateService()
	{
		return workflowTemplateService;
	}

	@Required
	public void setWorkflowTemplateService(final WorkflowTemplateService workflowTemplateService)
	{
		this.workflowTemplateService = workflowTemplateService;
	}
}
