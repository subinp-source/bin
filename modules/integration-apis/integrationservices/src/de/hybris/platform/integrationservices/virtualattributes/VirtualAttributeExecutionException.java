/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.virtualattributes;

import de.hybris.platform.integrationservices.exception.IntegrationAttributeProcessingException;
import de.hybris.platform.scripting.engine.exception.DisabledScriptException;

/**
 * Indicates a problem with execution of the script used in a virtual attribute configuration. This means that the attribute
 * configuration is valid, i.e. the script is found and loaded, but the script crashed during execution. The crash
 * may indicate a bug in the script or unexpected script parameter input, however after the script crashes at least
 * once it becomes disabled. Therefore correcting the payload or the data and repeating the request may not be sufficient.
 * The script may need to be re-enabled in the backoffice too.
 * See "Autodisabling of Model-Based Scripts" in https://help.sap.com/viewer/d0224eca81e249cb821f2cdf45a82ace/2005/en-US/8bec04a386691014938a9996a977d07f.html
 */
public class VirtualAttributeExecutionException extends IntegrationAttributeProcessingException
{
	private static final String MSG_TEMPLATE = "Execution of script located @ %s for virtual attribute %s has failed with %s parameters. %s";

	private final transient LogicParams scriptParameters;
	private final transient LogicExecutorContext executorContext;

	/**
	 * Instantiates this message with the specified context.
	 * @param c context for the failed execution.
	 * @param p parameters passed to the script.
	 * @param e intercepted original exception, which caused this exception to be thrown.
	 */
	public VirtualAttributeExecutionException(final LogicExecutorContext c,
	                                          final LogicParams p,
	                                          final RuntimeException e)
	{
		super(message(c, p, e), c.getDescriptor(), e);
		executorContext = c;
		scriptParameters = p;
	}

	private static String message(final LogicExecutorContext c, final LogicParams p,
	                              final RuntimeException e)
	{
		final String details = e instanceof DisabledScriptException
				? "The script is disable and must be re-enabled."
				: "The script may be disabled now.";
		return String.format(MSG_TEMPLATE, c.getLogicLocation(), c.getDescriptor(), p, details);
	}

	/**
	 * Retrieves parameters used by the script
	 *
	 * @return parameters that were passed to the script when it failed.
	 */
	public LogicParams getScriptParameters()
	{
		return scriptParameters;
	}

	/**
	 * Retrieves context of the failed {@link LogicExecutor}
	 * @return context used by the {@link LogicExecutor} that failed and caused this exception.
	 */
	public LogicExecutorContext getExecutorContext()
	{
		return executorContext;
	}
}
