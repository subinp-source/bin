/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 08-Nov-2021, 4:51:25 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.customercouponservices.jalo;

import de.hybris.platform.customercouponservices.constants.CustomercouponservicesConstants;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.promotionengineservices.jalo.PromotionSourceRule;
import de.hybris.platform.promotionengineservices.jalo.RuleBasedPromotion;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.jalo.GenericItem CustomerCouponForPromotionSourceRule}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedCustomerCouponForPromotionSourceRule extends GenericItem
{
	/** Qualifier of the <code>CustomerCouponForPromotionSourceRule.customerCouponCode</code> attribute **/
	public static final String CUSTOMERCOUPONCODE = "customerCouponCode";
	/** Qualifier of the <code>CustomerCouponForPromotionSourceRule.rule</code> attribute **/
	public static final String RULE = "rule";
	/** Qualifier of the <code>CustomerCouponForPromotionSourceRule.promotion</code> attribute **/
	public static final String PROMOTION = "promotion";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(CUSTOMERCOUPONCODE, AttributeMode.INITIAL);
		tmp.put(RULE, AttributeMode.INITIAL);
		tmp.put(PROMOTION, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CustomerCouponForPromotionSourceRule.customerCouponCode</code> attribute.
	 * @return the customerCouponCode
	 */
	public String getCustomerCouponCode(final SessionContext ctx)
	{
		return (String)getProperty( ctx, CUSTOMERCOUPONCODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CustomerCouponForPromotionSourceRule.customerCouponCode</code> attribute.
	 * @return the customerCouponCode
	 */
	public String getCustomerCouponCode()
	{
		return getCustomerCouponCode( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CustomerCouponForPromotionSourceRule.customerCouponCode</code> attribute. 
	 * @param value the customerCouponCode
	 */
	protected void setCustomerCouponCode(final SessionContext ctx, final String value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		// initial-only attribute: make sure this attribute can be set during item creation only
		if ( ctx.getAttribute( "core.types.creation.initial") != Boolean.TRUE )
		{
			throw new JaloInvalidParameterException( "attribute '"+CUSTOMERCOUPONCODE+"' is not changeable", 0 );
		}
		setProperty(ctx, CUSTOMERCOUPONCODE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CustomerCouponForPromotionSourceRule.customerCouponCode</code> attribute. 
	 * @param value the customerCouponCode
	 */
	protected void setCustomerCouponCode(final String value)
	{
		setCustomerCouponCode( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CustomerCouponForPromotionSourceRule.promotion</code> attribute.
	 * @return the promotion
	 */
	public RuleBasedPromotion getPromotion(final SessionContext ctx)
	{
		return (RuleBasedPromotion)getProperty( ctx, PROMOTION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CustomerCouponForPromotionSourceRule.promotion</code> attribute.
	 * @return the promotion
	 */
	public RuleBasedPromotion getPromotion()
	{
		return getPromotion( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CustomerCouponForPromotionSourceRule.promotion</code> attribute. 
	 * @param value the promotion
	 */
	protected void setPromotion(final SessionContext ctx, final RuleBasedPromotion value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		// initial-only attribute: make sure this attribute can be set during item creation only
		if ( ctx.getAttribute( "core.types.creation.initial") != Boolean.TRUE )
		{
			throw new JaloInvalidParameterException( "attribute '"+PROMOTION+"' is not changeable", 0 );
		}
		setProperty(ctx, PROMOTION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CustomerCouponForPromotionSourceRule.promotion</code> attribute. 
	 * @param value the promotion
	 */
	protected void setPromotion(final RuleBasedPromotion value)
	{
		setPromotion( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CustomerCouponForPromotionSourceRule.rule</code> attribute.
	 * @return the rule
	 */
	public PromotionSourceRule getRule(final SessionContext ctx)
	{
		return (PromotionSourceRule)getProperty( ctx, RULE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CustomerCouponForPromotionSourceRule.rule</code> attribute.
	 * @return the rule
	 */
	public PromotionSourceRule getRule()
	{
		return getRule( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CustomerCouponForPromotionSourceRule.rule</code> attribute. 
	 * @param value the rule
	 */
	protected void setRule(final SessionContext ctx, final PromotionSourceRule value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		// initial-only attribute: make sure this attribute can be set during item creation only
		if ( ctx.getAttribute( "core.types.creation.initial") != Boolean.TRUE )
		{
			throw new JaloInvalidParameterException( "attribute '"+RULE+"' is not changeable", 0 );
		}
		setProperty(ctx, RULE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CustomerCouponForPromotionSourceRule.rule</code> attribute. 
	 * @param value the rule
	 */
	protected void setRule(final PromotionSourceRule value)
	{
		setRule( getSession().getSessionContext(), value );
	}
	
}
