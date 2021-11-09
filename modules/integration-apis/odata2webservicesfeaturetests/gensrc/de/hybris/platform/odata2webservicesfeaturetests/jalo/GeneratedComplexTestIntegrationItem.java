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
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.odata2webservicesfeaturetests.constants.Odata2webservicesfeaturetestsConstants;
import de.hybris.platform.odata2webservicesfeaturetests.jalo.TestIntegrationItem;
import de.hybris.platform.odata2webservicesfeaturetests.jalo.TestIntegrationItemDetail;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Set;

/**
 * Generated class for type {@link de.hybris.platform.jalo.test.TestItem ComplexTestIntegrationItem}.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedComplexTestIntegrationItem extends TestIntegrationItem
{
	/** Qualifier of the <code>ComplexTestIntegrationItem.requiredDetails</code> attribute **/
	public static final String REQUIREDDETAILS = "requiredDetails";
	/**
	* {@link OneToManyHandler} for handling 1:n REQUIREDDETAILS's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<TestIntegrationItemDetail> REQUIREDDETAILSHANDLER = new OneToManyHandler<TestIntegrationItemDetail>(
	Odata2webservicesfeaturetestsConstants.TC.TESTINTEGRATIONITEMDETAIL,
	false,
	"master",
	null,
	false,
	true,
	CollectionType.SET
	);
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ComplexTestIntegrationItem.requiredDetails</code> attribute.
	 * @return the requiredDetails
	 */
	public Set<TestIntegrationItemDetail> getRequiredDetails(final SessionContext ctx)
	{
		return (Set<TestIntegrationItemDetail>)REQUIREDDETAILSHANDLER.getValues( ctx, this );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ComplexTestIntegrationItem.requiredDetails</code> attribute.
	 * @return the requiredDetails
	 */
	public Set<TestIntegrationItemDetail> getRequiredDetails()
	{
		return getRequiredDetails( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ComplexTestIntegrationItem.requiredDetails</code> attribute. 
	 * @param value the requiredDetails
	 */
	public void setRequiredDetails(final SessionContext ctx, final Set<TestIntegrationItemDetail> value)
	{
		REQUIREDDETAILSHANDLER.setValues( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ComplexTestIntegrationItem.requiredDetails</code> attribute. 
	 * @param value the requiredDetails
	 */
	public void setRequiredDetails(final Set<TestIntegrationItemDetail> value)
	{
		setRequiredDetails( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to requiredDetails. 
	 * @param value the item to add to requiredDetails
	 */
	public void addToRequiredDetails(final SessionContext ctx, final TestIntegrationItemDetail value)
	{
		REQUIREDDETAILSHANDLER.addValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to requiredDetails. 
	 * @param value the item to add to requiredDetails
	 */
	public void addToRequiredDetails(final TestIntegrationItemDetail value)
	{
		addToRequiredDetails( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from requiredDetails. 
	 * @param value the item to remove from requiredDetails
	 */
	public void removeFromRequiredDetails(final SessionContext ctx, final TestIntegrationItemDetail value)
	{
		REQUIREDDETAILSHANDLER.removeValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from requiredDetails. 
	 * @param value the item to remove from requiredDetails
	 */
	public void removeFromRequiredDetails(final TestIntegrationItemDetail value)
	{
		removeFromRequiredDetails( getSession().getSessionContext(), value );
	}
	
}
