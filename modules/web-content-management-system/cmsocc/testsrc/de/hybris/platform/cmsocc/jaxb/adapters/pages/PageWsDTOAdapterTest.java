/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsocc.jaxb.adapters.pages;

import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cmsocc.data.CMSPageWsDTO;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class PageWsDTOAdapterTest
{
	@InjectMocks
	@Spy
	private PageWsDTOAdapter pageWsDTOAdapter;

	@Test
	public void shouldReturnNullIfCMSPageWsDTOIsNull()
	{
		// WHEN
		final PageAdapterUtil.PageAdaptedData marshal = pageWsDTOAdapter.marshal(null);

		// THEN
		assertThat(marshal, nullValue());
	}

	@Test
	public void shouldCallPageAdapterUtilToCovertPage()
	{
		// GIVEN
		final CMSPageWsDTO page = new CMSPageWsDTO();

		// WHEN
		pageWsDTOAdapter.marshal(page);

		// THEN
		verify(pageWsDTOAdapter).convert(page);
	}
}
