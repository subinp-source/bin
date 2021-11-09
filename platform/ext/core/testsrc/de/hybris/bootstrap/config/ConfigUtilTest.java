/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.bootstrap.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.ManualTest;
import de.hybris.platform.util.Utilities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.ImmutableMap;


@ManualTest
@RunWith(MockitoJUnitRunner.class)
public class ConfigUtilTest
{
	private static final Logger LOG = Logger.getLogger(ConfigUtilTest.class);

	private static final String BUILD_VERSION_KEY = "build.version";
	private static final String NEW_BUILD_VERSION_VALUE = "bar";
	private static final String NEW_FOO_KEY = "test.runtime.foo";
	private static final String NEW_FOO_VALUE = "1111.1111.111.0";
	private static final String NEW_FOO_KEY_SPECIALCHARS = "test.rüntime.föö";
	private static final String NEW_FOO_VALUE_SPECIALCHARS = "11$11.11%11.11äöüß1.0";

	private final String HYBRIS_OPT_CONFIG_DIR = "hybris.optional.config.dir";

	private Path runtimePropertiesFile;
	private Path runtimePropertiesFileUTF8;
	private Path optConfigDir;

	@Mock
	ConfigUtil.EnvProvider mockedEnvProvider;

	private PrintStream systemOut;

	@Before
	public void createTempPropertiesFile() throws IOException
	{
		final Properties runtimeProperties = new Properties();
		runtimeProperties.setProperty(NEW_FOO_KEY, NEW_FOO_VALUE);
		runtimeProperties.setProperty(NEW_FOO_KEY_SPECIALCHARS, NEW_FOO_VALUE_SPECIALCHARS);
		runtimeProperties.setProperty(BUILD_VERSION_KEY, NEW_BUILD_VERSION_VALUE);

		//Property file in default ISO-8859-1 encoding
		runtimePropertiesFile = Files.createTempFile("runtime", ".properties");
		OutputStream out = new FileOutputStream(runtimePropertiesFile.toFile());
		runtimeProperties.store(out, "");
		out.close();

		//Property file in UTF-8 encoding
		runtimePropertiesFileUTF8 = Files.createTempFile("runtimeUTF8", ".properties");
		out = new FileOutputStream(runtimePropertiesFileUTF8.toFile());
		final OutputStreamWriter writer = new OutputStreamWriter(out, "UTF-8");
		runtimeProperties.store(writer, "");
		writer.close();
		out.close();

		optConfigDir = Files.createTempDirectory("opt-config");

		systemOut = System.out;
	}

	@After
	public void deleteTempPropertiesFile() throws IOException
	{
		Files.delete(runtimePropertiesFile);
		FileUtils.deleteDirectory(optConfigDir.toFile());
		System.clearProperty(ConfigUtil.CONFIG_FILE_ENCODING_PROP);

		System.out.flush();
		System.setOut(systemOut);
	}

	/**
	 * UTF-8 encoded property files should be supported
	 */
	@Test
	public void loadFromUTF8Properties() throws IOException
	{
		// given
		final PlatformConfig platformConfig = Utilities.getPlatformConfig();
		final Properties platformWithRuntimeProperties = new Properties();
		when(mockedEnvProvider.getRuntimePropertiesPath()).thenReturn(runtimePropertiesFileUTF8.toString());
		ConfigUtil.envProvider = mockedEnvProvider;

		// when
		System.setProperty(ConfigUtil.CONFIG_FILE_ENCODING_PROP, "UTF-8");
		ConfigUtil.loadRuntimeProperties(platformWithRuntimeProperties, platformConfig);

		// then
		assertThat(platformWithRuntimeProperties.getProperty(NEW_FOO_KEY)).isEqualTo(NEW_FOO_VALUE);
		assertThat(platformWithRuntimeProperties.getProperty(NEW_FOO_KEY_SPECIALCHARS)).isEqualTo(NEW_FOO_VALUE_SPECIALCHARS);
		assertThat(platformWithRuntimeProperties.getProperty(BUILD_VERSION_KEY)).isEqualTo(NEW_BUILD_VERSION_VALUE);
	}

