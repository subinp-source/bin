/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.integrationservices.populator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.entry;
import static org.mockito.Matchers.anyMapOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.integrationservices.integrationkey.IntegrationKeyCalculationException;
import de.hybris.platform.integrationservices.integrationkey.IntegrationKeyValueGenerator;
import de.hybris.platform.integrationservices.model.TypeDescriptor;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Maps;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultIntegrationKeyPopulatorUnitTest
{
	private static final String MY_INTEGRATION_KEY = "MY-INTEGRATION-KEY";
	private static final TypeDescriptor ITEM_TYPE = mock(TypeDescriptor.class);

	@Mock
	private IntegrationKeyValueGenerator<TypeDescriptor, Map<String, Object>> keyGenerator;
	@InjectMocks
	private DefaultIntegrationKeyPopulator populator;

	@Test
	public void testPopulate()
	{
		final Map<String, Object> map = Maps.newHashMap();
		when(keyGenerator.generate(ITEM_TYPE, map)).thenReturn(MY_INTEGRATION_KEY);

		populator.populate(conversionContext(), map);
		assertThat(map).contains(entry("integrationKey", MY_INTEGRATION_KEY));
	}

	@Test
	public void testPopulateWithError()
	{
		doThrow(IntegrationKeyCalculationException.class).when(keyGenerator).generate(eq(ITEM_TYPE), anyMapOf(String.class, Object.class));

		assertThatThrownBy(() -> populator.populate(conversionContext(), Maps.newHashMap()))
				.isInstanceOf(IntegrationKeyCalculationException.class);
	}

	private static ItemToMapConversionContext conversionContext()
	{
		final ItemToMapConversionContext context = mock(ItemToMapConversionContext.class);
		doReturn(ITEM_TYPE).when(context).getTypeDescriptor();
		return context;
	}
}
