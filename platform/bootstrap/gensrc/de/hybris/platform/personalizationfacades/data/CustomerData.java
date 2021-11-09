/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:28 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.personalizationfacades.data;

import java.io.Serializable;
import de.hybris.platform.personalizationfacades.data.CustomerSegmentationData;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


import java.util.Objects;
/**
 * Customer details
 */
@ApiModel(value="customer", description="Customer details")
public  class CustomerData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** ID of the customer<br/><br/><i>Generated property</i> for <code>CustomerData.uid</code> property defined at extension <code>personalizationfacades</code>. */
	@ApiModelProperty(name="uid", value="ID of the customer") 	
	private String uid;

	/** List of customer's segmentation details<br/><br/><i>Generated property</i> for <code>CustomerData.segmentLinks</code> property defined at extension <code>personalizationfacades</code>. */
	@ApiModelProperty(name="segmentLinks", value="List of customer's segmentation details") 	
	private List<CustomerSegmentationData> segmentLinks;
	
	public CustomerData()
	{
		// default constructor
	}
	
	public void setUid(final String uid)
	{
		this.uid = uid;
	}

	public String getUid() 
	{
		return uid;
	}
	
	public void setSegmentLinks(final List<CustomerSegmentationData> segmentLinks)
	{
		this.segmentLinks = segmentLinks;
	}

	public List<CustomerSegmentationData> getSegmentLinks() 
	{
		return segmentLinks;
	}
	

}