	/**
	 * When the requested encoding is not supported, property files should be read with default ISO 8859-1 encoding and not
	 * cause an exception.
	 */
	@Test
	public void loadFromPropertiesWithUnsupportedEncoding() throws IOException
	{
		// given
		final PlatformConfig platformConfig = Utilities.getPlatformConfig();
		final Properties platformWithRuntimeProperties = new Properties();
		when(mockedEnvProvider.getRuntimePropertiesPath()).thenReturn(runtimePropertiesFile.toString());
		ConfigUtil.envProvider = mockedEnvProvider;

		// when
		System.setProperty(ConfigUtil.CONFIG_FILE_ENCODING_PROP, "No-Such-Charset");
		ConfigUtil.loadRuntimeProperties(platformWithRuntimeProperties, platformConfig);

		// then
		assertThat(platformWithRuntimeProperties.getProperty(NEW_FOO_KEY)).isEqualTo(NEW_FOO_VALUE);
		assertThat(platformWithRuntimeProperties.getProperty(NEW_FOO_KEY_SPECIALCHARS)).isEqualTo(NEW_FOO_VALUE_SPECIALCHARS);
		assertThat(platformWithRuntimeProperties.getProperty(BUILD_VERSION_KEY)).isEqualTo(NEW_BUILD_VERSION_VALUE);
	}


	/**
	 * Using an incorrect encoding should cause failure when reading property files.
	 */
	@Test
	public void loadFromUTF8PropertiesWithWrongConfig() throws IOException
	{
		// given
		final PlatformConfig platformConfig = Utilities.getPlatformConfig();
		final Properties platformWithRuntimeProperties = new Properties();
		when(mockedEnvProvider.getRuntimePropertiesPath()).thenReturn(runtimePropertiesFileUTF8.toString());
		ConfigUtil.envProvider = mockedEnvProvider;

		// when
		ConfigUtil.loadRuntimeProperties(platformWithRuntimeProperties, platformConfig);

		// then
		assertThat(platformWithRuntimeProperties.getProperty(NEW_FOO_KEY_SPECIALCHARS)).isNullOrEmpty();
		assertThat(platformWithRuntimeProperties.getProperty(BUILD_VERSION_KEY)).isEqualTo(NEW_BUILD_VERSION_VALUE);

		// and when
		System.setProperty(ConfigUtil.CONFIG_FILE_ENCODING_PROP, "ISO-8859-1");
		ConfigUtil.loadRuntimeProperties(platformWithRuntimeProperties, platformConfig);

		// then
		assertThat(platformWithRuntimeProperties.getProperty(NEW_FOO_KEY_SPECIALCHARS)).isNullOrEmpty();
		assertThat(platformWithRuntimeProperties.getProperty(BUILD_VERSION_KEY)).isEqualTo(NEW_BUILD_VERSION_VALUE);
	}

	@Test
	public void loadAdditionalRuntimeProperties()
	{
		// given
		final PlatformConfig platformConfig = Utilities.getPlatformConfig();
		final Properties platformProperties = new Properties();

		// when
		ConfigUtil.loadRuntimeProperties(platformProperties, platformConfig);

		// then
		assertThat(platformProperties.getProperty(BUILD_VERSION_KEY)).isNotEqualTo(NEW_BUILD_VERSION_VALUE);
		assertThat(platformProperties.getProperty(NEW_FOO_KEY)).isNullOrEmpty();

		// and given
		final Properties platformWithRuntimeProperties = new Properties();
		when(mockedEnvProvider.getRuntimePropertiesPath()).thenReturn(runtimePropertiesFile.toString());
		ConfigUtil.envProvider = mockedEnvProvider;

		// when
		ConfigUtil.loadRuntimeProperties(platformWithRuntimeProperties, platformConfig);

		// then
		assertThat(platformWithRuntimeProperties.getProperty(NEW_FOO_KEY)).isEqualTo(NEW_FOO_VALUE);
		assertThat(platformWithRuntimeProperties.getProperty(BUILD_VERSION_KEY)).isEqualTo(NEW_BUILD_VERSION_VALUE);
	}

