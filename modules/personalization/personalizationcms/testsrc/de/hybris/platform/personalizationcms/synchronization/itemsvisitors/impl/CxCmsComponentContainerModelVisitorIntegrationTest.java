/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.personalizationcms.synchronization.itemsvisitors.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.personalizationcms.model.CxCmsComponentContainerModel;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;


@IntegrationTest
public class CxCmsComponentContainerModelVisitorIntegrationTest extends ServicelayerTest {

    private CxCmsComponentContainerModelVisitor visitor = new CxCmsComponentContainerModelVisitor();

    @Resource
    private FlexibleSearchService flexibleSearchService;

    @Resource
    private CatalogVersionService catalogVersionService;

    @Before
    public void initTest() throws Exception {
        createCoreData();
        createDefaultCatalog();
        importCsv("/personalizationcms/test/testdata_personalizationcms.impex", "utf-8");
    }

    @Test
    public void shouldVisitDefaultComponent() {
        final CatalogVersionModel cv = catalogVersionService.getCatalogVersion("testCatalog", "Online");

        final CxCmsComponentContainerModel exampleContainer = new CxCmsComponentContainerModel();
        exampleContainer.setSourceId("container1");
        exampleContainer.setCatalogVersion(cv);
        final CxCmsComponentContainerModel container = flexibleSearchService.getModelByExample(exampleContainer);

        final List<ItemModel> visited = visitor.visit(container, null, null);

        Assert.assertTrue(visited.contains(container.getDefaultCmsComponent()));
    }
}
