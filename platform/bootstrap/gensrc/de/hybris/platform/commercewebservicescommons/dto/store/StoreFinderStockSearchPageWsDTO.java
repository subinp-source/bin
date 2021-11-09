/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:28 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicescommons.dto.store;

import java.io.Serializable;
import de.hybris.platform.commercewebservicescommons.dto.product.ProductWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.search.pagedata.PaginationWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.search.pagedata.SortWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.store.PointOfServiceStockWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


import java.util.Objects;
/**
 * Representation of a Store Finder Stock Search Page
 */
@ApiModel(value="StoreFinderStockSearchPage", description="Representation of a Store Finder Stock Search Page")
public  class StoreFinderStockSearchPageWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** List of stores<br/><br/><i>Generated property</i> for <code>StoreFinderStockSearchPageWsDTO.stores</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="stores", value="List of stores") 	
	private List<PointOfServiceStockWsDTO> stores;

	/** List of sorts<br/><br/><i>Generated property</i> for <code>StoreFinderStockSearchPageWsDTO.sorts</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="sorts", value="List of sorts") 	
	private List<SortWsDTO> sorts;

	/** Pagination<br/><br/><i>Generated property</i> for <code>StoreFinderStockSearchPageWsDTO.pagination</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="pagination", value="Pagination") 	
	private PaginationWsDTO pagination;

	/** Location text<br/><br/><i>Generated property</i> for <code>StoreFinderStockSearchPageWsDTO.locationText</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="locationText", value="Location text") 	
	private String locationText;

	/** Source latitude<br/><br/><i>Generated property</i> for <code>StoreFinderStockSearchPageWsDTO.sourceLatitude</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="sourceLatitude", value="Source latitude") 	
	private Double sourceLatitude;

	/** Source longitude<br/><br/><i>Generated property</i> for <code>StoreFinderStockSearchPageWsDTO.sourceLongitude</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="sourceLongitude", value="Source longitude") 	
	private Double sourceLongitude;

	/** Bound to north latitude<br/><br/><i>Generated property</i> for <code>StoreFinderStockSearchPageWsDTO.boundNorthLatitude</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="boundNorthLatitude", value="Bound to north latitude") 	
	private Double boundNorthLatitude;

	/** Bound to east longitude<br/><br/><i>Generated property</i> for <code>StoreFinderStockSearchPageWsDTO.boundEastLongitude</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="boundEastLongitude", value="Bound to east longitude") 	
	private Double boundEastLongitude;

	/** Bound to south latitude<br/><br/><i>Generated property</i> for <code>StoreFinderStockSearchPageWsDTO.boundSouthLatitude</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="boundSouthLatitude", value="Bound to south latitude") 	
	private Double boundSouthLatitude;

	/** Bound to west longitude<br/><br/><i>Generated property</i> for <code>StoreFinderStockSearchPageWsDTO.boundWestLongitude</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="boundWestLongitude", value="Bound to west longitude") 	
	private Double boundWestLongitude;

	/** Product<br/><br/><i>Generated property</i> for <code>StoreFinderStockSearchPageWsDTO.product</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="product", value="Product") 	
	private ProductWsDTO product;
	
	public StoreFinderStockSearchPageWsDTO()
	{
		// default constructor
	}
	
	public void setStores(final List<PointOfServiceStockWsDTO> stores)
	{
		this.stores = stores;
	}

	public List<PointOfServiceStockWsDTO> getStores() 
	{
		return stores;
	}
	
	public void setSorts(final List<SortWsDTO> sorts)
	{
		this.sorts = sorts;
	}

	public List<SortWsDTO> getSorts() 
	{
		return sorts;
	}
	
	public void setPagination(final PaginationWsDTO pagination)
	{
		this.pagination = pagination;
	}

	public PaginationWsDTO getPagination() 
	{
		return pagination;
	}
	
	public void setLocationText(final String locationText)
	{
		this.locationText = locationText;
	}

	public String getLocationText() 
	{
		return locationText;
	}
	
	public void setSourceLatitude(final Double sourceLatitude)
	{
		this.sourceLatitude = sourceLatitude;
	}

	public Double getSourceLatitude() 
	{
		return sourceLatitude;
	}
	
	public void setSourceLongitude(final Double sourceLongitude)
	{
		this.sourceLongitude = sourceLongitude;
	}

	public Double getSourceLongitude() 
	{
		return sourceLongitude;
	}
	
	public void setBoundNorthLatitude(final Double boundNorthLatitude)
	{
		this.boundNorthLatitude = boundNorthLatitude;
	}

	public Double getBoundNorthLatitude() 
	{
		return boundNorthLatitude;
	}
	
	public void setBoundEastLongitude(final Double boundEastLongitude)
	{
		this.boundEastLongitude = boundEastLongitude;
	}

	public Double getBoundEastLongitude() 
	{
		return boundEastLongitude;
	}
	
	public void setBoundSouthLatitude(final Double boundSouthLatitude)
	{
		this.boundSouthLatitude = boundSouthLatitude;
	}

	public Double getBoundSouthLatitude() 
	{
		return boundSouthLatitude;
	}
	
	public void setBoundWestLongitude(final Double boundWestLongitude)
	{
		this.boundWestLongitude = boundWestLongitude;
	}

	public Double getBoundWestLongitude() 
	{
		return boundWestLongitude;
	}
	
	public void setProduct(final ProductWsDTO product)
	{
		this.product = product;
	}

	public ProductWsDTO getProduct() 
	{
		return product;
	}
	

}