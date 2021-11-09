/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 06-Oct-2021, 11:13:07 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.subscriptionservices.jalo;

import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.promotions.jalo.AbstractPromotionRestriction;
import de.hybris.platform.subscriptionservices.constants.SubscriptionservicesConstants;
import de.hybris.platform.subscriptionservices.jalo.BillingTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.subscriptionservices.jalo.PromotionBillingTimeRestriction PromotionBillingTimeRestriction}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedPromotionBillingTimeRestriction extends AbstractPromotionRestriction
{
	/** Qualifier of the <code>PromotionBillingTimeRestriction.positive</code> attribute **/
	public static final String POSITIVE = "positive";
	/** Qualifier of the <code>PromotionBillingTimeRestriction.billingTimes</code> attribute **/
	public static final String BILLINGTIMES = "billingTimes";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(AbstractPromotionRestriction.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(POSITIVE, AttributeMode.INITIAL);
		tmp.put(BILLINGTIMES, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PromotionBillingTimeRestriction.billingTimes</code> attribute.
	 * @return the billingTimes - The billing frequencies/events the promotion is not applied for
	 */
	public Collection<BillingTime> getBillingTimes(final SessionContext ctx)
	{
		Collection<BillingTime> coll = (Collection<BillingTime>)getProperty( ctx, BILLINGTIMES);
		return coll != null ? coll : Collections.EMPTY_LIST;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PromotionBillingTimeRestriction.billingTimes</code> attribute.
	 * @return the billingTimes - The billing frequencies/events the promotion is not applied for
	 */
	public Collection<BillingTime> getBillingTimes()
	{
		return getBillingTimes( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PromotionBillingTimeRestriction.billingTimes</code> attribute. 
	 * @param value the billingTimes - The billing frequencies/events the promotion is not applied for
	 */
	public void setBillingTimes(final SessionContext ctx, final Collection<BillingTime> value)
	{
		setProperty(ctx, BILLINGTIMES,value == null || !value.isEmpty() ? value : null );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PromotionBillingTimeRestriction.billingTimes</code> attribute. 
	 * @param value the billingTimes - The billing frequencies/events the promotion is not applied for
	 */
	public void setBillingTimes(final Collection<BillingTime> value)
	{
		setBillingTimes( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PromotionBillingTimeRestriction.positive</code> attribute.
	 * @return the positive - Specifies if this restriction is a positive (true) or negative (false) one.
	 */
	public Boolean isPositive(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, POSITIVE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PromotionBillingTimeRestriction.positive</code> attribute.
	 * @return the positive - Specifies if this restriction is a positive (true) or negative (false) one.
	 */
	public Boolean isPositive()
	{
		return isPositive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PromotionBillingTimeRestriction.positive</code> attribute. 
	 * @return the positive - Specifies if this restriction is a positive (true) or negative (false) one.
	 */
	public boolean isPositiveAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isPositive( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PromotionBillingTimeRestriction.positive</code> attribute. 
	 * @return the positive - Specifies if this restriction is a positive (true) or negative (false) one.
	 */
	public boolean isPositiveAsPrimitive()
	{
		return isPositiveAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PromotionBillingTimeRestriction.positive</code> attribute. 
	 * @param value the positive - Specifies if this restriction is a positive (true) or negative (false) one.
	 */
	public void setPositive(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, POSITIVE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PromotionBillingTimeRestriction.positive</code> attribute. 
	 * @param value the positive - Specifies if this restriction is a positive (true) or negative (false) one.
	 */
	public void setPositive(final Boolean value)
	{
		setPositive( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PromotionBillingTimeRestriction.positive</code> attribute. 
	 * @param value the positive - Specifies if this restriction is a positive (true) or negative (false) one.
	 */
	public void setPositive(final SessionContext ctx, final boolean value)
	{
		setPositive( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PromotionBillingTimeRestriction.positive</code> attribute. 
	 * @param value the positive - Specifies if this restriction is a positive (true) or negative (false) one.
	 */
	public void setPositive(final boolean value)
	{
		setPositive( getSession().getSessionContext(), value );
	}
	
}
