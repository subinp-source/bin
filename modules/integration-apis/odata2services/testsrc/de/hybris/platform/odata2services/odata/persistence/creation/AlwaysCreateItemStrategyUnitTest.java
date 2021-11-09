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

package de.hybris.platform.odata2services.odata.persistence.creation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.inboundservices.persistence.ItemModelFactory;
import de.hybris.platform.inboundservices.persistence.PersistenceContext;
import de.hybris.platform.integrationservices.item.IntegrationItem;
import de.hybris.platform.integrationservices.model.TypeDescriptor;
import de.hybris.platform.odata2services.odata.persistence.InternalProcessingException;
import de.hybris.platform.servicelayer.exceptions.ModelCreationException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class AlwaysCreateItemStrategyUnitTest
{
	@Mock
	private ItemModelFactory modelFactory;
	@Mock
	private PersistenceContext context;

	@InjectMocks
	private AlwaysCreateItemStrategy strategy;

	@Before
	public void setUp()
	{
		final TypeDescriptor itemType = mock(TypeDescriptor.class);
		doReturn("AType").when(itemType).getItemCode();
		final IntegrationItem item = mock(IntegrationItem.class);
		doReturn(itemType).when(item).getItemType();
		doReturn(item).when(context).getIntegrationItem();
	}


	@Test
	public void testItemCreation()
	{
		final ItemModel itemModel = mock(ItemModel.class);
		when(modelFactory.createItem(context)).thenReturn(itemModel);

		final ItemModel item = strategy.createItem(context);

		assertThat(item).isNotNull()
						.isSameAs(itemModel);
	}

	@Test
	public void testItemCreationFails()
	{
		when(modelFactory.createItem(context)).thenThrow(new ModelCreationException("message", new RuntimeException()));

		assertThatThrownBy(() -> strategy.createItem(context))
				.isInstanceOf(InternalProcessingException.class)
				.hasFieldOrPropertyWithValue("errorCode", "internal_error")
				.hasMessageContaining("There was an error encountered during the processing of the integration object.");
	}
}
