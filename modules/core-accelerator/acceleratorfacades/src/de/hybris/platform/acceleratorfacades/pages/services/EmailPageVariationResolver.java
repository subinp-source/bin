/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorfacades.pages.services;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminPageService;
import de.hybris.platform.cmsfacades.constants.CmsfacadesConstants;
import de.hybris.platform.cmsfacades.data.OptionData;
import de.hybris.platform.cmsfacades.page.DisplayCondition;
import de.hybris.platform.cmsfacades.pages.service.PageVariationResolver;
import org.springframework.beans.factory.annotation.Required;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the <code>PageVariationResolver</code>. This is used for retrieving the default display
 * condition for an email page.
 */
public class EmailPageVariationResolver implements PageVariationResolver<EmailPageModel>
{
    protected CMSAdminPageService adminPageService;

    @Override
    public List<EmailPageModel> findPagesByType(String typeCode, boolean isDefaultPage)
    {
        if (!isDefaultPage)
        {
            return Collections.emptyList();
        }

        return getAdminPageService().getAllPagesByType(EmailPageModel.TYPECODE) //
                .stream().map(EmailPageModel.class::cast).collect(Collectors.toList());
    }

    @Override
    public List<EmailPageModel> findDefaultPages(EmailPageModel pageModel)
    {
        return Collections.emptyList();
    }

    @Override
    public List<EmailPageModel> findVariationPages(EmailPageModel pageModel)
    {
        return Collections.emptyList();
    }

    @Override
    public boolean isDefaultPage(EmailPageModel pageModel)
    {
        return pageModel.getDefaultPage();
    }

    @Override
    public List<OptionData> findDisplayConditions(String typeCode)
    {
        final OptionData optionData = new OptionData();

        optionData.setId(DisplayCondition.PRIMARY.name());
        optionData.setLabel(CmsfacadesConstants.PAGE_DISPLAY_CONDITION_PRIMARY);

        return Arrays.asList(optionData);
    }

    protected CMSAdminPageService getAdminPageService()
    {
        return adminPageService;
    }

    @Required
    public void setAdminPageService(final CMSAdminPageService adminPageService)
    {
        this.adminPageService = adminPageService;
    }
}
