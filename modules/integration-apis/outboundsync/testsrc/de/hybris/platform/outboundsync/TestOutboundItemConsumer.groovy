/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.outboundsync

import de.hybris.platform.outboundsync.activator.OutboundItemConsumer
import de.hybris.platform.outboundsync.dto.OutboundItemDTO
import org.junit.rules.ExternalResource

import java.util.concurrent.atomic.AtomicInteger

class TestOutboundItemConsumer extends ExternalResource implements OutboundItemConsumer
{
	AtomicInteger invocations

	@Override
	void consume(OutboundItemDTO outboundItemDTO)
	{
		invocations.incrementAndGet()
	}

	int invocations()
	{
		invocations.get()
	}

	void reset() {
		invocations = new AtomicInteger(0)
	}

	@Override
	protected void before()
	{
		reset()
	}

	@Override
	protected void after() {
		reset()
	}
}