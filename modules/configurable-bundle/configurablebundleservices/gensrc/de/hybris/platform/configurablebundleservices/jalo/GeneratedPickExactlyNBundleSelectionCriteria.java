/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 08-Nov-2021, 4:51:25 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.configurablebundleservices.jalo;

import de.hybris.platform.configurablebundleservices.constants.ConfigurableBundleServicesConstants;
import de.hybris.platform.configurablebundleservices.jalo.BundleSelectionCriteria;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.configurablebundleservices.jalo.PickExactlyNBundleSelectionCriteria PickExactlyNBundleSelectionCriteria}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedPickExactlyNBundleSelectionCriteria extends BundleSelectionCriteria
{
	/** Qualifier of the <code>PickExactlyNBundleSelectionCriteria.n</code> attribute **/
	public static final String N = "n";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(BundleSelectionCriteria.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(N, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PickExactlyNBundleSelectionCriteria.n</code> attribute.
	 * @return the n - pick exactly n options
	 */
	public Integer getN(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, N);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PickExactlyNBundleSelectionCriteria.n</code> attribute.
	 * @return the n - pick exactly n options
	 */
	public Integer getN()
	{
		return getN( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PickExactlyNBundleSelectionCriteria.n</code> attribute. 
	 * @return the n - pick exactly n options
	 */
	public int getNAsPrimitive(final SessionContext ctx)
	{
		Integer value = getN( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PickExactlyNBundleSelectionCriteria.n</code> attribute. 
	 * @return the n - pick exactly n options
	 */
	public int getNAsPrimitive()
	{
		return getNAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PickExactlyNBundleSelectionCriteria.n</code> attribute. 
	 * @param value the n - pick exactly n options
	 */
	public void setN(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, N,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PickExactlyNBundleSelectionCriteria.n</code> attribute. 
	 * @param value the n - pick exactly n options
	 */
	public void setN(final Integer value)
	{
		setN( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PickExactlyNBundleSelectionCriteria.n</code> attribute. 
	 * @param value the n - pick exactly n options
	 */
	public void setN(final SessionContext ctx, final int value)
	{
		setN( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PickExactlyNBundleSelectionCriteria.n</code> attribute. 
	 * @param value the n - pick exactly n options
	 */
	public void setN(final int value)
	{
		setN( getSession().getSessionContext(), value );
	}
	
}
