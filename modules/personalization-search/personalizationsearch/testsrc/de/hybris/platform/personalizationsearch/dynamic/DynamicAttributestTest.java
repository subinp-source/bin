/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.personalizationsearch.dynamic;

import static org.assertj.core.api.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.personalizationsearch.model.CxSearchProfileActionModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.enums.ActionType;
import de.hybris.platform.servicelayer.model.ModelService;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class DynamicAttributestTest extends ServicelayerTransactionalTest
{
	protected static final String SEARCH_PROFILE_CODE = "searchProfile";
	protected static final String SEARCH_PROFILE_CATALOG = "productCatalog";

	protected static final String ACTION_CODE = "action";
	protected static final String ACTION_TARGET = "actionTarget";

	private final static String CATALOG_ID = "testCatalog";
	private final static String VERSION_ONLINE = "Online";

	@Resource
	private ModelService modelService;

	@Resource
	private CatalogVersionService catalogVersionService;

	@Before
	public void setUp() throws Exception
	{
		createCoreData();
		createDefaultCatalog();
	}

	@Test
	public void shouldCalculateAffectedObjectKey()
	{
		// given
		final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion(CATALOG_ID, VERSION_ONLINE);

		final CxSearchProfileActionModel action = modelService.create(CxSearchProfileActionModel.class);
		action.setCode(ACTION_CODE);
		action.setSearchProfileCode(SEARCH_PROFILE_CODE);
		action.setSearchProfileCatalog(SEARCH_PROFILE_CATALOG);
		action.setTarget(ACTION_TARGET);
		action.setType(ActionType.PLAIN);
		action.setCatalogVersion(catalogVersion);

		// when
		final String affectedObjectKey = action.getAffectedObjectKey();

		// then
		assertThat(affectedObjectKey).isEqualTo(SEARCH_PROFILE_CODE);
	}
}
