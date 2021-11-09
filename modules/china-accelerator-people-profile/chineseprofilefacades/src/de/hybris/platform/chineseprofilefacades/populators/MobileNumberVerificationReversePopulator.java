/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
 package de.hybris.platform.chineseprofilefacades.populators;

import de.hybris.platform.chineseprofilefacades.data.MobileNumberVerificationData;
import de.hybris.platform.chineseprofileservices.model.MobileNumberVerificationModel;
import de.hybris.platform.converters.Populator;

import org.springframework.util.Assert;


/**
 * Populating from {@link MobileNumberVerificationData} to {@link MobileNumberVerificationModel}.
 */
public class MobileNumberVerificationReversePopulator
		implements Populator<MobileNumberVerificationData,MobileNumberVerificationModel>
{
	@Override
	public void populate(final MobileNumberVerificationData source, final MobileNumberVerificationModel target)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");
		target.setMobileNumber(source.getMobileNumber());
		target.setVerificationCode(source.getVerificationCode());
	}

}
