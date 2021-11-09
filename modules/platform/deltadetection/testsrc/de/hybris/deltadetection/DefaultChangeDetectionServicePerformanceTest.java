/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.deltadetection;

import de.hybris.bootstrap.annotations.PerformanceTest;
import de.hybris.deltadetection.impl.InMemoryChangesCollector;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.testframework.PropertyConfigSwitcher;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Stopwatch;


@PerformanceTest
public class DefaultChangeDetectionServicePerformanceTest extends ServicelayerBaseTest
{


	@Resource
	private ModelService modelService;

	@Resource
	private TypeService typeService;

	@Resource
	private ChangeDetectionService changeDetectionService;
	private final PropertyConfigSwitcher legacyMode = new PropertyConfigSwitcher("persistence.legacy.mode");

	@Before
	public void prepareProducts()
	{
		legacyMode.switchToValue("false");
	}

	@After
	public void tearDown()
	{
		legacyMode.switchBackToDefault();
	}

	@Test
	public void changeConsumptionPerformanceTest()
	{
		final int productsNumber = 1000;
		final Y2YTestDataGenerator.ProductsFixture productsFixture = getProductsFixture(productsNumber);

		final Stopwatch started = Stopwatch.createStarted();
		final InMemoryChangesCollector changesCollector = new InMemoryChangesCollector();

		System.out.println("Starting changes consumption");
		changeDetectionService.collectChangesForType(productsFixture.getComposedType(), productsFixture.getStreamId(),
				changesCollector);
		System.out.println("Changes collected: " + started.elapsed(TimeUnit.MILLISECONDS) + " " + TimeUnit.MILLISECONDS);

		changeDetectionService.consumeChanges(changesCollector.getChanges());
		System.out.println("Changes consumed: " + started.elapsed(TimeUnit.MILLISECONDS) + " " + TimeUnit.MILLISECONDS);
	}

	@Test
	public void testVeryLargeQueryItemsCollection()
	{
		final int productsNumber = 1000000;
		final Y2YTestDataGenerator.ProductsFixture productsFixture = getProductsFixture(productsNumber, 10000, false);

		final Stopwatch started = Stopwatch.createStarted();
		final InMemoryChangesCollector changesCollector = new InMemoryChangesCollector();

		System.out.println("Starting changes consumption");
		changeDetectionService.collectChangesForType(productsFixture.getComposedType(), productsFixture.getStreamId(),
				changesCollector);
		System.out.println("Changes collected: " + started.elapsed(TimeUnit.MILLISECONDS) + " " + TimeUnit.MILLISECONDS);
	}

	private Y2YTestDataGenerator.ProductsFixture getProductsFixture(final int prodNumber)
	{
		return getProductsFixture(prodNumber, prodNumber, true);
	}

	private Y2YTestDataGenerator.ProductsFixture getProductsFixture(final int prodNumber, final int batchSaveSize,
	                                                                final boolean generate)
	{
		final Y2YTestDataGenerator y2YTestDataGenerator = new Y2YTestDataGenerator(modelService, typeService);
		return y2YTestDataGenerator.generateProducts(prodNumber, batchSaveSize, generate);
	}
}
