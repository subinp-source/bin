/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.synchronization.itemvisitors.impl;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.navigation.CMSNavigationEntryModel;
import de.hybris.platform.core.model.ItemModel;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class CMSNavigationEntryModelVisitorTest
{

	@Mock
	private CMSNavigationEntryModel entry;
	@Mock
	private ItemModel item;
	@InjectMocks
	private CMSNavigationEntryModelVisitor visitor;
	
	
	@Before
	public void setUp()
	{
		when(entry.getItem()).thenReturn(item);

	}

	@Test
	public void willCollectCmsComponents()
	{
		
		List<ItemModel> visit = visitor.visit(entry, null, null);
		
		assertThat(visit, containsInAnyOrder(item));
		
		
	}

}
