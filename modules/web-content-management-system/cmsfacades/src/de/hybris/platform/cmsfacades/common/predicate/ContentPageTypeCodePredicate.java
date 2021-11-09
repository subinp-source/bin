/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.common.predicate;

import de.hybris.platform.cms2.model.pages.ContentPageModel;

import java.util.function.Predicate;


/**
 * Predicate to test if a given page type code is a Content page code.
 */
public class ContentPageTypeCodePredicate implements Predicate<String>
{
	@Override
	public boolean test(String pageTypeModel)
	{
		return ContentPageModel._TYPECODE.equals(pageTypeModel);
	}
}
