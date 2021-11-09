/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.dataimport.batch;

/**
 * General task for execution in a batch pipeline.
 */
public interface HeaderTask
{
	/**
	 * Executes a task with a predefined {@link BatchHeader} identifying all relevant process information.
	 * 
	 * @param header
	 * @return the header
	 */
	BatchHeader execute(BatchHeader header) throws Exception;// NOSONAR

}
