/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.security.impl;

import de.hybris.bootstrap.annotations.UnitTest;

import java.util.Date;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * test for {@link DefaultBruteForceAttackHandlerTest}
 */
@UnitTest
public class DefaultBruteForceAttackHandlerTest
{
	private static final String KEY = "test-key";

	private static final Integer maxFailedAttempts = 3;
	private static final Integer waitTime = 3;
	private static final Integer timeFrame = 2;

	private DefaultBruteForceAttackHandler bruteForceAttackHandler;

	@Before
	public void setUp()
	{
		bruteForceAttackHandler = new DefaultBruteForceAttackHandler(maxFailedAttempts, waitTime, timeFrame, 100);
		bruteForceAttackHandler.resetAttemptCounter(KEY);
	}

	@Test
	public void shouldRegisterAttempt()
	{
		bruteForceAttackHandler.registerAttempt(KEY);
		Assert.assertEquals(1, bruteForceAttackHandler.getAttempts(KEY).getCounter());
	}

	@Test
	public void shouldCheckAttack()
	{
		bruteForceAttackHandler.registerAttempt(KEY);
		bruteForceAttackHandler.registerAttempt(KEY);

		boolean isAttack = false;
		isAttack = bruteForceAttackHandler.registerAttempt(KEY);
		Assert.assertFalse(isAttack);

		isAttack = bruteForceAttackHandler.registerAttempt(KEY);
		Assert.assertTrue(isAttack);
	}

	@Test
	public void shouldResetAttemptCounter()
	{
		bruteForceAttackHandler.resetAttemptCounter(KEY);
		Assert.assertEquals(0, bruteForceAttackHandler.getAttempts(KEY).getCounter());
	}

	@Test
	public void shouldEnableRetry() throws InterruptedException
	{
		boolean isAttack = false;
		for (int i = 0; i < maxFailedAttempts; i++)
		{
			bruteForceAttackHandler.registerAttempt(KEY);
		}

		isAttack = bruteForceAttackHandler.registerAttempt(KEY);
		Assert.assertTrue(isAttack);

		isAttack = bruteForceAttackHandler.registerAttempt(KEY, DateUtils.addSeconds(new Date(), waitTime + 1));
		Assert.assertFalse(isAttack);
	}

	@Test
	public void shouldRegisterAttemptsConcurrently() throws InterruptedException
	{
		final Date globalFakeTime = new Date();
		final AtomicInteger blockCounter = new AtomicInteger();
		final AtomicInteger passCounter = new AtomicInteger();
		final CyclicBarrier startTogether = new CyclicBarrier(3);

		final ExecutorService executor = Executors.newFixedThreadPool(3);
		for (int i = 0; i < 3; i++)
		{
			executor.submit(() -> {
				startTogether.await();
				final Date fakeTime = globalFakeTime;
				for (int j = 0; j < 10; j++)
				{
					if (bruteForceAttackHandler.registerAttempt(KEY, fakeTime))
					{
						blockCounter.incrementAndGet();
					}
					else
					{
						passCounter.incrementAndGet();
					}
				}
				return null;
			});
		}
		executor.shutdown();
		executor.awaitTermination(4, TimeUnit.SECONDS);

		Assert.assertEquals(30 - maxFailedAttempts, blockCounter.get());
		Assert.assertEquals((int) maxFailedAttempts, passCounter.get());
	}
}
