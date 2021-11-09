/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.model.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.integrationservices.model.AttributeValueGetter;
import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.integrationservices.scripting.CannotCreateLogicLocationException;
import de.hybris.platform.integrationservices.virtualattributes.LogicExecutor;
import de.hybris.platform.integrationservices.virtualattributes.LogicExecutorContext;
import de.hybris.platform.integrationservices.virtualattributes.LogicExecutorFactory;
import de.hybris.platform.integrationservices.scripting.LogicLocation;
import de.hybris.platform.integrationservices.virtualattributes.LogicParams;
import de.hybris.platform.integrationservices.virtualattributes.VirtualAttributeConfigurationException;

import java.util.Collections;
import java.util.Locale;
import java.util.Map;

import javax.validation.constraints.NotNull;

import com.google.common.base.Preconditions;

/**
 * Gets the virtual attribute values in the platform
 */
class VirtualAttributeValueGetter implements AttributeValueGetter
{
	private static final Log LOG = Log.getLogger(VirtualAttributeValueGetter.class);
	private final LogicExecutor logicExecutor;

	/**
	 * Instantiates the getter for virtual attribute values.
	 *
	 * @param descriptor           virtual attribute descriptor
	 * @param logicExecutorFactory factory to create {@link de.hybris.platform.integrationservices.virtualattributes.LogicExecutor}s
	 */
	VirtualAttributeValueGetter(@NotNull final VirtualTypeAttributeDescriptor descriptor,
	                            @NotNull final LogicExecutorFactory logicExecutorFactory)
	{
		Preconditions.checkArgument(descriptor != null, "Attribute descriptor must be provided");
		Preconditions.checkArgument(logicExecutorFactory != null, "Logic Executor Factory must be provided");
		this.logicExecutor = createLogicExecutor(descriptor, logicExecutorFactory);
	}

	private LogicLocation getLogicLocation(final VirtualTypeAttributeDescriptor descriptor)
	{
		try
		{
			return LogicLocation.from(descriptor.getLogicLocation());
		}
		catch (final CannotCreateLogicLocationException e)
		{
			LOG.trace(e.getMessage());
			throw new VirtualAttributeConfigurationException(descriptor);
		}
	}

	@Override
	public Object getValue(final Object model)
	{
		return logicExecutor.execute(LogicParams.create((ItemModel) model));
	}

	@Override
	public Object getValue(final Object model, final Locale locale)
	{
		return null;
	}

	@Override
	public Map<Locale, Object> getValues(final Object model, final Locale... locales)
	{
		return Collections.emptyMap();
	}

	private LogicExecutor createLogicExecutor(final VirtualTypeAttributeDescriptor descriptor, final LogicExecutorFactory factory){
		final LogicExecutorContext context = LogicExecutorContext.create(getLogicLocation(descriptor), descriptor);
		return factory.create(context);
	}
}
