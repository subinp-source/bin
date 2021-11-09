/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 08-Nov-2021, 4:51:25 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.productconfig.services.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.enums.ConfiguratorType;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.order.model.AbstractOrderEntryProductInfoModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type CPQOrderEntryProductInfo first defined at extension sapproductconfigservices.
 * <p>
 * CPQ configuration result for abstract order entry.
 */
@SuppressWarnings("all")
public class CPQOrderEntryProductInfoModel extends AbstractOrderEntryProductInfoModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CPQOrderEntryProductInfo";
	
	/** <i>Generated constant</i> - Attribute key of <code>CPQOrderEntryProductInfo.cpqCharacteristicName</code> attribute defined at extension <code>sapproductconfigservices</code>. */
	public static final String CPQCHARACTERISTICNAME = "cpqCharacteristicName";
	
	/** <i>Generated constant</i> - Attribute key of <code>CPQOrderEntryProductInfo.cpqCharacteristicAssignedValues</code> attribute defined at extension <code>sapproductconfigservices</code>. */
	public static final String CPQCHARACTERISTICASSIGNEDVALUES = "cpqCharacteristicAssignedValues";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CPQOrderEntryProductInfoModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CPQOrderEntryProductInfoModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _configuratorType initial attribute declared by type <code>AbstractOrderEntryProductInfo</code> at extension <code>catalog</code>
	 * @param _orderEntry initial attribute declared by type <code>AbstractOrderEntryProductInfo</code> at extension <code>catalog</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public CPQOrderEntryProductInfoModel(final ConfiguratorType _configuratorType, final AbstractOrderEntryModel _orderEntry)
	{
		super();
		setConfiguratorType(_configuratorType);
		setOrderEntry(_orderEntry);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _configuratorType initial attribute declared by type <code>AbstractOrderEntryProductInfo</code> at extension <code>catalog</code>
	 * @param _orderEntry initial attribute declared by type <code>AbstractOrderEntryProductInfo</code> at extension <code>catalog</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public CPQOrderEntryProductInfoModel(final ConfiguratorType _configuratorType, final AbstractOrderEntryModel _orderEntry, final ItemModel _owner)
	{
		super();
		setConfiguratorType(_configuratorType);
		setOrderEntry(_orderEntry);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CPQOrderEntryProductInfo.cpqCharacteristicAssignedValues</code> attribute defined at extension <code>sapproductconfigservices</code>. 
	 * @return the cpqCharacteristicAssignedValues - Language independent name of the characteristic assigned values for the inline configuration display
	 */
	@Accessor(qualifier = "cpqCharacteristicAssignedValues", type = Accessor.Type.GETTER)
	public String getCpqCharacteristicAssignedValues()
	{
		return getPersistenceContext().getPropertyValue(CPQCHARACTERISTICASSIGNEDVALUES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CPQOrderEntryProductInfo.cpqCharacteristicName</code> attribute defined at extension <code>sapproductconfigservices</code>. 
	 * @return the cpqCharacteristicName - Language independent name of the characteristic for the inline configuration display
	 */
	@Accessor(qualifier = "cpqCharacteristicName", type = Accessor.Type.GETTER)
	public String getCpqCharacteristicName()
	{
		return getPersistenceContext().getPropertyValue(CPQCHARACTERISTICNAME);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CPQOrderEntryProductInfo.cpqCharacteristicAssignedValues</code> attribute defined at extension <code>sapproductconfigservices</code>. 
	 *  
	 * @param value the cpqCharacteristicAssignedValues - Language independent name of the characteristic assigned values for the inline configuration display
	 */
	@Accessor(qualifier = "cpqCharacteristicAssignedValues", type = Accessor.Type.SETTER)
	public void setCpqCharacteristicAssignedValues(final String value)
	{
		getPersistenceContext().setPropertyValue(CPQCHARACTERISTICASSIGNEDVALUES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CPQOrderEntryProductInfo.cpqCharacteristicName</code> attribute defined at extension <code>sapproductconfigservices</code>. 
	 *  
	 * @param value the cpqCharacteristicName - Language independent name of the characteristic for the inline configuration display
	 */
	@Accessor(qualifier = "cpqCharacteristicName", type = Accessor.Type.SETTER)
	public void setCpqCharacteristicName(final String value)
	{
		getPersistenceContext().setPropertyValue(CPQCHARACTERISTICNAME, value);
	}
	
}
