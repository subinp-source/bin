package de.hybris.platform.webservicescommons.api.restrictions.interceptor;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.webservicescommons.api.restrictions.data.EndpointContextData;
import de.hybris.platform.webservicescommons.api.restrictions.services.EndpointRestrictionsService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.method.HandlerMethod;

import static de.hybris.platform.webservicescommons.api.restrictions.EndpointRestrictionsMockData.VALID_METHOD_WITH_VALID_ANNOTATIONS;
import static de.hybris.platform.webservicescommons.api.restrictions.EndpointRestrictionsMockData.getMockMethod;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.test.util.AssertionErrors.assertTrue;


/**
 * Unit tests for {@link BaseEndpointRestrictionsInterceptor}
 */
@UnitTest
public class BaseEndpointRestrictionsInterceptorTest
{
	private static final Object NOT_SUPPORTED_OBJECT = "NOT SUPPORTED OBJECT";
	private static final String CONFIG_PREFIX = "configPrefix";
	private static final int INVALID_STATUS = 999;

	@Mock
	private HttpServletRequest httpServletRequest;
	@Mock
	private HttpServletResponse httpServletResponse;
	@Mock
	private HandlerMethod handlerMethod;
	@Mock
	private EndpointRestrictionsService endpointRestrictionsService;

	private BaseEndpointRestrictionsInterceptor baseEndpointRestrictionsInterceptor;

	@Before
	public void setUp() throws NoSuchMethodException
	{
		MockitoAnnotations.initMocks(this);
		baseEndpointRestrictionsInterceptor = new BaseEndpointRestrictionsInterceptor(CONFIG_PREFIX, endpointRestrictionsService);
		given(handlerMethod.getMethod()).willReturn(getMockMethod(VALID_METHOD_WITH_VALID_ANNOTATIONS));
	}

	@Test
	public void testValidateErrorStatusSucceed()
	{
		//when
		final int errorStatus = BaseEndpointRestrictionsInterceptor.validateErrorStatus(BAD_REQUEST.value());
		//then
		assertTrue("When error status validation is successful a 400 (Bad Request) status should be returned",
				errorStatus == BAD_REQUEST.value());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testParseErrorStatusValueReturnsDefaultIfStatusIsInvalid()
	{
		//when
		BaseEndpointRestrictionsInterceptor.validateErrorStatus(INVALID_STATUS);
	}

	@Test
	@SuppressWarnings("squid:S00112")
	public void testEndpointIsDisabled() throws Exception
	{
		//given
		given(endpointRestrictionsService.isEndpointDisabled(anyString(), any(EndpointContextData.class))).willReturn(true);
		given(httpServletResponse.getStatus()).willReturn(NOT_FOUND.value());
		//when
		final boolean canInvokeHandler = baseEndpointRestrictionsInterceptor
				.preHandle(httpServletRequest, httpServletResponse, handlerMethod);
		//then
		verify(endpointRestrictionsService).isEndpointDisabled(anyString(), any(EndpointContextData.class));
		verify(httpServletResponse).sendError(NOT_FOUND.value());
		assertTrue("The HTTP response status should be 404 (Not Found)", httpServletResponse.getStatus() == NOT_FOUND.value());
		assertFalse("The endpoint method handler can't be invoked when endpoint is disabled", canInvokeHandler);
	}

	@Test
	@SuppressWarnings("squid:S00112")
	public void testEndpointIsNotDisabledForNullEndpointHandler() throws Exception
	{
		//when
		final boolean canInvokeHandler = baseEndpointRestrictionsInterceptor
				.preHandle(httpServletRequest, httpServletResponse, null);
		//then
		verify(endpointRestrictionsService, never()).isEndpointDisabled(anyString(), any());
		assertTrue("The endpoint method handler can be invoked when endpoint is not disabled", canInvokeHandler);
	}

	@Test
	@SuppressWarnings("squid:S00112")
	public void testEndpointIsNotDisabledForNotSupportedEndpointHandler() throws Exception
	{
		//when
		final boolean canInvokeHandler = baseEndpointRestrictionsInterceptor
				.preHandle(httpServletRequest, httpServletResponse, NOT_SUPPORTED_OBJECT);
		//then
		verify(endpointRestrictionsService, never()).isEndpointDisabled(anyString(), any());
		assertTrue("The endpoint method handler can be invoked when endpoint handler is not supported by interceptor",
				canInvokeHandler);
	}

	@Test
	@SuppressWarnings("squid:S00112")
	public void testEndpointIsNotDisabled() throws Exception
	{
		//given
		given(endpointRestrictionsService.isEndpointDisabled(anyString(), any(EndpointContextData.class))).willReturn(false);
		//when
		final boolean canInvokeHandler = baseEndpointRestrictionsInterceptor
				.preHandle(httpServletRequest, httpServletResponse, handlerMethod);
		//then
		verify(endpointRestrictionsService).isEndpointDisabled(anyString(), any(EndpointContextData.class));
		assertTrue("The endpoint method handler can be invoked when endpoint handler is not disabled", canInvokeHandler);
	}

	@Test
	public void testCreateEndpointContextData()
	{
		//when
		final EndpointContextData endpointContextData = baseEndpointRestrictionsInterceptor
				.createEndpointContextData(handlerMethod);
		//then
		assertNotNull("The endpoint context data should not be null", endpointContextData);
		assertNotNull("The endpoint context data should contain a not null method value", endpointContextData.getMethod());
	}
}
