/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 11-Dec-2021, 12:32:58 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.notificationservices.jalo;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.notificationservices.constants.NotificationservicesConstants;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.notificationservices.jalo.SiteMessage SiteMessage}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedSiteMessage extends GenericItem
{
	/** Qualifier of the <code>SiteMessage.uid</code> attribute **/
	public static final String UID = "uid";
	/** Qualifier of the <code>SiteMessage.title</code> attribute **/
	public static final String TITLE = "title";
	/** Qualifier of the <code>SiteMessage.subject</code> attribute **/
	public static final String SUBJECT = "subject";
	/** Qualifier of the <code>SiteMessage.content</code> attribute **/
	public static final String CONTENT = "content";
	/** Qualifier of the <code>SiteMessage.body</code> attribute **/
	public static final String BODY = "body";
	/** Qualifier of the <code>SiteMessage.externalItem</code> attribute **/
	public static final String EXTERNALITEM = "externalItem";
	/** Qualifier of the <code>SiteMessage.type</code> attribute **/
	public static final String TYPE = "type";
	/** Qualifier of the <code>SiteMessage.notificationType</code> attribute **/
	public static final String NOTIFICATIONTYPE = "notificationType";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(UID, AttributeMode.INITIAL);
		tmp.put(TITLE, AttributeMode.INITIAL);
		tmp.put(SUBJECT, AttributeMode.INITIAL);
		tmp.put(CONTENT, AttributeMode.INITIAL);
		tmp.put(BODY, AttributeMode.INITIAL);
		tmp.put(EXTERNALITEM, AttributeMode.INITIAL);
		tmp.put(TYPE, AttributeMode.INITIAL);
		tmp.put(NOTIFICATIONTYPE, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SiteMessage.body</code> attribute.
	 * @return the body
	 */
	public String getBody(final SessionContext ctx)
	{
		return (String)getProperty( ctx, BODY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SiteMessage.body</code> attribute.
	 * @return the body
	 */
	public String getBody()
	{
		return getBody( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SiteMessage.body</code> attribute. 
	 * @param value the body
	 */
	public void setBody(final SessionContext ctx, final String value)
	{
		setProperty(ctx, BODY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SiteMessage.body</code> attribute. 
	 * @param value the body
	 */
	public void setBody(final String value)
	{
		setBody( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SiteMessage.content</code> attribute.
	 * @return the content - Deprecated since 1905, will be replace by body.
	 */
	public String getContent(final SessionContext ctx)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedSiteMessage.getContent requires a session language", 0 );
		}
		return (String)getLocalizedProperty( ctx, CONTENT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SiteMessage.content</code> attribute.
	 * @return the content - Deprecated since 1905, will be replace by body.
	 */
	public String getContent()
	{
		return getContent( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SiteMessage.content</code> attribute. 
	 * @return the localized content - Deprecated since 1905, will be replace by body.
	 */
	public Map<Language,String> getAllContent(final SessionContext ctx)
	{
		return (Map<Language,String>)getAllLocalizedProperties(ctx,CONTENT,C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SiteMessage.content</code> attribute. 
	 * @return the localized content - Deprecated since 1905, will be replace by body.
	 */
	public Map<Language,String> getAllContent()
	{
		return getAllContent( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SiteMessage.content</code> attribute. 
	 * @param value the content - Deprecated since 1905, will be replace by body.
	 */
	public void setContent(final SessionContext ctx, final String value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		if( ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedSiteMessage.setContent requires a session language", 0 );
		}
		setLocalizedProperty(ctx, CONTENT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SiteMessage.content</code> attribute. 
	 * @param value the content - Deprecated since 1905, will be replace by body.
	 */
	public void setContent(final String value)
	{
		setContent( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SiteMessage.content</code> attribute. 
	 * @param value the content - Deprecated since 1905, will be replace by body.
	 */
	public void setAllContent(final SessionContext ctx, final Map<Language,String> value)
	{
		setAllLocalizedProperties(ctx,CONTENT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SiteMessage.content</code> attribute. 
	 * @param value the content - Deprecated since 1905, will be replace by body.
	 */
	public void setAllContent(final Map<Language,String> value)
	{
		setAllContent( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SiteMessage.externalItem</code> attribute.
	 * @return the externalItem
	 */
	public GenericItem getExternalItem(final SessionContext ctx)
	{
		return (GenericItem)getProperty( ctx, EXTERNALITEM);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SiteMessage.externalItem</code> attribute.
	 * @return the externalItem
	 */
	public GenericItem getExternalItem()
	{
		return getExternalItem( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SiteMessage.externalItem</code> attribute. 
	 * @param value the externalItem
	 */
	public void setExternalItem(final SessionContext ctx, final GenericItem value)
	{
		setProperty(ctx, EXTERNALITEM,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SiteMessage.externalItem</code> attribute. 
	 * @param value the externalItem
	 */
	public void setExternalItem(final GenericItem value)
	{
		setExternalItem( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SiteMessage.notificationType</code> attribute.
	 * @return the notificationType
	 */
	public EnumerationValue getNotificationType(final SessionContext ctx)
	{
		return (EnumerationValue)getProperty( ctx, NOTIFICATIONTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SiteMessage.notificationType</code> attribute.
	 * @return the notificationType
	 */
	public EnumerationValue getNotificationType()
	{
		return getNotificationType( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SiteMessage.notificationType</code> attribute. 
	 * @param value the notificationType
	 */
	public void setNotificationType(final SessionContext ctx, final EnumerationValue value)
	{
		setProperty(ctx, NOTIFICATIONTYPE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SiteMessage.notificationType</code> attribute. 
	 * @param value the notificationType
	 */
	public void setNotificationType(final EnumerationValue value)
	{
		setNotificationType( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SiteMessage.subject</code> attribute.
	 * @return the subject
	 */
	public String getSubject(final SessionContext ctx)
	{
		return (String)getProperty( ctx, SUBJECT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SiteMessage.subject</code> attribute.
	 * @return the subject
	 */
	public String getSubject()
	{
		return getSubject( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SiteMessage.subject</code> attribute. 
	 * @param value the subject
	 */
	public void setSubject(final SessionContext ctx, final String value)
	{
		setProperty(ctx, SUBJECT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SiteMessage.subject</code> attribute. 
	 * @param value the subject
	 */
	public void setSubject(final String value)
	{
		setSubject( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SiteMessage.title</code> attribute.
	 * @return the title - Deprecated since 1905, will be replace by subject.
	 */
	public String getTitle(final SessionContext ctx)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedSiteMessage.getTitle requires a session language", 0 );
		}
		return (String)getLocalizedProperty( ctx, TITLE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SiteMessage.title</code> attribute.
	 * @return the title - Deprecated since 1905, will be replace by subject.
	 */
	public String getTitle()
	{
		return getTitle( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SiteMessage.title</code> attribute. 
	 * @return the localized title - Deprecated since 1905, will be replace by subject.
	 */
	public Map<Language,String> getAllTitle(final SessionContext ctx)
	{
		return (Map<Language,String>)getAllLocalizedProperties(ctx,TITLE,C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SiteMessage.title</code> attribute. 
	 * @return the localized title - Deprecated since 1905, will be replace by subject.
	 */
	public Map<Language,String> getAllTitle()
	{
		return getAllTitle( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SiteMessage.title</code> attribute. 
	 * @param value the title - Deprecated since 1905, will be replace by subject.
	 */
	public void setTitle(final SessionContext ctx, final String value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		if( ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedSiteMessage.setTitle requires a session language", 0 );
		}
		setLocalizedProperty(ctx, TITLE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SiteMessage.title</code> attribute. 
	 * @param value the title - Deprecated since 1905, will be replace by subject.
	 */
	public void setTitle(final String value)
	{
		setTitle( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SiteMessage.title</code> attribute. 
	 * @param value the title - Deprecated since 1905, will be replace by subject.
	 */
	public void setAllTitle(final SessionContext ctx, final Map<Language,String> value)
	{
		setAllLocalizedProperties(ctx,TITLE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SiteMessage.title</code> attribute. 
	 * @param value the title - Deprecated since 1905, will be replace by subject.
	 */
	public void setAllTitle(final Map<Language,String> value)
	{
		setAllTitle( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SiteMessage.type</code> attribute.
	 * @return the type
	 */
	public EnumerationValue getType(final SessionContext ctx)
	{
		return (EnumerationValue)getProperty( ctx, TYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SiteMessage.type</code> attribute.
	 * @return the type
	 */
	public EnumerationValue getType()
	{
		return getType( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SiteMessage.type</code> attribute. 
	 * @param value the type
	 */
	public void setType(final SessionContext ctx, final EnumerationValue value)
	{
		setProperty(ctx, TYPE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SiteMessage.type</code> attribute. 
	 * @param value the type
	 */
	public void setType(final EnumerationValue value)
	{
		setType( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SiteMessage.uid</code> attribute.
	 * @return the uid
	 */
	public String getUid(final SessionContext ctx)
	{
		return (String)getProperty( ctx, UID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SiteMessage.uid</code> attribute.
	 * @return the uid
	 */
	public String getUid()
	{
		return getUid( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SiteMessage.uid</code> attribute. 
	 * @param value the uid
	 */
	public void setUid(final SessionContext ctx, final String value)
	{
		setProperty(ctx, UID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SiteMessage.uid</code> attribute. 
	 * @param value the uid
	 */
	public void setUid(final String value)
	{
		setUid( getSession().getSessionContext(), value );
	}
	
}
