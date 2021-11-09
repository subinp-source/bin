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

import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordermanagementfacades.workflow.data.WorkflowActionAttachmentItemData;
import de.hybris.platform.ordermanagementfacades.workflow.data.WorkflowActionData;
import de.hybris.platform.workflow.model.WorkflowActionModel;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;


/**
 * Converter implementation for {@link de.hybris.platform.workflow.model.WorkflowActionModel} as source and
 * {@link de.hybris.platform.ordermanagementfacades.workflow.data.WorkflowActionData} as target type.
 */
public class WorkflowActionPopulator implements Populator<WorkflowActionModel, WorkflowActionData>
{
	private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowActionPopulator.class);

	@Override
	public void populate(final WorkflowActionModel source, final WorkflowActionData target)
	{
		if (source != null && target != null)
		{
			target.setCode(source.getCode());
			target.setName(source.getName());
			target.setComment(source.getComment());
			target.setDescription(source.getDescription());
			target.setCreationTime(source.getCreationtime());
			target.setWorkflowCode(source.getWorkflow().getCode());
			target.setStatus(source.getStatus());
			final List<WorkflowActionAttachmentItemData> attachmentItemDataList = source.getAttachmentItems().stream().map(item -> {
				final WorkflowActionAttachmentItemData attachmentItemData = new WorkflowActionAttachmentItemData();
				attachmentItemData.setCode(invokeMethod(item, "getCode", String.class));
				final OrderModel order = invokeMethod(item, "getOrder", OrderModel.class);
				if (order != null)
				{
					attachmentItemData.setOrderCode(order.getCode());
				}
				return attachmentItemData;
			}).collect(Collectors.toList());
			target.setAttachmentItems(attachmentItemDataList);
		}
	}

	/**
	 * Use reflection to invoke a method of an object
	 *
	 * @param object
	 * 		the object to invoke on
	 * @param name
	 * 		the method name
	 * @param targetClass
	 * 		the target class of return type
	 * @return
	 *       value of the invocation
	 */
	protected <T> T invokeMethod(final Object object, final String name, final Class<T> targetClass)
	{
		T result = null;
		if (object != null)
		{
			try
			{
				final Method method = ReflectionUtils.findMethod(object.getClass(), name);
				result = method != null ? targetClass.cast(ReflectionUtils.invokeMethod(method, object)) : null;
			}
			catch (final ClassCastException e) //NOSONAR
			{
				LOGGER.error(e.getMessage());
			}
		}
		return result;
	}
}
