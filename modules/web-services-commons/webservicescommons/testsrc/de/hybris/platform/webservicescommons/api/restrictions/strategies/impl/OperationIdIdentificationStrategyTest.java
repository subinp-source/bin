package de.hybris.platform.webservicescommons.api.restrictions.strategies.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.webservicescommons.api.restrictions.data.EndpointContextData;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static de.hybris.platform.webservicescommons.api.restrictions.EndpointRestrictionsMockData.GET_MAPPING_OPERATION;
import static de.hybris.platform.webservicescommons.api.restrictions.EndpointRestrictionsMockData.INVALID_METHOD_WITHOUT_ANY_VALID_ANNOTATION;
import static de.hybris.platform.webservicescommons.api.restrictions.EndpointRestrictionsMockData.INVALID_METHOD_WITH_ONLY_API_OPERATION;
import static de.hybris.platform.webservicescommons.api.restrictions.EndpointRestrictionsMockData.REQUEST_MAPPING_GET_OPERATION;
import static de.hybris.platform.webservicescommons.api.restrictions.EndpointRestrictionsMockData.REQUEST_MAPPING_POST_OPERATION;
import static de.hybris.platform.webservicescommons.api.restrictions.EndpointRestrictionsMockData.TEST_NICKNAME;
import static de.hybris.platform.webservicescommons.api.restrictions.EndpointRestrictionsMockData.VALID_METHOD_WITH_GET_MAPPING;
import static de.hybris.platform.webservicescommons.api.restrictions.EndpointRestrictionsMockData.VALID_METHOD_WITH_GET_REQUEST_MAPPING;
import static de.hybris.platform.webservicescommons.api.restrictions.EndpointRestrictionsMockData.VALID_METHOD_WITH_POST_REQUEST_MAPPING;
import static de.hybris.platform.webservicescommons.api.restrictions.EndpointRestrictionsMockData.VALID_METHOD_WITH_VALID_ANNOTATIONS;
import static de.hybris.platform.webservicescommons.api.restrictions.EndpointRestrictionsMockData.getMockMethod;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;


/**
 * Unit tests for {@link OperationIdIdentificationStrategy );
 */
@UnitTest
public class OperationIdIdentificationStrategyTest
{

	@Mock
	private EndpointContextData endpointContextData;
	@InjectMocks
	private OperationIdIdentificationStrategy operationIdIdentificationStrategy;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testThrowExceptionWhenNullEndpointDataContext()
	{
		//when
		operationIdIdentificationStrategy.findEndpointId(null);
	}

	@Test
	public void testFindEndpointIdByNicknameOfApiOperation() throws NoSuchMethodException
	{
		//given
		final Method method = getMockMethod(VALID_METHOD_WITH_VALID_ANNOTATIONS);
		given(endpointContextData.getMethod()).willReturn(method);
		//when
		final Optional<String> endpointId = operationIdIdentificationStrategy.findEndpointId(endpointContextData);
		//then
		assertTrue("Endpoint ID should be found for method with valid ApiOperation and RequestMapping annotation",
				endpointId.isPresent());
		final String nickname = endpointId.orElse(null);
		assertEquals("Endpoint ID equals nickname attribute of ApiOperation annotation", TEST_NICKNAME, nickname);
	}

	@Test
	public void testFindEndpointIdForMethodWithRequestMapping() throws NoSuchMethodException
	{
		//given
		final Method method = getMockMethod(VALID_METHOD_WITH_GET_REQUEST_MAPPING);
		given(endpointContextData.getMethod()).willReturn(method);
		//when
		final Optional<String> endpointId = operationIdIdentificationStrategy.findEndpointId(endpointContextData);
		//then
		assertTrue("Endpoint ID should be found for method with RequestMapping annotation", endpointId.isPresent());
		final String nickname = endpointId.orElse(null);
		assertEquals("Endpoint ID is build based on method name and GET request method", REQUEST_MAPPING_GET_OPERATION, nickname);
	}

