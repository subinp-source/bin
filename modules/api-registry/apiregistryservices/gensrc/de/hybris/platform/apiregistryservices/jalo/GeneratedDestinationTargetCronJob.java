/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 08-Nov-2021, 4:51:25 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.jalo;

import de.hybris.platform.apiregistryservices.constants.ApiregistryservicesConstants;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.servicelayer.internal.jalo.ServicelayerJob;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.apiregistryservices.jalo.DestinationTargetCronJob DestinationTargetCronJob}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedDestinationTargetCronJob extends ServicelayerJob
{
	/** Qualifier of the <code>DestinationTargetCronJob.destinationTargetId</code> attribute **/
	public static final String DESTINATIONTARGETID = "destinationTargetId";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(ServicelayerJob.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(DESTINATIONTARGETID, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DestinationTargetCronJob.destinationTargetId</code> attribute.
	 * @return the destinationTargetId - ID of DestinationTarget to be processed
	 */
	public String getDestinationTargetId(final SessionContext ctx)
	{
		return (String)getProperty( ctx, DESTINATIONTARGETID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DestinationTargetCronJob.destinationTargetId</code> attribute.
	 * @return the destinationTargetId - ID of DestinationTarget to be processed
	 */
	public String getDestinationTargetId()
	{
		return getDestinationTargetId( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>DestinationTargetCronJob.destinationTargetId</code> attribute. 
	 * @param value the destinationTargetId - ID of DestinationTarget to be processed
	 */
	public void setDestinationTargetId(final SessionContext ctx, final String value)
	{
		setProperty(ctx, DESTINATIONTARGETID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>DestinationTargetCronJob.destinationTargetId</code> attribute. 
	 * @param value the destinationTargetId - ID of DestinationTarget to be processed
	 */
	public void setDestinationTargetId(final String value)
	{
		setDestinationTargetId( getSession().getSessionContext(), value );
	}
	
}
