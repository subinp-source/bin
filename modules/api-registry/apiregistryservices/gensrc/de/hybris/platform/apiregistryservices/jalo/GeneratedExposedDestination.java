/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 13-Dec-2021, 3:58:17 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.jalo;

import de.hybris.platform.apiregistryservices.constants.ApiregistryservicesConstants;
import de.hybris.platform.apiregistryservices.jalo.AbstractDestination;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.apiregistryservices.jalo.ExposedDestination ExposedDestination}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedExposedDestination extends AbstractDestination
{
	/** Qualifier of the <code>ExposedDestination.targetId</code> attribute **/
	public static final String TARGETID = "targetId";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(AbstractDestination.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(TARGETID, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ExposedDestination.targetId</code> attribute.
	 * @return the targetId - Unique Id of destination in the target system
	 */
	public String getTargetId(final SessionContext ctx)
	{
		return (String)getProperty( ctx, TARGETID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ExposedDestination.targetId</code> attribute.
	 * @return the targetId - Unique Id of destination in the target system
	 */
	public String getTargetId()
	{
		return getTargetId( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ExposedDestination.targetId</code> attribute. 
	 * @param value the targetId - Unique Id of destination in the target system
	 */
	public void setTargetId(final SessionContext ctx, final String value)
	{
		setProperty(ctx, TARGETID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ExposedDestination.targetId</code> attribute. 
	 * @param value the targetId - Unique Id of destination in the target system
	 */
	public void setTargetId(final String value)
	{
		setTargetId( getSession().getSessionContext(), value );
	}
	
}
