/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 08-Nov-2021, 4:51:25 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.jalo;

import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.searchservices.constants.SearchservicesConstants;
import de.hybris.platform.searchservices.jalo.SnIndexType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.cronjob.jalo.CronJob AbstractSnIndexerCronJob}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedAbstractSnIndexerCronJob extends CronJob
{
	/** Qualifier of the <code>AbstractSnIndexerCronJob.lastSuccessfulStartTime</code> attribute **/
	public static final String LASTSUCCESSFULSTARTTIME = "lastSuccessfulStartTime";
	/** Qualifier of the <code>AbstractSnIndexerCronJob.indexTypePOS</code> attribute **/
	public static final String INDEXTYPEPOS = "indexTypePOS";
	/** Qualifier of the <code>AbstractSnIndexerCronJob.indexType</code> attribute **/
	public static final String INDEXTYPE = "indexType";
	/**
	* {@link BidirectionalOneToManyHandler} for handling 1:n INDEXTYPE's relation attributes from 'one' side.
	**/
	protected static final BidirectionalOneToManyHandler<GeneratedAbstractSnIndexerCronJob> INDEXTYPEHANDLER = new BidirectionalOneToManyHandler<GeneratedAbstractSnIndexerCronJob>(
	SearchservicesConstants.TC.ABSTRACTSNINDEXERCRONJOB,
	false,
	"indexType",
	"indexTypePOS",
	true,
	true,
	CollectionType.LIST
	);
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(CronJob.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(LASTSUCCESSFULSTARTTIME, AttributeMode.INITIAL);
		tmp.put(INDEXTYPEPOS, AttributeMode.INITIAL);
		tmp.put(INDEXTYPE, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	@Override
	protected Item createItem(final SessionContext ctx, final ComposedType type, final ItemAttributeMap allAttributes) throws JaloBusinessException
	{
		INDEXTYPEHANDLER.newInstance(ctx, allAttributes);
		return super.createItem( ctx, type, allAttributes );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractSnIndexerCronJob.indexType</code> attribute.
	 * @return the indexType
	 */
	public SnIndexType getIndexType(final SessionContext ctx)
	{
		return (SnIndexType)getProperty( ctx, INDEXTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractSnIndexerCronJob.indexType</code> attribute.
	 * @return the indexType
	 */
	public SnIndexType getIndexType()
	{
		return getIndexType( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractSnIndexerCronJob.indexType</code> attribute. 
	 * @param value the indexType
	 */
	protected void setIndexType(final SessionContext ctx, final SnIndexType value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		// initial-only attribute: make sure this attribute can be set during item creation only
		if ( ctx.getAttribute( "core.types.creation.initial") != Boolean.TRUE )
		{
			throw new JaloInvalidParameterException( "attribute '"+INDEXTYPE+"' is not changeable", 0 );
		}
		INDEXTYPEHANDLER.addValue( ctx, value, this  );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractSnIndexerCronJob.indexType</code> attribute. 
	 * @param value the indexType
	 */
	protected void setIndexType(final SnIndexType value)
	{
		setIndexType( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractSnIndexerCronJob.indexTypePOS</code> attribute.
	 * @return the indexTypePOS
	 */
	 Integer getIndexTypePOS(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, INDEXTYPEPOS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractSnIndexerCronJob.indexTypePOS</code> attribute.
	 * @return the indexTypePOS
	 */
	 Integer getIndexTypePOS()
	{
		return getIndexTypePOS( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractSnIndexerCronJob.indexTypePOS</code> attribute. 
	 * @return the indexTypePOS
	 */
	 int getIndexTypePOSAsPrimitive(final SessionContext ctx)
	{
		Integer value = getIndexTypePOS( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractSnIndexerCronJob.indexTypePOS</code> attribute. 
	 * @return the indexTypePOS
	 */
	 int getIndexTypePOSAsPrimitive()
	{
		return getIndexTypePOSAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractSnIndexerCronJob.indexTypePOS</code> attribute. 
	 * @param value the indexTypePOS
	 */
	 void setIndexTypePOS(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, INDEXTYPEPOS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractSnIndexerCronJob.indexTypePOS</code> attribute. 
	 * @param value the indexTypePOS
	 */
	 void setIndexTypePOS(final Integer value)
	{
		setIndexTypePOS( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractSnIndexerCronJob.indexTypePOS</code> attribute. 
	 * @param value the indexTypePOS
	 */
	 void setIndexTypePOS(final SessionContext ctx, final int value)
	{
		setIndexTypePOS( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractSnIndexerCronJob.indexTypePOS</code> attribute. 
	 * @param value the indexTypePOS
	 */
	 void setIndexTypePOS(final int value)
	{
		setIndexTypePOS( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractSnIndexerCronJob.lastSuccessfulStartTime</code> attribute.
	 * @return the lastSuccessfulStartTime
	 */
	public Date getLastSuccessfulStartTime(final SessionContext ctx)
	{
		return (Date)getProperty( ctx, LASTSUCCESSFULSTARTTIME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractSnIndexerCronJob.lastSuccessfulStartTime</code> attribute.
	 * @return the lastSuccessfulStartTime
	 */
	public Date getLastSuccessfulStartTime()
	{
		return getLastSuccessfulStartTime( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractSnIndexerCronJob.lastSuccessfulStartTime</code> attribute. 
	 * @param value the lastSuccessfulStartTime
	 */
	public void setLastSuccessfulStartTime(final SessionContext ctx, final Date value)
	{
		setProperty(ctx, LASTSUCCESSFULSTARTTIME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractSnIndexerCronJob.lastSuccessfulStartTime</code> attribute. 
	 * @param value the lastSuccessfulStartTime
	 */
	public void setLastSuccessfulStartTime(final Date value)
	{
		setLastSuccessfulStartTime( getSession().getSessionContext(), value );
	}
	
}
