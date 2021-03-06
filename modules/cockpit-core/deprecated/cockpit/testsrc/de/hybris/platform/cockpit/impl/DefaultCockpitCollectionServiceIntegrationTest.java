/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved
 */
package de.hybris.platform.cockpit.impl;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.CatalogService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cockpit.CockpitCollectionService;
import de.hybris.platform.cockpit.model.CockpitObjectAbstractCollectionModel;
import de.hybris.platform.cockpit.model.CockpitObjectCollectionModel;
import de.hybris.platform.cockpit.model.CockpitObjectSpecialCollectionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.CoreBasicDataCreator;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.Before;
import org.junit.Test;


/**
 * Integration test for cockpit collection service
 */
@IntegrationTest
public class DefaultCockpitCollectionServiceIntegrationTest extends ServicelayerTest
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultCockpitCollectionServiceIntegrationTest.class);

	@Resource
	private CockpitCollectionService cockpitCollectionService;

	@Resource
	private UserService userService;

	@Resource
	private CatalogService catalogService;

	@Resource
	private ProductService productService;


	@Before
	public void setUp() throws Exception
	{
		// Create data for tests
		LOG.info("Creating data for collections ..");
		userService.setCurrentUser(userService.getAdminUser());
		final long startTime = System.currentTimeMillis();
		new CoreBasicDataCreator().createEssentialData(Collections.EMPTY_MAP, null);
		// importing test csv
		importCsv("/test/testCollections.csv", "windows-1252");

		LOG.info("Finished data for collections " + (System.currentTimeMillis() - startTime) + "ms");
	}


	@Test
	public void testGetCollectionsForUser()
	{
		UserModel user = userService.getUserForUID("ahertz");
		List<CockpitObjectAbstractCollectionModel> collections = cockpitCollectionService.getCollectionsForUser(user);

		assertThat(collections).isNotNull();
		assertThat(collections.size()).isEqualTo(3);

		user = userService.getUserForUID("abrode");
		collections = cockpitCollectionService.getCollectionsForUser(user);

		assertThat(collections).isNotNull();
		assertThat(collections.size()).isEqualTo(4);
	}


	@Test
	public void testGetSpecialCollectionsForUser()
	{
		final UserModel user = userService.getUserForUID("ahertz");
		final List<CockpitObjectSpecialCollectionModel> collections = cockpitCollectionService.getSpecialCollectionsForUser(user);

		assertThat(collections).isNotNull();
		assertThat(collections.size()).isEqualTo(1);
		assertThat(collections.get(0).getQualifier()).isEqualTo("testSpecialA");
		assertThat(collections.get(0).getCollectionType().getCode()).isEqualTo("blacklist");
		assertThat(collections.get(0).getElements().size()).isEqualTo(2);
	}


	@Test
	public void testGetSpecialCollections()
	{
		final UserModel user = userService.getUserForUID("abrode");
		final List<CockpitObjectSpecialCollectionModel> collections = cockpitCollectionService.getSpecialCollections(user,
				"quickcollection");

		assertThat(collections).isNotNull();
		assertThat(collections.size()).isEqualTo(1);
		assertThat(collections.get(0).getQualifier()).isEqualTo("testSpecialB");
		assertThat(collections.get(0).getCollectionType().getCode()).isEqualTo("quickcollection");
		assertThat(collections.get(0).getElements().size()).isEqualTo(3);
	}
	
	@Test
	public void testGetElements()
	{
		final UserModel user = userService.getUserForUID("abrode");
		final List<CockpitObjectSpecialCollectionModel> collections = cockpitCollectionService.getSpecialCollections(user,
				"quickcollection");
		CockpitObjectSpecialCollectionModel threeElementsCollection = null;
		for (final CockpitObjectSpecialCollectionModel collection : collections)
		{
			if ("testSpecialB".equals(collection.getQualifier()))
			{
				threeElementsCollection = collection;
				break;
			}
		}

		assertThat(threeElementsCollection).isNotNull();

		List<ItemModel> elements = cockpitCollectionService.getElements(threeElementsCollection, 0, 100);

		assertThat(elements.size()).isEqualTo(3);
		assertThat(elements.get(0) instanceof ProductModel).isTrue();

		elements = cockpitCollectionService.getElements(threeElementsCollection, 0, 2);

		assertThat(elements.size()).isEqualTo(2);
	}


	@Test
	public void testCloneCollection()
	{
		final UserModel user = userService.getUserForUID("ahertz");
		final UserModel userB = userService.getUserForUID("abrode");
		final List<CockpitObjectAbstractCollectionModel> collections = cockpitCollectionService.getCollectionsForUser(user);
		final CockpitObjectCollectionModel collection = (CockpitObjectCollectionModel) collections.get(0);

		final CockpitObjectAbstractCollectionModel clone = cockpitCollectionService.cloneCollection(collection, userB);

		assertThat(clone).isNotNull();
		assertThat(clone.getUser()).isEqualTo(userB);
		assertThat(clone.getQualifier()).isEqualTo(collection.getQualifier());
		assertThat(clone.getElements().size()).isEqualTo(Integer.valueOf(collection.getElements().size()));
	}


	@Test
	public void testHasReadCollectionRight()
	{
		final UserModel otherUser = userService.getUserForUID("other");
		final UserModel abrode = userService.getUserForUID("abrode");
		final UserModel dejol = userService.getUserForUID("dejol");

		final List<CockpitObjectAbstractCollectionModel> collections = cockpitCollectionService.getCollectionsForUser(dejol);

		assertThat(collections).isNotNull();
		assertThat(collections.size()).isEqualTo(2);

		final CockpitObjectAbstractCollectionModel collection = collections.get(0);
		boolean hasRight = cockpitCollectionService.hasReadCollectionRight(otherUser, collection);
		assertThat(hasRight).isFalse();

		hasRight = cockpitCollectionService.hasReadCollectionRight(abrode, collection);
		assertThat(hasRight).isTrue();

		hasRight = cockpitCollectionService.hasReadCollectionRight(dejol, collection);
		assertThat(hasRight).isTrue();
	}


	@Test
	public void testHasWriteCollectionRight()
	{
		final UserModel otherUser = userService.getUserForUID("other");
		final UserModel abrode = userService.getUserForUID("abrode");
		final UserModel dejol = userService.getUserForUID("dejol");

		final List<CockpitObjectAbstractCollectionModel> collections = cockpitCollectionService.getCollectionsForUser(dejol);

		assertThat(collections).isNotNull();
		assertThat(collections.size()).isEqualTo(2);

		CockpitObjectAbstractCollectionModel writableCollection = null;
		for (final CockpitObjectAbstractCollectionModel collection : collections)
		{
			if ("writable".equals(collection.getQualifier()))
			{
				writableCollection = collection;
				break;
			}
		}

		boolean hasRight = cockpitCollectionService.hasWriteCollectionRight(otherUser, writableCollection);
		assertThat(hasRight).isFalse();

		hasRight = cockpitCollectionService.hasWriteCollectionRight(abrode, writableCollection);
		assertThat(hasRight).isTrue();

		hasRight = cockpitCollectionService.hasWriteCollectionRight(dejol, writableCollection);
		assertThat(hasRight).isTrue();
	}

}
