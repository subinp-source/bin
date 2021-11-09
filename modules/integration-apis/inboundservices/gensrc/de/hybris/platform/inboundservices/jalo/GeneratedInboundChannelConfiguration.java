/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 06-Oct-2021, 11:13:07 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.inboundservices.jalo;

import de.hybris.platform.apiregistryservices.constants.ApiregistryservicesConstants;
import de.hybris.platform.apiregistryservices.jalo.ExposedDestination;
import de.hybris.platform.inboundservices.constants.InboundservicesConstants;
import de.hybris.platform.integrationservices.jalo.IntegrationObject;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.jalo.GenericItem InboundChannelConfiguration}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedInboundChannelConfiguration extends GenericItem
{
	/** Qualifier of the <code>InboundChannelConfiguration.integrationObject</code> attribute **/
	public static final String INTEGRATIONOBJECT = "integrationObject";
	/** Qualifier of the <code>InboundChannelConfiguration.authenticationType</code> attribute **/
	public static final String AUTHENTICATIONTYPE = "authenticationType";
	/** Qualifier of the <code>InboundChannelConfiguration.exposedDestinations</code> attribute **/
	public static final String EXPOSEDDESTINATIONS = "exposedDestinations";
	/**
	* {@link OneToManyHandler} for handling 1:n EXPOSEDDESTINATIONS's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<ExposedDestination> EXPOSEDDESTINATIONSHANDLER = new OneToManyHandler<ExposedDestination>(
	ApiregistryservicesConstants.TC.EXPOSEDDESTINATION,
	false,
	"inboundChannelConfiguration",
	"inboundChannelConfigurationPOS",
	true,
	true,
	CollectionType.LIST
	);
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(INTEGRATIONOBJECT, AttributeMode.INITIAL);
		tmp.put(AUTHENTICATIONTYPE, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>InboundChannelConfiguration.authenticationType</code> attribute.
	 * @return the authenticationType - Type of authentication for an integration object in an Inbound request, which can be of
	 *                         a type defined in the AuthenticationType Enum
	 */
	public EnumerationValue getAuthenticationType(final SessionContext ctx)
	{
		return (EnumerationValue)getProperty( ctx, AUTHENTICATIONTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>InboundChannelConfiguration.authenticationType</code> attribute.
	 * @return the authenticationType - Type of authentication for an integration object in an Inbound request, which can be of
	 *                         a type defined in the AuthenticationType Enum
	 */
	public EnumerationValue getAuthenticationType()
	{
		return getAuthenticationType( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>InboundChannelConfiguration.authenticationType</code> attribute. 
	 * @param value the authenticationType - Type of authentication for an integration object in an Inbound request, which can be of
	 *                         a type defined in the AuthenticationType Enum
	 */
	public void setAuthenticationType(final SessionContext ctx, final EnumerationValue value)
	{
		setProperty(ctx, AUTHENTICATIONTYPE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>InboundChannelConfiguration.authenticationType</code> attribute. 
	 * @param value the authenticationType - Type of authentication for an integration object in an Inbound request, which can be of
	 *                         a type defined in the AuthenticationType Enum
	 */
	public void setAuthenticationType(final EnumerationValue value)
	{
		setAuthenticationType( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>InboundChannelConfiguration.exposedDestinations</code> attribute.
	 * @return the exposedDestinations
	 */
	public List<ExposedDestination> getExposedDestinations(final SessionContext ctx)
	{
		return (List<ExposedDestination>)EXPOSEDDESTINATIONSHANDLER.getValues( ctx, this );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>InboundChannelConfiguration.exposedDestinations</code> attribute.
	 * @return the exposedDestinations
	 */
	public List<ExposedDestination> getExposedDestinations()
	{
		return getExposedDestinations( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>InboundChannelConfiguration.exposedDestinations</code> attribute. 
	 * @param value the exposedDestinations
	 */
	public void setExposedDestinations(final SessionContext ctx, final List<ExposedDestination> value)
	{
		EXPOSEDDESTINATIONSHANDLER.setValues( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>InboundChannelConfiguration.exposedDestinations</code> attribute. 
	 * @param value the exposedDestinations
	 */
	public void setExposedDestinations(final List<ExposedDestination> value)
	{
		setExposedDestinations( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to exposedDestinations. 
	 * @param value the item to add to exposedDestinations
	 */
	public void addToExposedDestinations(final SessionContext ctx, final ExposedDestination value)
	{
		EXPOSEDDESTINATIONSHANDLER.addValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to exposedDestinations. 
	 * @param value the item to add to exposedDestinations
	 */
	public void addToExposedDestinations(final ExposedDestination value)
	{
		addToExposedDestinations( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from exposedDestinations. 
	 * @param value the item to remove from exposedDestinations
	 */
	public void removeFromExposedDestinations(final SessionContext ctx, final ExposedDestination value)
	{
		EXPOSEDDESTINATIONSHANDLER.removeValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from exposedDestinations. 
	 * @param value the item to remove from exposedDestinations
	 */
	public void removeFromExposedDestinations(final ExposedDestination value)
	{
		removeFromExposedDestinations( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>InboundChannelConfiguration.integrationObject</code> attribute.
	 * @return the integrationObject - Integration Object configured with authentication for an Inbound Request
	 */
	public IntegrationObject getIntegrationObject(final SessionContext ctx)
	{
		return (IntegrationObject)getProperty( ctx, INTEGRATIONOBJECT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>InboundChannelConfiguration.integrationObject</code> attribute.
	 * @return the integrationObject - Integration Object configured with authentication for an Inbound Request
	 */
	public IntegrationObject getIntegrationObject()
	{
		return getIntegrationObject( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>InboundChannelConfiguration.integrationObject</code> attribute. 
	 * @param value the integrationObject - Integration Object configured with authentication for an Inbound Request
	 */
	public void setIntegrationObject(final SessionContext ctx, final IntegrationObject value)
	{
		setProperty(ctx, INTEGRATIONOBJECT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>InboundChannelConfiguration.integrationObject</code> attribute. 
	 * @param value the integrationObject - Integration Object configured with authentication for an Inbound Request
	 */
	public void setIntegrationObject(final IntegrationObject value)
	{
		setIntegrationObject( getSession().getSessionContext(), value );
	}
	
}
