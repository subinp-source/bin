/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 13-Dec-2021, 3:58:17 PM                     ---
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
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.platform.util.Utilities;
import de.hybris.training.core.constants.TrainingCoreConstants;
import de.hybris.training.core.jalo.DealerBase;
import de.hybris.training.core.jalo.VehicleBase;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Generated class for type {@link de.hybris.training.core.jalo.DriverBase DriverBase}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedDriverBase extends GenericItem
{
	/** Qualifier of the <code>DriverBase.nameOfDriver</code> attribute **/
	public static final String NAMEOFDRIVER = "nameOfDriver";
	/** Qualifier of the <code>DriverBase.driverId</code> attribute **/
	public static final String DRIVERID = "driverId";
	/** Qualifier of the <code>DriverBase.experience</code> attribute **/
	public static final String EXPERIENCE = "experience";
	/** Qualifier of the <code>DriverBase.dealer</code> attribute **/
	public static final String DEALER = "dealer";
	/** Relation ordering override parameter constants for DriverBase2DealerBase from ((trainingcore))*/
	protected static String DRIVERBASE2DEALERBASE_SRC_ORDERED = "relation.DriverBase2DealerBase.source.ordered";
	protected static String DRIVERBASE2DEALERBASE_TGT_ORDERED = "relation.DriverBase2DealerBase.target.ordered";
	/** Relation disable markmodifed parameter constants for DriverBase2DealerBase from ((trainingcore))*/
	protected static String DRIVERBASE2DEALERBASE_MARKMODIFIED = "relation.DriverBase2DealerBase.markmodified";
	/** Qualifier of the <code>DriverBase.vehicle</code> attribute **/
	public static final String VEHICLE = "vehicle";
	/**
	* {@link OneToManyHandler} for handling 1:n VEHICLE's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<VehicleBase> VEHICLEHANDLER = new OneToManyHandler<VehicleBase>(
	TrainingCoreConstants.TC.VEHICLEBASE,
	false,
	"driver",
	null,
	false,
	true,
	CollectionType.SET
	);
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(NAMEOFDRIVER, AttributeMode.INITIAL);
		tmp.put(DRIVERID, AttributeMode.INITIAL);
		tmp.put(EXPERIENCE, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DriverBase.dealer</code> attribute.
	 * @return the dealer - Dealer
	 */
	public Set<DealerBase> getDealer(final SessionContext ctx)
	{
		final List<DealerBase> items = getLinkedItems( 
			ctx,
			true,
			TrainingCoreConstants.Relations.DRIVERBASE2DEALERBASE,
			"DealerBase",
			null,
			false,
			false
		);
		return new LinkedHashSet<DealerBase>(items);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DriverBase.dealer</code> attribute.
	 * @return the dealer - Dealer
	 */
	public Set<DealerBase> getDealer()
	{
		return getDealer( getSession().getSessionContext() );
	}
	
	public long getDealerCount(final SessionContext ctx)
	{
		return getLinkedItemsCount(
			ctx,
			true,
			TrainingCoreConstants.Relations.DRIVERBASE2DEALERBASE,
			"DealerBase",
			null
		);
	}
	
	public long getDealerCount()
	{
		return getDealerCount( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>DriverBase.dealer</code> attribute. 
	 * @param value the dealer - Dealer
	 */
	public void setDealer(final SessionContext ctx, final Set<DealerBase> value)
	{
		setLinkedItems( 
			ctx,
			true,
			TrainingCoreConstants.Relations.DRIVERBASE2DEALERBASE,
			null,
			value,
			false,
			false,
			Utilities.getMarkModifiedOverride(DRIVERBASE2DEALERBASE_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>DriverBase.dealer</code> attribute. 
	 * @param value the dealer - Dealer
	 */
	public void setDealer(final Set<DealerBase> value)
	{
		setDealer( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to dealer. 
	 * @param value the item to add to dealer - Dealer
	 */
	public void addToDealer(final SessionContext ctx, final DealerBase value)
	{
		addLinkedItems( 
			ctx,
			true,
			TrainingCoreConstants.Relations.DRIVERBASE2DEALERBASE,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(DRIVERBASE2DEALERBASE_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to dealer. 
	 * @param value the item to add to dealer - Dealer
	 */
	public void addToDealer(final DealerBase value)
	{
		addToDealer( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from dealer. 
	 * @param value the item to remove from dealer - Dealer
	 */
	public void removeFromDealer(final SessionContext ctx, final DealerBase value)
	{
		removeLinkedItems( 
			ctx,
			true,
			TrainingCoreConstants.Relations.DRIVERBASE2DEALERBASE,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(DRIVERBASE2DEALERBASE_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from dealer. 
	 * @param value the item to remove from dealer - Dealer
	 */
	public void removeFromDealer(final DealerBase value)
	{
		removeFromDealer( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DriverBase.driverId</code> attribute.
	 * @return the driverId - id of driver
	 */
	public String getDriverId(final SessionContext ctx)
	{
		return (String)getProperty( ctx, DRIVERID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DriverBase.driverId</code> attribute.
	 * @return the driverId - id of driver
	 */
	public String getDriverId()
	{
		return getDriverId( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>DriverBase.driverId</code> attribute. 
	 * @param value the driverId - id of driver
	 */
	public void setDriverId(final SessionContext ctx, final String value)
	{
		setProperty(ctx, DRIVERID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>DriverBase.driverId</code> attribute. 
	 * @param value the driverId - id of driver
	 */
	public void setDriverId(final String value)
	{
		setDriverId( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DriverBase.experience</code> attribute.
	 * @return the experience - experience in years
	 */
	public String getExperience(final SessionContext ctx)
	{
		return (String)getProperty( ctx, EXPERIENCE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DriverBase.experience</code> attribute.
	 * @return the experience - experience in years
	 */
	public String getExperience()
	{
		return getExperience( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>DriverBase.experience</code> attribute. 
	 * @param value the experience - experience in years
	 */
	public void setExperience(final SessionContext ctx, final String value)
	{
		setProperty(ctx, EXPERIENCE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>DriverBase.experience</code> attribute. 
	 * @param value the experience - experience in years
	 */
	public void setExperience(final String value)
	{
		setExperience( getSession().getSessionContext(), value );
	}
	
	/**
	 * @deprecated since 2011, use {@link Utilities#getMarkModifiedOverride(de.hybris.platform.jalo.type.RelationType)
	 */
	@Override
	@Deprecated(since = "2105", forRemoval = true)
	public boolean isMarkModifiedDisabled(final Item referencedItem)
	{
		ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("DealerBase");
		if(relationSecondEnd0.isAssignableFrom(referencedItem.getComposedType()))
		{
			return Utilities.getMarkModifiedOverride(DRIVERBASE2DEALERBASE_MARKMODIFIED);
		}
		return true;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DriverBase.nameOfDriver</code> attribute.
	 * @return the nameOfDriver - driver name
	 */
	public String getNameOfDriver(final SessionContext ctx)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedDriverBase.getNameOfDriver requires a session language", 0 );
		}
		return (String)getLocalizedProperty( ctx, NAMEOFDRIVER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DriverBase.nameOfDriver</code> attribute.
	 * @return the nameOfDriver - driver name
	 */
	public String getNameOfDriver()
	{
		return getNameOfDriver( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DriverBase.nameOfDriver</code> attribute. 
	 * @return the localized nameOfDriver - driver name
	 */
	public Map<Language,String> getAllNameOfDriver(final SessionContext ctx)
	{
		return (Map<Language,String>)getAllLocalizedProperties(ctx,NAMEOFDRIVER,C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DriverBase.nameOfDriver</code> attribute. 
	 * @return the localized nameOfDriver - driver name
	 */
	public Map<Language,String> getAllNameOfDriver()
	{
		return getAllNameOfDriver( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>DriverBase.nameOfDriver</code> attribute. 
	 * @param value the nameOfDriver - driver name
	 */
	public void setNameOfDriver(final SessionContext ctx, final String value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		if( ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedDriverBase.setNameOfDriver requires a session language", 0 );
		}
		setLocalizedProperty(ctx, NAMEOFDRIVER,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>DriverBase.nameOfDriver</code> attribute. 
	 * @param value the nameOfDriver - driver name
	 */
	public void setNameOfDriver(final String value)
	{
		setNameOfDriver( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>DriverBase.nameOfDriver</code> attribute. 
	 * @param value the nameOfDriver - driver name
	 */
	public void setAllNameOfDriver(final SessionContext ctx, final Map<Language,String> value)
	{
		setAllLocalizedProperties(ctx,NAMEOFDRIVER,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>DriverBase.nameOfDriver</code> attribute. 
	 * @param value the nameOfDriver - driver name
	 */
	public void setAllNameOfDriver(final Map<Language,String> value)
	{
		setAllNameOfDriver( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DriverBase.vehicle</code> attribute.
	 * @return the vehicle - Vehicle
	 */
	public Set<VehicleBase> getVehicle(final SessionContext ctx)
	{
		return (Set<VehicleBase>)VEHICLEHANDLER.getValues( ctx, this );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DriverBase.vehicle</code> attribute.
	 * @return the vehicle - Vehicle
	 */
	public Set<VehicleBase> getVehicle()
	{
		return getVehicle( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>DriverBase.vehicle</code> attribute. 
	 * @param value the vehicle - Vehicle
	 */
	public void setVehicle(final SessionContext ctx, final Set<VehicleBase> value)
	{
		VEHICLEHANDLER.setValues( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>DriverBase.vehicle</code> attribute. 
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
		VEHICLEHANDLER.addValue( ctx, this, value );
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
		VEHICLEHANDLER.removeValue( ctx, this, value );
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
