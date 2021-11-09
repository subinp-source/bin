/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 06-Oct-2021, 11:13:07 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.subscriptionservices.jalo;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.subscriptionservices.constants.SubscriptionservicesConstants;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.subscriptionservices.jalo.UsageUnit UsageUnit}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedUsageUnit extends GenericItem
{
	/** Qualifier of the <code>UsageUnit.id</code> attribute **/
	public static final String ID = "id";
	/** Qualifier of the <code>UsageUnit.name</code> attribute **/
	public static final String NAME = "name";
	/** Qualifier of the <code>UsageUnit.namePlural</code> attribute **/
	public static final String NAMEPLURAL = "namePlural";
	/** Qualifier of the <code>UsageUnit.accumulative</code> attribute **/
	public static final String ACCUMULATIVE = "accumulative";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(ID, AttributeMode.INITIAL);
		tmp.put(NAME, AttributeMode.INITIAL);
		tmp.put(NAMEPLURAL, AttributeMode.INITIAL);
		tmp.put(ACCUMULATIVE, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UsageUnit.accumulative</code> attribute.
	 * @return the accumulative - Accumulative
	 */
	public Boolean isAccumulative(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, ACCUMULATIVE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UsageUnit.accumulative</code> attribute.
	 * @return the accumulative - Accumulative
	 */
	public Boolean isAccumulative()
	{
		return isAccumulative( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UsageUnit.accumulative</code> attribute. 
	 * @return the accumulative - Accumulative
	 */
	public boolean isAccumulativeAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isAccumulative( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UsageUnit.accumulative</code> attribute. 
	 * @return the accumulative - Accumulative
	 */
	public boolean isAccumulativeAsPrimitive()
	{
		return isAccumulativeAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>UsageUnit.accumulative</code> attribute. 
	 * @param value the accumulative - Accumulative
	 */
	public void setAccumulative(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, ACCUMULATIVE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>UsageUnit.accumulative</code> attribute. 
	 * @param value the accumulative - Accumulative
	 */
	public void setAccumulative(final Boolean value)
	{
		setAccumulative( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>UsageUnit.accumulative</code> attribute. 
	 * @param value the accumulative - Accumulative
	 */
	public void setAccumulative(final SessionContext ctx, final boolean value)
	{
		setAccumulative( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>UsageUnit.accumulative</code> attribute. 
	 * @param value the accumulative - Accumulative
	 */
	public void setAccumulative(final boolean value)
	{
		setAccumulative( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UsageUnit.id</code> attribute.
	 * @return the id - Identifier
	 */
	public String getId(final SessionContext ctx)
	{
		return (String)getProperty( ctx, ID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UsageUnit.id</code> attribute.
	 * @return the id - Identifier
	 */
	public String getId()
	{
		return getId( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>UsageUnit.id</code> attribute. 
	 * @param value the id - Identifier
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
	 * <i>Generated method</i> - Setter of the <code>UsageUnit.id</code> attribute. 
	 * @param value the id - Identifier
	 */
	protected void setId(final String value)
	{
		setId( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UsageUnit.name</code> attribute.
	 * @return the name - Name
	 */
	public String getName(final SessionContext ctx)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedUsageUnit.getName requires a session language", 0 );
		}
		return (String)getLocalizedProperty( ctx, NAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UsageUnit.name</code> attribute.
	 * @return the name - Name
	 */
	public String getName()
	{
		return getName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UsageUnit.name</code> attribute. 
	 * @return the localized name - Name
	 */
	public Map<Language,String> getAllName(final SessionContext ctx)
	{
		return (Map<Language,String>)getAllLocalizedProperties(ctx,NAME,C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UsageUnit.name</code> attribute. 
	 * @return the localized name - Name
	 */
	public Map<Language,String> getAllName()
	{
		return getAllName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>UsageUnit.name</code> attribute. 
	 * @param value the name - Name
	 */
	public void setName(final SessionContext ctx, final String value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		if( ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedUsageUnit.setName requires a session language", 0 );
		}
		setLocalizedProperty(ctx, NAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>UsageUnit.name</code> attribute. 
	 * @param value the name - Name
	 */
	public void setName(final String value)
	{
		setName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>UsageUnit.name</code> attribute. 
	 * @param value the name - Name
	 */
	public void setAllName(final SessionContext ctx, final Map<Language,String> value)
	{
		setAllLocalizedProperties(ctx,NAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>UsageUnit.name</code> attribute. 
	 * @param value the name - Name
	 */
	public void setAllName(final Map<Language,String> value)
	{
		setAllName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UsageUnit.namePlural</code> attribute.
	 * @return the namePlural - Name (Plural)
	 */
	public String getNamePlural(final SessionContext ctx)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedUsageUnit.getNamePlural requires a session language", 0 );
		}
		return (String)getLocalizedProperty( ctx, NAMEPLURAL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UsageUnit.namePlural</code> attribute.
	 * @return the namePlural - Name (Plural)
	 */
	public String getNamePlural()
	{
		return getNamePlural( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UsageUnit.namePlural</code> attribute. 
	 * @return the localized namePlural - Name (Plural)
	 */
	public Map<Language,String> getAllNamePlural(final SessionContext ctx)
	{
		return (Map<Language,String>)getAllLocalizedProperties(ctx,NAMEPLURAL,C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UsageUnit.namePlural</code> attribute. 
	 * @return the localized namePlural - Name (Plural)
	 */
	public Map<Language,String> getAllNamePlural()
	{
		return getAllNamePlural( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>UsageUnit.namePlural</code> attribute. 
	 * @param value the namePlural - Name (Plural)
	 */
	public void setNamePlural(final SessionContext ctx, final String value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		if( ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedUsageUnit.setNamePlural requires a session language", 0 );
		}
		setLocalizedProperty(ctx, NAMEPLURAL,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>UsageUnit.namePlural</code> attribute. 
	 * @param value the namePlural - Name (Plural)
	 */
	public void setNamePlural(final String value)
	{
		setNamePlural( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>UsageUnit.namePlural</code> attribute. 
	 * @param value the namePlural - Name (Plural)
	 */
	public void setAllNamePlural(final SessionContext ctx, final Map<Language,String> value)
	{
		setAllLocalizedProperties(ctx,NAMEPLURAL,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>UsageUnit.namePlural</code> attribute. 
	 * @param value the namePlural - Name (Plural)
	 */
	public void setAllNamePlural(final Map<Language,String> value)
	{
		setAllNamePlural( getSession().getSessionContext(), value );
	}
	
}
