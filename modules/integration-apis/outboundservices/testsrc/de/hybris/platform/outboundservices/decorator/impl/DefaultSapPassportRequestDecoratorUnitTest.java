/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundservices.decorator.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.data.MapEntry.entry;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.integrationservices.service.SapPassportService;
import de.hybris.platform.outboundservices.decorator.DecoratorContext;
import de.hybris.platform.outboundservices.decorator.DecoratorExecution;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpHeaders;

import com.google.common.collect.Maps;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultSapPassportRequestDecoratorUnitTest
{
	private static final String INTEGRATION_OBJECT = "MyIntegrationObject";

	@Mock
	private SapPassportService sapPassportService;
	@Mock
	private DecoratorExecution execution;
	@InjectMocks
	private DefaultSapPassportRequestDecorator decorator;

	@Test
	public void testDecorator()
	{
		final HttpHeaders httpHeaders = new HttpHeaders();
		final Map<String, Object> payload = Maps.newHashMap();
		final DecoratorContext context = decoratorContext();

		when(sapPassportService.generate(any())).thenReturn("MY-PASSPORT");

		decorator.decorate(httpHeaders, payload, context, execution);

		verify(sapPassportService).generate(INTEGRATION_OBJECT);
		verify(execution).createHttpEntity(httpHeaders, payload, context);

		assertThat(httpHeaders).contains(entry("SAP-PASSPORT", Collections.singletonList("MY-PASSPORT")));
	}

	@Test
	public void testDecoratorDoesNotHandleExceptions()
	{
		final HashMap<String, Object> payload = Maps.newHashMap();
		final DecoratorContext context = decoratorContext();
		final HttpHeaders httpHeaders = new HttpHeaders();
		doThrow(NullPointerException.class).when(sapPassportService).generate(any());

		assertThatThrownBy(() -> decorator.decorate(httpHeaders, payload, context, execution))
				.isInstanceOf(NullPointerException.class);
	}

	private DecoratorContext decoratorContext()
	{
		final DecoratorContext context = mock(DecoratorContext.class);
		doReturn(INTEGRATION_OBJECT).when(context).getIntegrationObjectCode();
		return context;
	}
}
