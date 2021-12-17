/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 15-Dec-2021, 3:07:44 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.notificationservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.notificationservices.enums.NotificationType;
import de.hybris.platform.notificationservices.enums.SiteMessageType;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;

/**
 * Generated model class for type SiteMessage first defined at extension notificationservices.
 */
@SuppressWarnings("all")
public class SiteMessageModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "SiteMessage";
	
	/** <i>Generated constant</i> - Attribute key of <code>SiteMessage.uid</code> attribute defined at extension <code>notificationservices</code>. */
	public static final String UID = "uid";
	
	/** <i>Generated constant</i> - Attribute key of <code>SiteMessage.title</code> attribute defined at extension <code>notificationservices</code>. */
	public static final String TITLE = "title";
	
	/** <i>Generated constant</i> - Attribute key of <code>SiteMessage.subject</code> attribute defined at extension <code>notificationservices</code>. */
	public static final String SUBJECT = "subject";
	
	/** <i>Generated constant</i> - Attribute key of <code>SiteMessage.content</code> attribute defined at extension <code>notificationservices</code>. */
	public static final String CONTENT = "content";
	
	/** <i>Generated constant</i> - Attribute key of <code>SiteMessage.body</code> attribute defined at extension <code>notificationservices</code>. */
	public static final String BODY = "body";
	
	/** <i>Generated constant</i> - Attribute key of <code>SiteMessage.externalItem</code> attribute defined at extension <code>notificationservices</code>. */
	public static final String EXTERNALITEM = "externalItem";
	
	/** <i>Generated constant</i> - Attribute key of <code>SiteMessage.type</code> attribute defined at extension <code>notificationservices</code>. */
	public static final String TYPE = "type";
	
	/** <i>Generated constant</i> - Attribute key of <code>SiteMessage.notificationType</code> attribute defined at extension <code>notificationservices</code>. */
	public static final String NOTIFICATIONTYPE = "notificationType";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public SiteMessageModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public SiteMessageModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _type initial attribute declared by type <code>SiteMessage</code> at extension <code>notificationservices</code>
	 * @param _uid initial attribute declared by type <code>SiteMessage</code> at extension <code>notificationservices</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public SiteMessageModel(final SiteMessageType _type, final String _uid)
	{
		super();
		setType(_type);
		setUid(_uid);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _type initial attribute declared by type <code>SiteMessage</code> at extension <code>notificationservices</code>
	 * @param _uid initial attribute declared by type <code>SiteMessage</code> at extension <code>notificationservices</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public SiteMessageModel(final ItemModel _owner, final SiteMessageType _type, final String _uid)
	{
		super();
		setOwner(_owner);
		setType(_type);
		setUid(_uid);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SiteMessage.body</code> attribute defined at extension <code>notificationservices</code>. 
	 * @return the body
	 */
	@Accessor(qualifier = "body", type = Accessor.Type.GETTER)
	public String getBody()
	{
		return getPersistenceContext().getPropertyValue(BODY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SiteMessage.content</code> attribute defined at extension <code>notificationservices</code>. 
	 * @return the content - Deprecated since 1905, will be replace by body.
	 * @deprecated since 1905
	 */
	@Deprecated(since = "1905", forRemoval = true)
	@Accessor(qualifier = "content", type = Accessor.Type.GETTER)
	public String getContent()
	{
		return getContent(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>SiteMessage.content</code> attribute defined at extension <code>notificationservices</code>. 
	 * @param loc the value localization key 
	 * @return the content - Deprecated since 1905, will be replace by body.
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Deprecated(since = "1905", forRemoval = true)
	@Accessor(qualifier = "content", type = Accessor.Type.GETTER)
	public String getContent(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(CONTENT, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SiteMessage.externalItem</code> attribute defined at extension <code>notificationservices</code>. 
	 * @return the externalItem
	 */
	@Accessor(qualifier = "externalItem", type = Accessor.Type.GETTER)
	public ItemModel getExternalItem()
	{
		return getPersistenceContext().getPropertyValue(EXTERNALITEM);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SiteMessage.notificationType</code> attribute defined at extension <code>notificationservices</code>. 
	 * @return the notificationType
	 */
	@Accessor(qualifier = "notificationType", type = Accessor.Type.GETTER)
	public NotificationType getNotificationType()
	{
		return getPersistenceContext().getPropertyValue(NOTIFICATIONTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SiteMessage.subject</code> attribute defined at extension <code>notificationservices</code>. 
	 * @return the subject
	 */
	@Accessor(qualifier = "subject", type = Accessor.Type.GETTER)
	public String getSubject()
	{
		return getPersistenceContext().getPropertyValue(SUBJECT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SiteMessage.title</code> attribute defined at extension <code>notificationservices</code>. 
	 * @return the title - Deprecated since 1905, will be replace by subject.
	 * @deprecated since 1905
	 */
	@Deprecated(since = "1905", forRemoval = true)
	@Accessor(qualifier = "title", type = Accessor.Type.GETTER)
	public String getTitle()
	{
		return getTitle(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>SiteMessage.title</code> attribute defined at extension <code>notificationservices</code>. 
	 * @param loc the value localization key 
	 * @return the title - Deprecated since 1905, will be replace by subject.
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Deprecated(since = "1905", forRemoval = true)
	@Accessor(qualifier = "title", type = Accessor.Type.GETTER)
	public String getTitle(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(TITLE, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SiteMessage.type</code> attribute defined at extension <code>notificationservices</code>. 
	 * @return the type
	 */
	@Accessor(qualifier = "type", type = Accessor.Type.GETTER)
	public SiteMessageType getType()
	{
		return getPersistenceContext().getPropertyValue(TYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SiteMessage.uid</code> attribute defined at extension <code>notificationservices</code>. 
	 * @return the uid
	 */
	@Accessor(qualifier = "uid", type = Accessor.Type.GETTER)
	public String getUid()
	{
		return getPersistenceContext().getPropertyValue(UID);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SiteMessage.body</code> attribute defined at extension <code>notificationservices</code>. 
	 *  
	 * @param value the body
	 */
	@Accessor(qualifier = "body", type = Accessor.Type.SETTER)
	public void setBody(final String value)
	{
		getPersistenceContext().setPropertyValue(BODY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SiteMessage.content</code> attribute defined at extension <code>notificationservices</code>. 
	 *  
	 * @param value the content - Deprecated since 1905, will be replace by body.
	 * @deprecated since 1905
	 */
	@Deprecated(since = "1905", forRemoval = true)
	@Accessor(qualifier = "content", type = Accessor.Type.SETTER)
	public void setContent(final String value)
	{
		setContent(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>SiteMessage.content</code> attribute defined at extension <code>notificationservices</code>. 
	 *  
	 * @param value the content - Deprecated since 1905, will be replace by body.
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Deprecated(since = "1905", forRemoval = true)
	@Accessor(qualifier = "content", type = Accessor.Type.SETTER)
	public void setContent(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(CONTENT, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SiteMessage.externalItem</code> attribute defined at extension <code>notificationservices</code>. 
	 *  
	 * @param value the externalItem
	 */
	@Accessor(qualifier = "externalItem", type = Accessor.Type.SETTER)
	public void setExternalItem(final ItemModel value)
	{
		getPersistenceContext().setPropertyValue(EXTERNALITEM, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SiteMessage.notificationType</code> attribute defined at extension <code>notificationservices</code>. 
	 *  
	 * @param value the notificationType
	 */
	@Accessor(qualifier = "notificationType", type = Accessor.Type.SETTER)
	public void setNotificationType(final NotificationType value)
	{
		getPersistenceContext().setPropertyValue(NOTIFICATIONTYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SiteMessage.subject</code> attribute defined at extension <code>notificationservices</code>. 
	 *  
	 * @param value the subject
	 */
	@Accessor(qualifier = "subject", type = Accessor.Type.SETTER)
	public void setSubject(final String value)
	{
		getPersistenceContext().setPropertyValue(SUBJECT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SiteMessage.title</code> attribute defined at extension <code>notificationservices</code>. 
	 *  
	 * @param value the title - Deprecated since 1905, will be replace by subject.
	 * @deprecated since 1905
	 */
	@Deprecated(since = "1905", forRemoval = true)
	@Accessor(qualifier = "title", type = Accessor.Type.SETTER)
	public void setTitle(final String value)
	{
		setTitle(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>SiteMessage.title</code> attribute defined at extension <code>notificationservices</code>. 
	 *  
	 * @param value the title - Deprecated since 1905, will be replace by subject.
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Deprecated(since = "1905", forRemoval = true)
	@Accessor(qualifier = "title", type = Accessor.Type.SETTER)
	public void setTitle(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(TITLE, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SiteMessage.type</code> attribute defined at extension <code>notificationservices</code>. 
	 *  
	 * @param value the type
	 */
	@Accessor(qualifier = "type", type = Accessor.Type.SETTER)
	public void setType(final SiteMessageType value)
	{
		getPersistenceContext().setPropertyValue(TYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SiteMessage.uid</code> attribute defined at extension <code>notificationservices</code>. 
	 *  
	 * @param value the uid
	 */
	@Accessor(qualifier = "uid", type = Accessor.Type.SETTER)
	public void setUid(final String value)
	{
		getPersistenceContext().setPropertyValue(UID, value);
	}
	
}
