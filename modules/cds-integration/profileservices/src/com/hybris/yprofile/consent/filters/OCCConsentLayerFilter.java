/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.yprofile.consent.filters;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.user.UserService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.filter.OncePerRequestFilter;

import com.hybris.yprofile.consent.services.ConsentService;
import com.hybris.yprofile.services.ProfileConfigurationService;

import static com.hybris.yprofile.constants.ProfileservicesConstants.DEBUG_HEADER_NAME;


public class OCCConsentLayerFilter extends OncePerRequestFilter
{

	private static final Logger LOG = Logger.getLogger(OCCConsentLayerFilter.class);

	private ConsentService consentService;
	private UserService userService;
	private ProfileConfigurationService profileConfigurationService;
	private ConfigurationService configurationService;


	@Override
	protected void doFilterInternal(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse,
			final FilterChain filterChain) throws ServletException, IOException
	{

		if (isProfileTrackingConsentGiven(httpServletRequest) && isActiveAccount())
		{
			LOG.debug("Profile tracking consent given");
			populateDebugFlagInSession(httpServletRequest);
			getConsentService().saveConsentReferenceInSessionAndCurrentUserModel(httpServletRequest);
		}
		else
		{
			LOG.debug("Profile tracking consent withdrawn | account is deactivated");
			getConsentService().removeConsentReferenceInSession(httpServletResponse);
		}

		filterChain.doFilter(httpServletRequest, httpServletResponse);
	}

	private boolean isAnonymousUser()
	{
		return getUserService().isAnonymousUser(getUserService().getCurrentUser());
	}

	protected void populateDebugFlagInSession(final HttpServletRequest httpServletRequest)
	{
		if (isAnonymousUser())
		{
			return;
		}

		final String headerName = getConfigurationService().getConfiguration().getString(DEBUG_HEADER_NAME, StringUtils.EMPTY);

		if(StringUtils.isNotBlank(headerName)) {
			String debug = httpServletRequest.getHeader(headerName);
			getProfileConfigurationService().setProfileTagDebugFlagInSession(Boolean.valueOf(debug));
		}

	}

	protected boolean isProfileTrackingConsentGiven(final HttpServletRequest httpServletRequest)
	{
		final boolean consent = getConsentService().isProfileTrackingConsentGiven(httpServletRequest);
		getConsentService().setProfileConsent(consent);
		return consent;
	}

	protected boolean isActiveAccount()
	{
		final UserModel currentUser = getUserService().getCurrentUser();
		return currentUser.getDeactivationDate() == null;
	}


	public ConsentService getConsentService()
	{
		return consentService;
	}

	@Required
	public void setConsentService(final ConsentService consentService)
	{
		this.consentService = consentService;
	}

	public UserService getUserService()
	{
		return userService;
	}

	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	public ConfigurationService getConfigurationService() {
		return configurationService;
	}

	@Required
	public void setConfigurationService(ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

	public ProfileConfigurationService getProfileConfigurationService() {
		return profileConfigurationService;
	}

	@Required
	public void setProfileConfigurationService(ProfileConfigurationService profileConfigurationService) {
		this.profileConfigurationService = profileConfigurationService;
	}
}
