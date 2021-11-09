/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.outboundsync

import de.hybris.platform.outboundsync.dto.OutboundItemDTO
import de.hybris.platform.outboundsync.job.ItemChangeSender
import org.junit.rules.ExternalResource

import java.util.concurrent.ConcurrentLinkedQueue

class TestItemChangeSender extends ExternalResource implements ItemChangeSender {

	Queue<OutboundItemDTO> items

	@Override
	void send(final OutboundItemDTO change) {
		items.add(change)
	}

	int getQueueSize() {
		items.size()
	}

	OutboundItemDTO getNextItem() {
		items.poll()
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

	void reset() {
		items = new ConcurrentLinkedQueue<>()
	}
}
