/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */

package de.hybris.platform.odata2services.odata.persistence.creation;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.inboundservices.persistence.PersistenceContext;
import de.hybris.platform.integrationservices.item.IntegrationItem;
import de.hybris.platform.integrationservices.model.TypeDescriptor;
import de.hybris.platform.odata2services.odata.persistence.exception.MissingNavigationPropertyException;

import org.junit.Test;

@UnitTest
public class NeverCreateItemStrategyUnitTest
{
	private final NeverCreateItemStrategy strategy = new NeverCreateItemStrategy();

	@Test
	public void testThatExceptionIsThrown()
	{
		final TypeDescriptor itemType = mock(TypeDescriptor.class);
		doReturn("AType").when(itemType).getItemCode();
		final IntegrationItem item = mock(IntegrationItem.class);
		doReturn(itemType).when(item).getItemType();
		final PersistenceContext context = mock(PersistenceContext.class);
		doReturn(item).when(context).getIntegrationItem();

		assertThatThrownBy(()-> strategy.createItem(context))
				.isInstanceOf(MissingNavigationPropertyException.class)
				.hasFieldOrPropertyWithValue("errorCode", "missing_nav_property");
	}
}
