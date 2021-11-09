/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cms2.servicelayer.daos.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.cms2.model.CMSComponentTypeModel;
import de.hybris.platform.cms2.model.contents.components.CMSFlexComponentModel;
import de.hybris.platform.cms2.model.contents.components.CMSImageComponentModel;
import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;
import de.hybris.platform.cms2.model.contents.components.CMSParagraphComponentModel;
import de.hybris.platform.cms2.model.pages.PageTemplateModel;
import de.hybris.platform.cms2.servicelayer.daos.CMSTypeRestrictionDao;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@IntegrationTest
public class DefaultCMSTypeRestrictionDaoTest extends ServicelayerTransactionalTest
{
    private static final String TEMPLATE_1_ID = "TemplateOne";
    private static final String TEMPLATE_2_ID = "TemplateTwo";
    private static final String TEMPLATE_3_ID = "TemplateThree";

    @Resource
    private CMSTypeRestrictionDao cmsTypeRestrictionDao;

    @Resource
    private FlexibleSearchService flexibleSearchService;

    @Before
    public void setUp() throws Exception
    {
        importCsv("/test/cmsTypeRestrictionsTestData.impex", "UTF-8");
    }

    @Test
    public void shouldFindTypeRestrictionsForPageTemplate()
    {
        // GIVEN
        final PageTemplateModel templateModel = getTemplateById(TEMPLATE_1_ID);

        // WHEN
        final List<CMSComponentTypeModel> result = cmsTypeRestrictionDao.getTypeRestrictionsForPageTemplate(templateModel);

        // THEN
        assertThat(result.size(), is(4));
        assertThat(result, hasItems(
                allOf(hasProperty(CMSComponentTypeModel.CODE, equalTo(CMSParagraphComponentModel._TYPECODE))),
                allOf(hasProperty(CMSComponentTypeModel.CODE, equalTo(CMSLinkComponentModel._TYPECODE))),
                allOf(hasProperty(CMSComponentTypeModel.CODE, equalTo(CMSFlexComponentModel._TYPECODE))),
                allOf(hasProperty(CMSComponentTypeModel.CODE, equalTo(CMSImageComponentModel._TYPECODE)))
        ));
    }

    @Test
    public void givenRestrictionsAreAssignedToContentSlotName_shouldFindTypeRestrictionsForPageTemplate()
    {
        // GIVEN
        final PageTemplateModel templateModel = getTemplateById(TEMPLATE_2_ID);

        // WHEN
        final List<CMSComponentTypeModel> result = cmsTypeRestrictionDao.getTypeRestrictionsForPageTemplate(templateModel);

        // THEN
        assertThat(result.size(), is(2));
        assertThat(result, hasItems(
                allOf(hasProperty(CMSComponentTypeModel.CODE, equalTo(CMSParagraphComponentModel._TYPECODE))),
                allOf(hasProperty(CMSComponentTypeModel.CODE, equalTo(CMSFlexComponentModel._TYPECODE)))
        ));
    }

    @Test
    public void givenRestrictionsAreAssignedToComponentTypeGroup_shouldFindTypeRestrictionsForPageTemplate()
    {
        // GIVEN
        final PageTemplateModel templateModel = getTemplateById(TEMPLATE_3_ID);

        // WHEN
        final List<CMSComponentTypeModel> result = cmsTypeRestrictionDao.getTypeRestrictionsForPageTemplate(templateModel);

        // THEN
        assertThat(result.size(), is(2));
        assertThat(result, hasItems(
                allOf(hasProperty(CMSComponentTypeModel.CODE, equalTo(CMSParagraphComponentModel._TYPECODE))),
                allOf(hasProperty(CMSComponentTypeModel.CODE, equalTo(CMSImageComponentModel._TYPECODE)))
        ));
    }

    protected PageTemplateModel getTemplateById(final String templateId)
    {
        final PageTemplateModel templateModel = new PageTemplateModel();
        templateModel.setUid(templateId);

        return flexibleSearchService.getModelByExample(templateModel);
    }
}
