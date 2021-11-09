/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 06-Oct-2021, 11:13:07 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.accountsummaryaddon.jalo;

import de.hybris.platform.accountsummaryaddon.constants.AccountsummaryaddonConstants;
import de.hybris.platform.cms2.jalo.contents.components.SimpleCMSComponent;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.accountsummaryaddon.jalo.AccountSummaryAccountStatusComponent AccountSummaryAccountStatusComponent}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedAccountSummaryAccountStatusComponent extends SimpleCMSComponent
{
	/** Qualifier of the <code>AccountSummaryAccountStatusComponent.listViewPageSize</code> attribute **/
	public static final String LISTVIEWPAGESIZE = "listViewPageSize";
	/** Qualifier of the <code>AccountSummaryAccountStatusComponent.gridViewPageSize</code> attribute **/
	public static final String GRIDVIEWPAGESIZE = "gridViewPageSize";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(SimpleCMSComponent.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(LISTVIEWPAGESIZE, AttributeMode.INITIAL);
		tmp.put(GRIDVIEWPAGESIZE, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AccountSummaryAccountStatusComponent.gridViewPageSize</code> attribute.
	 * @return the gridViewPageSize - code
	 */
	public Integer getGridViewPageSize(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, GRIDVIEWPAGESIZE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AccountSummaryAccountStatusComponent.gridViewPageSize</code> attribute.
	 * @return the gridViewPageSize - code
	 */
	public Integer getGridViewPageSize()
	{
		return getGridViewPageSize( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AccountSummaryAccountStatusComponent.gridViewPageSize</code> attribute. 
	 * @return the gridViewPageSize - code
	 */
	public int getGridViewPageSizeAsPrimitive(final SessionContext ctx)
	{
		Integer value = getGridViewPageSize( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AccountSummaryAccountStatusComponent.gridViewPageSize</code> attribute. 
	 * @return the gridViewPageSize - code
	 */
	public int getGridViewPageSizeAsPrimitive()
	{
		return getGridViewPageSizeAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AccountSummaryAccountStatusComponent.gridViewPageSize</code> attribute. 
	 * @param value the gridViewPageSize - code
	 */
	public void setGridViewPageSize(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, GRIDVIEWPAGESIZE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AccountSummaryAccountStatusComponent.gridViewPageSize</code> attribute. 
	 * @param value the gridViewPageSize - code
	 */
	public void setGridViewPageSize(final Integer value)
	{
		setGridViewPageSize( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AccountSummaryAccountStatusComponent.gridViewPageSize</code> attribute. 
	 * @param value the gridViewPageSize - code
	 */
	public void setGridViewPageSize(final SessionContext ctx, final int value)
	{
		setGridViewPageSize( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AccountSummaryAccountStatusComponent.gridViewPageSize</code> attribute. 
	 * @param value the gridViewPageSize - code
	 */
	public void setGridViewPageSize(final int value)
	{
		setGridViewPageSize( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AccountSummaryAccountStatusComponent.listViewPageSize</code> attribute.
	 * @return the listViewPageSize - code
	 */
	public Integer getListViewPageSize(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, LISTVIEWPAGESIZE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AccountSummaryAccountStatusComponent.listViewPageSize</code> attribute.
	 * @return the listViewPageSize - code
	 */
	public Integer getListViewPageSize()
	{
		return getListViewPageSize( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AccountSummaryAccountStatusComponent.listViewPageSize</code> attribute. 
	 * @return the listViewPageSize - code
	 */
	public int getListViewPageSizeAsPrimitive(final SessionContext ctx)
	{
		Integer value = getListViewPageSize( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AccountSummaryAccountStatusComponent.listViewPageSize</code> attribute. 
	 * @return the listViewPageSize - code
	 */
	public int getListViewPageSizeAsPrimitive()
	{
		return getListViewPageSizeAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AccountSummaryAccountStatusComponent.listViewPageSize</code> attribute. 
	 * @param value the listViewPageSize - code
	 */
	public void setListViewPageSize(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, LISTVIEWPAGESIZE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AccountSummaryAccountStatusComponent.listViewPageSize</code> attribute. 
	 * @param value the listViewPageSize - code
	 */
	public void setListViewPageSize(final Integer value)
	{
		setListViewPageSize( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AccountSummaryAccountStatusComponent.listViewPageSize</code> attribute. 
	 * @param value the listViewPageSize - code
	 */
	public void setListViewPageSize(final SessionContext ctx, final int value)
	{
		setListViewPageSize( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AccountSummaryAccountStatusComponent.listViewPageSize</code> attribute. 
	 * @param value the listViewPageSize - code
	 */
	public void setListViewPageSize(final int value)
	{
		setListViewPageSize( getSession().getSessionContext(), value );
	}
	
}
