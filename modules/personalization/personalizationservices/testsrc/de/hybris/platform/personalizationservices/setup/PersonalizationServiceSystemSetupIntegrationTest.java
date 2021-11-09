/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.personalizationservices.setup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.personalizationservices.RecalculateAction;
import de.hybris.platform.personalizationservices.enums.CxUserType;
import de.hybris.platform.personalizationservices.model.config.CxConfigModel;
import de.hybris.platform.personalizationservices.model.config.CxPeriodicVoterConfigModel;
import de.hybris.platform.personalizationservices.model.config.CxUrlVoterConfigModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.impex.impl.ClasspathImpExResource;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;


@IntegrationTest
public class PersonalizationServiceSystemSetupIntegrationTest extends ServicelayerTransactionalTest
{
	private static final String CONFIG_WITH_URL_VOTERS = "testConfig";
	private static final String CONFIG_WITH_URL_VOTER_AND_IGNORE_ANONYMOUS_RECALC = "testConfig1";
	private static final String CONFIG_WITH_URL_VOTER_AND_IGNORE_OTHER_ACTION_FOR_ANONYMOUS = "testConfig2";
	private static final String CONFIG_WITH_ANONYMOUS_RECALCULATION = "testConfig3";
	private static final String CONFIG_WITH_PERIODIC_VOTER = "testConfig4";
	private static final String CONFIG_WITH_NEGATIVE_MIN_REQUEST_NUMBER = "testConfig5";
	private static final String CONFIG_WITH_NEGATIVE_MIN_TIME = "testConfig6";
	private static final String CONFIG_WITHOUT_ANONYMOUS_ACTIONS = "testConfig7";

	@Resource
	private FlexibleSearchService flexibleSearchService;

	@Resource
	private PersonalizationServicesSystemSetup personalizationServicesSystemSetup;

	@Test
	public void updateCalculationConfigTest() throws Exception {

		//given
		importData(new ClasspathImpExResource("/personalizationservices/test/testdata_oldcxconfig.impex", "UTF-8"));

		//when
		personalizationServicesSystemSetup.updateCalculationConfig();

		//then
		CxConfigModel config = getConfiguration(CONFIG_WITH_URL_VOTERS);
		verifyConfigWithUrlVoter(config);

		config = getConfiguration(CONFIG_WITH_URL_VOTER_AND_IGNORE_ANONYMOUS_RECALC);
		verifyConfigWithUrlVoterAndIngnoreAnonymous(config);

		config = getConfiguration(CONFIG_WITH_URL_VOTER_AND_IGNORE_OTHER_ACTION_FOR_ANONYMOUS);
		verifyConfigWithUrlVoterAndIngnoreAnonymous(config);

		config = getConfiguration(CONFIG_WITH_ANONYMOUS_RECALCULATION);
		verifyConfigWithAnonymousRecalculation(config);

		config = getConfiguration(CONFIG_WITH_PERIODIC_VOTER);
		verifyConfigWithPeriodicVoter(config);

		config = getConfiguration(CONFIG_WITH_NEGATIVE_MIN_REQUEST_NUMBER);
		verifyConfigWithNegativeMinRequestNumber(config);

		config = getConfiguration(CONFIG_WITH_NEGATIVE_MIN_TIME);
		verifyConfigWithNegativeMinTime(config);

		config = getConfiguration(CONFIG_WITHOUT_ANONYMOUS_ACTIONS);
		verifyConfigWithoutAnonymousActions(config);
	}

	protected CxConfigModel getConfiguration(final String code) {
		final CxConfigModel configModel = new CxConfigModel();
		configModel.setCode(code);
		return flexibleSearchService.getModelByExample(configModel);
	}


	private void verifyConfigWithUrlVoter(final CxConfigModel config)
	{
		assertNotNull(config);
		final List<CxUrlVoterConfigModel> urlConfigs = config.getUrlVoterConfigs();
		assertNotNull(urlConfigs);
		assertEquals(2, urlConfigs.size());
		assertEquals("default",urlConfigs.get(0).getCode());
		assertEquals(CxUserType.ANONYMOUS,urlConfigs.get(0).getUserType());
		assertEquals("checkout",urlConfigs.get(1).getCode());
		assertEquals(CxUserType.ALL,urlConfigs.get(1).getUserType());
	}

