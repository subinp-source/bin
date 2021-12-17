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
import de.hybris.platform.apiregistryservices.jalo.AbstractCredential;
import de.hybris.platform.apiregistryservices.jalo.DestinationTarget;
import de.hybris.platform.apiregistryservices.jalo.Endpoint;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.apiregistryservices.jalo.AbstractDestination AbstractDestination}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedAbstractDestination extends GenericItem
{
	/** Qualifier of the <code>AbstractDestination.id</code> attribute **/
	public static final String ID = "id";
	/** Qualifier of the <code>AbstractDestination.url</code> attribute **/
	public static final String URL = "url";
	/** Qualifier of the <code>AbstractDestination.active</code> attribute **/
	public static final String ACTIVE = "active";
	/** Qualifier of the <code>AbstractDestination.additionalProperties</code> attribute **/
	public static final String ADDITIONALPROPERTIES = "additionalProperties";
	/** Qualifier of the <code>AbstractDestination.credential</code> attribute **/
	public static final String CREDENTIAL = "credential";
	/** Qualifier of the <code>AbstractDestination.endpoint</code> attribute **/
	public static final String ENDPOINT = "endpoint";
	/** Qualifier of the <code>AbstractDestination.destinationTarget</code> attribute **/
	public static final String DESTINATIONTARGET = "destinationTarget";
	/**
	* {@link BidirectionalOneToManyHandler} for handling 1:n ENDPOINT's relation attributes from 'one' side.
	**/
	protected static final BidirectionalOneToManyHandler<GeneratedAbstractDestination> ENDPOINTHANDLER = new BidirectionalOneToManyHandler<GeneratedAbstractDestination>(
	ApiregistryservicesConstants.TC.ABSTRACTDESTINATION,
	false,
	"endpoint",
	null,
	false,
	true,
	CollectionType.COLLECTION
	);
	/**
	* {@link BidirectionalOneToManyHandler} for handling 1:n DESTINATIONTARGET's relation attributes from 'one' side.
	**/
	protected static final BidirectionalOneToManyHandler<GeneratedAbstractDestination> DESTINATIONTARGETHANDLER = new BidirectionalOneToManyHandler<GeneratedAbstractDestination>(
	ApiregistryservicesConstants.TC.ABSTRACTDESTINATION,
	false,
	"destinationTarget",
	null,
	false,
	true,
	CollectionType.COLLECTION
	);
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(ID, AttributeMode.INITIAL);
		tmp.put(URL, AttributeMode.INITIAL);
		tmp.put(ACTIVE, AttributeMode.INITIAL);
		tmp.put(ADDITIONALPROPERTIES, AttributeMode.INITIAL);
		tmp.put(CREDENTIAL, AttributeMode.INITIAL);
		tmp.put(ENDPOINT, AttributeMode.INITIAL);
		tmp.put(DESTINATIONTARGET, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractDestination.active</code> attribute.
	 * @return the active - Active Flag. Indicates whether the destination can be used.
	 */
	public Boolean isActive(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, ACTIVE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractDestination.active</code> attribute.
	 * @return the active - Active Flag. Indicates whether the destination can be used.
	 */
	public Boolean isActive()
	{
		return isActive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractDestination.active</code> attribute. 
	 * @return the active - Active Flag. Indicates whether the destination can be used.
	 */
	public boolean isActiveAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isActive( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractDestination.active</code> attribute. 
	 * @return the active - Active Flag. Indicates whether the destination can be used.
	 */
	public boolean isActiveAsPrimitive()
	{
		return isActiveAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractDestination.active</code> attribute. 
	 * @param value the active - Active Flag. Indicates whether the destination can be used.
	 */
	public void setActive(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, ACTIVE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractDestination.active</code> attribute. 
	 * @param value the active - Active Flag. Indicates whether the destination can be used.
	 */
	public void setActive(final Boolean value)
	{
		setActive( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractDestination.active</code> attribute. 
	 * @param value the active - Active Flag. Indicates whether the destination can be used.
	 */
	public void setActive(final SessionContext ctx, final boolean value)
	{
		setActive( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractDestination.active</code> attribute. 
	 * @param value the active - Active Flag. Indicates whether the destination can be used.
	 */
	public void setActive(final boolean value)
	{
		setActive( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractDestination.additionalProperties</code> attribute.
	 * @return the additionalProperties - Map of Additional Properties
	 */
	public Map<String,String> getAllAdditionalProperties(final SessionContext ctx)
	{
		Map<String,String> map = (Map<String,String>)getProperty( ctx, ADDITIONALPROPERTIES);
		return map != null ? map : Collections.EMPTY_MAP;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractDestination.additionalProperties</code> attribute.
	 * @return the additionalProperties - Map of Additional Properties
	 */
	public Map<String,String> getAllAdditionalProperties()
	{
		return getAllAdditionalProperties( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractDestination.additionalProperties</code> attribute. 
	 * @param value the additionalProperties - Map of Additional Properties
	 */
	public void setAllAdditionalProperties(final SessionContext ctx, final Map<String,String> value)
	{
		setProperty(ctx, ADDITIONALPROPERTIES,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractDestination.additionalProperties</code> attribute. 
	 * @param value the additionalProperties - Map of Additional Properties
	 */
	public void setAllAdditionalProperties(final Map<String,String> value)
	{
		setAllAdditionalProperties( getSession().getSessionContext(), value );
	}
	
	@Override
	protected Item createItem(final SessionContext ctx, final ComposedType type, final ItemAttributeMap allAttributes) throws JaloBusinessException
	{
		ENDPOINTHANDLER.newInstance(ctx, allAttributes);
		DESTINATIONTARGETHANDLER.newInstance(ctx, allAttributes);
		return super.createItem( ctx, type, allAttributes );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractDestination.credential</code> attribute.
	 * @return the credential - Credential
	 */
	public AbstractCredential getCredential(final SessionContext ctx)
	{
		return (AbstractCredential)getProperty( ctx, CREDENTIAL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractDestination.credential</code> attribute.
	 * @return the credential - Credential
	 */
	public AbstractCredential getCredential()
	{
		return getCredential( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractDestination.credential</code> attribute. 
	 * @param value the credential - Credential
	 */
	public void setCredential(final SessionContext ctx, final AbstractCredential value)
	{
		setProperty(ctx, CREDENTIAL,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractDestination.credential</code> attribute. 
	 * @param value the credential - Credential
	 */
	public void setCredential(final AbstractCredential value)
	{
		setCredential( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractDestination.destinationTarget</code> attribute.
	 * @return the destinationTarget - Destination Target
	 */
	public DestinationTarget getDestinationTarget(final SessionContext ctx)
	{
		return (DestinationTarget)getProperty( ctx, DESTINATIONTARGET);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractDestination.destinationTarget</code> attribute.
	 * @return the destinationTarget - Destination Target
	 */
	public DestinationTarget getDestinationTarget()
	{
		return getDestinationTarget( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractDestination.destinationTarget</code> attribute. 
	 * @param value the destinationTarget - Destination Target
	 */
	protected void setDestinationTarget(final SessionContext ctx, final DestinationTarget value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		// initial-only attribute: make sure this attribute can be set during item creation only
		if ( ctx.getAttribute( "core.types.creation.initial") != Boolean.TRUE )
		{
			throw new JaloInvalidParameterException( "attribute '"+DESTINATIONTARGET+"' is not changeable", 0 );
		}
		DESTINATIONTARGETHANDLER.addValue( ctx, value, this  );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractDestination.destinationTarget</code> attribute. 
	 * @param value the destinationTarget - Destination Target
	 */
	protected void setDestinationTarget(final DestinationTarget value)
	{
		setDestinationTarget( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractDestination.endpoint</code> attribute.
	 * @return the endpoint - Endpoint
	 */
	public Endpoint getEndpoint(final SessionContext ctx)
	{
		return (Endpoint)getProperty( ctx, ENDPOINT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractDestination.endpoint</code> attribute.
	 * @return the endpoint - Endpoint
	 */
	public Endpoint getEndpoint()
	{
		return getEndpoint( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractDestination.endpoint</code> attribute. 
	 * @param value the endpoint - Endpoint
	 */
	public void setEndpoint(final SessionContext ctx, final Endpoint value)
	{
		ENDPOINTHANDLER.addValue( ctx, value, this  );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractDestination.endpoint</code> attribute. 
	 * @param value the endpoint - Endpoint
	 */
	public void setEndpoint(final Endpoint value)
	{
		setEndpoint( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractDestination.id</code> attribute.
	 * @return the id - Unique Id
	 */
	public String getId(final SessionContext ctx)
	{
		return (String)getProperty( ctx, ID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractDestination.id</code> attribute.
	 * @return the id - Unique Id
	 */
	public String getId()
	{
		return getId( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractDestination.id</code> attribute. 
	 * @param value the id - Unique Id
	 */
	protected void setId(final SessionContext ctx, final String value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		// initial-only attribute: make sure this attribute can be set during item creation only
		if ( ctx.getAttribute( "core.types.creation.initial") != Boolean.TRUE )
		{
			throw new JaloInvalidParameterException( "attribute '"+ID+"' is not changeable", 0 );
		}
		setProperty(ctx, ID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractDestination.id</code> attribute. 
	 * @param value the id - Unique Id
	 */
	protected void setId(final String value)
	{
		setId( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractDestination.url</code> attribute.
	 * @return the url - Destination URL
	 */
	public String getUrl(final SessionContext ctx)
	{
		return (String)getProperty( ctx, URL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractDestination.url</code> attribute.
	 * @return the url - Destination URL
	 */
	public String getUrl()
	{
		return getUrl( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractDestination.url</code> attribute. 
	 * @param value the url - Destination URL
	 */
	public void setUrl(final SessionContext ctx, final String value)
	{
		setProperty(ctx, URL,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractDestination.url</code> attribute. 
	 * @param value the url - Destination URL
	 */
	public void setUrl(final String value)
	{
		setUrl( getSession().getSessionContext(), value );
	}
	
}
