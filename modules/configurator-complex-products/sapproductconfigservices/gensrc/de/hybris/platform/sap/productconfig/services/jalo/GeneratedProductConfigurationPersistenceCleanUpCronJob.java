/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 11-Dec-2021, 12:32:58 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.productconfig.services.jalo;

import de.hybris.platform.basecommerce.jalo.site.BaseSite;
import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.sap.productconfig.services.constants.SapproductconfigservicesConstants;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.cronjob.jalo.CronJob ProductConfigurationPersistenceCleanUpCronJob}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedProductConfigurationPersistenceCleanUpCronJob extends CronJob
{
	/** Qualifier of the <code>ProductConfigurationPersistenceCleanUpCronJob.thresholdDays</code> attribute **/
	public static final String THRESHOLDDAYS = "thresholdDays";
	/** Qualifier of the <code>ProductConfigurationPersistenceCleanUpCronJob.cleanUpMode</code> attribute **/
	public static final String CLEANUPMODE = "cleanUpMode";
	/** Qualifier of the <code>ProductConfigurationPersistenceCleanUpCronJob.baseSite</code> attribute **/
	public static final String BASESITE = "baseSite";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(CronJob.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(THRESHOLDDAYS, AttributeMode.INITIAL);
		tmp.put(CLEANUPMODE, AttributeMode.INITIAL);
		tmp.put(BASESITE, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductConfigurationPersistenceCleanUpCronJob.baseSite</code> attribute.
	 * @return the baseSite
	 */
	public BaseSite getBaseSite(final SessionContext ctx)
	{
		return (BaseSite)getProperty( ctx, BASESITE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductConfigurationPersistenceCleanUpCronJob.baseSite</code> attribute.
	 * @return the baseSite
	 */
	public BaseSite getBaseSite()
	{
		return getBaseSite( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductConfigurationPersistenceCleanUpCronJob.baseSite</code> attribute. 
	 * @param value the baseSite
	 */
	public void setBaseSite(final SessionContext ctx, final BaseSite value)
	{
		setProperty(ctx, BASESITE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductConfigurationPersistenceCleanUpCronJob.baseSite</code> attribute. 
	 * @param value the baseSite
	 */
	public void setBaseSite(final BaseSite value)
	{
		setBaseSite( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductConfigurationPersistenceCleanUpCronJob.cleanUpMode</code> attribute.
	 * @return the cleanUpMode
	 */
	public EnumerationValue getCleanUpMode(final SessionContext ctx)
	{
		return (EnumerationValue)getProperty( ctx, CLEANUPMODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductConfigurationPersistenceCleanUpCronJob.cleanUpMode</code> attribute.
	 * @return the cleanUpMode
	 */
	public EnumerationValue getCleanUpMode()
	{
		return getCleanUpMode( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductConfigurationPersistenceCleanUpCronJob.cleanUpMode</code> attribute. 
	 * @param value the cleanUpMode
	 */
	public void setCleanUpMode(final SessionContext ctx, final EnumerationValue value)
	{
		setProperty(ctx, CLEANUPMODE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductConfigurationPersistenceCleanUpCronJob.cleanUpMode</code> attribute. 
	 * @param value the cleanUpMode
	 */
	public void setCleanUpMode(final EnumerationValue value)
	{
		setCleanUpMode( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductConfigurationPersistenceCleanUpCronJob.thresholdDays</code> attribute.
	 * @return the thresholdDays
	 */
	public Integer getThresholdDays(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, THRESHOLDDAYS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductConfigurationPersistenceCleanUpCronJob.thresholdDays</code> attribute.
	 * @return the thresholdDays
	 */
	public Integer getThresholdDays()
	{
		return getThresholdDays( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductConfigurationPersistenceCleanUpCronJob.thresholdDays</code> attribute. 
	 * @return the thresholdDays
	 */
	public int getThresholdDaysAsPrimitive(final SessionContext ctx)
	{
		Integer value = getThresholdDays( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductConfigurationPersistenceCleanUpCronJob.thresholdDays</code> attribute. 
	 * @return the thresholdDays
	 */
	public int getThresholdDaysAsPrimitive()
	{
		return getThresholdDaysAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductConfigurationPersistenceCleanUpCronJob.thresholdDays</code> attribute. 
	 * @param value the thresholdDays
	 */
	public void setThresholdDays(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, THRESHOLDDAYS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductConfigurationPersistenceCleanUpCronJob.thresholdDays</code> attribute. 
	 * @param value the thresholdDays
	 */
	public void setThresholdDays(final Integer value)
	{
		setThresholdDays( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductConfigurationPersistenceCleanUpCronJob.thresholdDays</code> attribute. 
	 * @param value the thresholdDays
	 */
	public void setThresholdDays(final SessionContext ctx, final int value)
	{
		setThresholdDays( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ProductConfigurationPersistenceCleanUpCronJob.thresholdDays</code> attribute. 
	 * @param value the thresholdDays
	 */
	public void setThresholdDays(final int value)
	{
		setThresholdDays( getSession().getSessionContext(), value );
	}
	
}
