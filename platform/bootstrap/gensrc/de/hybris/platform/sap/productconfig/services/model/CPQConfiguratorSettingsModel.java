/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 08-Nov-2021, 4:51:25 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.productconfig.services.model;

import de.hybris.platform.catalog.enums.ConfiguratorType;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.ConfigurationCategoryModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.product.model.AbstractConfiguratorSettingModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type CPQConfiguratorSettings first defined at extension sapproductconfigservices.
 * <p>
 * CPQ Configurator.
 */
@SuppressWarnings("all")
public class CPQConfiguratorSettingsModel extends AbstractConfiguratorSettingModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CPQConfiguratorSettings";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CPQConfiguratorSettingsModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CPQConfiguratorSettingsModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>AbstractConfiguratorSetting</code> at extension <code>catalog</code>
	 * @param _configurationCategory initial attribute declared by type <code>AbstractConfiguratorSetting</code> at extension <code>catalog</code>
	 * @param _configuratorType initial attribute declared by type <code>AbstractConfiguratorSetting</code> at extension <code>catalog</code>
	 * @param _id initial attribute declared by type <code>AbstractConfiguratorSetting</code> at extension <code>catalog</code>
	 * @param _qualifier initial attribute declared by type <code>AbstractConfiguratorSetting</code> at extension <code>catalog</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public CPQConfiguratorSettingsModel(final CatalogVersionModel _catalogVersion, final ConfigurationCategoryModel _configurationCategory, final ConfiguratorType _configuratorType, final String _id, final String _qualifier)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setConfigurationCategory(_configurationCategory);
		setConfiguratorType(_configuratorType);
		setId(_id);
		setQualifier(_qualifier);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>AbstractConfiguratorSetting</code> at extension <code>catalog</code>
	 * @param _configurationCategory initial attribute declared by type <code>AbstractConfiguratorSetting</code> at extension <code>catalog</code>
	 * @param _configuratorType initial attribute declared by type <code>AbstractConfiguratorSetting</code> at extension <code>catalog</code>
	 * @param _id initial attribute declared by type <code>AbstractConfiguratorSetting</code> at extension <code>catalog</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _qualifier initial attribute declared by type <code>AbstractConfiguratorSetting</code> at extension <code>catalog</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public CPQConfiguratorSettingsModel(final CatalogVersionModel _catalogVersion, final ConfigurationCategoryModel _configurationCategory, final ConfiguratorType _configuratorType, final String _id, final ItemModel _owner, final String _qualifier)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setConfigurationCategory(_configurationCategory);
		setConfiguratorType(_configuratorType);
		setId(_id);
		setOwner(_owner);
		setQualifier(_qualifier);
	}
	
	
}
