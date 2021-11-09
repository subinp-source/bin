/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.common.predicate;

import de.hybris.platform.cms2.model.pages.CategoryPageModel;

import java.util.function.Predicate;

/**
 * Predicate to test if a given page type code is a Category page code.
 */
public class CategoryPageTypeCodePredicate implements Predicate<String>
{
	@Override
	public boolean test(String pageTypeCode)
	{
		return CategoryPageModel._TYPECODE.equals(pageTypeCode);
	}
}
