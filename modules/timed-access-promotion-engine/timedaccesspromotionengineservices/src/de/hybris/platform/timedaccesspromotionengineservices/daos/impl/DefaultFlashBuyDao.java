/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.timedaccesspromotionengineservices.daos.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.promotionengineservices.model.ProductForPromotionSourceRuleModel;
import de.hybris.platform.promotionengineservices.model.PromotionSourceRuleModel;
import de.hybris.platform.promotionengineservices.model.RuleBasedPromotionModel;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.ruleengine.model.DroolsRuleModel;
import de.hybris.platform.ruleengineservices.enums.RuleStatus;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.timedaccesspromotionengineservices.daos.FlashBuyDao;
import de.hybris.platform.timedaccesspromotionengineservices.model.FlashBuyCouponModel;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;


/**
 * Default implementation of {@link FlashBuyDao}
 */
public class DefaultFlashBuyDao extends DefaultGenericDao<FlashBuyCouponModel> implements FlashBuyDao
{
	private static final String COUPON_ACTIVE = "active";
	private static final String CATALOG_VERSION_ACTIVE = "active";
	private static final String DROOLS_RULE_CURRENT_VERSION = "currentVersion";

	private static final Logger LOG = Logger.getLogger(DefaultFlashBuyDao.class);

	private static final String FIND_FLASHBUY_COUPON_BY_PROMOTIONCODE_QUERY = "SELECT {c:" + FlashBuyCouponModel.PK + "} FROM {"
			+ FlashBuyCouponModel._TYPECODE + " AS c JOIN " + PromotionSourceRuleModel._TYPECODE + " AS p ON {p:"
			+ PromotionSourceRuleModel.PK + "} = {c:" + FlashBuyCouponModel.RULE + "} JOIN " + RuleStatus._TYPECODE
			+ " AS rs ON {p:" + PromotionSourceRuleModel.STATUS + "} = {rs:pk}} WHERE {" + FlashBuyCouponModel.ACTIVE
			+ "} = ?" + COUPON_ACTIVE + " AND {rs:code} = '" + RuleStatus.PUBLISHED.getCode() + "' AND {p:"
			+ PromotionSourceRuleModel.CODE + "} = ?promotionCode";

	private static final String FIND_PRODUCT_BY_PROMOTION_QUERY = "SELECT {p:" + ProductModel.PK + "} FROM {"
			+ ProductForPromotionSourceRuleModel._TYPECODE + " AS pfr JOIN " + ProductModel._TYPECODE + " AS p ON {pfr:"
			+ ProductForPromotionSourceRuleModel.PRODUCTCODE + "} = {p:" + ProductModel.CODE + "} JOIN "
			+ CatalogVersionModel._TYPECODE + " AS cv ON {p:" + ProductModel.CATALOGVERSION + "} = {cv:" + CatalogVersionModel.PK
			+ "} AND {cv:" + CatalogVersionModel.ACTIVE + "} = ?" + COUPON_ACTIVE + "} WHERE {pfr:"
			+ ProductForPromotionSourceRuleModel.PROMOTION + "} = ?promotion";

	private static final String FIND_ALL_PRODUCTS_BY_PROMOTION_SOURCE_RULE = "SELECT {p:" + ProductModel.PK + "} FROM {"
			+ ProductForPromotionSourceRuleModel._TYPECODE + " AS pfr JOIN " + ProductModel._TYPECODE + " AS p ON {pfr:"
			+ ProductForPromotionSourceRuleModel.PRODUCTCODE + "} = {p:" + ProductModel.CODE + "} JOIN "
			+ CatalogVersionModel._TYPECODE + " AS cv ON {p:" + ProductModel.CATALOGVERSION + "} = {cv:" + CatalogVersionModel.PK
			+ "} AND {cv:" + CatalogVersionModel.ACTIVE + "} = ?" + CATALOG_VERSION_ACTIVE + " JOIN "
			+ PromotionSourceRuleModel._TYPECODE + " AS psr ON {psr:" + PromotionSourceRuleModel.PK + "} = {pfr:"
			+ ProductForPromotionSourceRuleModel.RULE + "} JOIN " + RuleStatus._TYPECODE + " AS rs ON {psr:"
			+ PromotionSourceRuleModel.STATUS + "} = {rs:pk} JOIN " + RuleBasedPromotionModel._TYPECODE + " AS rbp ON {pfr:"
			+ ProductForPromotionSourceRuleModel.PROMOTION + "} = {rbp:" + RuleBasedPromotionModel.PK
			+ "} JOIN " + DroolsRuleModel._TYPECODE + " AS dr ON {rbp:" + RuleBasedPromotionModel.RULE + "} = {dr:"
			+ DroolsRuleModel.PK + "} AND {dr:" + DroolsRuleModel.CURRENTVERSION + "} = ?" + DROOLS_RULE_CURRENT_VERSION
			+ "} WHERE {rs:code} = '" + RuleStatus.PUBLISHED.getCode() + "' AND {psr:" + PromotionSourceRuleModel.PK + "} = ?rule";

