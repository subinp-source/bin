/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.personalizationservices.stub;

import de.hybris.platform.personalizationservices.dynamic.CxCustomizationActiveAttributeHandler;
import de.hybris.platform.personalizationservices.dynamic.CxCustomizationRankAttributeHandler;
import de.hybris.platform.personalizationservices.model.CxCustomizationModel;
import de.hybris.platform.personalizationservices.strategies.impl.DefaultRankAssignmentStrategy;
import de.hybris.platform.servicelayer.time.TimeService;


public class CxCustomizationModelStub extends CxCustomizationModel
{
	private final CxCustomizationRankAttributeHandler rankHandler;
	private final CxCustomizationActiveAttributeHandler activeHandler;

	public CxCustomizationModelStub()
	{
		this(new MockTimeService());
	}

	public CxCustomizationModelStub(final TimeService timeService)
	{
		rankHandler = new CxCustomizationRankAttributeHandler();
		rankHandler.setRankAssigmentStrategy(new DefaultRankAssignmentStrategy());

		activeHandler = new CxCustomizationActiveAttributeHandler();
		activeHandler.setTimeService(timeService);
	}

	@Override
	public void setRank(final Integer value)
	{
		rankHandler.set(this, value);
	}

	@Override
	public Integer getRank()
	{
		return rankHandler.get(this);
	}

	@Override
	public boolean isActive()
	{
		return activeHandler.get(this).booleanValue();
	}
}
