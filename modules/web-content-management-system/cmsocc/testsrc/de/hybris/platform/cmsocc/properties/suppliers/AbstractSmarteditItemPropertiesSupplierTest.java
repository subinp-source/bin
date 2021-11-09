/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsocc.properties.suppliers;

import static de.hybris.platform.cmsocc.properties.suppliers.AbstractSmarteditItemPropertiesSupplier.GROUP_NAME;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.misc.CMSFilter;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.servicelayer.session.SessionService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class AbstractSmarteditItemPropertiesSupplierTest
{
	private AbstractSmarteditItemPropertiesSupplier supplier;

	@Mock
	private SessionService sessionService;
	@Mock
	private CMSItemModel itemModel;
	@Mock
	private Object previewTicket;

	@Before
	public void setUp()
	{
		supplier = Mockito.mock(AbstractSmarteditItemPropertiesSupplier.class, Mockito.CALLS_REAL_METHODS);
		supplier.setSessionService(sessionService);
	}

	@Test
	public void isEnabledShouldReturnTrueIfPreviewTicketExists()
	{
		// GIVEN
		when(sessionService.getAttribute(CMSFilter.PREVIEW_TICKET_ID_PARAM)).thenReturn(previewTicket);

		// WHEN
		final boolean enabled = supplier.isEnabled(itemModel);

		// THEN
		assertTrue(enabled);
	}

	@Test
	public void isEnabledShouldReturnFalseIfPreviewTicketIsNull()
	{
		// GIVEN
		when(sessionService.getAttribute(CMSFilter.PREVIEW_TICKET_ID_PARAM)).thenReturn(null);

		// WHEN
		final boolean enabled = supplier.isEnabled(itemModel);

		// THEN
		assertFalse(enabled);
	}

	@Test
	public void shouldReturnSmartEditGroupName()
	{
		// THEN
		assertThat(supplier.groupName(), is(GROUP_NAME));
	}
}