	@Test
	public void loadOptionalConfig()
	{
		// given
		final PlatformConfig platformConfig = Utilities.getPlatformConfig();
		final Properties platformProperties = new Properties();

		platformProperties.setProperty(HYBRIS_OPT_CONFIG_DIR, optConfigDir.toString());

		createPropFile("34-local.properties", ImmutableMap.of("fooz", "bar"));
		createPropFile("45-local.properties", ImmutableMap.of("fooz", "bar2", "hybris.url", "www.hybris.com"));

		// when
		ConfigUtil.loadRuntimeProperties(platformProperties, platformConfig);

		// then
		if (platformProperties.getProperty(HYBRIS_OPT_CONFIG_DIR).equals(optConfigDir.toString()))
		{
			assertThat(platformProperties.getProperty("fooz")).isEqualTo("bar2");
			assertThat(platformProperties.getProperty("hybris.url")).isEqualTo("www.hybris.com");
		}
		else
		{
			LOG.info(HYBRIS_OPT_CONFIG_DIR + " was overwritten - skipping test");
		}
	}


	@Test
	public void loadOptionalConfigViaEnvVariable()
	{
		// given
		final PlatformConfig platformConfig = Utilities.getPlatformConfig();
		final Properties platformProperties = new Properties();

		when(mockedEnvProvider.getOptionalConfigDirPath()).thenReturn(optConfigDir.toString());
		ConfigUtil.envProvider = mockedEnvProvider;

		createPropFile("34-local.properties", ImmutableMap.of("fooz", "bar"));
		createPropFile("45-local.properties", ImmutableMap.of("fooz", "bar3", "hybris.url", "www.hybris.com"));

		// when
		ConfigUtil.loadRuntimeProperties(platformProperties, platformConfig);

		// then
		assertThat(platformProperties.getProperty("fooz")).isEqualTo("bar3");
		assertThat(platformProperties.getProperty("hybris.url")).isEqualTo("www.hybris.com");
	}

	@Test
	public void shouldLogOnlyOnceUsageOfOptionalPropertiesEnvVariable()
	{
		// given
		final PlatformConfig platformConfig = Utilities.getPlatformConfig();
		final Properties platformProperties = new Properties();

		final PrintStream out = spy(systemOut);
		System.setOut(out);

		when(mockedEnvProvider.getOptionalConfigDirPath()).thenReturn(optConfigDir.toString());
		ConfigUtil.envProvider = mockedEnvProvider;

		createPropFile("34-local.properties", ImmutableMap.of("fooz", "bar"));
		createPropFile("45-local.properties", ImmutableMap.of("fooz", "bar3", "hybris.url", "www.hybris.com"));

		// when
		ConfigUtil.loadRuntimeProperties(platformProperties, platformConfig);
		ConfigUtil.loadRuntimeProperties(platformProperties, platformConfig);

		// then
		verify(out, times(1)).print("HYBRIS_OPT_CONFIG_DIR environment variable is set.");
		verify(out, times(1)).println(" Loading optional hybris properties from " + optConfigDir.toString() + " directory");
	}

