/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.webservicescommons.api.restrictions;

import java.lang.reflect.Method;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import io.swagger.annotations.ApiOperation;


/**
 * Mocked data for disabled endpoint strategy tests
 */
public class EndpointRestrictionsMockData
{

	public static final String VALID_METHOD_WITH_VALID_ANNOTATIONS = "validMockMethod1";
	public static final String VALID_METHOD_WITH_GET_REQUEST_MAPPING = "validMockMethod2";
	public static final String VALID_METHOD_WITH_GET_MAPPING = "validMockMethod3";
	public static final String VALID_METHOD_WITH_POST_REQUEST_MAPPING = "validMockMethod4";
	public static final String INVALID_METHOD_WITH_ONLY_API_OPERATION = "invalidMockMethod5";
	public static final String INVALID_METHOD_WITHOUT_ANY_VALID_ANNOTATION = "invalidMockMethod6";

	public static final String TEST_CONFIG_PREFIX = "configPrefix";
	public static final String TEST_NICKNAME = "testNickname";
	public static final String TEST_ANOTHER_NICKNAME = "testAnotherNickname";
	public static final String TEST_ALLOWED_NICKNAME = "allowedOperation";
	public static final String REQUEST_MAPPING_GET_OPERATION = VALID_METHOD_WITH_GET_REQUEST_MAPPING + "UsingGET";
	public static final String GET_MAPPING_OPERATION = VALID_METHOD_WITH_GET_MAPPING + "UsingGET";
	public static final String REQUEST_MAPPING_POST_OPERATION = VALID_METHOD_WITH_POST_REQUEST_MAPPING + "UsingPOST";
	public static final String TEST_VALUE = "Test value";

	/**
	 * Gets mock method of mock data controller
	 *
	 * @param methodName
	 * 		mock method
	 * @return mock method
	 * @throws NoSuchMethodException
	 */
	public static Method getMockMethod(final String methodName) throws NoSuchMethodException
	{
		return MockApiController.class.getMethod(methodName);
	}

	/**
	 * Mock controller, that contains mock method using in tests
	 */
	public class MockApiController
	{
		// valid method with ApiOperation annotation, that contains nickname attribute, and containing RequestMapping annotation
		@ApiOperation(nickname = TEST_NICKNAME, value = TEST_VALUE)
		@RequestMapping(method = RequestMethod.GET)
		public void validMockMethod1()
		{
			// this is mocked method, used in tests
		}

		// valid method with ApiOperation annotation, without nickname, but containing RequestMapping annotation
		@ApiOperation(value = TEST_VALUE)
		@RequestMapping(method = RequestMethod.GET)
		public void validMockMethod2()
		{
			// this is mocked method, used in tests
		}

		// valid method with GetMapping annotation
		@GetMapping
		public void validMockMethod3()
		{
			// this is mocked method, used in tests
		}

		// valid method with RequestMapping annotation containing POST method
		@RequestMapping(method = RequestMethod.POST)
		public void validMockMethod4()
		{
			// this is mocked method, used in tests
		}

		// not supported method with ApiOperation annotation, but without RequestMapping annotation
		@ApiOperation(nickname = TEST_NICKNAME, value = TEST_VALUE)
		public void invalidMockMethod5()
		{
			// this is mocked method, used in tests
		}

		// not supported method without any valid annotation
		public void invalidMockMethod6()
		{
			// this is mocked method, used in tests
		}
	}
}
