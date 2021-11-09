/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.virtualattributes;

import javax.validation.constraints.NotNull;

/**
 * Executor of some logic
 */
public interface LogicExecutor
{
	/**
	 * Executes the logic with the given parameters
	 *
	 * @param params Parameters to be used by the logic.
	 * @return the result of the execution of the logic
	 */
	Object execute(@NotNull LogicParams params);
}
