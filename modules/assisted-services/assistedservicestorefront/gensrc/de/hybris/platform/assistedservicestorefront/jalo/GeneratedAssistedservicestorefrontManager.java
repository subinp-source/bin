/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 11-Dec-2021, 12:32:58 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.assistedservicestorefront.jalo;

import de.hybris.platform.assistedservicestorefront.constants.AssistedservicestorefrontConstants;
import de.hybris.platform.assistedservicestorefront.jalo.AsmDevicesUsedComponent;
import de.hybris.platform.assistedservicestorefront.jalo.AsmFavoriteColorsComponent;
import de.hybris.platform.assistedservicestorefront.jalo.AssistedServiceComponent;
import de.hybris.platform.cms2.jalo.restrictions.ASMCMSUserGroupRestriction;
import de.hybris.platform.cms2.jalo.restrictions.AssistedServiceSessionRestriction;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type <code>AssistedservicestorefrontManager</code>.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedAssistedservicestorefrontManager extends Extension
{
	protected static final Map<String, Map<String, AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, Map<String, AttributeMode>> ttmp = new HashMap();
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
	
	public ASMCMSUserGroupRestriction createASMCMSUserGroupRestriction(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( AssistedservicestorefrontConstants.TC.ASMCMSUSERGROUPRESTRICTION );
			return (ASMCMSUserGroupRestriction)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating ASMCMSUserGroupRestriction : "+e.getMessage(), 0 );
		}
	}
	
	public ASMCMSUserGroupRestriction createASMCMSUserGroupRestriction(final Map attributeValues)
	{
		return createASMCMSUserGroupRestriction( getSession().getSessionContext(), attributeValues );
	}
	
	public AsmDevicesUsedComponent createAsmDevicesUsedComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( AssistedservicestorefrontConstants.TC.ASMDEVICESUSEDCOMPONENT );
			return (AsmDevicesUsedComponent)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating AsmDevicesUsedComponent : "+e.getMessage(), 0 );
		}
	}
	
	public AsmDevicesUsedComponent createAsmDevicesUsedComponent(final Map attributeValues)
	{
		return createAsmDevicesUsedComponent( getSession().getSessionContext(), attributeValues );
	}
	
	public AsmFavoriteColorsComponent createAsmFavoriteColorsComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( AssistedservicestorefrontConstants.TC.ASMFAVORITECOLORSCOMPONENT );
			return (AsmFavoriteColorsComponent)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating AsmFavoriteColorsComponent : "+e.getMessage(), 0 );
		}
	}
	
	public AsmFavoriteColorsComponent createAsmFavoriteColorsComponent(final Map attributeValues)
	{
		return createAsmFavoriteColorsComponent( getSession().getSessionContext(), attributeValues );
	}
	
	public AssistedServiceComponent createAssistedServiceComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( AssistedservicestorefrontConstants.TC.ASSISTEDSERVICECOMPONENT );
			return (AssistedServiceComponent)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating AssistedServiceComponent : "+e.getMessage(), 0 );
		}
	}
	
	public AssistedServiceComponent createAssistedServiceComponent(final Map attributeValues)
	{
		return createAssistedServiceComponent( getSession().getSessionContext(), attributeValues );
	}
	
	public AssistedServiceSessionRestriction createAssistedServiceSessionRestriction(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( AssistedservicestorefrontConstants.TC.ASSISTEDSERVICESESSIONRESTRICTION );
			return (AssistedServiceSessionRestriction)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating AssistedServiceSessionRestriction : "+e.getMessage(), 0 );
		}
	}
	
	public AssistedServiceSessionRestriction createAssistedServiceSessionRestriction(final Map attributeValues)
	{
		return createAssistedServiceSessionRestriction( getSession().getSessionContext(), attributeValues );
	}
	
	@Override
	public String getName()
	{
		return AssistedservicestorefrontConstants.EXTENSIONNAME;
	}
	
}
