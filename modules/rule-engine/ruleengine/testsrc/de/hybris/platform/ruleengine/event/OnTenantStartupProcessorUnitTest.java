/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.ruleengine.event;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.configuration.Configuration;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import com.google.common.collect.ImmutableMap;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class OnTenantStartupProcessorUnitTest
{
	@InjectMocks
	private OnTenantStartupProcessor startupProcessor;

	@InjectMocks
	private FixedBackOffPolicy backOffPolicy;
	@InjectMocks
	private RetryTemplate retryTemplate;
	@Mock
	private Unreliable unreliableObject;
	@Mock
	private ConfigurationService configurationService;
	@Mock
	private Configuration configuration;

	@Before
	public void setUp()
	{
		final RetryPolicy retryPolicy = new SimpleRetryPolicy(3, ImmutableMap.of(IllegalStateException.class, true));

		startupProcessor.setRuleEngineInitRetryTemplate(retryTemplate);
		retryTemplate.setRetryPolicy(retryPolicy);
		retryTemplate.setBackOffPolicy(backOffPolicy);

		backOffPolicy.setBackOffPeriod(500l);
	}

	@Test
	public void testRefreshCurrentSessionWithRetryOk()
	{
		startupProcessor.refreshCurrentSessionWithRetry(() -> {
			unreliableObject.unreliableMethod();
			return null;
		});
		verify(unreliableObject, times(1)).unreliableMethod();
	}

	@Test
	public void testRefreshCurrentSessionWithRetryFails()
	{
		doThrow(IllegalStateException.class).when(unreliableObject).unreliableMethod();

		Assertions.assertThatThrownBy(() ->
				startupProcessor.refreshCurrentSessionWithRetry(() -> {
					unreliableObject.unreliableMethod();
					return null;
				})
		).isInstanceOf(IllegalStateException.class);
		verify(unreliableObject, times(3)).unreliableMethod();
	}

	@Test
	public void testRefreshCurrentSessionWithRetrySucceedsAfterRetry()
	{
		doThrow(IllegalStateException.class).doThrow(IllegalStateException.class).doNothing().when(unreliableObject)
				.unreliableMethod();

		startupProcessor.refreshCurrentSessionWithRetry(() -> {
			unreliableObject.unreliableMethod();
			return null;
		});
		verify(unreliableObject, times(3)).unreliableMethod();
	}

	@Test
	public void testRefreshCurrentSessionWithRetryFailsWithFatalException()
	{
		doThrow(IllegalStateException.class).doThrow(NullPointerException.class).doNothing().when(unreliableObject)
				.unreliableMethod();

		Assertions.assertThatThrownBy(() ->
				startupProcessor.refreshCurrentSessionWithRetry(() -> {
					unreliableObject.unreliableMethod();
					return null;
				})
		).isInstanceOf(NullPointerException.class);
		verify(unreliableObject, times(2)).unreliableMethod();
	}

	@Test
	public void testSetDroolsSystemProperties()
	{
		// define some test properties
		final String PROP1_SHOULD_BE_FOUND = "test.OnTenantStartupProcessorUnitTest.found.in.config";
		final String PROP2_SHOULD_NOT_BE_FOUND = "test.OnTenantStartupProcessorUnitTest.not.found.in.config";

		final String INITIAL = "initial";
		final String CHANGED = "changed";

		final Set<String> droolsProperties = Stream.of(PROP1_SHOULD_BE_FOUND, PROP2_SHOULD_NOT_BE_FOUND)
				.collect(Collectors.toSet());
		startupProcessor.setDroolsProperties(droolsProperties);
		when(configurationService.getConfiguration()).thenReturn(configuration);
		when(configuration.getString(PROP1_SHOULD_BE_FOUND)).thenReturn("changed");

		// set initial system property values
		System.setProperty(PROP1_SHOULD_BE_FOUND, INITIAL);
		System.setProperty(PROP2_SHOULD_NOT_BE_FOUND, INITIAL);

		// test call
		startupProcessor.setDroolsSystemProperties();

		// ensure only first property has changed
		assertEquals("property should have changed", CHANGED, System.getProperty(PROP1_SHOULD_BE_FOUND));
		assertEquals("property should not have changed", INITIAL, System.getProperty(PROP2_SHOULD_NOT_BE_FOUND));

		// cleanup
		System.clearProperty(PROP1_SHOULD_BE_FOUND);
		System.clearProperty(PROP2_SHOULD_NOT_BE_FOUND);
	}

}