	@Test
	public void loadFromEnv()
	{
		// given
		final PlatformConfig platformConfig = Utilities.getPlatformConfig();
		final Properties platformProperties = new Properties();

		final Map<String, String> testEnv = new HashMap<>();
		testEnv.put("not_this_one", "xxx");
		testEnv.put("ynot_that_one", "xxx");
		testEnv.put("y_first_second_third", "true");
		testEnv.put("y_aaa__bb_c__dd", "false");

		when(mockedEnvProvider.getEnv()).thenReturn(testEnv);
		ConfigUtil.envProvider = mockedEnvProvider;

		// when
		ConfigUtil.loadRuntimeProperties(platformProperties, platformConfig);

		// then
		assertThat(platformProperties.getProperty("not_this_one")).isNull();
		assertThat(platformProperties.getProperty("ynot_that_one")).isNull();
		assertThat(platformProperties.getProperty("first.second.third")).isEqualTo("true");
		assertThat(platformProperties.getProperty("aaa_bb.c_dd")).isEqualTo("false");
	}


	@Test
	public void getPropertyOrEnvShouldReturnNullIfNotSet()
	{
		assertThat(ConfigUtil.getPropertyOrEnv("foo")).isNull();
	}

	@Test
	public void getPropertyOrEnvShouldReturnPropertyIfSet()
	{
		ConfigUtil.envProvider = mockedEnvProvider;
		when(mockedEnvProvider.getenv("foo")).thenReturn("bar");

		assertThat(ConfigUtil.getPropertyOrEnv("foo")).isEqualTo("bar");
	}

	@Test
	public void getPropertyOrEnvShouldReturnEnvIfSet()
	{
		ConfigUtil.envProvider = mockedEnvProvider;
		when(mockedEnvProvider.getProperty("foo")).thenReturn("bar");

		assertThat(ConfigUtil.getPropertyOrEnv("foo")).isEqualTo("bar");
	}

	@Test
	public void getPropertyOrEnvShouldPreferEnvToProperty()
	{
		ConfigUtil.envProvider = mockedEnvProvider;
		when(mockedEnvProvider.getenv("foo")).thenReturn("env_value");
		when(mockedEnvProvider.getProperty("foo")).thenReturn("property_value");

		assertThat(ConfigUtil.getPropertyOrEnv("foo")).isEqualTo("env_value");
	}

	@Test
	public void shouldOverwritePropertiesFromEnvs()
	{
		// given
		final PlatformConfig platformConfig = Utilities.getPlatformConfig();
		final Properties platformProperties = new Properties();

		ConfigUtil.envProvider = mockedEnvProvider;

		when(mockedEnvProvider.getenv("HTTP_CONNECTOR_SECURE")).thenReturn("envOverwritten");
		when(mockedEnvProvider.getenv("HTTP_PORT")).thenReturn("envOverwritten");
		when(mockedEnvProvider.getenv("HTTPS_PORT")).thenReturn("envOverwritten");

		// when
		ConfigUtil.loadRuntimeProperties(platformProperties, platformConfig);

		// then
		assertThat(platformProperties.getProperty("tomcat.http.connector.secure")).isEqualTo("envOverwritten");
		assertThat(platformProperties.getProperty("tomcat.http.port")).isEqualTo("envOverwritten");
		assertThat(platformProperties.getProperty("tomcat.https.port")).isEqualTo("envOverwritten");
	}

	@Test
	public void shouldOverwritePropertiesFromProperties()
	{
		// given
		final PlatformConfig platformConfig = Utilities.getPlatformConfig();
		final Properties platformProperties = new Properties();

		ConfigUtil.envProvider = mockedEnvProvider;

		when(mockedEnvProvider.getProperty("HTTP_CONNECTOR_SECURE")).thenReturn("propOverwritten");
		when(mockedEnvProvider.getProperty("HTTP_PORT")).thenReturn("propOverwritten");
		when(mockedEnvProvider.getProperty("HTTPS_PORT")).thenReturn("propOverwritten");

		// when
		ConfigUtil.loadRuntimeProperties(platformProperties, platformConfig);

		// then
		assertThat(platformProperties.getProperty("tomcat.http.connector.secure")).isEqualTo("propOverwritten");
		assertThat(platformProperties.getProperty("tomcat.http.port")).isEqualTo("propOverwritten");
		assertThat(platformProperties.getProperty("tomcat.https.port")).isEqualTo("propOverwritten");
	}

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

}
