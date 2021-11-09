/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorcms.component.cache.impl;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.exceptions.RestrictionEvaluationException;
import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;
import de.hybris.platform.cms2.model.restrictions.AbstractRestrictionModel;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;


@UnitTest
public class DefaultCmsCacheKeyProviderTest
{

	@Test
	public void handleRestrictionEvaluationExceptionTest()
	{
		final DefaultCmsCacheKeyProvider prov = new DefaultCmsCacheKeyProvider();
		final HttpServletRequest request = null;
		final SimpleCMSComponentModel component = null;
		final AbstractRestrictionModel restriction = null;
		final RestrictionEvaluationException e = null;
		assertThat(prov.handleRestrictionEvaluationException(request, component, restriction, e))
				.isEqualToIgnoringCase(StringUtils.EMPTY);

	}

}
