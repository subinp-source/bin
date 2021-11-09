/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.common.service;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;

import org.junit.Test;


@UnitTest
public class ClassFieldFinderTest
{
	@Test
	public void shouldFindTypecode()
	{
		final String result = ClassFieldFinder.getTypeCode(AbstractPageModel.class);
		assertThat(result, is(AbstractPageModel._TYPECODE));
	}

	@Test
	public void shouldNotFindTypecode()
	{
		final String result = ClassFieldFinder.getTypeCode(String.class);
		assertThat(result, nullValue());
	}

}
