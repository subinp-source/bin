/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.services;

import de.hybris.platform.b2b.model.B2BReportingSetModel;
import de.hybris.platform.b2b.model.B2BUnitModel;


/**
 * A service for b2b reporting.
 *
 * @spring.bean b2bReportingService
 */
public interface B2BReportingService
{

	/**
	 * Find reporting set by code.
	 *
	 * @param code
	 *           the code
	 * @return the b2 b reporting set model
	 * @deprecated Since 4.4. Use {@link #getReportingSetForCode(String)} instead
	 */
	@Deprecated(since = "4.4", forRemoval = true)
	public B2BReportingSetModel findReportingSetByCode(String code);

	/**
	 * Find reporting set by code.
	 *
	 * @param code
	 *           the code
	 * @return the b2 b reporting set model
	 */
	public B2BReportingSetModel getReportingSetForCode(String code);

	/**
	 * Find reporting set for b2 b unit.
	 *
	 * @param unit
	 *           the unit
	 * @return the b2 b reporting set model
	 * @deprecated Since 4.4. Use {@link #getReportingSetForB2BUnit(B2BUnitModel)} instead
	 */
	@Deprecated(since = "4.4", forRemoval = true)
	public B2BReportingSetModel findReportingSetForB2BUnit(B2BUnitModel unit);

	/**
	 * Find reporting set for a b2b unit.
	 *
	 * @param unit
	 *           the unit
	 * @return the b2 b reporting set model
	 */
	public B2BReportingSetModel getReportingSetForB2BUnit(B2BUnitModel unit);


	/**
	 * Create or Update Reporting Set for B2BUnit which is the Branch Units.
	 *
	 * @param unit
	 *           the unit
	 * @return {@link B2BReportingSetModel} the new reporting organization for unit
	 */
	public B2BReportingSetModel setReportSetForUnit(final B2BUnitModel unit);

	/**
	 * Updates the reporting set for a b2b unit.
	 *
	 * @param unit
	 *           the unit that has been updated
	 */
	public abstract void updateReportingSetForUnitAndParents(final B2BUnitModel unit);

	/**
	 * Sets the reporting organization on a B2BUnit
	 *
	 * @param unit
	 *           the unit to have the reporting organization set on
	 */
	public abstract void setReportingOrganizationForUnit(final B2BUnitModel unit);
}
