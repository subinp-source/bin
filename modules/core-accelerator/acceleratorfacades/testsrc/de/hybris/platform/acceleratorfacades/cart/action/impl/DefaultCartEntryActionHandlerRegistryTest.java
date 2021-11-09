/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorfacades.cart.action.impl;

import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.acceleratorfacades.cart.action.CartEntryAction;
import de.hybris.platform.acceleratorfacades.cart.action.CartEntryActionHandler;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;



@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultCartEntryActionHandlerRegistryTest
{
	@InjectMocks
	private DefaultCartEntryActionHandlerRegistry defaultCartEntryActionHandlerRegistry = new DefaultCartEntryActionHandlerRegistry();

	@Mock
	private Map<CartEntryAction, CartEntryActionHandler> cartEntryActionHandlerMap;

	@Mock
	private RemoveCartEntryActionHandler removeCartEntryActionHandler;

	@Test
	public void shouldRetrieveValidActionHandler()
	{
		given(cartEntryActionHandlerMap.get(CartEntryAction.REMOVE)).willReturn(removeCartEntryActionHandler);
		final CartEntryActionHandler cartEntryActionHandler = defaultCartEntryActionHandlerRegistry
				.getHandler(CartEntryAction.REMOVE);
		Assert.assertNotNull("Handler should not be null", cartEntryActionHandler);
		Assert.assertTrue("CartEntryActionHandler is not of the expected type",
				cartEntryActionHandler instanceof RemoveCartEntryActionHandler);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldValidateNullCartEntryAction()
	{
		defaultCartEntryActionHandlerRegistry.getHandler(null);
	}
}
