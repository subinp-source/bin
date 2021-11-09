/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsocc.properties.suppliers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.misc.CMSFilter;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.contents.containers.AbstractCMSComponentContainerModel;
import de.hybris.platform.servicelayer.session.SessionService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class SmartEditComponentPropertiesSupplierTest
{
	@InjectMocks
	private SmartEditComponentPropertiesSupplier supplier;
	@Mock
	private CMSItemModel itemModel;
	@Mock
	private SessionService sessionService;
	@Mock
	private AbstractCMSComponentContainerModel containerModel;
	@Mock
	private Object previewTicket;

	@Before()
	public void setUp()
	{
		when(sessionService.getAttribute(CMSFilter.PREVIEW_TICKET_ID_PARAM)).thenReturn(previewTicket);
	}

	@Test
	public void isEnabledShouldReturnTrueIfNotContainerAndPreviewTicketExists()
	{
		// WHEN
		final boolean enabled = supplier.isEnabled(itemModel);

		// THEN
		assertTrue(enabled);
	}

	@Test
	public void isEnabledShouldReturnFalseIfItemIsAContainer()
	{
		// WHEN
		final boolean enabled = supplier.isEnabled(containerModel);

		// THEN
		assertFalse(enabled);
	}
}
