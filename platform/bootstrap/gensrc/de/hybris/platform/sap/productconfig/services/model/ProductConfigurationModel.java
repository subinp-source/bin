/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 15-Dec-2021, 3:07:44 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.productconfig.services.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

/**
 * Generated model class for type ProductConfiguration first defined at extension sapproductconfigservices.
 * <p>
 * Product Configuration attributes for hybris persistence enhancements.
 */
@SuppressWarnings("all")
public class ProductConfigurationModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ProductConfiguration";
	
	/**<i>Generated relation code constant for relation <code>Product2ProductConfigs</code> defining source attribute <code>product</code> in extension <code>sapproductconfigservices</code>.</i>*/
	public static final String _PRODUCT2PRODUCTCONFIGS = "Product2ProductConfigs";
	
	/**<i>Generated relation code constant for relation <code>User2ProductConfigs</code> defining source attribute <code>user</code> in extension <code>sapproductconfigservices</code>.</i>*/
	public static final String _USER2PRODUCTCONFIGS = "User2ProductConfigs";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductConfiguration.configurationId</code> attribute defined at extension <code>sapproductconfigservices</code>. */
	public static final String CONFIGURATIONID = "configurationId";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductConfiguration.version</code> attribute defined at extension <code>sapproductconfigservices</code>. */
	public static final String VERSION = "version";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductConfiguration.userSessionId</code> attribute defined at extension <code>sapproductconfigservices</code>. */
	public static final String USERSESSIONID = "userSessionId";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductConfiguration.kbName</code> attribute defined at extension <code>sapproductconfigservices</code>. */
	public static final String KBNAME = "kbName";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductConfiguration.kbVersion</code> attribute defined at extension <code>sapproductconfigservices</code>. */
	public static final String KBVERSION = "kbVersion";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductConfiguration.kbLogsys</code> attribute defined at extension <code>sapproductconfigservices</code>. */
	public static final String KBLOGSYS = "kbLogsys";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductConfiguration.product</code> attribute defined at extension <code>sapproductconfigservices</code>. */
	public static final String PRODUCT = "product";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductConfiguration.user</code> attribute defined at extension <code>sapproductconfigservices</code>. */
	public static final String USER = "user";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ProductConfigurationModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ProductConfigurationModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _product initial attribute declared by type <code>ProductConfiguration</code> at extension <code>sapproductconfigservices</code>
	 * @param _user initial attribute declared by type <code>ProductConfiguration</code> at extension <code>sapproductconfigservices</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public ProductConfigurationModel(final Collection<ProductModel> _product, final UserModel _user)
	{
		super();
		setProduct(_product);
		setUser(_user);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _product initial attribute declared by type <code>ProductConfiguration</code> at extension <code>sapproductconfigservices</code>
	 * @param _user initial attribute declared by type <code>ProductConfiguration</code> at extension <code>sapproductconfigservices</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public ProductConfigurationModel(final ItemModel _owner, final Collection<ProductModel> _product, final UserModel _user)
	{
		super();
		setOwner(_owner);
		setProduct(_product);
		setUser(_user);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductConfiguration.configurationId</code> attribute defined at extension <code>sapproductconfigservices</code>. 
	 * @return the configurationId - Configuration Id.
	 */
	@Accessor(qualifier = "configurationId", type = Accessor.Type.GETTER)
	public String getConfigurationId()
	{
		return getPersistenceContext().getPropertyValue(CONFIGURATIONID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductConfiguration.kbLogsys</code> attribute defined at extension <code>sapproductconfigservices</code>. 
	 * @return the kbLogsys - Logical system of the knowledgebase object.
	 */
	@Accessor(qualifier = "kbLogsys", type = Accessor.Type.GETTER)
	public String getKbLogsys()
	{
		return getPersistenceContext().getPropertyValue(KBLOGSYS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductConfiguration.kbName</code> attribute defined at extension <code>sapproductconfigservices</code>. 
	 * @return the kbName - Name of the knowledgebase object.
	 */
	@Accessor(qualifier = "kbName", type = Accessor.Type.GETTER)
	public String getKbName()
	{
		return getPersistenceContext().getPropertyValue(KBNAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductConfiguration.kbVersion</code> attribute defined at extension <code>sapproductconfigservices</code>. 
	 * @return the kbVersion - Version of the knowledgebase object.
	 */
	@Accessor(qualifier = "kbVersion", type = Accessor.Type.GETTER)
	public String getKbVersion()
	{
		return getPersistenceContext().getPropertyValue(KBVERSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductConfiguration.product</code> attribute defined at extension <code>sapproductconfigservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the product
	 */
	@Accessor(qualifier = "product", type = Accessor.Type.GETTER)
	public Collection<ProductModel> getProduct()
	{
		return getPersistenceContext().getPropertyValue(PRODUCT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductConfiguration.user</code> attribute defined at extension <code>sapproductconfigservices</code>. 
	 * @return the user
	 */
	@Accessor(qualifier = "user", type = Accessor.Type.GETTER)
	public UserModel getUser()
	{
		return getPersistenceContext().getPropertyValue(USER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductConfiguration.userSessionId</code> attribute defined at extension <code>sapproductconfigservices</code>. 
	 * @return the userSessionId - ID of the user session this config is associated with.
	 */
	@Accessor(qualifier = "userSessionId", type = Accessor.Type.GETTER)
	public String getUserSessionId()
	{
		return getPersistenceContext().getPropertyValue(USERSESSIONID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductConfiguration.version</code> attribute defined at extension <code>sapproductconfigservices</code>. 
	 * @return the version - Configuration Runtime Version.
	 */
	@Accessor(qualifier = "version", type = Accessor.Type.GETTER)
	public String getVersion()
	{
		return getPersistenceContext().getPropertyValue(VERSION);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductConfiguration.configurationId</code> attribute defined at extension <code>sapproductconfigservices</code>. 
	 *  
	 * @param value the configurationId - Configuration Id.
	 */
	@Accessor(qualifier = "configurationId", type = Accessor.Type.SETTER)
	public void setConfigurationId(final String value)
	{
		getPersistenceContext().setPropertyValue(CONFIGURATIONID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductConfiguration.kbLogsys</code> attribute defined at extension <code>sapproductconfigservices</code>. 
	 *  
	 * @param value the kbLogsys - Logical system of the knowledgebase object.
	 */
	@Accessor(qualifier = "kbLogsys", type = Accessor.Type.SETTER)
	public void setKbLogsys(final String value)
	{
		getPersistenceContext().setPropertyValue(KBLOGSYS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductConfiguration.kbName</code> attribute defined at extension <code>sapproductconfigservices</code>. 
	 *  
	 * @param value the kbName - Name of the knowledgebase object.
	 */
	@Accessor(qualifier = "kbName", type = Accessor.Type.SETTER)
	public void setKbName(final String value)
	{
		getPersistenceContext().setPropertyValue(KBNAME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductConfiguration.kbVersion</code> attribute defined at extension <code>sapproductconfigservices</code>. 
	 *  
	 * @param value the kbVersion - Version of the knowledgebase object.
	 */
	@Accessor(qualifier = "kbVersion", type = Accessor.Type.SETTER)
	public void setKbVersion(final String value)
	{
		getPersistenceContext().setPropertyValue(KBVERSION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductConfiguration.product</code> attribute defined at extension <code>sapproductconfigservices</code>. 
	 *  
	 * @param value the product
	 */
	@Accessor(qualifier = "product", type = Accessor.Type.SETTER)
	public void setProduct(final Collection<ProductModel> value)
	{
		getPersistenceContext().setPropertyValue(PRODUCT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductConfiguration.user</code> attribute defined at extension <code>sapproductconfigservices</code>. 
	 *  
	 * @param value the user
	 */
	@Accessor(qualifier = "user", type = Accessor.Type.SETTER)
	public void setUser(final UserModel value)
	{
		getPersistenceContext().setPropertyValue(USER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductConfiguration.userSessionId</code> attribute defined at extension <code>sapproductconfigservices</code>. 
	 *  
	 * @param value the userSessionId - ID of the user session this config is associated with.
	 */
	@Accessor(qualifier = "userSessionId", type = Accessor.Type.SETTER)
	public void setUserSessionId(final String value)
	{
		getPersistenceContext().setPropertyValue(USERSESSIONID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductConfiguration.version</code> attribute defined at extension <code>sapproductconfigservices</code>. 
	 *  
	 * @param value the version - Configuration Runtime Version.
	 */
	@Accessor(qualifier = "version", type = Accessor.Type.SETTER)
	public void setVersion(final String value)
	{
		getPersistenceContext().setPropertyValue(VERSION, value);
	}
	
}
