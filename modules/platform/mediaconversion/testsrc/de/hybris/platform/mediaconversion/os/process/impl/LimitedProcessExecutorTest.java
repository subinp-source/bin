/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.mediaconversion.os.process.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.mediaconversion.os.ProcessExecutor;
import de.hybris.platform.mediaconversion.os.process.AbstractProcessExecutorTestCase;


/**
 * @author pohl
 */
@UnitTest
public class LimitedProcessExecutorTest extends AbstractProcessExecutorTestCase
{

	/**
	 * @see de.hybris.platform.mediaconversion.os.process.AbstractProcessExecutorTestCase#createExecutor()
	 */
	@Override
	protected ProcessExecutor createExecutor() throws Exception
	{
		return new LimitedProcessExecutor(5, new EmbeddedProcessExecutor());
	}

	@Override
	protected int amountOfThreads()
	{
		return 150;
	}
}
