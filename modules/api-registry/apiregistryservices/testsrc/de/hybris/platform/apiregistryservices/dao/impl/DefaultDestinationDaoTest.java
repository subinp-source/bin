/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.apiregistryservices.dao.DestinationDao;
import de.hybris.platform.apiregistryservices.model.AbstractDestinationModel;
import de.hybris.platform.apiregistryservices.model.ExposedDestinationModel;
import de.hybris.platform.apiregistryservices.services.CredentialService;
import de.hybris.platform.apiregistryservices.services.DestinationTargetService;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.BooleanUtils;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class DefaultDestinationDaoTest extends ServicelayerTransactionalTest
{
	private static final String KYMA_DEFAULT = "kymaDefault";
	private static final String FIRST_DEST = "first_dest";

	@Resource
	 private DestinationDao<AbstractDestinationModel> destinationDao;
	@Resource
	private ModelService modelService;
	@Resource
	private CredentialService credentialService;
	@Resource
	private DestinationTargetService destinationTargetService;

    @Before
    public void setUp() throws Exception
    {
        importCsv("/test/apis.impex", "UTF-8");
    }

    @Test
    public void getDestinationById() throws Exception
    {
        final AbstractDestinationModel firstDest = destinationDao.getDestinationById(FIRST_DEST);
        assertEquals("e1", firstDest.getEndpoint().getId());
    }

	@Test
	public void getActiveDestinationsByClientId() throws Exception
	{
		final List destinationsByChannel = destinationDao
				.findActiveExposedDestinationsByClientId("kyma");
		assertTrue(destinationsByChannel.size() == 4);
	}

	@Test
	public void findAllDestinationsCheckNonTemplateDestinationsFiltered()
	{
		final List<AbstractDestinationModel> allDestinations = destinationDao.findAllDestinations();
		assertFalse(allDestinations.stream().anyMatch(destination -> BooleanUtils.isTrue(destination.getDestinationTarget().getTemplate())));
		assertTrue(allDestinations.size() == 5);
	}

	@Test
	public void findActiveExposedDestinationsByDestinationTargetIdInactiveDestinationsFiltered()
	{
		final List<ExposedDestinationModel> destinations = destinationDao
				.findActiveExposedDestinationsByDestinationTargetId(KYMA_DEFAULT);

		assertTrue(destinations.stream().allMatch(AbstractDestinationModel::isActive));
		assertEquals(4, destinations.size());
	}

	@Test
	public void findDestinationByIdAndByDestinationTargetId()
	{
		final AbstractDestinationModel destination = destinationDao
				.findDestinationByIdAndByDestinationTargetId(FIRST_DEST, KYMA_DEFAULT);

		assertNotNull(destination);
		assertEquals(FIRST_DEST, destination.getId());
	}

	@Test
	public void getAllConsumedDestinations() throws ImpExException
	{
		assertTrue(destinationDao.findAllConsumedDestinations().size() == 0);

		importCsv("/test/consumedDestinations.impex", "UTF-8");

		assertTrue(destinationDao.findAllConsumedDestinations().size() == 1);
	}
}
