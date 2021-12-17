/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 15-Dec-2021, 3:07:44 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.customercouponservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.promotionengineservices.model.PromotionSourceRuleModel;
import de.hybris.platform.promotionengineservices.model.RuleBasedPromotionModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type CustomerCouponForPromotionSourceRule first defined at extension customercouponservices.
 */
@SuppressWarnings("all")
public class CustomerCouponForPromotionSourceRuleModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CustomerCouponForPromotionSourceRule";
	
	/** <i>Generated constant</i> - Attribute key of <code>CustomerCouponForPromotionSourceRule.customerCouponCode</code> attribute defined at extension <code>customercouponservices</code>. */
	public static final String CUSTOMERCOUPONCODE = "customerCouponCode";
	
	/** <i>Generated constant</i> - Attribute key of <code>CustomerCouponForPromotionSourceRule.rule</code> attribute defined at extension <code>customercouponservices</code>. */
	public static final String RULE = "rule";
	
	/** <i>Generated constant</i> - Attribute key of <code>CustomerCouponForPromotionSourceRule.promotion</code> attribute defined at extension <code>customercouponservices</code>. */
	public static final String PROMOTION = "promotion";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CustomerCouponForPromotionSourceRuleModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CustomerCouponForPromotionSourceRuleModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _customerCouponCode initial attribute declared by type <code>CustomerCouponForPromotionSourceRule</code> at extension <code>customercouponservices</code>
	 * @param _promotion initial attribute declared by type <code>CustomerCouponForPromotionSourceRule</code> at extension <code>customercouponservices</code>
	 * @param _rule initial attribute declared by type <code>CustomerCouponForPromotionSourceRule</code> at extension <code>customercouponservices</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public CustomerCouponForPromotionSourceRuleModel(final String _customerCouponCode, final RuleBasedPromotionModel _promotion, final PromotionSourceRuleModel _rule)
	{
		super();
		setCustomerCouponCode(_customerCouponCode);
		setPromotion(_promotion);
		setRule(_rule);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _customerCouponCode initial attribute declared by type <code>CustomerCouponForPromotionSourceRule</code> at extension <code>customercouponservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _promotion initial attribute declared by type <code>CustomerCouponForPromotionSourceRule</code> at extension <code>customercouponservices</code>
	 * @param _rule initial attribute declared by type <code>CustomerCouponForPromotionSourceRule</code> at extension <code>customercouponservices</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public CustomerCouponForPromotionSourceRuleModel(final String _customerCouponCode, final ItemModel _owner, final RuleBasedPromotionModel _promotion, final PromotionSourceRuleModel _rule)
	{
		super();
		setCustomerCouponCode(_customerCouponCode);
		setOwner(_owner);
		setPromotion(_promotion);
		setRule(_rule);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CustomerCouponForPromotionSourceRule.customerCouponCode</code> attribute defined at extension <code>customercouponservices</code>. 
	 * @return the customerCouponCode
	 */
	@Accessor(qualifier = "customerCouponCode", type = Accessor.Type.GETTER)
	public String getCustomerCouponCode()
	{
		return getPersistenceContext().getPropertyValue(CUSTOMERCOUPONCODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CustomerCouponForPromotionSourceRule.promotion</code> attribute defined at extension <code>customercouponservices</code>. 
	 * @return the promotion
	 */
	@Accessor(qualifier = "promotion", type = Accessor.Type.GETTER)
	public RuleBasedPromotionModel getPromotion()
	{
		return getPersistenceContext().getPropertyValue(PROMOTION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CustomerCouponForPromotionSourceRule.rule</code> attribute defined at extension <code>customercouponservices</code>. 
	 * @return the rule
	 */
	@Accessor(qualifier = "rule", type = Accessor.Type.GETTER)
	public PromotionSourceRuleModel getRule()
	{
		return getPersistenceContext().getPropertyValue(RULE);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>CustomerCouponForPromotionSourceRule.customerCouponCode</code> attribute defined at extension <code>customercouponservices</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the customerCouponCode
	 */
	@Accessor(qualifier = "customerCouponCode", type = Accessor.Type.SETTER)
	public void setCustomerCouponCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CUSTOMERCOUPONCODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>CustomerCouponForPromotionSourceRule.promotion</code> attribute defined at extension <code>customercouponservices</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the promotion
	 */
	@Accessor(qualifier = "promotion", type = Accessor.Type.SETTER)
	public void setPromotion(final RuleBasedPromotionModel value)
	{
		getPersistenceContext().setPropertyValue(PROMOTION, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>CustomerCouponForPromotionSourceRule.rule</code> attribute defined at extension <code>customercouponservices</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the rule
	 */
	@Accessor(qualifier = "rule", type = Accessor.Type.SETTER)
	public void setRule(final PromotionSourceRuleModel value)
	{
		getPersistenceContext().setPropertyValue(RULE, value);
	}
	
}
