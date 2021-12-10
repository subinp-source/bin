/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 11-Dec-2021, 12:32:58 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.jalo.constraints;

import de.hybris.platform.apiregistryservices.constants.ApiregistryservicesConstants;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.validation.jalo.constraints.TypeConstraint;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Generated class for type {@link de.hybris.platform.apiregistryservices.jalo.constraints.EventMappingConstraint EventMappingConstraint}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedEventMappingConstraint extends TypeConstraint
{
	/** Qualifier of the <code>EventMappingConstraint.keyRegexp</code> attribute **/
	public static final String KEYREGEXP = "keyRegexp";
	/** Qualifier of the <code>EventMappingConstraint.keyFlags</code> attribute **/
	public static final String KEYFLAGS = "keyFlags";
	/** Qualifier of the <code>EventMappingConstraint.valueRegexp</code> attribute **/
	public static final String VALUEREGEXP = "valueRegexp";
	/** Qualifier of the <code>EventMappingConstraint.valueFlags</code> attribute **/
	public static final String VALUEFLAGS = "valueFlags";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(TypeConstraint.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(KEYREGEXP, AttributeMode.INITIAL);
		tmp.put(KEYFLAGS, AttributeMode.INITIAL);
		tmp.put(VALUEREGEXP, AttributeMode.INITIAL);
		tmp.put(VALUEFLAGS, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventMappingConstraint.keyFlags</code> attribute.
	 * @return the keyFlags - Regular expression for constraint
	 */
	public Set<EnumerationValue> getKeyFlags(final SessionContext ctx)
	{
		Set<EnumerationValue> coll = (Set<EnumerationValue>)getProperty( ctx, KEYFLAGS);
		return coll != null ? coll : Collections.EMPTY_SET;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventMappingConstraint.keyFlags</code> attribute.
	 * @return the keyFlags - Regular expression for constraint
	 */
	public Set<EnumerationValue> getKeyFlags()
	{
		return getKeyFlags( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EventMappingConstraint.keyFlags</code> attribute. 
	 * @param value the keyFlags - Regular expression for constraint
	 */
	public void setKeyFlags(final SessionContext ctx, final Set<EnumerationValue> value)
	{
		setProperty(ctx, KEYFLAGS,value == null || !value.isEmpty() ? value : null );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EventMappingConstraint.keyFlags</code> attribute. 
	 * @param value the keyFlags - Regular expression for constraint
	 */
	public void setKeyFlags(final Set<EnumerationValue> value)
	{
		setKeyFlags( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventMappingConstraint.keyRegexp</code> attribute.
	 * @return the keyRegexp - Regular expression of toString() Map key representation
	 */
	public String getKeyRegexp(final SessionContext ctx)
	{
		return (String)getProperty( ctx, KEYREGEXP);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventMappingConstraint.keyRegexp</code> attribute.
	 * @return the keyRegexp - Regular expression of toString() Map key representation
	 */
	public String getKeyRegexp()
	{
		return getKeyRegexp( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EventMappingConstraint.keyRegexp</code> attribute. 
	 * @param value the keyRegexp - Regular expression of toString() Map key representation
	 */
	public void setKeyRegexp(final SessionContext ctx, final String value)
	{
		setProperty(ctx, KEYREGEXP,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EventMappingConstraint.keyRegexp</code> attribute. 
	 * @param value the keyRegexp - Regular expression of toString() Map key representation
	 */
	public void setKeyRegexp(final String value)
	{
		setKeyRegexp( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventMappingConstraint.valueFlags</code> attribute.
	 * @return the valueFlags - Regular expression for constraint
	 */
	public Set<EnumerationValue> getValueFlags(final SessionContext ctx)
	{
		Set<EnumerationValue> coll = (Set<EnumerationValue>)getProperty( ctx, VALUEFLAGS);
		return coll != null ? coll : Collections.EMPTY_SET;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventMappingConstraint.valueFlags</code> attribute.
	 * @return the valueFlags - Regular expression for constraint
	 */
	public Set<EnumerationValue> getValueFlags()
	{
		return getValueFlags( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EventMappingConstraint.valueFlags</code> attribute. 
	 * @param value the valueFlags - Regular expression for constraint
	 */
	public void setValueFlags(final SessionContext ctx, final Set<EnumerationValue> value)
	{
		setProperty(ctx, VALUEFLAGS,value == null || !value.isEmpty() ? value : null );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EventMappingConstraint.valueFlags</code> attribute. 
	 * @param value the valueFlags - Regular expression for constraint
	 */
	public void setValueFlags(final Set<EnumerationValue> value)
	{
		setValueFlags( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventMappingConstraint.valueRegexp</code> attribute.
	 * @return the valueRegexp - Regular expression of toString() Map value representation
	 */
	public String getValueRegexp(final SessionContext ctx)
	{
		return (String)getProperty( ctx, VALUEREGEXP);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EventMappingConstraint.valueRegexp</code> attribute.
	 * @return the valueRegexp - Regular expression of toString() Map value representation
	 */
	public String getValueRegexp()
	{
		return getValueRegexp( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EventMappingConstraint.valueRegexp</code> attribute. 
	 * @param value the valueRegexp - Regular expression of toString() Map value representation
	 */
	public void setValueRegexp(final SessionContext ctx, final String value)
	{
		setProperty(ctx, VALUEREGEXP,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>EventMappingConstraint.valueRegexp</code> attribute. 
	 * @param value the valueRegexp - Regular expression of toString() Map value representation
	 */
	public void setValueRegexp(final String value)
	{
		setValueRegexp( getSession().getSessionContext(), value );
	}
	
}
