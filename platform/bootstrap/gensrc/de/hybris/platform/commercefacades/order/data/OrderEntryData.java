/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 08-Nov-2021, 4:51:28 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.order.data;

import java.io.Serializable;
import de.hybris.platform.catalog.enums.ProductInfoStatus;
import de.hybris.platform.commercefacades.comment.data.CommentData;
import de.hybris.platform.commercefacades.order.data.ConfigurationInfoData;
import de.hybris.platform.commercefacades.order.data.DeliveryModeData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.selectivecartservices.enums.CartSourceType;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;


import java.util.Objects;
public  class OrderEntryData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>OrderEntryData.entryNumber</code> property defined at extension <code>commercefacades</code>. */
		
	private Integer entryNumber;

	/** <i>Generated property</i> for <code>OrderEntryData.quantity</code> property defined at extension <code>commercefacades</code>. */
		
	private Long quantity;

	/** <i>Generated property</i> for <code>OrderEntryData.basePrice</code> property defined at extension <code>commercefacades</code>. */
		
	private PriceData basePrice;

	/** <i>Generated property</i> for <code>OrderEntryData.totalPrice</code> property defined at extension <code>commercefacades</code>. */
		
	private PriceData totalPrice;

	/** <i>Generated property</i> for <code>OrderEntryData.product</code> property defined at extension <code>commercefacades</code>. */
		
	private ProductData product;

	/** <i>Generated property</i> for <code>OrderEntryData.updateable</code> property defined at extension <code>commercefacades</code>. */
		
	private boolean updateable;

	/** <i>Generated property</i> for <code>OrderEntryData.deliveryMode</code> property defined at extension <code>commercefacades</code>. */
		
	private DeliveryModeData deliveryMode;

	/** <i>Generated property</i> for <code>OrderEntryData.deliveryPointOfService</code> property defined at extension <code>commercefacades</code>. */
		
	private PointOfServiceData deliveryPointOfService;

	/** <i>Generated property</i> for <code>OrderEntryData.entries</code> property defined at extension <code>commercefacades</code>. */
		
	private List<OrderEntryData> entries;

	/** <i>Generated property</i> for <code>OrderEntryData.configurationInfos</code> property defined at extension <code>commercefacades</code>. */
		
	private List<ConfigurationInfoData> configurationInfos;

	/** <i>Generated property</i> for <code>OrderEntryData.statusSummaryMap</code> property defined at extension <code>commercefacades</code>. */
		
	private Map<ProductInfoStatus, Integer> statusSummaryMap;

	/** <i>Generated property</i> for <code>OrderEntryData.entryGroupNumbers</code> property defined at extension <code>commercefacades</code>. */
		
	private Collection<Integer> entryGroupNumbers;

	/** <i>Generated property</i> for <code>OrderEntryData.comments</code> property defined at extension <code>commercefacades</code>. */
		
	private List<CommentData> comments;

	/** <i>Generated property</i> for <code>OrderEntryData.url</code> property defined at extension <code>ordermanagementfacades</code>. */
		
	private String url;

	/** <i>Generated property</i> for <code>OrderEntryData.cancellableQty</code> property defined at extension <code>ordermanagementfacades</code>. */
		
	private long cancellableQty;

	/** <i>Generated property</i> for <code>OrderEntryData.returnableQty</code> property defined at extension <code>ordermanagementfacades</code>. */
		
	private long returnableQty;

	/** <i>Generated property</i> for <code>OrderEntryData.cancelledItemsPrice</code> property defined at extension <code>ordermanagementfacades</code>. */
		
	private PriceData cancelledItemsPrice;

	/** <i>Generated property</i> for <code>OrderEntryData.returnedItemsPrice</code> property defined at extension <code>ordermanagementfacades</code>. */
		
	private PriceData returnedItemsPrice;

	/** <i>Generated property</i> for <code>OrderEntryData.removeable</code> property defined at extension <code>configurablebundlefacades</code>. */
		
	private boolean removeable;

	/** <i>Generated property</i> for <code>OrderEntryData.editable</code> property defined at extension <code>configurablebundlefacades</code>. */
		
	private boolean editable;

	/** <i>Generated property</i> for <code>OrderEntryData.valid</code> property defined at extension <code>configurablebundlefacades</code>. */
		
	private boolean valid;

	/** <i>Generated property</i> for <code>OrderEntryData.addable</code> property defined at extension <code>configurablebundlefacades</code>. */
		
	private boolean addable;

	/** <i>Generated property</i> for <code>OrderEntryData.supportedActions</code> property defined at extension <code>acceleratorfacades</code>. */
		
	private Set<String> supportedActions;

	/** <i>Generated property</i> for <code>OrderEntryData.configurationAttached</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private boolean configurationAttached;

	/** <i>Generated property</i> for <code>OrderEntryData.itemPK</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private String itemPK;

	/** <i>Generated property</i> for <code>OrderEntryData.configurationConsistent</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private boolean configurationConsistent;

	/** <i>Generated property</i> for <code>OrderEntryData.configurationErrorCount</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private int configurationErrorCount;

	/** <i>Generated property</i> for <code>OrderEntryData.addToCartTime</code> property defined at extension <code>selectivecartfacades</code>. */
		
	private Date addToCartTime;

	/** <i>Generated property</i> for <code>OrderEntryData.cartSourceType</code> property defined at extension <code>selectivecartfacades</code>. */
		
	private CartSourceType cartSourceType;
	
	public OrderEntryData()
	{
		// default constructor
	}
	
	public void setEntryNumber(final Integer entryNumber)
	{
		this.entryNumber = entryNumber;
	}

	public Integer getEntryNumber() 
	{
		return entryNumber;
	}
	
	public void setQuantity(final Long quantity)
	{
		this.quantity = quantity;
	}

	public Long getQuantity() 
	{
		return quantity;
	}
	
	public void setBasePrice(final PriceData basePrice)
	{
		this.basePrice = basePrice;
	}

	public PriceData getBasePrice() 
	{
		return basePrice;
	}
	
	public void setTotalPrice(final PriceData totalPrice)
	{
		this.totalPrice = totalPrice;
	}

	public PriceData getTotalPrice() 
	{
		return totalPrice;
	}
	
	public void setProduct(final ProductData product)
	{
		this.product = product;
	}

	public ProductData getProduct() 
	{
		return product;
	}
	
	public void setUpdateable(final boolean updateable)
	{
		this.updateable = updateable;
	}

	public boolean isUpdateable() 
	{
		return updateable;
	}
	
	public void setDeliveryMode(final DeliveryModeData deliveryMode)
	{
		this.deliveryMode = deliveryMode;
	}

	public DeliveryModeData getDeliveryMode() 
	{
		return deliveryMode;
	}
	
	public void setDeliveryPointOfService(final PointOfServiceData deliveryPointOfService)
	{
		this.deliveryPointOfService = deliveryPointOfService;
	}

	public PointOfServiceData getDeliveryPointOfService() 
	{
		return deliveryPointOfService;
	}
	
	public void setEntries(final List<OrderEntryData> entries)
	{
		this.entries = entries;
	}

	public List<OrderEntryData> getEntries() 
	{
		return entries;
	}
	
	public void setConfigurationInfos(final List<ConfigurationInfoData> configurationInfos)
	{
		this.configurationInfos = configurationInfos;
	}

	public List<ConfigurationInfoData> getConfigurationInfos() 
	{
		return configurationInfos;
	}
	
	public void setStatusSummaryMap(final Map<ProductInfoStatus, Integer> statusSummaryMap)
	{
		this.statusSummaryMap = statusSummaryMap;
	}

	public Map<ProductInfoStatus, Integer> getStatusSummaryMap() 
	{
		return statusSummaryMap;
	}
	
	public void setEntryGroupNumbers(final Collection<Integer> entryGroupNumbers)
	{
		this.entryGroupNumbers = entryGroupNumbers;
	}

	public Collection<Integer> getEntryGroupNumbers() 
	{
		return entryGroupNumbers;
	}
	
	public void setComments(final List<CommentData> comments)
	{
		this.comments = comments;
	}

	public List<CommentData> getComments() 
	{
		return comments;
	}
	
	public void setUrl(final String url)
	{
		this.url = url;
	}

	public String getUrl() 
	{
		return url;
	}
	
	public void setCancellableQty(final long cancellableQty)
	{
		this.cancellableQty = cancellableQty;
	}

	public long getCancellableQty() 
	{
		return cancellableQty;
	}
	
	public void setReturnableQty(final long returnableQty)
	{
		this.returnableQty = returnableQty;
	}

	public long getReturnableQty() 
	{
		return returnableQty;
	}
	
	public void setCancelledItemsPrice(final PriceData cancelledItemsPrice)
	{
		this.cancelledItemsPrice = cancelledItemsPrice;
	}

	public PriceData getCancelledItemsPrice() 
	{
		return cancelledItemsPrice;
	}
	
	public void setReturnedItemsPrice(final PriceData returnedItemsPrice)
	{
		this.returnedItemsPrice = returnedItemsPrice;
	}

	public PriceData getReturnedItemsPrice() 
	{
		return returnedItemsPrice;
	}
	
	public void setRemoveable(final boolean removeable)
	{
		this.removeable = removeable;
	}

	public boolean isRemoveable() 
	{
		return removeable;
	}
	
	public void setEditable(final boolean editable)
	{
		this.editable = editable;
	}

	public boolean isEditable() 
	{
		return editable;
	}
	
	public void setValid(final boolean valid)
	{
		this.valid = valid;
	}

	public boolean isValid() 
	{
		return valid;
	}
	
	public void setAddable(final boolean addable)
	{
		this.addable = addable;
	}

	public boolean isAddable() 
	{
		return addable;
	}
	
	public void setSupportedActions(final Set<String> supportedActions)
	{
		this.supportedActions = supportedActions;
	}

	public Set<String> getSupportedActions() 
	{
		return supportedActions;
	}
	
	public void setConfigurationAttached(final boolean configurationAttached)
	{
		this.configurationAttached = configurationAttached;
	}

	public boolean isConfigurationAttached() 
	{
		return configurationAttached;
	}
	
	public void setItemPK(final String itemPK)
	{
		this.itemPK = itemPK;
	}

	public String getItemPK() 
	{
		return itemPK;
	}
	
	public void setConfigurationConsistent(final boolean configurationConsistent)
	{
		this.configurationConsistent = configurationConsistent;
	}

	public boolean isConfigurationConsistent() 
	{
		return configurationConsistent;
	}
	
	public void setConfigurationErrorCount(final int configurationErrorCount)
	{
		this.configurationErrorCount = configurationErrorCount;
	}

	public int getConfigurationErrorCount() 
	{
		return configurationErrorCount;
	}
	
	public void setAddToCartTime(final Date addToCartTime)
	{
		this.addToCartTime = addToCartTime;
	}

	public Date getAddToCartTime() 
	{
		return addToCartTime;
	}
	
	public void setCartSourceType(final CartSourceType cartSourceType)
	{
		this.cartSourceType = cartSourceType;
	}

	public CartSourceType getCartSourceType() 
	{
		return cartSourceType;
	}
	

}