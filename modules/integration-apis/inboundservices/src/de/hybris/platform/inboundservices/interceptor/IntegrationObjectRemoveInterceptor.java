/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.inboundservices.interceptor;

import de.hybris.platform.inboundservices.model.InboundChannelConfigurationModel;
import de.hybris.platform.inboundservices.persistence.populator.IntegrationObjectDeletionException;
import de.hybris.platform.inboundservices.service.InboundChannelConfigurationService;
import de.hybris.platform.integrationservices.model.IntegrationObjectModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;

import javax.validation.constraints.NotNull;

import com.google.common.base.Preconditions;

/**
 * Interceptor to prevent Integration Object deletion {@link de.hybris.platform.integrationservices.model.IntegrationObjectModel}
 * if it was assigned an InboundChannelConfiguration {@link de.hybris.platform.inboundservices.model.InboundChannelConfigurationModel}.
 */
public class IntegrationObjectRemoveInterceptor implements RemoveInterceptor<IntegrationObjectModel>
{
	private InboundChannelConfigurationService defaultInboundChannelConfigurationService;

	/**
	 * Constructor
	 * @param defaultInboundChannelConfigurationService
	 * 		to query if integration object {@link IntegrationObjectModel}
	 * 		was assigned inbound channel configurations {@link InboundChannelConfigurationModel}.
	 */
	public IntegrationObjectRemoveInterceptor(
			@NotNull final InboundChannelConfigurationService defaultInboundChannelConfigurationService)
	{
		Preconditions.checkArgument(defaultInboundChannelConfigurationService != null,
									"Non-null InboundChannelConfigurationService must be provided");
		this.defaultInboundChannelConfigurationService = defaultInboundChannelConfigurationService;
	}

	@Override
	public void onRemove(final IntegrationObjectModel integrationObject, final InterceptorContext ctx) throws InterceptorException
	{
		if (defaultInboundChannelConfigurationService.findInboundChannelConfigurationByIntegrationObject(integrationObject)
				.isPresent())
		{
			throw new IntegrationObjectDeletionException(integrationObject.getCode());
		}
	}
}