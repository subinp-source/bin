/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.dao.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.apiregistryservices.dao.DestinationTargetDao;
import de.hybris.platform.apiregistryservices.model.DestinationTargetModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;


@IntegrationTest
public class DefaultDestinationTargetDaoTest extends ServicelayerTransactionalTest
{
	private static final String KYMA_DEFAULT = "kymaDefault";
	private static final String KYMA_CRED_1 = "kymaCred1";
	private static final String TEST_CREDENTIAL = "testCredential";
	private static final String INVALID_TARGET_ID = "invalidTargetId";
	@Resource
	DestinationTargetDao destinationTargetDao;


	@Test
	public void testFindDestinationTargetByCredentialIdWithExposedOauthCredentialId() throws ImpExException
	{
		importCsv("/test/apis.impex", "UTF-8");

		final DestinationTargetModel destinationTarget = destinationTargetDao.findDestinationTargetByCredentialId(KYMA_CRED_1);

		Assert.assertEquals(KYMA_DEFAULT, destinationTarget.getId());
	}

	@Test
	public void testFindDestinationTargetByCredentialIdWithConsumedCertificateCredentialId() throws ImpExException
	{
		importCsv("/test/certificate.impex", "UTF-8");

		final DestinationTargetModel destinationTarget = destinationTargetDao.findDestinationTargetByCredentialId(TEST_CREDENTIAL);

		Assert.assertEquals(KYMA_DEFAULT, destinationTarget.getId());
	}

	@Test
	public void testFindDestinationTargetById() throws ImpExException
	{
		importCsv("/test/apis.impex", "UTF-8");

		final DestinationTargetModel destinationTarget = destinationTargetDao.findDestinationTargetById(KYMA_DEFAULT);

		Assert.assertEquals(KYMA_DEFAULT, destinationTarget.getId());
	}

	@Test(expected = ModelNotFoundException.class)
	public void testFindDestinationTargetByIdFailed() throws ImpExException
	{
		importCsv("/test/apis.impex", "UTF-8");
		destinationTargetDao.findDestinationTargetById(INVALID_TARGET_ID);
	}
}
