/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.dao.impl;

import de.hybris.platform.returns.dao.ReturnRequestDao;
import de.hybris.platform.servicelayer.ServicelayerTest;

import javax.annotation.Resource;

import org.junit.Test;


public class DefaultReturnRequestDaoTest extends ServicelayerTest
{

	@Resource
	private ReturnRequestDao returnRequestDao;

	@Test
	public void shouldThrowNoSuchElementExceptionWhenGetingReturnRequest()
	{
		returnRequestDao.getReturnRequests("IDONTEXIST");

	}
}
