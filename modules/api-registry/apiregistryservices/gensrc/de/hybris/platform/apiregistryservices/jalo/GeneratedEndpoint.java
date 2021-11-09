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
import de.hybris.platform.apiregistryservices.jalo.AbstractDestination;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.apiregistryservices.jalo.Endpoint Endpoint}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedEndpoint extends GenericItem
{
	/** Qualifier of the <code>Endpoint.id</code> attribute **/
	public static final String ID = "id";
	/** Qualifier of the <code>Endpoint.version</code> attribute **/
	public static final String VERSION = "version";
	/** Qualifier of the <code>Endpoint.name</code> attribute **/
	public static final String NAME = "name";
	/** Qualifier of the <code>Endpoint.description</code> attribute **/
	public static final String DESCRIPTION = "description";
	/** Qualifier of the <code>Endpoint.specUrl</code> attribute **/
	public static final String SPECURL = "specUrl";
	/** Qualifier of the <code>Endpoint.specData</code> attribute **/
	public static final String SPECDATA = "specData";
	/** Qualifier of the <code>Endpoint.extensionName</code> attribute **/
	public static final String EXTENSIONNAME = "extensionName";
	/** Qualifier of the <code>Endpoint.destinations</code> attribute **/
	public static final String DESTINATIONS = "destinations";
	/**
	* {@link OneToManyHandler} for handling 1:n DESTINATIONS's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<AbstractDestination> DESTINATIONSHANDLER = new OneToManyHandler<AbstractDestination>(
	ApiregistryservicesConstants.TC.ABSTRACTDESTINATION,
	true,
	"endpoint",
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
		tmp.put(VERSION, AttributeMode.INITIAL);
		tmp.put(NAME, AttributeMode.INITIAL);
		tmp.put(DESCRIPTION, AttributeMode.INITIAL);
		tmp.put(SPECURL, AttributeMode.INITIAL);
		tmp.put(SPECDATA, AttributeMode.INITIAL);
		tmp.put(EXTENSIONNAME, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Endpoint.description</code> attribute.
	 * @return the description - Short Description
	 */
	public String getDescription(final SessionContext ctx)
	{
		return (String)getProperty( ctx, DESCRIPTION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Endpoint.description</code> attribute.
	 * @return the description - Short Description
	 */
	public String getDescription()
	{
		return getDescription( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Endpoint.description</code> attribute. 
	 * @param value the description - Short Description
	 */
	public void setDescription(final SessionContext ctx, final String value)
	{
		setProperty(ctx, DESCRIPTION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Endpoint.description</code> attribute. 
	 * @param value the description - Short Description
	 */
	public void setDescription(final String value)
	{
		setDescription( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Endpoint.destinations</code> attribute.
	 * @return the destinations - Destinations
	 */
	public Collection<AbstractDestination> getDestinations(final SessionContext ctx)
	{
		return DESTINATIONSHANDLER.getValues( ctx, this );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Endpoint.destinations</code> attribute.
	 * @return the destinations - Destinations
	 */
	public Collection<AbstractDestination> getDestinations()
	{
		return getDestinations( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Endpoint.destinations</code> attribute. 
	 * @param value the destinations - Destinations
	 */
	public void setDestinations(final SessionContext ctx, final Collection<AbstractDestination> value)
	{
		DESTINATIONSHANDLER.setValues( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Endpoint.destinations</code> attribute. 
	 * @param value the destinations - Destinations
	 */
	public void setDestinations(final Collection<AbstractDestination> value)
	{
		setDestinations( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to destinations. 
	 * @param value the item to add to destinations - Destinations
	 */
	public void addToDestinations(final SessionContext ctx, final AbstractDestination value)
	{
		DESTINATIONSHANDLER.addValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to destinations. 
	 * @param value the item to add to destinations - Destinations
	 */
	public void addToDestinations(final AbstractDestination value)
	{
		addToDestinations( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from destinations. 
	 * @param value the item to remove from destinations - Destinations
	 */
	public void removeFromDestinations(final SessionContext ctx, final AbstractDestination value)
	{
		DESTINATIONSHANDLER.removeValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from destinations. 
	 * @param value the item to remove from destinations - Destinations
	 */
	public void removeFromDestinations(final AbstractDestination value)
	{
		removeFromDestinations( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Endpoint.extensionName</code> attribute.
	 * @return the extensionName - Endpoint Extension
	 */
	public String getExtensionName(final SessionContext ctx)
	{
		return (String)getProperty( ctx, EXTENSIONNAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Endpoint.extensionName</code> attribute.
	 * @return the extensionName - Endpoint Extension
	 */
	public String getExtensionName()
	{
		return getExtensionName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Endpoint.extensionName</code> attribute. 
	 * @param value the extensionName - Endpoint Extension
	 */
	public void setExtensionName(final SessionContext ctx, final String value)
	{
		setProperty(ctx, EXTENSIONNAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Endpoint.extensionName</code> attribute. 
	 * @param value the extensionName - Endpoint Extension
	 */
	public void setExtensionName(final String value)
	{
		setExtensionName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Endpoint.id</code> attribute.
	 * @return the id - Unique Id
	 */
	public String getId(final SessionContext ctx)
	{
		return (String)getProperty( ctx, ID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Endpoint.id</code> attribute.
	 * @return the id - Unique Id
	 */
	public String getId()
	{
		return getId( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Endpoint.id</code> attribute. 
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
	 * <i>Generated method</i> - Setter of the <code>Endpoint.id</code> attribute. 
	 * @param value the id - Unique Id
	 */
	protected void setId(final String value)
	{
		setId( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Endpoint.name</code> attribute.
	 * @return the name - Human-readable Name
	 */
	public String getName(final SessionContext ctx)
	{
		return (String)getProperty( ctx, NAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Endpoint.name</code> attribute.
	 * @return the name - Human-readable Name
	 */
	public String getName()
	{
		return getName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Endpoint.name</code> attribute. 
	 * @param value the name - Human-readable Name
	 */
	public void setName(final SessionContext ctx, final String value)
	{
		setProperty(ctx, NAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Endpoint.name</code> attribute. 
	 * @param value the name - Human-readable Name
	 */
	public void setName(final String value)
	{
		setName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Endpoint.specData</code> attribute.
	 * @return the specData - Endpoint specification data, in case url is absent
	 */
	public String getSpecData(final SessionContext ctx)
	{
		return (String)getProperty( ctx, SPECDATA);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Endpoint.specData</code> attribute.
	 * @return the specData - Endpoint specification data, in case url is absent
	 */
	public String getSpecData()
	{
		return getSpecData( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Endpoint.specData</code> attribute. 
	 * @param value the specData - Endpoint specification data, in case url is absent
	 */
	public void setSpecData(final SessionContext ctx, final String value)
	{
		setProperty(ctx, SPECDATA,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Endpoint.specData</code> attribute. 
	 * @param value the specData - Endpoint specification data, in case url is absent
	 */
	public void setSpecData(final String value)
	{
		setSpecData( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Endpoint.specUrl</code> attribute.
	 * @return the specUrl - Endpoint Specification URL
	 */
	public String getSpecUrl(final SessionContext ctx)
	{
		return (String)getProperty( ctx, SPECURL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Endpoint.specUrl</code> attribute.
	 * @return the specUrl - Endpoint Specification URL
	 */
	public String getSpecUrl()
	{
		return getSpecUrl( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Endpoint.specUrl</code> attribute. 
	 * @param value the specUrl - Endpoint Specification URL
	 */
	public void setSpecUrl(final SessionContext ctx, final String value)
	{
		setProperty(ctx, SPECURL,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Endpoint.specUrl</code> attribute. 
	 * @param value the specUrl - Endpoint Specification URL
	 */
	public void setSpecUrl(final String value)
	{
		setSpecUrl( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Endpoint.version</code> attribute.
	 * @return the version - Endpoint Version
	 */
	public String getVersion(final SessionContext ctx)
	{
		return (String)getProperty( ctx, VERSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Endpoint.version</code> attribute.
	 * @return the version - Endpoint Version
	 */
	public String getVersion()
	{
		return getVersion( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Endpoint.version</code> attribute. 
	 * @param value the version - Endpoint Version
	 */
	protected void setVersion(final SessionContext ctx, final String value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		// initial-only attribute: make sure this attribute can be set during item creation only
		if ( ctx.getAttribute( "core.types.creation.initial") != Boolean.TRUE )
		{
			throw new JaloInvalidParameterException( "attribute '"+VERSION+"' is not changeable", 0 );
		}
		setProperty(ctx, VERSION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Endpoint.version</code> attribute. 
	 * @param value the version - Endpoint Version
	 */
	protected void setVersion(final String value)
	{
		setVersion( getSession().getSessionContext(), value );
	}
	
}
