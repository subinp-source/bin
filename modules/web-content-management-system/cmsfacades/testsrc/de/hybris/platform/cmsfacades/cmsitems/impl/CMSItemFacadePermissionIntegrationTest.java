/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.cmsitems.impl;

import static de.hybris.platform.cms2.model.contents.CMSItemModel.NAME;
import static de.hybris.platform.cmsfacades.util.models.CatalogVersionModelMother.CatalogVersion.STAGED;
import static de.hybris.platform.cmsfacades.util.models.ContentCatalogModelMother.CatalogTemplate.ID_APPLE;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;
import de.hybris.platform.cms2.permissions.impl.DefaultPermissionEnablerService;
import de.hybris.platform.cmsfacades.cmsitems.CMSItemFacade;
import de.hybris.platform.cmsfacades.uniqueidentifier.EncodedItemComposedKey;
import de.hybris.platform.cmsfacades.util.BaseIntegrationTest;
import de.hybris.platform.cmsfacades.util.models.CatalogVersionModelMother;
import de.hybris.platform.cmsfacades.util.models.ContentSlotModelMother;
import de.hybris.platform.cmsfacades.util.models.LinkComponentModelMother;
import de.hybris.platform.cmsfacades.util.models.ParagraphComponentModelMother;
import de.hybris.platform.cmsfacades.util.models.SiteModelMother;

import java.util.Arrays;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


/**
 * Test covering CMSItem API interactions with different types of users in order to replicate the different permissions
 * behaviors.
 */
@IntegrationTest
public class CMSItemFacadePermissionIntegrationTest extends BaseIntegrationTest
{
	@Resource
	private SiteModelMother siteModelMother;
	@Resource
	private CatalogVersionModelMother catalogVersionModelMother;
	@Resource
	private ContentSlotModelMother contentSlotModelMother;

	@Resource
	private CMSItemFacade cmsItemFacade;
	@Resource
	private DefaultPermissionEnablerService cmsPermissionEnablerService;

	@Before
	public void setUp() throws Exception
	{
		setCurrentUserCmsEditor();
		siteModelMother.createElectronicsWithAppleCatalog();
		final CatalogVersionModel appleStaged = catalogVersionModelMother.createAppleStagedCatalogVersionModel();
		contentSlotModelMother.createHeaderSlotWithParagraphAndLink(appleStaged);

		// Enable type and attribute checking
		cmsPermissionEnablerService.setCheckingAllAttributes(true);
		cmsPermissionEnablerService.setCheckingAllTypes(true);
		cmsPermissionEnablerService.afterPropertiesSet();
	}

	@Test
	public void testAttributeWithNoPermissionIsNotVisibleToCmsEditor() throws CMSItemNotFoundException
	{
		final String linkUUID = getUuidForAppleStage(LinkComponentModelMother.UID_EXTERNAL_LINK);

		final Map<String, Object> linkData = cmsItemFacade.findCMSItems(Arrays.asList(linkUUID)).get(0);

		assertThat(linkData, not(hasKey(CMSLinkComponentModel.NAVIGATIONNODES)));
	}

	@Test
	public void testReadOnlyAttributeIsNotEditableByCmsEditor() throws CMSItemNotFoundException
	{
		final String paragraphUUID = getUuidForAppleStage(ParagraphComponentModelMother.UID_HEADER);
		final Map<String, Object> paragraphData = cmsItemFacade.findCMSItems(Arrays.asList(paragraphUUID)).get(0);

		assertThat(paragraphData, hasKey(NAME));
		final String originalName = paragraphData.get(NAME).toString();
		paragraphData.put(NAME, "NEW NAME");
		final Map<String, Object> updatedData = cmsItemFacade.updateItem(paragraphUUID, paragraphData);

		assertThat(updatedData.get(NAME), equalTo(originalName));
	}

	protected String getUuidForAppleStage(final String uid)
	{
		final EncodedItemComposedKey itemComposedKey = new EncodedItemComposedKey();
		itemComposedKey.setCatalogId(ID_APPLE.name());
		itemComposedKey.setCatalogVersion(STAGED.getVersion());
		itemComposedKey.setItemId(uid);

		return itemComposedKey.toEncoded();
	}
}
