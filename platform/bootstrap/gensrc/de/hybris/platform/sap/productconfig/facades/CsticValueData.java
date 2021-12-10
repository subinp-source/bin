/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 11-Dec-2021, 12:33:00 AM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.productconfig.facades;

import java.io.Serializable;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.sap.productconfig.facades.ProductConfigMessageData;
import java.util.List;


import java.util.Objects;
public  class CsticValueData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>CsticValueData.key</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private String key;

	/** <i>Generated property</i> for <code>CsticValueData.selected</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private boolean selected;

	/** <i>Generated property</i> for <code>CsticValueData.readonly</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private boolean readonly;

	/** <i>Generated property</i> for <code>CsticValueData.name</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private String name;

	/** <i>Generated property</i> for <code>CsticValueData.langdepname</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private String langdepname;

	/** <i>Generated property</i> for <code>CsticValueData.longText</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private String longText;

	/** <i>Generated property</i> for <code>CsticValueData.longTextHTMLFormat</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private boolean longTextHTMLFormat;

	/** <i>Generated property</i> for <code>CsticValueData.deltaPrice</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private PriceData deltaPrice;

	/** <i>Generated property</i> for <code>CsticValueData.price</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private PriceData price;

	/** <i>Generated property</i> for <code>CsticValueData.obsoletePrice</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private PriceData obsoletePrice;

	/** <i>Generated property</i> for <code>CsticValueData.showDeltaPrice</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private boolean showDeltaPrice;

	/** <i>Generated property</i> for <code>CsticValueData.media</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private List<ImageData> media;

	/** <i>Generated property</i> for <code>CsticValueData.messages</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private List<ProductConfigMessageData> messages;
	
	public CsticValueData()
	{
		// default constructor
	}
	
	public void setKey(final String key)
	{
		this.key = key;
	}

	public String getKey() 
	{
		return key;
	}
	
	public void setSelected(final boolean selected)
	{
		this.selected = selected;
	}

	public boolean isSelected() 
	{
		return selected;
	}
	
	public void setReadonly(final boolean readonly)
	{
		this.readonly = readonly;
	}

	public boolean isReadonly() 
	{
		return readonly;
	}
	
	public void setName(final String name)
	{
		this.name = name;
	}

	public String getName() 
	{
		return name;
	}
	
	public void setLangdepname(final String langdepname)
	{
		this.langdepname = langdepname;
	}

	public String getLangdepname() 
	{
		return langdepname;
	}
	
	public void setLongText(final String longText)
	{
		this.longText = longText;
	}

	public String getLongText() 
	{
		return longText;
	}
	
	public void setLongTextHTMLFormat(final boolean longTextHTMLFormat)
	{
		this.longTextHTMLFormat = longTextHTMLFormat;
	}

	public boolean isLongTextHTMLFormat() 
	{
		return longTextHTMLFormat;
	}
	
	/**
	 * @deprecated Please use price and showDeltaPrice
	 */
	@Deprecated(since = "6.6", forRemoval = true)
	public void setDeltaPrice(final PriceData deltaPrice)
	{
		this.deltaPrice = deltaPrice;
	}

	/**
	 * @deprecated Please use price and showDeltaPrice
	 */
	@Deprecated(since = "6.6", forRemoval = true)
	public PriceData getDeltaPrice() 
	{
		return deltaPrice;
	}
	
	public void setPrice(final PriceData price)
	{
		this.price = price;
	}

	public PriceData getPrice() 
	{
		return price;
	}
	
	public void setObsoletePrice(final PriceData obsoletePrice)
	{
		this.obsoletePrice = obsoletePrice;
	}

	public PriceData getObsoletePrice() 
	{
		return obsoletePrice;
	}
	
	public void setShowDeltaPrice(final boolean showDeltaPrice)
	{
		this.showDeltaPrice = showDeltaPrice;
	}

	public boolean isShowDeltaPrice() 
	{
		return showDeltaPrice;
	}
	
	public void setMedia(final List<ImageData> media)
	{
		this.media = media;
	}

	public List<ImageData> getMedia() 
	{
		return media;
	}
	
	public void setMessages(final List<ProductConfigMessageData> messages)
	{
		this.messages = messages;
	}

	public List<ProductConfigMessageData> getMessages() 
	{
		return messages;
	}
	

}