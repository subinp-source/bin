/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundservices.decorator;

import static de.hybris.platform.outboundservices.decorator.DecoratorContext.decoratorContextBuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectDescriptor;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import com.google.common.collect.Maps;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultDecoratorExecutionUnitTest
{
	private static final String MY_HEADER = "MY_HEADER";
	private static final String MY_ATTRIBUTE = "MY_ATTRIBUTE";
	private static final String DECORATOR_HEADER = "DECORATOR-HEADER";
	private static final String DECORATOR_ATTRIBUTE = "DECORATOR-ATTR";
	private static final DecoratorContext CONTEXT = decoratorContextBuilder().withDestinationModel(mock(ConsumedDestinationModel.class))
	                                                                         .withItemModel(mock(ItemModel.class))
	                                                                         .withIntegrationObject(mock(IntegrationObjectDescriptor.class))
	                                                                         .build();

	private final HttpHeaders httpHeaders = new HttpHeaders();
	private final Map<String, Object> payload = Maps.newHashMap();

	@Test
	public void testExecutionWithEmptyIterator()
	{
		httpHeaders.add(MY_HEADER, "my-value");
		payload.put(MY_ATTRIBUTE, "my-attribute");
		final Iterable<OutboundRequestDecorator> emptyDecorators = Collections.emptyList();

		final DecoratorExecution execution = new DefaultDecoratorExecution(emptyDecorators);
		final HttpEntity<Map<String, Object>> httpEntity = execution.createHttpEntity(httpHeaders, payload, CONTEXT);

		assertThat(httpEntity).isNotNull();
		assertThat(httpEntity.getHeaders()).contains(entry(MY_HEADER, Collections.singletonList("my-value")));
		assertThat(httpEntity.getBody()).containsAllEntriesOf(payload);
	}

	@Test
	public void testExecutionWithMultipleDecorators()
	{
		httpHeaders.add(MY_HEADER, "my-value");
		payload.put(MY_ATTRIBUTE, "my-attribute");
		final Iterable<OutboundRequestDecorator> decorators = Arrays.asList(mockDecorator("d1"), mockDecorator("d2"));

		final DecoratorExecution execution = new DefaultDecoratorExecution(decorators);
		final HttpEntity<Map<String, Object>> httpEntity = execution.createHttpEntity(httpHeaders, payload, CONTEXT);

		assertThat(httpEntity).isNotNull();
		assertThat(httpEntity.getHeaders()).contains(
				entry(MY_HEADER, Collections.singletonList("my-value")),
				entry(DECORATOR_HEADER, Arrays.asList("d1", "d2")));
		assertThat(httpEntity.getBody()).contains(entry(MY_ATTRIBUTE, "my-attribute"), entry(DECORATOR_ATTRIBUTE, "d2"));
	}

	@Test
	public void testExecutionWithMultipleDecorators_havingAnStopDecorator()
	{
		httpHeaders.add(MY_HEADER, "my-value");
		payload.put(MY_ATTRIBUTE, "my-attribute");
		final Iterable<OutboundRequestDecorator> decorators = Arrays.asList(
				mockDecorator("d1"), mockStopDecorator("d2"), mockDecorator("d3"));

		final DecoratorExecution execution = new DefaultDecoratorExecution(decorators);
		final HttpEntity<Map<String, Object>> httpEntity = execution.createHttpEntity(httpHeaders, payload, CONTEXT);

		assertThat(httpEntity).isNotNull();
		assertThat(httpEntity.getHeaders()).contains(
				entry(MY_HEADER, Collections.singletonList("my-value")),
				entry(DECORATOR_HEADER, Arrays.asList("d1", "d2")));
		assertThat(httpEntity.getBody()).contains(entry(MY_ATTRIBUTE, "my-attribute"), entry("DECORATOR-ATTR", "d2"));
	}

	private OutboundRequestDecorator mockDecorator(final String name)
	{
		final OutboundRequestDecorator decorator = mock(OutboundRequestDecorator.class);

		when(decorator.decorate(eq(httpHeaders), eq(payload), eq(CONTEXT), any())).then(call -> {
			final HttpHeaders h = call.getArgumentAt(0, HttpHeaders.class);
			final Map<String, Object> p = call.getArgumentAt(1, Map.class);
			final DecoratorContext c = call.getArgumentAt(2, DecoratorContext.class);
			final DecoratorExecution e = call.getArgumentAt(3, DecoratorExecution.class);
			h.add(DECORATOR_HEADER, name);
			p.put(DECORATOR_ATTRIBUTE, name);
			return e.createHttpEntity(h, p, c);
		});

		return decorator;
	}

	private OutboundRequestDecorator mockStopDecorator(final String name)
	{
		final OutboundRequestDecorator decorator = mock(OutboundRequestDecorator.class);

		when(decorator.decorate(eq(httpHeaders), eq(payload), eq(CONTEXT), any())).then(call -> {
			final HttpHeaders h = call.getArgumentAt(0, HttpHeaders.class);
			final Map<String, Object> p = call.getArgumentAt(1, Map.class);
			h.add(DECORATOR_HEADER, name);
			p.put("DECORATOR-ATTR", name);
			return new HttpEntity<>(p, h);
		});

		return decorator;
	}
}
