package de.hybris.platform.webservicescommons.api.restrictions.services.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.webservicescommons.api.restrictions.strategies.EndpointIdentificationStrategy;
import de.hybris.platform.webservicescommons.api.restrictions.strategies.EndpointRestrictionsConfigStrategy;
import de.hybris.platform.webservicescommons.api.restrictions.data.EndpointContextData;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static de.hybris.platform.webservicescommons.api.restrictions.EndpointRestrictionsMockData.TEST_ALLOWED_NICKNAME;
import static de.hybris.platform.webservicescommons.api.restrictions.EndpointRestrictionsMockData.TEST_CONFIG_PREFIX;
import static de.hybris.platform.webservicescommons.api.restrictions.EndpointRestrictionsMockData.TEST_NICKNAME;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


/**
 * Unit tests for {@link ListableEndpointRestrictionsService}
 */
@UnitTest
public class ListableEndpointRestrictionsServiceTest
{
	@Mock
	private EndpointRestrictionsConfigStrategy endpointRestrictionsConfigStrategy;
	@Mock
	private List<EndpointIdentificationStrategy> endpointIdentificationStrategies;
	@Mock
	private EndpointIdentificationStrategy firstEndpointIdentificationStrategy;
	@Mock
	private EndpointIdentificationStrategy secondEndpointIdentificationStrategy;
	@Mock
	private EndpointContextData endpointContextData;
	@InjectMocks
	private ListableEndpointRestrictionsService endpointListableRestrictionsService;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testEndpointIsAllowedWhenEmptyIdentificationStrategyList()
	{
		//given
		given(endpointRestrictionsConfigStrategy.isDisabledEndpointListEmpty(anyString())).willReturn(true);
		given(endpointIdentificationStrategies.stream()).willReturn(Stream.empty());
		//when
		final boolean disabled = endpointListableRestrictionsService.isEndpointDisabled(TEST_CONFIG_PREFIX, endpointContextData);
		//then
		assertFalse("Endpoint should be allowed when empty identification strategy list", disabled);
	}

	@Test
	public void testEndpointIsAllowedWhenDisabledEndpointListIsEmpty()
	{
		//given
		given(endpointRestrictionsConfigStrategy.isDisabledEndpointListEmpty(anyString())).willReturn(true);
		//when
		final boolean disabled = endpointListableRestrictionsService.isEndpointDisabled(TEST_CONFIG_PREFIX, endpointContextData);
		//then
		assertFalse("Endpoint should be allowed when the list of disabled endpoints is empty", disabled);
	}

	@Test
	public void testEndpointIsAllowedWhenNoEndpointId()
	{
		//given
		given(endpointRestrictionsConfigStrategy.isDisabledEndpointListEmpty(anyString())).willReturn(false);
		given(endpointIdentificationStrategies.stream()).willReturn(Stream.of(firstEndpointIdentificationStrategy));
		given(firstEndpointIdentificationStrategy.findEndpointId(anyObject())).willReturn(Optional.empty());

		//when
		final boolean disabled = endpointListableRestrictionsService.isEndpointDisabled(TEST_CONFIG_PREFIX, endpointContextData);
		//then
		assertFalse("Endpoint should be allowed when endpoint ID can not be found", disabled);
	}

	@Test
	public void testEndpointIsAllowedWhenEndpointIdIsNotDisabled()
	{
		//given
		given(endpointRestrictionsConfigStrategy.isDisabledEndpointListEmpty(anyString())).willReturn(false);
		given(endpointIdentificationStrategies.stream()).willReturn(Stream.of(firstEndpointIdentificationStrategy));
		given(firstEndpointIdentificationStrategy.findEndpointId(anyObject())).willReturn(Optional.of(TEST_NICKNAME));
		given(endpointRestrictionsConfigStrategy.isEndpointDisabled(anyString(), anyString())).willReturn(false);

		//when
		final boolean disabled = endpointListableRestrictionsService.isEndpointDisabled(TEST_CONFIG_PREFIX, endpointContextData);
		//then
		assertFalse("Endpoint should be allowed when endpoint ID is not disabled by strategy", disabled);
	}

	@Test
	public void testEndpointIsDisabledWhenEndpointIdIsDisabled()
	{
		//given
		given(endpointRestrictionsConfigStrategy.isDisabledEndpointListEmpty(TEST_CONFIG_PREFIX)).willReturn(false);
		given(endpointIdentificationStrategies.stream())
				.willReturn(Stream.of(firstEndpointIdentificationStrategy, secondEndpointIdentificationStrategy));
		given(firstEndpointIdentificationStrategy.findEndpointId(endpointContextData))
				.willReturn(Optional.of(TEST_ALLOWED_NICKNAME));
		given(secondEndpointIdentificationStrategy.findEndpointId(endpointContextData)).willReturn(Optional.of(TEST_NICKNAME));

		given(endpointRestrictionsConfigStrategy.isEndpointDisabled(TEST_CONFIG_PREFIX, TEST_ALLOWED_NICKNAME)).willReturn(false);
		given(endpointRestrictionsConfigStrategy.isEndpointDisabled(TEST_CONFIG_PREFIX, TEST_NICKNAME)).willReturn(true);

		//when
		final boolean disabled = endpointListableRestrictionsService.isEndpointDisabled(TEST_CONFIG_PREFIX, endpointContextData);
		//then
		verify(endpointRestrictionsConfigStrategy).isDisabledEndpointListEmpty(TEST_CONFIG_PREFIX);
		verify(firstEndpointIdentificationStrategy).findEndpointId(endpointContextData);
		verify(secondEndpointIdentificationStrategy).findEndpointId(endpointContextData);
		verify(endpointRestrictionsConfigStrategy, times(2)).isEndpointDisabled(anyString(), anyString());
		assertTrue("Endpoint should be disabled for endpoint ID from secondary identification strategy", disabled);
	}
}
