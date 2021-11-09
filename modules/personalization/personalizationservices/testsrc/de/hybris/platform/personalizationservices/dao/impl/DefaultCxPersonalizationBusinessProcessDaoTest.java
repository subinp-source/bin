/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.personalizationservices.dao.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.personalizationservices.model.process.CxPersonalizationProcessModel;
import de.hybris.platform.personalizationservices.process.dao.impl.DefaultCxPersonalizationBusinessProcessDao;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.impex.impl.ClasspathImpExResource;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class DefaultCxPersonalizationBusinessProcessDaoTest extends ServicelayerTest
{
	@Resource
	private DefaultCxPersonalizationBusinessProcessDao defaultCxPersonalizationBusinessProcessDao;

	@Resource
	private UserService userService;

	@Resource
	private CatalogVersionService catalogVersionService;

	@Before
	public void setupData() throws Exception
	{
		createCoreData();
		importData(new ClasspathImpExResource("/personalizationservices/test/testdata_cxsite.impex", "UTF-8"));
		importData(new ClasspathImpExResource("/personalizationservices/test/personalizationprocessdao_test.impex", "UTF-8"));
	}

	@Test
	public void shouldRetrieveRunningBusinessProcess()
	{
		final List<CxPersonalizationProcessModel> result = defaultCxPersonalizationBusinessProcessDao
				.findActiveBusinessProcesses("testdefinitionname", "testkey1");

		Assert.assertEquals(1, result.size());
		Assert.assertEquals("defaultcxcustomer", result.get(0).getUser().getUid());
		Assert.assertEquals(2, result.get(0).getCatalogVersions().size());
	}
}
