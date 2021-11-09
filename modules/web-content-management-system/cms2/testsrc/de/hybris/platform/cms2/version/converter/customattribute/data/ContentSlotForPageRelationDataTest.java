/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cms2.version.converter.customattribute.data;

import static de.hybris.platform.core.PK.fromLong;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.PK;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class ContentSlotForPageRelationDataTest
{

	private final String position = "1";
	private final PK pk = fromLong(Long.valueOf(123));

	@InjectMocks
	private ContentSlotForPageRelationData contentSlotForPageRelationData;

	@Test
	public void test()
	{

		// WHEN
		contentSlotForPageRelationData = new ContentSlotForPageRelationData();
		contentSlotForPageRelationData.setPosition(position);
		contentSlotForPageRelationData.setPk(pk);

		// THEN
		assertThat(contentSlotForPageRelationData.toData(), is(position + "__::__" + pk.toString()));
	}

}
