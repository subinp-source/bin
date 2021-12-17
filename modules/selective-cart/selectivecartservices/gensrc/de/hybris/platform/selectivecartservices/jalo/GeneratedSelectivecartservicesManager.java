/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 13-Dec-2021, 3:58:17 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.selectivecartservices.jalo;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.order.Cart;
import de.hybris.platform.selectivecartservices.constants.SelectivecartservicesConstants;
import de.hybris.platform.wishlist2.jalo.Wishlist2Entry;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type <code>SelectivecartservicesManager</code>.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedSelectivecartservicesManager extends Extension
{
	protected static final Map<String, Map<String, AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, Map<String, AttributeMode>> ttmp = new HashMap();
		Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put("quantity", AttributeMode.INITIAL);
		tmp.put("addToCartTime", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.wishlist2.jalo.Wishlist2Entry", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("addToCartTime", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.jalo.order.AbstractOrderEntry", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("visible", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.jalo.order.Cart", Collections.unmodifiableMap(tmp));
		DEFAULT_INITIAL_ATTRIBUTES = ttmp;
	}
	@Override
	public Map<String, AttributeMode> getDefaultAttributeModes(final Class<? extends Item> itemClass)
	{
		Map<String, AttributeMode> ret = new HashMap<>();
		final Map<String, AttributeMode> attr = DEFAULT_INITIAL_ATTRIBUTES.get(itemClass.getName());
		if (attr != null)
		{
			ret.putAll(attr);
		}
		return ret;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Wishlist2Entry.addToCartTime</code> attribute.
	 * @return the addToCartTime - The time when item is added to cart.
	 */
	public Date getAddToCartTime(final SessionContext ctx, final Wishlist2Entry item)
	{
		return (Date)item.getProperty( ctx, SelectivecartservicesConstants.Attributes.Wishlist2Entry.ADDTOCARTTIME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Wishlist2Entry.addToCartTime</code> attribute.
	 * @return the addToCartTime - The time when item is added to cart.
	 */
	public Date getAddToCartTime(final Wishlist2Entry item)
	{
		return getAddToCartTime( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Wishlist2Entry.addToCartTime</code> attribute. 
	 * @param value the addToCartTime - The time when item is added to cart.
	 */
	public void setAddToCartTime(final SessionContext ctx, final Wishlist2Entry item, final Date value)
	{
		item.setProperty(ctx, SelectivecartservicesConstants.Attributes.Wishlist2Entry.ADDTOCARTTIME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Wishlist2Entry.addToCartTime</code> attribute. 
	 * @param value the addToCartTime - The time when item is added to cart.
	 */
	public void setAddToCartTime(final Wishlist2Entry item, final Date value)
	{
		setAddToCartTime( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.addToCartTime</code> attribute.
	 * @return the addToCartTime - The time when item is added to cart.
	 */
	public Date getAddToCartTime(final SessionContext ctx, final AbstractOrderEntry item)
	{
		return (Date)item.getProperty( ctx, SelectivecartservicesConstants.Attributes.AbstractOrderEntry.ADDTOCARTTIME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.addToCartTime</code> attribute.
	 * @return the addToCartTime - The time when item is added to cart.
	 */
	public Date getAddToCartTime(final AbstractOrderEntry item)
	{
		return getAddToCartTime( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.addToCartTime</code> attribute. 
	 * @param value the addToCartTime - The time when item is added to cart.
	 */
	public void setAddToCartTime(final SessionContext ctx, final AbstractOrderEntry item, final Date value)
	{
		item.setProperty(ctx, SelectivecartservicesConstants.Attributes.AbstractOrderEntry.ADDTOCARTTIME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.addToCartTime</code> attribute. 
	 * @param value the addToCartTime - The time when item is added to cart.
	 */
	public void setAddToCartTime(final AbstractOrderEntry item, final Date value)
	{
		setAddToCartTime( getSession().getSessionContext(), item, value );
	}
	
	@Override
	public String getName()
	{
		return SelectivecartservicesConstants.EXTENSIONNAME;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Wishlist2Entry.quantity</code> attribute.
	 * @return the quantity - Attribute that represents total number saved for this product
	 */
	public Integer getQuantity(final SessionContext ctx, final Wishlist2Entry item)
	{
		return (Integer)item.getProperty( ctx, SelectivecartservicesConstants.Attributes.Wishlist2Entry.QUANTITY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Wishlist2Entry.quantity</code> attribute.
	 * @return the quantity - Attribute that represents total number saved for this product
	 */
	public Integer getQuantity(final Wishlist2Entry item)
	{
		return getQuantity( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Wishlist2Entry.quantity</code> attribute. 
	 * @return the quantity - Attribute that represents total number saved for this product
	 */
	public int getQuantityAsPrimitive(final SessionContext ctx, final Wishlist2Entry item)
	{
		Integer value = getQuantity( ctx,item );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Wishlist2Entry.quantity</code> attribute. 
	 * @return the quantity - Attribute that represents total number saved for this product
	 */
	public int getQuantityAsPrimitive(final Wishlist2Entry item)
	{
		return getQuantityAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Wishlist2Entry.quantity</code> attribute. 
	 * @param value the quantity - Attribute that represents total number saved for this product
	 */
	public void setQuantity(final SessionContext ctx, final Wishlist2Entry item, final Integer value)
	{
		item.setProperty(ctx, SelectivecartservicesConstants.Attributes.Wishlist2Entry.QUANTITY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Wishlist2Entry.quantity</code> attribute. 
	 * @param value the quantity - Attribute that represents total number saved for this product
	 */
	public void setQuantity(final Wishlist2Entry item, final Integer value)
	{
		setQuantity( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Wishlist2Entry.quantity</code> attribute. 
	 * @param value the quantity - Attribute that represents total number saved for this product
	 */
	public void setQuantity(final SessionContext ctx, final Wishlist2Entry item, final int value)
	{
		setQuantity( ctx, item, Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Wishlist2Entry.quantity</code> attribute. 
	 * @param value the quantity - Attribute that represents total number saved for this product
	 */
	public void setQuantity(final Wishlist2Entry item, final int value)
	{
		setQuantity( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Cart.visible</code> attribute.
	 * @return the visible - set the status of the cart visible
	 */
	public Boolean isVisible(final SessionContext ctx, final Cart item)
	{
		return (Boolean)item.getProperty( ctx, SelectivecartservicesConstants.Attributes.Cart.VISIBLE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Cart.visible</code> attribute.
	 * @return the visible - set the status of the cart visible
	 */
	public Boolean isVisible(final Cart item)
	{
		return isVisible( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Cart.visible</code> attribute. 
	 * @return the visible - set the status of the cart visible
	 */
	public boolean isVisibleAsPrimitive(final SessionContext ctx, final Cart item)
	{
		Boolean value = isVisible( ctx,item );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Cart.visible</code> attribute. 
	 * @return the visible - set the status of the cart visible
	 */
	public boolean isVisibleAsPrimitive(final Cart item)
	{
		return isVisibleAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Cart.visible</code> attribute. 
	 * @param value the visible - set the status of the cart visible
	 */
	public void setVisible(final SessionContext ctx, final Cart item, final Boolean value)
	{
		item.setProperty(ctx, SelectivecartservicesConstants.Attributes.Cart.VISIBLE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Cart.visible</code> attribute. 
	 * @param value the visible - set the status of the cart visible
	 */
	public void setVisible(final Cart item, final Boolean value)
	{
		setVisible( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Cart.visible</code> attribute. 
	 * @param value the visible - set the status of the cart visible
	 */
	public void setVisible(final SessionContext ctx, final Cart item, final boolean value)
	{
		setVisible( ctx, item, Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Cart.visible</code> attribute. 
	 * @param value the visible - set the status of the cart visible
	 */
	public void setVisible(final Cart item, final boolean value)
	{
		setVisible( getSession().getSessionContext(), item, value );
	}
	
}
