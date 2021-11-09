/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 08-Nov-2021, 4:51:25 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.xyformsservices.jalo;

import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.link.Link;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.xyformsservices.constants.XyformsservicesConstants;
import de.hybris.platform.xyformsservices.jalo.YFormData;
import de.hybris.platform.xyformsservices.jalo.YFormDataHistory;
import de.hybris.platform.xyformsservices.jalo.YFormDefinition;
import de.hybris.platform.xyformsservices.jalo.component.YFormCMSComponent;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Generated class for type <code>XyformsservicesManager</code>.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedXyformsservicesManager extends Extension
{
	/** Relation ordering override parameter constants for Category2YFormDefinitionRelation from ((xyformsservices))*/
	protected static String CATEGORY2YFORMDEFINITIONRELATION_SRC_ORDERED = "relation.Category2YFormDefinitionRelation.source.ordered";
	protected static String CATEGORY2YFORMDEFINITIONRELATION_TGT_ORDERED = "relation.Category2YFormDefinitionRelation.target.ordered";
	/** Relation disable markmodifed parameter constants for Category2YFormDefinitionRelation from ((xyformsservices))*/
	protected static String CATEGORY2YFORMDEFINITIONRELATION_MARKMODIFIED = "relation.Category2YFormDefinitionRelation.markmodified";
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
	
	public YFormCMSComponent createYFormCMSComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( XyformsservicesConstants.TC.YFORMCMSCOMPONENT );
			return (YFormCMSComponent)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating YFormCMSComponent : "+e.getMessage(), 0 );
		}
	}
	
	public YFormCMSComponent createYFormCMSComponent(final Map attributeValues)
	{
		return createYFormCMSComponent( getSession().getSessionContext(), attributeValues );
	}
	
	public YFormData createYFormData(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( XyformsservicesConstants.TC.YFORMDATA );
			return (YFormData)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating YFormData : "+e.getMessage(), 0 );
		}
	}
	
	public YFormData createYFormData(final Map attributeValues)
	{
		return createYFormData( getSession().getSessionContext(), attributeValues );
	}
	
	public YFormDataHistory createYFormDataHistory(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( XyformsservicesConstants.TC.YFORMDATAHISTORY );
			return (YFormDataHistory)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating YFormDataHistory : "+e.getMessage(), 0 );
		}
	}
	
	public YFormDataHistory createYFormDataHistory(final Map attributeValues)
	{
		return createYFormDataHistory( getSession().getSessionContext(), attributeValues );
	}
	
	public YFormDefinition createYFormDefinition(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( XyformsservicesConstants.TC.YFORMDEFINITION );
			return (YFormDefinition)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating YFormDefinition : "+e.getMessage(), 0 );
		}
	}
	
	public YFormDefinition createYFormDefinition(final Map attributeValues)
	{
		return createYFormDefinition( getSession().getSessionContext(), attributeValues );
	}
	
	@Override
	public String getName()
	{
		return XyformsservicesConstants.EXTENSIONNAME;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.yFormDefinitions</code> attribute.
	 * @return the yFormDefinitions - Form Definitions
	 */
	public Set<YFormDefinition> getYFormDefinitions(final SessionContext ctx, final Category item)
	{
		final List<YFormDefinition> items = item.getLinkedItems( 
			ctx,
			true,
			XyformsservicesConstants.Relations.CATEGORY2YFORMDEFINITIONRELATION,
			"YFormDefinition",
			null,
			false,
			false
		);
		return new LinkedHashSet<YFormDefinition>(items);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Category.yFormDefinitions</code> attribute.
	 * @return the yFormDefinitions - Form Definitions
	 */
	public Set<YFormDefinition> getYFormDefinitions(final Category item)
	{
		return getYFormDefinitions( getSession().getSessionContext(), item );
	}
	
	public long getYFormDefinitionsCount(final SessionContext ctx, final Category item)
	{
		return item.getLinkedItemsCount(
			ctx,
			true,
			XyformsservicesConstants.Relations.CATEGORY2YFORMDEFINITIONRELATION,
			"YFormDefinition",
			null
		);
	}
	
	public long getYFormDefinitionsCount(final Category item)
	{
		return getYFormDefinitionsCount( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Category.yFormDefinitions</code> attribute. 
	 * @param value the yFormDefinitions - Form Definitions
	 */
	public void setYFormDefinitions(final SessionContext ctx, final Category item, final Set<YFormDefinition> value)
	{
		item.setLinkedItems( 
			ctx,
			true,
			XyformsservicesConstants.Relations.CATEGORY2YFORMDEFINITIONRELATION,
			null,
			value,
			false,
			false,
			Utilities.getMarkModifiedOverride(CATEGORY2YFORMDEFINITIONRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Category.yFormDefinitions</code> attribute. 
	 * @param value the yFormDefinitions - Form Definitions
	 */
	public void setYFormDefinitions(final Category item, final Set<YFormDefinition> value)
	{
		setYFormDefinitions( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to yFormDefinitions. 
	 * @param value the item to add to yFormDefinitions - Form Definitions
	 */
	public void addToYFormDefinitions(final SessionContext ctx, final Category item, final YFormDefinition value)
	{
		item.addLinkedItems( 
			ctx,
			true,
			XyformsservicesConstants.Relations.CATEGORY2YFORMDEFINITIONRELATION,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(CATEGORY2YFORMDEFINITIONRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to yFormDefinitions. 
	 * @param value the item to add to yFormDefinitions - Form Definitions
	 */
	public void addToYFormDefinitions(final Category item, final YFormDefinition value)
	{
		addToYFormDefinitions( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from yFormDefinitions. 
	 * @param value the item to remove from yFormDefinitions - Form Definitions
	 */
	public void removeFromYFormDefinitions(final SessionContext ctx, final Category item, final YFormDefinition value)
	{
		item.removeLinkedItems( 
			ctx,
			true,
			XyformsservicesConstants.Relations.CATEGORY2YFORMDEFINITIONRELATION,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(CATEGORY2YFORMDEFINITIONRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from yFormDefinitions. 
	 * @param value the item to remove from yFormDefinitions - Form Definitions
	 */
	public void removeFromYFormDefinitions(final Category item, final YFormDefinition value)
	{
		removeFromYFormDefinitions( getSession().getSessionContext(), item, value );
	}
	
}
