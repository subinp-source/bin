/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.accountsummaryaddon.document.criteria.validator;

import org.springframework.ui.Model;


/**
 *
 */
public interface CriteriaValidator
{

	boolean isValid(final String startValue, final String endValue, final Model model);
}
