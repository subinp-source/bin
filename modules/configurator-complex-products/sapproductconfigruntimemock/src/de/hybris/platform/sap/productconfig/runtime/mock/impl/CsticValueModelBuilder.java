/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.runtime.mock.impl;

import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticValueModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.CsticValueModelImpl;


/**
 * helper class to build cstic values for Mock implemntations<br>
 * <b>create a new instance for every model object you want to build</b>
 */
public class CsticValueModelBuilder
{

	private final CsticValueModel csticValue;

	public CsticValueModelBuilder()
	{
		csticValue = new CsticValueModelImpl();
		csticValue.setDomainValue(true);
	}


	public CsticValueModel build()
	{
		return csticValue;
	}


	public CsticValueModelBuilder withName(final String csticValueName, final String langDependentName)
	{
		csticValue.setName(csticValueName);
		csticValue.setLanguageDependentName(langDependentName);

		return this;
	}
	
	public CsticValueModelBuilder selectable() {
		csticValue.setSelectable(true);
		return this;
	}
}
