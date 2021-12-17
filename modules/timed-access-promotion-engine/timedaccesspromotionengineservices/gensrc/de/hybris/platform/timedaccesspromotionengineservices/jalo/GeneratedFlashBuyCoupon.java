/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 13-Dec-2021, 3:58:17 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.timedaccesspromotionengineservices.jalo;

import de.hybris.platform.couponservices.jalo.SingleCodeCoupon;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.promotionengineservices.jalo.PromotionSourceRule;
import de.hybris.platform.timedaccesspromotionengineservices.constants.TimedaccesspromotionengineservicesConstants;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.couponservices.jalo.SingleCodeCoupon FlashBuyCoupon}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedFlashBuyCoupon extends SingleCodeCoupon
{
	/** Qualifier of the <code>FlashBuyCoupon.rule</code> attribute **/
	public static final String RULE = "rule";
	/** Qualifier of the <code>FlashBuyCoupon.maxProductQuantityPerOrder</code> attribute **/
	public static final String MAXPRODUCTQUANTITYPERORDER = "maxProductQuantityPerOrder";
	/** Qualifier of the <code>FlashBuyCoupon.product</code> attribute **/
	public static final String PRODUCT = "product";
	/** Qualifier of the <code>FlashBuyCoupon.originalMaxOrderQuantity</code> attribute **/
	public static final String ORIGINALMAXORDERQUANTITY = "originalMaxOrderQuantity";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(SingleCodeCoupon.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(RULE, AttributeMode.INITIAL);
		tmp.put(MAXPRODUCTQUANTITYPERORDER, AttributeMode.INITIAL);
		tmp.put(PRODUCT, AttributeMode.INITIAL);
		tmp.put(ORIGINALMAXORDERQUANTITY, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FlashBuyCoupon.maxProductQuantityPerOrder</code> attribute.
	 * @return the maxProductQuantityPerOrder
	 */
	public Integer getMaxProductQuantityPerOrder(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, MAXPRODUCTQUANTITYPERORDER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FlashBuyCoupon.maxProductQuantityPerOrder</code> attribute.
	 * @return the maxProductQuantityPerOrder
	 */
	public Integer getMaxProductQuantityPerOrder()
	{
		return getMaxProductQuantityPerOrder( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FlashBuyCoupon.maxProductQuantityPerOrder</code> attribute. 
	 * @return the maxProductQuantityPerOrder
	 */
	public int getMaxProductQuantityPerOrderAsPrimitive(final SessionContext ctx)
	{
		Integer value = getMaxProductQuantityPerOrder( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FlashBuyCoupon.maxProductQuantityPerOrder</code> attribute. 
	 * @return the maxProductQuantityPerOrder
	 */
	public int getMaxProductQuantityPerOrderAsPrimitive()
	{
		return getMaxProductQuantityPerOrderAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>FlashBuyCoupon.maxProductQuantityPerOrder</code> attribute. 
	 * @param value the maxProductQuantityPerOrder
	 */
	public void setMaxProductQuantityPerOrder(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, MAXPRODUCTQUANTITYPERORDER,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>FlashBuyCoupon.maxProductQuantityPerOrder</code> attribute. 
	 * @param value the maxProductQuantityPerOrder
	 */
	public void setMaxProductQuantityPerOrder(final Integer value)
	{
		setMaxProductQuantityPerOrder( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>FlashBuyCoupon.maxProductQuantityPerOrder</code> attribute. 
	 * @param value the maxProductQuantityPerOrder
	 */
	public void setMaxProductQuantityPerOrder(final SessionContext ctx, final int value)
	{
		setMaxProductQuantityPerOrder( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>FlashBuyCoupon.maxProductQuantityPerOrder</code> attribute. 
	 * @param value the maxProductQuantityPerOrder
	 */
	public void setMaxProductQuantityPerOrder(final int value)
	{
		setMaxProductQuantityPerOrder( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FlashBuyCoupon.originalMaxOrderQuantity</code> attribute.
	 * @return the originalMaxOrderQuantity
	 */
	public Integer getOriginalMaxOrderQuantity(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, ORIGINALMAXORDERQUANTITY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FlashBuyCoupon.originalMaxOrderQuantity</code> attribute.
	 * @return the originalMaxOrderQuantity
	 */
	public Integer getOriginalMaxOrderQuantity()
	{
		return getOriginalMaxOrderQuantity( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FlashBuyCoupon.originalMaxOrderQuantity</code> attribute. 
	 * @return the originalMaxOrderQuantity
	 */
	public int getOriginalMaxOrderQuantityAsPrimitive(final SessionContext ctx)
	{
		Integer value = getOriginalMaxOrderQuantity( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FlashBuyCoupon.originalMaxOrderQuantity</code> attribute. 
	 * @return the originalMaxOrderQuantity
	 */
	public int getOriginalMaxOrderQuantityAsPrimitive()
	{
		return getOriginalMaxOrderQuantityAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>FlashBuyCoupon.originalMaxOrderQuantity</code> attribute. 
	 * @param value the originalMaxOrderQuantity
	 */
	public void setOriginalMaxOrderQuantity(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, ORIGINALMAXORDERQUANTITY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>FlashBuyCoupon.originalMaxOrderQuantity</code> attribute. 
	 * @param value the originalMaxOrderQuantity
	 */
	public void setOriginalMaxOrderQuantity(final Integer value)
	{
		setOriginalMaxOrderQuantity( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>FlashBuyCoupon.originalMaxOrderQuantity</code> attribute. 
	 * @param value the originalMaxOrderQuantity
	 */
	public void setOriginalMaxOrderQuantity(final SessionContext ctx, final int value)
	{
		setOriginalMaxOrderQuantity( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>FlashBuyCoupon.originalMaxOrderQuantity</code> attribute. 
	 * @param value the originalMaxOrderQuantity
	 */
	public void setOriginalMaxOrderQuantity(final int value)
	{
		setOriginalMaxOrderQuantity( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FlashBuyCoupon.product</code> attribute.
	 * @return the product
	 */
	public Product getProduct(final SessionContext ctx)
	{
		return (Product)getProperty( ctx, PRODUCT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FlashBuyCoupon.product</code> attribute.
	 * @return the product
	 */
	public Product getProduct()
	{
		return getProduct( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>FlashBuyCoupon.product</code> attribute. 
	 * @param value the product
	 */
	public void setProduct(final SessionContext ctx, final Product value)
	{
		setProperty(ctx, PRODUCT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>FlashBuyCoupon.product</code> attribute. 
	 * @param value the product
	 */
	public void setProduct(final Product value)
	{
		setProduct( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FlashBuyCoupon.rule</code> attribute.
	 * @return the rule
	 */
	public PromotionSourceRule getRule(final SessionContext ctx)
	{
		return (PromotionSourceRule)getProperty( ctx, RULE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FlashBuyCoupon.rule</code> attribute.
	 * @return the rule
	 */
	public PromotionSourceRule getRule()
	{
		return getRule( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>FlashBuyCoupon.rule</code> attribute. 
	 * @param value the rule
	 */
	public void setRule(final SessionContext ctx, final PromotionSourceRule value)
	{
		setProperty(ctx, RULE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>FlashBuyCoupon.rule</code> attribute. 
	 * @param value the rule
	 */
	public void setRule(final PromotionSourceRule value)
	{
		setRule( getSession().getSessionContext(), value );
	}
	
}
