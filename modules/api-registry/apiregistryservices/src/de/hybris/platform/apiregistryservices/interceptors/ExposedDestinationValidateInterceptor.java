/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.interceptors;

import de.hybris.platform.apiregistryservices.model.ExposedDestinationModel;
import de.hybris.platform.apiregistryservices.services.DestinationCredentialService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;

public class ExposedDestinationValidateInterceptor implements ValidateInterceptor<ExposedDestinationModel>
{

	private DestinationCredentialService destinationCredentialService;

	@Override
	public void onValidate(final ExposedDestinationModel exposedDestinationModel, final InterceptorContext interceptorContext)
			throws InterceptorException
	{
		if(!getDestinationCredentialService().isValidDestinationCredential(exposedDestinationModel))
		{
			throw new InterceptorException("Invalid credential type. Exposed credential cannot be assigned to consumed destination!");
		}
	}

	protected DestinationCredentialService getDestinationCredentialService()
	{
		return destinationCredentialService;
	}

	public void setDestinationCredentialService(
			final DestinationCredentialService destinationCredentialService)
	{
		this.destinationCredentialService = destinationCredentialService;
	}
}
