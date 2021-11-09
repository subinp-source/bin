/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.yprofile.services;

import com.hybris.yprofile.rest.clients.ConsentServiceClient;
import com.hybris.yprofile.rest.clients.ProfileClient;

public interface RetrieveRestClientStrategy {

    ProfileClient getProfileRestClient();

    ConsentServiceClient getConsentServiceRestClient();
}
