/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.ycommercewebservicestest.setup;

import de.hybris.platform.commerceservices.dataimport.impl.CoreDataImportService;
import de.hybris.platform.commerceservices.dataimport.impl.SampleDataImportService;
import de.hybris.platform.commerceservices.setup.AbstractSystemSetup;
import de.hybris.platform.commerceservices.setup.data.ImportData;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.initialization.SystemSetupParameter;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


//@SystemSetup(extension = YcommercewebservicestestConstants.EXTENSIONNAME)
public class YCommerceWebServicesTestSetup extends AbstractSystemSetup
{
	public static final String WS_INTEGRATION = "wsIntegrationTest";
	public static final String WS_B2B = "wsB2BTest";
	public static final String WS_TEST = "wsTest";
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(YCommerceWebServicesTestSetup.class);
	private CoreDataImportService coreDataImportService;
	private SampleDataImportService sampleDataImportService;

	@Override
	public List<SystemSetupParameter> getInitializationOptions()
	{
		final List<SystemSetupParameter> params = new ArrayList<>();

		params.add(createBooleanSystemSetupParameter(CoreDataImportService.IMPORT_CORE_DATA, "Import Core Data", true));
		params.add(createBooleanSystemSetupParameter(SampleDataImportService.IMPORT_SAMPLE_DATA, "Import Sample Data", true));
		params.add(
				createBooleanSystemSetupParameter(CoreDataImportService.ACTIVATE_SOLR_CRON_JOBS, "Activate Solr Cron Jobs", true));

		return params;
	}

	//	@SystemSetup(type = SystemSetup.Type.PROJECT, process = SystemSetup.Process.ALL)
	public void createProjectData(final SystemSetupContext context)
	{
		final boolean enableWsIntegrationTest = isEnableWsIntegrationTest();
		final List<ImportData> importData = new ArrayList<>();

		final List<String> contentCatalogNames = createContentCatalogNames(enableWsIntegrationTest);
		final List<String> storeNames = createStoreNames(enableWsIntegrationTest);

		final ImportData wsIntegrationImportData = new ImportData();
		wsIntegrationImportData.setProductCatalogName(WS_TEST);
		wsIntegrationImportData.setContentCatalogNames(contentCatalogNames);
		wsIntegrationImportData.setStoreNames(storeNames);
		importData.add(wsIntegrationImportData);

		getCoreDataImportService().execute(this, context, importData);

		loadOptionalSampleData(storeNames);

		getSampleDataImportService().execute(this, context, importData);
		getSetupImpexService().importImpexFile("/ycommercewebservicestest/import/sampledata/user-orders.impex", true, false);
		getSetupImpexService().importImpexFile("/ycommercewebservicestest/import/sampledata/paymentmodes.impex", true, false);

		if (enableWsIntegrationTest)
		{
			loadIntegrationData();
		}

		loadMultiDimensionalData();
	}

	protected boolean isEnableWsIntegrationTest()
	{
		return Config.getBoolean("ycommercewebservicestest.enableWsIntegrationTest", false);
	}

	protected List<String> createContentCatalogNames(final boolean enableWsIntegrationTest)
	{
		final List<String> catalogNames = Arrays.asList(WS_TEST, WS_INTEGRATION, WS_B2B);
		return catalogNames.stream().filter(s -> enableWsIntegrationTest || !StringUtils.equals(s, WS_INTEGRATION))
				.collect(Collectors.toList());
	}

	protected List<String> createStoreNames(final boolean enableWsIntegrationTest)
	{
		final List<String> storeNames = Arrays.asList(WS_TEST, WS_INTEGRATION, WS_B2B);
		return storeNames.stream().filter(s -> enableWsIntegrationTest || !StringUtils.equals(s, WS_INTEGRATION))
				.collect(Collectors.toList());
	}

