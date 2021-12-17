/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 13-Dec-2021, 3:58:17 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.productconfig.services.jalo;

import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.order.jalo.AbstractOrderEntryProductInfo;
import de.hybris.platform.sap.productconfig.services.constants.SapproductconfigservicesConstants;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.sap.productconfig.services.jalo.CPQOrderEntryProductInfo CPQOrderEntryProductInfo}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedCPQOrderEntryProductInfo extends AbstractOrderEntryProductInfo
{
	/** Qualifier of the <code>CPQOrderEntryProductInfo.cpqCharacteristicName</code> attribute **/
	public static final String CPQCHARACTERISTICNAME = "cpqCharacteristicName";
	/** Qualifier of the <code>CPQOrderEntryProductInfo.cpqCharacteristicAssignedValues</code> attribute **/
	public static final String CPQCHARACTERISTICASSIGNEDVALUES = "cpqCharacteristicAssignedValues";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(AbstractOrderEntryProductInfo.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(CPQCHARACTERISTICNAME, AttributeMode.INITIAL);
		tmp.put(CPQCHARACTERISTICASSIGNEDVALUES, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CPQOrderEntryProductInfo.cpqCharacteristicAssignedValues</code> attribute.
	 * @return the cpqCharacteristicAssignedValues - Language independent name of the characteristic assigned values for the inline configuration display
	 */
	public String getCpqCharacteristicAssignedValues(final SessionContext ctx)
	{
		return (String)getProperty( ctx, CPQCHARACTERISTICASSIGNEDVALUES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CPQOrderEntryProductInfo.cpqCharacteristicAssignedValues</code> attribute.
	 * @return the cpqCharacteristicAssignedValues - Language independent name of the characteristic assigned values for the inline configuration display
	 */
	public String getCpqCharacteristicAssignedValues()
	{
		return getCpqCharacteristicAssignedValues( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CPQOrderEntryProductInfo.cpqCharacteristicAssignedValues</code> attribute. 
	 * @param value the cpqCharacteristicAssignedValues - Language independent name of the characteristic assigned values for the inline configuration display
	 */
	public void setCpqCharacteristicAssignedValues(final SessionContext ctx, final String value)
	{
		setProperty(ctx, CPQCHARACTERISTICASSIGNEDVALUES,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CPQOrderEntryProductInfo.cpqCharacteristicAssignedValues</code> attribute. 
	 * @param value the cpqCharacteristicAssignedValues - Language independent name of the characteristic assigned values for the inline configuration display
	 */
	public void setCpqCharacteristicAssignedValues(final String value)
	{
		setCpqCharacteristicAssignedValues( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CPQOrderEntryProductInfo.cpqCharacteristicName</code> attribute.
	 * @return the cpqCharacteristicName - Language independent name of the characteristic for the inline configuration display
	 */
	public String getCpqCharacteristicName(final SessionContext ctx)
	{
		return (String)getProperty( ctx, CPQCHARACTERISTICNAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CPQOrderEntryProductInfo.cpqCharacteristicName</code> attribute.
	 * @return the cpqCharacteristicName - Language independent name of the characteristic for the inline configuration display
	 */
	public String getCpqCharacteristicName()
	{
		return getCpqCharacteristicName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CPQOrderEntryProductInfo.cpqCharacteristicName</code> attribute. 
	 * @param value the cpqCharacteristicName - Language independent name of the characteristic for the inline configuration display
	 */
	public void setCpqCharacteristicName(final SessionContext ctx, final String value)
	{
		setProperty(ctx, CPQCHARACTERISTICNAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CPQOrderEntryProductInfo.cpqCharacteristicName</code> attribute. 
	 * @param value the cpqCharacteristicName - Language independent name of the characteristic for the inline configuration display
	 */
	public void setCpqCharacteristicName(final String value)
	{
		setCpqCharacteristicName( getSession().getSessionContext(), value );
	}
	
}
