/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2webservices.matchers;

import de.hybris.platform.inboundservices.enums.AuthenticationType;
import de.hybris.platform.integrationservices.service.IntegrationObjectService;
import de.hybris.platform.odata2services.odata.processor.ServiceNameExtractor;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

/**
 * Matcher to determine if OAuth Authentication should apply to a request
 */
public class OAuthMatcher extends AbstractIntegrationObjectRequestMatcher
{

	public OAuthMatcher(
			final IntegrationObjectService integrationObjectService,
			final ServiceNameExtractor serviceNameExtractor,
			final FlexibleSearchService flexibleSearchService)
	{
		super(integrationObjectService, serviceNameExtractor, flexibleSearchService);
	}

	@Override
	protected boolean matchesSecurityLegacyMode()
	{
		return false;
	}

	@Override
	boolean matchesAuthenticationType(final AuthenticationType authenticationType)
	{
		return AuthenticationType.OAUTH.equals(authenticationType);
	}

	@Override
	protected AuthorizationPrefix getAuthorizationPrefix(){
		return AuthorizationPrefix.BEARER;
	}
}
