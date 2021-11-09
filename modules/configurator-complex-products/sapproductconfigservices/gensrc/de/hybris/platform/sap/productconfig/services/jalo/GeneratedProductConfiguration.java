/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 08-Nov-2021, 4:51:25 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.productconfig.services.jalo;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.sap.productconfig.services.constants.SapproductconfigservicesConstants;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.jalo.GenericItem ProductConfiguration}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedProductConfiguration extends GenericItem
{
	/** Qualifier of the <code>ProductConfiguration.configurationId</code> attribute **/
	public static final String CONFIGURATIONID = "configurationId";
	/** Qualifier of the <code>ProductConfiguration.version</code> attribute **/
	public static final String VERSION = "version";
	/** Qualifier of the <code>ProductConfiguration.userSessionId</code> attribute **/
	public static final String USERSESSIONID = "userSessionId";
	/** Qualifier of the <code>ProductConfiguration.kbName</code> attribute **/
	public static final String KBNAME = "kbName";
	/** Qualifier of the <code>ProductConfiguration.kbVersion</code> attribute **/
	public static final String KBVERSION = "kbVersion";
	/** Qualifier of the <code>ProductConfiguration.kbLogsys</code> attribute **/
	public static final String KBLOGSYS = "kbLogsys";
	/** Qualifier of the <code>ProductConfiguration.product</code> attribute **/
	public static final String PRODUCT = "product";
	/** Relation ordering override parameter constants for Product2ProductConfigs from ((sapproductconfigservices))*/
	protected static String PRODUCT2PRODUCTCONFIGS_SRC_ORDERED = "relation.Product2ProductConfigs.source.ordered";
	protected static String PRODUCT2PRODUCTCONFIGS_TGT_ORDERED = "relation.Product2ProductConfigs.target.ordered";
	/** Relation disable markmodifed parameter constants for Product2ProductConfigs from ((sapproductconfigservices))*/
	protected static String PRODUCT2PRODUCTCONFIGS_MARKMODIFIED = "relation.Product2ProductConfigs.markmodified";
	/** Qualifier of the <code>ProductConfiguration.user</code> attribute **/
	public static final String USER = "user";
	/**
	* {@link BidirectionalOneToManyHandler} for handling 1:n USER's relation attributes from 'one' side.
	**/
	protected static final BidirectionalOneToManyHandler<GeneratedProductConfiguration> USERHANDLER = new BidirectionalOneToManyHandler<GeneratedProductConfiguration>(
	SapproductconfigservicesConstants.TC.PRODUCTCONFIGURATION,
	false,
	"user",
	null,
	false,
	true,
	CollectionType.COLLECTION
	);
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(CONFIGURATIONID, AttributeMode.INITIAL);
		tmp.put(VERSION, AttributeMode.INITIAL);
		tmp.put(USERSESSIONID, AttributeMode.INITIAL);
		tmp.put(KBNAME, AttributeMode.INITIAL);
		tmp.put(KBVERSION, AttributeMode.INITIAL);
		tmp.put(KBLOGSYS, AttributeMode.INITIAL);
		tmp.put(USER, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductConfiguration.configurationId</code> attribute.
	 * @return the configurationId - Configuration Id.
	 */
	public String getConfigurationId(final SessionContext ctx)
	{
		return (String)getProperty( ctx, CONFIGURATIONID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductConfiguration.configurationId</code> attribute.
	 * @return the configurationId - Configuration Id.
	 */
	public String getConfigurationId()
	{
		return getConfigurationId( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductConfiguration.configurationId</code> attribute. 
	 * @param value the configurationId - Configuration Id.
	 */
	public void setConfigurationId(final SessionContext ctx, final String value)
	{
		setProperty(ctx, CONFIGURATIONID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductConfiguration.configurationId</code> attribute. 
	 * @param value the configurationId - Configuration Id.
	 */
	public void setConfigurationId(final String value)
	{
		setConfigurationId( getSession().getSessionContext(), value );
	}
	
	@Override
	protected Item createItem(final SessionContext ctx, final ComposedType type, final ItemAttributeMap allAttributes) throws JaloBusinessException
	{
		USERHANDLER.newInstance(ctx, allAttributes);
		return super.createItem( ctx, type, allAttributes );
	}
	
	/**
	 * @deprecated since 2011, use {@link Utilities#getMarkModifiedOverride(de.hybris.platform.jalo.type.RelationType)
	 */
	@Override
	@Deprecated(since = "2105", forRemoval = true)
	public boolean isMarkModifiedDisabled(final Item referencedItem)
	{
		ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("Product");
		if(relationSecondEnd0.isAssignableFrom(referencedItem.getComposedType()))
		{
			return Utilities.getMarkModifiedOverride(PRODUCT2PRODUCTCONFIGS_MARKMODIFIED);
		}
		return true;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductConfiguration.kbLogsys</code> attribute.
	 * @return the kbLogsys - Logical system of the knowledgebase object.
	 */
	public String getKbLogsys(final SessionContext ctx)
	{
		return (String)getProperty( ctx, KBLOGSYS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductConfiguration.kbLogsys</code> attribute.
	 * @return the kbLogsys - Logical system of the knowledgebase object.
	 */
	public String getKbLogsys()
	{
		return getKbLogsys( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductConfiguration.kbLogsys</code> attribute. 
	 * @param value the kbLogsys - Logical system of the knowledgebase object.
	 */
	public void setKbLogsys(final SessionContext ctx, final String value)
	{
		setProperty(ctx, KBLOGSYS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductConfiguration.kbLogsys</code> attribute. 
	 * @param value the kbLogsys - Logical system of the knowledgebase object.
	 */
	public void setKbLogsys(final String value)
	{
		setKbLogsys( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductConfiguration.kbName</code> attribute.
	 * @return the kbName - Name of the knowledgebase object.
	 */
	public String getKbName(final SessionContext ctx)
	{
		return (String)getProperty( ctx, KBNAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductConfiguration.kbName</code> attribute.
	 * @return the kbName - Name of the knowledgebase object.
	 */
	public String getKbName()
	{
		return getKbName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductConfiguration.kbName</code> attribute. 
	 * @param value the kbName - Name of the knowledgebase object.
	 */
	public void setKbName(final SessionContext ctx, final String value)
	{
		setProperty(ctx, KBNAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductConfiguration.kbName</code> attribute. 
	 * @param value the kbName - Name of the knowledgebase object.
	 */
	public void setKbName(final String value)
	{
		setKbName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductConfiguration.kbVersion</code> attribute.
	 * @return the kbVersion - Version of the knowledgebase object.
	 */
	public String getKbVersion(final SessionContext ctx)
	{
		return (String)getProperty( ctx, KBVERSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductConfiguration.kbVersion</code> attribute.
	 * @return the kbVersion - Version of the knowledgebase object.
	 */
	public String getKbVersion()
	{
		return getKbVersion( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductConfiguration.kbVersion</code> attribute. 
	 * @param value the kbVersion - Version of the knowledgebase object.
	 */
	public void setKbVersion(final SessionContext ctx, final String value)
	{
		setProperty(ctx, KBVERSION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductConfiguration.kbVersion</code> attribute. 
	 * @param value the kbVersion - Version of the knowledgebase object.
	 */
	public void setKbVersion(final String value)
	{
		setKbVersion( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductConfiguration.product</code> attribute.
	 * @return the product
	 */
	public Collection<Product> getProduct(final SessionContext ctx)
	{
		final List<Product> items = getLinkedItems( 
			ctx,
			false,
			SapproductconfigservicesConstants.Relations.PRODUCT2PRODUCTCONFIGS,
			"Product",
			null,
			false,
			false
		);
		return items;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductConfiguration.product</code> attribute.
	 * @return the product
	 */
	public Collection<Product> getProduct()
	{
		return getProduct( getSession().getSessionContext() );
	}
	
	public long getProductCount(final SessionContext ctx)
	{
		return getLinkedItemsCount(
			ctx,
			false,
			SapproductconfigservicesConstants.Relations.PRODUCT2PRODUCTCONFIGS,
			"Product",
			null
		);
	}
	
	public long getProductCount()
	{
		return getProductCount( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductConfiguration.product</code> attribute. 
	 * @param value the product
	 */
	public void setProduct(final SessionContext ctx, final Collection<Product> value)
	{
		setLinkedItems( 
			ctx,
			false,
			SapproductconfigservicesConstants.Relations.PRODUCT2PRODUCTCONFIGS,
			null,
			value,
			false,
			false,
			Utilities.getMarkModifiedOverride(PRODUCT2PRODUCTCONFIGS_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductConfiguration.product</code> attribute. 
	 * @param value the product
	 */
	public void setProduct(final Collection<Product> value)
	{
		setProduct( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to product. 
	 * @param value the item to add to product
	 */
	public void addToProduct(final SessionContext ctx, final Product value)
	{
		addLinkedItems( 
			ctx,
			false,
			SapproductconfigservicesConstants.Relations.PRODUCT2PRODUCTCONFIGS,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(PRODUCT2PRODUCTCONFIGS_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to product. 
	 * @param value the item to add to product
	 */
	public void addToProduct(final Product value)
	{
		addToProduct( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from product. 
	 * @param value the item to remove from product
	 */
	public void removeFromProduct(final SessionContext ctx, final Product value)
	{
		removeLinkedItems( 
			ctx,
			false,
			SapproductconfigservicesConstants.Relations.PRODUCT2PRODUCTCONFIGS,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(PRODUCT2PRODUCTCONFIGS_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from product. 
	 * @param value the item to remove from product
	 */
	public void removeFromProduct(final Product value)
	{
		removeFromProduct( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductConfiguration.user</code> attribute.
	 * @return the user
	 */
	public User getUser(final SessionContext ctx)
	{
		return (User)getProperty( ctx, USER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductConfiguration.user</code> attribute.
	 * @return the user
	 */
	public User getUser()
	{
		return getUser( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductConfiguration.user</code> attribute. 
	 * @param value the user
	 */
	public void setUser(final SessionContext ctx, final User value)
	{
		USERHANDLER.addValue( ctx, value, this  );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductConfiguration.user</code> attribute. 
	 * @param value the user
	 */
	public void setUser(final User value)
	{
		setUser( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductConfiguration.userSessionId</code> attribute.
	 * @return the userSessionId - ID of the user session this config is associated with.
	 */
	public String getUserSessionId(final SessionContext ctx)
	{
		return (String)getProperty( ctx, USERSESSIONID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductConfiguration.userSessionId</code> attribute.
	 * @return the userSessionId - ID of the user session this config is associated with.
	 */
	public String getUserSessionId()
	{
		return getUserSessionId( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductConfiguration.userSessionId</code> attribute. 
	 * @param value the userSessionId - ID of the user session this config is associated with.
	 */
	public void setUserSessionId(final SessionContext ctx, final String value)
	{
		setProperty(ctx, USERSESSIONID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductConfiguration.userSessionId</code> attribute. 
	 * @param value the userSessionId - ID of the user session this config is associated with.
	 */
	public void setUserSessionId(final String value)
	{
		setUserSessionId( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductConfiguration.version</code> attribute.
	 * @return the version - Configuration Runtime Version.
	 */
	public String getVersion(final SessionContext ctx)
	{
		return (String)getProperty( ctx, VERSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductConfiguration.version</code> attribute.
	 * @return the version - Configuration Runtime Version.
	 */
	public String getVersion()
	{
		return getVersion( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductConfiguration.version</code> attribute. 
	 * @param value the version - Configuration Runtime Version.
	 */
	public void setVersion(final SessionContext ctx, final String value)
	{
		setProperty(ctx, VERSION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductConfiguration.version</code> attribute. 
	 * @param value the version - Configuration Runtime Version.
	 */
	public void setVersion(final String value)
	{
		setVersion( getSession().getSessionContext(), value );
	}
	
}
