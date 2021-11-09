/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.y2ysync.model;

import static org.assertj.core.api.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.y2ysync.services.SyncConfigService;

import java.util.Collections;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class Y2YStreamConfigurationContainerPrepareTest extends ServicelayerTransactionalTest
{
	@Resource
	private ModelService modelService;

	@Resource
	private SyncConfigService syncConfigService;
	private Y2YStreamConfigurationContainerModel testContainer;

	@Before
	public void setUp() throws Exception
	{
		testContainer = syncConfigService.createStreamConfigurationContainer("testContainer");
	}

	@Test
	public void shouldGenerateFeedAndPool()
	{
		testContainer = syncConfigService.createStreamConfigurationContainer("testContainer");

		modelService.save(testContainer);

		assertThat(testContainer.getFeed()).isEqualTo("testContainer_feed");
		assertThat(testContainer.getPool()).isEqualTo("testContainer_pool");
	}

	@Test
	public void shouldNotGenerateFeedAndPoolIfExplicitlySet()
	{
		testContainer = syncConfigService.createStreamConfigurationContainer("testContainer");
		testContainer.setFeed("customFeed");
		testContainer.setPool("customPool");

		modelService.save(testContainer);

		assertThat(testContainer.getFeed()).isEqualTo("customFeed");
		assertThat(testContainer.getPool()).isEqualTo("customPool");
	}

}
