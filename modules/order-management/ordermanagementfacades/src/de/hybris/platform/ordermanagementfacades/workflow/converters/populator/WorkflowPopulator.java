/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 *
 */
package de.hybris.platform.ordermanagementfacades.workflow.converters.populator;

import de.hybris.platform.converters.Converters;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.ordermanagementfacades.workflow.data.WorkflowActionData;
import de.hybris.platform.ordermanagementfacades.workflow.data.WorkflowData;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowModel;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Converter implementation for {@link WorkflowModel} as source and {@link WorkflowData} as target type.
 */
public class WorkflowPopulator implements Populator<WorkflowModel, WorkflowData>
{
	private Converter<WorkflowActionModel, WorkflowActionData> workflowActionConverter;

	@Override
	public void populate(final WorkflowModel source, final WorkflowData target)
	{
		if (source != null && target != null)
		{
			target.setCode(source.getCode());
			target.setName(source.getName());
			target.setDescription(source.getDescription());
			if (!CollectionUtils.isEmpty(source.getActions()))
			{
				target.setActions(Converters.convertAll(source.getActions(), getWorkflowActionConverter()));
			}
		}
	}

	protected Converter<WorkflowActionModel, WorkflowActionData> getWorkflowActionConverter()
	{
		return workflowActionConverter;
	}

	@Required
	public void setWorkflowActionConverter(final Converter<WorkflowActionModel, WorkflowActionData> workflowActionConverter)
	{
		this.workflowActionConverter = workflowActionConverter;
	}
}
