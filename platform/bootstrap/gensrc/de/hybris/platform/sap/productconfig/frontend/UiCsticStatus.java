/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:32:59 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.productconfig.frontend;

import java.io.Serializable;
import de.hybris.platform.sap.productconfig.frontend.UiCsticValueStatus;
import de.hybris.platform.sap.productconfig.frontend.UiPromoMessageStatus;
import java.util.List;


import java.util.Objects;
public  class UiCsticStatus  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>UiCsticStatus.id</code> property defined at extension <code>ysapproductconfigaddon</code>. */
		
	private String id;

	/** <i>Generated property</i> for <code>UiCsticStatus.showFullLongText</code> property defined at extension <code>ysapproductconfigaddon</code>. */
		
	private boolean showFullLongText;

	/** <i>Generated property</i> for <code>UiCsticStatus.promoMessages</code> property defined at extension <code>ysapproductconfigaddon</code>. */
		
	private List<UiPromoMessageStatus> promoMessages;

	/** <i>Generated property</i> for <code>UiCsticStatus.csticValuess</code> property defined at extension <code>ysapproductconfigaddon</code>. */
		
	private List<UiCsticValueStatus> csticValuess;
	
	public UiCsticStatus()
	{
		// default constructor
	}
	
	public void setId(final String id)
	{
		this.id = id;
	}

	public String getId() 
	{
		return id;
	}
	
	public void setShowFullLongText(final boolean showFullLongText)
	{
		this.showFullLongText = showFullLongText;
	}

	public boolean isShowFullLongText() 
	{
		return showFullLongText;
	}
	
	public void setPromoMessages(final List<UiPromoMessageStatus> promoMessages)
	{
		this.promoMessages = promoMessages;
	}

	public List<UiPromoMessageStatus> getPromoMessages() 
	{
		return promoMessages;
	}
	
	public void setCsticValuess(final List<UiCsticValueStatus> csticValuess)
	{
		this.csticValuess = csticValuess;
	}

	public List<UiCsticValueStatus> getCsticValuess() 
	{
		return csticValuess;
	}
	

}