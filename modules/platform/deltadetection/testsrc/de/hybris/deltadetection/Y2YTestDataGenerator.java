/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.deltadetection;


import de.hybris.deltadetection.model.StreamConfigurationContainerModel;
import de.hybris.deltadetection.model.StreamConfigurationModel;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;


public class Y2YTestDataGenerator
{
	private final ModelService modelService;
	private final TypeService typeService;

	public Y2YTestDataGenerator(final ModelService modelService, final TypeService typeService)
	{
		this.modelService = modelService;
		this.typeService = typeService;
	}

	public ProductsFixture generateProducts(final int itemsNumber)
	{
		return generateProducts(itemsNumber, itemsNumber, true);
	}

	public ProductsFixture generateProducts(final int itemsNumber, final int batchSaveSize, final boolean generate)
	{
		if (generate)
		{
			generateProductItems(itemsNumber, batchSaveSize);
		}
		final StreamConfigFixture fixture = getStreamConfigFixture(ProductModel.class);
		return new ProductsFixture(fixture.getStreamId(), fixture.getComposedType());
	}

	public TitlesFixture generateTitles(final int itemsNumber)
	{
		return generateTitles(itemsNumber, itemsNumber, true);
	}

	public TitlesFixture generateTitles(final int itemsNumber, final int batchSaveSize, final boolean generate)
	{
		if (generate)
		{
			generateTitleItems(itemsNumber, batchSaveSize);
		}
		final StreamConfigFixture fixture = getStreamConfigFixture(TitleModel.class);
		return new TitlesFixture(fixture.getStreamId(), fixture.getComposedType());
	}

	private StreamConfigFixture getStreamConfigFixture(final Class<? extends ItemModel> clazz)
	{
		final ComposedTypeModel unitComposedType = typeService.getComposedTypeForClass(clazz);

		final StreamConfigurationContainerModel streamCfgContainer = modelService.create(StreamConfigurationContainerModel.class);
		streamCfgContainer.setId(UUID.randomUUID().toString());
		modelService.save(streamCfgContainer);

		final String streamId = UUID.randomUUID().toString();

		final StreamConfigurationModel streamCfg = modelService.create(StreamConfigurationModel.class);
		streamCfg.setStreamId(streamId);
		streamCfg.setContainer(streamCfgContainer);
		streamCfg.setItemTypeForStream(unitComposedType);
		streamCfg.setWhereClause("not used");
		streamCfg.setInfoExpression("#{getPk()}");

		modelService.save(streamCfg);

		return new StreamConfigFixture(streamId, unitComposedType);
	}

	private void generateTitleItems(final int titlesNumber, final int batchSaveSize)
	{
		final List<TitleModel> titles = new ArrayList<>();

		final Stopwatch started = Stopwatch.createStarted();
		System.out.println("Starting titles generation");
		for (int i = 0; i < titlesNumber; ++i)
		{
			final TitleModel title = modelService.create(TitleModel.class);
			title.setCode(UUID.randomUUID().toString() + new Date().getTime());
			titles.add(title);

			if (i > 0 && batchSaveSize < titlesNumber && i % batchSaveSize == 0)
			{
				modelService.saveAll(titles);
				titles.clear();
				System.out.println("Batch save " + i + " of " + titlesNumber + " elapsed: " + started.elapsed(
						TimeUnit.SECONDS) + " " + TimeUnit.SECONDS);
			}
		}
		modelService.saveAll(titles);
		System.out.println("Saved titles: " + started.elapsed(TimeUnit.SECONDS) + " " + TimeUnit.SECONDS);
	}

	private void generateProductItems(final int productsNumber, final int batchSaveSize)
	{
		final List<ProductModel> products = new ArrayList<>();
		final CatalogModel catalogModel = modelService.create(CatalogModel.class);
		final CatalogVersionModel staged = modelService.create(CatalogVersionModel.class);
		catalogModel.setId("id");
		staged.setCatalog(catalogModel);
		staged.setVersion("staged");
		modelService.saveAll(catalogModel, staged);
		final Stopwatch started = Stopwatch.createStarted();
		System.out.println("Starting products generation");
		for (int i = 0; i < productsNumber; ++i)
		{
			final ProductModel product = modelService.create(ProductModel.class);
			product.setCode(UUID.randomUUID().toString() + new Date().getTime());
			product.setCatalogVersion(staged);
			products.add(product);

			if (i > 0 && batchSaveSize < productsNumber && i % batchSaveSize == 0)
			{
				modelService.saveAll(products);
				products.clear();
				System.out.println("Batch save " + i + " of " + productsNumber + " elapsed: " + started.elapsed(
						TimeUnit.SECONDS) + " " + TimeUnit.SECONDS);
			}
		}
		modelService.saveAll(products);
		System.out.println("Saved products: " + started.elapsed(TimeUnit.SECONDS) + " " + TimeUnit.SECONDS);
	}

	public static class StreamConfigFixture
	{
		private final String streamId;
		private final ComposedTypeModel composedType;

		public StreamConfigFixture(final String streamId, final ComposedTypeModel composedType)
		{
			this.streamId = streamId;
			this.composedType = composedType;
		}

		public String getStreamId()
		{
			return streamId;
		}

		public ComposedTypeModel getComposedType()
		{
			return composedType;
		}
	}

	public static class TitlesFixture extends StreamConfigFixture
	{

		public TitlesFixture(final String streamId, final ComposedTypeModel composedType)
		{
			super(streamId, composedType);
		}
	}

	public static class ProductsFixture extends StreamConfigFixture
	{
		public ProductsFixture(final String streamId, final ComposedTypeModel composedType)
		{
			super(streamId, composedType);
		}
	}
}
