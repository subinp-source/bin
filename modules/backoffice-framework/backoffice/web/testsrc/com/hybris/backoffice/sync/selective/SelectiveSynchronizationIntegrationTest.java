/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.sync.selective;

import static org.assertj.core.api.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.enums.ArticleApprovalStatus;
import de.hybris.platform.catalog.jalo.SyncAttributeDescriptorConfig;
import de.hybris.platform.catalog.jalo.synchronization.CatalogVersionSyncCronJob;
import de.hybris.platform.catalog.jalo.synchronization.CatalogVersionSyncJob;
import de.hybris.platform.catalog.jalo.synchronization.SynchronizationTestHelper;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.model.collector.DefaultRelatedItemsCollector;
import de.hybris.platform.servicelayer.model.collector.RelatedItemsCollector;
import de.hybris.platform.servicelayer.model.visitor.ItemVisitorRegistry;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class SelectiveSynchronizationIntegrationTest extends ServicelayerTest
{
	static final String PRODUCT_CODE = "product";

	@Resource
	ModelService modelService;
	@Resource
	ProductService productService;
	@Resource
	FlexibleSearchService flexibleSearchService;
	@Resource
	CommonI18NService commonI18NService;
	@Resource
	ItemVisitorRegistry itemVisitorRegistry;

	LanguageHelper languageHelper;
	DefaultRelatedItemsCollector relatedItemsCollector;

	@Before
	public void setUp()
	{
		assertThat(flexibleSearchService.search("SELECT {c.PK} from {Catalog as c}").getTotalCount()).isEqualTo(0);

		languageHelper = new LanguageHelper(commonI18NService, modelService);
		relatedItemsCollector = new DefaultRelatedItemsCollector();
		relatedItemsCollector.setItemVisitorRegistry(itemVisitorRegistry);
	}

	@Test
	public void testProductIsPartiallySynchronized()
	{
		// given
		final String[] attributesNotIncludedInSync =
		{ "ean", "deliveryTime", "endLineNumber", "unit" };
		final SyncContext syncContext = prepareSyncContext(attributesNotIncludedInSync);
		final ProductModel product = createProductWithDependencies(syncContext);

		// when
		executeSync(syncContext, Collections.singletonList(product));

		// then
		final ProductModel synchronizedProduct = getSynchronizedProduct(syncContext);
		assertThatProductIsPartiallySynchronized(product, synchronizedProduct);
	}

	@Test
	public void testProductIsFullySynchronized()
	{
		// given
		final String[] attributesNotIncludedInSync = {};
		final SyncContext syncContext = prepareSyncContext(attributesNotIncludedInSync);
		final ProductModel product = createProductWithDependencies(syncContext);

		// when
		executeSync(syncContext, Collections.singletonList(product));

		// then
		final ProductModel synchronizedProduct = getSynchronizedProduct(syncContext);
		assertThatProductIsFullySynchronized(product, synchronizedProduct);
	}

	SyncContext prepareSyncContext(final String[] attributesNotIncludedInSync)
	{
		final CatalogModel catalogModel = modelService.create(CatalogModel._TYPECODE);
		catalogModel.setId("catalog");
		modelService.save(catalogModel);
		modelService.refresh(catalogModel);

		final CatalogVersionModel catalogVersionModelStaged = modelService.create(CatalogVersionModel._TYPECODE);
		catalogVersionModelStaged.setCatalog(catalogModel);
		catalogVersionModelStaged.setVersion("staged");
		catalogVersionModelStaged.setLanguages(languageHelper.getLanguages());
		modelService.save(catalogVersionModelStaged);
		modelService.refresh(catalogVersionModelStaged);

		final CatalogVersionModel catalogVersionModelOnline = modelService.create(CatalogVersionModel._TYPECODE);
		catalogVersionModelOnline.setCatalog(catalogModel);
		catalogVersionModelOnline.setVersion("online");
		catalogVersionModelOnline.setLanguages(languageHelper.getLanguages());
		modelService.save(catalogVersionModelOnline);
		modelService.refresh(catalogVersionModelOnline);

		final SyncContext syncContext = new SyncContext(catalogModel, catalogVersionModelStaged, catalogVersionModelOnline,
				attributesNotIncludedInSync);
		return syncContext;
	}

	ProductModel createProductWithDependencies(final SyncContext syncContext)
	{
		final UnitModel unit = modelService.create(UnitModel._TYPECODE);
		unit.setCode("unit1");
		unit.setUnitType("a");
		modelService.save(unit);
		modelService.refresh(unit);

		final MediaModel media = modelService.create(MediaModel._TYPECODE);
		media.setCode("m1");
		media.setCatalogVersion(syncContext.getCatalogVersionStaged());
		modelService.save(media);
		modelService.refresh(media);

		final ProductModel product = modelService.create(ProductModel._TYPECODE);
		product.setCode(PRODUCT_CODE);
		product.setCatalogVersion(syncContext.catalogVersionStaged);
		product.setApprovalStatus(ArticleApprovalStatus.APPROVED);
		product.setEan("EAN1");
		product.setSegment("segment1");
		product.setManufacturerAID("123er");
		product.setDeliveryTime(10.);
		product.setPriceQuantity(940.436);
		product.setOrder(12);
		product.setEndLineNumber(14);
		product.setUnit(unit);
		product.setThumbnail(media);
		modelService.save(product);
		modelService.refresh(product);

		return product;
	}

	void executeSync(final SyncContext syncContext, final List<? extends ItemModel> itemsToSynchronize)
	{
		final SynchronizationTestHelper.Builder builder = SynchronizationTestHelper
				.builder(syncContext.getCatalogVersionStaged(), syncContext.getCatalogVersionOnline())
				.configure(job -> configureJobAttributes(syncContext, job));

		itemsToSynchronize.stream().map(this::collectRelatedItems).flatMap(Collection::stream)
				.forEach(item -> builder.add(SynchronizationTestHelper.create(item)));

		builder.build().performSynchronization();
	}

	List<ItemModel> collectRelatedItems(final ItemModel toSynchronize)
	{
		final Map<String, Object> ctx = new HashMap<>();
		ctx.put(RelatedItemsCollector.MAX_RECURSION_DEPTH, 10);
		return relatedItemsCollector.collect(toSynchronize, ctx);
	}

	void configureJobAttributes(final SyncContext syncContext, final CatalogVersionSyncCronJob job)
	{
		for (final SyncAttributeDescriptorConfig config : ((CatalogVersionSyncJob) job.getJob()).getSyncAttributeConfigurations())
		{
			if (Arrays.asList(syncContext.getAttributesNotIncludedInSync()).contains(config.getAttributeDescriptor().getQualifier()))
			{
				config.setIncludedInSync(Boolean.FALSE);
			}
		}
		((CatalogVersionSyncJob) job.getJob()).setSyncLanguages(languageHelper.getJaloLanguages());
	}

	ProductModel getSynchronizedProduct(final SyncContext catalogAndVersions)
	{
		final ProductModel synchronizedProduct = productService.getProductForCode(catalogAndVersions.catalogVersionOnline,
				PRODUCT_CODE);

		return synchronizedProduct;
	}

	void assertThatProductIsPartiallySynchronized(ProductModel product, ProductModel synchronizedProduct)
	{
		assertThat(synchronizedProduct).as("product should exist in online catalog").isNotNull();

		assertThat(product.getCatalogVersion().getCatalog()).isEqualTo(synchronizedProduct.getCatalogVersion().getCatalog());
		assertThat(product.getCode()).isEqualTo(synchronizedProduct.getCode());
		assertThat(product.getApprovalStatus()).isEqualTo(synchronizedProduct.getApprovalStatus());
		assertThat(product.getEan()).isNotEqualTo(synchronizedProduct.getEan());
		assertThat(product.getSegment()).isEqualTo(synchronizedProduct.getSegment());
		assertThat(product.getManufacturerAID()).isEqualTo(synchronizedProduct.getManufacturerAID());
		assertThat(product.getDeliveryTime()).isNotEqualTo(synchronizedProduct.getDeliveryTime());
		assertThat(product.getEndLineNumber()).isNotEqualTo(synchronizedProduct.getEndLineNumber());
		assertThat(synchronizedProduct.getUnit()).isNull();
		assertThat(product.getThumbnail().getCode()).isEqualTo(synchronizedProduct.getThumbnail().getCode());
	}

	void assertThatProductIsFullySynchronized(ProductModel product, ProductModel synchronizedProduct)
	{
		assertThat(synchronizedProduct).as("product should exist in online catalog").isNotNull();

		assertThat(product.getCatalogVersion().getCatalog()).isEqualTo(synchronizedProduct.getCatalogVersion().getCatalog());
		assertThat(product.getCode()).isEqualTo(synchronizedProduct.getCode());
		assertThat(product.getApprovalStatus()).isEqualTo(synchronizedProduct.getApprovalStatus());
		assertThat(product.getEan()).isEqualTo(synchronizedProduct.getEan());
		assertThat(product.getSegment()).isEqualTo(synchronizedProduct.getSegment());
		assertThat(product.getManufacturerAID()).isEqualTo(synchronizedProduct.getManufacturerAID());
		assertThat(product.getDeliveryTime()).isEqualTo(synchronizedProduct.getDeliveryTime());
		assertThat(product.getEndLineNumber()).isEqualTo(synchronizedProduct.getEndLineNumber());
		assertThat(product.getUnit()).isEqualTo(synchronizedProduct.getUnit());
		assertThat(product.getThumbnail().getCode()).isEqualTo(synchronizedProduct.getThumbnail().getCode());
	}

	static class SyncContext
	{
		final CatalogModel catalogModel;
		final CatalogVersionModel catalogVersionStaged;
		final CatalogVersionModel catalogVersionOnline;
		final String[] attributesNotIncludedInSync;

		public SyncContext(final CatalogModel catalogModel, final CatalogVersionModel catalogVersionStaged,
				final CatalogVersionModel catalogVersionOnline, final String[] attributesNotIncludedInSync)
		{
			this.catalogModel = catalogModel;
			this.catalogVersionStaged = catalogVersionStaged;
			this.catalogVersionOnline = catalogVersionOnline;
			this.attributesNotIncludedInSync = attributesNotIncludedInSync;
		}

		public CatalogModel getCatalogModel()
		{
			return catalogModel;
		}

		public CatalogVersionModel getCatalogVersionStaged()
		{
			return catalogVersionStaged;
		}

		public CatalogVersionModel getCatalogVersionOnline()
		{
			return catalogVersionOnline;
		}

		public String[] getAttributesNotIncludedInSync()
		{
			return attributesNotIncludedInSync;
		}
	}
}
