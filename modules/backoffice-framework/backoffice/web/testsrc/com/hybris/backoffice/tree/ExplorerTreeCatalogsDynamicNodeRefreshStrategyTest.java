/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.tree;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacade;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectNotFoundException;
import com.hybris.cockpitng.tree.node.DynamicNode;


@RunWith(MockitoJUnitRunner.class)
public class ExplorerTreeCatalogsDynamicNodeRefreshStrategyTest
{

	@InjectMocks
	private ExplorerTreeCatalogsDynamicNodeRefreshStrategy testSubject;

	@Mock
	private ObjectFacade objectFacade;

	@Test
	public void shouldReloadDynamicNodeData() throws ObjectNotFoundException
	{
		// given
		final Object beforeRefresh = mock(Object.class);
		final Object afterRefresh = mock(Object.class);
		final DynamicNode node = mock(DynamicNode.class);
		given(node.getData()).willReturn(beforeRefresh);
		given(objectFacade.reload(beforeRefresh)).willReturn(afterRefresh);

		// when
		testSubject.refreshNode(node);

		// then
		then(node).should().setData(afterRefresh);
	}

	@Test
	public void shouldNotReloadDeletedDynamicNodeData() throws ObjectNotFoundException
	{
		// given
		final Object beforeRefresh = mock(Object.class);
		final DynamicNode node = mock(DynamicNode.class);
		given(node.getData()).willReturn(beforeRefresh);
		given(objectFacade.reload(beforeRefresh)).willThrow(ObjectNotFoundException.class);

		// when
		testSubject.refreshNode(node);

		// then
		then(node).should().setData(beforeRefresh);
	}

}
