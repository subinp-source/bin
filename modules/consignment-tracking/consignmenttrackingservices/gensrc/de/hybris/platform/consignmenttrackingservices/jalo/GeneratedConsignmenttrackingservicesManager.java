/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 13-Dec-2021, 3:58:17 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.consignmenttrackingservices.jalo;

import de.hybris.platform.consignmenttrackingservices.constants.ConsignmenttrackingservicesConstants;
import de.hybris.platform.consignmenttrackingservices.jalo.Carrier;
import de.hybris.platform.consignmenttrackingservices.jalo.ConsignmentTrackingIdValidConstraint;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.ordersplitting.jalo.Consignment;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type <code>ConsignmenttrackingservicesManager</code>.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedConsignmenttrackingservicesManager extends Extension
{
	protected static final Map<String, Map<String, AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, Map<String, AttributeMode>> ttmp = new HashMap();
		Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put("carrierDetails", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.ordersplitting.jalo.Consignment", Collections.unmodifiableMap(tmp));
		DEFAULT_INITIAL_ATTRIBUTES = ttmp;
	}
	@Override
	public Map<String, AttributeMode> getDefaultAttributeModes(final Class<? extends Item> itemClass)
	{
		Map<String, AttributeMode> ret = new HashMap<>();
		final Map<String, AttributeMode> attr = DEFAULT_INITIAL_ATTRIBUTES.get(itemClass.getName());
		if (attr != null)
		{
			ret.putAll(attr);
		}
		return ret;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Consignment.carrierDetails</code> attribute.
	 * @return the carrierDetails
	 */
	public Carrier getCarrierDetails(final SessionContext ctx, final Consignment item)
	{
		return (Carrier)item.getProperty( ctx, ConsignmenttrackingservicesConstants.Attributes.Consignment.CARRIERDETAILS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Consignment.carrierDetails</code> attribute.
	 * @return the carrierDetails
	 */
	public Carrier getCarrierDetails(final Consignment item)
	{
		return getCarrierDetails( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Consignment.carrierDetails</code> attribute. 
	 * @param value the carrierDetails
	 */
	public void setCarrierDetails(final SessionContext ctx, final Consignment item, final Carrier value)
	{
		item.setProperty(ctx, ConsignmenttrackingservicesConstants.Attributes.Consignment.CARRIERDETAILS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Consignment.carrierDetails</code> attribute. 
	 * @param value the carrierDetails
	 */
	public void setCarrierDetails(final Consignment item, final Carrier value)
	{
		setCarrierDetails( getSession().getSessionContext(), item, value );
	}
	
	public Carrier createCarrier(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( ConsignmenttrackingservicesConstants.TC.CARRIER );
			return (Carrier)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating Carrier : "+e.getMessage(), 0 );
		}
	}
	
	public Carrier createCarrier(final Map attributeValues)
	{
		return createCarrier( getSession().getSessionContext(), attributeValues );
	}
	
	public ConsignmentTrackingIdValidConstraint createConsignmentTrackingIdValidConstraint(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( ConsignmenttrackingservicesConstants.TC.CONSIGNMENTTRACKINGIDVALIDCONSTRAINT );
			return (ConsignmentTrackingIdValidConstraint)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating ConsignmentTrackingIdValidConstraint : "+e.getMessage(), 0 );
		}
	}
	
	public ConsignmentTrackingIdValidConstraint createConsignmentTrackingIdValidConstraint(final Map attributeValues)
	{
		return createConsignmentTrackingIdValidConstraint( getSession().getSessionContext(), attributeValues );
	}
	
	@Override
	public String getName()
	{
		return ConsignmenttrackingservicesConstants.EXTENSIONNAME;
	}
	
}
