/**
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.merchandising.model;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Product is a simple POJO used as a payload for exporting products to CDS.
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product
{
	private String action;
	private String reportingGroup;
	private String pageUrl;
	private List<ProductFacet> facets;
	private List<String> categories;
	private Map<String, ProductMetadata> metadata;
	private ProductImage images;

	@JsonIgnore
	private Map<String, Object> productDetails;

	public String getAction()
	{
		return action;
	}

	public void setAction(final String action)
	{
		this.action = action;
	}

	public String getReportingGroup()
	{
		return reportingGroup;
	}

	public void setReportingGroup(final String reportingGroup)
	{
		this.reportingGroup = reportingGroup;
	}

	public List<ProductFacet> getFacets()
	{
		return facets;
	}

	public void setFacets(final List<ProductFacet> facets)
	{
		this.facets = facets;
	}

	public List<String> getCategories()
	{
		return categories;
	}

	public void setCategories(final List<String> categories)
	{
		this.categories = categories;
	}

	public Map<String, ProductMetadata> getMetadata()
	{
		return metadata;
	}

	public void setMetadata(final Map<String, ProductMetadata> metadata)
	{
		this.metadata = metadata;
	}

	public ProductImage getImages()
	{
		return images;
	}

	public void setImages(final ProductImage images)
	{
		this.images = images;
	}

	@JsonAnyGetter
	public Map<String, Object> getProductDetails()
	{
		return productDetails;
	}

	public void setProductDetails(final Map<String, Object> productDetails)
	{
		this.productDetails = productDetails;
	}

	public String getPageUrl() {
		return pageUrl;
	}

	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}
}
