/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.event;

import de.hybris.platform.servicelayer.event.ClusterAwareEvent;
import de.hybris.platform.servicelayer.event.PublishEventContext;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;

import java.util.List;

import org.springframework.util.CollectionUtils;

/**
 * Event triggering event export enabling
 */
public class EventExportEnabledEvent extends AbstractEvent implements ClusterAwareEvent
{
	private List<Integer> targetNodeIds;

	@Override
	public boolean canPublish(final PublishEventContext publishEventContext)
	{
		return CollectionUtils.isEmpty(targetNodeIds) || targetNodeIds.contains(publishEventContext.getTargetNodeId());
	}

	public List<Integer> getTargetNodeIds()
	{
		return targetNodeIds;
	}

	public void setTargetNodeIds(final List<Integer> targetNodeIds)
	{
		this.targetNodeIds = targetNodeIds;
	}
}
