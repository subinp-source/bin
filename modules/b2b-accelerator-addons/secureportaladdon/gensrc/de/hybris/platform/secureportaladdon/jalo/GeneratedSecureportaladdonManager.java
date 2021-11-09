/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 06-Oct-2021, 11:13:07 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.secureportaladdon.jalo;

import de.hybris.platform.basecommerce.jalo.site.BaseSite;
import de.hybris.platform.cms2.jalo.site.CMSSite;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.secureportaladdon.constants.SecureportaladdonConstants;
import de.hybris.platform.secureportaladdon.jalo.B2BRegistration;
import de.hybris.platform.secureportaladdon.jalo.B2BRegistrationApprovedProcess;
import de.hybris.platform.secureportaladdon.jalo.B2BRegistrationProcess;
import de.hybris.platform.secureportaladdon.jalo.B2BRegistrationRejectedProcess;
import de.hybris.platform.secureportaladdon.jalo.restrictions.CMSSecurePortalRestriction;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type <code>SecureportaladdonManager</code>.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedSecureportaladdonManager extends Extension
{
	protected static final Map<String, Map<String, AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, Map<String, AttributeMode>> ttmp = new HashMap();
		Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put("requiresAuthentication", AttributeMode.INITIAL);
		tmp.put("enableRegistration", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.cms2.jalo.site.CMSSite", Collections.unmodifiableMap(tmp));
		DEFAULT_INITIAL_ATTRIBUTES = ttmp;
	}
	@Override
	public Map<String, AttributeMode> getDefaultAttributeModes(final Class<? extends Item> itemClass)
	{
		Map<String, AttributeMode> ret = new HashMap<>();
		final Map<String, AttributeMode> attr = DEFAULT_INITIAL_ATTRIBUTES.get(itemClass.getName());
		if (attr != null)
		{
			ret.putAll(attr);
		}
		return ret;
	}
	
	public B2BRegistration createB2BRegistration(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( SecureportaladdonConstants.TC.B2BREGISTRATION );
			return (B2BRegistration)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating B2BRegistration : "+e.getMessage(), 0 );
		}
	}
	
	public B2BRegistration createB2BRegistration(final Map attributeValues)
	{
		return createB2BRegistration( getSession().getSessionContext(), attributeValues );
	}
	
	public B2BRegistrationApprovedProcess createB2BRegistrationApprovedProcess(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( SecureportaladdonConstants.TC.B2BREGISTRATIONAPPROVEDPROCESS );
			return (B2BRegistrationApprovedProcess)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating B2BRegistrationApprovedProcess : "+e.getMessage(), 0 );
		}
	}
	
	public B2BRegistrationApprovedProcess createB2BRegistrationApprovedProcess(final Map attributeValues)
	{
		return createB2BRegistrationApprovedProcess( getSession().getSessionContext(), attributeValues );
	}
	
	public B2BRegistrationProcess createB2BRegistrationProcess(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( SecureportaladdonConstants.TC.B2BREGISTRATIONPROCESS );
			return (B2BRegistrationProcess)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating B2BRegistrationProcess : "+e.getMessage(), 0 );
		}
	}
	
	public B2BRegistrationProcess createB2BRegistrationProcess(final Map attributeValues)
	{
		return createB2BRegistrationProcess( getSession().getSessionContext(), attributeValues );
	}
	
	public B2BRegistrationRejectedProcess createB2BRegistrationRejectedProcess(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( SecureportaladdonConstants.TC.B2BREGISTRATIONREJECTEDPROCESS );
			return (B2BRegistrationRejectedProcess)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating B2BRegistrationRejectedProcess : "+e.getMessage(), 0 );
		}
	}
	
	public B2BRegistrationRejectedProcess createB2BRegistrationRejectedProcess(final Map attributeValues)
	{
		return createB2BRegistrationRejectedProcess( getSession().getSessionContext(), attributeValues );
	}
	
	public CMSSecurePortalRestriction createCMSSecurePortalRestriction(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( SecureportaladdonConstants.TC.CMSSECUREPORTALRESTRICTION );
			return (CMSSecurePortalRestriction)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating CMSSecurePortalRestriction : "+e.getMessage(), 0 );
		}
	}
	
	public CMSSecurePortalRestriction createCMSSecurePortalRestriction(final Map attributeValues)
	{
		return createCMSSecurePortalRestriction( getSession().getSessionContext(), attributeValues );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSSite.enableRegistration</code> attribute.
	 * @return the enableRegistration - Indicates if the website supports registration request.
	 */
	public Boolean isEnableRegistration(final SessionContext ctx, final CMSSite item)
	{
		return (Boolean)item.getProperty( ctx, SecureportaladdonConstants.Attributes.CMSSite.ENABLEREGISTRATION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSSite.enableRegistration</code> attribute.
	 * @return the enableRegistration - Indicates if the website supports registration request.
	 */
	public Boolean isEnableRegistration(final CMSSite item)
	{
		return isEnableRegistration( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSSite.enableRegistration</code> attribute. 
	 * @return the enableRegistration - Indicates if the website supports registration request.
	 */
	public boolean isEnableRegistrationAsPrimitive(final SessionContext ctx, final CMSSite item)
	{
		Boolean value = isEnableRegistration( ctx,item );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSSite.enableRegistration</code> attribute. 
	 * @return the enableRegistration - Indicates if the website supports registration request.
	 */
	public boolean isEnableRegistrationAsPrimitive(final CMSSite item)
	{
		return isEnableRegistrationAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSSite.enableRegistration</code> attribute. 
	 * @param value the enableRegistration - Indicates if the website supports registration request.
	 */
	public void setEnableRegistration(final SessionContext ctx, final CMSSite item, final Boolean value)
	{
		item.setProperty(ctx, SecureportaladdonConstants.Attributes.CMSSite.ENABLEREGISTRATION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSSite.enableRegistration</code> attribute. 
	 * @param value the enableRegistration - Indicates if the website supports registration request.
	 */
	public void setEnableRegistration(final CMSSite item, final Boolean value)
	{
		setEnableRegistration( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSSite.enableRegistration</code> attribute. 
	 * @param value the enableRegistration - Indicates if the website supports registration request.
	 */
	public void setEnableRegistration(final SessionContext ctx, final CMSSite item, final boolean value)
	{
		setEnableRegistration( ctx, item, Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSSite.enableRegistration</code> attribute. 
	 * @param value the enableRegistration - Indicates if the website supports registration request.
	 */
	public void setEnableRegistration(final CMSSite item, final boolean value)
	{
		setEnableRegistration( getSession().getSessionContext(), item, value );
	}
	
	@Override
	public String getName()
	{
		return SecureportaladdonConstants.EXTENSIONNAME;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSSite.requiresAuthentication</code> attribute.
	 * @return the requiresAuthentication - Indicates if the website requires authentication or not.
	 */
	public Boolean isRequiresAuthentication(final SessionContext ctx, final CMSSite item)
	{
		return (Boolean)item.getProperty( ctx, SecureportaladdonConstants.Attributes.CMSSite.REQUIRESAUTHENTICATION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSSite.requiresAuthentication</code> attribute.
	 * @return the requiresAuthentication - Indicates if the website requires authentication or not.
	 */
	public Boolean isRequiresAuthentication(final CMSSite item)
	{
		return isRequiresAuthentication( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSSite.requiresAuthentication</code> attribute. 
	 * @return the requiresAuthentication - Indicates if the website requires authentication or not.
	 */
	public boolean isRequiresAuthenticationAsPrimitive(final SessionContext ctx, final CMSSite item)
	{
		Boolean value = isRequiresAuthentication( ctx,item );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSSite.requiresAuthentication</code> attribute. 
	 * @return the requiresAuthentication - Indicates if the website requires authentication or not.
	 */
	public boolean isRequiresAuthenticationAsPrimitive(final CMSSite item)
	{
		return isRequiresAuthenticationAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSSite.requiresAuthentication</code> attribute. 
	 * @param value the requiresAuthentication - Indicates if the website requires authentication or not.
	 */
	public void setRequiresAuthentication(final SessionContext ctx, final CMSSite item, final Boolean value)
	{
		item.setProperty(ctx, SecureportaladdonConstants.Attributes.CMSSite.REQUIRESAUTHENTICATION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSSite.requiresAuthentication</code> attribute. 
	 * @param value the requiresAuthentication - Indicates if the website requires authentication or not.
	 */
	public void setRequiresAuthentication(final CMSSite item, final Boolean value)
	{
		setRequiresAuthentication( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSSite.requiresAuthentication</code> attribute. 
	 * @param value the requiresAuthentication - Indicates if the website requires authentication or not.
	 */
	public void setRequiresAuthentication(final SessionContext ctx, final CMSSite item, final boolean value)
	{
		setRequiresAuthentication( ctx, item, Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSSite.requiresAuthentication</code> attribute. 
	 * @param value the requiresAuthentication - Indicates if the website requires authentication or not.
	 */
	public void setRequiresAuthentication(final CMSSite item, final boolean value)
	{
		setRequiresAuthentication( getSession().getSessionContext(), item, value );
	}
	
}
