/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.cmsitems.predicates;

import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.CategoryPageModel;
import de.hybris.platform.cms2.model.pages.ProductPageModel;

import java.util.function.Predicate;

import static java.util.Arrays.asList;


/**
 * Predicate to test if a given page is of a type that allows having only one ACTIVE primary page of that type at a time.
 *
 * <p>
 * Returns <tt>TRUE</tt> if the page can only have one primary; <tt>FALSE</tt> otherwise.
 * </p>
 */
public class PageCanOnlyHaveOnePrimaryPredicate implements Predicate<AbstractPageModel>
{
	@Override
	public boolean test(final AbstractPageModel pageModel)
	{
		return asList(ProductPageModel._TYPECODE, CategoryPageModel._TYPECODE).contains(pageModel.getItemtype());

	}


}
