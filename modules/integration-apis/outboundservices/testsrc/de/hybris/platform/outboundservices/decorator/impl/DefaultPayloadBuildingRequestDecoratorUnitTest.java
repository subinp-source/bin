/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundservices.decorator.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.data.MapEntry.entry;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectDescriptor;
import de.hybris.platform.integrationservices.model.TypeDescriptor;
import de.hybris.platform.integrationservices.populator.ItemToMapConversionContext;
import de.hybris.platform.integrationservices.service.IntegrationObjectAndItemMismatchException;
import de.hybris.platform.integrationservices.service.IntegrationObjectConversionService;
import de.hybris.platform.outboundservices.decorator.DecoratorContext;
import de.hybris.platform.outboundservices.decorator.DecoratorExecution;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import com.google.common.collect.Maps;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultPayloadBuildingRequestDecoratorUnitTest
{
	private static final HttpEntity<Map<String, Object>> ENTITY = new HttpEntity<>(new HashMap<>());
	private static final ItemModel CONTEXT_ITEM = new ItemModel();
	private static final IntegrationObjectDescriptor IO = mock(IntegrationObjectDescriptor.class);
	private static final TypeDescriptor CONTEXT_ITEM_TYPE = createContextTypeDescriptor();
	private static final HttpHeaders HEADERS = new HttpHeaders();

	@Mock
	private IntegrationObjectConversionService conversionService;
	@InjectMocks
	private DefaultPayloadBuildingRequestDecorator decorator;

	@Test
	public void testDecorate()
	{
		final Map<String, Object> payload = new HashMap<>();
		payload.put("EXISTING_ATTR", "EXISTING VALUE");
		final Map<String, Object> convertedEntity = Map.of("HEADER", "VALUE");
		when(conversionService.convert(any())).thenReturn(convertedEntity);
		final DecoratorContext context = decoratorContext();
		when(context.getIntegrationObjectItem()).thenReturn(Optional.of(CONTEXT_ITEM_TYPE));

		final HttpEntity<Map<String, Object>> result = decorator.decorate(HEADERS, payload, context, execution(payload, context));

		assertThat(payload).contains(entry("EXISTING_ATTR", "EXISTING VALUE"))
		                   .containsAllEntriesOf(convertedEntity);
		assertThat(result).isEqualTo(ENTITY);
		final ArgumentCaptor<ItemToMapConversionContext> conversion = ArgumentCaptor.forClass(ItemToMapConversionContext.class);
		verify(conversionService).convert(conversion.capture());
		assertThat(conversion.getValue())
				.hasFieldOrPropertyWithValue("itemModel", CONTEXT_ITEM)
				.hasFieldOrPropertyWithValue("typeDescriptor", CONTEXT_ITEM_TYPE);
	}

	@Test
	public void testDecorateWhenContextItemNotFound()
	{
		final Map<String, Object> payload = Map.of("EXISTING_ATTR", "EXISTING VALUE");
		final DecoratorContext context = decoratorContext();
		when(context.getIntegrationObjectItem()).thenReturn(Optional.empty());
		final DecoratorExecution execution = execution(payload, context);

		assertThatThrownBy(() -> decorator.decorate(HEADERS, payload, context, execution))
				.isInstanceOf(IntegrationObjectAndItemMismatchException.class)
				.hasFieldOrPropertyWithValue("dataItem", CONTEXT_ITEM)
				.hasFieldOrPropertyWithValue("integrationObject", IO);
	}

	@Test
	public void testDecoratorDoesNotHandleExceptions()
	{
		final HashMap<String, Object> payload = Maps.newHashMap();
		final DecoratorContext context = decoratorContext();
		final DecoratorExecution execution = execution(payload, context);
		doThrow(NullPointerException.class).when(conversionService).convert(any());

		assertThatThrownBy(() -> decorator.decorate(HEADERS, payload, context, execution))
				.isInstanceOf(NullPointerException.class);
	}

	private static TypeDescriptor createContextTypeDescriptor()
	{
		final TypeDescriptor itemType = mock(TypeDescriptor.class);
		when(itemType.isInstance(CONTEXT_ITEM)).thenReturn(true);
		return itemType;
	}

	private DecoratorExecution execution(final Map<String, Object> payload,
	                                     final DecoratorContext context)
	{
		final DecoratorExecution execution = mock(DecoratorExecution.class);
		when(execution.createHttpEntity(HEADERS, payload, context)).thenReturn(ENTITY);
		return execution;
	}

	private DecoratorContext decoratorContext()
	{
		final DecoratorContext ctx = mock(DecoratorContext.class);
		when(ctx.getItemModel()).thenReturn(CONTEXT_ITEM);
		when(ctx.getIntegrationObject()).thenReturn(IO);
		return ctx;
	}
}
