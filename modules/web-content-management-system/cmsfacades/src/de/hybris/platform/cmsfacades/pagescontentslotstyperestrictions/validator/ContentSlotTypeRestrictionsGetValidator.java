/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.pagescontentslotstyperestrictions.validator;

import de.hybris.platform.cmsfacades.CMSPageContentSlotListData;
import de.hybris.platform.cmsfacades.common.predicate.PageExistsPredicate;
import de.hybris.platform.cmsfacades.constants.CmsfacadesConstants;
import org.assertj.core.util.Strings;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;
import java.util.Objects;

/**
 * Validates fields of {@link CMSPageContentSlotListData} which will be used to retrieve type restriction information
 * for slots in a page.
 */
public class ContentSlotTypeRestrictionsGetValidator implements Validator
{
    private static final String PAGE_ID = "pageId";
    private static final String SLOT_IDS = "slotIds";

    private PageExistsPredicate pageExistsPredicate;

    @Override
    public boolean supports(final Class<?> aClass)
    {
        return CMSPageContentSlotListData.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(final Object object, final Errors errors)
    {
        final CMSPageContentSlotListData pageContentSlotListData = (CMSPageContentSlotListData) object;

        if (Objects.isNull(pageContentSlotListData.getPageId()))
        {
            errors.rejectValue(PAGE_ID, CmsfacadesConstants.FIELD_REQUIRED);
        }
        else if (!getPageExistsPredicate().test(pageContentSlotListData.getPageId()))
        {
            errors.rejectValue(PAGE_ID, CmsfacadesConstants.FIELD_DOES_NOT_EXIST);
        }

        final List<String> slotIds = pageContentSlotListData.getSlotIds();
        if (CollectionUtils.isEmpty(slotIds))
        {
            errors.rejectValue(SLOT_IDS, CmsfacadesConstants.FIELD_REQUIRED);
        }
        else
        {
            final boolean hasInvalidSlots = pageContentSlotListData.getSlotIds().stream()
                    .anyMatch(Strings::isNullOrEmpty);

            if (hasInvalidSlots)
            {
                errors.rejectValue(SLOT_IDS, CmsfacadesConstants.CONTENT_SLOT_INVALID_UID);
            }
        }
    }

    public PageExistsPredicate getPageExistsPredicate()
    {
        return pageExistsPredicate;
    }

    @Required
    public void setPageExistsPredicate(final PageExistsPredicate pageExistsPredicate)
    {
        this.pageExistsPredicate = pageExistsPredicate;
    }
}
