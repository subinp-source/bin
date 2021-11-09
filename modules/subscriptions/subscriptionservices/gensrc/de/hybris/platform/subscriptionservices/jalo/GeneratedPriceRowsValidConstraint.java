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
import de.hybris.platform.subscriptionservices.constants.SubscriptionservicesConstants;
import de.hybris.platform.validation.jalo.constraints.AttributeConstraint;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.subscriptionservices.jalo.PriceRowsValidConstraint PriceRowsValidConstraint}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedPriceRowsValidConstraint extends AttributeConstraint
{
	/** Qualifier of the <code>PriceRowsValidConstraint.priceRowType</code> attribute **/
	public static final String PRICEROWTYPE = "priceRowType";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(AttributeConstraint.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(PRICEROWTYPE, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PriceRowsValidConstraint.priceRowType</code> attribute.
	 * @return the priceRowType - The price row type
	 */
	public String getPriceRowType(final SessionContext ctx)
	{
		return (String)getProperty( ctx, PRICEROWTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PriceRowsValidConstraint.priceRowType</code> attribute.
	 * @return the priceRowType - The price row type
	 */
	public String getPriceRowType()
	{
		return getPriceRowType( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PriceRowsValidConstraint.priceRowType</code> attribute. 
	 * @param value the priceRowType - The price row type
	 */
	public void setPriceRowType(final SessionContext ctx, final String value)
	{
		setProperty(ctx, PRICEROWTYPE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PriceRowsValidConstraint.priceRowType</code> attribute. 
	 * @param value the priceRowType - The price row type
	 */
	public void setPriceRowType(final String value)
	{
		setPriceRowType( getSession().getSessionContext(), value );
	}
	
}
