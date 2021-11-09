/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.virtualattributes.impl;

import de.hybris.platform.integrationservices.virtualattributes.LogicExecutor;
import de.hybris.platform.integrationservices.virtualattributes.LogicExecutorContext;
import de.hybris.platform.integrationservices.virtualattributes.LogicExecutorFactory;
import de.hybris.platform.integrationservices.scripting.LogicLocationScheme;
import de.hybris.platform.integrationservices.virtualattributes.LogicParams;
import de.hybris.platform.integrationservices.virtualattributes.VirtualAttributeConfigurationException;
import de.hybris.platform.scripting.engine.ScriptingLanguagesService;

import javax.validation.constraints.NotNull;

import com.google.common.base.Preconditions;

/**
 * Default implementation of the {@link LogicExecutorFactory}
 */
public class DefaultLogicExecutorFactory implements LogicExecutorFactory
{
	private final ScriptingLanguagesService scriptingLanguagesService;

	public DefaultLogicExecutorFactory(@NotNull final ScriptingLanguagesService scriptingLanguagesService)
	{
		Preconditions.checkArgument(scriptingLanguagesService != null, "Scripting languages service must be provided");
		this.scriptingLanguagesService = scriptingLanguagesService;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @return An instance of the {@link LogicExecutor} that can handle the provided {@link LogicExecutorContext}.
	 * If no {@link LogicExecutor} can handle the {@link LogicExecutorContext}, a default implementation of the
	 * {@link LogicExecutor} is returned where {@link LogicExecutor#execute(LogicParams)} throws the
	 * {@link VirtualAttributeConfigurationException}.
	 * @throws IllegalArgumentException if the {@code context} parameter is {@code null}
	 */
	@Override
	public LogicExecutor create(@NotNull final LogicExecutorContext context)
	{
		Preconditions.checkArgument(context != null, "LogicExecutorContext must be provided");
		return isScriptLogicLocation(context)
				? new ScriptLogicExecutor(context, scriptingLanguagesService)
				: new NullLogicExecutor(context);
	}

	private boolean isScriptLogicLocation(final LogicExecutorContext context)
	{
		return context.getLogicLocation().getScheme() == LogicLocationScheme.MODEL;
	}

	private static class NullLogicExecutor implements LogicExecutor
	{
		private final LogicExecutorContext context;

		private NullLogicExecutor(final LogicExecutorContext context)
		{
			this.context = context;
		}

		@Override
		public Object execute(final LogicParams params)
		{
			final var info = "Schema " + context.getLogicLocation().getScheme() + " is unsupported in logic locations";
			// ideally this should be UnsupportedLogicLocationSchemeException
			throw new VirtualAttributeConfigurationException(context, info);
		}
	}
}
