/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 11-Dec-2021, 12:32:58 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.jalo;

import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.searchservices.constants.SearchservicesConstants;
import de.hybris.platform.validation.jalo.constraints.AttributeConstraint;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Generated class for type {@link de.hybris.platform.validation.jalo.constraints.AttributeConstraint SnCollectionPatternConstraint}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedSnCollectionPatternConstraint extends AttributeConstraint
{
	/** Qualifier of the <code>SnCollectionPatternConstraint.regexp</code> attribute **/
	public static final String REGEXP = "regexp";
	/** Qualifier of the <code>SnCollectionPatternConstraint.flags</code> attribute **/
	public static final String FLAGS = "flags";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(AttributeConstraint.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(REGEXP, AttributeMode.INITIAL);
		tmp.put(FLAGS, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnCollectionPatternConstraint.flags</code> attribute.
	 * @return the flags
	 */
	public Set<EnumerationValue> getFlags(final SessionContext ctx)
	{
		Set<EnumerationValue> coll = (Set<EnumerationValue>)getProperty( ctx, FLAGS);
		return coll != null ? coll : Collections.EMPTY_SET;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnCollectionPatternConstraint.flags</code> attribute.
	 * @return the flags
	 */
	public Set<EnumerationValue> getFlags()
	{
		return getFlags( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnCollectionPatternConstraint.flags</code> attribute. 
	 * @param value the flags
	 */
	public void setFlags(final SessionContext ctx, final Set<EnumerationValue> value)
	{
		setProperty(ctx, FLAGS,value == null || !value.isEmpty() ? value : null );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnCollectionPatternConstraint.flags</code> attribute. 
	 * @param value the flags
	 */
	public void setFlags(final Set<EnumerationValue> value)
	{
		setFlags( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnCollectionPatternConstraint.regexp</code> attribute.
	 * @return the regexp
	 */
	public String getRegexp(final SessionContext ctx)
	{
		return (String)getProperty( ctx, REGEXP);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SnCollectionPatternConstraint.regexp</code> attribute.
	 * @return the regexp
	 */
	public String getRegexp()
	{
		return getRegexp( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnCollectionPatternConstraint.regexp</code> attribute. 
	 * @param value the regexp
	 */
	public void setRegexp(final SessionContext ctx, final String value)
	{
		setProperty(ctx, REGEXP,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SnCollectionPatternConstraint.regexp</code> attribute. 
	 * @param value the regexp
	 */
	public void setRegexp(final String value)
	{
		setRegexp( getSession().getSessionContext(), value );
	}
	
}
