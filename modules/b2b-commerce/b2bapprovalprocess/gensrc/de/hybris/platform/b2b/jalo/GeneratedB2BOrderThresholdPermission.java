/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 06-Oct-2021, 11:13:07 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.jalo;

import de.hybris.platform.b2b.constants.B2BApprovalprocessConstants;
import de.hybris.platform.b2b.jalo.B2BPermission;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Currency;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.b2b.jalo.B2BOrderThresholdPermission B2BOrderThresholdPermission}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedB2BOrderThresholdPermission extends B2BPermission
{
	/** Qualifier of the <code>B2BOrderThresholdPermission.threshold</code> attribute **/
	public static final String THRESHOLD = "threshold";
	/** Qualifier of the <code>B2BOrderThresholdPermission.currency</code> attribute **/
	public static final String CURRENCY = "currency";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(B2BPermission.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(THRESHOLD, AttributeMode.INITIAL);
		tmp.put(CURRENCY, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BOrderThresholdPermission.currency</code> attribute.
	 * @return the currency
	 */
	public Currency getCurrency(final SessionContext ctx)
	{
		return (Currency)getProperty( ctx, CURRENCY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BOrderThresholdPermission.currency</code> attribute.
	 * @return the currency
	 */
	public Currency getCurrency()
	{
		return getCurrency( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BOrderThresholdPermission.currency</code> attribute. 
	 * @param value the currency
	 */
	public void setCurrency(final SessionContext ctx, final Currency value)
	{
		setProperty(ctx, CURRENCY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BOrderThresholdPermission.currency</code> attribute. 
	 * @param value the currency
	 */
	public void setCurrency(final Currency value)
	{
		setCurrency( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BOrderThresholdPermission.threshold</code> attribute.
	 * @return the threshold
	 */
	public Double getThreshold(final SessionContext ctx)
	{
		return (Double)getProperty( ctx, THRESHOLD);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BOrderThresholdPermission.threshold</code> attribute.
	 * @return the threshold
	 */
	public Double getThreshold()
	{
		return getThreshold( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BOrderThresholdPermission.threshold</code> attribute. 
	 * @return the threshold
	 */
	public double getThresholdAsPrimitive(final SessionContext ctx)
	{
		Double value = getThreshold( ctx );
		return value != null ? value.doubleValue() : 0.0d;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BOrderThresholdPermission.threshold</code> attribute. 
	 * @return the threshold
	 */
	public double getThresholdAsPrimitive()
	{
		return getThresholdAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BOrderThresholdPermission.threshold</code> attribute. 
	 * @param value the threshold
	 */
	public void setThreshold(final SessionContext ctx, final Double value)
	{
		setProperty(ctx, THRESHOLD,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BOrderThresholdPermission.threshold</code> attribute. 
	 * @param value the threshold
	 */
	public void setThreshold(final Double value)
	{
		setThreshold( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BOrderThresholdPermission.threshold</code> attribute. 
	 * @param value the threshold
	 */
	public void setThreshold(final SessionContext ctx, final double value)
	{
		setThreshold( ctx,Double.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BOrderThresholdPermission.threshold</code> attribute. 
	 * @param value the threshold
	 */
	public void setThreshold(final double value)
	{
		setThreshold( getSession().getSessionContext(), value );
	}
	
}
