/*
* ----------------------------------------------------------------
* --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
* --- Generated at 11-Dec-2021, 12:33:00 AM
* ----------------------------------------------------------------
*
* Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
*/
package de.hybris.platform.ruleengineservices.rao;

import java.util.Objects;
import de.hybris.platform.ruleengineservices.rao.AbstractActionedRAO;
import de.hybris.platform.ruleengineservices.rao.AbstractOrderRAO;
import de.hybris.platform.ruleengineservices.rao.DiscountValueRAO;
import de.hybris.platform.ruleengineservices.rao.ProductRAO;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public  class OrderEntryRAO extends AbstractActionedRAO 
{

	/** <i>Generated property</i> for <code>OrderEntryRAO.entryNumber</code> property defined at extension <code>ruleengineservices</code>. */
	private Integer entryNumber;
	/** <i>Generated property</i> for <code>OrderEntryRAO.quantity</code> property defined at extension <code>ruleengineservices</code>. */
	private int quantity;
	/** <i>Generated property</i> for <code>OrderEntryRAO.basePrice</code> property defined at extension <code>ruleengineservices</code>. */
	private BigDecimal basePrice;
	/** <i>Generated property</i> for <code>OrderEntryRAO.price</code> property defined at extension <code>ruleengineservices</code>. */
	private BigDecimal price;
	/** <i>Generated property</i> for <code>OrderEntryRAO.totalPrice</code> property defined at extension <code>ruleengineservices</code>. */
	private BigDecimal totalPrice;
	/** <i>Generated property</i> for <code>OrderEntryRAO.currencyIsoCode</code> property defined at extension <code>ruleengineservices</code>. */
	private String currencyIsoCode;
	/** <i>Generated property</i> for <code>OrderEntryRAO.order</code> property defined at extension <code>ruleengineservices</code>. */
	private AbstractOrderRAO order;
	/** <i>Generated property</i> for <code>OrderEntryRAO.product</code> property defined at extension <code>ruleengineservices</code>. */
	private ProductRAO product;
	/** <i>Generated property</i> for <code>OrderEntryRAO.discountValues</code> property defined at extension <code>ruleengineservices</code>. */
	private List<DiscountValueRAO> discountValues;
	/** <i>Generated property</i> for <code>OrderEntryRAO.entryGroupNumbers</code> property defined at extension <code>ruleengineservices</code>. */
	private List<Integer> entryGroupNumbers;
	/** <i>Generated property</i> for <code>OrderEntryRAO.giveAway</code> property defined at extension <code>ruleengineservices</code>. */
	private boolean giveAway;
	/** <i>Generated property</i> for <code>OrderEntryRAO.productCode</code> property defined at extension <code>ruleengineservices</code>. */
	private String productCode;
	/** <i>Generated property</i> for <code>OrderEntryRAO.categoryCodes</code> property defined at extension <code>ruleengineservices</code>. */
	private Set<String> categoryCodes;
	/** <i>Generated property</i> for <code>OrderEntryRAO.baseProductCodes</code> property defined at extension <code>ruleengineservices</code>. */
	private Set<String> baseProductCodes;
	/** <i>Generated property</i> for <code>OrderEntryRAO.availableQuantity</code> property defined at extension <code>ruleengineservices</code>. */
	private int availableQuantity;
		
	public OrderEntryRAO()
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
		
		public void setQuantity(final int quantity)
	{
		this.quantity = quantity;
	}
		public int getQuantity() 
	{
		return quantity;
	}
		
		public void setBasePrice(final BigDecimal basePrice)
	{
		this.basePrice = basePrice;
	}
		public BigDecimal getBasePrice() 
	{
		return basePrice;
	}
		
		public void setPrice(final BigDecimal price)
	{
		this.price = price;
	}
		public BigDecimal getPrice() 
	{
		return price;
	}
		
		public void setTotalPrice(final BigDecimal totalPrice)
	{
		this.totalPrice = totalPrice;
	}
		public BigDecimal getTotalPrice() 
	{
		return totalPrice;
	}
		
		public void setCurrencyIsoCode(final String currencyIsoCode)
	{
		this.currencyIsoCode = currencyIsoCode;
	}
		public String getCurrencyIsoCode() 
	{
		return currencyIsoCode;
	}
		
		public void setOrder(final AbstractOrderRAO order)
	{
		this.order = order;
	}
		public AbstractOrderRAO getOrder() 
	{
		return order;
	}
		
		/**
	* @deprecated not used anymore, instead productCode is used (and categoryCodes and baseProductCodes for CategoryRAO)
	*/
	@Deprecated(since = "2005", forRemoval = true)
	public void setProduct(final ProductRAO product)
	{
		this.product = product;
	}
	/**
	* @deprecated not used anymore, instead productCode is used (and categoryCodes and baseProductCodes for CategoryRAO)
	*/
	@Deprecated(since = "2005", forRemoval = true)
		public ProductRAO getProduct() 
	{
		return product;
	}
		
		public void setDiscountValues(final List<DiscountValueRAO> discountValues)
	{
		this.discountValues = discountValues;
	}
		public List<DiscountValueRAO> getDiscountValues() 
	{
		return discountValues;
	}
		
		public void setEntryGroupNumbers(final List<Integer> entryGroupNumbers)
	{
		this.entryGroupNumbers = entryGroupNumbers;
	}
		public List<Integer> getEntryGroupNumbers() 
	{
		return entryGroupNumbers;
	}
		
		public void setGiveAway(final boolean giveAway)
	{
		this.giveAway = giveAway;
	}
		public boolean isGiveAway() 
	{
		return giveAway;
	}
		
		public void setProductCode(final String productCode)
	{
		this.productCode = productCode;
	}
		public String getProductCode() 
	{
		return productCode;
	}
		
		public void setCategoryCodes(final Set<String> categoryCodes)
	{
		this.categoryCodes = categoryCodes;
	}
		public Set<String> getCategoryCodes() 
	{
		return categoryCodes;
	}
		
		public void setBaseProductCodes(final Set<String> baseProductCodes)
	{
		this.baseProductCodes = baseProductCodes;
	}
		public Set<String> getBaseProductCodes() 
	{
		return baseProductCodes;
	}
		
		public void setAvailableQuantity(final int availableQuantity)
	{
		this.availableQuantity = availableQuantity;
	}
		public int getAvailableQuantity() 
	{
		return availableQuantity;
	}
		
	
		@Override
	public boolean equals(final Object o)
	{
		
		if (o == null) return false;
		if (o == this) return true;

		if (getClass() != o.getClass()) return false;

		final OrderEntryRAO other = (OrderEntryRAO) o;
		return				Objects.equals(getEntryNumber(), other.getEntryNumber())
 &&  			Objects.equals(getOrder(), other.getOrder())
 &&  			Objects.equals(getProductCode(), other.getProductCode())
  ;
	}

	@Override
	public int hashCode()
	{
		int result = 1;
		Object attribute;

				attribute = entryNumber;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());
				attribute = order;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());
				attribute = productCode;
		result = 31 * result + (attribute == null ? 0 : attribute.hashCode());
		
		return result;
	}
	}
