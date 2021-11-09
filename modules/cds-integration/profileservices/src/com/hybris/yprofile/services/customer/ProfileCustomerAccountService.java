/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.yprofile.services.customer;

import com.hybris.yprofile.consent.services.ConsentService;
import com.hybris.yprofile.services.ProfileConfigurationService;
import com.hybris.yprofile.services.ProfileTransactionService;
import de.hybris.platform.commerceservices.customer.CustomerAccountService;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commerceservices.customer.impl.DefaultCustomerAccountService;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.session.impl.DefaultSessionTokenService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

/**
 * yProfile implementation for the {@link CustomerAccountService} to send registration events.
 * @deprecated since 2005 use RegisterEventListener instead
 */
@Deprecated(since = "2005", forRemoval=true)
public class ProfileCustomerAccountService extends DefaultCustomerAccountService {

    private static final Logger LOG = Logger.getLogger(ProfileCustomerAccountService.class);

    private ProfileTransactionService profileTransactionService;
    private ConsentService consentService;
    private DefaultSessionTokenService defaultSessionTokenService;
    private ProfileConfigurationService profileConfigurationService;

    @Override
    public void register(final CustomerModel customerModel, final String password) throws DuplicateUidException
    {
        super.register(customerModel, password);

        try {

            if (!getProfileConfigurationService().isProfileTrackingPaused()) {
                final String consentReferenceId = getConsentService().getConsentReferenceFromSession();
                final String sessionId = getDefaultSessionTokenService().getOrCreateSessionToken();
                final Boolean profileTagDebugSession = profileConfigurationService.isProfileTagDebugEnabledInSession();

                if (consentReferenceId != null) {
                    setDebugFLag(customerModel, profileTagDebugSession);
                    getProfileTransactionService().sendUserRegistrationEvent(customerModel, consentReferenceId, sessionId, getSiteId());
                } else {
                    LOG.debug("Consent reference is null. Could not send event to CDS");
                }

            }
        } catch (Exception e){
            LOG.error("Error sending login event to profile", e);
        }
    }

    private void setDebugFLag(final CustomerModel customerModel, final Boolean profileTagDebugSession) {
        if (Boolean.TRUE.equals(profileTagDebugSession)) {
            customerModel.setProfileTagDebug(profileTagDebugSession);
        }
    }

    protected String getSiteId() {
        return getBaseSiteService().getCurrentBaseSite().getUid();
    }

    public DefaultSessionTokenService getDefaultSessionTokenService() {
        return defaultSessionTokenService;
    }

    @Required
    public void setDefaultSessionTokenService(DefaultSessionTokenService defaultSessionTokenService) {
        this.defaultSessionTokenService = defaultSessionTokenService;
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

    public ProfileConfigurationService getProfileConfigurationService() {
        return profileConfigurationService;
    }

    @Required
    public void setProfileConfigurationService(ProfileConfigurationService profileConfigurationService) {
        this.profileConfigurationService = profileConfigurationService;
    }
}
