/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.personalizationcms.rendering.populators;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cmsfacades.data.PageContentSlotData;
import de.hybris.platform.personalizationcms.data.CxCmsActionResult;
import de.hybris.platform.personalizationservices.action.CxActionResultService;
import de.hybris.platform.personalizationservices.data.CxAbstractActionResult;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class CxContentSlotModelToDataRenderingPopulatorIntegrationTest extends ServicelayerTest
{
	private static final String SINGLE_SLOT = "contentSlot1";
	private static final String PLURAL_SLOT = "contentSlot4";
	private static final String DEFAULT_COMPONENT = "cxcomponent6";
	private static final String PERSONALIZED_COMPONENT = "cxcomponent1";


	@Resource(name = "cxContentSlotModelToDataRenderingPopulator")
	private CxContentSlotModelToDataRenderingPopulator populator;

	@Resource
	private FlexibleSearchService flexibleSearchService;

	@Resource
	private CxActionResultService cxActionResultService;

	@Resource
	private UserService userService;

	private PageContentSlotData data;

	@Before
	public void setUp() throws Exception
	{
		createCoreData();
		createDefaultCatalog();
		importCsv("/personalizationcms/test/testdata_personalizationcms.impex", "utf-8");
		data = new PageContentSlotData();
	}

	@Test
	public void testNotPersonalizedContainer()
	{
		//given
		final ContentSlotModel slot = getSlot(SINGLE_SLOT);

		//when
		populator.populateComponents(slot, data);

		//then
		assertComponentsWerePopulated(DEFAULT_COMPONENT);
	}

	@Test
	public void testPersonalizedContainer()
	{
		//given
		final ContentSlotModel slot = getSlot(SINGLE_SLOT);
		personalizeContainer("container1", slot);

		//when
		populator.populateComponents(slot, data);

		//then
		assertComponentsWerePopulated(PERSONALIZED_COMPONENT);
	}

	@Test
	public void testPersonalizedContainers()
	{
		//given
		final ContentSlotModel slot = getSlot(PLURAL_SLOT);
		personalizeContainer("container4", slot);

		//when
		populator.populateComponents(slot, data);

		//then
		assertComponentsWerePopulated(DEFAULT_COMPONENT, PERSONALIZED_COMPONENT);
	}

	private ContentSlotModel getSlot(final String id)
	{
		final ContentSlotModel example = new ContentSlotModel();
		example.setUid(id);
		return flexibleSearchService.getModelByExample(example);
	}

	private void personalizeContainer(final String container, final ContentSlotModel slot)
	{
		final CxCmsActionResult result = new CxCmsActionResult();
		result.setContainerId(container);
		result.setComponentId(PERSONALIZED_COMPONENT);

		final List<CxAbstractActionResult> results = new ArrayList<>();
		results.add(result);

		cxActionResultService.setActionResultsInSession(userService.getCurrentUser(), slot.getCatalogVersion(), results);
	}

	private void assertComponentsWerePopulated(final String... components)
	{
		Assert.assertNotNull(data.getComponents());

		final List<String> uidList = data.getComponents().stream().map(c -> c.getUid()).collect(Collectors.toList());
		final List<String> expectedUidList = List.of(components);
		Assert.assertEquals(expectedUidList, uidList);
	}

}
