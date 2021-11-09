/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.catalogversioneventhandling.impl;

import static org.fest.assertions.Assertions.assertThat;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;


public class DefaultAvailableCatalogVersionsTagTest
{
	private DefaultAvailableCatalogVersionsTag testSubject;

	@Before
	public void setUp()
	{
		testSubject = new DefaultAvailableCatalogVersionsTag();
	}

	@Test
	public void shouldSetRandomTagAfterInitialization()
	{
		//when
		testSubject.refresh();

		//then
		assertThat(testSubject.getTag()).isNotNull();
	}

	@Test
	public void shouldChangeTagAfterCatalogVersionChanged()
	{
		//given
		testSubject.refresh();

		//when
		final UUID tagBeforeChangeCatalogVersions = testSubject.getTag();
		testSubject.refresh();
		final UUID tagAfterChangeCatalogVersions = testSubject.getTag();

		//then
		assertThat(tagAfterChangeCatalogVersions).isNotEqualTo(tagBeforeChangeCatalogVersions);
	}
}
