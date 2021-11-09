/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.yprofile.services.impl;

import com.hybris.yprofile.exceptions.ProfileException;
import com.hybris.yprofile.rest.clients.ProfileClient;
import com.hybris.yprofile.services.RetrieveRestClientStrategy;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel;
import de.hybris.platform.apiregistryservices.strategies.ConsumedDestinationLocatorStrategy;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@UnitTest
public class DefaultProfileConfigurationServiceTest {

    private DefaultProfileConfigurationService defaultProfileConfigurationService;

    @Mock
    private RetrieveRestClientStrategy retrieveRestClientStrategy;

    @Mock
    private ConsumedDestinationLocatorStrategy baseSiteConsumedDestinationLocatorStrategy;


    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        defaultProfileConfigurationService = new DefaultProfileConfigurationService();
        defaultProfileConfigurationService.setRetrieveRestClientStrategy(retrieveRestClientStrategy);
        defaultProfileConfigurationService.setBaseSiteConsumedDestinationLocatorStrategy(baseSiteConsumedDestinationLocatorStrategy);
        ConsumedDestinationModel destination = mock(ConsumedDestinationModel.class);
        when(baseSiteConsumedDestinationLocatorStrategy.lookup(ProfileClient.class.getSimpleName())).thenReturn(destination);
    }


    @Test
    public void assertConfigurationIsPresentForCurrentSiteAndProfileService(){

        ProfileClient profileClient = mock(ProfileClient.class);
        when(retrieveRestClientStrategy.getProfileRestClient()).thenReturn(profileClient);

        boolean result = defaultProfileConfigurationService.isConfigurationPresent();

        assertTrue(result);
    }

    @Test
    public void assertConfigurationIsNotPresentForMissingSiteAndServiceMappging(){

        when(retrieveRestClientStrategy.getProfileRestClient()).thenThrow(new ProfileException("Error"));

        boolean result = defaultProfileConfigurationService.isConfigurationPresent();

        assertFalse(result);
    }
}