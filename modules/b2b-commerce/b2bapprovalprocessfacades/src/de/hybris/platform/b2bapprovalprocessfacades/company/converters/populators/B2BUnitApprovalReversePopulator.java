/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bapprovalprocessfacades.company.converters.populators;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitData;
import de.hybris.platform.converters.Populator;


/**
 * Reverse populator, to populate a {@link B2BUnitModel} from a {@link B2BUnitData}.
 */
public class B2BUnitApprovalReversePopulator implements Populator<B2BUnitData, B2BUnitModel>
{
	@Override
	public void populate(final B2BUnitData source, final B2BUnitModel target)
	{
		target.setApprovalProcessCode(source.getApprovalProcessCode());
	}
}
