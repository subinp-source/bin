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
import de.hybris.platform.commercewebservicescommons.dto.product.ImageWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.store.GeoPointWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.store.OpeningScheduleWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.user.AddressWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Collection;
import java.util.Map;


import java.util.Objects;
/**
 * Representation of a Point of service
 */
@ApiModel(value="PointOfService", description="Representation of a Point of service")
public  class PointOfServiceWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** Name of the point of service<br/><br/><i>Generated property</i> for <code>PointOfServiceWsDTO.name</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="name", value="Name of the point of service") 	
	private String name;

	/** Display name of the point of service<br/><br/><i>Generated property</i> for <code>PointOfServiceWsDTO.displayName</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="displayName", value="Display name of the point of service") 	
	private String displayName;

	/** Url address of the point of service<br/><br/><i>Generated property</i> for <code>PointOfServiceWsDTO.url</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="url", value="Url address of the point of service") 	
	private String url;

	/** Description of the point of service<br/><br/><i>Generated property</i> for <code>PointOfServiceWsDTO.description</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="description", value="Description of the point of service") 	
	private String description;

	/** Opening hours of point of service<br/><br/><i>Generated property</i> for <code>PointOfServiceWsDTO.openingHours</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="openingHours", value="Opening hours of point of service") 	
	private OpeningScheduleWsDTO openingHours;

	/** Store content of given point of service<br/><br/><i>Generated property</i> for <code>PointOfServiceWsDTO.storeContent</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="storeContent", value="Store content of given point of service") 	
	private String storeContent;

	/** List of features for a given point of service<br/><br/><i>Generated property</i> for <code>PointOfServiceWsDTO.features</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="features", value="List of features for a given point of service") 	
	private Map<String, String> features;

	/** Geopoint localization info about point of service<br/><br/><i>Generated property</i> for <code>PointOfServiceWsDTO.geoPoint</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="geoPoint", value="Geopoint localization info about point of service") 	
	private GeoPointWsDTO geoPoint;

	/** Distance to the point of service as text value<br/><br/><i>Generated property</i> for <code>PointOfServiceWsDTO.formattedDistance</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="formattedDistance", value="Distance to the point of service as text value") 	
	private String formattedDistance;

	/** Distance to the point of service as number value<br/><br/><i>Generated property</i> for <code>PointOfServiceWsDTO.distanceKm</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="distanceKm", value="Distance to the point of service as number value") 	
	private Double distanceKm;

	/** Image associated with the point of service<br/><br/><i>Generated property</i> for <code>PointOfServiceWsDTO.mapIcon</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="mapIcon", value="Image associated with the point of service") 	
	private ImageWsDTO mapIcon;

	/** Address information of point of service<br/><br/><i>Generated property</i> for <code>PointOfServiceWsDTO.address</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="address", value="Address information of point of service") 	
	private AddressWsDTO address;

	/** Collection of images associated with a point of service<br/><br/><i>Generated property</i> for <code>PointOfServiceWsDTO.storeImages</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="storeImages", value="Collection of images associated with a point of service") 	
	private Collection<ImageWsDTO> storeImages;
	
	public PointOfServiceWsDTO()
	{
		// default constructor
	}
	
	public void setName(final String name)
	{
		this.name = name;
	}

	public String getName() 
	{
		return name;
	}
	
	public void setDisplayName(final String displayName)
	{
		this.displayName = displayName;
	}

	public String getDisplayName() 
	{
		return displayName;
	}
	
	public void setUrl(final String url)
	{
		this.url = url;
	}

	public String getUrl() 
	{
		return url;
	}
	
	public void setDescription(final String description)
	{
		this.description = description;
	}

	public String getDescription() 
	{
		return description;
	}
	
	public void setOpeningHours(final OpeningScheduleWsDTO openingHours)
	{
		this.openingHours = openingHours;
	}

	public OpeningScheduleWsDTO getOpeningHours() 
	{
		return openingHours;
	}
	
	public void setStoreContent(final String storeContent)
	{
		this.storeContent = storeContent;
	}

	public String getStoreContent() 
	{
		return storeContent;
	}
	
	public void setFeatures(final Map<String, String> features)
	{
		this.features = features;
	}

	public Map<String, String> getFeatures() 
	{
		return features;
	}
	
	public void setGeoPoint(final GeoPointWsDTO geoPoint)
	{
		this.geoPoint = geoPoint;
	}

	public GeoPointWsDTO getGeoPoint() 
	{
		return geoPoint;
	}
	
	public void setFormattedDistance(final String formattedDistance)
	{
		this.formattedDistance = formattedDistance;
	}

	public String getFormattedDistance() 
	{
		return formattedDistance;
	}
	
	public void setDistanceKm(final Double distanceKm)
	{
		this.distanceKm = distanceKm;
	}

	public Double getDistanceKm() 
	{
		return distanceKm;
	}
	
	public void setMapIcon(final ImageWsDTO mapIcon)
	{
		this.mapIcon = mapIcon;
	}

	public ImageWsDTO getMapIcon() 
	{
		return mapIcon;
	}
	
	public void setAddress(final AddressWsDTO address)
	{
		this.address = address;
	}

	public AddressWsDTO getAddress() 
	{
		return address;
	}
	
	public void setStoreImages(final Collection<ImageWsDTO> storeImages)
	{
		this.storeImages = storeImages;
	}

	public Collection<ImageWsDTO> getStoreImages() 
	{
		return storeImages;
	}
	

}