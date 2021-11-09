/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 06-Oct-2021, 11:13:07 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2webservicesfeaturetests.jalo;

import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.test.TestItem;
import de.hybris.platform.odata2webservicesfeaturetests.constants.Odata2webservicesfeaturetestsConstants;
import de.hybris.platform.odata2webservicesfeaturetests.jalo.TestIntegrationItem;

/**
 * Generated class for type {@link de.hybris.platform.jalo.test.TestItem TestIntegrationItem3}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedTestIntegrationItem3 extends TestItem
{
	/** Qualifier of the <code>TestIntegrationItem3.code</code> attribute **/
	public static final String CODE = "code";
	/** Qualifier of the <code>TestIntegrationItem3.requiredItem</code> attribute **/
	public static final String REQUIREDITEM = "requiredItem";
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestIntegrationItem3.code</code> attribute.
	 * @return the code
	 */
	public String getCode(final SessionContext ctx)
	{
		return (String)getProperty( ctx, CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestIntegrationItem3.code</code> attribute.
	 * @return the code
	 */
	public String getCode()
	{
		return getCode( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TestIntegrationItem3.code</code> attribute. 
	 * @param value the code
	 */
	public void setCode(final SessionContext ctx, final String value)
	{
		setProperty(ctx, CODE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TestIntegrationItem3.code</code> attribute. 
	 * @param value the code
	 */
	public void setCode(final String value)
	{
		setCode( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestIntegrationItem3.requiredItem</code> attribute.
	 * @return the requiredItem
	 */
	public TestIntegrationItem getRequiredItem(final SessionContext ctx)
	{
		return (TestIntegrationItem)getProperty( ctx, REQUIREDITEM);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestIntegrationItem3.requiredItem</code> attribute.
	 * @return the requiredItem
	 */
	public TestIntegrationItem getRequiredItem()
	{
		return getRequiredItem( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TestIntegrationItem3.requiredItem</code> attribute. 
	 * @param value the requiredItem
	 */
	public void setRequiredItem(final SessionContext ctx, final TestIntegrationItem value)
	{
		setProperty(ctx, REQUIREDITEM,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TestIntegrationItem3.requiredItem</code> attribute. 
	 * @param value the requiredItem
	 */
	public void setRequiredItem(final TestIntegrationItem value)
	{
		setRequiredItem( getSession().getSessionContext(), value );
	}
	
}
