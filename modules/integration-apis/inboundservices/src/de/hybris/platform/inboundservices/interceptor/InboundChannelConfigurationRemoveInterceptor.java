/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.inboundservices.interceptor;

import de.hybris.platform.inboundservices.model.InboundChannelConfigurationModel;
import de.hybris.platform.inboundservices.strategies.ICCDeletionValidationStrategy;
import de.hybris.platform.inboundservices.strategies.impl.DefaultICCDeletionValidationStrategy;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;

public class InboundChannelConfigurationRemoveInterceptor implements RemoveInterceptor<InboundChannelConfigurationModel>
{
	private ICCDeletionValidationStrategy iccDeletionValidationStrategy;

	public ICCDeletionValidationStrategy getIccDeletionValidationStrategy()
	{
		return iccDeletionValidationStrategy;
	}

	public void setIccDeletionValidationStrategy(
			final DefaultICCDeletionValidationStrategy iccDeletionValidationStrategy)
	{
		this.iccDeletionValidationStrategy = iccDeletionValidationStrategy;
	}

	@Override
	public void onRemove(final InboundChannelConfigurationModel inboundChannelConfigurationModel,
	                     final InterceptorContext interceptorContext)
			throws InterceptorException
	{
		iccDeletionValidationStrategy.checkICCLinkedWithExposedDestination(inboundChannelConfigurationModel);
	}

}
