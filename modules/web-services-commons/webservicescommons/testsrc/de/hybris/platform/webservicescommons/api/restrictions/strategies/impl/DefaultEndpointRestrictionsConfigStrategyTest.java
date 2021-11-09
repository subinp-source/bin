package de.hybris.platform.webservicescommons.api.restrictions.strategies.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import org.apache.commons.configuration.Configuration;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static de.hybris.platform.webservicescommons.api.restrictions.EndpointRestrictionsMockData.TEST_ALLOWED_NICKNAME;
import static de.hybris.platform.webservicescommons.api.restrictions.EndpointRestrictionsMockData.TEST_ANOTHER_NICKNAME;
import static de.hybris.platform.webservicescommons.api.restrictions.EndpointRestrictionsMockData.TEST_CONFIG_PREFIX;
import static de.hybris.platform.webservicescommons.api.restrictions.EndpointRestrictionsMockData.TEST_NICKNAME;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;


/**
 * Unit tests for {@link DefaultEndpointRestrictionsConfigStrategy}
 */
@UnitTest
public class DefaultEndpointRestrictionsConfigStrategyTest
{

	private static final String DISABLED_ENDPOINTS_VALUE = TEST_NICKNAME + "," + TEST_ANOTHER_NICKNAME;
	private static final String[] DISABLED_ENDPOINTS = { TEST_NICKNAME, TEST_ANOTHER_NICKNAME };

	@Mock
	private ConfigurationService configurationService;
	@Mock
	private Configuration configuration;
	@InjectMocks
	private DefaultEndpointRestrictionsConfigStrategy endpointRestrictionsConfigStrategy;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		given(configurationService.getConfiguration()).willReturn(configuration);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testThrowExceptionWhenNullEndpointId()
	{
		//when
		endpointRestrictionsConfigStrategy.isEndpointDisabled(TEST_CONFIG_PREFIX, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testThrowExceptionWhenNullConfigPrefix()
	{
		//when
		endpointRestrictionsConfigStrategy.isEndpointDisabled(null, TEST_NICKNAME);
	}

	@Test
	public void testEndpointIsDisabled()
	{
		//given
		given(configuration.getString(anyString())).willReturn(DISABLED_ENDPOINTS_VALUE);
		//when
		final boolean disabled = endpointRestrictionsConfigStrategy.isEndpointDisabled(TEST_CONFIG_PREFIX, TEST_NICKNAME);
		//then
		verify(configurationService).getConfiguration();
		verify(configuration).getString(anyString());
		assertTrue("Endpoint should be disabled when endpoint ID exists in the disabled endpoint array", disabled);
	}

	@Test
	public void testEndpointIsAllowed()
	{
		//given
		given(configuration.getString(anyString())).willReturn(DISABLED_ENDPOINTS_VALUE);
		//when
		final boolean disabled = endpointRestrictionsConfigStrategy.isEndpointDisabled(TEST_CONFIG_PREFIX, TEST_ALLOWED_NICKNAME);
		//then
		verify(configurationService).getConfiguration();
		verify(configuration).getString(anyString());
		assertFalse("Endpoint should be allowed when endpoint ID does not exist in the disabled endpoint array", disabled);
	}

	@Test
	public void testEndpointIsAllowedWhenNoDisabledEndpointsConfiguration()
	{
		//given
		given(configuration.getString(anyString())).willReturn(null);
		//when
		final boolean disabled = endpointRestrictionsConfigStrategy.isEndpointDisabled(TEST_CONFIG_PREFIX, TEST_NICKNAME);
		//then
		verify(configurationService).getConfiguration();
		verify(configuration).getString(anyString());
		assertFalse("Endpoint should be allowed when there is no disabled endpoints configuration", disabled);
	}

	@Test
	public void testDisabledEndpointListIsEmpty()
	{
		//given
		// Api restrictions feature is disabled
		given(configuration.getString(anyString())).willReturn("");
		//when
		final boolean empty = endpointRestrictionsConfigStrategy.isDisabledEndpointListEmpty(TEST_CONFIG_PREFIX);
		//then
		verify(configurationService).getConfiguration();
		verify(configuration).getString(anyString());
		assertTrue("Disabled endpoint list should be empty", empty);
	}

	@Test
	public void testDisabledEndpointListIsNotEmpty()
	{
		//given
		given(configuration.getString(anyString())).willReturn(DISABLED_ENDPOINTS_VALUE);
		//when
		final boolean empty = endpointRestrictionsConfigStrategy.isDisabledEndpointListEmpty(TEST_CONFIG_PREFIX);
		//then
		verify(configurationService).getConfiguration();
		verify(configuration).getString(anyString());
		assertFalse("Disabled endpoint list should not be empty", empty);
	}

	@Test
	public void testGetDisabledEndpoints()
	{
		//given
		given(configuration.getString(anyString())).willReturn(DISABLED_ENDPOINTS_VALUE);
		//when
		final String[] disabledEndpoints = endpointRestrictionsConfigStrategy.getDisabledEndpoints(TEST_CONFIG_PREFIX);
		//then
		assertArrayEquals("Disabled endpoints array should be returned", DISABLED_ENDPOINTS, disabledEndpoints);
	}
}
