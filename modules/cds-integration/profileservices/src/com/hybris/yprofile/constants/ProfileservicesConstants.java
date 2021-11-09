/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.yprofile.constants;

/**
 * Global class for all Profileservices constants. You can add global constants for your extension into this class.
 */
public final class ProfileservicesConstants extends GeneratedProfileservicesConstants
{
	public static final String EXTENSIONNAME = "profileservices";
    /**
     * Session attribute for logged in users
     */
    public static final String USER_CONSENTS = "user-consents";
    /**
     * Cookie for anonymous user
     */
    public static final String ANONYMOUS_CONSENTS_COOKIE_NAME = "anonymous-consents";

    /**
     * Header for anonymous user
     */
    public static final String ANONYMOUS_CONSENTS_HEADER = "X-Anonymous-Consents";
    /**
     * Consent code for Profile
     */
    public static final String PROFILE_CONSENT = "PROFILE";
    /**
     * Expected consent value
     */
    public static final String CONSENT_GIVEN = "GIVEN";
    /**
     * Expected consent value
     */
    public static final String CONSENT_WITHDRAWN = "WITHDRAWN";
    /**
     * Cookie and Session attribute key
     */
    public static final String PROFILE_CONSENT_GIVEN = "profile.consent.given";
    /**
     * Cookie and Session attribute key
     */
    public static final String PROFILE_TRACKING_PAUSE = "profile.tracking.pause";
    /**
     * Profile tag url identifier
     */
    public static final String PROFILE_TAG_URL = "ProfileTagUrl";
    /**
     * Profile tag configuration url identifier
     */
    public static final String PROFILE_TAG_CONFIG_URL = "ProfileTagConfigUrl";

    /**
     * Profile tag debug header name property
     */
    public static final String DEBUG_HEADER_NAME = "profileservices.occ.debug.header.name";

    /**
     * Consent reference header name property
     */
    public static final String CONSENT_REFERENCE_HEADER_NAME = "profileservices.occ.consent.header.name";


    private ProfileservicesConstants()
	{
		//empty to avoid instantiating this constant class
	}

	// implement here constants used by this extension

    public static final String PLATFORM_LOGO_CODE = "profileservicesPlatformLogo";
}
