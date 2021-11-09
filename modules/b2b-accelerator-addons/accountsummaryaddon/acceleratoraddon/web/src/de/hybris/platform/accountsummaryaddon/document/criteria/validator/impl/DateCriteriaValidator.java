/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.accountsummaryaddon.document.criteria.validator.impl;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.accountsummaryaddon.constants.AccountsummaryaddonConstants;
import de.hybris.platform.accountsummaryaddon.document.criteria.validator.CriteriaValidator;
import de.hybris.platform.accountsummaryaddon.utils.AccountSummaryAddonUtils;

import java.util.Date;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.ui.Model;


/**
 *
 */
public class DateCriteriaValidator implements CriteriaValidator
{
	private static final Logger LOG = Logger.getLogger(DateCriteriaValidator.class);

	@Override
	public boolean isValid(final String startRange, final String endRange, final Model model)
	{

		Optional<Date> parsedStartRange = Optional.empty();
		Optional<Date> parsedEndRange = Optional.empty();

		if (LOG.isDebugEnabled())
		{
			LOG.debug("validating date ranges");
		}

		if (StringUtils.isNotBlank(startRange))
		{
			parsedStartRange = AccountSummaryAddonUtils.parseDate(startRange, AccountsummaryaddonConstants.DATE_FORMAT_MM_DD_YYYY);
			if (!parsedStartRange.isPresent())
			{
				GlobalMessages.addErrorMessage(model, "text.company.accountsummary.criteria.date.format.from.invalid");
				return false;
			}

		}

		if (StringUtils.isNotBlank(endRange))
		{
			parsedEndRange = AccountSummaryAddonUtils.parseDate(endRange, AccountsummaryaddonConstants.DATE_FORMAT_MM_DD_YYYY);
			if (!parsedEndRange.isPresent())
			{
				GlobalMessages.addErrorMessage(model, "text.company.accountsummary.criteria.date.format.to.invalid");
				return false;
			}
		}

		if (parsedStartRange.isPresent() && parsedEndRange.isPresent() && parsedStartRange.get().after(parsedEndRange.get()))
		{
			GlobalMessages.addErrorMessage(model, "text.company.accountsummary.criteria.date.invalid");
			return false;
		}

		return true;
	}
}