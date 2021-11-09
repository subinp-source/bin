/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.inboundservices.interceptor;

import de.hybris.platform.apiregistryservices.model.AbstractCredentialModel;
import de.hybris.platform.apiregistryservices.model.BasicCredentialModel;
import de.hybris.platform.apiregistryservices.model.ExposedDestinationModel;
import de.hybris.platform.apiregistryservices.model.ExposedOAuthCredentialModel;
import de.hybris.platform.inboundservices.enums.AuthenticationType;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;

/**
 * Interceptor to validate the ExposedDestination credential
 * {@link de.hybris.platform.apiregistryservices.model.ExposedDestinationModel}
 * type matches the assigned InboundChannelConfiguration credential type.
 */
public class ExposedDestinationICCMatchedCredentialValidateInterceptor implements ValidateInterceptor<ExposedDestinationModel>
{
	private static final String ERROR_MESSAGE =
			"Credential type of ExposedDestination [%s] does not match assigned credential " + "type of " +
					"InboundChannelConfiguration [%s] ";

	@Override
	public void onValidate(final ExposedDestinationModel exposedDestinationModel, final InterceptorContext interceptorContext)
			throws InterceptorException
	{
		final AuthenticationType iccAuthenticationType = exposedDestinationModel.getInboundChannelConfiguration() != null
												? exposedDestinationModel.getInboundChannelConfiguration().getAuthenticationType()
												: null;

		final AbstractCredentialModel destinationCredential = exposedDestinationModel.getCredential();

		if (!isMatchedCredentials(destinationCredential, iccAuthenticationType))
		{
			throw new InterceptorException(String.format(ERROR_MESSAGE, destinationCredential != null
																 ? destinationCredential.getItemtype()
																 : "exposed destination credential is null",iccAuthenticationType));
		}
	}

	private boolean isMatchedCredentials(final AbstractCredentialModel exposedDestinationCredential,
			final AuthenticationType inboundChannelConfigurationAuthenticationType)
	{
		if (inboundChannelConfigurationAuthenticationType == null)
		{
			return true;
		}
		else if (exposedDestinationCredential instanceof ExposedOAuthCredentialModel)
		{
			return AuthenticationType.OAUTH.equals(inboundChannelConfigurationAuthenticationType);
		}
		else if (exposedDestinationCredential instanceof BasicCredentialModel)
		{
			return AuthenticationType.BASIC.equals(inboundChannelConfigurationAuthenticationType);
		}

		return false;
	}
}
