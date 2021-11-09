/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.y2ysync.deltadetection;

import static org.assertj.core.api.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.type.TypeService;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class InfoExpressionGeneratorTest extends ServicelayerBaseTest
{
	@Resource
	private TypeService typeService;

	private InfoExpressionGenerator generator;

	@Before
	public void setUp()
	{
		generator = new InfoExpressionGenerator(typeService);
	}

	@Test
	public void shouldGenerateProperInfoExpressionForCatalog()
	{
		final String expected = "#{getId()}";

		final String actual = generator.getInfoExpressionForType("Catalog");

		assertThat(actual).isNotNull().isNotEmpty().isEqualTo(expected);
	}

	@Test
	public void shouldGenerateProperInfoExpressionForCatalogVersion()
	{
		final String expected = "#{getCatalog().getId()}|#{getVersion()}";

		final String actual = generator.getInfoExpressionForType("CatalogVersion");

		assertThat(actual).isNotNull().isNotEmpty().isEqualTo(expected);
	}

	@Test
	public void shouldGenerateProperInfoExpressionForCategory()
	{
		final String expected = "#{getCatalogVersion().getCatalog().getId()}:#{getCatalogVersion().getVersion()}|#{getCode()}";

		final String actual = generator.getInfoExpressionForType("Category");

		assertThat(actual).isNotNull().isNotEmpty().isEqualTo(expected);
	}

	@Test
	public void shouldGenerateProperInfoExpressionForProduct()
	{
		final String expected = "#{getCatalogVersion().getCatalog().getId()}:#{getCatalogVersion().getVersion()}|#{getCode()}";

		final String actual = generator.getInfoExpressionForType("Product");

		assertThat(actual).isNotNull().isNotEmpty().isEqualTo(expected);
	}
}
