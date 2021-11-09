/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.virtualattributes;

import javax.validation.constraints.NotNull;

/**
 * Factory to create {@link LogicExecutor}s
 */
public interface LogicExecutorFactory
{
	/**
	 * Creates a {@link LogicExecutor} from the given {@link LogicExecutorContext}
	 * @param context Provides context on how to create the executor
	 * @return a LogicExecutor instance
	 */
	LogicExecutor create(@NotNull LogicExecutorContext context);
}
