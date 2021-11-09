/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.configurablebundleservices.bundle.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.configurablebundleservices.bundle.BundleTemplateService;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.order.EntryGroup;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;


/**
 * Test to check when an entry can be modified
 */
@UnitTest
public class BundleOrderEntryModifiableCheckerTest
{
    @Mock
    private BundleTemplateService bundleTemplateService;
    @InjectMocks
	private final BundleOrderEntryModifiableChecker checker = new BundleOrderEntryModifiableChecker();

    private EntryGroup entryGroup;

    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);

        entryGroup = new EntryGroup();
        entryGroup.setGroupNumber(Integer.valueOf(5));
        entryGroup.setExternalReferenceId("bundleID");
    }

	@Test
	public void testShouldMakeBunldeEntriesNotUpdatable()
	{
		when(bundleTemplateService.getBundleEntryGroup(any(AbstractOrderEntryModel.class))).thenReturn(entryGroup);

		final AbstractOrderEntryModel entryToUpdate = new CartEntryModel();
		entryToUpdate.setEntryGroupNumbers(Collections.singleton(5));
		Assert.assertFalse(checker.canModify(entryToUpdate));

	}

	@Test
	public void standAloneEntriesCanBeUpdated()
	{
		when(bundleTemplateService.getBundleEntryGroup(any(AbstractOrderEntryModel.class))).thenReturn(null);

		final AbstractOrderEntryModel entryToUpdate = new CartEntryModel();
		entryToUpdate.setEntryGroupNumbers(Collections.singleton(1));
		Assert.assertTrue(checker.canModify(entryToUpdate));

	}

}