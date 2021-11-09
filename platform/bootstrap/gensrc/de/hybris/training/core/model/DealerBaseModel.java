/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 08-Nov-2021, 4:51:25 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.training.core.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.training.core.model.DriverBaseModel;
import de.hybris.training.core.model.VehicleBaseModel;
import java.util.Locale;
import java.util.Set;

/**
 * Generated model class for type DealerBase first defined at extension trainingcore.
 */
@SuppressWarnings("all")
public class DealerBaseModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "DealerBase";
	
	/**<i>Generated relation code constant for relation <code>DriverBase2DealerBase</code> defining source attribute <code>driver</code> in extension <code>trainingcore</code>.</i>*/
	public static final String _DRIVERBASE2DEALERBASE = "DriverBase2DealerBase";
	
	/**<i>Generated relation code constant for relation <code>VehicleBase2DealerBase</code> defining source attribute <code>vehicle</code> in extension <code>trainingcore</code>.</i>*/
	public static final String _VEHICLEBASE2DEALERBASE = "VehicleBase2DealerBase";
	
	/** <i>Generated constant</i> - Attribute key of <code>DealerBase.address</code> attribute defined at extension <code>trainingcore</code>. */
	public static final String ADDRESS = "address";
	
	/** <i>Generated constant</i> - Attribute key of <code>DealerBase.localizeName</code> attribute defined at extension <code>trainingcore</code>. */
	public static final String LOCALIZENAME = "localizeName";
	
	/** <i>Generated constant</i> - Attribute key of <code>DealerBase.uniqueId</code> attribute defined at extension <code>trainingcore</code>. */
	public static final String UNIQUEID = "uniqueId";
	
	/** <i>Generated constant</i> - Attribute key of <code>DealerBase.driver</code> attribute defined at extension <code>trainingcore</code>. */
	public static final String DRIVER = "driver";
	
	/** <i>Generated constant</i> - Attribute key of <code>DealerBase.vehicle</code> attribute defined at extension <code>trainingcore</code>. */
	public static final String VEHICLE = "vehicle";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public DealerBaseModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public DealerBaseModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public DealerBaseModel(final ItemModel _owner)
	{
		super();
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DealerBase.address</code> attribute defined at extension <code>trainingcore</code>. 
	 * @return the address - dealer address
	 */
	@Accessor(qualifier = "address", type = Accessor.Type.GETTER)
	public String getAddress()
	{
		return getAddress(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>DealerBase.address</code> attribute defined at extension <code>trainingcore</code>. 
	 * @param loc the value localization key 
	 * @return the address - dealer address
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "address", type = Accessor.Type.GETTER)
	public String getAddress(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(ADDRESS, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DealerBase.driver</code> attribute defined at extension <code>trainingcore</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the driver - Driver
	 */
	@Accessor(qualifier = "driver", type = Accessor.Type.GETTER)
	public Set<DriverBaseModel> getDriver()
	{
		return getPersistenceContext().getPropertyValue(DRIVER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DealerBase.localizeName</code> attribute defined at extension <code>trainingcore</code>. 
	 * @return the localizeName - name of the dealer
	 */
	@Accessor(qualifier = "localizeName", type = Accessor.Type.GETTER)
	public String getLocalizeName()
	{
		return getLocalizeName(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>DealerBase.localizeName</code> attribute defined at extension <code>trainingcore</code>. 
	 * @param loc the value localization key 
	 * @return the localizeName - name of the dealer
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "localizeName", type = Accessor.Type.GETTER)
	public String getLocalizeName(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(LOCALIZENAME, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DealerBase.uniqueId</code> attribute defined at extension <code>trainingcore</code>. 
	 * @return the uniqueId - dealer of unique id
	 */
	@Accessor(qualifier = "uniqueId", type = Accessor.Type.GETTER)
	public String getUniqueId()
	{
		return getPersistenceContext().getPropertyValue(UNIQUEID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DealerBase.vehicle</code> attribute defined at extension <code>trainingcore</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the vehicle - Vehicle
	 */
	@Accessor(qualifier = "vehicle", type = Accessor.Type.GETTER)
	public Set<VehicleBaseModel> getVehicle()
	{
		return getPersistenceContext().getPropertyValue(VEHICLE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DealerBase.address</code> attribute defined at extension <code>trainingcore</code>. 
	 *  
	 * @param value the address - dealer address
	 */
	@Accessor(qualifier = "address", type = Accessor.Type.SETTER)
	public void setAddress(final String value)
	{
		setAddress(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>DealerBase.address</code> attribute defined at extension <code>trainingcore</code>. 
	 *  
	 * @param value the address - dealer address
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "address", type = Accessor.Type.SETTER)
	public void setAddress(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(ADDRESS, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DealerBase.driver</code> attribute defined at extension <code>trainingcore</code>. 
	 *  
	 * @param value the driver - Driver
	 */
	@Accessor(qualifier = "driver", type = Accessor.Type.SETTER)
	public void setDriver(final Set<DriverBaseModel> value)
	{
		getPersistenceContext().setPropertyValue(DRIVER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DealerBase.localizeName</code> attribute defined at extension <code>trainingcore</code>. 
	 *  
	 * @param value the localizeName - name of the dealer
	 */
	@Accessor(qualifier = "localizeName", type = Accessor.Type.SETTER)
	public void setLocalizeName(final String value)
	{
		setLocalizeName(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>DealerBase.localizeName</code> attribute defined at extension <code>trainingcore</code>. 
	 *  
	 * @param value the localizeName - name of the dealer
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "localizeName", type = Accessor.Type.SETTER)
	public void setLocalizeName(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(LOCALIZENAME, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DealerBase.uniqueId</code> attribute defined at extension <code>trainingcore</code>. 
	 *  
	 * @param value the uniqueId - dealer of unique id
	 */
	@Accessor(qualifier = "uniqueId", type = Accessor.Type.SETTER)
	public void setUniqueId(final String value)
	{
		getPersistenceContext().setPropertyValue(UNIQUEID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DealerBase.vehicle</code> attribute defined at extension <code>trainingcore</code>. 
	 *  
	 * @param value the vehicle - Vehicle
	 */
	@Accessor(qualifier = "vehicle", type = Accessor.Type.SETTER)
	public void setVehicle(final Set<VehicleBaseModel> value)
	{
		getPersistenceContext().setPropertyValue(VEHICLE, value);
	}
	
}
