/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.synchronization.validator;

import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.UNAPPROVED_SYNCHRONIZATION_REQUEST;
import static java.util.function.Predicate.not;

import de.hybris.platform.cms2.enums.CmsApprovalStatus;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.relateditems.RelatedItemsService;
import de.hybris.platform.cmsfacades.data.ItemData;
import de.hybris.platform.cmsfacades.data.ItemSynchronizationData;
import de.hybris.platform.cmsfacades.data.SynchronizationData;
import de.hybris.platform.cmsfacades.uniqueidentifier.UniqueItemIdentifierService;
import de.hybris.platform.core.model.ItemModel;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Validates that the items in {@link SynchronizationData} can be synchronized.
 *
 * <p>
 *     Rules to determine if an item can be synchronized:</br>
 *     <ul>
 *         <li>If the CmsItem is a page then it must be approved.</li>
 *         <li>If the CmsItem is a slot it must be a shared slot or used within a page that is approved.</li>
 *         <li>Otherwise, the CmsItem must only be used in shared slots or within a page that is approved.</li>
 *     </ul>
 * </p>
 */
public class ItemSynchronizationValidator implements Validator
{
    private static final String ITEMS_ATTR = "items";

    private UniqueItemIdentifierService uniqueItemIdentifierService;
    private RelatedItemsService relatedItemsService;
    private Predicate<ItemModel> cmsContentSlotPredicate;
    private Predicate<ItemModel> pageTypePredicate;

    @Override
    public boolean supports(final Class<?> clazz)
    {
        return clazz.isAssignableFrom(SynchronizationData.class);
    }

    @Override
    public void validate(final Object objToValidate, final Errors errors)
    {
        final SynchronizationData data = (SynchronizationData) objToValidate;

        final boolean isAnyItemUnapproved = data.getItems().stream()
                .map(this::getItem)
                .filter(item -> item instanceof CMSItemModel)
                .anyMatch(not(this::isCmsItemApproved));

        if (isAnyItemUnapproved)
        {
            errors.rejectValue(ITEMS_ATTR, UNAPPROVED_SYNCHRONIZATION_REQUEST);
        }
    }

    /**
     * This method is called to determine if the given {@link CMSItemModel} is approved to be synchronized.
     *
     * @param itemModel
     *      The item to check if it has been approved to be synchronized.
     * @return boolean True if the item is approved to be synchronized. False, otherwise.
     */
    protected boolean isCmsItemApproved(final ItemModel itemModel)
    {
        final CMSItemModel cmsItem = (CMSItemModel) itemModel;

        if(isPage(cmsItem))
        {
            return isPageApproved(cmsItem);
        }
        else if(isSlot(cmsItem))
        {
            return isSlotApproved(cmsItem);
        }

        final List<CMSItemModel> relatedItems = getRelatedItemsService().getRelatedItems(cmsItem);
        return isAnyRelatedPageApproved(relatedItems) || (hasNoRelatedPages(relatedItems) && areAllRelatedSlotsApproved(relatedItems));
    }

    /**
     * This method is used to determine if the given {@link CMSItemModel} is a page (is an instance of {@link AbstractPageModel} or one of its
     * subclasses).
     *
     * @param itemModel
     *      The item to check if it is a page
     * @return boolean True if the item is a page. False, otherwise.
     */
    protected boolean isPage(final CMSItemModel itemModel)
    {
        return getPageTypePredicate().test(itemModel);
    }

    /**
     * This method is used to determine if the given {@link CMSItemModel} is a slot.
     *
     * @param itemModel
     *      The item to check if it is a slot
     * @return boolean True if the item is a slot. False, otherwise.
     */
    protected boolean isSlot(final CMSItemModel itemModel)
    {
        return getCmsContentSlotPredicate().test(itemModel);
    }

    /**
     * This method is used to determine if a given page has been approved to be synchronized.
     *
     * @param itemModel
     *      The page to check if it has been approved.
     * @return boolean True if the page has been approved. False, otherwise.
     */
    protected boolean isPageApproved(final CMSItemModel itemModel)
    {
        final AbstractPageModel pageModel = (AbstractPageModel) itemModel;
        return pageModel.getApprovalStatus() == CmsApprovalStatus.APPROVED;
    }

