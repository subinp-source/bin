/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.modals.builders;

import de.hybris.platform.audit.TypeAuditReportConfig;
import de.hybris.platform.audit.view.AuditViewService;
import de.hybris.platform.audit.view.impl.ReportView;
import de.hybris.platform.auditreport.model.AuditReportTemplateModel;
import de.hybris.platform.auditreport.service.ReportConversionData;
import de.hybris.platform.auditreport.service.ReportViewConverterStrategy;
import de.hybris.platform.commons.renderer.RendererService;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.C2LItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.integrationbackoffice.widgets.modals.utility.ModalUtils;
import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.user.UserService;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;

public abstract class AbstractAuditReportBuilder implements AuditReportBuilder
{
	protected final Logger LOG = Log.getLogger(AbstractAuditReportBuilder.class);

	// Maybe overwritten in subtype
	private final Long PAYLOAD_TIME_THRESHOLD = 3000L;
	private final String TEMPLATE_NAME = "IntegrationObjectAuditReportTemplate";
	private final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	// have to be specified in subtype's spring.xml
	private String configName;
	private boolean isDownload = false;

	@Resource
	private AuditViewService auditViewService;
	@Resource
	private RendererService rendererService;
	@Resource
	private CommonI18NService commonI18NService;
	@Resource
	private UserService userService;
	@Resource
	private List<ReportViewConverterStrategy> reportViewConverterStrategies;
	private ItemModel selectedItemModel;

	///////////////////////////////
	// GLOBAL ACCESSORS/MUTATORS //
	///////////////////////////////
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	public void setAuditViewService(final AuditViewService auditViewService)
	{
		this.auditViewService = auditViewService;
	}

	public void setRendererService(final RendererService rendererService)
	{
		this.rendererService = rendererService;
	}

	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

	public void setSelectedModel(final ItemModel selectedItemModel)
	{
		this.selectedItemModel = selectedItemModel;
	}

	public void setReportViewConverterStrategies(final List<ReportViewConverterStrategy> reportViewConverterStrategies)
	{
		this.reportViewConverterStrategies = reportViewConverterStrategies;
	}

	public AuditViewService getAuditViewService()
	{
		return auditViewService;
	}

	public RendererService getRendererService()
	{
		return rendererService;
	}

	public CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	public UserService getUserService()
	{
		return userService;
	}

	public List<ReportViewConverterStrategy> getReportViewConverterStrategies()
	{
		return this.reportViewConverterStrategies;
	}

	public ItemModel getSelectedModel()
	{
		return this.selectedItemModel;
	}

	public String getConfigName()
	{
		return this.configName;
	}

	public void setConfigName(final String configName)
	{
		this.configName = configName;
	}

	public boolean getIsDownload()
	{
		return isDownload;
	}

	public void setIsDownload(final boolean download)
	{
		isDownload = download;
	}

	public abstract void traversePayload(Map map);

	public abstract String getDownloadFileName();

	///////////////////////////////
	//     FUNCTIONAL METHODS    //
	///////////////////////////////

	public Map<String, InputStream> generateAuditReport(final ItemModel rootTypeModel)
	{
		this.setSelectedModel(rootTypeModel);
		final TypeAuditReportConfig.Builder builder = TypeAuditReportConfig.builder()
		                                                                   .withConfigName(this.getConfigName())
		                                                                   .withRootTypePk(this.getSelectedModel().getPk())
		                                                                   .withFullReport();
		final List<LanguageModel> languageModelList = Collections.singletonList(commonI18NService.getCurrentLanguage());
		if (CollectionUtils.isNotEmpty(languageModelList))
		{
			builder.withLangIsoCodes(languageModelList.stream().map(C2LItemModel::getIsocode).toArray(String[]::new));
		}
		final Stream<ReportView> report = auditViewService.getViewOn(builder.build());
		final Stream<ReportView> newReport = this.filterReportView(report);
		final Map<String, InputStream> files = this.evaluateStrategiesToStreams(newReport,
				this.populateReportGenerationContext());
		if (this.isDownload)
		{
			this.downloadAuditReport(files);
		}
		return files;
	}

