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
import de.hybris.training.core.model.DealerBaseModel;
import de.hybris.training.core.model.VehicleBaseModel;
import java.util.Locale;
import java.util.Set;

/**
 * Generated model class for type DriverBase first defined at extension trainingcore.
 */
@SuppressWarnings("all")
public class DriverBaseModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "DriverBase";
	
	/** <i>Generated constant</i> - Attribute key of <code>DriverBase.nameOfDriver</code> attribute defined at extension <code>trainingcore</code>. */
	public static final String NAMEOFDRIVER = "nameOfDriver";
	
	/** <i>Generated constant</i> - Attribute key of <code>DriverBase.driverId</code> attribute defined at extension <code>trainingcore</code>. */
	public static final String DRIVERID = "driverId";
	
	/** <i>Generated constant</i> - Attribute key of <code>DriverBase.experience</code> attribute defined at extension <code>trainingcore</code>. */
	public static final String EXPERIENCE = "experience";
	
	/** <i>Generated constant</i> - Attribute key of <code>DriverBase.dealer</code> attribute defined at extension <code>trainingcore</code>. */
	public static final String DEALER = "dealer";
	
	/** <i>Generated constant</i> - Attribute key of <code>DriverBase.vehicle</code> attribute defined at extension <code>trainingcore</code>. */
	public static final String VEHICLE = "vehicle";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public DriverBaseModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public DriverBaseModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _driverId initial attribute declared by type <code>DriverBase</code> at extension <code>trainingcore</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public DriverBaseModel(final String _driverId)
	{
		super();
		setDriverId(_driverId);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _driverId initial attribute declared by type <code>DriverBase</code> at extension <code>trainingcore</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public DriverBaseModel(final String _driverId, final ItemModel _owner)
	{
		super();
		setDriverId(_driverId);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DriverBase.dealer</code> attribute defined at extension <code>trainingcore</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the dealer - Dealer
	 */
	@Accessor(qualifier = "dealer", type = Accessor.Type.GETTER)
	public Set<DealerBaseModel> getDealer()
	{
		return getPersistenceContext().getPropertyValue(DEALER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DriverBase.driverId</code> attribute defined at extension <code>trainingcore</code>. 
	 * @return the driverId - id of driver
	 */
	@Accessor(qualifier = "driverId", type = Accessor.Type.GETTER)
	public String getDriverId()
	{
		return getPersistenceContext().getPropertyValue(DRIVERID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DriverBase.experience</code> attribute defined at extension <code>trainingcore</code>. 
	 * @return the experience - experience in years
	 */
	@Accessor(qualifier = "experience", type = Accessor.Type.GETTER)
	public String getExperience()
	{
		return getPersistenceContext().getPropertyValue(EXPERIENCE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DriverBase.nameOfDriver</code> attribute defined at extension <code>trainingcore</code>. 
	 * @return the nameOfDriver - driver name
	 */
	@Accessor(qualifier = "nameOfDriver", type = Accessor.Type.GETTER)
	public String getNameOfDriver()
	{
		return getNameOfDriver(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>DriverBase.nameOfDriver</code> attribute defined at extension <code>trainingcore</code>. 
	 * @param loc the value localization key 
	 * @return the nameOfDriver - driver name
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "nameOfDriver", type = Accessor.Type.GETTER)
	public String getNameOfDriver(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(NAMEOFDRIVER, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DriverBase.vehicle</code> attribute defined at extension <code>trainingcore</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the vehicle - Vehicle
	 */
	@Accessor(qualifier = "vehicle", type = Accessor.Type.GETTER)
	public Set<VehicleBaseModel> getVehicle()
	{
		return getPersistenceContext().getPropertyValue(VEHICLE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DriverBase.dealer</code> attribute defined at extension <code>trainingcore</code>. 
	 *  
	 * @param value the dealer - Dealer
	 */
	@Accessor(qualifier = "dealer", type = Accessor.Type.SETTER)
	public void setDealer(final Set<DealerBaseModel> value)
	{
		getPersistenceContext().setPropertyValue(DEALER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DriverBase.driverId</code> attribute defined at extension <code>trainingcore</code>. 
	 *  
	 * @param value the driverId - id of driver
	 */
	@Accessor(qualifier = "driverId", type = Accessor.Type.SETTER)
	public void setDriverId(final String value)
	{
		getPersistenceContext().setPropertyValue(DRIVERID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DriverBase.experience</code> attribute defined at extension <code>trainingcore</code>. 
	 *  
	 * @param value the experience - experience in years
	 */
	@Accessor(qualifier = "experience", type = Accessor.Type.SETTER)
	public void setExperience(final String value)
	{
		getPersistenceContext().setPropertyValue(EXPERIENCE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DriverBase.nameOfDriver</code> attribute defined at extension <code>trainingcore</code>. 
	 *  
	 * @param value the nameOfDriver - driver name
	 */
	@Accessor(qualifier = "nameOfDriver", type = Accessor.Type.SETTER)
	public void setNameOfDriver(final String value)
	{
		setNameOfDriver(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>DriverBase.nameOfDriver</code> attribute defined at extension <code>trainingcore</code>. 
	 *  
	 * @param value the nameOfDriver - driver name
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "nameOfDriver", type = Accessor.Type.SETTER)
	public void setNameOfDriver(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(NAMEOFDRIVER, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DriverBase.vehicle</code> attribute defined at extension <code>trainingcore</code>. 
	 *  
	 * @param value the vehicle - Vehicle
	 */
	@Accessor(qualifier = "vehicle", type = Accessor.Type.SETTER)
	public void setVehicle(final Set<VehicleBaseModel> value)
	{
		getPersistenceContext().setPropertyValue(VEHICLE, value);
	}
	
}
