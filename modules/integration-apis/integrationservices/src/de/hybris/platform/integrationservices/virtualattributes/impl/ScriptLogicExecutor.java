/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.virtualattributes.impl;

import de.hybris.platform.integrationservices.virtualattributes.LogicExecutor;
import de.hybris.platform.integrationservices.virtualattributes.LogicExecutorContext;
import de.hybris.platform.integrationservices.scripting.LogicLocationScheme;
import de.hybris.platform.integrationservices.virtualattributes.LogicParams;
import de.hybris.platform.integrationservices.virtualattributes.VirtualAttributeConfigurationException;
import de.hybris.platform.integrationservices.virtualattributes.VirtualAttributeExecutionException;
import de.hybris.platform.scripting.engine.ScriptExecutable;
import de.hybris.platform.scripting.engine.ScriptExecutionResult;
import de.hybris.platform.scripting.engine.ScriptingLanguagesService;
import de.hybris.platform.scripting.engine.exception.ScriptExecutionException;
import de.hybris.platform.scripting.engine.exception.ScriptingException;

import javax.validation.constraints.NotNull;

import com.google.common.base.Preconditions;

/**
 * Implementation of the {@link LogicExecutor} for executing {@link LogicLocationScheme#MODEL} scripts.
 */
public class ScriptLogicExecutor implements LogicExecutor
{
	private final LogicExecutorContext context;
	private final ScriptingLanguagesService scriptingLanguagesService;

	/**
	 * Instantiates this executor
	 *
	 * @param ctx context for the script execution
	 * @param service service to execute a script
	 */
	public ScriptLogicExecutor(@NotNull final LogicExecutorContext ctx, @NotNull final ScriptingLanguagesService service)
	{
		Preconditions.checkArgument(ctx != null, "LogicExecutorContext must be provided");
		Preconditions.checkArgument(service != null, "ScriptingLanguagesService must be provided");

		context = ctx;
		scriptingLanguagesService = service;
	}

	@Override
	public Object execute(final LogicParams params)
	{
		try
		{
			final ScriptExecutionResult result = getScriptExecutable(context).execute(params.asMap());
			return result.getScriptResult();
		}
		catch (final ScriptExecutionException e)
		{
			throw new VirtualAttributeExecutionException(context, params, e);
		}
		catch (final ScriptingException e)
		{
			throw new VirtualAttributeConfigurationException(context, e);
		}
		catch (final RuntimeException e)
		{
			throw new VirtualAttributeExecutionException(context, params, e);
		}
	}


	private ScriptExecutable getScriptExecutable(final LogicExecutorContext ctx)
	{
		return scriptingLanguagesService.getExecutableByURI(ctx.getLogicLocation().getLocation());
	}
}
