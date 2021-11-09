/*
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.productconfig.services.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum ProductConfigurationPersistenceCleanUpMode declared at extension sapproductconfigservices.
 * <p/>
 * This enumeration defines the different modes for performing the product configuration persistence related clean up.
 */
public enum ProductConfigurationPersistenceCleanUpMode implements HybrisEnumValue
{
	/**
	 * Generated enum value for ProductConfigurationPersistenceCleanUpMode.All declared at extension sapproductconfigservices.
	 */
	ALL("All"),
	/**
	 * Generated enum value for ProductConfigurationPersistenceCleanUpMode.OnlyProductRelated declared at extension sapproductconfigservices.
	 */
	ONLYPRODUCTRELATED("OnlyProductRelated"),
	/**
	 * Generated enum value for ProductConfigurationPersistenceCleanUpMode.OnlyOrphaned declared at extension sapproductconfigservices.
	 */
	ONLYORPHANED("OnlyOrphaned");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "ProductConfigurationPersistenceCleanUpMode";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "ProductConfigurationPersistenceCleanUpMode";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private ProductConfigurationPersistenceCleanUpMode(final String code)
	{
		this.code = code.intern();
	}
	
	
	/**
	 * Gets the code of this enum value.
	 *  
	 * @return code of value
	 */
	@Override
	public String getCode()
	{
		return this.code;
	}
	
	/**
	 * Gets the type this enum value belongs to.
	 *  
	 * @return code of type
	 */
	@Override
	public String getType()
	{
		return SIMPLE_CLASSNAME;
	}
	
}
