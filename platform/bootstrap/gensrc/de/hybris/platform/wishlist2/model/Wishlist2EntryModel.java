/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 11-Dec-2021, 12:32:58 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.wishlist2.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.wishlist2.enums.Wishlist2EntryPriority;
import de.hybris.platform.wishlist2.model.Wishlist2Model;
import java.util.Date;

/**
 * Generated model class for type Wishlist2Entry first defined at extension wishlist.
 */
@SuppressWarnings("all")
public class Wishlist2EntryModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "Wishlist2Entry";
	
	/**<i>Generated relation code constant for relation <code>WishList22Entry</code> defining source attribute <code>wishlist</code> in extension <code>wishlist</code>.</i>*/
	public static final String _WISHLIST22ENTRY = "WishList22Entry";
	
	/** <i>Generated constant</i> - Attribute key of <code>Wishlist2Entry.product</code> attribute defined at extension <code>wishlist</code>. */
	public static final String PRODUCT = "product";
	
	/** <i>Generated constant</i> - Attribute key of <code>Wishlist2Entry.desired</code> attribute defined at extension <code>wishlist</code>. */
	public static final String DESIRED = "desired";
	
	/** <i>Generated constant</i> - Attribute key of <code>Wishlist2Entry.received</code> attribute defined at extension <code>wishlist</code>. */
	public static final String RECEIVED = "received";
	
	/** <i>Generated constant</i> - Attribute key of <code>Wishlist2Entry.priority</code> attribute defined at extension <code>wishlist</code>. */
	public static final String PRIORITY = "priority";
	
	/** <i>Generated constant</i> - Attribute key of <code>Wishlist2Entry.addedDate</code> attribute defined at extension <code>wishlist</code>. */
	public static final String ADDEDDATE = "addedDate";
	
	/** <i>Generated constant</i> - Attribute key of <code>Wishlist2Entry.comment</code> attribute defined at extension <code>wishlist</code>. */
	public static final String COMMENT = "comment";
	
	/** <i>Generated constant</i> - Attribute key of <code>Wishlist2Entry.wishlist</code> attribute defined at extension <code>wishlist</code>. */
	public static final String WISHLIST = "wishlist";
	
	/** <i>Generated constant</i> - Attribute key of <code>Wishlist2Entry.quantity</code> attribute defined at extension <code>selectivecartservices</code>. */
	public static final String QUANTITY = "quantity";
	
	/** <i>Generated constant</i> - Attribute key of <code>Wishlist2Entry.addToCartTime</code> attribute defined at extension <code>selectivecartservices</code>. */
	public static final String ADDTOCARTTIME = "addToCartTime";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public Wishlist2EntryModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public Wishlist2EntryModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _addedDate initial attribute declared by type <code>Wishlist2Entry</code> at extension <code>wishlist</code>
	 * @param _priority initial attribute declared by type <code>Wishlist2Entry</code> at extension <code>wishlist</code>
	 * @param _product initial attribute declared by type <code>Wishlist2Entry</code> at extension <code>wishlist</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public Wishlist2EntryModel(final Date _addedDate, final Wishlist2EntryPriority _priority, final ProductModel _product)
	{
		super();
		setAddedDate(_addedDate);
		setPriority(_priority);
		setProduct(_product);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _addedDate initial attribute declared by type <code>Wishlist2Entry</code> at extension <code>wishlist</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _priority initial attribute declared by type <code>Wishlist2Entry</code> at extension <code>wishlist</code>
	 * @param _product initial attribute declared by type <code>Wishlist2Entry</code> at extension <code>wishlist</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public Wishlist2EntryModel(final Date _addedDate, final ItemModel _owner, final Wishlist2EntryPriority _priority, final ProductModel _product)
	{
		super();
		setAddedDate(_addedDate);
		setOwner(_owner);
		setPriority(_priority);
		setProduct(_product);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Wishlist2Entry.addedDate</code> attribute defined at extension <code>wishlist</code>. 
	 * @return the addedDate
	 */
	@Accessor(qualifier = "addedDate", type = Accessor.Type.GETTER)
	public Date getAddedDate()
	{
		return getPersistenceContext().getPropertyValue(ADDEDDATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Wishlist2Entry.addToCartTime</code> attribute defined at extension <code>selectivecartservices</code>. 
	 * @return the addToCartTime - The time when item is added to cart.
	 */
	@Accessor(qualifier = "addToCartTime", type = Accessor.Type.GETTER)
	public Date getAddToCartTime()
	{
		return getPersistenceContext().getPropertyValue(ADDTOCARTTIME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Wishlist2Entry.comment</code> attribute defined at extension <code>wishlist</code>. 
	 * @return the comment
	 */
	@Accessor(qualifier = "comment", type = Accessor.Type.GETTER)
	public String getComment()
	{
		return getPersistenceContext().getPropertyValue(COMMENT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Wishlist2Entry.desired</code> attribute defined at extension <code>wishlist</code>. 
	 * @return the desired
	 */
	@Accessor(qualifier = "desired", type = Accessor.Type.GETTER)
	public Integer getDesired()
	{
		return getPersistenceContext().getPropertyValue(DESIRED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Wishlist2Entry.priority</code> attribute defined at extension <code>wishlist</code>. 
	 * @return the priority
	 */
	@Accessor(qualifier = "priority", type = Accessor.Type.GETTER)
	public Wishlist2EntryPriority getPriority()
	{
		return getPersistenceContext().getPropertyValue(PRIORITY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Wishlist2Entry.product</code> attribute defined at extension <code>wishlist</code>. 
	 * @return the product
	 */
	@Accessor(qualifier = "product", type = Accessor.Type.GETTER)
	public ProductModel getProduct()
	{
		return getPersistenceContext().getPropertyValue(PRODUCT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Wishlist2Entry.quantity</code> attribute defined at extension <code>selectivecartservices</code>. 
	 * @return the quantity - Attribute that represents total number saved for this product
	 */
	@Accessor(qualifier = "quantity", type = Accessor.Type.GETTER)
	public Integer getQuantity()
	{
		return getPersistenceContext().getPropertyValue(QUANTITY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Wishlist2Entry.received</code> attribute defined at extension <code>wishlist</code>. 
	 * @return the received
	 */
	@Accessor(qualifier = "received", type = Accessor.Type.GETTER)
	public Integer getReceived()
	{
		return getPersistenceContext().getPropertyValue(RECEIVED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Wishlist2Entry.wishlist</code> attribute defined at extension <code>wishlist</code>. 
	 * @return the wishlist
	 */
	@Accessor(qualifier = "wishlist", type = Accessor.Type.GETTER)
	public Wishlist2Model getWishlist()
	{
		return getPersistenceContext().getPropertyValue(WISHLIST);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Wishlist2Entry.addedDate</code> attribute defined at extension <code>wishlist</code>. 
	 *  
	 * @param value the addedDate
	 */
	@Accessor(qualifier = "addedDate", type = Accessor.Type.SETTER)
	public void setAddedDate(final Date value)
	{
		getPersistenceContext().setPropertyValue(ADDEDDATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Wishlist2Entry.addToCartTime</code> attribute defined at extension <code>selectivecartservices</code>. 
	 *  
	 * @param value the addToCartTime - The time when item is added to cart.
	 */
	@Accessor(qualifier = "addToCartTime", type = Accessor.Type.SETTER)
	public void setAddToCartTime(final Date value)
	{
		getPersistenceContext().setPropertyValue(ADDTOCARTTIME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Wishlist2Entry.comment</code> attribute defined at extension <code>wishlist</code>. 
	 *  
	 * @param value the comment
	 */
	@Accessor(qualifier = "comment", type = Accessor.Type.SETTER)
	public void setComment(final String value)
	{
		getPersistenceContext().setPropertyValue(COMMENT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Wishlist2Entry.desired</code> attribute defined at extension <code>wishlist</code>. 
	 *  
	 * @param value the desired
	 */
	@Accessor(qualifier = "desired", type = Accessor.Type.SETTER)
	public void setDesired(final Integer value)
	{
		getPersistenceContext().setPropertyValue(DESIRED, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Wishlist2Entry.priority</code> attribute defined at extension <code>wishlist</code>. 
	 *  
	 * @param value the priority
	 */
	@Accessor(qualifier = "priority", type = Accessor.Type.SETTER)
	public void setPriority(final Wishlist2EntryPriority value)
	{
		getPersistenceContext().setPropertyValue(PRIORITY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Wishlist2Entry.product</code> attribute defined at extension <code>wishlist</code>. 
	 *  
	 * @param value the product
	 */
	@Accessor(qualifier = "product", type = Accessor.Type.SETTER)
	public void setProduct(final ProductModel value)
	{
		getPersistenceContext().setPropertyValue(PRODUCT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Wishlist2Entry.quantity</code> attribute defined at extension <code>selectivecartservices</code>. 
	 *  
	 * @param value the quantity - Attribute that represents total number saved for this product
	 */
	@Accessor(qualifier = "quantity", type = Accessor.Type.SETTER)
	public void setQuantity(final Integer value)
	{
		getPersistenceContext().setPropertyValue(QUANTITY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Wishlist2Entry.received</code> attribute defined at extension <code>wishlist</code>. 
	 *  
	 * @param value the received
	 */
	@Accessor(qualifier = "received", type = Accessor.Type.SETTER)
	public void setReceived(final Integer value)
	{
		getPersistenceContext().setPropertyValue(RECEIVED, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Wishlist2Entry.wishlist</code> attribute defined at extension <code>wishlist</code>. 
	 *  
	 * @param value the wishlist
	 */
	@Accessor(qualifier = "wishlist", type = Accessor.Type.SETTER)
	public void setWishlist(final Wishlist2Model value)
	{
		getPersistenceContext().setPropertyValue(WISHLIST, value);
	}
	
}
