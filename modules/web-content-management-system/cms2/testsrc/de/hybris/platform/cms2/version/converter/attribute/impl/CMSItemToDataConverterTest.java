/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cms2.version.converter.attribute.impl;

import static de.hybris.platform.core.PK.fromLong;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.data.CMSItemData;
import de.hybris.platform.cms2.model.CMSVersionModel;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.version.service.CMSVersionService;
import de.hybris.platform.cms2.version.service.CMSVersionSessionContextProvider;
import de.hybris.platform.core.PK;

import java.util.HashSet;
import java.util.function.Predicate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class CMSItemToDataConverterTest
{

	private static final String PK_VALUE = "123";
	private static final String TRANSACTION_ID = "someTransactionId";

	@InjectMocks
	private CMSItemToDataConverter converter;

	@Mock
	private CMSVersionService cmsVersionService;
	@Mock
	private CMSVersionSessionContextProvider cmsVersionSessionContextProvider;

	@Mock
	private Predicate<CMSItemModel> abstractPageTypePredicate;

	@Mock
	private CMSItemModel cmsItem;

	@Mock
	private CMSVersionModel versionModel;

	private final PK pk = fromLong(Long.valueOf(PK_VALUE));
	private final PK itemPk = fromLong(Long.valueOf(PK_VALUE));

	@Before
	public void setup()
	{
		when(versionModel.getPk()).thenReturn(pk);
		when(cmsItem.getPk()).thenReturn(itemPk);
		when(cmsVersionService.getTransactionId()).thenReturn(TRANSACTION_ID);
		when(cmsVersionSessionContextProvider.getAllUnsavedVersionedItemsFromCached()).thenReturn(new HashSet<CMSItemData>());
		when(abstractPageTypePredicate.test(cmsItem)).thenReturn(false);
	}

	@Test
	public void whenConvertNullValueReturnsNull()
	{
		assertThat(converter.convert(null), nullValue());
	}

	@Test
	public void shouldConvertValidCMSItemAndReturnPKVersionByCreatingOneAVersionForIt()
	{
		// GIVEN
		when(cmsVersionService.createRevisionForItem(cmsItem)).thenReturn(versionModel);
		when(cmsVersionService.isVersionable(cmsItem)).thenReturn(true);

		// WHEN
		final Object value = converter.convert(cmsItem);

		// THEN
		verify(cmsVersionService).createRevisionForItem(cmsItem);
		assertThat(value, is(pk));
	}

	@Test
	public void shouldReturnPKOfSourceItemIfItemIsNotVersionable()
	{

		// GIVEN
		when(cmsVersionService.createRevisionForItem(cmsItem)).thenReturn(versionModel);
		when(cmsVersionService.isVersionable(cmsItem)).thenReturn(false);

		// WHEN
		final Object value = converter.convert(cmsItem);

		// THEN
		assertThat(value, is(itemPk));
	}

	@Test
	public void shouldReturnPKIfSourceItemIsAbstractPage()
	{
		// GIVEN
		when(cmsVersionService.isVersionable(cmsItem)).thenReturn(true);
		when(abstractPageTypePredicate.test(cmsItem)).thenReturn(true);

		// WHEN
		final Object value = converter.convert(cmsItem);

		// THEN
		assertThat(value, is(itemPk));
	}

}
