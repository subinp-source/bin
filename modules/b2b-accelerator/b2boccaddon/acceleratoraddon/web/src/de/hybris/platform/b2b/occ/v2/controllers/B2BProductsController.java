/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.occ.v2.controllers;

import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.request.mapping.annotation.ApiVersion;
import de.hybris.platform.commerceservices.request.mapping.annotation.RequestMappingOverride;
import de.hybris.platform.commercewebservicescommons.dto.product.ProductWsDTO;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import de.hybris.platform.webservicescommons.mapping.FieldSetBuilder;
import de.hybris.platform.webservicescommons.mapping.FieldSetLevelHelper;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdParam;
import de.hybris.platform.webservicescommons.swagger.ApiFieldsParam;

import javax.annotation.Resource;

import java.util.Arrays;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import static de.hybris.platform.b2b.occ.constants.B2boccaddonConstants.OCC_REWRITE_OVERLAPPING_PRODUCTS_PATH;


/**
 * Controller REST resource which replaces/extends some Product URIs
 */
@Controller
@RequestMapping(value = OCC_REWRITE_OVERLAPPING_PRODUCTS_PATH)
@ApiVersion("v2")
@Api(tags = "B2B Products")
public class B2BProductsController extends BaseController
{
	@Resource(name = "productVariantFacade")
	private ProductFacade productFacade;

	@Resource(name = "dataMapper")
	protected DataMapper dataMapper;

	@Resource
	private FieldSetBuilder fieldSetBuilder;

	@RequestMapping(value = "/{productCode}", method = RequestMethod.GET)
	@RequestMappingOverride(priorityProperty = "b2bocc.B2BProductsController.getProductByCode.priority")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@ApiBaseSiteIdParam
	@ApiOperation(nickname = "getOrgProduct", value = "Returns a product.", notes = "Returns a product, based on the specified product code.")
	public ProductWsDTO getProductByCode(
			@ApiParam(value = "The product code", required = true) @PathVariable final String productCode,
			@ApiFieldsParam @RequestParam(required = false, defaultValue = FieldSetLevelHelper.DEFAULT_LEVEL) final String fields)
	{

		final Set<String> fieldSet = fieldSetBuilder.createFieldSet(ProductWsDTO.class, DataMapper.FIELD_PREFIX, fields);

		final ProductData productData = productFacade.getProductForCodeAndOptions(productCode,
				Arrays.asList(ProductOption.BASIC, ProductOption.PRICE, ProductOption.SUMMARY, ProductOption.DESCRIPTION,
						ProductOption.GALLERY, ProductOption.CATEGORIES, ProductOption.REVIEW, ProductOption.PROMOTIONS,
						ProductOption.CLASSIFICATION, ProductOption.VARIANT_FULL, ProductOption.STOCK, ProductOption.VOLUME_PRICES,
						ProductOption.PRICE_RANGE, ProductOption.VARIANT_MATRIX_ALL_OPTIONS, ProductOption.VARIANT_MATRIX_BASE));

		final ProductWsDTO dto = dataMapper.map(productData, ProductWsDTO.class, fieldSet);

		return dto;
	}
}
