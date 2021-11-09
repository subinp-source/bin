/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.test;

import static org.assertj.core.api.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;
import de.hybris.platform.util.encryption.EncryptionUtil;

import org.assertj.core.api.ThrowableAssert;
import org.junit.Test;


@IntegrationTest
public class EncryptionUtilTest extends ServicelayerBaseTest
{
	@Test
	public void shouldNotThrowExceptionDuringMigration()
	{

		final Throwable actual = ThrowableAssert
				.catchThrowable(() -> EncryptionUtil.migrate("User", "passwordAnswer", null));
		assertThat(actual).isNull();
	}

}
