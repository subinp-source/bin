/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:45 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicescommons.dto.order;

import java.io.Serializable;
import de.hybris.platform.commercewebservicescommons.dto.order.ConsignmentEntryWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.store.PointOfServiceWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.user.AddressWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import java.util.List;


import java.util.Objects;
/**
 * Representation of a Consignment
 */
@ApiModel(value="Consignment", description="Representation of a Consignment")
public  class ConsignmentWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** Consignment code<br/><br/><i>Generated property</i> for <code>ConsignmentWsDTO.code</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="code", value="Consignment code") 	
	private String code;

	/** Consignment tracking identifier<br/><br/><i>Generated property</i> for <code>ConsignmentWsDTO.trackingID</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="trackingID", value="Consignment tracking identifier") 	
	private String trackingID;

	/** Consignment status<br/><br/><i>Generated property</i> for <code>ConsignmentWsDTO.status</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="status", value="Consignment status") 	
	private String status;

	/** Consignment status display<br/><br/><i>Generated property</i> for <code>ConsignmentWsDTO.statusDisplay</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="statusDisplay", value="Consignment status display") 	
	private String statusDisplay;

	/** Consignment status date<br/><br/><i>Generated property</i> for <code>ConsignmentWsDTO.statusDate</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="statusDate", value="Consignment status date") 	
	private Date statusDate;

	/** List of consignment entries<br/><br/><i>Generated property</i> for <code>ConsignmentWsDTO.entries</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="entries", value="List of consignment entries") 	
	private List<ConsignmentEntryWsDTO> entries;

	/** Shipping address<br/><br/><i>Generated property</i> for <code>ConsignmentWsDTO.shippingAddress</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="shippingAddress", value="Shipping address") 	
	private AddressWsDTO shippingAddress;

	/** Delivery point of service<br/><br/><i>Generated property</i> for <code>ConsignmentWsDTO.deliveryPointOfService</code> property defined at extension <code>commercewebservicescommons</code>. */
	@ApiModelProperty(name="deliveryPointOfService", value="Delivery point of service") 	
	private PointOfServiceWsDTO deliveryPointOfService;
	
	public ConsignmentWsDTO()
	{
		// default constructor
	}
	
	public void setCode(final String code)
	{
		this.code = code;
	}

	public String getCode() 
	{
		return code;
	}
	
	public void setTrackingID(final String trackingID)
	{
		this.trackingID = trackingID;
	}

	public String getTrackingID() 
	{
		return trackingID;
	}
	
	public void setStatus(final String status)
	{
		this.status = status;
	}

	public String getStatus() 
	{
		return status;
	}
	
	public void setStatusDisplay(final String statusDisplay)
	{
		this.statusDisplay = statusDisplay;
	}

	public String getStatusDisplay() 
	{
		return statusDisplay;
	}
	
	public void setStatusDate(final Date statusDate)
	{
		this.statusDate = statusDate;
	}

	public Date getStatusDate() 
	{
		return statusDate;
	}
	
	public void setEntries(final List<ConsignmentEntryWsDTO> entries)
	{
		this.entries = entries;
	}

	public List<ConsignmentEntryWsDTO> getEntries() 
	{
		return entries;
	}
	
	public void setShippingAddress(final AddressWsDTO shippingAddress)
	{
		this.shippingAddress = shippingAddress;
	}

	public AddressWsDTO getShippingAddress() 
	{
		return shippingAddress;
	}
	
	public void setDeliveryPointOfService(final PointOfServiceWsDTO deliveryPointOfService)
	{
		this.deliveryPointOfService = deliveryPointOfService;
	}

	public PointOfServiceWsDTO getDeliveryPointOfService() 
	{
		return deliveryPointOfService;
	}
	

}