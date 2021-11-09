/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.synchronization.itemvisitors.impl;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.core.model.ItemModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;



@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class ContentSlotForPageModelVisitorTest
{

	@Mock
	private ContentSlotModel contentSlotModel;
	@Mock
	private ContentSlotForPageModel contentSlotForPageModel;
	@InjectMocks
	private ContentSlotForPageModelVisitor visitor;

	@Before
	public void setUp()
	{
		when(contentSlotForPageModel.getContentSlot()).thenReturn(contentSlotModel);
	}

	@Test
	public void shouldCollectContentSlotElement()
	{
		//execute
		List<ItemModel> visit = visitor.visit(contentSlotForPageModel, null, null);

		//assert
		assertThat("ContentSlotForPageModelVisitorTest should contain only one element", visit, iterableWithSize(1));
		assertThat("ContentSlotForPageModelVisitorTest should ContentSlotModel object", visit, contains(contentSlotModel));
		verify(contentSlotForPageModel, times(1)).getContentSlot();
	}
}
