/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.common.predicate;

import de.hybris.platform.cms2.model.pages.ProductPageModel;

import java.util.function.Predicate;


/**
 * Predicate to test if a given page type code is a Product page code.
 */
public class ProductPageTypeCodePredicate implements Predicate<String>
{
	@Override
	public boolean test(String pageTypeCode)
	{
		return ProductPageModel._TYPECODE.equals(pageTypeCode);
	}
}
