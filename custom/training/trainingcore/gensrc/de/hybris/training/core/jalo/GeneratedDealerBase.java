/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 11-Dec-2021, 12:32:58 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.training.core.jalo;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.Utilities;
import de.hybris.training.core.constants.TrainingCoreConstants;
import de.hybris.training.core.jalo.DriverBase;
import de.hybris.training.core.jalo.VehicleBase;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Generated class for type {@link de.hybris.training.core.jalo.DealerBase DealerBase}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedDealerBase extends GenericItem
{
	/** Qualifier of the <code>DealerBase.address</code> attribute **/
	public static final String ADDRESS = "address";
	/** Qualifier of the <code>DealerBase.localizeName</code> attribute **/
	public static final String LOCALIZENAME = "localizeName";
	/** Qualifier of the <code>DealerBase.uniqueId</code> attribute **/
	public static final String UNIQUEID = "uniqueId";
	/** Qualifier of the <code>DealerBase.driver</code> attribute **/
	public static final String DRIVER = "driver";
	/** Relation ordering override parameter constants for DriverBase2DealerBase from ((trainingcore))*/
	protected static String DRIVERBASE2DEALERBASE_SRC_ORDERED = "relation.DriverBase2DealerBase.source.ordered";
	protected static String DRIVERBASE2DEALERBASE_TGT_ORDERED = "relation.DriverBase2DealerBase.target.ordered";
	/** Relation disable markmodifed parameter constants for DriverBase2DealerBase from ((trainingcore))*/
	protected static String DRIVERBASE2DEALERBASE_MARKMODIFIED = "relation.DriverBase2DealerBase.markmodified";
	/** Qualifier of the <code>DealerBase.vehicle</code> attribute **/
	public static final String VEHICLE = "vehicle";
	/** Relation ordering override parameter constants for VehicleBase2DealerBase from ((trainingcore))*/
	protected static String VEHICLEBASE2DEALERBASE_SRC_ORDERED = "relation.VehicleBase2DealerBase.source.ordered";
	protected static String VEHICLEBASE2DEALERBASE_TGT_ORDERED = "relation.VehicleBase2DealerBase.target.ordered";
	/** Relation disable markmodifed parameter constants for VehicleBase2DealerBase from ((trainingcore))*/
	protected static String VEHICLEBASE2DEALERBASE_MARKMODIFIED = "relation.VehicleBase2DealerBase.markmodified";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(ADDRESS, AttributeMode.INITIAL);
		tmp.put(LOCALIZENAME, AttributeMode.INITIAL);
		tmp.put(UNIQUEID, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DealerBase.address</code> attribute.
	 * @return the address - dealer address
	 */
	public String getAddress(final SessionContext ctx)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedDealerBase.getAddress requires a session language", 0 );
		}
		return (String)getLocalizedProperty( ctx, ADDRESS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DealerBase.address</code> attribute.
	 * @return the address - dealer address
	 */
	public String getAddress()
	{
		return getAddress( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DealerBase.address</code> attribute. 
	 * @return the localized address - dealer address
	 */
	public Map<Language,String> getAllAddress(final SessionContext ctx)
	{
		return (Map<Language,String>)getAllLocalizedProperties(ctx,ADDRESS,C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DealerBase.address</code> attribute. 
	 * @return the localized address - dealer address
	 */
	public Map<Language,String> getAllAddress()
	{
		return getAllAddress( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>DealerBase.address</code> attribute. 
	 * @param value the address - dealer address
	 */
	public void setAddress(final SessionContext ctx, final String value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		if( ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedDealerBase.setAddress requires a session language", 0 );
		}
		setLocalizedProperty(ctx, ADDRESS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>DealerBase.address</code> attribute. 
	 * @param value the address - dealer address
	 */
	public void setAddress(final String value)
	{
		setAddress( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>DealerBase.address</code> attribute. 
	 * @param value the address - dealer address
	 */
	public void setAllAddress(final SessionContext ctx, final Map<Language,String> value)
	{
		setAllLocalizedProperties(ctx,ADDRESS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>DealerBase.address</code> attribute. 
	 * @param value the address - dealer address
	 */
	public void setAllAddress(final Map<Language,String> value)
	{
		setAllAddress( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DealerBase.driver</code> attribute.
	 * @return the driver - Driver
	 */
	public Set<DriverBase> getDriver(final SessionContext ctx)
	{
		final List<DriverBase> items = getLinkedItems( 
			ctx,
			false,
			TrainingCoreConstants.Relations.DRIVERBASE2DEALERBASE,
			"DriverBase",
			null,
			false,
			false
		);
		return new LinkedHashSet<DriverBase>(items);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DealerBase.driver</code> attribute.
	 * @return the driver - Driver
	 */
	public Set<DriverBase> getDriver()
	{
		return getDriver( getSession().getSessionContext() );
	}
	
	public long getDriverCount(final SessionContext ctx)
	{
		return getLinkedItemsCount(
			ctx,
			false,
			TrainingCoreConstants.Relations.DRIVERBASE2DEALERBASE,
			"DriverBase",
			null
		);
	}
	
	public long getDriverCount()
	{
		return getDriverCount( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>DealerBase.driver</code> attribute. 
	 * @param value the driver - Driver
	 */
	public void setDriver(final SessionContext ctx, final Set<DriverBase> value)
	{
		setLinkedItems( 
			ctx,
			false,
			TrainingCoreConstants.Relations.DRIVERBASE2DEALERBASE,
			null,
			value,
			false,
			false,
			Utilities.getMarkModifiedOverride(DRIVERBASE2DEALERBASE_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>DealerBase.driver</code> attribute. 
	 * @param value the driver - Driver
	 */
	public void setDriver(final Set<DriverBase> value)
	{
		setDriver( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to driver. 
	 * @param value the item to add to driver - Driver
	 */
	public void addToDriver(final SessionContext ctx, final DriverBase value)
	{
		addLinkedItems( 
			ctx,
			false,
			TrainingCoreConstants.Relations.DRIVERBASE2DEALERBASE,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(DRIVERBASE2DEALERBASE_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to driver. 
	 * @param value the item to add to driver - Driver
	 */
	public void addToDriver(final DriverBase value)
	{
		addToDriver( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from driver. 
	 * @param value the item to remove from driver - Driver
	 */
	public void removeFromDriver(final SessionContext ctx, final DriverBase value)
	{
		removeLinkedItems( 
			ctx,
			false,
			TrainingCoreConstants.Relations.DRIVERBASE2DEALERBASE,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(DRIVERBASE2DEALERBASE_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from driver. 
	 * @param value the item to remove from driver - Driver
	 */
	public void removeFromDriver(final DriverBase value)
	{
		removeFromDriver( getSession().getSessionContext(), value );
	}
	
	/**
	 * @deprecated since 2011, use {@link Utilities#getMarkModifiedOverride(de.hybris.platform.jalo.type.RelationType)
	 */
	@Override
	@Deprecated(since = "2105", forRemoval = true)
	public boolean isMarkModifiedDisabled(final Item referencedItem)
	{
		ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("DriverBase");
		if(relationSecondEnd0.isAssignableFrom(referencedItem.getComposedType()))
		{
			return Utilities.getMarkModifiedOverride(DRIVERBASE2DEALERBASE_MARKMODIFIED);
		}
		ComposedType relationSecondEnd1 = TypeManager.getInstance().getComposedType("VehicleBase");
		if(relationSecondEnd1.isAssignableFrom(referencedItem.getComposedType()))
		{
			return Utilities.getMarkModifiedOverride(VEHICLEBASE2DEALERBASE_MARKMODIFIED);
		}
		return true;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DealerBase.localizeName</code> attribute.
	 * @return the localizeName - name of the dealer
	 */
	public String getLocalizeName(final SessionContext ctx)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedDealerBase.getLocalizeName requires a session language", 0 );
		}
		return (String)getLocalizedProperty( ctx, LOCALIZENAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DealerBase.localizeName</code> attribute.
	 * @return the localizeName - name of the dealer
	 */
	public String getLocalizeName()
	{
		return getLocalizeName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DealerBase.localizeName</code> attribute. 
	 * @return the localized localizeName - name of the dealer
	 */
	public Map<Language,String> getAllLocalizeName(final SessionContext ctx)
	{
		return (Map<Language,String>)getAllLocalizedProperties(ctx,LOCALIZENAME,C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DealerBase.localizeName</code> attribute. 
	 * @return the localized localizeName - name of the dealer
	 */
	public Map<Language,String> getAllLocalizeName()
	{
		return getAllLocalizeName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>DealerBase.localizeName</code> attribute. 
	 * @param value the localizeName - name of the dealer
	 */
	public void setLocalizeName(final SessionContext ctx, final String value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		if( ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedDealerBase.setLocalizeName requires a session language", 0 );
		}
		setLocalizedProperty(ctx, LOCALIZENAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>DealerBase.localizeName</code> attribute. 
	 * @param value the localizeName - name of the dealer
	 */
	public void setLocalizeName(final String value)
	{
		setLocalizeName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>DealerBase.localizeName</code> attribute. 
	 * @param value the localizeName - name of the dealer
	 */
	public void setAllLocalizeName(final SessionContext ctx, final Map<Language,String> value)
	{
		setAllLocalizedProperties(ctx,LOCALIZENAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>DealerBase.localizeName</code> attribute. 
	 * @param value the localizeName - name of the dealer
	 */
	public void setAllLocalizeName(final Map<Language,String> value)
	{
		setAllLocalizeName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DealerBase.uniqueId</code> attribute.
	 * @return the uniqueId - dealer of unique id
	 */
	public String getUniqueId(final SessionContext ctx)
	{
		return (String)getProperty( ctx, UNIQUEID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DealerBase.uniqueId</code> attribute.
	 * @return the uniqueId - dealer of unique id
	 */
	public String getUniqueId()
	{
		return getUniqueId( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>DealerBase.uniqueId</code> attribute. 
	 * @param value the uniqueId - dealer of unique id
	 */
	public void setUniqueId(final SessionContext ctx, final String value)
	{
		setProperty(ctx, UNIQUEID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>DealerBase.uniqueId</code> attribute. 
	 * @param value the uniqueId - dealer of unique id
	 */
	public void setUniqueId(final String value)
	{
		setUniqueId( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DealerBase.vehicle</code> attribute.
	 * @return the vehicle - Vehicle
	 */
	public Set<VehicleBase> getVehicle(final SessionContext ctx)
	{
		final List<VehicleBase> items = getLinkedItems( 
			ctx,
			false,
			TrainingCoreConstants.Relations.VEHICLEBASE2DEALERBASE,
			"VehicleBase",
			null,
			false,
			false
		);
		return new LinkedHashSet<VehicleBase>(items);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DealerBase.vehicle</code> attribute.
	 * @return the vehicle - Vehicle
	 */
	public Set<VehicleBase> getVehicle()
	{
		return getVehicle( getSession().getSessionContext() );
	}
	
	public long getVehicleCount(final SessionContext ctx)
	{
		return getLinkedItemsCount(
			ctx,
			false,
			TrainingCoreConstants.Relations.VEHICLEBASE2DEALERBASE,
			"VehicleBase",
			null
		);
	}
	
	public long getVehicleCount()
	{
		return getVehicleCount( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>DealerBase.vehicle</code> attribute. 
	 * @param value the vehicle - Vehicle
	 */
	public void setVehicle(final SessionContext ctx, final Set<VehicleBase> value)
	{
		setLinkedItems( 
			ctx,
			false,
			TrainingCoreConstants.Relations.VEHICLEBASE2DEALERBASE,
			null,
			value,
			false,
			false,
			Utilities.getMarkModifiedOverride(VEHICLEBASE2DEALERBASE_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>DealerBase.vehicle</code> attribute. 
	 * @param value the vehicle - Vehicle
	 */
	public void setVehicle(final Set<VehicleBase> value)
	{
		setVehicle( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to vehicle. 
	 * @param value the item to add to vehicle - Vehicle
	 */
	public void addToVehicle(final SessionContext ctx, final VehicleBase value)
	{
		addLinkedItems( 
			ctx,
			false,
			TrainingCoreConstants.Relations.VEHICLEBASE2DEALERBASE,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(VEHICLEBASE2DEALERBASE_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to vehicle. 
	 * @param value the item to add to vehicle - Vehicle
	 */
	public void addToVehicle(final VehicleBase value)
	{
		addToVehicle( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from vehicle. 
	 * @param value the item to remove from vehicle - Vehicle
	 */
	public void removeFromVehicle(final SessionContext ctx, final VehicleBase value)
	{
		removeLinkedItems( 
			ctx,
			false,
			TrainingCoreConstants.Relations.VEHICLEBASE2DEALERBASE,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(VEHICLEBASE2DEALERBASE_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from vehicle. 
	 * @param value the item to remove from vehicle - Vehicle
	 */
	public void removeFromVehicle(final VehicleBase value)
	{
		removeFromVehicle( getSession().getSessionContext(), value );
	}
	
}
