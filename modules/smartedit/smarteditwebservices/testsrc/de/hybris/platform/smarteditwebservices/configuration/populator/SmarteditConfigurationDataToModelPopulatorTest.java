/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.smarteditwebservices.configuration.populator;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.smarteditwebservices.data.ConfigurationData;
import de.hybris.platform.smarteditwebservices.model.SmarteditConfigurationModel;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import org.junit.Test;


@UnitTest
public class SmarteditConfigurationDataToModelPopulatorTest
{

	private SmarteditConfigurationDataToModelPopulator populator = new SmarteditConfigurationDataToModelPopulator();
	@Test
	public void populateNonEmptyData()
	{
		final ConfigurationData data = new ConfigurationData();
		data.setKey("KEY");
		data.setValue("VALUE");
		final SmarteditConfigurationModel model = new SmarteditConfigurationModel();
		populator.populate(data, model);
		assertThat(data.getKey(), is(model.getKey()));
		assertThat(data.getValue(), is(model.getValue()));
	}
}
