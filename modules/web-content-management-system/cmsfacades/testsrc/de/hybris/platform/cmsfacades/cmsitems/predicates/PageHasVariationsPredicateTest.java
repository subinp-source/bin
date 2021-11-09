/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.cmsitems.predicates;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cmsfacades.pages.service.PageVariationResolver;
import de.hybris.platform.cmsfacades.pages.service.PageVariationResolverType;
import de.hybris.platform.cmsfacades.pages.service.PageVariationResolverTypeRegistry;

import java.util.Collections;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class PageHasVariationsPredicateTest
{

	@InjectMocks
	private PageHasVariationsPredicate pageHasVariationsPredicate;

	@Mock
	private PageVariationResolverTypeRegistry pageVariationResolverTypeRegistry;

	@Mock
	private PageVariationResolverType pageVariationResolverType;

	@Mock
	private PageVariationResolver<AbstractPageModel> pageVariationResolver;

	@Mock
	private AbstractPageModel defaultPageModel;

	@Mock
	private AbstractPageModel variationPageModel;

	@Before
	public void setup()
	{
		when(defaultPageModel.getItemtype()).thenReturn(AbstractPageModel._TYPECODE);
		when(pageVariationResolverTypeRegistry.getPageVariationResolverType(AbstractPageModel._TYPECODE)).thenReturn(Optional.of(pageVariationResolverType));
		when(pageVariationResolverType.getResolver()).thenReturn(pageVariationResolver);
	}

	@Test
	public void testPageHasVariations()
	{
		when(pageVariationResolver.findVariationPages(defaultPageModel)).thenReturn(Collections.singletonList(variationPageModel));

		assertThat(pageHasVariationsPredicate.test(defaultPageModel), is(true));
	}

	@Test
	public void testPageHasNoVariations()
	{
		when(pageVariationResolver.findVariationPages(defaultPageModel)).thenReturn(Collections.emptyList());

		assertThat(pageHasVariationsPredicate.test(defaultPageModel), is(false));
	}
}
