/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.synchronization.validator;

import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.UNAPPROVED_SYNCHRONIZATION_REQUEST;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.enums.CmsApprovalStatus;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.relateditems.RelatedItemsService;
import de.hybris.platform.cmsfacades.data.ItemData;
import de.hybris.platform.cmsfacades.data.ItemSynchronizationData;
import de.hybris.platform.cmsfacades.data.SynchronizationData;
import de.hybris.platform.cmsfacades.uniqueidentifier.UniqueItemIdentifierService;
import de.hybris.platform.core.model.ItemModel;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.Errors;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class ItemSynchronizationValidatorTest
{
    private static final String ITEMS_ATTR = "items";
    private final String KEY_SEPARATOR = "_";

    private final String NON_CMS_ITEM_ID = "Some Non-CMSItem ID";
    private final String NON_CMS_ITEM_TYPE = "Some Non-CMSItem Type";
    private final String CMS_ITEM_ID = "Some CMSItem ID";
    private final String CMS_ITEM_TYPE = "Some CMSItem Type";
    private final String SLOT_ITEM_ID = "Some Slot ID";
    private final String SLOT_ITEM_TYPE = "Some Slot Type";
    private final String PAGE_ITEM_ID = "Some Page Item ID";
    private final String PAGE_ITEM_TYPE = "Some Page Type";

    private Map<String, Optional<ItemModel>> modelMap;

    @Mock
    private SynchronizationData synchronizationData;

    @Mock
    private ItemSynchronizationData nonCmsItemSynchronizationData;

    @Mock
    private ItemSynchronizationData cmsItemSynchronizationData;

    @Mock
    private ItemSynchronizationData slotSynchronizationData;

    @Mock
    private ItemSynchronizationData pageSynchronizationData;

    @Mock
    private ItemModel nonCmsItemModel;

    @Mock
    private CMSItemModel cmsItemModel;

    @Mock
    private ContentSlotModel slotModel;

    @Mock
    private AbstractPageModel pageModel;

    @Mock
    private AbstractPageModel relatedPageModel;

    @Mock
    private Errors errors;

    @Mock
    private UniqueItemIdentifierService uniqueItemIdentifierService;

    @Mock
    private RelatedItemsService relatedItemsService;

    @Mock
    private Predicate<ItemModel> cmsContentSlotPredicate;

    @Mock
    private Predicate<ItemModel> pageTypePredicate;

    @InjectMocks
    private ItemSynchronizationValidator itemSynchronizationValidator;

    @Before
    public void setUp()
    {
        modelMap = new HashMap<>();
        when(uniqueItemIdentifierService.getItemModel(any())).thenAnswer((invocationOnMock) -> {
            final ItemData itemData = invocationOnMock.getArgumentAt(0, ItemData.class);
            final String key = itemData.getItemId() + KEY_SEPARATOR + itemData.getItemType();

            return modelMap.get(key);
        });

        setUpItemSynchronizationData(nonCmsItemSynchronizationData, NON_CMS_ITEM_ID, NON_CMS_ITEM_TYPE, nonCmsItemModel);
        setUpItemSynchronizationData(cmsItemSynchronizationData, CMS_ITEM_ID, CMS_ITEM_TYPE, cmsItemModel);
        setUpItemSynchronizationData(slotSynchronizationData, SLOT_ITEM_ID, SLOT_ITEM_TYPE, slotModel);
        setUpItemSynchronizationData(pageSynchronizationData, PAGE_ITEM_ID, PAGE_ITEM_TYPE, pageModel);

        when(pageModel.getApprovalStatus()).thenReturn(CmsApprovalStatus.APPROVED);

        when(cmsContentSlotPredicate.test(any())).then(answer -> {
            final CMSItemModel itemModel = answer.getArgumentAt(0, CMSItemModel.class);
            return itemModel instanceof ContentSlotModel;
        });

        when(pageTypePredicate.test(any())).then(answer -> {
            final CMSItemModel itemModel = answer.getArgumentAt(0, CMSItemModel.class);
            return itemModel instanceof AbstractPageModel;
        });
    }

    @Test
    public void givenNotSupportedItem_WhenSupportsIsCalled_ThenItReturnsFalse()
    {
        // GIVEN
        final String unsupportedType = "some unsupported type";

        // WHEN
        final boolean result = itemSynchronizationValidator.supports(unsupportedType.getClass());

        // THEN
        assertFalse("Should not support validation of wrong class", result);
    }

    @Test
    public void givenNonCmsItem_WhenValidated_ThenItShouldNotReturnErrors()
    {
        // GIVEN
        when(synchronizationData.getItems()).thenReturn(Collections.singletonList(nonCmsItemSynchronizationData));

        // WHEN
        itemSynchronizationValidator.validate(synchronizationData, errors);

        // THEN
        assertHasNoErrors();
    }

    @Test
    public void givenItemIsAnApprovedPage_WhenValidated_ThenItShouldNotReturnErrors()
    {
        // GIVEN
        when(synchronizationData.getItems()).thenReturn(Collections.singletonList(pageSynchronizationData));

        // WHEN
        itemSynchronizationValidator.validate(synchronizationData, errors);

        // THEN
        assertHasNoErrors();
    }

    @Test
    public void givenItemIsAnUnapprovedPage_WhenValidated_ThenItShouldReturnAnUnapprovedError()
    {
        // GIVEN
        when(pageModel.getApprovalStatus()).thenReturn(CmsApprovalStatus.UNAPPROVED);
        when(synchronizationData.getItems()).thenReturn(Collections.singletonList(pageSynchronizationData));

        // WHEN
        itemSynchronizationValidator.validate(synchronizationData, errors);

        // THEN
        assertHasSynchronizationUnapprovedError();
    }

    @Test
    public void givenItemIsASharedSlot_WhenValidated_ThenItShouldNotReturnErrors()
    {
        // GIVEN
        when(relatedItemsService.getRelatedItems(slotModel)).thenReturn(Collections.emptyList());
        when(synchronizationData.getItems()).thenReturn(Collections.singletonList(slotSynchronizationData));

        // WHEN
        itemSynchronizationValidator.validate(synchronizationData, errors);

        // THEN
        assertHasNoErrors();
    }

    @Test
    public void givenItemIsNotASharedSlotAndRelatedPageIsApproved_WhenValidated_ThenItShouldNotReturnErrors()
    {
        // GIVEN
        when(relatedPageModel.getApprovalStatus()).thenReturn(CmsApprovalStatus.APPROVED);
        when(relatedItemsService.getRelatedItems(slotModel)).thenReturn(Collections.singletonList(relatedPageModel));
        when(synchronizationData.getItems()).thenReturn(Collections.singletonList(slotSynchronizationData));

        // WHEN
        itemSynchronizationValidator.validate(synchronizationData, errors);

        // THEN
        assertHasNoErrors();
    }

    @Test
    public void givenItemIsNotASharedSlotAndRelatedPageIsUnapproved_WhenValidated_ThenItShouldReturnAnUnapprovedError()
    {
        // GIVEN
        when(relatedPageModel.getApprovalStatus()).thenReturn(CmsApprovalStatus.UNAPPROVED);
        when(relatedItemsService.getRelatedItems(slotModel)).thenReturn(Collections.singletonList(relatedPageModel));
        when(synchronizationData.getItems()).thenReturn(Collections.singletonList(slotSynchronizationData));

        // WHEN
        itemSynchronizationValidator.validate(synchronizationData, errors);

        // THEN
        assertHasSynchronizationUnapprovedError();
    }

    @Test
    public void givenItemIsNotUsedInAPageOrSharedSlot_WhenValidated_ThenItShouldReturnAnUnapprovedError()
    {
        // GIVEN
        when(relatedItemsService.getRelatedItems(cmsItemModel)).thenReturn(Collections.emptyList());
        when(synchronizationData.getItems()).thenReturn(Arrays.asList(pageSynchronizationData, cmsItemSynchronizationData));

        // WHEN
        itemSynchronizationValidator.validate(synchronizationData, errors);

        // THEN
        assertHasSynchronizationUnapprovedError();

    }

    @Test
    public void givenItemIsUsedInUnapprovedPage_WhenValidated_ThenItShouldReturnAnUnapprovedError()
    {
        // GIVEN
        when(relatedPageModel.getApprovalStatus()).thenReturn(CmsApprovalStatus.UNAPPROVED);
        when(relatedItemsService.getRelatedItems(cmsItemModel)).thenReturn(Collections.singletonList(relatedPageModel));
        when(synchronizationData.getItems()).thenReturn(Arrays.asList(pageSynchronizationData, cmsItemSynchronizationData));

        // WHEN
        itemSynchronizationValidator.validate(synchronizationData, errors);

        // THEN
        assertHasSynchronizationUnapprovedError();
    }

    @Test
    public void givenItemIsUsedInApprovedPage_WhenValidated_ThenItShouldNotReturnErrors()
    {
        // GIVEN
        when(relatedPageModel.getApprovalStatus()).thenReturn(CmsApprovalStatus.APPROVED);
        when(relatedItemsService.getRelatedItems(cmsItemModel)).thenReturn(Collections.singletonList(relatedPageModel));
        when(synchronizationData.getItems()).thenReturn(Arrays.asList(pageSynchronizationData, cmsItemSynchronizationData));

        // WHEN
        itemSynchronizationValidator.validate(synchronizationData, errors);

        // THEN
        assertHasNoErrors();
    }

    @Test
    public void givenItemIsUsedInSharedSlot_WhenValidated_ThenItShouldNotReturnErrors()
    {
        // GIVEN
        when(relatedItemsService.getRelatedItems(slotModel)).thenReturn(Collections.emptyList());
        when(relatedItemsService.getRelatedItems(cmsItemModel)).thenReturn(Collections.singletonList(slotModel));
        when(synchronizationData.getItems()).thenReturn(Arrays.asList(pageSynchronizationData, cmsItemSynchronizationData));

        // WHEN
        itemSynchronizationValidator.validate(synchronizationData, errors);

        // THEN
        assertHasNoErrors();
    }

    @Test
    public void givenItemIsUsedInUnapprovedPageAndInASharedSlot_WhenValidated_ThenItShouldReturnAnUnapprovedError()
    {
        // GIVEN
        when(relatedPageModel.getApprovalStatus()).thenReturn(CmsApprovalStatus.UNAPPROVED);
        when(relatedItemsService.getRelatedItems(slotModel)).thenReturn(Collections.emptyList());
        when(relatedItemsService.getRelatedItems(cmsItemModel)).thenReturn(Arrays.asList(relatedPageModel, slotModel));
        when(synchronizationData.getItems()).thenReturn(Arrays.asList(pageSynchronizationData, cmsItemSynchronizationData));

        // WHEN
        itemSynchronizationValidator.validate(synchronizationData, errors);

        // THEN
        assertHasSynchronizationUnapprovedError();
    }

    protected void setUpItemSynchronizationData(final ItemSynchronizationData itemData, final String itemId, final String itemType, final ItemModel objectToReturn)
    {
        when(itemData.getItemId()).thenReturn(itemId);
        when(itemData.getItemType()).thenReturn(itemType);

        final String key = itemId + KEY_SEPARATOR + itemType;
        modelMap.put(key, Optional.of(objectToReturn));
    }

    protected void assertHasNoErrors()
    {
        verify(errors, never()).rejectValue(any(), any());
    }

    protected void assertHasSynchronizationUnapprovedError()
    {
        verify(errors).rejectValue(ITEMS_ATTR, UNAPPROVED_SYNCHRONIZATION_REQUEST);
    }
}
