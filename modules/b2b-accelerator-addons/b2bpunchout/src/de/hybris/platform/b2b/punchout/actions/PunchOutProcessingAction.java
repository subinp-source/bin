/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.punchout.actions;

/**
 * A generic action that takes an input and depending on its purpose may populate the output.
 * 
 * @param <In>
 *           the input type
 * @param <Out>
 *           the output type
 */
public interface PunchOutProcessingAction<In, Out>
{

	/**
	 * Processes the input and populates the output.
	 * 
	 * @param input
	 *           the input object
	 * @param output
	 *           the output object
	 */
	void process(In input, Out output);

}
