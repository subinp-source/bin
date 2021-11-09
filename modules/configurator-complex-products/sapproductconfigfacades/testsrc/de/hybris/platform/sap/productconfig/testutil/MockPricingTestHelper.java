/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.testutil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import de.hybris.platform.sap.productconfig.facades.ConfigurationData;
import de.hybris.platform.sap.productconfig.facades.CsticData;
import de.hybris.platform.sap.productconfig.facades.CsticValueData;
import de.hybris.platform.sap.productconfig.facades.UiGroupData;
import de.hybris.platform.sap.productconfig.facades.overview.CharacteristicGroup;
import de.hybris.platform.sap.productconfig.facades.overview.CharacteristicValue;
import de.hybris.platform.sap.productconfig.facades.overview.ConfigurationOverviewData;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


/**
 * Utility bean for testing pricing with active mock implementation
 */
@SuppressWarnings("javadoc")
public class MockPricingTestHelper
{
	private static final String SEPARATOR = "@";

	public static final String GROUP_DESCRIPTION = "Lens";
	public static final String CSTIC_NAME = "CAMERA_LENS_TYPE";
	public static final BigDecimal BASE_PRICE = BigDecimal.valueOf(750);
	public static final BigDecimal VALUE_PRICE = BigDecimal.valueOf(750);
	public static final String GROUP_NAME = "4";
	public static final String VALUE_NAME = "STANDARD_ZOOM_24_70";
	public static final List<String> CSTIC_UI_KEYS = new ArrayList<String>(
			Arrays.asList("1-CONF_CAMERA_SL" + MockPricingTestHelper.SEPARATOR + MockPricingTestHelper.GROUP_NAME
					+ MockPricingTestHelper.SEPARATOR + MockPricingTestHelper.CSTIC_NAME));


	public void checkConfigurationCallAssignableValuePrices(final ConfigurationData configuration,
			final BigDecimal expectedValuePrice)
	{
		final Optional<UiGroupData> uiGroup = configuration.getGroups().stream()
				.filter(group -> GROUP_DESCRIPTION.equals(group.getDescription())).findFirst();
		assertTrue(uiGroup.isPresent());
		final Optional<CsticData> csticOptional = uiGroup.get().getCstics().stream()
				.filter(cstic -> cstic.getName().equals(CSTIC_NAME)).findFirst();
		assertTrue(csticOptional.isPresent());
		final CsticData csticData = csticOptional.get();
		final List<CsticValueData> domainvalues = csticData.getDomainvalues();
		assertNotNull(domainvalues);
		assertEquals(4, domainvalues.size());
		assertEquals(expectedValuePrice, domainvalues.get(1).getPrice().getValue());
	}

	public void checkAssignedValuePrices(final ConfigurationOverviewData overviewData)
	{
		assertNotNull(overviewData);
		final Optional<CharacteristicGroup> overviewGroup = overviewData.getGroups().stream()
				.filter(group -> GROUP_DESCRIPTION.equals(group.getGroupDescription())).findFirst();
		assertTrue(overviewGroup.isPresent());
		final Optional<CharacteristicValue> overviewValue = overviewGroup.get().getCharacteristicValues().stream().findFirst();
		assertTrue(overviewValue.isPresent());
		final String expected = VALUE_PRICE.toString();
		final String actual = overviewValue.get().getPriceDescription();
		assertTrue(String.format("string '%s', does not contain '%s'", actual, expected), actual.contains(expected));
	}

}
