/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.bootstrap.config;


import com.google.common.collect.ImmutableMap;

import de.hybris.bootstrap.annotations.UnitTest;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;


@UnitTest
public class DirectoryConfigLoaderTest
{

	private static final String LOCAL_PROPERTIES = "local.properties";

	private Path optConfigDir;

	private File createPropFile(final String name, final Map props)
	{
		final Properties properties = new Properties();
		properties.putAll(props);

		final File file = optConfigDir.resolve(name).toFile();

		try
		{
			final FileOutputStream fileOut = new FileOutputStream(file);
			properties.store(fileOut, name);
			fileOut.close();
		}
		catch (final IOException e)
		{
			e.printStackTrace();
			throw new RuntimeException("Couldn't create temp property file for test");
		}
		return file;
	}


	@Before
	public void createTempPropertiesFile() throws IOException
	{
		optConfigDir = Files.createTempDirectory("opt-config");
	}

	@After
	public void deleteTempPropertiesFile() throws IOException
	{
		FileUtils.deleteDirectory(optConfigDir.toFile());
	}


	@Test
	public void shouldReturnZeroPropsForNullPath()
	{
		final Properties props = DirectoryConfigLoader.loadFromDir((File) null, LOCAL_PROPERTIES);
		assertThat(props).hasSize(0);
	}

	@Test
	public void shouldNotLoadFromNonexistentPath()
	{
		// given
		createPropFile("10-local.properties", ImmutableMap.of("foo", "bar"));

		// when
		final Properties props = DirectoryConfigLoader.loadFromDir(optConfigDir.resolve("aa").toFile(), LOCAL_PROPERTIES);

		// then
		assertThat(props).hasSize(0);
	}

	@Test
	public void shouldNotLoadFromExistingPropertyFile()
	{
		// given
		final File createdFile = createPropFile("10-local.properties", ImmutableMap.of("foo", "bar"));

		// when
		final Properties props = DirectoryConfigLoader.loadFromDir(createdFile, LOCAL_PROPERTIES);

		// then
		assertThat(props).hasSize(0);
	}

	@Test
	public void shouldReadSingleFileFromDir()
	{
		// given
		createPropFile("10-local.properties", ImmutableMap.of("foo", "bar"));

		// when
		final Properties props = DirectoryConfigLoader.loadFromDir(optConfigDir.toFile(), LOCAL_PROPERTIES);

		// then
		assertThat(props.getProperty("foo")).isEqualTo("bar");
	}

	@Test
	public void shouldNotReadFromFilesNotMatchingPattern()
	{
		// given
		createPropFile("1-local.properties", ImmutableMap.of("foo", "bar"));
		createPropFile("100-local.properties", ImmutableMap.of("foo2", "bar2"));

		// when
		final Properties props = DirectoryConfigLoader.loadFromDir(optConfigDir.toFile(), LOCAL_PROPERTIES);

		// then
		assertThat(props).hasSize(0);
	}


	@Test
	public void shouldMergeAndOverrideProperties()
	{
		// given
		createPropFile("10-local.properties", ImmutableMap.of("foo", "bar"));
		createPropFile("20-local.properties", ImmutableMap.of("foo", "bar2", "hybris.url", "www.hybris.com"));


		// when
		final Properties props = DirectoryConfigLoader.loadFromDir(optConfigDir.toFile(), LOCAL_PROPERTIES);

		// then
		assertThat(props).hasSize(2);
		assertThat(props.getProperty("foo")).isEqualTo("bar2");
		assertThat(props.getProperty("hybris.url")).isEqualTo("www.hybris.com");
	}

	@Test
	public void shouldSkipNotMatchingSuffix()
	{
		// given
		createPropFile("34-prop", ImmutableMap.of("foo", "bar"));
		createPropFile("45-prop", ImmutableMap.of("foo", "bar2", "hybris.url", "www.hybris.com"));
		createPropFile("50-local.properties", ImmutableMap.of("foo", "bar3", "role", "b2c"));

		// when
		final Properties props = DirectoryConfigLoader.loadFromDir(optConfigDir.toFile(), "prop");

		// then
		assertThat(props).hasSize(2);
		assertThat(props.getProperty("foo")).isEqualTo("bar2");
		assertThat(props.getProperty("hybris.url")).isEqualTo("www.hybris.com");
	}

	@Test
	public void shouldHandlePrioritiesUnderTen()
	{
		// given
		createPropFile("10-local.properties", ImmutableMap.of("foo", "bar3"));
		createPropFile("09-local.properties", ImmutableMap.of("foo", "bar2", "foo09", "09"));
		createPropFile("01-local.properties", ImmutableMap.of("foo", "bar1", "foo01", "01"));
		createPropFile("00-local.properties", ImmutableMap.of("foo", "bar0", "foo00", "00", "foo01", "00"));

		// when
		final Properties props = DirectoryConfigLoader.loadFromDir(optConfigDir.toFile(), LOCAL_PROPERTIES);

		// then
		assertThat(props).hasSize(4);
		assertThat(props.getProperty("foo")).isEqualTo("bar3");
		assertThat(props.getProperty("foo00")).isEqualTo("00");
		assertThat(props.getProperty("foo01")).isEqualTo("01");
		assertThat(props.getProperty("foo09")).isEqualTo("09");
	}

}
