/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.process.approval.services.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2b.strategies.impl.DefaultB2BApprovalProcessLookUpStrategy;
import de.hybris.platform.store.BaseStoreModel;

import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;



@UnitTest
public class DefaultB2BApprovalProcessServiceTest
{
	private DefaultB2BApprovalProcessService b2bApprovalProcessService;

	@Mock
	private DefaultB2BApprovalProcessLookUpStrategy b2bApprovalProcessLookUpStrategy;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		b2bApprovalProcessService = new DefaultB2BApprovalProcessService();
		b2bApprovalProcessService.setB2bApprovalProcessLookUpStrategy(b2bApprovalProcessLookUpStrategy);
	}

	@Test
	public void shouldGetProcess()
	{
		final BaseStoreModel store = mock(BaseStoreModel.class);
		final Map<String, String> mockedProcesses = mock(Map.class);
		when(b2bApprovalProcessLookUpStrategy.getProcesses(store)).thenReturn(mockedProcesses);

		Assert.assertEquals(mockedProcesses, b2bApprovalProcessService.getProcesses(store));
	}
}