	public void downloadAuditReport(final Map<String, InputStream> files)
	{
		for (final InputStream inputStream : files.values())
		{
			ModalUtils.executeReportDownload(inputStream, "application/html", this.getDownloadFileName());
		}
	}

	public Stream<ReportView> filterReportView(final Stream<ReportView> report)
	{
		final List<ReportView> reportViewList = report.collect(Collectors.toList());
		final List<ReportView> newReportViewList = new ArrayList<>();
		String nextChangingUser;
		long nextTimestamp;
		for (int index = 0; index < reportViewList.size(); index++)
		{
			// Inside DefaultAuditViewService guaranteed the Date is created from processItemsCache so here it can't be null.
			// By default the payloads are chronological.
			final long currentTimestamp = reportViewList.get(index).getTimestamp().getTime();
			if (index < reportViewList.size() - 1)
			{
				nextTimestamp = reportViewList.get(index + 1).getTimestamp().getTime();
				nextChangingUser = reportViewList.get(index + 1).getChangingUser();
			}
			else
			{
				nextTimestamp = Long.MAX_VALUE;
				nextChangingUser = "";
			}
			if (!reportViewList.get(index).getChangingUser().equals(nextChangingUser) ||
					nextTimestamp - currentTimestamp > PAYLOAD_TIME_THRESHOLD)
			{
				newReportViewList.add(reportViewList.get(index));
			}
		}
		addComposedTypeAndRemoveRedundantInfo(newReportViewList);
		return newReportViewList.stream();
	}

	public void addComposedTypeAndRemoveRedundantInfo(final List<ReportView> reportViewList)
	{
		for (final ReportView reportView : reportViewList)
		{
			traversePayload(reportView.getPayload());
		}
	}

	public Map<String, InputStream> evaluateStrategiesToStreams(final Stream<ReportView> reports,
	                                                            final Map<String, Object> context)
	{
		final Map<String, InputStream> files = new HashMap();
		this.getReportViewConverterStrategies().forEach(strategy -> {
			final Collection<ReportConversionData> conversionResult = strategy.convert(reports, context);
			if (CollectionUtils.isNotEmpty(conversionResult))
			{
				conversionResult.forEach(file -> {
					if (file.getStream() != null)
					{
						mapStreamsToFile(files, file);
					}
				});
			}
		});
		return files;
	}

	public void mapStreamsToFile(final Map<String, InputStream> files, final ReportConversionData file)
	{
		try (final InputStream previousFileData = files.put(file.getName(), file.getStream()))
		{
			if (previousFileData != null)
			{
				LOG.warn("Another file uses the same name: \"{}\". The content of the previous file was discarded.",
						file.getName());
			}
		}
		catch (final IOException e)
		{
			LOG.error("There was a problem with the InputStream", e);
		}
	}


	public Map<String, Object> populateReportGenerationContext()
	{
		final Map<String, Object> context = new HashMap<>();
		context.put("rootItem", this.getSelectedModel());
		context.put("configName", this.getConfigName());
		final String objectLabel = this.getDownloadFileName();
		final String formattedData = LocalDateTime.now().format(DateTimeFormatter.ofPattern(this.DATE_FORMAT));
		final String reportName = String.format("%s-Audit Log-(%s)", objectLabel, formattedData);
		context.put("reportId", reportName);
		context.put("currentUser", userService.getCurrentUser());
		final AuditReportTemplateModel reportTemplate = this.getAuditReportTemplate();
		if (reportTemplate == null)
		{
			LOG.error("Template file loading error.");
			return null;
		}
		context.put("template", reportTemplate);
		return context;
	}

	public AuditReportTemplateModel getAuditReportTemplate()
	{
		// report template should be essential data.
		final AuditReportTemplateModel template = (AuditReportTemplateModel) rendererService.getRendererTemplateForCode(
				this.TEMPLATE_NAME);
		return template;
	}
}