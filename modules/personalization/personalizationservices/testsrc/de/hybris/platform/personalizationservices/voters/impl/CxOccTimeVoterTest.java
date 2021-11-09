/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.personalizationservices.voters.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.personalizationservices.action.CxActionResultService;
import de.hybris.platform.personalizationservices.configuration.CxConfigurationService;
import de.hybris.platform.personalizationservices.model.CxResultsModel;
import de.hybris.platform.personalizationservices.model.config.CxConfigModel;
import de.hybris.platform.personalizationservices.occ.CxOccAttributesStrategy;
import de.hybris.platform.personalizationservices.occ.voters.impl.CxOccTimeVoter;
import de.hybris.platform.personalizationservices.service.CxCatalogService;
import de.hybris.platform.personalizationservices.stub.MockTimeService;
import de.hybris.platform.personalizationservices.voters.Vote;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


@UnitTest
public class CxOccTimeVoterTest
{
	public static final Long OCC_TTL = 600000L;
	public static final Long CURRENT_TIME = 1000000L;

	@Mock
	private CxConfigurationService cxConfigurationService;

	@Mock
	private HttpServletRequest request;

	@Mock
	private CxOccAttributesStrategy cxOccAttributesStrategy;

	@Mock
	private CxActionResultService cxActionResultService;

	@Mock
	private CxCatalogService cxCatalogService;

	@Mock
	private CxConfigModel cxConfigModel;

	@Mock
	private CatalogVersionModel cvModel;

	@Mock
	private CxResultsModel cxResults;

	@Mock
	private UserService userService;

	private final TimeService timeService = new MockTimeService();
	private final CxOccTimeVoter voter = new CxOccTimeVoter();


	@Before
	public void setupMocks()
	{
		MockitoAnnotations.initMocks(this);

		given(cxConfigurationService.getConfiguration()).willReturn(Optional.of(cxConfigModel));
		given(cxCatalogService.getConfiguredCatalogVersions()).willReturn(Collections.singletonList(cvModel));
		given(cxActionResultService.getCxResults(Mockito.any(), Mockito.any())).willReturn(Optional.of(cxResults));
		given(cxConfigurationService.getConfiguration()).willReturn(Optional.of(cxConfigModel));
		given(cxConfigModel.getOccTTL()).willReturn(OCC_TTL);

		timeService.setCurrentTime(new Date(CURRENT_TIME));

		voter.setTimeService(timeService);
		voter.setCxOccAttributesStrategy(cxOccAttributesStrategy);
		voter.setCxConfigurationService(cxConfigurationService);
		voter.setCxActionResultService(cxActionResultService);
		voter.setUserService(userService);
		voter.setCxCatalogService(cxCatalogService);
	}


	@Test
	public void shouldNotRecalculateWhenTTLNotPassed()
	{
		final Long cookieTime = CURRENT_TIME - OCC_TTL + 1;
		final Optional<Long> cookieTimeOptional = Optional.of(cookieTime);
		given(cxOccAttributesStrategy.readPersonalizationCalculationTime(Mockito.any())).willReturn(cookieTimeOptional);

		final Vote vote = voter.getVote(request);

		assertNotNull(vote);
		assertTrue(vote.getRecalculateActions().isEmpty());
	}

	@Test
	public void shouldRecalculateWhenTTLPassedAndNoResultFound()
	{
		final Long cookieTime = CURRENT_TIME - OCC_TTL - 1;
		final Optional<Long> cookieTimeOptional = Optional.of(cookieTime);

		given(cxOccAttributesStrategy.readPersonalizationCalculationTime(Mockito.any())).willReturn(cookieTimeOptional);
		given(cxActionResultService.getCxResults(Mockito.any(), Mockito.any())).willReturn(Optional.empty());

		final Vote vote = voter.getVote(request);

		assertNotNull(vote);
		assertEquals(1, vote.getRecalculateActions().size());
	}


	@Test
	public void shouldNotRecalculateWhenTTLPassedAndFreshResultFound()
	{
		final Long cookieTime = CURRENT_TIME - OCC_TTL - 1;
		final Optional<Long> cookieTimeOptional = Optional.of(cookieTime);
		given(cxOccAttributesStrategy.readPersonalizationCalculationTime(Mockito.any())).willReturn(cookieTimeOptional);
		given(cxResults.getCalculationTime()).willReturn(new Date(CURRENT_TIME - OCC_TTL + 1));

		final Vote vote = voter.getVote(request);

		assertNotNull(vote);
		assertTrue(vote.getRecalculateActions().isEmpty());
	}

	@Test
	public void shouldRecalculateWhenTTLPassedAndOldResultFound()
	{
		final Long cookieTime = CURRENT_TIME - OCC_TTL - 1;
		final Optional<Long> cookieTimeOptional = Optional.of(cookieTime);
		given(cxOccAttributesStrategy.readPersonalizationCalculationTime(Mockito.any())).willReturn(cookieTimeOptional);
		given(cxResults.getCalculationTime()).willReturn(new Date(CURRENT_TIME - OCC_TTL - 1));

		final Vote vote = voter.getVote(request);

		assertNotNull(vote);
		assertEquals(1, vote.getRecalculateActions().size());
	}

	@Test
	public void shouldRecalculateWhenNoLastCalcTimeAndOldResultFound()
	{
		final Optional<Long> cookieTimeOptional = Optional.empty();
		given(cxOccAttributesStrategy.readPersonalizationCalculationTime(Mockito.any())).willReturn(cookieTimeOptional);
		given(cxResults.getCalculationTime()).willReturn(new Date(CURRENT_TIME - OCC_TTL - 1));

		final Vote vote = voter.getVote(request);

		assertNotNull(vote);
		assertEquals(1, vote.getRecalculateActions().size());
	}

	@Test
	public void shouldRecalculateWhenNoLastCalcTimeAndNoResultFound()
	{
		final Optional<Long> cookieTimeOptional = Optional.empty();
		given(cxOccAttributesStrategy.readPersonalizationCalculationTime(Mockito.any())).willReturn(cookieTimeOptional);
		given(cxActionResultService.getCxResults(Mockito.any(), Mockito.any())).willReturn(Optional.empty());

		final Vote vote = voter.getVote(request);

		assertNotNull(vote);
		assertEquals(1, vote.getRecalculateActions().size());
	}

}
