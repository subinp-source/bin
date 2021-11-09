/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundsync.config.impl;

import de.hybris.deltadetection.model.StreamConfigurationModel;
import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.outboundsync.config.ChannelConfigurationFactory;
import de.hybris.platform.outboundsync.model.OutboundChannelConfigurationModel;
import de.hybris.platform.outboundsync.model.OutboundSyncCronJobModel;
import de.hybris.platform.outboundsync.model.OutboundSyncJobModel;
import de.hybris.platform.outboundsync.model.OutboundSyncStreamConfigurationContainerModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DefaultChannelConfigurationPrepareInterceptor implements PrepareInterceptor<OutboundChannelConfigurationModel>
{
	private static final Logger LOG = Log.getLogger(DefaultChannelConfigurationPrepareInterceptor.class);

	private ChannelConfigurationFactory factory;

	@Override
	public void onPrepare(final OutboundChannelConfigurationModel channel, final InterceptorContext ctx)
	{
		if (isGenerateConfig(channel, ctx))
		{
			final OutboundSyncStreamConfigurationContainerModel streamContainer = createStreamContainer(channel, ctx);
			createStreams(channel, streamContainer, ctx);
			final OutboundSyncJobModel job = createJob(channel, streamContainer, ctx);
			createCronJob(channel, job, ctx);
		}
	}

	private boolean isGenerateConfig(final OutboundChannelConfigurationModel channel, final InterceptorContext ctx)
	{
		return channel.getAutoGenerate() && ctx.getModelService().isNew(channel);
	}

	private OutboundSyncStreamConfigurationContainerModel createStreamContainer(final OutboundChannelConfigurationModel channel, final InterceptorContext ctx)
	{
		final OutboundSyncStreamConfigurationContainerModel streamContainer = getFactory().createStreamContainer(channel);
		LOG.info("Auto-generated stream container {}: {}", streamContainer.getId(), streamContainer);
		ctx.registerElement(streamContainer);
		return streamContainer;
	}

	private void createStreams(final OutboundChannelConfigurationModel channel, final OutboundSyncStreamConfigurationContainerModel streamContainer, final InterceptorContext ctx)
	{
		getFactory().createStreams(channel, streamContainer)
				.forEach(s -> registerStream(ctx, s));
	}

	private OutboundSyncJobModel createJob(final OutboundChannelConfigurationModel channel, final OutboundSyncStreamConfigurationContainerModel streamContainer, final InterceptorContext ctx)
	{
		final OutboundSyncJobModel job = getFactory().createJob(channel, streamContainer);
		LOG.info("Auto-generated job {}: {}", job.getCode(), job);
		ctx.registerElement(job);
		return job;
	}

	private void createCronJob(final OutboundChannelConfigurationModel channel, final OutboundSyncJobModel job, final InterceptorContext ctx)
	{
		final OutboundSyncCronJobModel cronJob = getFactory().createCronJob(channel, job);
		LOG.info("Auto-generated cron job {}: {}", cronJob.getCode(), cronJob);
		ctx.registerElement(cronJob);
	}

	private void registerStream(final InterceptorContext ctx, final StreamConfigurationModel stream)
	{
		LOG.info("Auto-generated stream {}: {}", stream.getStreamId(), stream);
		ctx.registerElement(stream);
	}

	protected ChannelConfigurationFactory getFactory()
	{
		return factory;
	}

	@Required
	public void setFactory(final ChannelConfigurationFactory factory)
	{
		this.factory = factory;
	}
}
