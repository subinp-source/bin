/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.configurablebundleservices.constraints;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.configurablebundleservices.model.AbstractBundleRuleModel;
import de.hybris.platform.configurablebundleservices.model.BundleTemplateModel;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import javax.validation.ConstraintValidatorContext;

import org.apache.commons.collections.CollectionUtils;

/**
 * Triggers when {@link de.hybris.platform.configurablebundleservices.model.DisableProductBundleRuleModel#getTargetProducts()}
 * contains a product is not a part of
 * {@link de.hybris.platform.configurablebundleservices.model.BundleTemplateModel#getProducts()}.
 */
public class DisableRuleTargetIntegrityValidator
        extends BasicBundleRuleValidator<DisableRuleTargetIntegrity>
{
    @Override
    public boolean isValid(final AbstractBundleRuleModel value, final ConstraintValidatorContext context)
    {
        validateParameterNotNull(value, "Validating object is null");
        if(value.getTargetProducts() == null) {
            return true;
        }

        final BundleTemplateModel bundleTemplate = getBundleTemplate(value);
        Collection<ProductModel> invalidProducts = getInvalidProducts(value, bundleTemplate);

        invalidProducts.forEach(invalidProduct ->
                                    buildErrorMessage(AbstractBundleRuleModel.TARGETPRODUCTS, context, invalidProduct.getCode()));

        return CollectionUtils.isEmpty(invalidProducts);
    }

    private Collection<ProductModel> getInvalidProducts(final AbstractBundleRuleModel abstractBundleRule, final BundleTemplateModel bundleTemplate) {
        final Collection<ProductModel> targetProducts = Optional.ofNullable(abstractBundleRule).map(AbstractBundleRuleModel::getTargetProducts).orElseGet(Collections::emptyList);
        final Collection<ProductModel> bundleProducts = Optional.ofNullable(bundleTemplate).map(BundleTemplateModel::getProducts).orElseGet(Collections::emptyList);
        return CollectionUtils.subtract(targetProducts, bundleProducts);
    }
}
