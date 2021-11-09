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
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.odata2webservicesfeaturetests.constants.Odata2webservicesfeaturetestsConstants;
import de.hybris.platform.odata2webservicesfeaturetests.jalo.TestIntegrationItem;
import de.hybris.platform.util.BidirectionalOneToManyHandler;

/**
 * Generated class for type {@link de.hybris.platform.jalo.test.TestItem TestIntegrationItemDetail}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedTestIntegrationItemDetail extends TestItem
{
	/** Qualifier of the <code>TestIntegrationItemDetail.code</code> attribute **/
	public static final String CODE = "code";
	/** Qualifier of the <code>TestIntegrationItemDetail.item</code> attribute **/
	public static final String ITEM = "item";
	/** Qualifier of the <code>TestIntegrationItemDetail.master</code> attribute **/
	public static final String MASTER = "master";
	/**
	* {@link BidirectionalOneToManyHandler} for handling 1:n MASTER's relation attributes from 'one' side.
	**/
	protected static final BidirectionalOneToManyHandler<GeneratedTestIntegrationItemDetail> MASTERHANDLER = new BidirectionalOneToManyHandler<GeneratedTestIntegrationItemDetail>(
	Odata2webservicesfeaturetestsConstants.TC.TESTINTEGRATIONITEMDETAIL,
	false,
	"master",
	null,
	false,
	true,
	CollectionType.LIST
	);
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestIntegrationItemDetail.code</code> attribute.
	 * @return the code - Unique identifier of this detail item.
	 */
	public String getCode(final SessionContext ctx)
	{
		return (String)getProperty( ctx, CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestIntegrationItemDetail.code</code> attribute.
	 * @return the code - Unique identifier of this detail item.
	 */
	public String getCode()
	{
		return getCode( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TestIntegrationItemDetail.code</code> attribute. 
	 * @param value the code - Unique identifier of this detail item.
	 */
	public void setCode(final SessionContext ctx, final String value)
	{
		setProperty(ctx, CODE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TestIntegrationItemDetail.code</code> attribute. 
	 * @param value the code - Unique identifier of this detail item.
	 */
	public void setCode(final String value)
	{
		setCode( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestIntegrationItemDetail.item</code> attribute.
	 * @return the item - Defines optional one-to-one association between this detail and a TestIntegrationItem.
	 */
	public TestIntegrationItem getItem(final SessionContext ctx)
	{
		return (TestIntegrationItem)getProperty( ctx, ITEM);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestIntegrationItemDetail.item</code> attribute.
	 * @return the item - Defines optional one-to-one association between this detail and a TestIntegrationItem.
	 */
	public TestIntegrationItem getItem()
	{
		return getItem( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TestIntegrationItemDetail.item</code> attribute. 
	 * @param value the item - Defines optional one-to-one association between this detail and a TestIntegrationItem.
	 */
	public void setItem(final SessionContext ctx, final TestIntegrationItem value)
	{
		setProperty(ctx, ITEM,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TestIntegrationItemDetail.item</code> attribute. 
	 * @param value the item - Defines optional one-to-one association between this detail and a TestIntegrationItem.
	 */
	public void setItem(final TestIntegrationItem value)
	{
		setItem( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestIntegrationItemDetail.master</code> attribute.
	 * @return the master
	 */
	public TestIntegrationItem getMaster(final SessionContext ctx)
	{
		return (TestIntegrationItem)getProperty( ctx, MASTER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestIntegrationItemDetail.master</code> attribute.
	 * @return the master
	 */
	public TestIntegrationItem getMaster()
	{
		return getMaster( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TestIntegrationItemDetail.master</code> attribute. 
	 * @param value the master
	 */
	public void setMaster(final SessionContext ctx, final TestIntegrationItem value)
	{
		MASTERHANDLER.addValue( ctx, value, this  );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TestIntegrationItemDetail.master</code> attribute. 
	 * @param value the master
	 */
	public void setMaster(final TestIntegrationItem value)
	{
		setMaster( getSession().getSessionContext(), value );
	}
	
}