	protected void loadOptionalSampleData(final List<String> storeNames)
	{
		final List<String> extensionNames = Registry.getCurrentTenant().getTenantSpecificExtensionNames();
		if (extensionNames.contains("acceleratorwebservicesaddon") || extensionNames.contains("acceleratorocc"))
		{
			storeNames.forEach(store -> {
				getSetupImpexService().importImpexFile(
						String.format("/ycommercewebservicestest/import/acceleratorwebservicesaddon/%s/solr.impex", store), true, true);
				getSetupImpexService().importImpexFile(
						String.format("/ycommercewebservicestest/import/acceleratorwebservicesaddon/%s/site.impex", store), true, true);
			});
		}

		if (extensionNames.contains("yacceleratorfulfilmentprocess"))
		{
			getSetupImpexService().importImpexFile("/ycommercewebservicestest/import/orderprocess/store.impex", true, true);
		}

		if (extensionNames.contains("yacceleratorordermanagement"))
		{
			getSetupImpexService().importImpexFile(
					"/ycommercewebservicestest/import/ordermanagement/projectdata-dynamic-business-process-order.impex", true);
			getSetupImpexService().importImpexFile(
					"/ycommercewebservicestest/import/ordermanagement/projectdata-dynamic-business-process-consignment.impex", true);
			getSetupImpexService().importImpexFile(
					"/ycommercewebservicestest/import/ordermanagement/projectdata-dynamic-business-process-return.impex", true);
			getSetupImpexService().importImpexFile(
					"/ycommercewebservicestest/import/ordermanagement/projectdata-dynamic-business-process-sendReturnLabelEmail.impex",
					true);
			getSetupImpexService().importImpexFile("/ycommercewebservicestest/import/ordermanagement/store.impex", true, true);
		}
	}

	public void initializePromotionEngineModule()
	{
		getSetupImpexService()
				.importImpexFile("/ycommercewebservicestest/import/promotionengine/promotionsmodule.impex", true, false);
	}

	protected void loadIntegrationData()
	{
		final List<String> extensionNames = Registry.getCurrentTenant().getTenantSpecificExtensionNames();
		if (extensionNames.contains("cissampledata"))
		{
			getSetupImpexService().importImpexFile("/ycommercewebservicestest/import/integration/cis-integration-data.impex", true);
		}

		if (extensionNames.contains("omssampledata"))
		{
			getSetupImpexService().importImpexFile("/ycommercewebservicestest/import/integration/oms-integration-data.impex", true);
		}
	}

	protected void loadMultiDimensionalData()
	{
		getSetupImpexService().importImpexFile(
				"/ycommercewebservicestest/import/sampledata/productCatalogs/wsTestProductCatalog/dimension-categories.impex", true,
				false);
		getSetupImpexService().importImpexFile(
				"/ycommercewebservicestest/import/sampledata/productCatalogs/wsTestProductCatalog/dimension-products.impex", true,
				false);
		getSetupImpexService().importImpexFile(
				"/ycommercewebservicestest/import/sampledata/productCatalogs/wsTestProductCatalog/dimension-products-prices.impex",
				true, false);
		getSetupImpexService().importImpexFile(
				"/ycommercewebservicestest/import/sampledata/productCatalogs/wsTestProductCatalog/dimension-products-stock-levels.impex",
				true, false);

		getSetupSyncJobService().executeCatalogSyncJob(String.format("%sProductCatalog", WS_TEST));
		getSetupSolrIndexerService().executeSolrIndexerCronJob(String.format("%sIndex", WS_TEST), true);
	}

	public CoreDataImportService getCoreDataImportService()
	{
		return coreDataImportService;
	}

	@Required
	public void setCoreDataImportService(final CoreDataImportService coreDataImportService)
	{
		this.coreDataImportService = coreDataImportService;
	}

	public SampleDataImportService getSampleDataImportService()
	{
		return sampleDataImportService;
	}

	@Required
	public void setSampleDataImportService(final SampleDataImportService sampleDataImportService)
	{
		this.sampleDataImportService = sampleDataImportService;
	}
}
