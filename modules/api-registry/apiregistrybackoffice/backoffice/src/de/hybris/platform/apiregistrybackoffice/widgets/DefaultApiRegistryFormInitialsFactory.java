/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistrybackoffice.widgets;

import de.hybris.platform.apiregistrybackoffice.data.ApiRegistryResetCredentialsForm;
import de.hybris.platform.util.Config;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.KeyGenerator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory for ApiRegistryResetCredentialsForm creation.
 */
public class DefaultApiRegistryFormInitialsFactory
{

	private static final String GRACE_PERIOD_PROP = "apiregistryservices.credentials.gracePeriod";
	private static final String SECRET_ALG_PROP = "apiregistryservices.credentials.secretAlgorithm";
	private static final String SECRET_SIZE_PROP = "apiregistryservices.credentials.secretSize";
	private static final int DEFAULT_SECRET_SIZE = 256;

	private static final Logger LOG = LoggerFactory.getLogger(DefaultApiRegistryFormInitialsFactory.class);

	/**
     * Factory-method for ApiRegistryResetCredentialsForm default creation.
     */
    public ApiRegistryResetCredentialsForm getApiRegistryResetCredentialsForm()
    {
        final ApiRegistryResetCredentialsForm resetCredentialsForm = new ApiRegistryResetCredentialsForm();
		resetCredentialsForm.setGracePeriod(Config.getInt(GRACE_PERIOD_PROP, 0));
		final String secretAlg = Config.getString(SECRET_ALG_PROP, "AES");
		try
		{
			final KeyGenerator keyGenerator = KeyGenerator.getInstance(secretAlg);
			keyGenerator.init(Config.getInt(SECRET_SIZE_PROP, DEFAULT_SECRET_SIZE));
			resetCredentialsForm.setClientSecret(Base64.getEncoder().encodeToString(keyGenerator.generateKey().getEncoded()));

		}
		catch (final NoSuchAlgorithmException e)
		{
			LOG.error(String.format("Cannot set default secret for Reset Credentials Form. Inexistent algorithm : %s", secretAlg),
					e);
		}
        return resetCredentialsForm;
    }
}
