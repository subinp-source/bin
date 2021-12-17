/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 15-Dec-2021, 3:07:44 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.training.core.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.training.core.model.DealerBaseModel;
import de.hybris.training.core.model.DriverBaseModel;
import java.util.Locale;
import java.util.Set;

/**
 * Generated model class for type VehicleBase first defined at extension trainingcore.
 */
@SuppressWarnings("all")
public class VehicleBaseModel extends ProductModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "VehicleBase";
	
	/**<i>Generated relation code constant for relation <code>DriverBase2VehicleBase</code> defining source attribute <code>driver</code> in extension <code>trainingcore</code>.</i>*/
	public static final String _DRIVERBASE2VEHICLEBASE = "DriverBase2VehicleBase";
	
	/** <i>Generated constant</i> - Attribute key of <code>VehicleBase.vehicleRegistrationNumber</code> attribute defined at extension <code>trainingcore</code>. */
	public static final String VEHICLEREGISTRATIONNUMBER = "vehicleRegistrationNumber";
	
	/** <i>Generated constant</i> - Attribute key of <code>VehicleBase.vehicleId</code> attribute defined at extension <code>trainingcore</code>. */
	public static final String VEHICLEID = "vehicleId";
	
	/** <i>Generated constant</i> - Attribute key of <code>VehicleBase.vehicleDescription</code> attribute defined at extension <code>trainingcore</code>. */
	public static final String VEHICLEDESCRIPTION = "vehicleDescription";
	
	/** <i>Generated constant</i> - Attribute key of <code>VehicleBase.vehicleReview</code> attribute defined at extension <code>trainingcore</code>. */
	public static final String VEHICLEREVIEW = "vehicleReview";
	
	/** <i>Generated constant</i> - Attribute key of <code>VehicleBase.dealer</code> attribute defined at extension <code>trainingcore</code>. */
	public static final String DEALER = "dealer";
	
	/** <i>Generated constant</i> - Attribute key of <code>VehicleBase.driver</code> attribute defined at extension <code>trainingcore</code>. */
	public static final String DRIVER = "driver";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public VehicleBaseModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public VehicleBaseModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>Product</code> at extension <code>catalog</code>
	 * @param _code initial attribute declared by type <code>Product</code> at extension <code>core</code>
	 * @param _vehicleId initial attribute declared by type <code>VehicleBase</code> at extension <code>trainingcore</code>
	 * @param _vehicleRegistrationNumber initial attribute declared by type <code>VehicleBase</code> at extension <code>trainingcore</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public VehicleBaseModel(final CatalogVersionModel _catalogVersion, final String _code, final String _vehicleId, final String _vehicleRegistrationNumber)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setCode(_code);
		setVehicleId(_vehicleId);
		setVehicleRegistrationNumber(_vehicleRegistrationNumber);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>Product</code> at extension <code>catalog</code>
	 * @param _code initial attribute declared by type <code>Product</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _vehicleId initial attribute declared by type <code>VehicleBase</code> at extension <code>trainingcore</code>
	 * @param _vehicleRegistrationNumber initial attribute declared by type <code>VehicleBase</code> at extension <code>trainingcore</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public VehicleBaseModel(final CatalogVersionModel _catalogVersion, final String _code, final ItemModel _owner, final String _vehicleId, final String _vehicleRegistrationNumber)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setCode(_code);
		setOwner(_owner);
		setVehicleId(_vehicleId);
		setVehicleRegistrationNumber(_vehicleRegistrationNumber);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>VehicleBase.dealer</code> attribute defined at extension <code>trainingcore</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the dealer - Dealer
	 */
	@Accessor(qualifier = "dealer", type = Accessor.Type.GETTER)
	public Set<DealerBaseModel> getDealer()
	{
		return getPersistenceContext().getPropertyValue(DEALER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>VehicleBase.driver</code> attribute defined at extension <code>trainingcore</code>. 
	 * @return the driver - Driver
	 */
	@Accessor(qualifier = "driver", type = Accessor.Type.GETTER)
	public DriverBaseModel getDriver()
	{
		return getPersistenceContext().getPropertyValue(DRIVER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>VehicleBase.vehicleDescription</code> attribute defined at extension <code>trainingcore</code>. 
	 * @return the vehicleDescription - vehicle description
	 */
	@Accessor(qualifier = "vehicleDescription", type = Accessor.Type.GETTER)
	public String getVehicleDescription()
	{
		return getVehicleDescription(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>VehicleBase.vehicleDescription</code> attribute defined at extension <code>trainingcore</code>. 
	 * @param loc the value localization key 
	 * @return the vehicleDescription - vehicle description
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "vehicleDescription", type = Accessor.Type.GETTER)
	public String getVehicleDescription(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(VEHICLEDESCRIPTION, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>VehicleBase.vehicleId</code> attribute defined at extension <code>trainingcore</code>. 
	 * @return the vehicleId - vehicle Id
	 */
	@Accessor(qualifier = "vehicleId", type = Accessor.Type.GETTER)
	public String getVehicleId()
	{
		return getPersistenceContext().getPropertyValue(VEHICLEID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>VehicleBase.vehicleRegistrationNumber</code> attribute defined at extension <code>trainingcore</code>. 
	 * @return the vehicleRegistrationNumber - vehicle number
	 */
	@Accessor(qualifier = "vehicleRegistrationNumber", type = Accessor.Type.GETTER)
	public String getVehicleRegistrationNumber()
	{
		return getPersistenceContext().getPropertyValue(VEHICLEREGISTRATIONNUMBER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>VehicleBase.vehicleReview</code> attribute defined at extension <code>trainingcore</code>. 
	 * @return the vehicleReview - vehicle review
	 */
	@Accessor(qualifier = "vehicleReview", type = Accessor.Type.GETTER)
	public String getVehicleReview()
	{
		return getVehicleReview(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>VehicleBase.vehicleReview</code> attribute defined at extension <code>trainingcore</code>. 
	 * @param loc the value localization key 
	 * @return the vehicleReview - vehicle review
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "vehicleReview", type = Accessor.Type.GETTER)
	public String getVehicleReview(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(VEHICLEREVIEW, loc);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>VehicleBase.dealer</code> attribute defined at extension <code>trainingcore</code>. 
	 *  
	 * @param value the dealer - Dealer
	 */
	@Accessor(qualifier = "dealer", type = Accessor.Type.SETTER)
	public void setDealer(final Set<DealerBaseModel> value)
	{
		getPersistenceContext().setPropertyValue(DEALER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>VehicleBase.driver</code> attribute defined at extension <code>trainingcore</code>. 
	 *  
	 * @param value the driver - Driver
	 */
	@Accessor(qualifier = "driver", type = Accessor.Type.SETTER)
	public void setDriver(final DriverBaseModel value)
	{
		getPersistenceContext().setPropertyValue(DRIVER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>VehicleBase.vehicleDescription</code> attribute defined at extension <code>trainingcore</code>. 
	 *  
	 * @param value the vehicleDescription - vehicle description
	 */
	@Accessor(qualifier = "vehicleDescription", type = Accessor.Type.SETTER)
	public void setVehicleDescription(final String value)
	{
		setVehicleDescription(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>VehicleBase.vehicleDescription</code> attribute defined at extension <code>trainingcore</code>. 
	 *  
	 * @param value the vehicleDescription - vehicle description
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "vehicleDescription", type = Accessor.Type.SETTER)
	public void setVehicleDescription(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(VEHICLEDESCRIPTION, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>VehicleBase.vehicleId</code> attribute defined at extension <code>trainingcore</code>. 
	 *  
	 * @param value the vehicleId - vehicle Id
	 */
	@Accessor(qualifier = "vehicleId", type = Accessor.Type.SETTER)
	public void setVehicleId(final String value)
	{
		getPersistenceContext().setPropertyValue(VEHICLEID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>VehicleBase.vehicleRegistrationNumber</code> attribute defined at extension <code>trainingcore</code>. 
	 *  
	 * @param value the vehicleRegistrationNumber - vehicle number
	 */
	@Accessor(qualifier = "vehicleRegistrationNumber", type = Accessor.Type.SETTER)
	public void setVehicleRegistrationNumber(final String value)
	{
		getPersistenceContext().setPropertyValue(VEHICLEREGISTRATIONNUMBER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>VehicleBase.vehicleReview</code> attribute defined at extension <code>trainingcore</code>. 
	 *  
	 * @param value the vehicleReview - vehicle review
	 */
	@Accessor(qualifier = "vehicleReview", type = Accessor.Type.SETTER)
	public void setVehicleReview(final String value)
	{
		setVehicleReview(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>VehicleBase.vehicleReview</code> attribute defined at extension <code>trainingcore</code>. 
	 *  
	 * @param value the vehicleReview - vehicle review
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "vehicleReview", type = Accessor.Type.SETTER)
	public void setVehicleReview(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(VEHICLEREVIEW, loc, value);
	}
	
}
