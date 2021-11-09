/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.yprofile.listeners;

import com.hybris.yprofile.consent.services.ConsentService;
import com.hybris.yprofile.services.ProfileConfigurationService;
import com.hybris.yprofile.services.ProfileTransactionService;
import de.hybris.platform.commerceservices.event.AbstractSiteEventListener;
import de.hybris.platform.commerceservices.event.LoginSuccessEvent;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.session.impl.DefaultSessionTokenService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class LoginSuccessEventListener extends AbstractSiteEventListener<LoginSuccessEvent> {

    private static final Logger LOG = Logger.getLogger(LoginSuccessEventListener.class);

    private ProfileTransactionService profileTransactionService;
    private DefaultSessionTokenService defaultSessionTokenService;
    private ProfileConfigurationService profileConfigurationService;
    private ConsentService consentService;

    @Override
    protected void onSiteEvent(final LoginSuccessEvent event) {

        try {
            final String baseSiteId = event.getBaseStore().getUid();
            final String sessionId = getDefaultSessionTokenService().getOrCreateSessionToken();
            final Boolean profileTagDebugSession = getProfileConfigurationService().isProfileTagDebugEnabledInSession();

            String consentReference = getConsentService().getConsentReferenceFromSession();
            if (consentReference == null) {
                consentReference = event.getCustomer().getConsentReference();
            }

            if (consentReference != null) {
                setDebugFlag(event.getCustomer(), profileTagDebugSession);
                getProfileTransactionService().sendLoginEvent(event.getCustomer(), consentReference, sessionId, baseSiteId);
                LOG.debug("Login Success Event Sent Successfully!");
            } else {
                LOG.debug("Consent reference is null. Could not send event to CDS");
            }

        } catch (Exception e){
            LOG.error("Error sending login event: " + e.getMessage());
            LOG.debug("Error sending login event: ", e);
        }

    }

    private void setDebugFlag(final UserModel currentUser, final Boolean profileTagDebugSession) {
        if (Boolean.TRUE.equals(profileTagDebugSession)) {
            currentUser.setProfileTagDebug(profileTagDebugSession);
        }
    }

    @Override
    protected boolean shouldHandleEvent(final LoginSuccessEvent event) {
        return event.getCustomer() != null;
    }

    @Required
    public void setProfileTransactionService(ProfileTransactionService profileTransactionService) {
        this.profileTransactionService = profileTransactionService;
    }

    private ProfileTransactionService getProfileTransactionService() {
        return profileTransactionService;
    }

    public DefaultSessionTokenService getDefaultSessionTokenService() {
        return defaultSessionTokenService;
    }

    @Required
    public void setDefaultSessionTokenService(DefaultSessionTokenService defaultSessionTokenService) {
        this.defaultSessionTokenService = defaultSessionTokenService;
    }

    public ConsentService getConsentService() {
        return this.consentService;
    }

    @Required
    public void setConsentService(ConsentService consentService) {
        this.consentService = consentService;
    }


    public ProfileConfigurationService getProfileConfigurationService() {
        return profileConfigurationService;
    }

    @Required
    public void setProfileConfigurationService(ProfileConfigurationService profileConfigurationService) {
        this.profileConfigurationService = profileConfigurationService;
    }
}
