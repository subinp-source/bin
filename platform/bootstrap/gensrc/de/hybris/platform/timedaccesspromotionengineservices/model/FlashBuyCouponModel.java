/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 11-Dec-2021, 12:32:58 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.timedaccesspromotionengineservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.couponservices.model.SingleCodeCouponModel;
import de.hybris.platform.promotionengineservices.model.PromotionSourceRuleModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type FlashBuyCoupon first defined at extension timedaccesspromotionengineservices.
 * <p>
 * Flash buy special coupon.
 */
@SuppressWarnings("all")
public class FlashBuyCouponModel extends SingleCodeCouponModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "FlashBuyCoupon";
	
	/** <i>Generated constant</i> - Attribute key of <code>FlashBuyCoupon.rule</code> attribute defined at extension <code>timedaccesspromotionengineservices</code>. */
	public static final String RULE = "rule";
	
	/** <i>Generated constant</i> - Attribute key of <code>FlashBuyCoupon.maxProductQuantityPerOrder</code> attribute defined at extension <code>timedaccesspromotionengineservices</code>. */
	public static final String MAXPRODUCTQUANTITYPERORDER = "maxProductQuantityPerOrder";
	
	/** <i>Generated constant</i> - Attribute key of <code>FlashBuyCoupon.product</code> attribute defined at extension <code>timedaccesspromotionengineservices</code>. */
	public static final String PRODUCT = "product";
	
	/** <i>Generated constant</i> - Attribute key of <code>FlashBuyCoupon.originalMaxOrderQuantity</code> attribute defined at extension <code>timedaccesspromotionengineservices</code>. */
	public static final String ORIGINALMAXORDERQUANTITY = "originalMaxOrderQuantity";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public FlashBuyCouponModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public FlashBuyCouponModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _couponId initial attribute declared by type <code>AbstractCoupon</code> at extension <code>couponservices</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public FlashBuyCouponModel(final String _couponId)
	{
		super();
		setCouponId(_couponId);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _couponId initial attribute declared by type <code>AbstractCoupon</code> at extension <code>couponservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public FlashBuyCouponModel(final String _couponId, final ItemModel _owner)
	{
		super();
		setCouponId(_couponId);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FlashBuyCoupon.maxProductQuantityPerOrder</code> attribute defined at extension <code>timedaccesspromotionengineservices</code>. 
	 * @return the maxProductQuantityPerOrder
	 */
	@Accessor(qualifier = "maxProductQuantityPerOrder", type = Accessor.Type.GETTER)
	public Integer getMaxProductQuantityPerOrder()
	{
		return getPersistenceContext().getPropertyValue(MAXPRODUCTQUANTITYPERORDER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FlashBuyCoupon.originalMaxOrderQuantity</code> attribute defined at extension <code>timedaccesspromotionengineservices</code>. 
	 * @return the originalMaxOrderQuantity
	 */
	@Accessor(qualifier = "originalMaxOrderQuantity", type = Accessor.Type.GETTER)
	public Integer getOriginalMaxOrderQuantity()
	{
		return getPersistenceContext().getPropertyValue(ORIGINALMAXORDERQUANTITY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FlashBuyCoupon.product</code> attribute defined at extension <code>timedaccesspromotionengineservices</code>. 
	 * @return the product
	 */
	@Accessor(qualifier = "product", type = Accessor.Type.GETTER)
	public ProductModel getProduct()
	{
		return getPersistenceContext().getPropertyValue(PRODUCT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FlashBuyCoupon.rule</code> attribute defined at extension <code>timedaccesspromotionengineservices</code>. 
	 * @return the rule
	 */
	@Accessor(qualifier = "rule", type = Accessor.Type.GETTER)
	public PromotionSourceRuleModel getRule()
	{
		return getPersistenceContext().getPropertyValue(RULE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>FlashBuyCoupon.maxProductQuantityPerOrder</code> attribute defined at extension <code>timedaccesspromotionengineservices</code>. 
	 *  
	 * @param value the maxProductQuantityPerOrder
	 */
	@Accessor(qualifier = "maxProductQuantityPerOrder", type = Accessor.Type.SETTER)
	public void setMaxProductQuantityPerOrder(final Integer value)
	{
		getPersistenceContext().setPropertyValue(MAXPRODUCTQUANTITYPERORDER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>FlashBuyCoupon.originalMaxOrderQuantity</code> attribute defined at extension <code>timedaccesspromotionengineservices</code>. 
	 *  
	 * @param value the originalMaxOrderQuantity
	 */
	@Accessor(qualifier = "originalMaxOrderQuantity", type = Accessor.Type.SETTER)
	public void setOriginalMaxOrderQuantity(final Integer value)
	{
		getPersistenceContext().setPropertyValue(ORIGINALMAXORDERQUANTITY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>FlashBuyCoupon.product</code> attribute defined at extension <code>timedaccesspromotionengineservices</code>. 
	 *  
	 * @param value the product
	 */
	@Accessor(qualifier = "product", type = Accessor.Type.SETTER)
	public void setProduct(final ProductModel value)
	{
		getPersistenceContext().setPropertyValue(PRODUCT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>FlashBuyCoupon.rule</code> attribute defined at extension <code>timedaccesspromotionengineservices</code>. 
	 *  
	 * @param value the rule
	 */
	@Accessor(qualifier = "rule", type = Accessor.Type.SETTER)
	public void setRule(final PromotionSourceRuleModel value)
	{
		getPersistenceContext().setPropertyValue(RULE, value);
	}
	
}
