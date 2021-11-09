/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.converters.config;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.converters.ModifiableConfigurablePopulator;
import de.hybris.platform.converters.impl.DefaultModifableConfigurablePopulator;

import org.junit.Assert;
import org.junit.Test;


/**
 * Unit test for the {@link ConfigurablePopulatorModification} type.
 */
@UnitTest
public class ConfigurablePopulatorModificationTest
{
	@Test
	public void testInitialize()
	{
		final ModifiableConfigurablePopulator modifiablePopulator = new DefaultModifableConfigurablePopulator();

		final ConfigurablePopulatorModification mod1 = new ConfigurablePopulatorModification();
		mod1.setTarget(modifiablePopulator);
		mod1.setKeyType(OptionEnum.class);
		mod1.setKey("OPTION_1");

		final ConfigurablePopulatorModification mod2 = new ConfigurablePopulatorModification();
		mod2.setTarget(modifiablePopulator);
		mod2.setKeyType(OptionEnum.class);
		mod2.setKey("OPTION_2");

		mod1.initialize();
		mod2.initialize();

		Assert.assertEquals(OptionEnum.OPTION_1, mod1.getResolvedKey());
		Assert.assertEquals(OptionEnum.OPTION_2, mod2.getResolvedKey());
	}

	private enum OptionEnum
	{
		OPTION_1, OPTION_2;
	}
}
