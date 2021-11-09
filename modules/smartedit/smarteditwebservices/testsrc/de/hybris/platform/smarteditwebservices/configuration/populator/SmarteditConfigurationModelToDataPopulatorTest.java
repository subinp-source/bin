/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.smarteditwebservices.configuration.populator;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.smarteditwebservices.configuration.SmarteditConfigurationModelFactory;
import de.hybris.platform.smarteditwebservices.data.ConfigurationData;
import de.hybris.platform.smarteditwebservices.model.SmarteditConfigurationModel;

import org.junit.Test;

@UnitTest
public class SmarteditConfigurationModelToDataPopulatorTest
{

	private SmarteditConfigurationModelToDataPopulator populator = new SmarteditConfigurationModelToDataPopulator();
	private final SmarteditConfigurationModel model = SmarteditConfigurationModelFactory.modelBuilder("1", "KEY", "VALUE");

	@Test
	public void populateNonEmptyData()
	{
		final ConfigurationData data = new ConfigurationData();
		populator.populate(model, data);
		assertThat(model.getKey(), is(data.getKey()));
		assertThat(model.getValue(), is(data.getValue()));
	}
}