	@Test
	public void testFindEndpointIdForMethodWithGetMapping() throws NoSuchMethodException
	{
		//given
		final Method method = getMockMethod(VALID_METHOD_WITH_GET_MAPPING);
		given(endpointContextData.getMethod()).willReturn(method);
		//when
		final Optional<String> endpointId = operationIdIdentificationStrategy.findEndpointId(endpointContextData);
		//then
		assertTrue("Endpoint ID should be found for method with GetMapping annotation", endpointId.isPresent());
		final String nickname = endpointId.orElse(null);
		assertEquals("Endpoint ID is build based on method name and GET request method", GET_MAPPING_OPERATION, nickname);
	}

	@Test
	public void testFindEndpointIdForMethodWithPOSTRequestMapping() throws NoSuchMethodException
	{
		//given
		final Method method = getMockMethod(VALID_METHOD_WITH_POST_REQUEST_MAPPING);
		given(endpointContextData.getMethod()).willReturn(method);
		//when
		final Optional<String> endpointId = operationIdIdentificationStrategy.findEndpointId(endpointContextData);
		//then
		assertTrue("Endpoint ID should be found for method with RequestMapping annotation", endpointId.isPresent());
		final String nickname = endpointId.orElse(null);
		assertEquals("Endpoint ID is build based on method name and POST request method", REQUEST_MAPPING_POST_OPERATION, nickname);
	}

	@Test
	public void testFindEndpointIdForMethodWithInvalidApiOperation() throws NoSuchMethodException
	{
		//given
		final Method method = getMockMethod(INVALID_METHOD_WITH_ONLY_API_OPERATION);
		given(endpointContextData.getMethod()).willReturn(method);
		//when
		final Optional<String> endpointId = operationIdIdentificationStrategy.findEndpointId(endpointContextData);
		//then
		assertTrue("Endpoint ID should not be found for method with only ApiOperation annotation", endpointId.isEmpty());
	}

	@Test
	public void testEndpointIdIsNotFoundForMethodWithoutAnyValidAnnotation() throws NoSuchMethodException
	{
		//given
		final Method method = getMockMethod(INVALID_METHOD_WITHOUT_ANY_VALID_ANNOTATION);
		given(endpointContextData.getMethod()).willReturn(method);
		//when
		final Optional<String> endpointId = operationIdIdentificationStrategy.findEndpointId(endpointContextData);
		//then
		assertTrue("Endpoint ID should not be found for method without any valid annotation", endpointId.isEmpty());
	}

	@Test
	public void testEndpointIdCacheExistsAndIsEmpty()
	{
		//when
		final Map<Method, String> endpointIdCache = operationIdIdentificationStrategy.getEndpointIdCache();
		//then
		assertNotNull("Endpoint ID cache exists", endpointIdCache);
		assertTrue("Endpoint ID cache is empty", endpointIdCache.isEmpty());
	}

	@Test
	public void testEndpointIdIsCached() throws NoSuchMethodException
	{
		//given
		final Method method = getMockMethod(VALID_METHOD_WITH_GET_REQUEST_MAPPING);
		given(endpointContextData.getMethod()).willReturn(method);
		//when
		final Optional<String> firstResult = operationIdIdentificationStrategy.findEndpointId(endpointContextData);
		//then
		assertTrue("Endpoint ID should be found at first time", firstResult.isPresent());
		assertTrue("Cache should contain one entry", operationIdIdentificationStrategy.getEndpointIdCache().size() == 1);
		//when
		final Optional<String> secondResult = operationIdIdentificationStrategy.findEndpointId(endpointContextData);
		//then
		assertTrue("Endpoint ID should be found at second time", secondResult.isPresent());
		assertTrue("Cache should not change", operationIdIdentificationStrategy.getEndpointIdCache().size() == 1);
		final String firstEndpointId = firstResult.orElse(null);
		final String secondEndpointId = secondResult.orElse(null);
		assertSame("Endpoint ID objects should be the same", firstEndpointId, secondEndpointId);
		final String cachedEndpointId = operationIdIdentificationStrategy.getEndpointIdCache().get(method);
		assertSame("Endpoint ID returned should be the same as endpoint ID cached", firstEndpointId, cachedEndpointId);
	}
}
