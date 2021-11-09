/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.bootstrap.config;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.util.Utilities;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@UnitTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test/dummy-test-spring.xml")
//just for sake to run an autowiring
public class TenantsInfoLoaderTest
{

	private PlatformConfig localPlatformConfig;

	private File platformHome;
	private File configDir;

	@Before
	public void setUp()
	{
		this.localPlatformConfig = Utilities.getPlatformConfig();

		//both platform and config dir use the same for test cases
		final String testDir = "ext/core/resources/test";
		this.platformHome = new File(localPlatformConfig.getPlatformHome(), testDir);
		this.configDir = platformHome;
	}

	@Test
	public void testAssureTenantsInfoLoaderWhenNoLocalNoGlobalFileAvaialble()
	{
		try
		{
			final TestTenantsInforLoader loader = new TestTenantsInforLoader(localPlatformConfig, Collections.singleton("noProp"),
					platformHome, configDir);
			loader.getSlaveTenants();
			fail("BootstrapConfigException expected");
		}
		catch (final BootstrapConfigException bce)
		{
			//ok, expected
		}
	}

	@Test
	public void testLoadTwoTenants()
	{
		final TestTenantsInforLoader loader = new TestTenantsInforLoader(localPlatformConfig, Arrays.asList("foo", "bar"),
				platformHome, configDir);
		final Map<String, TenantInfo> slaveTenants = loader.getSlaveTenants();
		assertEquals(2, slaveTenants.size());
		assertNotNull(slaveTenants.get("bar"));
		assertNotNull(slaveTenants.get("foo"));
	}

	/**
	 * content of "core/resources/test/tenant_bar.properties":
	 *
	 * <pre>
	 * foo=value2
	 * faa=${db.url}
	 * </pre>
	 */
	@Test
	public void testAssureTenantsInfoLoaderWhenGlobalFileAvailable()
	{
		final TestTenantsInforLoader loader = new TestTenantsInforLoader(localPlatformConfig, Collections.singleton("bar"),
				platformHome, configDir);
		final Map<String, TenantInfo> slaveTenants = loader.getSlaveTenants();
		assertEquals(1, slaveTenants.size());
		for (final Map.Entry<String, TenantInfo> entry : slaveTenants.entrySet())
		{
			assertEquals("bar", entry.getKey());
			final TenantInfo tenantBarInfo = entry.getValue();
			final Properties tenantBarProperties = tenantBarInfo.getTenantProperties();
			assertEquals(3, tenantBarProperties.size());
			assertEquals("value2", tenantBarProperties.get("foo"));
			assertEquals("${db.url}", tenantBarProperties.get("faa"));
			final String allowedExtensions = (String) tenantBarProperties.get(TenantsInfoLoader.ALLOWED_EXTENSIONS);
			assertTrue(allowedExtensions.indexOf("core") > -1);
		}
	}

	/**
	 * content of "core/resources/test/local_tenant_foo.properties":
	 *
	 * <pre>
	 * boo=value1
	 * baa=${HYBRIS_BIN_DIR}
	 * baar.webroot=/baar
	 * boor.webroot=boor
	 * </pre>
	 */
	@Test
	public void testAssureTenantsInfoLoaderWhenLocalFileAvailable()
	{
		final TestTenantsInforLoader loader = new TestTenantsInforLoader(localPlatformConfig, Collections.singleton("foo"),
				platformHome, configDir);
		final Map<String, TenantInfo> slaveTenants = loader.getSlaveTenants();
		assertEquals(1, slaveTenants.size());
		for (final Map.Entry<String, TenantInfo> entry : slaveTenants.entrySet())
		{
			assertEquals("foo", entry.getKey());
			final TenantInfo tenantBarInfo = entry.getValue();
			final Properties tenantBarProperties = tenantBarInfo.getTenantProperties();
			assertEquals(5, tenantBarProperties.size());
			assertEquals("value1", tenantBarProperties.get("boo"));
			final String replacedProp = localPlatformConfig.getSystemConfig().replaceProperties("${HYBRIS_BIN_DIR}");
			assertEquals(replacedProp, tenantBarProperties.get("baa"));
			final String allowedExtensions = (String) tenantBarProperties.get(TenantsInfoLoader.ALLOWED_EXTENSIONS);
			assertTrue(allowedExtensions.indexOf("core") > -1);

			assertEquals("/baar", tenantBarInfo.getWebMapping("baar"));
			assertTrue(tenantBarInfo.isWebrootOwner("/baar"));
			assertEquals("boor", tenantBarInfo.getWebMapping("boor"));
			assertTrue(tenantBarInfo.isWebrootOwner("boor"));

			assertFalse(tenantBarInfo.isWebrootOwner("/foo"));
		}
	}

	/**
	 * content of "core/resources/test/tenant_both.properties":
	 *
	 * <pre>
	 * brr = valuePlatform
	 * </pre>
	 * <p>
	 * content of "core/resources/test/local_tenant_both.properties":
	 *
	 * <pre>
	 * brr = valueLocal
	 * </pre>
	 */
	@Test
	public void testAssureTenantsInfoLoaderWhenBothFilesAvailable()
	{
		final TestTenantsInforLoader loader = new TestTenantsInforLoader(localPlatformConfig, Collections.singleton("both"),
				platformHome, configDir);
		final Map<String, TenantInfo> slaveTenants = loader.getSlaveTenants();
		assertEquals(1, slaveTenants.size());
		for (final Map.Entry<String, TenantInfo> entry : slaveTenants.entrySet())
		{
			assertEquals("both", entry.getKey());
			final TenantInfo tenantBarInfo = entry.getValue();
			final Properties tenantBarProperties = tenantBarInfo.getTenantProperties();
			assertEquals(2, tenantBarProperties.size());
			assertEquals("valueLocal", tenantBarProperties.get("brr"));
			final String allowedExtensions = (String) tenantBarProperties.get(TenantsInfoLoader.ALLOWED_EXTENSIONS);
			assertTrue(allowedExtensions.indexOf("core") > -1);
		}
	}

	static class TestTenantsInforLoader extends TenantsInfoLoader
	{
		final Collection<String> installed;
		final File plHome;
		final File cfgDir;

		TestTenantsInforLoader(final PlatformConfig platformConfig, final Collection<String> installed, final File plHome,
		                       final File cfgDir)
		{
			super(platformConfig);
			this.installed = installed;
			this.plHome = plHome;
			this.cfgDir = cfgDir;
		}

		@Override
		protected Collection<String> getInstalledTenantIDs()
		{
			return installed;
		}

		@Override
		protected File getPlatformHome()
		{
			return plHome;
		}

		@Override
		protected File getConfigDir()
		{
			return cfgDir;
		}
	}

}
