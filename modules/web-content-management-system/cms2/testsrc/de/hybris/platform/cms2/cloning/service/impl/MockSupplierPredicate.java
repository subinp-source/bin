/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cms2.cloning.service.impl;

import de.hybris.platform.core.model.ItemModel;

import java.util.function.BiPredicate;
import java.util.function.Supplier;


/**
 * Mock class to mimic the presetValue supplier and predicate methods
 */
public class MockSupplierPredicate implements BiPredicate<ItemModel, String>, Supplier<String>
{

	@Override
	public String get()
	{
		return null;
	}

	@Override
	public boolean test(final ItemModel t, final String u)
	{
		return false;
	}

}
