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
import de.hybris.platform.commercewebservicescommons.dto.search.pagedata.PaginationWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.search.pagedata.SortWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.store.PointOfServiceWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


import java.util.Objects;
/**
 * Representation of a Store finder search page
 */
@ApiModel(value="StoreFinderSearchPage", description="Representation of a Store finder search page")
public  class StoreFinderSearchPageWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** List of stores<br/><br/><i>Generated property</i> for <code>StoreFinderSearchPageWsDTO.stores</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="stores", value="List of stores") 	
	private List<PointOfServiceWsDTO> stores;

	/** List of sortings<br/><br/><i>Generated property</i> for <code>StoreFinderSearchPageWsDTO.sorts</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="sorts", value="List of sortings") 	
	private List<SortWsDTO> sorts;

	/** Pagination<br/><br/><i>Generated property</i> for <code>StoreFinderSearchPageWsDTO.pagination</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="pagination", value="Pagination") 	
	private PaginationWsDTO pagination;

	/** Location text<br/><br/><i>Generated property</i> for <code>StoreFinderSearchPageWsDTO.locationText</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="locationText", value="Location text") 	
	private String locationText;

	/** Source latitude<br/><br/><i>Generated property</i> for <code>StoreFinderSearchPageWsDTO.sourceLatitude</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="sourceLatitude", value="Source latitude") 	
	private Double sourceLatitude;

	/** Source longitude<br/><br/><i>Generated property</i> for <code>StoreFinderSearchPageWsDTO.sourceLongitude</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="sourceLongitude", value="Source longitude") 	
	private Double sourceLongitude;

	/** Bound north latitude<br/><br/><i>Generated property</i> for <code>StoreFinderSearchPageWsDTO.boundNorthLatitude</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="boundNorthLatitude", value="Bound north latitude") 	
	private Double boundNorthLatitude;

	/** Bound east longitude<br/><br/><i>Generated property</i> for <code>StoreFinderSearchPageWsDTO.boundEastLongitude</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="boundEastLongitude", value="Bound east longitude") 	
	private Double boundEastLongitude;

	/** Bound south latitude<br/><br/><i>Generated property</i> for <code>StoreFinderSearchPageWsDTO.boundSouthLatitude</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="boundSouthLatitude", value="Bound south latitude") 	
	private Double boundSouthLatitude;

	/** Bound west longitude<br/><br/><i>Generated property</i> for <code>StoreFinderSearchPageWsDTO.boundWestLongitude</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="boundWestLongitude", value="Bound west longitude") 	
	private Double boundWestLongitude;
	
	public StoreFinderSearchPageWsDTO()
	{
		// default constructor
	}
	
	public void setStores(final List<PointOfServiceWsDTO> stores)
	{
		this.stores = stores;
	}

	public List<PointOfServiceWsDTO> getStores() 
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
	

}