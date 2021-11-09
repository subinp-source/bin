/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.notificationservices.retention.hook;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.notificationservices.model.SiteMessageForCustomerModel;
import de.hybris.platform.notificationservices.service.SiteMessageService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


/**
 * Unit test for {@link SiteMessageCleanupHook}
 */
@UnitTest
public class SiteMessageCleanupHookTest
{
	private SiteMessageCleanupHook siteMessageCleanupHook;
	@Mock
	private ModelService modelService;
	@Mock
	private SiteMessageService siteMessageService;
	@Mock
	private CustomerModel customer;
	@Mock
	private SiteMessageForCustomerModel siteMessageForCustomer;

	@Before
	public void prepare()
	{
		MockitoAnnotations.initMocks(this);
		siteMessageCleanupHook = new SiteMessageCleanupHook();
		siteMessageCleanupHook.setSiteMessageService(siteMessageService);
		siteMessageCleanupHook.setModelService(modelService);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCleanupRelatedObjects_customerNull()
	{
		siteMessageCleanupHook.cleanupRelatedObjects(null);
	}

	@Test()
	public void testCleanupRelatedObjects_customerWithSiteMessage()
	{
		final List<SiteMessageForCustomerModel> models = new ArrayList<SiteMessageForCustomerModel>();
		models.add(siteMessageForCustomer);
		Mockito.when(siteMessageService.getSiteMessagesForCustomer(customer)).thenReturn(models);
		Mockito.doNothing().when(modelService).removeAll(models);
		siteMessageCleanupHook.cleanupRelatedObjects(customer);
		Mockito.verify(modelService, Mockito.times(1)).removeAll(models);
	}

	@Test()
	public void testCleanupRelatedObjects_customerWithoutSiteMessage()
	{
		final List<SiteMessageForCustomerModel> models = Collections.emptyList();
		Mockito.when(siteMessageService.getSiteMessagesForCustomer(customer)).thenReturn(models);
		Mockito.doNothing().when(modelService).removeAll(models);
		siteMessageCleanupHook.cleanupRelatedObjects(customer);
		Mockito.verify(modelService, Mockito.times(0)).removeAll(models);
	}
}
