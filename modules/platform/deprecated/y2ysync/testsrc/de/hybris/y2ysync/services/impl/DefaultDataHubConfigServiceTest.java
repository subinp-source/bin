/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.y2ysync.services.impl;

import static de.hybris.y2ysync.XMLContentAssert.assertThat;

import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.DescriptorModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.y2ysync.model.Y2YColumnDefinitionModel;
import de.hybris.y2ysync.model.Y2YStreamConfigurationContainerModel;
import de.hybris.y2ysync.model.Y2YStreamConfigurationModel;
import de.hybris.y2ysync.services.DataHubConfigService;
import de.hybris.y2ysync.services.DataHubExtGenerationConfig;
import de.hybris.y2ysync.services.SyncConfigService;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;


@IntegrationTest
public class DefaultDataHubConfigServiceTest extends ServicelayerTransactionalBaseTest
{
	@Resource
	private ModelService modelService;
	@Resource
	private DataHubConfigService dataHubConfigService;
	@Resource
	private SyncConfigService syncConfigService;
	@Resource
	private TypeService typeService;
	private Y2YStreamConfigurationContainerModel container;

	@Before
	public void setUp()
	{
		container = syncConfigService.createStreamConfigurationContainer("testContainer");
		modelService.save(container);
	}

	@Test
	public void shouldThrowIllegalStateExceptionWhenForUnsavedStreamConfiguration()
	{
		// given
		final Set<AttributeDescriptorModel> attributeDescriptors = getAttributeDescriptorsFor("Product", "code", "catalogVersion",
				"name", "thumbnail");
		final Y2YStreamConfigurationModel configuration = syncConfigService.createStreamConfiguration(container, "Product",
				attributeDescriptors,
				Collections.emptySet());

		try
		{
			final DataHubExtGenerationConfig dataHubExtGenerationConfig = getDataHubExtGenerationConfig();
			// when
			dataHubConfigService.createModelDefinitions(configuration, dataHubExtGenerationConfig);
			fail("Should throw IllegalStateException");
		}
		catch (final IllegalStateException e)
		{
			// then fine
		}
	}

	@Test
	public void shouldThrowIllegalStateExceptionWhenForUnsavedStreamConfigurationContainer()
	{
		// given
		final Y2YStreamConfigurationContainerModel container = syncConfigService.createStreamConfigurationContainer("unsaved");

		try
		{
			// when
			dataHubConfigService.createModelDefinitions(container);
			fail("Should throw IllegalStateException");
		}
		catch (final IllegalStateException e)
		{
			// then fine
		}
	}

	@Test
	public void shouldGenerateDataHubModelForStreamConfiguration() throws Exception
	{
		// given
		final Set<AttributeDescriptorModel> attributeDescriptors = getAttributeDescriptorsFor("Product", "code", "catalogVersion",
				"name", "thumbnail");
		final Y2YStreamConfigurationModel configuration = syncConfigService.createStreamConfiguration(container, "Product",
				attributeDescriptors,
				Collections.emptySet());
		modelService.save(configuration);

		final DataHubExtGenerationConfig dataHubExtGenerationConfig = getDataHubExtGenerationConfig();

		// when
		final String modelDefinitions = dataHubConfigService.createModelDefinitions(configuration, dataHubExtGenerationConfig);

		// then
		assertThat(modelDefinitions).isNotNull()
		                            .ignoreWhitespaces()
		                            .ignoreNodeOrder()
		                            .ignoreComments()
		                            .isIdenticalToResource("/test/datahubxml/dataHubModelFromStreamConfig.xml");

	}

	private DataHubExtGenerationConfig getDataHubExtGenerationConfig()
	{
		final DataHubExtGenerationConfig dataHubExtGenerationConfig = new DataHubExtGenerationConfig();
		dataHubExtGenerationConfig.setGenerateRawItems(true);
		dataHubExtGenerationConfig.setGenerateCanonicalItems(true);
		dataHubExtGenerationConfig.setGenerateTargetItems(true);
		dataHubExtGenerationConfig.setPrettyFormat(false);
		dataHubExtGenerationConfig.setTargetExportCodes("");
		dataHubExtGenerationConfig.setTargetExportURL("http://www.xsd2xml.com");
		dataHubExtGenerationConfig.setTargetPassword("str1234");
		dataHubExtGenerationConfig.setTargetType("str1234");
		dataHubExtGenerationConfig.setTargetUserName("str1234");
		return dataHubExtGenerationConfig;
	}

	@Test
	public void shouldGenerateDataHubModelForStreamConfigurationContainer() throws Exception
	{
		// given
		final Set<AttributeDescriptorModel> productDescriptors = getAttributeDescriptorsFor("Product", "code", "catalogVersion",
				"name", "thumbnail");
		final Set<AttributeDescriptorModel> titleDescriptors = getAttributeDescriptorsFor("Title", "code", "name");
		final Y2YStreamConfigurationModel productConfig = syncConfigService.createStreamConfiguration(container, "Product",
				productDescriptors,
				Collections.emptySet());
		final Y2YStreamConfigurationModel titleConfig = syncConfigService.createStreamConfiguration(container, "Title",
				titleDescriptors,
				Collections.emptySet());
		modelService.saveAll(productConfig, titleConfig);

		final DataHubExtGenerationConfig dataHubExtGenerationConfig = getDataHubExtGenerationConfig();

		// when
		final String modelDefinitions = dataHubConfigService.createDataHubExtension(container, dataHubExtGenerationConfig);

		// then
		assertThat(modelDefinitions).isNotNull()
		                            .ignoreComments()
		                            .ignoreNodeOrder()
		                            .ignoreWhitespaces()
		                            .isIdenticalToResource("/test/datahubxml/dataHubModelFromStreamConfigContainer.xml");
	}


	@Test
	public void shouldGenerateDataHubModelForStreamConfigurationWithUntypedColumnDefintion() throws Exception
	{
		final Y2YColumnDefinitionModel mediaColDef = syncConfigService.createUntypedColumnDefinition("@media(Translator)",
				"pullURL");
		final Set<AttributeDescriptorModel> mediaDesriptors = getAttributeDescriptorsFor("Media", "code", "mime");
		final Y2YStreamConfigurationModel configuration = syncConfigService.createStreamConfiguration(container, "Media",
				mediaDesriptors,
				Sets.newHashSet(
						mediaColDef));
		modelService.save(configuration);

		// when
		final DataHubExtGenerationConfig dataHubExtGenerationConfig = getDataHubExtGenerationConfig();

		final String modelDefinitions = dataHubConfigService.createModelDefinitions(configuration, dataHubExtGenerationConfig);

		// then
		assertThat(modelDefinitions)
				.isNotNull()
				.ignoreWhitespaces()
				.ignoreNodeOrder()
				.ignoreComments()
				.isIdenticalToResource("/test/datahubxml/dataHubModelForStreamConfigurationWithUntypedColumnDefintion.xml");

	}

	private Set<AttributeDescriptorModel> getAttributeDescriptorsFor(final String typeCode, final String... qualifiers)
	{
		return Arrays.stream(qualifiers).map(q -> typeService.getAttributeDescriptor(typeCode, q))
		             .sorted(Comparator.comparing(DescriptorModel::getQualifier))
		             .collect(Collectors.toCollection(LinkedHashSet::new));

	}

}
