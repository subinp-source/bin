/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 13-Dec-2021, 3:58:17 PM                     ---
 * ----------------------------------------------------------------
 */
package com.hybris.merchandising.jalo;

import com.hybris.merchandising.constants.MerchandisingservicesConstants;
import com.hybris.merchandising.jalo.MerchImageProperty;
import com.hybris.merchandising.jalo.MerchIndexingConfig;
import com.hybris.merchandising.jalo.MerchProductDirectoryConfig;
import com.hybris.merchandising.jalo.MerchProperty;
import com.hybris.merchandising.jalo.MerchSynchronizationConfig;
import de.hybris.platform.basecommerce.jalo.site.BaseSite;
import de.hybris.platform.catalog.jalo.CatalogVersion;
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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generated class for type <code>MerchandisingservicesManager</code>.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedMerchandisingservicesManager extends Extension
{
	/** Relation ordering override parameter constants for MerchProductDir2CatalogVersion from ((merchandisingservices))*/
	protected static String MERCHPRODUCTDIR2CATALOGVERSION_SRC_ORDERED = "relation.MerchProductDir2CatalogVersion.source.ordered";
	protected static String MERCHPRODUCTDIR2CATALOGVERSION_TGT_ORDERED = "relation.MerchProductDir2CatalogVersion.target.ordered";
	/** Relation disable markmodifed parameter constants for MerchProductDir2CatalogVersion from ((merchandisingservices))*/
	protected static String MERCHPRODUCTDIR2CATALOGVERSION_MARKMODIFIED = "relation.MerchProductDir2CatalogVersion.markmodified";
	/** Relation ordering override parameter constants for MerchIndexingConfig2CatalogVersion from ((merchandisingservices))*/
	protected static String MERCHINDEXINGCONFIG2CATALOGVERSION_SRC_ORDERED = "relation.MerchIndexingConfig2CatalogVersion.source.ordered";
	protected static String MERCHINDEXINGCONFIG2CATALOGVERSION_TGT_ORDERED = "relation.MerchIndexingConfig2CatalogVersion.target.ordered";
	/** Relation disable markmodifed parameter constants for MerchIndexingConfig2CatalogVersion from ((merchandisingservices))*/
	protected static String MERCHINDEXINGCONFIG2CATALOGVERSION_MARKMODIFIED = "relation.MerchIndexingConfig2CatalogVersion.markmodified";
	protected static final Map<String, Map<String, AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, Map<String, AttributeMode>> ttmp = new HashMap();
		Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put("merchProductDirectoryConfigPOS", AttributeMode.INITIAL);
		tmp.put("merchProductDirectoryConfig", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.basecommerce.jalo.site.BaseSite", Collections.unmodifiableMap(tmp));
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
	
	public MerchImageProperty createMerchImageProperty(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( MerchandisingservicesConstants.TC.MERCHIMAGEPROPERTY );
			return (MerchImageProperty)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating MerchImageProperty : "+e.getMessage(), 0 );
		}
	}
	
	public MerchImageProperty createMerchImageProperty(final Map attributeValues)
	{
		return createMerchImageProperty( getSession().getSessionContext(), attributeValues );
	}
	
	public MerchIndexingConfig createMerchIndexingConfig(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( MerchandisingservicesConstants.TC.MERCHINDEXINGCONFIG );
			return (MerchIndexingConfig)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating MerchIndexingConfig : "+e.getMessage(), 0 );
		}
	}
	
	public MerchIndexingConfig createMerchIndexingConfig(final Map attributeValues)
	{
		return createMerchIndexingConfig( getSession().getSessionContext(), attributeValues );
	}
	
	public MerchProductDirectoryConfig createMerchProductDirectoryConfig(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( MerchandisingservicesConstants.TC.MERCHPRODUCTDIRECTORYCONFIG );
			return (MerchProductDirectoryConfig)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating MerchProductDirectoryConfig : "+e.getMessage(), 0 );
		}
	}
	
	public MerchProductDirectoryConfig createMerchProductDirectoryConfig(final Map attributeValues)
	{
		return createMerchProductDirectoryConfig( getSession().getSessionContext(), attributeValues );
	}
	
	public MerchProperty createMerchProperty(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( MerchandisingservicesConstants.TC.MERCHPROPERTY );
			return (MerchProperty)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating MerchProperty : "+e.getMessage(), 0 );
		}
	}
	
	public MerchProperty createMerchProperty(final Map attributeValues)
	{
		return createMerchProperty( getSession().getSessionContext(), attributeValues );
	}
	
	public MerchSynchronizationConfig createMerchSynchronizationConfig(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( MerchandisingservicesConstants.TC.MERCHSYNCHRONIZATIONCONFIG );
			return (MerchSynchronizationConfig)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating MerchSynchronizationConfig : "+e.getMessage(), 0 );
		}
	}
	
	public MerchSynchronizationConfig createMerchSynchronizationConfig(final Map attributeValues)
	{
		return createMerchSynchronizationConfig( getSession().getSessionContext(), attributeValues );
	}
	
	@Override
	public String getName()
	{
		return MerchandisingservicesConstants.EXTENSIONNAME;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersion.merchIndexingConfig</code> attribute.
	 * @return the merchIndexingConfig
	 */
	public Collection<MerchIndexingConfig> getMerchIndexingConfig(final SessionContext ctx, final CatalogVersion item)
	{
		final List<MerchIndexingConfig> items = item.getLinkedItems( 
			ctx,
			false,
			MerchandisingservicesConstants.Relations.MERCHINDEXINGCONFIG2CATALOGVERSION,
			"MerchIndexingConfig",
			null,
			Utilities.getRelationOrderingOverride(MERCHINDEXINGCONFIG2CATALOGVERSION_SRC_ORDERED, true),
			false
		);
		return items;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersion.merchIndexingConfig</code> attribute.
	 * @return the merchIndexingConfig
	 */
	public Collection<MerchIndexingConfig> getMerchIndexingConfig(final CatalogVersion item)
	{
		return getMerchIndexingConfig( getSession().getSessionContext(), item );
	}
	
	public long getMerchIndexingConfigCount(final SessionContext ctx, final CatalogVersion item)
	{
		return item.getLinkedItemsCount(
			ctx,
			false,
			MerchandisingservicesConstants.Relations.MERCHINDEXINGCONFIG2CATALOGVERSION,
			"MerchIndexingConfig",
			null
		);
	}
	
	public long getMerchIndexingConfigCount(final CatalogVersion item)
	{
		return getMerchIndexingConfigCount( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CatalogVersion.merchIndexingConfig</code> attribute. 
	 * @param value the merchIndexingConfig
	 */
	public void setMerchIndexingConfig(final SessionContext ctx, final CatalogVersion item, final Collection<MerchIndexingConfig> value)
	{
		item.setLinkedItems( 
			ctx,
			false,
			MerchandisingservicesConstants.Relations.MERCHINDEXINGCONFIG2CATALOGVERSION,
			null,
			value,
			Utilities.getRelationOrderingOverride(MERCHINDEXINGCONFIG2CATALOGVERSION_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(MERCHINDEXINGCONFIG2CATALOGVERSION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CatalogVersion.merchIndexingConfig</code> attribute. 
	 * @param value the merchIndexingConfig
	 */
	public void setMerchIndexingConfig(final CatalogVersion item, final Collection<MerchIndexingConfig> value)
	{
		setMerchIndexingConfig( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to merchIndexingConfig. 
	 * @param value the item to add to merchIndexingConfig
	 */
	public void addToMerchIndexingConfig(final SessionContext ctx, final CatalogVersion item, final MerchIndexingConfig value)
	{
		item.addLinkedItems( 
			ctx,
			false,
			MerchandisingservicesConstants.Relations.MERCHINDEXINGCONFIG2CATALOGVERSION,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(MERCHINDEXINGCONFIG2CATALOGVERSION_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(MERCHINDEXINGCONFIG2CATALOGVERSION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to merchIndexingConfig. 
	 * @param value the item to add to merchIndexingConfig
	 */
	public void addToMerchIndexingConfig(final CatalogVersion item, final MerchIndexingConfig value)
	{
		addToMerchIndexingConfig( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from merchIndexingConfig. 
	 * @param value the item to remove from merchIndexingConfig
	 */
	public void removeFromMerchIndexingConfig(final SessionContext ctx, final CatalogVersion item, final MerchIndexingConfig value)
	{
		item.removeLinkedItems( 
			ctx,
			false,
			MerchandisingservicesConstants.Relations.MERCHINDEXINGCONFIG2CATALOGVERSION,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(MERCHINDEXINGCONFIG2CATALOGVERSION_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(MERCHINDEXINGCONFIG2CATALOGVERSION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from merchIndexingConfig. 
	 * @param value the item to remove from merchIndexingConfig
	 */
	public void removeFromMerchIndexingConfig(final CatalogVersion item, final MerchIndexingConfig value)
	{
		removeFromMerchIndexingConfig( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseSite.merchProductDirectoryConfig</code> attribute.
	 * @return the merchProductDirectoryConfig
	 */
	public MerchProductDirectoryConfig getMerchProductDirectoryConfig(final SessionContext ctx, final BaseSite item)
	{
		return (MerchProductDirectoryConfig)item.getProperty( ctx, MerchandisingservicesConstants.Attributes.BaseSite.MERCHPRODUCTDIRECTORYCONFIG);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseSite.merchProductDirectoryConfig</code> attribute.
	 * @return the merchProductDirectoryConfig
	 */
	public MerchProductDirectoryConfig getMerchProductDirectoryConfig(final BaseSite item)
	{
		return getMerchProductDirectoryConfig( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseSite.merchProductDirectoryConfig</code> attribute. 
	 * @param value the merchProductDirectoryConfig
	 */
	public void setMerchProductDirectoryConfig(final SessionContext ctx, final BaseSite item, final MerchProductDirectoryConfig value)
	{
		item.setProperty(ctx, MerchandisingservicesConstants.Attributes.BaseSite.MERCHPRODUCTDIRECTORYCONFIG,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseSite.merchProductDirectoryConfig</code> attribute. 
	 * @param value the merchProductDirectoryConfig
	 */
	public void setMerchProductDirectoryConfig(final BaseSite item, final MerchProductDirectoryConfig value)
	{
		setMerchProductDirectoryConfig( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersion.merchProductDirectoryConfig</code> attribute.
	 * @return the merchProductDirectoryConfig
	 */
	public Collection<MerchProductDirectoryConfig> getMerchProductDirectoryConfig(final SessionContext ctx, final CatalogVersion item)
	{
		final List<MerchProductDirectoryConfig> items = item.getLinkedItems( 
			ctx,
			false,
			MerchandisingservicesConstants.Relations.MERCHPRODUCTDIR2CATALOGVERSION,
			"MerchProductDirectoryConfig",
			null,
			Utilities.getRelationOrderingOverride(MERCHPRODUCTDIR2CATALOGVERSION_SRC_ORDERED, true),
			false
		);
		return items;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersion.merchProductDirectoryConfig</code> attribute.
	 * @return the merchProductDirectoryConfig
	 */
	public Collection<MerchProductDirectoryConfig> getMerchProductDirectoryConfig(final CatalogVersion item)
	{
		return getMerchProductDirectoryConfig( getSession().getSessionContext(), item );
	}
	
	public long getMerchProductDirectoryConfigCount(final SessionContext ctx, final CatalogVersion item)
	{
		return item.getLinkedItemsCount(
			ctx,
			false,
			MerchandisingservicesConstants.Relations.MERCHPRODUCTDIR2CATALOGVERSION,
			"MerchProductDirectoryConfig",
			null
		);
	}
	
	public long getMerchProductDirectoryConfigCount(final CatalogVersion item)
	{
		return getMerchProductDirectoryConfigCount( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CatalogVersion.merchProductDirectoryConfig</code> attribute. 
	 * @param value the merchProductDirectoryConfig
	 */
	public void setMerchProductDirectoryConfig(final SessionContext ctx, final CatalogVersion item, final Collection<MerchProductDirectoryConfig> value)
	{
		item.setLinkedItems( 
			ctx,
			false,
			MerchandisingservicesConstants.Relations.MERCHPRODUCTDIR2CATALOGVERSION,
			null,
			value,
			Utilities.getRelationOrderingOverride(MERCHPRODUCTDIR2CATALOGVERSION_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(MERCHPRODUCTDIR2CATALOGVERSION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CatalogVersion.merchProductDirectoryConfig</code> attribute. 
	 * @param value the merchProductDirectoryConfig
	 */
	public void setMerchProductDirectoryConfig(final CatalogVersion item, final Collection<MerchProductDirectoryConfig> value)
	{
		setMerchProductDirectoryConfig( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to merchProductDirectoryConfig. 
	 * @param value the item to add to merchProductDirectoryConfig
	 */
	public void addToMerchProductDirectoryConfig(final SessionContext ctx, final CatalogVersion item, final MerchProductDirectoryConfig value)
	{
		item.addLinkedItems( 
			ctx,
			false,
			MerchandisingservicesConstants.Relations.MERCHPRODUCTDIR2CATALOGVERSION,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(MERCHPRODUCTDIR2CATALOGVERSION_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(MERCHPRODUCTDIR2CATALOGVERSION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to merchProductDirectoryConfig. 
	 * @param value the item to add to merchProductDirectoryConfig
	 */
	public void addToMerchProductDirectoryConfig(final CatalogVersion item, final MerchProductDirectoryConfig value)
	{
		addToMerchProductDirectoryConfig( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from merchProductDirectoryConfig. 
	 * @param value the item to remove from merchProductDirectoryConfig
	 */
	public void removeFromMerchProductDirectoryConfig(final SessionContext ctx, final CatalogVersion item, final MerchProductDirectoryConfig value)
	{
		item.removeLinkedItems( 
			ctx,
			false,
			MerchandisingservicesConstants.Relations.MERCHPRODUCTDIR2CATALOGVERSION,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(MERCHPRODUCTDIR2CATALOGVERSION_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(MERCHPRODUCTDIR2CATALOGVERSION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from merchProductDirectoryConfig. 
	 * @param value the item to remove from merchProductDirectoryConfig
	 */
	public void removeFromMerchProductDirectoryConfig(final CatalogVersion item, final MerchProductDirectoryConfig value)
	{
		removeFromMerchProductDirectoryConfig( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseSite.merchProductDirectoryConfigPOS</code> attribute.
	 * @return the merchProductDirectoryConfigPOS
	 */
	 Integer getMerchProductDirectoryConfigPOS(final SessionContext ctx, final BaseSite item)
	{
		return (Integer)item.getProperty( ctx, MerchandisingservicesConstants.Attributes.BaseSite.MERCHPRODUCTDIRECTORYCONFIGPOS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseSite.merchProductDirectoryConfigPOS</code> attribute.
	 * @return the merchProductDirectoryConfigPOS
	 */
	 Integer getMerchProductDirectoryConfigPOS(final BaseSite item)
	{
		return getMerchProductDirectoryConfigPOS( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseSite.merchProductDirectoryConfigPOS</code> attribute. 
	 * @return the merchProductDirectoryConfigPOS
	 */
	 int getMerchProductDirectoryConfigPOSAsPrimitive(final SessionContext ctx, final BaseSite item)
	{
		Integer value = getMerchProductDirectoryConfigPOS( ctx,item );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseSite.merchProductDirectoryConfigPOS</code> attribute. 
	 * @return the merchProductDirectoryConfigPOS
	 */
	 int getMerchProductDirectoryConfigPOSAsPrimitive(final BaseSite item)
	{
		return getMerchProductDirectoryConfigPOSAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseSite.merchProductDirectoryConfigPOS</code> attribute. 
	 * @param value the merchProductDirectoryConfigPOS
	 */
	 void setMerchProductDirectoryConfigPOS(final SessionContext ctx, final BaseSite item, final Integer value)
	{
		item.setProperty(ctx, MerchandisingservicesConstants.Attributes.BaseSite.MERCHPRODUCTDIRECTORYCONFIGPOS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseSite.merchProductDirectoryConfigPOS</code> attribute. 
	 * @param value the merchProductDirectoryConfigPOS
	 */
	 void setMerchProductDirectoryConfigPOS(final BaseSite item, final Integer value)
	{
		setMerchProductDirectoryConfigPOS( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseSite.merchProductDirectoryConfigPOS</code> attribute. 
	 * @param value the merchProductDirectoryConfigPOS
	 */
	 void setMerchProductDirectoryConfigPOS(final SessionContext ctx, final BaseSite item, final int value)
	{
		setMerchProductDirectoryConfigPOS( ctx, item, Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseSite.merchProductDirectoryConfigPOS</code> attribute. 
	 * @param value the merchProductDirectoryConfigPOS
	 */
	 void setMerchProductDirectoryConfigPOS(final BaseSite item, final int value)
	{
		setMerchProductDirectoryConfigPOS( getSession().getSessionContext(), item, value );
	}
	
}
