/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 11-Dec-2021, 12:32:58 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.productconfig.services.jalo;

import de.hybris.platform.catalog.jalo.classification.ClassificationAttribute;
import de.hybris.platform.catalog.jalo.classification.ClassificationAttributeValue;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.link.Link;
import de.hybris.platform.jalo.media.MediaContainer;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.sap.productconfig.services.constants.SapproductconfigservicesConstants;
import de.hybris.platform.sap.productconfig.services.jalo.CMSCartConfigurationRestriction;
import de.hybris.platform.sap.productconfig.services.jalo.CPQConfiguratorSettings;
import de.hybris.platform.sap.productconfig.services.jalo.CPQOrderEntryProductInfo;
import de.hybris.platform.sap.productconfig.services.jalo.MockVariantProduct;
import de.hybris.platform.sap.productconfig.services.jalo.ProductConfiguration;
import de.hybris.platform.sap.productconfig.services.jalo.ProductConfigurationPersistenceCleanUpCronJob;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.platform.util.PartOfHandler;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generated class for type <code>SapproductconfigservicesManager</code>.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedSapproductconfigservicesManager extends Extension
{
	/** Relation ordering override parameter constants for Product2ProductConfigs from ((sapproductconfigservices))*/
	protected static String PRODUCT2PRODUCTCONFIGS_SRC_ORDERED = "relation.Product2ProductConfigs.source.ordered";
	protected static String PRODUCT2PRODUCTCONFIGS_TGT_ORDERED = "relation.Product2ProductConfigs.target.ordered";
	/** Relation disable markmodifed parameter constants for Product2ProductConfigs from ((sapproductconfigservices))*/
	protected static String PRODUCT2PRODUCTCONFIGS_MARKMODIFIED = "relation.Product2ProductConfigs.markmodified";
	/**
	* {@link OneToManyHandler} for handling 1:n PRODUCTCONFIGURATION's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<ProductConfiguration> USER2PRODUCTCONFIGSPRODUCTCONFIGURATIONHANDLER = new OneToManyHandler<ProductConfiguration>(
	SapproductconfigservicesConstants.TC.PRODUCTCONFIGURATION,
	true,
	"user",
	null,
	false,
	true,
	CollectionType.COLLECTION
	);
	protected static final Map<String, Map<String, AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, Map<String, AttributeMode>> ttmp = new HashMap();
		Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put("cpqMedia", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.catalog.jalo.classification.ClassificationAttribute", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("cpqMedia", AttributeMode.INITIAL);
		tmp.put("description", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.catalog.jalo.classification.ClassificationAttributeValue", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("cpqStatusSummaryMap", AttributeMode.INITIAL);
		tmp.put("productConfiguration", AttributeMode.INITIAL);
		tmp.put("productConfigurationDraft", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.jalo.order.AbstractOrderEntry", Collections.unmodifiableMap(tmp));
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
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassificationAttribute.cpqMedia</code> attribute.
	 * @return the cpqMedia - It is a media container with images corresponding to the characteristic
	 */
	public MediaContainer getCpqMedia(final SessionContext ctx, final ClassificationAttribute item)
	{
		return (MediaContainer)item.getProperty( ctx, SapproductconfigservicesConstants.Attributes.ClassificationAttribute.CPQMEDIA);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassificationAttribute.cpqMedia</code> attribute.
	 * @return the cpqMedia - It is a media container with images corresponding to the characteristic
	 */
	public MediaContainer getCpqMedia(final ClassificationAttribute item)
	{
		return getCpqMedia( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ClassificationAttribute.cpqMedia</code> attribute. 
	 * @param value the cpqMedia - It is a media container with images corresponding to the characteristic
	 */
	public void setCpqMedia(final SessionContext ctx, final ClassificationAttribute item, final MediaContainer value)
	{
		item.setProperty(ctx, SapproductconfigservicesConstants.Attributes.ClassificationAttribute.CPQMEDIA,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ClassificationAttribute.cpqMedia</code> attribute. 
	 * @param value the cpqMedia - It is a media container with images corresponding to the characteristic
	 */
	public void setCpqMedia(final ClassificationAttribute item, final MediaContainer value)
	{
		setCpqMedia( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassificationAttributeValue.cpqMedia</code> attribute.
	 * @return the cpqMedia - It is a media container with images corresponding to the value
	 */
	public MediaContainer getCpqMedia(final SessionContext ctx, final ClassificationAttributeValue item)
	{
		return (MediaContainer)item.getProperty( ctx, SapproductconfigservicesConstants.Attributes.ClassificationAttributeValue.CPQMEDIA);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassificationAttributeValue.cpqMedia</code> attribute.
	 * @return the cpqMedia - It is a media container with images corresponding to the value
	 */
	public MediaContainer getCpqMedia(final ClassificationAttributeValue item)
	{
		return getCpqMedia( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ClassificationAttributeValue.cpqMedia</code> attribute. 
	 * @param value the cpqMedia - It is a media container with images corresponding to the value
	 */
	public void setCpqMedia(final SessionContext ctx, final ClassificationAttributeValue item, final MediaContainer value)
	{
		item.setProperty(ctx, SapproductconfigservicesConstants.Attributes.ClassificationAttributeValue.CPQMEDIA,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ClassificationAttributeValue.cpqMedia</code> attribute. 
	 * @param value the cpqMedia - It is a media container with images corresponding to the value
	 */
	public void setCpqMedia(final ClassificationAttributeValue item, final MediaContainer value)
	{
		setCpqMedia( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.cpqStatusSummaryMap</code> attribute.
	 * @return the cpqStatusSummaryMap - Status of the configuration and number of errors.
	 */
	public Map<EnumerationValue,Integer> getAllCpqStatusSummaryMap(final SessionContext ctx, final AbstractOrderEntry item)
	{
		Map<EnumerationValue,Integer> map = (Map<EnumerationValue,Integer>)item.getProperty( ctx, SapproductconfigservicesConstants.Attributes.AbstractOrderEntry.CPQSTATUSSUMMARYMAP);
		return map != null ? map : Collections.EMPTY_MAP;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.cpqStatusSummaryMap</code> attribute.
	 * @return the cpqStatusSummaryMap - Status of the configuration and number of errors.
	 */
	public Map<EnumerationValue,Integer> getAllCpqStatusSummaryMap(final AbstractOrderEntry item)
	{
		return getAllCpqStatusSummaryMap( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.cpqStatusSummaryMap</code> attribute. 
	 * @param value the cpqStatusSummaryMap - Status of the configuration and number of errors.
	 */
	public void setAllCpqStatusSummaryMap(final SessionContext ctx, final AbstractOrderEntry item, final Map<EnumerationValue,Integer> value)
	{
		item.setProperty(ctx, SapproductconfigservicesConstants.Attributes.AbstractOrderEntry.CPQSTATUSSUMMARYMAP,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.cpqStatusSummaryMap</code> attribute. 
	 * @param value the cpqStatusSummaryMap - Status of the configuration and number of errors.
	 */
	public void setAllCpqStatusSummaryMap(final AbstractOrderEntry item, final Map<EnumerationValue,Integer> value)
	{
		setAllCpqStatusSummaryMap( getSession().getSessionContext(), item, value );
	}
	
	public CMSCartConfigurationRestriction createCMSCartConfigurationRestriction(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( SapproductconfigservicesConstants.TC.CMSCARTCONFIGURATIONRESTRICTION );
			return (CMSCartConfigurationRestriction)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating CMSCartConfigurationRestriction : "+e.getMessage(), 0 );
		}
	}
	
	public CMSCartConfigurationRestriction createCMSCartConfigurationRestriction(final Map attributeValues)
	{
		return createCMSCartConfigurationRestriction( getSession().getSessionContext(), attributeValues );
	}
	
	public CPQConfiguratorSettings createCPQConfiguratorSettings(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( SapproductconfigservicesConstants.TC.CPQCONFIGURATORSETTINGS );
			return (CPQConfiguratorSettings)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating CPQConfiguratorSettings : "+e.getMessage(), 0 );
		}
	}
	
	public CPQConfiguratorSettings createCPQConfiguratorSettings(final Map attributeValues)
	{
		return createCPQConfiguratorSettings( getSession().getSessionContext(), attributeValues );
	}
	
	public CPQOrderEntryProductInfo createCPQOrderEntryProductInfo(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( SapproductconfigservicesConstants.TC.CPQORDERENTRYPRODUCTINFO );
			return (CPQOrderEntryProductInfo)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating CPQOrderEntryProductInfo : "+e.getMessage(), 0 );
		}
	}
	
	public CPQOrderEntryProductInfo createCPQOrderEntryProductInfo(final Map attributeValues)
	{
		return createCPQOrderEntryProductInfo( getSession().getSessionContext(), attributeValues );
	}
	
	public MockVariantProduct createMockVariantProduct(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( SapproductconfigservicesConstants.TC.MOCKVARIANTPRODUCT );
			return (MockVariantProduct)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating MockVariantProduct : "+e.getMessage(), 0 );
		}
	}
	
	public MockVariantProduct createMockVariantProduct(final Map attributeValues)
	{
		return createMockVariantProduct( getSession().getSessionContext(), attributeValues );
	}
	
	public ProductConfiguration createProductConfiguration(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( SapproductconfigservicesConstants.TC.PRODUCTCONFIGURATION );
			return (ProductConfiguration)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating ProductConfiguration : "+e.getMessage(), 0 );
		}
	}
	
	public ProductConfiguration createProductConfiguration(final Map attributeValues)
	{
		return createProductConfiguration( getSession().getSessionContext(), attributeValues );
	}
	
	public ProductConfigurationPersistenceCleanUpCronJob createProductConfigurationPersistenceCleanUpCronJob(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( SapproductconfigservicesConstants.TC.PRODUCTCONFIGURATIONPERSISTENCECLEANUPCRONJOB );
			return (ProductConfigurationPersistenceCleanUpCronJob)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating ProductConfigurationPersistenceCleanUpCronJob : "+e.getMessage(), 0 );
		}
	}
	
	public ProductConfigurationPersistenceCleanUpCronJob createProductConfigurationPersistenceCleanUpCronJob(final Map attributeValues)
	{
		return createProductConfigurationPersistenceCleanUpCronJob( getSession().getSessionContext(), attributeValues );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassificationAttributeValue.description</code> attribute.
	 * @return the description - Language independent name of the characteristic value
	 */
	public String getDescription(final SessionContext ctx, final ClassificationAttributeValue item)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedClassificationAttributeValue.getDescription requires a session language", 0 );
		}
		return (String)item.getLocalizedProperty( ctx, SapproductconfigservicesConstants.Attributes.ClassificationAttributeValue.DESCRIPTION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassificationAttributeValue.description</code> attribute.
	 * @return the description - Language independent name of the characteristic value
	 */
	public String getDescription(final ClassificationAttributeValue item)
	{
		return getDescription( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassificationAttributeValue.description</code> attribute. 
	 * @return the localized description - Language independent name of the characteristic value
	 */
	public Map<Language,String> getAllDescription(final SessionContext ctx, final ClassificationAttributeValue item)
	{
		return (Map<Language,String>)item.getAllLocalizedProperties(ctx,SapproductconfigservicesConstants.Attributes.ClassificationAttributeValue.DESCRIPTION,C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassificationAttributeValue.description</code> attribute. 
	 * @return the localized description - Language independent name of the characteristic value
	 */
	public Map<Language,String> getAllDescription(final ClassificationAttributeValue item)
	{
		return getAllDescription( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ClassificationAttributeValue.description</code> attribute. 
	 * @param value the description - Language independent name of the characteristic value
	 */
	public void setDescription(final SessionContext ctx, final ClassificationAttributeValue item, final String value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		if( ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedClassificationAttributeValue.setDescription requires a session language", 0 );
		}
		item.setLocalizedProperty(ctx, SapproductconfigservicesConstants.Attributes.ClassificationAttributeValue.DESCRIPTION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ClassificationAttributeValue.description</code> attribute. 
	 * @param value the description - Language independent name of the characteristic value
	 */
	public void setDescription(final ClassificationAttributeValue item, final String value)
	{
		setDescription( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ClassificationAttributeValue.description</code> attribute. 
	 * @param value the description - Language independent name of the characteristic value
	 */
	public void setAllDescription(final SessionContext ctx, final ClassificationAttributeValue item, final Map<Language,String> value)
	{
		item.setAllLocalizedProperties(ctx,SapproductconfigservicesConstants.Attributes.ClassificationAttributeValue.DESCRIPTION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ClassificationAttributeValue.description</code> attribute. 
	 * @param value the description - Language independent name of the characteristic value
	 */
	public void setAllDescription(final ClassificationAttributeValue item, final Map<Language,String> value)
	{
		setAllDescription( getSession().getSessionContext(), item, value );
	}
	
	@Override
	public String getName()
	{
		return SapproductconfigservicesConstants.EXTENSIONNAME;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.productConfiguration</code> attribute.
	 * @return the productConfiguration - Active Product Configuration.
	 */
	public ProductConfiguration getProductConfiguration(final SessionContext ctx, final AbstractOrderEntry item)
	{
		return (ProductConfiguration)item.getProperty( ctx, SapproductconfigservicesConstants.Attributes.AbstractOrderEntry.PRODUCTCONFIGURATION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.productConfiguration</code> attribute.
	 * @return the productConfiguration - Active Product Configuration.
	 */
	public ProductConfiguration getProductConfiguration(final AbstractOrderEntry item)
	{
		return getProductConfiguration( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.productConfiguration</code> attribute. 
	 * @param value the productConfiguration - Active Product Configuration.
	 */
	public void setProductConfiguration(final SessionContext ctx, final AbstractOrderEntry item, final ProductConfiguration value)
	{
		item.setProperty(ctx, SapproductconfigservicesConstants.Attributes.AbstractOrderEntry.PRODUCTCONFIGURATION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.productConfiguration</code> attribute. 
	 * @param value the productConfiguration - Active Product Configuration.
	 */
	public void setProductConfiguration(final AbstractOrderEntry item, final ProductConfiguration value)
	{
		setProductConfiguration( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.productConfiguration</code> attribute.
	 * @return the productConfiguration
	 */
	public Collection<ProductConfiguration> getProductConfiguration(final SessionContext ctx, final Product item)
	{
		final List<ProductConfiguration> items = item.getLinkedItems( 
			ctx,
			true,
			SapproductconfigservicesConstants.Relations.PRODUCT2PRODUCTCONFIGS,
			"ProductConfiguration",
			null,
			false,
			false
		);
		return items;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.productConfiguration</code> attribute.
	 * @return the productConfiguration
	 */
	public Collection<ProductConfiguration> getProductConfiguration(final Product item)
	{
		return getProductConfiguration( getSession().getSessionContext(), item );
	}
	
	public long getProductConfigurationCount(final SessionContext ctx, final Product item)
	{
		return item.getLinkedItemsCount(
			ctx,
			true,
			SapproductconfigservicesConstants.Relations.PRODUCT2PRODUCTCONFIGS,
			"ProductConfiguration",
			null
		);
	}
	
	public long getProductConfigurationCount(final Product item)
	{
		return getProductConfigurationCount( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.productConfiguration</code> attribute. 
	 * @param value the productConfiguration
	 */
	public void setProductConfiguration(final SessionContext ctx, final Product item, final Collection<ProductConfiguration> value)
	{
		new PartOfHandler<Collection<ProductConfiguration>>()
		{
			@Override
			protected Collection<ProductConfiguration> doGetValue(final SessionContext ctx)
			{
				return getProductConfiguration( ctx, item );
			}
			@Override
			protected void doSetValue(final SessionContext ctx, final Collection<ProductConfiguration> _value)
			{
				final Collection<ProductConfiguration> value = _value;
				item.setLinkedItems( 
					ctx,
					true,
					SapproductconfigservicesConstants.Relations.PRODUCT2PRODUCTCONFIGS,
					null,
					value,
					false,
					false,
					Utilities.getMarkModifiedOverride(PRODUCT2PRODUCTCONFIGS_MARKMODIFIED)
				);
			}
		}.setValue( ctx, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.productConfiguration</code> attribute. 
	 * @param value the productConfiguration
	 */
	public void setProductConfiguration(final Product item, final Collection<ProductConfiguration> value)
	{
		setProductConfiguration( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to productConfiguration. 
	 * @param value the item to add to productConfiguration
	 */
	public void addToProductConfiguration(final SessionContext ctx, final Product item, final ProductConfiguration value)
	{
		item.addLinkedItems( 
			ctx,
			true,
			SapproductconfigservicesConstants.Relations.PRODUCT2PRODUCTCONFIGS,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(PRODUCT2PRODUCTCONFIGS_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to productConfiguration. 
	 * @param value the item to add to productConfiguration
	 */
	public void addToProductConfiguration(final Product item, final ProductConfiguration value)
	{
		addToProductConfiguration( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from productConfiguration. 
	 * @param value the item to remove from productConfiguration
	 */
	public void removeFromProductConfiguration(final SessionContext ctx, final Product item, final ProductConfiguration value)
	{
		item.removeLinkedItems( 
			ctx,
			true,
			SapproductconfigservicesConstants.Relations.PRODUCT2PRODUCTCONFIGS,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(PRODUCT2PRODUCTCONFIGS_MARKMODIFIED)
		);
		if( !item.getLinkedItems( ctx, true,SapproductconfigservicesConstants.Relations.PRODUCT2PRODUCTCONFIGS,null).contains( value ) )
		{
			try
			{
				value.remove( ctx );
			}
			catch( ConsistencyCheckException e )
			{
				throw new JaloSystemException(e);
			}
		}
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from productConfiguration. 
	 * @param value the item to remove from productConfiguration
	 */
	public void removeFromProductConfiguration(final Product item, final ProductConfiguration value)
	{
		removeFromProductConfiguration( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>User.productConfiguration</code> attribute.
	 * @return the productConfiguration
	 */
	public Collection<ProductConfiguration> getProductConfiguration(final SessionContext ctx, final User item)
	{
		return USER2PRODUCTCONFIGSPRODUCTCONFIGURATIONHANDLER.getValues( ctx, item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>User.productConfiguration</code> attribute.
	 * @return the productConfiguration
	 */
	public Collection<ProductConfiguration> getProductConfiguration(final User item)
	{
		return getProductConfiguration( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>User.productConfiguration</code> attribute. 
	 * @param value the productConfiguration
	 */
	public void setProductConfiguration(final SessionContext ctx, final User item, final Collection<ProductConfiguration> value)
	{
		USER2PRODUCTCONFIGSPRODUCTCONFIGURATIONHANDLER.setValues( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>User.productConfiguration</code> attribute. 
	 * @param value the productConfiguration
	 */
	public void setProductConfiguration(final User item, final Collection<ProductConfiguration> value)
	{
		setProductConfiguration( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to productConfiguration. 
	 * @param value the item to add to productConfiguration
	 */
	public void addToProductConfiguration(final SessionContext ctx, final User item, final ProductConfiguration value)
	{
		USER2PRODUCTCONFIGSPRODUCTCONFIGURATIONHANDLER.addValue( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to productConfiguration. 
	 * @param value the item to add to productConfiguration
	 */
	public void addToProductConfiguration(final User item, final ProductConfiguration value)
	{
		addToProductConfiguration( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from productConfiguration. 
	 * @param value the item to remove from productConfiguration
	 */
	public void removeFromProductConfiguration(final SessionContext ctx, final User item, final ProductConfiguration value)
	{
		USER2PRODUCTCONFIGSPRODUCTCONFIGURATIONHANDLER.removeValue( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from productConfiguration. 
	 * @param value the item to remove from productConfiguration
	 */
	public void removeFromProductConfiguration(final User item, final ProductConfiguration value)
	{
		removeFromProductConfiguration( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.productConfigurationDraft</code> attribute.
	 * @return the productConfigurationDraft - ProductConfiguration Draft.
	 */
	public ProductConfiguration getProductConfigurationDraft(final SessionContext ctx, final AbstractOrderEntry item)
	{
		return (ProductConfiguration)item.getProperty( ctx, SapproductconfigservicesConstants.Attributes.AbstractOrderEntry.PRODUCTCONFIGURATIONDRAFT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.productConfigurationDraft</code> attribute.
	 * @return the productConfigurationDraft - ProductConfiguration Draft.
	 */
	public ProductConfiguration getProductConfigurationDraft(final AbstractOrderEntry item)
	{
		return getProductConfigurationDraft( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.productConfigurationDraft</code> attribute. 
	 * @param value the productConfigurationDraft - ProductConfiguration Draft.
	 */
	public void setProductConfigurationDraft(final SessionContext ctx, final AbstractOrderEntry item, final ProductConfiguration value)
	{
		item.setProperty(ctx, SapproductconfigservicesConstants.Attributes.AbstractOrderEntry.PRODUCTCONFIGURATIONDRAFT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.productConfigurationDraft</code> attribute. 
	 * @param value the productConfigurationDraft - ProductConfiguration Draft.
	 */
	public void setProductConfigurationDraft(final AbstractOrderEntry item, final ProductConfiguration value)
	{
		setProductConfigurationDraft( getSession().getSessionContext(), item, value );
	}
	
}
