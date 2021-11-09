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
import de.hybris.platform.personalizationfacades.data.SegmentTriggerData;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


import java.util.Objects;
/**
 * Segment details
 */
@ApiModel(value="segment", description="Segment details")
public  class SegmentData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** Segment code<br/><br/><i>Generated property</i> for <code>SegmentData.code</code> property defined at extension <code>personalizationfacades</code>. */
	@ApiModelProperty(name="code", value="Segment code") 	
	private String code;

	/** Segment description<br/><br/><i>Generated property</i> for <code>SegmentData.description</code> property defined at extension <code>personalizationfacades</code>. */
	@ApiModelProperty(name="description", value="Segment description") 	
	private String description;

	/** Customer segmentation details<br/><br/><i>Generated property</i> for <code>SegmentData.customerLinks</code> property defined at extension <code>personalizationfacades</code>. */
	@ApiModelProperty(name="customerLinks", value="Customer segmentation details") 	
	private List<CustomerSegmentationData> customerLinks;

	/** Segment trigger details<br/><br/><i>Generated property</i> for <code>SegmentData.segmentTriggers</code> property defined at extension <code>personalizationfacades</code>. */
	@ApiModelProperty(name="segmentTriggers", value="Segment trigger details") 	
	private List<SegmentTriggerData> segmentTriggers;
	
	public SegmentData()
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
	
	public void setDescription(final String description)
	{
		this.description = description;
	}

	public String getDescription() 
	{
		return description;
	}
	
	public void setCustomerLinks(final List<CustomerSegmentationData> customerLinks)
	{
		this.customerLinks = customerLinks;
	}

	public List<CustomerSegmentationData> getCustomerLinks() 
	{
		return customerLinks;
	}
	
	public void setSegmentTriggers(final List<SegmentTriggerData> segmentTriggers)
	{
		this.segmentTriggers = segmentTriggers;
	}

	public List<SegmentTriggerData> getSegmentTriggers() 
	{
		return segmentTriggers;
	}
	

}