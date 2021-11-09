/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.jmx;

import de.hybris.platform.jmx.mbeans.impl.AbstractJMXMBean;

import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.support.management.Statistics;


/**
 * @see QueueInfoBean
 */
public class QueueInfoBeanImpl extends AbstractJMXMBean implements QueueInfoBean
{
	private String beanName;
	private QueueChannel channel;

	@Override
	public String getBeanName()
	{
		return beanName;
	}

	public void setBeanName(final String beanName)
	{
		this.beanName = beanName;
	}

	public QueueChannel getChannel()
	{
		return channel;
	}

	public void setChannel(final QueueChannel channel)
	{
		this.channel = channel;
	}

	@Override
	public int getQueueSize()
	{
		return channel.getQueueSize();
	}

	@Override
	public int getRemainingCapacity()
	{
		return channel.getRemainingCapacity();
	}

	@Override
	public long getSendCountLong()
	{
		return channel.getSendCount();
	}

	@Override
	public int getSendErrorCount()
	{
		return channel.getSendErrorCount();
	}

	@Override
	public long getSendErrorCountLong()
	{
		return channel.getSendErrorCountLong();
	}

	@Override
	public double getTimeSinceLastSend()
	{
		return channel.getTimeSinceLastSend();
	}

	@Override
	public double getMeanSendRate()
	{
		return channel.getMeanSendRate();
	}

	@Override
	public double getMeanErrorRate()
	{
		return channel.getMeanErrorRate();
	}

	@Override
	public double getMeanErrorRatio()
	{
		return channel.getMeanErrorRatio();
	}

	@Override
	public double getMeanSendDuration()
	{
		return channel.getMeanSendDuration();
	}

	@Override
	public double getMinSendDuration()
	{
		return channel.getMinSendDuration();
	}

	@Override
	public double getMaxSendDuration()
	{
		return channel.getMaxSendDuration();
	}

	@Override
	public double getStandardDeviationSendDuration()
	{
		return channel.getStandardDeviationSendDuration();
	}

	/**
	 * @deprecated since 2005
	 */
	@Deprecated(since = "2005", forRemoval = true)
	@Override
	public Statistics getSendDuration()
	{
		return channel.getSendDuration();
	}

	/**
	 * @deprecated since 2005
	 */
	@Deprecated(since = "2005", forRemoval = true)
	@Override
	public Statistics getSendRate()
	{
		return channel.getSendRate();
	}

	/**
	 * @deprecated since 2005
	 */
	@Deprecated(since = "2005", forRemoval = true)
	@Override
	public Statistics getErrorRate()
	{
		return channel.getErrorRate();
	}

	@Override
	public String toString()
	{
		return "QueueInfoBeanImpl{" +
				"beanName='" + beanName + '\'' +
				", channel=" + channel +
				'}';
	}
}
