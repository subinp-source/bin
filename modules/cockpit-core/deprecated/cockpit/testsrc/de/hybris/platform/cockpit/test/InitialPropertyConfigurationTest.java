/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved
 */
package de.hybris.platform.cockpit.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.services.config.BaseConfiguration;
import de.hybris.platform.cockpit.services.config.InitialPropertyConfiguration;
import de.hybris.platform.cockpit.services.config.PropertyMappingConfiguration;

import java.util.Collection;

import org.junit.Test;


@IntegrationTest
public class InitialPropertyConfigurationTest extends UIConfigurationTestBase
{

	@Test
	public void testAdvancedSearchConfiguration() throws Exception
	{
		final ObjectTemplate objectTemplate = typeService.getObjectTemplate("Product.test");
		assertNotNull("Object template not found", objectTemplate);
		final BaseConfiguration baseConfiguration = uiConfigurationService.getComponentConfiguration(objectTemplate, "base",
				BaseConfiguration.class);
		assertNotNull("UIComponentConfig not found", baseConfiguration);
		final InitialPropertyConfiguration initialPropertyConfig = baseConfiguration.getInitialPropertyConfiguration(
				objectTemplate, null);
		assertNotNull(initialPropertyConfig);
		assertEquals(2, initialPropertyConfig.getPropertyMappingConfigurations().size());
		assertTrue(containsMapping("source1", "target1", initialPropertyConfig.getPropertyMappingConfigurations()));
	}

	private boolean containsMapping(final String source, final String target,
			final Collection<PropertyMappingConfiguration> propertyMappingConfigurations)
	{
		for (final PropertyMappingConfiguration config : propertyMappingConfigurations)
		{
			if (source.equals(config.getSource()) && target.equals(config.getTarget()))
			{
				return true;
			}
		}
		return false;
	}

}
