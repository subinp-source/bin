/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 11-Dec-2021, 12:32:58 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.productconfig.frontend.jalo;

import de.hybris.platform.cms2.jalo.contents.components.CMSParagraphComponent;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.sap.productconfig.frontend.constants.SapproductconfigaddonConstants;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.cms2.jalo.contents.components.CMSParagraphComponent ProductConfigurationPriceSummaryComponent}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedProductConfigurationPriceSummaryComponent extends CMSParagraphComponent
{
	/** Qualifier of the <code>ProductConfigurationPriceSummaryComponent.showPriceDetails</code> attribute **/
	public static final String SHOWPRICEDETAILS = "showPriceDetails";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(CMSParagraphComponent.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(SHOWPRICEDETAILS, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductConfigurationPriceSummaryComponent.showPriceDetails</code> attribute.
	 * @return the showPriceDetails
	 */
	public Boolean isShowPriceDetails(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, SHOWPRICEDETAILS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductConfigurationPriceSummaryComponent.showPriceDetails</code> attribute.
	 * @return the showPriceDetails
	 */
	public Boolean isShowPriceDetails()
	{
		return isShowPriceDetails( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductConfigurationPriceSummaryComponent.showPriceDetails</code> attribute. 
	 * @return the showPriceDetails
	 */
	public boolean isShowPriceDetailsAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isShowPriceDetails( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductConfigurationPriceSummaryComponent.showPriceDetails</code> attribute. 
	 * @return the showPriceDetails
	 */
	public boolean isShowPriceDetailsAsPrimitive()
	{
		return isShowPriceDetailsAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductConfigurationPriceSummaryComponent.showPriceDetails</code> attribute. 
	 * @param value the showPriceDetails
	 */
	public void setShowPriceDetails(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, SHOWPRICEDETAILS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductConfigurationPriceSummaryComponent.showPriceDetails</code> attribute. 
	 * @param value the showPriceDetails
	 */
	public void setShowPriceDetails(final Boolean value)
	{
		setShowPriceDetails( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductConfigurationPriceSummaryComponent.showPriceDetails</code> attribute. 
	 * @param value the showPriceDetails
	 */
	public void setShowPriceDetails(final SessionContext ctx, final boolean value)
	{
		setShowPriceDetails( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductConfigurationPriceSummaryComponent.showPriceDetails</code> attribute. 
	 * @param value the showPriceDetails
	 */
	public void setShowPriceDetails(final boolean value)
	{
		setShowPriceDetails( getSession().getSessionContext(), value );
	}
	
}
