/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.mediaconversion.os.process;

import de.hybris.bootstrap.annotations.UnitTest;

import org.junit.Assert;
import org.junit.Test;


/**
 * @author pohl
 */
@UnitTest
public class StringDrainTest
{

	@Test
	public void testNoNewlines()
	{
		final StringDrain drain = new StringDrain(false);
		Assert.assertEquals("", drain.toString());
		drain.drain("First line.");
		Assert.assertEquals("First line.", drain.toString());
		drain.drain("Second line.");
		Assert.assertEquals("First line.Second line.", drain.toString());
		drain.drain("Other\nInformation");
		Assert.assertEquals("First line.Second line.Other\nInformation", drain.toString());
	}

	@Test
	public void testAppendNewlines()
	{
		final StringDrain drain = new StringDrain();
		Assert.assertEquals("", drain.toString());
		drain.drain("First line.");
		Assert.assertEquals("First line.\n", drain.toString());
		drain.drain("Second line.");
		Assert.assertEquals("First line.\nSecond line.\n", drain.toString());
		drain.drain("Other\nInformation");
		Assert.assertEquals("First line.\nSecond line.\nOther\nInformation\n", drain.toString());
	}

	@Test
	public void testNullTolerance()
	{
		final StringDrain drain = new StringDrain();
		Assert.assertEquals("", drain.toString());
		drain.drain(null);
		Assert.assertEquals("", drain.toString());
		drain.drain("First line.");
		Assert.assertEquals("First line.\n", drain.toString());
		drain.drain(null);
		Assert.assertEquals("First line.\n", drain.toString());
	}

}
