/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 13-Dec-2021, 3:58:17 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.jalo;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.searchservices.constants.SearchservicesConstants;
import de.hybris.platform.searchservices.jalo.SnIndexConfiguration;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.jalo.GenericItem AbstractSnSearchProviderConfiguration}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedAbstractSnSearchProviderConfiguration extends GenericItem
{
	/** Qualifier of the <code>AbstractSnSearchProviderConfiguration.id</code> attribute **/
	public static final String ID = "id";
	/** Qualifier of the <code>AbstractSnSearchProviderConfiguration.name</code> attribute **/
	public static final String NAME = "name";
	/** Qualifier of the <code>AbstractSnSearchProviderConfiguration.listeners</code> attribute **/
	public static final String LISTENERS = "listeners";
	/** Qualifier of the <code>AbstractSnSearchProviderConfiguration.indexConfigurations</code> attribute **/
	public static final String INDEXCONFIGURATIONS = "indexConfigurations";
	/**
	* {@link OneToManyHandler} for handling 1:n INDEXCONFIGURATIONS's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<SnIndexConfiguration> INDEXCONFIGURATIONSHANDLER = new OneToManyHandler<SnIndexConfiguration>(
	SearchservicesConstants.TC.SNINDEXCONFIGURATION,
	false,
	"searchProviderConfiguration",
	null,
	false,
	true,
	CollectionType.LIST
	);
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(ID, AttributeMode.INITIAL);
		tmp.put(NAME, AttributeMode.INITIAL);
		tmp.put(LISTENERS, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractSnSearchProviderConfiguration.id</code> attribute.
	 * @return the id
	 */
	public String getId(final SessionContext ctx)
	{
		return (String)getProperty( ctx, ID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractSnSearchProviderConfiguration.id</code> attribute.
	 * @return the id
	 */
	public String getId()
	{
		return getId( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractSnSearchProviderConfiguration.id</code> attribute. 
	 * @param value the id
	 */
	public void setId(final SessionContext ctx, final String value)
	{
		setProperty(ctx, ID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractSnSearchProviderConfiguration.id</code> attribute. 
	 * @param value the id
	 */
	public void setId(final String value)
	{
		setId( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractSnSearchProviderConfiguration.indexConfigurations</code> attribute.
	 * @return the indexConfigurations
	 */
	public List<SnIndexConfiguration> getIndexConfigurations(final SessionContext ctx)
	{
		return (List<SnIndexConfiguration>)INDEXCONFIGURATIONSHANDLER.getValues( ctx, this );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractSnSearchProviderConfiguration.indexConfigurations</code> attribute.
	 * @return the indexConfigurations
	 */
	public List<SnIndexConfiguration> getIndexConfigurations()
	{
		return getIndexConfigurations( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractSnSearchProviderConfiguration.indexConfigurations</code> attribute. 
	 * @param value the indexConfigurations
	 */
	public void setIndexConfigurations(final SessionContext ctx, final List<SnIndexConfiguration> value)
	{
		INDEXCONFIGURATIONSHANDLER.setValues( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractSnSearchProviderConfiguration.indexConfigurations</code> attribute. 
	 * @param value the indexConfigurations
	 */
	public void setIndexConfigurations(final List<SnIndexConfiguration> value)
	{
		setIndexConfigurations( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to indexConfigurations. 
	 * @param value the item to add to indexConfigurations
	 */
	public void addToIndexConfigurations(final SessionContext ctx, final SnIndexConfiguration value)
	{
		INDEXCONFIGURATIONSHANDLER.addValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to indexConfigurations. 
	 * @param value the item to add to indexConfigurations
	 */
	public void addToIndexConfigurations(final SnIndexConfiguration value)
	{
		addToIndexConfigurations( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from indexConfigurations. 
	 * @param value the item to remove from indexConfigurations
	 */
	public void removeFromIndexConfigurations(final SessionContext ctx, final SnIndexConfiguration value)
	{
		INDEXCONFIGURATIONSHANDLER.removeValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from indexConfigurations. 
	 * @param value the item to remove from indexConfigurations
	 */
	public void removeFromIndexConfigurations(final SnIndexConfiguration value)
	{
		removeFromIndexConfigurations( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractSnSearchProviderConfiguration.listeners</code> attribute.
	 * @return the listeners
	 */
	public List<String> getListeners(final SessionContext ctx)
	{
		List<String> coll = (List<String>)getProperty( ctx, LISTENERS);
		return coll != null ? coll : Collections.EMPTY_LIST;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractSnSearchProviderConfiguration.listeners</code> attribute.
	 * @return the listeners
	 */
	public List<String> getListeners()
	{
		return getListeners( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractSnSearchProviderConfiguration.listeners</code> attribute. 
	 * @param value the listeners
	 */
	public void setListeners(final SessionContext ctx, final List<String> value)
	{
		setProperty(ctx, LISTENERS,value == null || !value.isEmpty() ? value : null );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractSnSearchProviderConfiguration.listeners</code> attribute. 
	 * @param value the listeners
	 */
	public void setListeners(final List<String> value)
	{
		setListeners( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractSnSearchProviderConfiguration.name</code> attribute.
	 * @return the name
	 */
	public String getName(final SessionContext ctx)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedAbstractSnSearchProviderConfiguration.getName requires a session language", 0 );
		}
		return (String)getLocalizedProperty( ctx, NAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractSnSearchProviderConfiguration.name</code> attribute.
	 * @return the name
	 */
	public String getName()
	{
		return getName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractSnSearchProviderConfiguration.name</code> attribute. 
	 * @return the localized name
	 */
	public Map<Language,String> getAllName(final SessionContext ctx)
	{
		return (Map<Language,String>)getAllLocalizedProperties(ctx,NAME,C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractSnSearchProviderConfiguration.name</code> attribute. 
	 * @return the localized name
	 */
	public Map<Language,String> getAllName()
	{
		return getAllName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractSnSearchProviderConfiguration.name</code> attribute. 
	 * @param value the name
	 */
	public void setName(final SessionContext ctx, final String value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		if( ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedAbstractSnSearchProviderConfiguration.setName requires a session language", 0 );
		}
		setLocalizedProperty(ctx, NAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractSnSearchProviderConfiguration.name</code> attribute. 
	 * @param value the name
	 */
	public void setName(final String value)
	{
		setName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractSnSearchProviderConfiguration.name</code> attribute. 
	 * @param value the name
	 */
	public void setAllName(final SessionContext ctx, final Map<Language,String> value)
	{
		setAllLocalizedProperties(ctx,NAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractSnSearchProviderConfiguration.name</code> attribute. 
	 * @param value the name
	 */
	public void setAllName(final Map<Language,String> value)
	{
		setAllName( getSession().getSessionContext(), value );
	}
	
}
