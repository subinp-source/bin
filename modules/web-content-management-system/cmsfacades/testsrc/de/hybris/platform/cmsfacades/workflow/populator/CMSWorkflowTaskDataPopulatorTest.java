/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.workflow.populator;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.workflow.service.CMSWorkflowParticipantService;
import de.hybris.platform.cmsfacades.data.CMSWorkflowActionData;
import de.hybris.platform.cmsfacades.data.CMSWorkflowTaskData;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.workflow.enums.WorkflowActionStatus;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowItemAttachmentModel;
import de.hybris.platform.workflow.model.WorkflowModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class CMSWorkflowTaskDataPopulatorTest
{
	private final String PAGE_UID = "page-uid";
	private final String PAGE_NAME = "page-name";
	private final String CATALOG_ID = "catalog-id";
	private final String CATALOG_NAME = "catalog-name";
	private final String CATALOG_VERSION = "catalog-version";

	@Mock
	private TypeService typeService;
	@Mock
	private UserService userService;
	@Mock
	private CMSWorkflowParticipantService cmsWorkflowParticipantService;
	@Mock
	private Converter<WorkflowActionModel, CMSWorkflowActionData> cmsWorkflowActionDataConverter;
	@Mock
	private WorkflowModel workflowModel;
	@Mock
	private List<CMSWorkflowTaskData> target;
	@Mock
	private WorkflowItemAttachmentModel attachmentModel;
	@Mock
	private ComposedTypeModel composedTypeModel;
	@Mock
	private CMSItemModel itemModel;
	@Mock
	private WorkflowActionModel workflowActionModel;
	@Mock
	private UserModel userModel;
	@Mock
	private CMSWorkflowActionData workflowActionData;
	@Mock
	private CatalogVersionModel catalogVersionModel;
	@Mock
	private CatalogModel catalogModel;

	@InjectMocks
	private CMSWorkflowTaskDataPopulator cmsWorkflowTaskDataPopulator;

	@Before
	public void setUp()
	{

		target = new ArrayList<>();

		when(attachmentModel.getTypeOfItem()).thenReturn(composedTypeModel);
		when(attachmentModel.getItem()).thenReturn(itemModel);
		when(itemModel.getUid()).thenReturn(PAGE_UID);
		when(itemModel.getName()).thenReturn(PAGE_NAME);
		when(itemModel.getCatalogVersion()).thenReturn(catalogVersionModel);

		when(catalogVersionModel.getVersion()).thenReturn(CATALOG_VERSION);
		when(catalogVersionModel.getCatalog()).thenReturn(catalogModel);
		when(catalogModel.getId()).thenReturn(CATALOG_ID);
		when(catalogModel.getName()).thenReturn(CATALOG_NAME);

		when(workflowActionModel.getWorkflow()).thenReturn(workflowModel);
		when(workflowActionModel.getPrincipalAssigned()).thenReturn(userModel);
		when(workflowActionModel.getStatus()).thenReturn(WorkflowActionStatus.IN_PROGRESS);

		when(workflowModel.getAttachments()).thenReturn(Collections.singletonList(attachmentModel));

		when(typeService.getComposedTypeForClass(AbstractPageModel.class)).thenReturn(composedTypeModel);
		when(typeService.isAssignableFrom(composedTypeModel, composedTypeModel)).thenReturn(true);

		when(userService.getCurrentUser()).thenReturn(userModel);

		when(cmsWorkflowParticipantService.getRelatedPrincipals(userModel)).thenReturn(Collections.singletonList(userModel));

		when(cmsWorkflowActionDataConverter.convert(workflowActionModel)).thenReturn(workflowActionData);

		when(workflowActionData.getStartedAgoInMillis()).thenReturn(20L);
	}

	@Test
	public void shouldRetrieveWorkflowTasks()
	{
		// WHEN
		cmsWorkflowTaskDataPopulator.populate(workflowActionModel, target);

		// THEN
		assertThat(target.get(0).getAction(), is(workflowActionData));

		assertThat(target.get(0).getAttachments(),
				hasItems(allOf(hasProperty("pageUid", equalTo(PAGE_UID))), allOf(hasProperty("pageName", equalTo(PAGE_NAME))),
						allOf(hasProperty("catalogId", equalTo(CATALOG_ID))), allOf(hasProperty("catalogName", equalTo(CATALOG_NAME))),
						allOf(hasProperty("catalogVersion", equalTo(CATALOG_VERSION)))));
	}
}
