/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.util.config;

import static org.assertj.core.api.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.testframework.PropertyConfigSwitcher;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Test;

@IntegrationTest
public class PropertyActionReaderTest extends ServicelayerBaseTest
{

	@Resource
	PropertyActionReader propertyActionReader;

	private final PropertyConfigSwitcher aclRemovalForTypes = new PropertyConfigSwitcher("acl.removal.disabled.for.types");
	private final PropertyConfigSwitcher aclRemovalForTypeTitle = new PropertyConfigSwitcher(
			"acl.removal.disabled.for.type.Title");

	@After
	public void cleanUp()
	{
		aclRemovalForTypes.switchBackToDefault();
		aclRemovalForTypeTitle.switchBackToDefault();
		propertyActionReader.clearConfiguration();
	}

	@Test
	public void shouldReturnFalseWhenActionAclRemovalIsDisabledForTitle()
	{
		final boolean isDisabled = propertyActionReader.isActionDisabledForType("acl.removal", "Title");
		assertThat(isDisabled).isFalse();
	}

	@Test
	public void shouldReturnFalseWhenActionForTitleIsConfiguredInListAndSwitchedOffIndividually()
	{
		aclRemovalForTypes.switchToValue("TiTle");
		aclRemovalForTypeTitle.switchToValue("false");
		propertyActionReader.clearConfiguration();

		final boolean isDisabled = propertyActionReader.isActionDisabledForType("acl.removal", "Title");
		assertThat(isDisabled).isFalse();
	}

	@Test
	public void shouldReturnTrueWhenActionForTitleIsConfiguredIndividually()
	{
		aclRemovalForTypeTitle.switchToValue("true");
		propertyActionReader.clearConfiguration();

		final boolean isDisabled = propertyActionReader.isActionDisabledForType("acl.removal", "Title");
		assertThat(isDisabled).isTrue();
	}

}
