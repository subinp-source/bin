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
import de.hybris.platform.b2b.jalo.B2BOrderThresholdPermission;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.b2b.jalo.B2BOrderThresholdTimespanPermission B2BOrderThresholdTimespanPermission}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedB2BOrderThresholdTimespanPermission extends B2BOrderThresholdPermission
{
	/** Qualifier of the <code>B2BOrderThresholdTimespanPermission.range</code> attribute **/
	public static final String RANGE = "range";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(B2BOrderThresholdPermission.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(RANGE, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BOrderThresholdTimespanPermission.range</code> attribute.
	 * @return the range
	 */
	public EnumerationValue getRange(final SessionContext ctx)
	{
		return (EnumerationValue)getProperty( ctx, RANGE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BOrderThresholdTimespanPermission.range</code> attribute.
	 * @return the range
	 */
	public EnumerationValue getRange()
	{
		return getRange( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BOrderThresholdTimespanPermission.range</code> attribute. 
	 * @param value the range
	 */
	public void setRange(final SessionContext ctx, final EnumerationValue value)
	{
		setProperty(ctx, RANGE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BOrderThresholdTimespanPermission.range</code> attribute. 
	 * @param value the range
	 */
	public void setRange(final EnumerationValue value)
	{
		setRange( getSession().getSessionContext(), value );
	}
	
}
