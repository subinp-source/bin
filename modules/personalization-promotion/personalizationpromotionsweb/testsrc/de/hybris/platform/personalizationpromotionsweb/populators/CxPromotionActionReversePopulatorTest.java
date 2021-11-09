/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.personalizationpromotionsweb.populators;


import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.personalizationpromotions.model.CxPromotionActionModel;
import de.hybris.platform.personalizationpromotionsweb.data.CxPromotionActionData;
import org.junit.Assert;
import org.junit.Test;

@UnitTest
public class CxPromotionActionReversePopulatorTest
{

    private static final String PROMOTION_ID = "testPromotionID";

    private final CxPromotionActionReversePopulator cxPromotionActionReversePopulator = new CxPromotionActionReversePopulator();


    @Test
    public void shouldPopulate()
    {
        final CxPromotionActionData source = new CxPromotionActionData();
        source.setPromotionId(PROMOTION_ID);
        final CxPromotionActionModel target = new CxPromotionActionModel();
        cxPromotionActionReversePopulator.populate(source, target);

        Assert.assertEquals(PROMOTION_ID, target.getPromotionId());
    }
}
