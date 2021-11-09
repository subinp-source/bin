/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.configurablebundleservices.daos.impl;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.configurablebundleservices.model.BundleTemplateModel;
import de.hybris.platform.configurablebundleservices.model.DisableProductBundleRuleModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;


/**
 * Default implementation of the {@link AbstractBundleRuleDao} for sub-type {@link DisableProductBundleRuleModel}.
 */
public class DefaultDisableProductBundleRuleDao extends AbstractBundleRuleDao<DisableProductBundleRuleModel>
{
	private static final String FIND_BUNDLE_RULES = "GET {" + DisableProductBundleRuleModel._TYPECODE + "}";

	private static final String FIND_BUNDLE_RULES_BY_TEMPLATE_QUERY = FIND_BUNDLE_RULES
			+ " WHERE {" + DisableProductBundleRuleModel.BUNDLETEMPLATE + "}=?bundleTemplate";

	private static final String FIND_BUNDLE_RULES_BY_PRODUCT_AND_ROOT_TEMPLATE_QUERY = FIND_BUNDLE_RULES;

	@Override
	public String getFindBundleRulesByTargetProductQuery()
	{
		return FIND_BUNDLE_RULES;
	}

	@Override
	public String getFindBundleRulesByTargetProductAndTemplateQuery()
	{
		return FIND_BUNDLE_RULES_BY_TEMPLATE_QUERY;
	}

	@Override
	public String getFindBundleRulesByProductAndRootTemplateQuery()
	{
		return FIND_BUNDLE_RULES_BY_PRODUCT_AND_ROOT_TEMPLATE_QUERY;
	}

	@Override
	@Nonnull
	public List<DisableProductBundleRuleModel> findBundleRulesByProductAndRootTemplate(@Nonnull final ProductModel product,
														   @Nonnull final BundleTemplateModel rootBundleTemplate)
	{
		validateParameterNotNullStandardMessage("product", product);
		validateParameterNotNullStandardMessage("rootBundleTemplate", rootBundleTemplate);

		final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(getFindBundleRulesByProductAndRootTemplateQuery());

		final SearchResult<DisableProductBundleRuleModel> results = search(flexibleSearchQuery);
		return results.getResult().stream()
				.filter(rule -> rootBundleTemplate.equals(rule.getBundleTemplate().getParentTemplate()) &&
						(rule.getTargetProducts().contains(product) || rule.getConditionalProducts().contains(product)))
				.collect(Collectors.toList());
	}
}
