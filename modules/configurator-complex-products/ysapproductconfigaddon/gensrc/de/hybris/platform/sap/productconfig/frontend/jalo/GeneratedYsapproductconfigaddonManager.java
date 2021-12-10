/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 11-Dec-2021, 12:32:58 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.productconfig.frontend.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.sap.productconfig.frontend.constants.SapproductconfigaddonConstants;
import de.hybris.platform.sap.productconfig.frontend.jalo.CartConfigurationDisplayComponent;
import de.hybris.platform.sap.productconfig.frontend.jalo.ProductAddConfigToCartComponent;
import de.hybris.platform.sap.productconfig.frontend.jalo.ProductConfigOverviewPage;
import de.hybris.platform.sap.productconfig.frontend.jalo.ProductConfigPage;
import de.hybris.platform.sap.productconfig.frontend.jalo.ProductConfigurationFilterComponent;
import de.hybris.platform.sap.productconfig.frontend.jalo.ProductConfigurationFormComponent;
import de.hybris.platform.sap.productconfig.frontend.jalo.ProductConfigurationImageComponent;
import de.hybris.platform.sap.productconfig.frontend.jalo.ProductConfigurationMenuComponent;
import de.hybris.platform.sap.productconfig.frontend.jalo.ProductConfigurationOverviewComponent;
import de.hybris.platform.sap.productconfig.frontend.jalo.ProductConfigurationOverviewTitleComponent;
import de.hybris.platform.sap.productconfig.frontend.jalo.ProductConfigurationPrevNextComponent;
import de.hybris.platform.sap.productconfig.frontend.jalo.ProductConfigurationPriceSummaryComponent;
import de.hybris.platform.sap.productconfig.frontend.jalo.ProductConfigurationTitleSummaryComponent;
import de.hybris.platform.sap.productconfig.frontend.jalo.ProductConfigurationVariantListComponent;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type <code>YsapproductconfigaddonManager</code>.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedYsapproductconfigaddonManager extends Extension
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
	
	public CartConfigurationDisplayComponent createCartConfigurationDisplayComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( SapproductconfigaddonConstants.TC.CARTCONFIGURATIONDISPLAYCOMPONENT );
			return (CartConfigurationDisplayComponent)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating CartConfigurationDisplayComponent : "+e.getMessage(), 0 );
		}
	}
	
	public CartConfigurationDisplayComponent createCartConfigurationDisplayComponent(final Map attributeValues)
	{
		return createCartConfigurationDisplayComponent( getSession().getSessionContext(), attributeValues );
	}
	
	public ProductAddConfigToCartComponent createProductAddConfigToCartComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( SapproductconfigaddonConstants.TC.PRODUCTADDCONFIGTOCARTCOMPONENT );
			return (ProductAddConfigToCartComponent)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating ProductAddConfigToCartComponent : "+e.getMessage(), 0 );
		}
	}
	
	public ProductAddConfigToCartComponent createProductAddConfigToCartComponent(final Map attributeValues)
	{
		return createProductAddConfigToCartComponent( getSession().getSessionContext(), attributeValues );
	}
	
	public ProductConfigOverviewPage createProductConfigOverviewPage(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( SapproductconfigaddonConstants.TC.PRODUCTCONFIGOVERVIEWPAGE );
			return (ProductConfigOverviewPage)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating ProductConfigOverviewPage : "+e.getMessage(), 0 );
		}
	}
	
	public ProductConfigOverviewPage createProductConfigOverviewPage(final Map attributeValues)
	{
		return createProductConfigOverviewPage( getSession().getSessionContext(), attributeValues );
	}
	
	public ProductConfigPage createProductConfigPage(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( SapproductconfigaddonConstants.TC.PRODUCTCONFIGPAGE );
			return (ProductConfigPage)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating ProductConfigPage : "+e.getMessage(), 0 );
		}
	}
	
	public ProductConfigPage createProductConfigPage(final Map attributeValues)
	{
		return createProductConfigPage( getSession().getSessionContext(), attributeValues );
	}
	
	public ProductConfigurationFilterComponent createProductConfigurationFilterComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( SapproductconfigaddonConstants.TC.PRODUCTCONFIGURATIONFILTERCOMPONENT );
			return (ProductConfigurationFilterComponent)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating ProductConfigurationFilterComponent : "+e.getMessage(), 0 );
		}
	}
	
	public ProductConfigurationFilterComponent createProductConfigurationFilterComponent(final Map attributeValues)
	{
		return createProductConfigurationFilterComponent( getSession().getSessionContext(), attributeValues );
	}
	
	public ProductConfigurationFormComponent createProductConfigurationFormComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( SapproductconfigaddonConstants.TC.PRODUCTCONFIGURATIONFORMCOMPONENT );
			return (ProductConfigurationFormComponent)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating ProductConfigurationFormComponent : "+e.getMessage(), 0 );
		}
	}
	
	public ProductConfigurationFormComponent createProductConfigurationFormComponent(final Map attributeValues)
	{
		return createProductConfigurationFormComponent( getSession().getSessionContext(), attributeValues );
	}
	
	public ProductConfigurationImageComponent createProductConfigurationImageComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( SapproductconfigaddonConstants.TC.PRODUCTCONFIGURATIONIMAGECOMPONENT );
			return (ProductConfigurationImageComponent)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating ProductConfigurationImageComponent : "+e.getMessage(), 0 );
		}
	}
	
	public ProductConfigurationImageComponent createProductConfigurationImageComponent(final Map attributeValues)
	{
		return createProductConfigurationImageComponent( getSession().getSessionContext(), attributeValues );
	}
	
	public ProductConfigurationMenuComponent createProductConfigurationMenuComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( SapproductconfigaddonConstants.TC.PRODUCTCONFIGURATIONMENUCOMPONENT );
			return (ProductConfigurationMenuComponent)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating ProductConfigurationMenuComponent : "+e.getMessage(), 0 );
		}
	}
	
	public ProductConfigurationMenuComponent createProductConfigurationMenuComponent(final Map attributeValues)
	{
		return createProductConfigurationMenuComponent( getSession().getSessionContext(), attributeValues );
	}
	
	public ProductConfigurationOverviewComponent createProductConfigurationOverviewComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( SapproductconfigaddonConstants.TC.PRODUCTCONFIGURATIONOVERVIEWCOMPONENT );
			return (ProductConfigurationOverviewComponent)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating ProductConfigurationOverviewComponent : "+e.getMessage(), 0 );
		}
	}
	
	public ProductConfigurationOverviewComponent createProductConfigurationOverviewComponent(final Map attributeValues)
	{
		return createProductConfigurationOverviewComponent( getSession().getSessionContext(), attributeValues );
	}
	
	public ProductConfigurationOverviewTitleComponent createProductConfigurationOverviewTitleComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( SapproductconfigaddonConstants.TC.PRODUCTCONFIGURATIONOVERVIEWTITLECOMPONENT );
			return (ProductConfigurationOverviewTitleComponent)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating ProductConfigurationOverviewTitleComponent : "+e.getMessage(), 0 );
		}
	}
	
	public ProductConfigurationOverviewTitleComponent createProductConfigurationOverviewTitleComponent(final Map attributeValues)
	{
		return createProductConfigurationOverviewTitleComponent( getSession().getSessionContext(), attributeValues );
	}
	
	public ProductConfigurationPrevNextComponent createProductConfigurationPrevNextComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( SapproductconfigaddonConstants.TC.PRODUCTCONFIGURATIONPREVNEXTCOMPONENT );
			return (ProductConfigurationPrevNextComponent)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating ProductConfigurationPrevNextComponent : "+e.getMessage(), 0 );
		}
	}
	
	public ProductConfigurationPrevNextComponent createProductConfigurationPrevNextComponent(final Map attributeValues)
	{
		return createProductConfigurationPrevNextComponent( getSession().getSessionContext(), attributeValues );
	}
	
	public ProductConfigurationPriceSummaryComponent createProductConfigurationPriceSummaryComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( SapproductconfigaddonConstants.TC.PRODUCTCONFIGURATIONPRICESUMMARYCOMPONENT );
			return (ProductConfigurationPriceSummaryComponent)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating ProductConfigurationPriceSummaryComponent : "+e.getMessage(), 0 );
		}
	}
	
	public ProductConfigurationPriceSummaryComponent createProductConfigurationPriceSummaryComponent(final Map attributeValues)
	{
		return createProductConfigurationPriceSummaryComponent( getSession().getSessionContext(), attributeValues );
	}
	
	public ProductConfigurationTitleSummaryComponent createProductConfigurationTitleSummaryComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( SapproductconfigaddonConstants.TC.PRODUCTCONFIGURATIONTITLESUMMARYCOMPONENT );
			return (ProductConfigurationTitleSummaryComponent)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating ProductConfigurationTitleSummaryComponent : "+e.getMessage(), 0 );
		}
	}
	
	public ProductConfigurationTitleSummaryComponent createProductConfigurationTitleSummaryComponent(final Map attributeValues)
	{
		return createProductConfigurationTitleSummaryComponent( getSession().getSessionContext(), attributeValues );
	}
	
	public ProductConfigurationVariantListComponent createProductConfigurationVariantListComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( SapproductconfigaddonConstants.TC.PRODUCTCONFIGURATIONVARIANTLISTCOMPONENT );
			return (ProductConfigurationVariantListComponent)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating ProductConfigurationVariantListComponent : "+e.getMessage(), 0 );
		}
	}
	
	public ProductConfigurationVariantListComponent createProductConfigurationVariantListComponent(final Map attributeValues)
	{
		return createProductConfigurationVariantListComponent( getSession().getSessionContext(), attributeValues );
	}
	
	@Override
	public String getName()
	{
		return SapproductconfigaddonConstants.EXTENSIONNAME;
	}
	
}
