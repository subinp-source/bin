/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chineseprofileservices.customer.impl;

import de.hybris.platform.chineseprofileservices.customer.ChineseCustomerAccountService;
import de.hybris.platform.chineseprofileservices.daos.UserProfileServicesDao;
import de.hybris.platform.chineseprofileservices.data.VerificationData;
import de.hybris.platform.chineseprofileservices.model.MobileNumberVerificationModel;
import de.hybris.platform.chineseprofileservices.strategies.EmailLanguageStrategy;
import de.hybris.platform.chineseprofileservices.strategies.VerificationCodeGenerationStrategy;
import de.hybris.platform.chineseprofileservices.strategies.VerificationCodeSendingStrategy;
import de.hybris.platform.chineseprofileservices.strategies.VerificationCodeStrategy;
import de.hybris.platform.commerceservices.customer.impl.DefaultCustomerAccountService;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.user.daos.UserDao;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Required;

/**
 * Implementation for {@link ChineseCustomerAccountService}. Delivers main functionality for chinese Customer account.
 */
public class DefaultChineseCustomerAccountService extends DefaultCustomerAccountService implements ChineseCustomerAccountService
{
	/**
	 * @deprecated Since 1905. Use {@link VerificationCodeStrategy} instead.
	 */
	@Deprecated(since = "1905", forRemoval= true )
	private VerificationCodeGenerationStrategy verificationCodeGenerationStrategy;
	/**
	 * @deprecated Since 1905. Use {@link VerificationCodeStrategy} instead.
	 */
	@Deprecated(since = "1905", forRemoval= true )
	private VerificationCodeSendingStrategy verificationCodeSendingStrategy;
	private VerificationCodeStrategy verificationCodeStrategy;
	private UserDao userDao;
	private EmailLanguageStrategy emailLanguageStrategy;

	private UserProfileServicesDao userProfileDao;
	private static final String VERIFICATION_EXPIRED_TIME_KEY = "verification.code.time.out";


	@Override
	public String getDefaultEmailLanguage()
	{
		return getEmailLanguageStrategy().getDefaultEmailLanguage();

	}

	@Override
	public Optional<MobileNumberVerificationModel> getMobileNumberVerificationCode(final String mobileNumber)
	{
		return getUserProfileDao().findVerificationCodeByMobileNumber(mobileNumber);
	}

	@Override
	public boolean isVerificationCodeExpired(final Date date)
	{
		return date.before(getVerificationCodeExpiredDate());

	}

	@Override
	public List<MobileNumberVerificationModel> findExpiredVerificationCode(final Date expiredDate)
	{
		return getUserProfileDao().findExpiredVerificationCode(expiredDate);
	}

	@Override
	public Date getVerificationCodeExpiredDate()
	{
		final long timeout = getConfigurationService().getConfiguration().getInt(VERIFICATION_EXPIRED_TIME_KEY) * 1000L;

		return new Date(System.currentTimeMillis() - timeout);
	}

	@Override
	public String generateVerificationCode()
	{
		return getVerificationCodeStrategy().generate();
	}

	@Override
	public void sendVerificationCode(final VerificationData data)
	{
		getVerificationCodeStrategy().send(data.getMobileNumber(), data.getVerificationCode());
	}

	@Override
	public void sendVerificationCode(final String mobileNumber, final String verificationCode)
	{
		getVerificationCodeStrategy().send(mobileNumber, verificationCode);
	}

	@Override
	public void updateMobileNumber(final CustomerModel customerModel)
	{
		getModelService().save(customerModel);
	}

	@Override
	public Optional<CustomerModel> getCustomerForMobileNumber(final String mobileNumber)
	{
		final CustomerModel customer = (CustomerModel) userDao.findUserByUID(mobileNumber);
		return Objects.nonNull(customer) ? Optional.of(customer) : Optional.empty();
	}

	/**
	 * @deprecated Since 1905. Use {@link VerificationCodeStrategy} instead.
	 */
	@Deprecated(since = "1905", forRemoval= true )
	protected VerificationCodeGenerationStrategy getVerificationCodeGenerationStrategy()
	{
		return verificationCodeGenerationStrategy;
	}

	/**
	 * @deprecated Since 1905. Use {@link VerificationCodeStrategy} instead.
	 */
	@Deprecated(since = "1905", forRemoval= true )
	@Required
	public void setVerificationCodeGenerationStrategy(final VerificationCodeGenerationStrategy verificationCodeGenerationStrategy)
	{
		this.verificationCodeGenerationStrategy = verificationCodeGenerationStrategy;
	}

	/**
	 * @deprecated Since 1905. Use {@link VerificationCodeStrategy} instead.
	 */
	@Deprecated(since = "1905", forRemoval= true )
	protected VerificationCodeSendingStrategy getVerificationCodeSendingStrategy()
	{
		return verificationCodeSendingStrategy;
	}

	/**
	 * @deprecated Since 1905. Use {@link VerificationCodeStrategy} instead.
	 */
	@Deprecated(since = "1905", forRemoval= true )
	@Required
	public void setVerificationCodeSendingStrategy(final VerificationCodeSendingStrategy verificationCodeSendingStrategy)
	{
		this.verificationCodeSendingStrategy = verificationCodeSendingStrategy;
	}

	protected UserDao getUserDao()
	{
		return userDao;
	}

	@Required
	public void setUserDao(final UserDao userDao)
	{
		this.userDao = userDao;
	}

	protected VerificationCodeStrategy getVerificationCodeStrategy()
	{
		return verificationCodeStrategy;
	}

	@Required
	public void setVerificationCodeStrategy(final VerificationCodeStrategy verificationCodeStrategy)
	{
		this.verificationCodeStrategy = verificationCodeStrategy;
	}

	@Required
	public void setUserProfileDao(final UserProfileServicesDao userProfileDao)
	{
		this.userProfileDao = userProfileDao;
	}

	public UserProfileServicesDao getUserProfileDao()
	{
		return this.userProfileDao;
	}

	public EmailLanguageStrategy getEmailLanguageStrategy()
	{
		return this.emailLanguageStrategy;
	}

	@Required
	public void setEmailLanguageStrategy(final EmailLanguageStrategy emailLanguageStrategy)
	{
		this.emailLanguageStrategy = emailLanguageStrategy;

	}

}
