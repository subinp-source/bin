/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.process.approval.actions;

import de.hybris.platform.b2b.enums.MerchantCheckStatus;
import de.hybris.platform.b2b.model.B2BMerchantCheckResultModel;
import java.util.Collection;


public class B2BMerchantCheckResultHelperImpl implements B2BMerchantCheckResultHelper
{
	@Override
	public Collection<B2BMerchantCheckResultModel> filterResultByMerchantCheckResultStatus(
			final Collection<B2BMerchantCheckResultModel> result, final MerchantCheckStatus status)
	{
		return null;
	}

}
