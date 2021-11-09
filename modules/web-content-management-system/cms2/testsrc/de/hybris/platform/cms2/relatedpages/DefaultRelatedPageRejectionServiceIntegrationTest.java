/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cms2.relatedpages;

import static de.hybris.platform.catalog.model.CatalogModel.ACTIVECATALOGVERSION;
import static de.hybris.platform.cms2.enums.CmsApprovalStatus.APPROVED;
import static de.hybris.platform.cms2.enums.CmsApprovalStatus.CHECK;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.constants.CatalogConstants;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.cloning.strategy.impl.ContentSlotCloningStrategy;
import de.hybris.platform.cms2.constants.Cms2Constants;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.model.restrictions.CMSTimeRestrictionModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminComponentService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminContentSlotService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminPageService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminRestrictionService;
import de.hybris.platform.cms2.workflow.service.CMSWorkflowService;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.workflow.model.WorkflowModel;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class DefaultRelatedPageRejectionServiceIntegrationTest extends ServicelayerTest
{
	private static final String CATALOG_ID = "cms_Catalog";
	private static final String CATALOG_VERSION = "CatalogVersion";
	private static final String HOMEPAGE_UID = "homepage";
	private static final String FAQ_UID = "faq";
	private static final String PAGE_IN_WORKFLOW_UID = "pageInWorkflow";
	private static final String FIRST_HOMEPAGE_CONTENT_SLOT_UID = "FirstHomePageBodySlot";
	private static final String SECOND_HOMEPAGE_CONTENT_SLOT_UID = "SecondHomePageBodySlot";
	private static final String FAQ_CONTENT_SLOT_UID = "FaqPageBodySlot";
	private static final String PAGE_IN_WORKFLOW_CONTENT_SLOT_UID = "PageInWorkflowBodySlot";
	private static final String COMPONENT_UID = "cmsLinkComponent";
	private static final String WORKFLOW_CODE = "workflow";
	private static final String TIME_RESTRICTION_UID = "timeRestriction";
	private static final String SHARED_CONTENT_SLOT_UID = "SharedSlotUid";

	private static final String CLONED_CONTENT_SLOT_UID = "ClonedSharedSlotUid";

	private static final String FAKE_DATA = "FakeData";

	@Resource
	private UserService userService;
	@Resource
	private CatalogVersionService catalogVersionService;
	@Resource
	private CMSAdminPageService cmsAdminPageService;
	@Resource
	private CMSAdminContentSlotService cmsAdminContentSlotService;
	@Resource
	private CMSAdminComponentService cmsAdminComponentService;
	@Resource
	private ModelService modelService;
	@Resource(name = "cmsContentSlotCloningStrategy")
	private ContentSlotCloningStrategy contentSlotCloningStrategy;
	@Resource
	private CMSWorkflowService cmsWorkflowService;
	@Resource
	private CMSAdminRestrictionService cmsAdminRestrictionService;
	@Resource
	private SessionService sessionService;

	private CatalogVersionModel catalogVersion;
	private UserModel user;
	private AbstractPageModel homepage;
	private AbstractPageModel faqpage;
	private AbstractPageModel pageInWorkflow;
	private ContentSlotModel firstHomepageContentSlot;
	private ContentSlotModel secondHomepageContentSlot;
	private ContentSlotModel faqContentSlot;
	private ContentSlotModel pageInWorkflowContentSlot;
	private ContentSlotModel sharedContentSlotForTemplate;

	private CMSLinkComponentModel componentModel;
	private WorkflowModel workflow;
	private CMSTimeRestrictionModel restrictionModel;

	@Before
	public void setUp() throws Exception
	{
		importCsv("/test/cmsTypePermissionTestData.impex", "UTF-8");
		importCsv("/test/cmsUnApprovePageTestData.impex", "UTF-8");

		catalogVersion = catalogVersionService.getCatalogVersion(CATALOG_ID, CATALOG_VERSION);
		sessionService.setAttribute(ACTIVECATALOGVERSION, catalogVersion.getPk());
		sessionService.setAttribute(CatalogConstants.SESSION_CATALOG_VERSIONS, Arrays.asList(catalogVersion));
		user = userService.getUserForUID("cmsmanager");
		userService.setCurrentUser(user);

		homepage = cmsAdminPageService.getPageForId(HOMEPAGE_UID, Arrays.asList(catalogVersion));
		faqpage = cmsAdminPageService.getPageForId(FAQ_UID, Arrays.asList(catalogVersion));
		pageInWorkflow = cmsAdminPageService.getPageForId(PAGE_IN_WORKFLOW_UID, Arrays.asList(catalogVersion));

		firstHomepageContentSlot = cmsAdminContentSlotService.getContentSlotForIdAndCatalogVersions(FIRST_HOMEPAGE_CONTENT_SLOT_UID,
				Arrays.asList(catalogVersion));
		secondHomepageContentSlot = cmsAdminContentSlotService
				.getContentSlotForIdAndCatalogVersions(SECOND_HOMEPAGE_CONTENT_SLOT_UID, Arrays.asList(catalogVersion));
		faqContentSlot = cmsAdminContentSlotService.getContentSlotForIdAndCatalogVersions(FAQ_CONTENT_SLOT_UID,
				Arrays.asList(catalogVersion));
		pageInWorkflowContentSlot = cmsAdminContentSlotService
				.getContentSlotForIdAndCatalogVersions(PAGE_IN_WORKFLOW_CONTENT_SLOT_UID, Arrays.asList(catalogVersion));
		sharedContentSlotForTemplate = cmsAdminContentSlotService.getContentSlotForIdAndCatalogVersions(SHARED_CONTENT_SLOT_UID,
				Arrays.asList(catalogVersion));

		componentModel = (CMSLinkComponentModel) cmsAdminComponentService.getCMSComponentForIdAndCatalogVersions(COMPONENT_UID,
				Arrays.asList(catalogVersion));
		restrictionModel = (CMSTimeRestrictionModel) cmsAdminRestrictionService.getRestriction(TIME_RESTRICTION_UID);


		workflow = cmsWorkflowService.getWorkflowForCode(WORKFLOW_CODE);
	}

	@Test
	public void givenComponentInTwoApprovedPagesThatAreNotInWorkflowWhenComponentIsChangedThenBothPagesAreRejected()
	{
		// GIVEN
		addComponentToSlot(faqContentSlot, 0);
		addComponentToSlot(firstHomepageContentSlot, 1);
		approvePages(faqpage, homepage);

		// WHEN
		changeComponent();

		// THEN
		assertThat(homepage.getApprovalStatus(), is(CHECK));
		assertThat(faqpage.getApprovalStatus(), is(CHECK));
	}

	@Test
	public void givenComponentInTwoApprovedPagesAndSecondPageIsInWorkflowWhenComponentIsChangedThenFirstPageIsRejected()
	{
		// GIVEN
		addComponentToSlot(faqContentSlot, 1);
		addComponentToSlot(pageInWorkflowContentSlot, 0);
		approvePages(pageInWorkflow, faqpage);

		// WHEN
		changeComponent();

		// THEN
		assertThat(faqpage.getApprovalStatus(), is(CHECK));
		assertThat(pageInWorkflow.getApprovalStatus(), is(APPROVED));
	}

	@Test
	public void givenComponentInTwoApprovedPagesWhenComponentIsMovedOnOnePageThenAnotherPageIsStillApproved()
	{
		// GIVEN
		addComponentToSlot(faqContentSlot, 0);
		addComponentToSlot(firstHomepageContentSlot, 1);
		approvePages(faqpage, homepage);

		// WHEN
		moveComponent(firstHomepageContentSlot, secondHomepageContentSlot);

		// THEN
		assertThat(homepage.getApprovalStatus(), is(CHECK));
		assertThat(faqpage.getApprovalStatus(), is(APPROVED));
	}

	@Test
	public void givenComponentInTwoApprovedPagesWhenComponentIsRemovedFromOnePageThenAnotherPageIsStillApproved()
	{
		// GIVEN
		addComponentToSlot(faqContentSlot, 0);
		addComponentToSlot(firstHomepageContentSlot, 1);
		approvePages(faqpage, homepage);

		// WHEN
		removeComponent(firstHomepageContentSlot);

		// THEN
		assertThat(homepage.getApprovalStatus(), is(CHECK));
		assertThat(faqpage.getApprovalStatus(), is(APPROVED));
	}

	@Test
	public void givenComponentInFirstApprovedPageAndSecondPageIsAlsoApprovedWhenComponentIsAddedToSecondPageThenFirstPageIsStillApproved()
	{
		// GIVEN
		addComponentToSlot(faqContentSlot, 0);
		approvePages(faqpage, homepage);

		// WHEN
		addComponentToSlot(firstHomepageContentSlot, 0);

		// THEN
		assertThat(homepage.getApprovalStatus(), is(CHECK));
		assertThat(faqpage.getApprovalStatus(), is(APPROVED));
	}

	@Test
	public void givenComponentOnApprovedPageWhenComponentIsChangedThenPageIsRejected()
	{
		// GIVEN
		addComponentToSlot(firstHomepageContentSlot, 0);
		approvePages(homepage);

		// WHEN
		changeComponent();

		// THEN
		assertThat(homepage.getApprovalStatus(), is(CHECK));
	}

	@Test
	public void givenApprovedPageWhenUpdatePageMetaDataThenPageIsStillApproved()
	{
		// when the page metadata is changed, the facade layer is responsible of rejecting the page, not the interceptor
		// GIVEN
		approvePages(homepage);

		// WHEN
		changePageMetadata(homepage);

		// THEN
		assertThat(homepage.getApprovalStatus(), is(APPROVED));
	}

	@Test
	public void givenTwoApprovedPagesWhenSharedSlotOnFirstPageIsUnsharedThenFirstPageIsUnApprovedAndSecondPageIsStillApproved()
			throws CMSItemNotFoundException
	{
		// GIVEN
		addComponentToSlot(faqContentSlot, 0);
		addComponentToSlot(firstHomepageContentSlot, 1);
		approvePages(faqpage, homepage);

		// WHEN
		unshareSlot(homepage);

		// THEN
		assertThat(homepage.getApprovalStatus(), is(CHECK));
		assertThat(faqpage.getApprovalStatus(), is(APPROVED));
	}

	@Test
	public void givenTwoApprovedPagesWithSameRestrictionWhenRestrictionMetadataIsChangedThenAllPagesThatAreNotInWorkflowAreRejected()
	{
		// GIVEN
		addRestrictionToPage(homepage);
		addRestrictionToPage(faqpage);
		approvePages(homepage, faqpage);

		// WHEN
		changeRestrictionMetadata();

		// THEN
		assertThat(homepage.getApprovalStatus(), is(CHECK));
		assertThat(faqpage.getApprovalStatus(), is(CHECK));
	}

	@Test
	public void givenParentComponentWithChildNestedComponentWhenChildComponentIsChangedThenPageIsStillApproved()
	{
		// GIVEN
		addNestedComponent();
		approvePages(homepage);

		// WHEN
		changeNestedComponent();

		// THEN
		assertThat(homepage.getApprovalStatus(), is(APPROVED));
	}

	protected void approvePages(final AbstractPageModel... pages)
	{
		for (final AbstractPageModel page : pages)
		{
			page.setApprovalStatus(APPROVED);
		}
		modelService.saveAll(pages);
	}

	protected void moveComponent(final ContentSlotModel fromSlot, final ContentSlotModel toSlot)
	{
		addComponentToSlot(toSlot, 0);
		removeComponent(fromSlot);
	}

	protected void removeComponent(final ContentSlotModel fromSlot)
	{
		cmsAdminComponentService.removeCMSComponentFromContentSlot(componentModel, fromSlot);
	}

	protected void addComponentToSlot(final ContentSlotModel slot, final int index)
	{
		cmsAdminContentSlotService.addCMSComponentToContentSlot(componentModel, slot, index);
	}

	protected void changeComponent()
	{
		componentModel.setUrl(FAKE_DATA);
		modelService.save(componentModel);
	}

	protected void changeNestedComponent()
	{
		componentModel.getContentPage().setTitle(FAKE_DATA);
		modelService.save(componentModel.getContentPage());
	}

	protected void addNestedComponent()
	{
		componentModel.setContentPage((ContentPageModel) faqpage);
		modelService.save(componentModel);
	}

	protected void changePageMetadata(final AbstractPageModel page)
	{
		page.setTitle(FAKE_DATA);
		modelService.save(homepage);
	}

	protected void changeRestrictionMetadata()
	{
		restrictionModel.setName(FAKE_DATA);
		modelService.save(restrictionModel);
	}

	protected void addRestrictionToPage(final AbstractPageModel page)
	{
		page.setRestrictions(Arrays.asList(restrictionModel));
		modelService.save(page);
	}

	protected void unshareSlot(final AbstractPageModel sourcePage) throws CMSItemNotFoundException
	{
		final ContentSlotModel newContentSlot = modelService.create(ContentSlotModel.class);
		newContentSlot.setCatalogVersion(catalogVersion);
		newContentSlot.setActive(true);
		newContentSlot.setUid(CLONED_CONTENT_SLOT_UID);
		newContentSlot.setName(sharedContentSlotForTemplate.getName() + "_Cloned");

		final Map<String, Object> context = new HashMap<>();
		context.put(Cms2Constants.PAGE_CONTEXT_KEY, sourcePage);
		context.put(Cms2Constants.SHOULD_CLONE_COMPONENTS_CONTEXT_KEY, true);

		contentSlotCloningStrategy.clone(sharedContentSlotForTemplate, Optional.of(newContentSlot), Optional.of(context));

		modelService.saveAll();
	}
}

