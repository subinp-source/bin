/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:28 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.productconfig.facades;

import java.io.Serializable;
import de.hybris.platform.sap.productconfig.facades.ProductConfigMessageUISeverity;
import de.hybris.platform.sap.productconfig.runtime.interf.model.ProductConfigMessagePromoType;


import java.util.Objects;
public  class ProductConfigMessageData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>ProductConfigMessageData.message</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private String message;

	/** <i>Generated property</i> for <code>ProductConfigMessageData.severity</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private ProductConfigMessageUISeverity severity;

	/** <i>Generated property</i> for <code>ProductConfigMessageData.promoType</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private ProductConfigMessagePromoType promoType;

	/** <i>Generated property</i> for <code>ProductConfigMessageData.extendedMessage</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private String extendedMessage;

	/** <i>Generated property</i> for <code>ProductConfigMessageData.endDate</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private String endDate;

	/** <i>Generated property</i> for <code>ProductConfigMessageData.showExtendedMessage</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private boolean showExtendedMessage;
	
	public ProductConfigMessageData()
	{
		// default constructor
	}
	
	public void setMessage(final String message)
	{
		this.message = message;
	}

	public String getMessage() 
	{
		return message;
	}
	
	public void setSeverity(final ProductConfigMessageUISeverity severity)
	{
		this.severity = severity;
	}

	public ProductConfigMessageUISeverity getSeverity() 
	{
		return severity;
	}
	
	public void setPromoType(final ProductConfigMessagePromoType promoType)
	{
		this.promoType = promoType;
	}

	public ProductConfigMessagePromoType getPromoType() 
	{
		return promoType;
	}
	
	public void setExtendedMessage(final String extendedMessage)
	{
		this.extendedMessage = extendedMessage;
	}

	public String getExtendedMessage() 
	{
		return extendedMessage;
	}
	
	public void setEndDate(final String endDate)
	{
		this.endDate = endDate;
	}

	public String getEndDate() 
	{
		return endDate;
	}
	
	public void setShowExtendedMessage(final boolean showExtendedMessage)
	{
		this.showExtendedMessage = showExtendedMessage;
	}

	public boolean isShowExtendedMessage() 
	{
		return showExtendedMessage;
	}
	

}