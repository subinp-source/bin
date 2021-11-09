/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.synchronization.itemvisitors.impl;

import static com.google.common.collect.Lists.newLinkedList;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.navigation.CMSNavigationNodeModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.servicelayer.services.AttributeDescriptorModelHelperService;
import de.hybris.platform.core.model.ItemModel;

import java.util.List;

import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultAbstractCMSComponentModelVisitorTest
{
	@Mock
	private AttributeDescriptorModelHelperService attributeDescriptorModelHelperService;
	@Mock
	private TypeService typeService;
	@Mock
	private ModelService modelService;

	private List<AttributeDescriptorModel> attributeDescriptors;

	@Mock
	private AttributeDescriptorModel slotAttributeDescriptorModel;
	@Mock
	private AttributeDescriptorModel cmsItemAttributeDescriptorModel;
	@Mock
	private AttributeDescriptorModel cmsItemCollectionAttributeDescriptorModel;
	@Mock
	private AttributeDescriptorModel navigationNodeAttributeDescriptorModel;


	@Mock
	private ComposedTypeModel composedTypeModel;
	@Mock
	private AbstractCMSComponentModel component;
	@Mock
	private CMSItemModel cmsItemValue;
	@Mock
	private ContentSlotModel contentSlot;
	@Mock
	private AbstractPageModel otherValue;
	@Mock
	private CMSItemModel cmsItem2Value;
	@Mock
	private CMSItemModel cmsItem3Value;

	private List<CMSItemModel> cmsItemCollectionValue;

	@InjectMocks
	private DefaultAbstractCMSComponentModelVisitor visitor;

	private String slotQualifier = "slotQualifier";
	private String cmsItemQualifier = "cmsItemQualifier";
	private String navigationNodeQualifier = "navigationNodeQualifier";
	private String cmsItemCollectionQualifier = "cmsItemCollectionQualifier";

	@Before
	public void setUp()
	{
		when(cmsItemAttributeDescriptorModel.getQualifier()).thenReturn(cmsItemQualifier);
		when(navigationNodeAttributeDescriptorModel.getQualifier()).thenReturn(navigationNodeQualifier);
		when(cmsItemCollectionAttributeDescriptorModel.getQualifier()).thenReturn(cmsItemCollectionQualifier);

		attributeDescriptors = newLinkedList();
		attributeDescriptors.add(cmsItemAttributeDescriptorModel);
		attributeDescriptors.add(navigationNodeAttributeDescriptorModel);
		attributeDescriptors.add(cmsItemCollectionAttributeDescriptorModel);

		doReturn(ContentSlotModel.class).when(attributeDescriptorModelHelperService).getAttributeClass(slotAttributeDescriptorModel);
		doReturn(CMSItemModel.class).when(attributeDescriptorModelHelperService).getAttributeClass(cmsItemAttributeDescriptorModel);
		doReturn(CMSNavigationNodeModel.class).when(attributeDescriptorModelHelperService).getAttributeClass(navigationNodeAttributeDescriptorModel);
		doReturn(CMSItemModel.class).when(attributeDescriptorModelHelperService).getAttributeClass(cmsItemCollectionAttributeDescriptorModel);

		cmsItemCollectionValue = newLinkedList();
		cmsItemCollectionValue.add(cmsItem2Value);
		cmsItemCollectionValue.add(cmsItem3Value);

		when(modelService.getAttributeValue(component, slotQualifier)).thenReturn(contentSlot);
		when(modelService.getAttributeValue(component, cmsItemQualifier)).thenReturn(cmsItemValue);
		when(modelService.getAttributeValue(component, navigationNodeQualifier)).thenReturn(otherValue);
		when(modelService.getAttributeValue(component, cmsItemCollectionQualifier)).thenReturn(cmsItemCollectionValue);


		when(typeService.getComposedTypeForClass(component.getClass())).thenReturn(composedTypeModel);

		when(composedTypeModel.getDeclaredattributedescriptors()).thenReturn(attributeDescriptors);
	}

	@Test
	public void willCollectAllCMSItemChildrenExceptSlotsAndNavigationNodes()
	{
		
		List<ItemModel> visit = visitor.visit(component, null, null);
		
		assertThat(visit, containsInAnyOrder(cmsItemValue, cmsItem2Value, cmsItem3Value));
		
		
	}

}
