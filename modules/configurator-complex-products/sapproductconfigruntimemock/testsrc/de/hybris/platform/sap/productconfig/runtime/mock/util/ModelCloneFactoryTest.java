/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.runtime.mock.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.sap.productconfig.runtime.interf.KBKey;
import de.hybris.platform.sap.productconfig.runtime.interf.impl.KBKeyImpl;
import de.hybris.platform.sap.productconfig.runtime.interf.model.ConfigModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticGroupModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticValueModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.PriceModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.CsticGroupModelImpl;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.CsticValueModelImpl;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.PriceModelImpl;
import de.hybris.platform.sap.productconfig.runtime.mock.impl.CsticModelBuilder;
import de.hybris.platform.sap.productconfig.runtime.mock.impl.RunTimeConfigMockFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;


@UnitTest
public class ModelCloneFactoryTest
{

	private static final String OPT1 = "OPT1";
	public static final String CSTIC_1 = "CSTIC_1";
	public static final String CSTIC_2 = "CSTIC_2";

	public static final String NAME = "1";
	public static final String DESCRIPTION = "Group Description";
	private static final String OPT2 = "OPT2";

	private final CsticGroupModel csticGroupModel = new CsticGroupModelImpl();

	@Test
	public void testCloneConfigModel()
	{
		ConfigModel configModel = new RunTimeConfigMockFactory().createConfigMockForProductCode("CPQ_HOME_THEATER")
				.createDefaultConfiguration();

		ConfigModel clonedModel = ModelCloneFactory.cloneConfigModel(configModel);

		assertEquals(configModel, clonedModel);

		configModel = new RunTimeConfigMockFactory().createConfigMockForProductCode("CPQ_LAPTOP").createDefaultConfiguration();

		clonedModel = ModelCloneFactory.cloneConfigModel(configModel);

		assertEquals(configModel, clonedModel);
		assertEquals(configModel.hashCode(), clonedModel.hashCode());
	}

	@Test
	public void testClonePriceModel()
	{
		final PriceModel priceModel = new PriceModelImpl();
		priceModel.setPriceValue(new BigDecimal(5));
		priceModel.setCurrency("USD");
		final PriceModel clonedModel = ModelCloneFactory.clonePriceModel(priceModel);

		assertEquals(priceModel, clonedModel);
	}

	@Test
	public void testClonePriceModelWithObsoletePrice()
	{
		final PriceModel priceModel = new PriceModelImpl();
		priceModel.setPriceValue(new BigDecimal(5));
		priceModel.setCurrency("USD");
		priceModel.setObsoletePriceValue(new BigDecimal(10));
		final PriceModel clonedModel = ModelCloneFactory.clonePriceModel(priceModel);

		assertEquals(priceModel, clonedModel);
	}

	@Test
	public void testCloneKbKey()
	{
		final KBKey kbKey = new KBKeyImpl("product", "kbName", "kbLogsys", "kbVersion");
		final KBKey clonedKbKey = ModelCloneFactory.cloneKbKey(kbKey);
		assertEquals(kbKey, clonedKbKey);
	}

	@Test
	public void testCloneCsticValueModel()
	{
		final CsticValueModel csticValueModel = new CsticValueModelImpl();
		csticValueModel.setAuthor("ABC");

		final PriceModel deltaPrice = new PriceModelImpl();
		deltaPrice.setCurrency("USD");
		deltaPrice.setPriceValue(BigDecimal.valueOf(0));
		csticValueModel.setDeltaPrice(deltaPrice);

		final PriceModel valuePrice = new PriceModelImpl();
		valuePrice.setCurrency("EUR");
		valuePrice.setPriceValue(BigDecimal.valueOf(10));
		csticValueModel.setDeltaPrice(valuePrice);

		final CsticValueModel clonedCsticValueModel = ModelCloneFactory.cloneCsticValueModel(csticValueModel);

		assertEquals(csticValueModel, clonedCsticValueModel);
	}


	@Test
	public void testClone()
	{

		fillCsticGroup();

		final CsticGroupModel clonedCsticGroup = ModelCloneFactory.cloneCsticGroupModel(csticGroupModel);

		clonedCsticGroup.setName("2");
		clonedCsticGroup.setDescription("Other Description");
		final List<String> csticNames = new ArrayList<String>();
		csticNames.add(CSTIC_1 + "XXX");
		csticNames.add(CSTIC_2 + "XXX");
		clonedCsticGroup.setCsticNames(csticNames);

		assertNotEquals(csticGroupModel.getName(), clonedCsticGroup.getName());
		assertNotEquals(csticGroupModel.getDescription(), clonedCsticGroup.getDescription());
		assertNotEquals(csticGroupModel.getCsticNames().get(0), clonedCsticGroup.getCsticNames().get(0));
		assertNotEquals(csticGroupModel.getCsticNames().get(1), clonedCsticGroup.getCsticNames().get(1));
	}


	@Test
	public void testCloneMustBeEquals() throws Exception
	{
		fillCsticGroup();

		final CsticGroupModel clonedCsticGroup = ModelCloneFactory.cloneCsticGroupModel(csticGroupModel);

		assertEquals("Clone must be equal", csticGroupModel, clonedCsticGroup);
	}

	@Test
	public void testCloneMustHaveSomeHashCode() throws Exception
	{
		fillCsticGroup();

		final CsticGroupModel clonedCsticGroup = ModelCloneFactory.cloneCsticGroupModel(csticGroupModel);

		assertEquals("Clone must be equal", csticGroupModel.hashCode(), clonedCsticGroup.hashCode());

	}

	protected void fillCsticGroup()
	{
		final List<String> csticNames = new ArrayList<String>();
		csticNames.add(CSTIC_1);
		csticNames.add(CSTIC_2);

		csticGroupModel.setName(NAME);
		csticGroupModel.setDescription(DESCRIPTION);
		csticGroupModel.setCsticNames(csticNames);
	}

	@Test
	public void testCloneDoesNotModifyAuthor()
	{
		final CsticModelBuilder builder = new CsticModelBuilder();
		builder.withName(CSTIC_1, CSTIC_1);
		builder.singleSelection().addOption(OPT1, OPT1).addSelectedOption(OPT2, OPT2);
		builder.withDefaultUIState();
		final CsticModel sourceModel = builder.build();

		final CsticModel clonedModel = ModelCloneFactory.cloneCsticModel(sourceModel);
		assertEquals(CsticModel.AUTHOR_NOAUTHOR, clonedModel.getAuthor());
		assertNull(clonedModel.getAssignableValues().get(0).getAuthorExternal());
		assertNull(clonedModel.getAssignableValues().get(1).getAuthorExternal());
		assertNull(clonedModel.getAssignedValues().get(0).getAuthorExternal());
		assertNull(clonedModel.getAssignableValues().get(0).getAuthor());
		assertNull(clonedModel.getAssignableValues().get(1).getAuthor());
		assertNull(clonedModel.getAssignedValues().get(0).getAuthor());
	}

}
