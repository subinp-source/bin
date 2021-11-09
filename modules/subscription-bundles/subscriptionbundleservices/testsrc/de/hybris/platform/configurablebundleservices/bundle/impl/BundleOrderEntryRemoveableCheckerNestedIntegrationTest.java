/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.configurablebundleservices.bundle.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.configurablebundleservices.bundle.BundleTemplateService;
import de.hybris.platform.configurablebundleservices.bundle.RemoveableChecker;
import de.hybris.platform.configurablebundleservices.constants.ConfigurableBundleServicesConstants;
import de.hybris.platform.configurablebundleservices.model.BundleTemplateModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.impex.constants.ImpExConstants;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.product.UnitService;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Resource;

import java.util.Collections;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
@IntegrationTest
public class BundleOrderEntryRemoveableCheckerNestedIntegrationTest  extends ServicelayerTest
{
    private static final Logger LOG = Logger.getLogger(BundleOrderEntryRemoveableCheckerNestedIntegrationTest.class);
    public static final String PRODUCT01 = "PRODUCT01";
    public static final String REGULAR_COMPONENT = "ProductComponent1";
    public static final String OPTIONAL_COMPONENT = "OptionalComponent";

    protected CartModel cart;
    protected UnitModel unitModel;
    @Resource
    protected UserService userService;
    @Resource
    private UnitService unitService;
    @Resource
    protected CommerceCartService commerceCartService;
    @Resource
    private CatalogVersionService catalogVersionService;
    @Resource
    protected ProductService productService;
    @Resource
    private FlexibleSearchService flexibleSearchService;
    @Resource
    private BundleTemplateService bundleTemplateService;
    @Resource(name = "orderEntryRemoveableChecker")
    private RemoveableChecker removeableChecker;

    @Before
    public void setUp() throws Exception
    {
        LOG.debug("Preparing test data");
        final String legacyModeBackup = Config.getParameter(ImpExConstants.Params.LEGACY_MODE_KEY);
        Config.setParameter(ImpExConstants.Params.LEGACY_MODE_KEY, "true");
        try
        {
            importCsv("/configurablebundleservices/test/cartRegistration.impex", "utf-8");
            importCsv("/subscriptionbundleservices/test/nestedBundleTemplates.impex", "utf-8");
        }
        finally
        {
            Config.setParameter(ImpExConstants.Params.LEGACY_MODE_KEY, legacyModeBackup);
        }

        cart = userService.getUserForUID("bundle").getCarts().iterator().next();
        unitModel = unitService.getUnitForCode("pieces");
    }

    @Test
    public void shouldNotDeleteItemsWhichDontSatisfyMinCondition() throws CommerceCartModificationException, CalculationException
    {
        addToCart(PRODUCT01, REGULAR_COMPONENT);
        assertEquals(1, cart.getEntries().size());
        AbstractOrderEntryModel abstractOrderEntryModel = cart.getEntries().iterator().next();
        assertFalse(removeableChecker.canRemove(abstractOrderEntryModel));
    }

    @Test
    public void shouldDeleteItemsWhichSatisfyMinCondition() throws CommerceCartModificationException, CalculationException
    {
        addToCart(PRODUCT01, OPTIONAL_COMPONENT);
        assertEquals(1, cart.getEntries().size());
        AbstractOrderEntryModel abstractOrderEntryModel = cart.getEntries().iterator().next();
        assertTrue(removeableChecker.canRemove(abstractOrderEntryModel));
    }

    @Test
    public void shouldRemoveNonBundleEntry() throws CommerceCartModificationException, CalculationException
    {
        addToCart(PRODUCT01);
        assertEquals(1, cart.getEntries().size());
        AbstractOrderEntryModel abstractOrderEntryModel = cart.getEntries().iterator().next();
        assertTrue(removeableChecker.canRemove(abstractOrderEntryModel));
    }

    protected void addToCart(final String productCode, final String componentId)
            throws CommerceCartModificationException
    {
   	 CommerceCartParameter parameter = new CommerceCartParameter();
       parameter.setEnableHooks(true);
       parameter.setCart(cart);
       parameter.setProduct(getProduct(productCode));
       parameter.setQuantity(1);
       parameter.setUnit(unitModel);
       parameter.setEntryGroupNumbers(Collections.emptySet());
       parameter.setBundleTemplate(bundleTemplateService.getBundleTemplateForCode(componentId));
       commerceCartService.addToCart(parameter);
    }

    protected void addToCart(final String productCode)
            throws CommerceCartModificationException
    {
   	 CommerceCartParameter parameter = new CommerceCartParameter();
       parameter.setCart(cart);
       parameter.setProduct(getProduct(productCode));
       parameter.setQuantity(1);
       parameter.setUnit(unitModel);
       parameter.setEntryGroupNumbers(null);
       commerceCartService.addToCart(parameter);
    }

    protected ProductModel getProduct(final String code)
    {
        return productService.getProductForCode(getCatalog(), code);
    }

    protected BundleTemplateModel getBundleTemplate(final String templateId)
    {
        final BundleTemplateModel exampleModel = new BundleTemplateModel();
        exampleModel.setId(templateId);
        exampleModel.setCatalogVersion(getCatalog());
        return flexibleSearchService.getModelByExample(exampleModel);
    }

    protected CatalogVersionModel getCatalog()
    {
        return catalogVersionService.getCatalogVersion("testCatalog", "Online");
    }
}
