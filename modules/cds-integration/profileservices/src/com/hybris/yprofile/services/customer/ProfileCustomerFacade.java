/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.yprofile.services.customer;

import com.hybris.yprofile.consent.services.ConsentService;
import com.hybris.yprofile.services.ProfileConfigurationService;
import com.hybris.yprofile.services.ProfileTransactionService;
import org.apache.commons.lang3.StringUtils;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.customer.impl.DefaultCustomerFacade;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.session.impl.DefaultSessionTokenService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * yProfile implementation for the {@link CustomerFacade} to send login events.
 * @deprecated since 2005 use LoginSuccessEventListener instead
 */
@Deprecated(since = "2005", forRemoval=true)
public class ProfileCustomerFacade extends DefaultCustomerFacade {

    private static final Logger LOG = Logger.getLogger(ProfileCustomerFacade.class);

    private ProfileTransactionService profileTransactionService;
    private ConsentService consentService;
    private DefaultSessionTokenService defaultSessionTokenService;
    private ProfileConfigurationService profileConfigurationService;

    @Override
    public void loginSuccess(){

        super.loginSuccess();

        final UserModel currentUser = getUserService().getCurrentUser();

        try {
            if (!getProfileConfigurationService().isProfileTrackingPaused()) {
                // the current http request to be passed on to the consent service for extracting the consent reference cookie.
                final HttpServletRequest httpServletRequest = this.getHttpServletRequest();
                // updates the session before fetching the consent reference.
                getConsentService().saveConsentReferenceInSessionAndCurrentUserModel(httpServletRequest);
                String consentReferenceId = getConsentService().getConsentReferenceFromSession();
                // If the request does not have any cookies owing to the user not accepting tracking, get the profile id from the model
                if(StringUtils.isBlank(consentReferenceId)){
                    consentReferenceId = currentUser.getConsentReference();
                }
                final String sessionId = getDefaultSessionTokenService().getOrCreateSessionToken();
                final Boolean profileTagDebugSession = getProfileConfigurationService().isProfileTagDebugEnabledInSession();
                if (consentReferenceId != null) {
                    setDebugFlag(currentUser, profileTagDebugSession);
                    getProfileTransactionService().sendLoginEvent(currentUser, consentReferenceId, sessionId, getSiteId());
                } else {
                    LOG.debug("Consent reference is null. Could not send event to CDS");
                }
            }
        } catch (Exception e){
            LOG.error("Error sending login event to profile", e);
        }
    }

    private void setDebugFlag(final UserModel currentUser, final Boolean profileTagDebugSession) {
        if (Boolean.TRUE.equals(profileTagDebugSession)) {
            currentUser.setProfileTagDebug(profileTagDebugSession);
        }
    }

    protected String getSiteId() {
        return getBaseSiteService().getCurrentBaseSite().getUid();
    }

    public ProfileTransactionService getProfileTransactionService() {
        return profileTransactionService;
    }

    @Required
    public void setProfileTransactionService(ProfileTransactionService profileTransactionService) {
        this.profileTransactionService = profileTransactionService;
    }

    public ConsentService getConsentService() {
        return consentService;
    }

    @Required
    public void setConsentService(ConsentService consentService) {
        this.consentService = consentService;
    }

    public DefaultSessionTokenService getDefaultSessionTokenService() {
        return defaultSessionTokenService;
    }

    @Required
    public void setDefaultSessionTokenService(DefaultSessionTokenService defaultSessionTokenService) {
        this.defaultSessionTokenService = defaultSessionTokenService;
    }

    public ProfileConfigurationService getProfileConfigurationService() {
        return profileConfigurationService;
    }

    @Required
    public void setProfileConfigurationService(ProfileConfigurationService profileConfigurationService) {
        this.profileConfigurationService = profileConfigurationService;
    }

    protected HttpServletRequest getHttpServletRequest(){
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    }
}