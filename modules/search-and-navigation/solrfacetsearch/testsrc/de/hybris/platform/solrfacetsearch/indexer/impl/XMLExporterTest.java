/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.solrfacetsearch.indexer.impl;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.ExporterException;
import de.hybris.platform.testframework.HybrisJUnit4Test;

import org.junit.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 *
 */
public class XMLExporterTest extends HybrisJUnit4Test
{

	private XMLExporter xmlExporter;

	@Mock
	private IndexConfig mockIndexConfig;

	/**
	 * 
	 */
	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		xmlExporter = new XMLExporter();
	}

	@Test
	public void testGetExportDirEmptyPath()
	{
		when(mockIndexConfig.getExportPath()).thenReturn("");
		try
		{
			xmlExporter.getExportDirPath(mockIndexConfig);
		}
		catch (final ExporterException e)
		{
			fail("Should apply fallback value : hybris temp dir");
		}
	}

	@Test
	public void testGetExportDirNullPath()
	{
		when(mockIndexConfig.getExportPath()).thenReturn(null);
		try
		{
			xmlExporter.getExportDirPath(mockIndexConfig);
		}
		catch (final ExporterException e)
		{
			fail("Should apply fallback value : hybris temp dir");
		}
	}

	@Test
	public void testGetExportDirPath()
	{
		final String configPath = "test123";
		String exportPath = "";
		when(mockIndexConfig.getExportPath()).thenReturn(configPath);
		try
		{
			exportPath = xmlExporter.getExportDirPath(mockIndexConfig);
		}
		catch (final ExporterException e)
		{
			fail("Should use configured path");
		}
		Assert.assertEquals(configPath, exportPath);
	}
}
