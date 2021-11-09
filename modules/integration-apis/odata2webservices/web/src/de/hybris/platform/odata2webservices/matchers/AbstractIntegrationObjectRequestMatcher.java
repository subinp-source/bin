/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2webservices.matchers;

import de.hybris.platform.inboundservices.enums.AuthenticationType;
import de.hybris.platform.inboundservices.model.InboundChannelConfigurationModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectModel;
import de.hybris.platform.integrationservices.service.IntegrationObjectService;
import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.odata2services.odata.processor.ServiceNameExtractor;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * Abstract implementation containing common logic for the Integration Services request matchers.
 */
public abstract class AbstractIntegrationObjectRequestMatcher implements RequestMatcher
{
	private static final Logger LOGGER = Log.getLogger(AbstractIntegrationObjectRequestMatcher.class);

	private final IntegrationObjectService integrationObjectService;
	private final ServiceNameExtractor serviceNameExtractor;
	private final FlexibleSearchService flexibleSearchService;

	public AbstractIntegrationObjectRequestMatcher(
			final IntegrationObjectService integrationObjectService,
			final ServiceNameExtractor serviceNameExtractor,
			final FlexibleSearchService flexibleSearchService)
	{
		this.integrationObjectService = integrationObjectService;
		this.serviceNameExtractor = serviceNameExtractor;
		this.flexibleSearchService = flexibleSearchService;
	}

	@Override
	public boolean matches(final HttpServletRequest httpServletRequest)
	{
		final String ioCode = serviceNameExtractor.extract(httpServletRequest);
		return findIntegrationObject(ioCode).map(this::isMatchInboundChannelConfigAuthType)
		                                    .orElseGet(() -> isMatchWhenIntegrationObjectNotFound(httpServletRequest));
	}

	private Optional<IntegrationObjectModel> findIntegrationObject(final String ioCode)
	{
		try
		{
			final IntegrationObjectModel ioModel = integrationObjectService.findIntegrationObject(ioCode);
			return Optional.of(ioModel);
		}
		catch (final ModelNotFoundException | IllegalArgumentException e)
		{
			LOGGER.trace("IntegrationObject not found for code: '{}'", ioCode, e);
			return Optional.empty();
		}
	}

	private boolean isMatchInboundChannelConfigAuthType(final IntegrationObjectModel ioModel)
	{
		try
		{
			final InboundChannelConfigurationModel cf = new InboundChannelConfigurationModel();
			cf.setIntegrationObject(ioModel);
			final InboundChannelConfigurationModel inboundChannelConfigurationModel = flexibleSearchService.getModelByExample(
					cf);
			return matchesAuthenticationType(inboundChannelConfigurationModel.getAuthenticationType());
		}
		catch (final ModelNotFoundException | IllegalArgumentException e)
		{
			LOGGER.trace("InboundChannelConfiguration not found for Integration Object code: '{}'", ioModel.getCode(), e);
			return matchesSecurityLegacyMode();
		}
	}

	private boolean isMatchWhenIntegrationObjectNotFound(final HttpServletRequest httpServletRequest)
	{
		final var authorization = httpServletRequest.getHeader("Authorization");
		return authorization != null && authorization.toUpperCase().contains(getAuthorizationPrefix().name());
	}

	protected abstract boolean matchesSecurityLegacyMode();

	/**
	 * Retrieves the authorization prefix from the matcher.
	 *
	 * @return the matcher's authorization header value.
	 */
	protected abstract AuthorizationPrefix getAuthorizationPrefix();

	/**
	 * Determines if the matcher applies based on the AuthenticationType derived from the request.
	 *
	 * @param authenticationType derived from the request
	 * @return boolean with the response from the matcher implementation
	 */
	abstract boolean matchesAuthenticationType(final AuthenticationType authenticationType);

}
