/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2webservices.matchers;

import de.hybris.platform.inboundservices.config.InboundServicesConfiguration;
import de.hybris.platform.inboundservices.enums.AuthenticationType;
import de.hybris.platform.integrationservices.service.IntegrationObjectService;
import de.hybris.platform.odata2services.odata.processor.ServiceNameExtractor;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

/**
 * Matcher to determine if Basic Authentication should apply to a request
 */
public class BasicAuthMatcher extends AbstractIntegrationObjectRequestMatcher
{
	private final InboundServicesConfiguration inboundServicesConfiguration;

	public BasicAuthMatcher(
			final IntegrationObjectService integrationObjectService,
			final ServiceNameExtractor serviceNameExtractor,
			final FlexibleSearchService flexibleSearchService,
			final InboundServicesConfiguration inboundServicesConfiguration)
	{
		super(integrationObjectService, serviceNameExtractor, flexibleSearchService);
		this.inboundServicesConfiguration = inboundServicesConfiguration;
	}

	@Override
	protected boolean matchesSecurityLegacyMode()
	{
		return inboundServicesConfiguration.isLegacySecurity();
	}

	@Override
	boolean matchesAuthenticationType(final AuthenticationType authenticationType)
	{
		return AuthenticationType.BASIC.equals(authenticationType);
	}

	@Override
	protected AuthorizationPrefix getAuthorizationPrefix(){
		return AuthorizationPrefix.BASIC;
	}
}
