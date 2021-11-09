/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.personalizationpromotions.dynamic;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.personalizationpromotions.model.CxPromotionActionModel;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.enums.ActionType;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Date;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class DynamicAttributestTest extends ServicelayerTest
{

	@Resource
	private ModelService modelService;
	@Resource
	private CatalogVersionService catalogVersionService;

	@Before
	public void setUp() throws Exception
	{
		createCoreData();
		createDefaultCatalog();
	}

	@Test
	public void shouldCalculateAffectedObjectKey()
	{
		final CxPromotionActionModel action = modelService.create(CxPromotionActionModel.class);
		action.setPromotionId("randomPromoId");
		action.setCode("code" + new Date().getTime());
		action.setTarget("target");
		action.setType(ActionType.PLAIN);
		action.setCatalogVersion(catalogVersionService.getCatalogVersion("testCatalog", "Online"));

		Assert.assertEquals("randomPromoId", action.getAffectedObjectKey());
	}

}
