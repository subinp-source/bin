/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.mediaconversion.imagemagick;

import de.hybris.bootstrap.annotations.UnitTest;

import org.apache.log4j.Level;
import org.junit.Assert;
import org.junit.Test;


/**
 * @author pohl
 */
@UnitTest
public class LoggingDrainTest
{
	@Test
	public void testNullTolerance()
	{
		final LoggingDrain drain = new LoggingDrain(this.getClass(), Level.DEBUG);
		drain.drain("Test output only...");
		drain.drain(null);
		Assert.assertNotNull(drain);
	}
}