    /**
     * This method is used to determine if a given slot can be synchronized. This can only happen if
     * the slot is a shared slot or if it's related to at least one page that is approved to be synchronized.
     *
     * @param cmsItem
     *      The slot to check if it can be synchronized.
     * @return boolean True if the slot can be synchronized. False, otherwise.
     */
    protected boolean isSlotApproved(final CMSItemModel cmsItem)
    {
        final List<CMSItemModel> relatedPages = getRelatedItemsService().getRelatedItems(cmsItem).stream()
                .filter(this::isPage)
                .collect(Collectors.toList());

        return relatedPages.isEmpty() || isAnyRelatedPageApproved(relatedPages);
    }

    /**
     * This method is used to check if there is at least one page approved to be synchronized in the given list.
     *
     * @param items
     *      The items where to look for pages.
     * @return boolean True if at least one page is approved. False, otherwise.
     */
    protected boolean isAnyRelatedPageApproved(final List<CMSItemModel> items)
    {
        return items.stream()
                .filter(this::isPage)
                .anyMatch(this::isPageApproved);
    }

    /**
     * This method checks if all the slots in the given list are allowed to be synchronized.
     * A slot can be synchronized if it is a shared slot or if it's related to at least one page that is approved to be
     * synchronized.
     *
     * @param items
     *      The items where to look for the slots to check.
     * @return boolean True if all the slots are approved. False, otherwise.
     */
    protected boolean areAllRelatedSlotsApproved(final List<CMSItemModel> items)
    {
        final List<CMSItemModel> slots = items.stream().filter(this::isSlot).collect(Collectors.toList());

        return !slots.isEmpty() && slots.stream().allMatch(this::isSlotApproved);
    }

    /**
     * This method is used to ensure there are no pages in the given list.
     *
     * @param items
     *      The items where to look for pages.
     * @return boolean True if no pages were found in the given list. False, otherwise.
     */
    protected boolean hasNoRelatedPages(final List<CMSItemModel> items)
    {
        return items.stream().noneMatch(this::isPage);
    }

    /**
     * This method is used to retrieve the {@link ItemModel} referenced by the given {@link ItemSynchronizationData}.
     *
     * @param itemSynchronizationData
     *      The object that contains the data that identifies the itemModel to retrieve
     * @return the retrieved ItemModel.
     */
    protected ItemModel getItem(final ItemSynchronizationData itemSynchronizationData)
    {
        final ItemData itemData = new ItemData();
        itemData.setItemId(itemSynchronizationData.getItemId());
        itemData.setItemType(itemSynchronizationData.getItemType());

        return getUniqueItemIdentifierService().getItemModel(itemData).get();
    }

    protected UniqueItemIdentifierService getUniqueItemIdentifierService()
    {
        return uniqueItemIdentifierService;
    }

    @Required
    public void setUniqueItemIdentifierService(final UniqueItemIdentifierService uniqueItemIdentifierService)
    {
        this.uniqueItemIdentifierService = uniqueItemIdentifierService;
    }

    protected RelatedItemsService getRelatedItemsService()
    {
        return relatedItemsService;
    }

    @Required
    public void setRelatedItemsService(final RelatedItemsService relatedItemsService)
    {
        this.relatedItemsService = relatedItemsService;
    }

    protected Predicate<ItemModel> getCmsContentSlotPredicate()
    {
        return cmsContentSlotPredicate;
    }

    @Required
    public void setCmsContentSlotPredicate(final Predicate<ItemModel> cmsContentSlotPredicate)
    {
        this.cmsContentSlotPredicate = cmsContentSlotPredicate;
    }

    protected Predicate<ItemModel> getPageTypePredicate()
    {
        return pageTypePredicate;
    }

    @Required
    public void setPageTypePredicate(final Predicate<ItemModel> pageTypePredicate)
    {
        this.pageTypePredicate = pageTypePredicate;
    }
}