	private static final String FIND_PROMOTION_RULE_BY_PRODUCT = "SELECT {r.pk} as pr FROM {PromotionSourceRule as r}, "
			+ "{ProductForPromotionSourceRule as rel}, {" + RuleStatus._TYPECODE
			+ " as rs} WHERE {r.status} = {rs.pk} and {rs.code} " + "= '" + RuleStatus.PUBLISHED.getCode()
			+ "' AND {rel.rule} = {r.pk} AND {rel.productCode} = ?productCode";

	private static final String FIND_PRODUCT_FOR_PROMOTION_RULE = "SELECT {rel.pk} as pr FROM {PromotionSourceRule as r}, "
			+ "{ProductForPromotionSourceRule as rel}, {" + RuleStatus._TYPECODE
			+ " as rs} WHERE {r.status} = {rs.pk} and {rs.code} = '" + RuleStatus.PUBLISHED.getCode()
			+ "' AND {rel.rule} = {r.pk} AND {r.code} = ?code";

	private static final String FIND_FLASHBUYCOUPON_BY_PRODUCTCODE = "SELECT {c:" + FlashBuyCouponModel.PK + "} as pr FROM {"
			+ FlashBuyCouponModel._TYPECODE + " as c JOIN " + PromotionSourceRuleModel._TYPECODE + " AS p ON {p:"
			+ PromotionSourceRuleModel.PK + "} = {c:" + FlashBuyCouponModel.RULE + "} JOIN " + RuleStatus._TYPECODE + " AS rs ON {p:"
			+ PromotionSourceRuleModel.STATUS + "} = {rs:pk}}  WHERE {c:" + FlashBuyCouponModel.ACTIVE + "} = ?" + COUPON_ACTIVE
			+ " AND {rs:code} = '" + RuleStatus.PUBLISHED.getCode() + "' AND ({p:" + PromotionSourceRuleModel.ENDDATE
			+ "} > ?now OR {p:" + PromotionSourceRuleModel.ENDDATE + "} is null) AND {c:" + FlashBuyCouponModel.PRODUCT
			+ "} = ?product ";

	public DefaultFlashBuyDao()
	{
		super(FlashBuyCouponModel._TYPECODE);
	}

	@Override
	public Optional<FlashBuyCouponModel> findFlashBuyCouponByPromotionCode(final String promotionCode)
	{
		final FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_FLASHBUY_COUPON_BY_PROMOTIONCODE_QUERY);
		query.addQueryParameter(COUPON_ACTIVE, Boolean.TRUE);
		query.addQueryParameter("promotionCode", promotionCode);

		try
		{
			return Optional.of(getFlexibleSearchService().searchUnique(query));
		}
		catch (final ModelNotFoundException e)
		{
			return Optional.empty();
		}
		catch (final AmbiguousIdentifierException e)
		{
			LOG.warn("More than one FlashBuyCoupon Found, return empty.");
			return Optional.empty();
		}
	}

	@Override
	public List<PromotionSourceRuleModel> findPromotionSourceRuleByProduct(final String productCode)
	{
		final FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_PROMOTION_RULE_BY_PRODUCT);
		query.addQueryParameter("productCode", productCode);

		return getFlexibleSearchService().<PromotionSourceRuleModel> search(query).getResult();
	}

	@Override
	public Optional<ProductModel> findProductByPromotion(final AbstractPromotionModel promotion)
	{
		final FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_PRODUCT_BY_PROMOTION_QUERY);
		query.addQueryParameter(COUPON_ACTIVE, Boolean.TRUE);
		query.addQueryParameter("promotion", promotion);

		try
		{
			return Optional.of(getFlexibleSearchService().searchUnique(query));
		}
		catch (final ModelNotFoundException e)
		{
			return Optional.empty();
		}
	}

	@Override
	public List<ProductForPromotionSourceRuleModel> findProductForPromotionSourceRules(final PromotionSourceRuleModel rule)
	{
		final FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_PRODUCT_FOR_PROMOTION_RULE);
		query.addQueryParameter("code", rule.getCode());
		return getFlexibleSearchService().<ProductForPromotionSourceRuleModel> search(query).getResult();
	}

	@Override
	public List<ProductModel> findAllProductsByPromotionSourceRule(final PromotionSourceRuleModel rule)
	{
		final FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_ALL_PRODUCTS_BY_PROMOTION_SOURCE_RULE);
		query.addQueryParameter(CATALOG_VERSION_ACTIVE, Boolean.TRUE);
		query.addQueryParameter(DROOLS_RULE_CURRENT_VERSION, Boolean.TRUE);
		query.addQueryParameter("rule", rule);
		return getFlexibleSearchService().<ProductModel> search(query).getResult();
	}

	@Override
	public List<FlashBuyCouponModel> findFlashBuyCouponByProduct(final ProductModel product)
	{
		final FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_FLASHBUYCOUPON_BY_PRODUCTCODE);
		query.addQueryParameter(COUPON_ACTIVE, Boolean.TRUE);
		query.addQueryParameter("now", new Date());
		query.addQueryParameter("product", product);
		return getFlexibleSearchService().<FlashBuyCouponModel> search(query).getResult();
	}

}
