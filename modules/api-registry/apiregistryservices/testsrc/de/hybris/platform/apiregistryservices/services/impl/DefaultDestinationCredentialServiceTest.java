/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.services.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.apiregistryservices.model.AbstractCredentialModel;
import de.hybris.platform.apiregistryservices.model.BasicCredentialModel;
import de.hybris.platform.apiregistryservices.model.ConsumedCertificateCredentialModel;
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel;
import de.hybris.platform.apiregistryservices.model.ConsumedOAuthCredentialModel;
import de.hybris.platform.apiregistryservices.model.ExposedDestinationModel;
import de.hybris.platform.apiregistryservices.model.ExposedOAuthCredentialModel;
import de.hybris.platform.apiregistryservices.services.DestinationCredentialService;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

@UnitTest
public class DefaultDestinationCredentialServiceTest
{

	private final DestinationCredentialService destinationCredentialService = new DefaultDestinationCredentialService();

	private final List<AbstractCredentialModel> credentials = new ArrayList<>();

	@Before
	public void before()
	{
		/* All 4 different type of credentials */
		credentials.add(0, new BasicCredentialModel());
		credentials.add(1, new ConsumedCertificateCredentialModel());
		credentials.add(2, new ConsumedOAuthCredentialModel());
		credentials.add(3, new ExposedOAuthCredentialModel());
	}

	@Test
	public void validDestinationCredentialTest()
	{

		/* Set exposed destinations with all available credentials */
		final ExposedDestinationModel exposedDestinationWithBasicCredential = new ExposedDestinationModel();
		exposedDestinationWithBasicCredential.setCredential(credentials.get(0));

		final ExposedDestinationModel exposedDestinationWithCertificate = new ExposedDestinationModel();
		exposedDestinationWithCertificate.setCredential(credentials.get(1));

		final ExposedDestinationModel exposedDestinationWithConsumedOAuth = new ExposedDestinationModel();
		exposedDestinationWithConsumedOAuth.setCredential(credentials.get(2));

		final ExposedDestinationModel exposedDestinationWithExposedOAuth = new ExposedDestinationModel();
		exposedDestinationWithExposedOAuth.setCredential(credentials.get(3));


		/* Set consumed destinations with all available credentials */
		final ConsumedDestinationModel consumedDestinationWithBasicCredential = new ConsumedDestinationModel();
		consumedDestinationWithBasicCredential.setCredential(credentials.get(0));

		final ConsumedDestinationModel consumedDestinationWithCertificate = new ConsumedDestinationModel();
		consumedDestinationWithCertificate.setCredential(credentials.get(1));

		final ConsumedDestinationModel consumedDestinationWithConsumedOAuth = new ConsumedDestinationModel();
		consumedDestinationWithConsumedOAuth.setCredential(credentials.get(2));

		final ConsumedDestinationModel consumedDestinationWithExposedOAuth = new ConsumedDestinationModel();
		consumedDestinationWithExposedOAuth.setCredential(credentials.get(3));

		Assert.assertTrue( "ExposedDestination with BasicCredential", destinationCredentialService.isValidDestinationCredential(exposedDestinationWithBasicCredential));
		Assert.assertTrue("ExposedDestination with CertificateCredential", destinationCredentialService.isValidDestinationCredential(exposedDestinationWithCertificate));
		Assert.assertFalse("ExposedDestination with ConsumedOAuthCredential", destinationCredentialService.isValidDestinationCredential(exposedDestinationWithConsumedOAuth));
		Assert.assertTrue("ExposedDestination with ExposedOAuthCredential", destinationCredentialService.isValidDestinationCredential(exposedDestinationWithExposedOAuth));

		Assert.assertTrue("ConsumedDestination with BasicCredential", destinationCredentialService.isValidDestinationCredential(consumedDestinationWithBasicCredential));
		Assert.assertTrue("ConsumedDestination with CertificateCredential", destinationCredentialService.isValidDestinationCredential(consumedDestinationWithCertificate));
		Assert.assertFalse("ConsumedDestination with ExposedOAuthCredential", destinationCredentialService.isValidDestinationCredential(consumedDestinationWithExposedOAuth));
		Assert.assertTrue("ConsumedDestination with ConsumedOAuthCredential", destinationCredentialService.isValidDestinationCredential(consumedDestinationWithConsumedOAuth));
	}
}
