/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.patches;

import static org.assertj.core.api.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.testframework.PropertyConfigSwitcher;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Joiner;


@IntegrationTest
public class AbstractPatchesSystemSetupTest extends ServicelayerBaseTest
{
	private static final String PATCH_A_ID = "patch_a";
	private static final String PATCHES_PREFIX_PROPERTY_KEY = "patches";
	private static final String NOT_SELECTED_POSTFIX_PROPERTY_KEY = "notSelected";
	private static final Joiner DOT_JOINER = Joiner.on('.').skipNulls();

	private static final PropertyConfigSwitcher PATCH_A_SWITCHER = new PropertyConfigSwitcher(buildPropertyKey(PATCH_A_ID));

	private TestPatchesSystemSetup testPatchesSystemSetup;

	@Before
	public void setUp()
	{
		testPatchesSystemSetup = new TestPatchesSystemSetup();
	}

	@After
	public void cleanUp()
	{
		PATCH_A_SWITCHER.switchBackToDefault();
	}

	@Test
	public void testIsPatchSelectedForTrue()
	{
		PATCH_A_SWITCHER.switchToValue("true");

		assertThat(testPatchesSystemSetup.isPatchSelected(PATCH_A_ID)).isFalse();
	}

	@Test
	public void testIsPatchSelectedForFalse()
	{
		PATCH_A_SWITCHER.switchToValue("false");

		assertThat(testPatchesSystemSetup.isPatchSelected(PATCH_A_ID)).isTrue();
	}

	@Test
	public void testIsPatchSelectedForNull()
	{

		assertThat(testPatchesSystemSetup.isPatchSelected(PATCH_A_ID)).isTrue();
	}

	private static final String buildPropertyKey(final String patchId)
	{
		final String[] keyElements =
		{ PATCHES_PREFIX_PROPERTY_KEY, patchId, NOT_SELECTED_POSTFIX_PROPERTY_KEY };
		return DOT_JOINER.join(keyElements);
	}


	private class TestPatchesSystemSetup extends AbstractPatchesSystemSetup
	{
		// noop
	}
}
