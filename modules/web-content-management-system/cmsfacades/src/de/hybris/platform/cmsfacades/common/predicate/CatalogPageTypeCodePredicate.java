/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.common.predicate;

import de.hybris.platform.cms2.model.pages.CatalogPageModel;

import java.util.function.Predicate;


/**
 * Predicate to test if a given page type code is a Catalog page code.
 */
public class CatalogPageTypeCodePredicate implements Predicate<String>
{
	@Override
	public boolean test(String pageTypeCode)
	{
		return CatalogPageModel._TYPECODE.equals(pageTypeCode);
	}
}
