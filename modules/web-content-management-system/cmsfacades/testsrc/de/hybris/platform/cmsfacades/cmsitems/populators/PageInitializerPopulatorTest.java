/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.cmsitems.populators;

import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.FIELD_CLONE_COMPONENTS;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.FIELD_PAGE_UUID;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.relatedpages.service.RelatedPageRejectionService;
import de.hybris.platform.cmsfacades.pages.service.PageInitializer;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class PageInitializerPopulatorTest
{
	@InjectMocks
	private PageInitializerPopulator pageInitializerPopulator;

	@Mock
	private AbstractPageModel itemModel;

	@Mock
	private PageInitializer pageInitializer;

	@Mock
	private ModelService modelService;

	@Mock
	private RelatedPageRejectionService relatedPageRejectionService;

	private Map<String, Object> source;

	@Before
	public void setup()
	{
		source = new HashMap<>();
	}

	@Test
	public void testRejectAndInitializePageForNewObjects()
	{
		// GIVEN
		when(relatedPageRejectionService.hasUserChangedApprovalStatus(itemModel)).thenReturn(false);
		when(modelService.isNew(itemModel)).thenReturn(true);

		// WHEN
		pageInitializerPopulator.populate(source, itemModel);

		// THEN
		verify(modelService).save(itemModel);
		verify(pageInitializer).initialize(itemModel);
		verify(relatedPageRejectionService).rejectPage(itemModel);
	}

	@Test
	public void testNotInitializeIfObjectIsNotNew()
	{
		// GIVEN
		when(modelService.isNew(itemModel)).thenReturn(false);

		// WHEN
		pageInitializerPopulator.populate(source, itemModel);

		// THEN
		verify(modelService, never()).save(itemModel);
		verifyZeroInteractions(pageInitializer);
	}

	@Test
	public void testNotRejectPageWhenApprovalStatusDirectlyChanged()
	{
		when(relatedPageRejectionService.hasUserChangedApprovalStatus(itemModel)).thenReturn(true);

		pageInitializerPopulator.populate(source, itemModel);

		verify(relatedPageRejectionService, never()).rejectPage(itemModel);
	}

	@Test
	public void testNotRejectPageWhenPageIsBeingClone()
	{
		when(relatedPageRejectionService.hasUserChangedApprovalStatus(itemModel)).thenReturn(false);
		source.put(FIELD_PAGE_UUID, "mock-page-uuid");
		source.put(FIELD_CLONE_COMPONENTS, Boolean.FALSE);

		pageInitializerPopulator.populate(source, itemModel);

		verify(relatedPageRejectionService, never()).rejectPage(itemModel);
	}

}
