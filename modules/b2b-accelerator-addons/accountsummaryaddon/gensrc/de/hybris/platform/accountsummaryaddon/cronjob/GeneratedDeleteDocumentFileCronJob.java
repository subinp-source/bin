/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 06-Oct-2021, 11:13:07 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.accountsummaryaddon.cronjob;

import de.hybris.platform.accountsummaryaddon.constants.AccountsummaryaddonConstants;
import de.hybris.platform.accountsummaryaddon.jalo.B2BDocumentType;
import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.accountsummaryaddon.cronjob.DeleteDocumentFileCronJob DeleteDocumentFileCronJob}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedDeleteDocumentFileCronJob extends CronJob
{
	/** Qualifier of the <code>DeleteDocumentFileCronJob.numberOfDay</code> attribute **/
	public static final String NUMBEROFDAY = "numberOfDay";
	/** Qualifier of the <code>DeleteDocumentFileCronJob.documentStatusList</code> attribute **/
	public static final String DOCUMENTSTATUSLIST = "documentStatusList";
	/** Qualifier of the <code>DeleteDocumentFileCronJob.documentTypeList</code> attribute **/
	public static final String DOCUMENTTYPELIST = "documentTypeList";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(CronJob.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(NUMBEROFDAY, AttributeMode.INITIAL);
		tmp.put(DOCUMENTSTATUSLIST, AttributeMode.INITIAL);
		tmp.put(DOCUMENTTYPELIST, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DeleteDocumentFileCronJob.documentStatusList</code> attribute.
	 * @return the documentStatusList
	 */
	public List<EnumerationValue> getDocumentStatusList(final SessionContext ctx)
	{
		List<EnumerationValue> coll = (List<EnumerationValue>)getProperty( ctx, DOCUMENTSTATUSLIST);
		return coll != null ? coll : Collections.EMPTY_LIST;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DeleteDocumentFileCronJob.documentStatusList</code> attribute.
	 * @return the documentStatusList
	 */
	public List<EnumerationValue> getDocumentStatusList()
	{
		return getDocumentStatusList( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>DeleteDocumentFileCronJob.documentStatusList</code> attribute. 
	 * @param value the documentStatusList
	 */
	public void setDocumentStatusList(final SessionContext ctx, final List<EnumerationValue> value)
	{
		setProperty(ctx, DOCUMENTSTATUSLIST,value == null || !value.isEmpty() ? value : null );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>DeleteDocumentFileCronJob.documentStatusList</code> attribute. 
	 * @param value the documentStatusList
	 */
	public void setDocumentStatusList(final List<EnumerationValue> value)
	{
		setDocumentStatusList( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DeleteDocumentFileCronJob.documentTypeList</code> attribute.
	 * @return the documentTypeList
	 */
	public List<B2BDocumentType> getDocumentTypeList(final SessionContext ctx)
	{
		List<B2BDocumentType> coll = (List<B2BDocumentType>)getProperty( ctx, DOCUMENTTYPELIST);
		return coll != null ? coll : Collections.EMPTY_LIST;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DeleteDocumentFileCronJob.documentTypeList</code> attribute.
	 * @return the documentTypeList
	 */
	public List<B2BDocumentType> getDocumentTypeList()
	{
		return getDocumentTypeList( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>DeleteDocumentFileCronJob.documentTypeList</code> attribute. 
	 * @param value the documentTypeList
	 */
	public void setDocumentTypeList(final SessionContext ctx, final List<B2BDocumentType> value)
	{
		setProperty(ctx, DOCUMENTTYPELIST,value == null || !value.isEmpty() ? value : null );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>DeleteDocumentFileCronJob.documentTypeList</code> attribute. 
	 * @param value the documentTypeList
	 */
	public void setDocumentTypeList(final List<B2BDocumentType> value)
	{
		setDocumentTypeList( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DeleteDocumentFileCronJob.numberOfDay</code> attribute.
	 * @return the numberOfDay - number of day to keep files
	 */
	public Integer getNumberOfDay(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, NUMBEROFDAY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DeleteDocumentFileCronJob.numberOfDay</code> attribute.
	 * @return the numberOfDay - number of day to keep files
	 */
	public Integer getNumberOfDay()
	{
		return getNumberOfDay( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DeleteDocumentFileCronJob.numberOfDay</code> attribute. 
	 * @return the numberOfDay - number of day to keep files
	 */
	public int getNumberOfDayAsPrimitive(final SessionContext ctx)
	{
		Integer value = getNumberOfDay( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DeleteDocumentFileCronJob.numberOfDay</code> attribute. 
	 * @return the numberOfDay - number of day to keep files
	 */
	public int getNumberOfDayAsPrimitive()
	{
		return getNumberOfDayAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>DeleteDocumentFileCronJob.numberOfDay</code> attribute. 
	 * @param value the numberOfDay - number of day to keep files
	 */
	public void setNumberOfDay(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, NUMBEROFDAY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>DeleteDocumentFileCronJob.numberOfDay</code> attribute. 
	 * @param value the numberOfDay - number of day to keep files
	 */
	public void setNumberOfDay(final Integer value)
	{
		setNumberOfDay( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>DeleteDocumentFileCronJob.numberOfDay</code> attribute. 
	 * @param value the numberOfDay - number of day to keep files
	 */
	public void setNumberOfDay(final SessionContext ctx, final int value)
	{
		setNumberOfDay( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>DeleteDocumentFileCronJob.numberOfDay</code> attribute. 
	 * @param value the numberOfDay - number of day to keep files
	 */
	public void setNumberOfDay(final int value)
	{
		setNumberOfDay( getSession().getSessionContext(), value );
	}
	
}