	private void verifyConfigWithUrlVoterAndIngnoreAnonymous(final CxConfigModel config)
	{
		assertNotNull(config);
		final List<CxUrlVoterConfigModel> urlConfigs = config.getUrlVoterConfigs();
		assertNotNull(urlConfigs);
		assertEquals(1, urlConfigs.size());
		assertEquals("default",urlConfigs.get(0).getCode());
		assertEquals(CxUserType.REGISTERED,urlConfigs.get(0).getUserType());
	}

	private void verifyConfigWithAnonymousRecalculation(final CxConfigModel config)
	{
		assertNotNull(config);
		assertNotNull(config.getPeriodicVoterConfigs());
		assertEquals(1,config.getPeriodicVoterConfigs().size());
		final CxPeriodicVoterConfigModel periodicConfig = config.getPeriodicVoterConfigs().iterator().next();
		assertEquals("anonymousPeriodicVoter",periodicConfig.getCode());
		assertEquals(Integer.valueOf(0),periodicConfig.getUserMinRequestNumber());
		assertEquals(Long.valueOf(6000),periodicConfig.getUserMinTime());
		assertNotNull(periodicConfig.getActions());
		assertEquals(1,periodicConfig.getActions().size());
		assertEquals(RecalculateAction.LOAD.toString(), periodicConfig.getActions().iterator().next());

		assertTrue(CollectionUtils.isEmpty(config.getAnonymousUserActions()));
		assertNull(config.getAnonymousUserMinRequestNumber());
		assertNull(config.getAnonymousUserMinTime());
	}
	private void verifyConfigWithPeriodicVoter(final CxConfigModel config)
	{
		assertNotNull(config);
		assertTrue(CollectionUtils.isNotEmpty(config.getPeriodicVoterConfigs()));

		assertEquals(Integer.valueOf(0),config.getAnonymousUserMinRequestNumber());
		assertEquals(Long.valueOf(6000),config.getAnonymousUserMinTime());
		assertNotNull(config.getAnonymousUserActions());
		assertEquals(1,config.getAnonymousUserActions().size());
		assertEquals(RecalculateAction.LOAD.toString(), config.getAnonymousUserActions().iterator().next());
	}

	private void verifyConfigWithNegativeMinRequestNumber(final CxConfigModel config)
	{
		assertNotNull(config);
		assertTrue(CollectionUtils.isEmpty(config.getPeriodicVoterConfigs()));

		assertEquals(Integer.valueOf(-1),config.getAnonymousUserMinRequestNumber());
		assertEquals(Long.valueOf(0),config.getAnonymousUserMinTime());
		assertNotNull(config.getAnonymousUserActions());
		assertEquals(1,config.getAnonymousUserActions().size());
		assertEquals(RecalculateAction.LOAD.toString(), config.getAnonymousUserActions().iterator().next());
	}

	private void verifyConfigWithNegativeMinTime(final CxConfigModel config)
	{
		assertNotNull(config);
		assertTrue(CollectionUtils.isEmpty(config.getPeriodicVoterConfigs()));

		assertEquals(Integer.valueOf(0),config.getAnonymousUserMinRequestNumber());
		assertEquals(Long.valueOf(-1),config.getAnonymousUserMinTime());
		assertNotNull(config.getAnonymousUserActions());
		assertEquals(1,config.getAnonymousUserActions().size());
		assertEquals(RecalculateAction.LOAD.toString(), config.getAnonymousUserActions().iterator().next());
	}

	private void verifyConfigWithoutAnonymousActions(final CxConfigModel config)
	{
		assertNotNull(config);
		assertTrue(CollectionUtils.isEmpty(config.getPeriodicVoterConfigs()));

		assertEquals(Integer.valueOf(0),config.getAnonymousUserMinRequestNumber());
		assertEquals(Long.valueOf(6000),config.getAnonymousUserMinTime());
		assertTrue(CollectionUtils.isEmpty(config.getAnonymousUserActions()));
	}
}
