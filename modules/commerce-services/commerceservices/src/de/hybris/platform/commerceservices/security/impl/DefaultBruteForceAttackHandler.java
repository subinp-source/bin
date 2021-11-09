/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.security.impl;

import de.hybris.platform.commerceservices.security.BruteForceAttackHandler;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.util.Assert;


/**
 * Default implementation of {@link BruteForceAttackHandler}
 */
public class DefaultBruteForceAttackHandler implements BruteForceAttackHandler
{
	private final ConcurrentHashMap<String, Attempts> bruteForceAttackCache;

	private final Integer maxAttempts;
	private final Integer timeFrame;
	private final Integer waitTime;
	private final Integer cacheSizeLimit;

	private static final double HALF_SIZE = 0.5;

	public DefaultBruteForceAttackHandler(final Integer maxAttempts, final Integer waitTime, final Integer timeFrame,
			final Integer cacheSizeLimit)
	{
		Assert.notNull(maxAttempts, "Constructor param maxAttempts must not be null.");
		Assert.notNull(waitTime, "Constructor param waitTime must not be null.");
		Assert.notNull(timeFrame, "Constructor param timeFrame must not be null.");
		Assert.notNull(cacheSizeLimit, "Constructor param cacheSizeLimit must not be null.");
		Assert.isTrue(waitTime >= timeFrame, "waitTime should be >= timeFrame");

		bruteForceAttackCache = new ConcurrentHashMap((int) (HALF_SIZE * cacheSizeLimit.intValue()));
		this.maxAttempts = maxAttempts;
		this.waitTime = waitTime;
		this.timeFrame = timeFrame;
		this.cacheSizeLimit = cacheSizeLimit;
	}

	@Override
	public boolean registerAttempt(final String key)
	{
		return registerAttempt(key, new Date());
	}

	/**
	 * Internal implementation of register attempt
	 */
	protected boolean registerAttempt(final String key, final Date now)
	{
		if (StringUtils.isNotEmpty(key))
		{
			final Attempts attempts = getAttempts(key);
			synchronized (attempts)
			{
				if (attempts.isAttack())
				{
					final Date dateLimit = DateUtils.addSeconds(now, 0 - waitTime.intValue());
					final boolean afterWait = attempts.getLastTime().before(dateLimit);
					if (afterWait)
					{
						resetAttemptCounter(key);
					}

					attempts.addTime(now);
					return !afterWait;
				}
				else
				{
					final Date dateLimit = DateUtils.addSeconds(now, 0 - timeFrame.intValue());
					final boolean isAttack = maxAttempts.compareTo(attempts.getCounter()) <= 0
							&& attempts.getFirstTime().after(dateLimit);
					if (isAttack)
					{
						attempts.setAttack(isAttack);
					}

					attempts.addTime(now);
					return isAttack;
				}
			}
		}
		return false;
	}

	@Override
	public void resetAttemptCounter(final String key)
	{
		if (StringUtils.isNotEmpty(key))
		{
			bruteForceAttackCache.remove(key);
		}
	}

	protected Attempts getAttempts(final String key)
	{
		final Attempts newAttempts = new Attempts();
		final Attempts storedAttempts = bruteForceAttackCache.putIfAbsent(key, newAttempts);
		if (storedAttempts == null)
		{
			if (bruteForceAttackCache.size() > cacheSizeLimit.intValue())
			{
				evict();
			}
			return newAttempts;
		}
		return storedAttempts;
	}

	protected void evict()
	{
		final Iterator<String> cacheIterator = bruteForceAttackCache.keySet().iterator();
		final Date dateLimit = DateUtils.addSeconds(new Date(), 0 - waitTime.intValue());
		while (cacheIterator.hasNext())
		{
			final String userKey = cacheIterator.next();
			final Attempts attempts = bruteForceAttackCache.get(userKey);
			if (attempts.getLastTime().before(dateLimit))
			{
				cacheIterator.remove();
			}
		}
	}


	public class Attempts
	{
		private final LinkedList<Date> times;
		private boolean isAttack;

		public Attempts()
		{
			times = new LinkedList<>();
			isAttack = false;
		}

		public boolean isAttack()
		{
			return isAttack;
		}

		public void setAttack(final boolean isAttack)
		{
			this.isAttack = isAttack;
		}

		public void addTime(final Date time)
		{
			synchronized (times)
			{
				times.add(time);
				if (times.size() > maxAttempts)
				{
					times.remove();
				}
			}
		}

		public Date getFirstTime()
		{
			return times.peek();
		}

		public Date getLastTime()
		{
			return times.peekLast();
		}

		public int getCounter()
		{
			return times.size();
		}
	}
}
