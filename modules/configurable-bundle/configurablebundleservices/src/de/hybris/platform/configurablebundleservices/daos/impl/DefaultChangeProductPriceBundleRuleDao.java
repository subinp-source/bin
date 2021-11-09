/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.configurablebundleservices.daos.impl;

import de.hybris.platform.configurablebundleservices.daos.ChangeProductPriceBundleRuleDao;
import de.hybris.platform.configurablebundleservices.model.BundleTemplateModel;
import de.hybris.platform.configurablebundleservices.model.ChangeProductPriceBundleRuleModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;


/**
 * Default implementation of the {@link ChangeProductPriceBundleRuleDao}
 */
public class DefaultChangeProductPriceBundleRuleDao extends AbstractBundleRuleDao<ChangeProductPriceBundleRuleModel> implements
		ChangeProductPriceBundleRuleDao
{

	private static final String FIND_BUNDLE_RULES = "GET {" + ChangeProductPriceBundleRuleModel._TYPECODE + "}";

	private static final String FIND_PRICE_RULES_BY_CURRENCY_QUERY = FIND_BUNDLE_RULES
			+ " WHERE {" + ChangeProductPriceBundleRuleModel.CURRENCY + "}=?currency";

	private static final String FIND_BUNDLE_RULES_BY_TEMPLATE_QUERY = FIND_BUNDLE_RULES
			+ " WHERE {" + ChangeProductPriceBundleRuleModel.BUNDLETEMPLATE + "}=?bundleTemplate";

	private static final String FIND_PRICE_RULES_BY_TEMPLATE_AND_CURRENCY_QUERY = FIND_BUNDLE_RULES_BY_TEMPLATE_QUERY
			+ " AND {" + ChangeProductPriceBundleRuleModel.CURRENCY + "}=?currency";

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
	public List<ChangeProductPriceBundleRuleModel> findBundleRulesByTargetProductAndCurrency(@Nonnull final ProductModel targetProduct,
																							 @Nonnull final CurrencyModel currency)
	{
		validateParameterNotNullStandardMessage("product", targetProduct);
		validateParameterNotNullStandardMessage("currency", currency);

		final FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_PRICE_RULES_BY_CURRENCY_QUERY);
		query.addQueryParameter("currency", currency);

		final SearchResult<ChangeProductPriceBundleRuleModel> rulesQueryResults = search(query);

		final List<ChangeProductPriceBundleRuleModel> filteredResult = rulesQueryResults.getResult().stream()
				.filter(rule -> rule.getTargetProducts().contains(targetProduct))
				.collect(Collectors.toList());

		return filteredResult;
	}

	@Override
	@Nonnull
	public List<ChangeProductPriceBundleRuleModel> findBundleRulesByTargetProductAndTemplateAndCurrency(
			@Nonnull final ProductModel targetProduct,
			@Nonnull final BundleTemplateModel bundleTemplate,
			@Nonnull final CurrencyModel currency)
	{
		validateParameterNotNullStandardMessage("targetProduct", targetProduct);
		validateParameterNotNullStandardMessage("bundleTemplate", bundleTemplate);
		validateParameterNotNullStandardMessage("currency", currency);

		final FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_PRICE_RULES_BY_TEMPLATE_AND_CURRENCY_QUERY);
		query.addQueryParameter("bundleTemplate", bundleTemplate);
		query.addQueryParameter("currency", currency);

		final SearchResult<ChangeProductPriceBundleRuleModel> rulesQueryResults = search(query);

		final List<ChangeProductPriceBundleRuleModel> filteredResult = rulesQueryResults.getResult().stream()
				.filter(rule -> rule.getTargetProducts().contains(targetProduct))
				.collect(Collectors.toList());

		return filteredResult;
	}

	@Override
	@Nonnull
	public List<ChangeProductPriceBundleRuleModel> findBundleRulesByProductAndRootTemplate(@Nonnull final ProductModel product,
														   @Nonnull final BundleTemplateModel rootBundleTemplate)

	{
		validateParameterNotNullStandardMessage("product", product);
		validateParameterNotNullStandardMessage("rootBundleTemplate", rootBundleTemplate);

		final FlexibleSearchQuery query = new FlexibleSearchQuery(getFindBundleRulesByProductAndRootTemplateQuery());

		final SearchResult<ChangeProductPriceBundleRuleModel> results = search(query);

		final List<ChangeProductPriceBundleRuleModel> filteredResult = results.getResult().stream()
				.filter(rule -> rootBundleTemplate.equals(rule.getBundleTemplate().getParentTemplate()) &&
						(rule.getTargetProducts().contains(product) || rule.getConditionalProducts().contains(product)))
				.collect(Collectors.toList());

		return filteredResult;
	}

}
