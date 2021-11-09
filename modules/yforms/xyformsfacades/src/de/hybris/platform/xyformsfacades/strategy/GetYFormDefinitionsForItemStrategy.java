/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.xyformsfacades.strategy;

import de.hybris.platform.xyformsfacades.data.YFormDefinitionData;
import de.hybris.platform.xyformsservices.exception.YFormServiceException;

import java.util.List;


/**
 * Strategy used to get yForm Definitions associated to an hybris item.
 */
public interface GetYFormDefinitionsForItemStrategy<T>
{
	public List<YFormDefinitionData> execute(T code) throws YFormServiceException;
}
