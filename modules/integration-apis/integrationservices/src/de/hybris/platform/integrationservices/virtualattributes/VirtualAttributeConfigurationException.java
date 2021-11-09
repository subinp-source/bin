/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.virtualattributes;

import de.hybris.platform.integrationservices.exception.IntegrationAttributeException;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;
import de.hybris.platform.integrationservices.model.impl.VirtualTypeAttributeDescriptor;

/**
 * Indicates a problem related to a virtual attribute configuration. Such exceptions indicate persistent problems with
 * retrieving/providing a specific virtual attribute values regardless of the specific context {@code ItemModel} or other
 * changing factors, e.g. time of execution, permissions, etc. The problem is consistent and can be resolved only by changing the
 * virtual attribute definition or descriptor.
 */
public class VirtualAttributeConfigurationException extends IntegrationAttributeException
{
	private static final String MSG_TEMPLATE = "Configuration for virtual attribute %s in item %s is invalid. %s";
	private final transient LogicExecutorContext executorContext;

	/**
	 * Instantiates this exception with the provided context.
	 *
	 * @param c context for the virtual attribute, that is mis-configured or became invalid over time.
	 * @param e another exception related to the logic location, that caused this exception to be thrown.
	 */
	public VirtualAttributeConfigurationException(final LogicExecutorContext c, final RuntimeException e)
	{
		super(message(c.getDescriptor(), ""), c.getDescriptor(), e);
		executorContext = c;
	}

	/**
	 * Instantiates this exception with the provided descriptor
	 *
	 * @param descriptor descriptor for virtual attribute that is mis-configured.
	 */
	public VirtualAttributeConfigurationException(final VirtualTypeAttributeDescriptor descriptor)
	{
		super(message(descriptor, ""), descriptor);
		executorContext = null;
	}

	/**
	 * Instantiates this exception with the provided context and additional information.
	 *
	 * @param ctx  context for the virtual attribute, that is mis-configured or became invalid over time.
	 * @param info additional information about the failure that is sensitive to be included in the message.
	 */
	public VirtualAttributeConfigurationException(final LogicExecutorContext ctx, final String info)
	{
		super(message(ctx.getDescriptor(), info), ctx.getDescriptor(), null);
		executorContext = ctx;
	}

	private static String message(final TypeAttributeDescriptor descriptor, final String info){
		return String.format(MSG_TEMPLATE, descriptor.getAttributeName(), descriptor.getTypeDescriptor().getItemCode(), info);
	}

	/**
	 * Retrieves context of the failed {@link LogicExecutor}
	 *
	 * @return context used by the {@link LogicExecutor} that failed and caused this exception.
	 */
	public LogicExecutorContext getExecutorContext()
	{
		return executorContext;
	}
}
