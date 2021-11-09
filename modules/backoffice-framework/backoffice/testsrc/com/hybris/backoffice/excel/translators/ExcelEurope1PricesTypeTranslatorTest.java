/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.excel.translators;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.europe1.enums.PriceRowChannel;
import de.hybris.platform.europe1.enums.UserPriceGroup;
import de.hybris.platform.europe1.jalo.impex.Europe1PricesTranslator;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.util.StandardDateRange;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;
import com.hybris.backoffice.excel.data.ImpexValue;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.util.DefaultExcelDateUtils;


@RunWith(MockitoJUnitRunner.class)
public class ExcelEurope1PricesTypeTranslatorTest
{
	private static final String TEN_EUR = "10 EUR";
	@Spy
	private final DefaultExcelDateUtils excelDateUtils = new DefaultExcelDateUtils();
	@Mock
	private CommonI18NService commonI18NService;
	@Spy
	@InjectMocks
	private ExcelEurope1PricesTypeTranslator translator;
	private Date dateFrom;
	private Date dateTo;
	private StandardDateRange dateRange;
	private CurrencyModel currencyUsd;
	private UnitModel pieces;
	private UserModel admin;
	private AttributeDescriptorModel attributeDescriptor;

	@Before
	public void setUp()
	{
		dateFrom = Date.from(LocalDateTime.of(2017, 10, 23, 10, 46).toInstant(ZoneOffset.UTC));
		dateTo = Date.from(LocalDateTime.of(2017, 12, 11, 4, 22).toInstant(ZoneOffset.UTC));
		dateRange = new StandardDateRange(dateFrom, dateTo);
		currencyUsd = mock(CurrencyModel.class);
		when(currencyUsd.getIsocode()).thenReturn("USD");

		pieces = mock(UnitModel.class);
		when(pieces.getCode()).thenReturn("pieces");

		admin = mock(UserModel.class);
		when(admin.getUid()).thenReturn("admin");

		attributeDescriptor = mock(AttributeDescriptorModel.class);

		final I18NService i18NService = mock(I18NService.class);
		when(i18NService.getCurrentTimeZone()).thenReturn(TimeZone.getDefault());
		excelDateUtils.setI18NService(i18NService);
	}

	@Test
	public void testExportDataWithAllValues()
	{

		final PriceRowModel priceRowModel = mockPriceRow();

		final Optional<Object> data = translator.exportData(Lists.newArrayList(priceRowModel));

		assertThat(data.isPresent()).isTrue();
		assertThat(data.get()).isEqualTo("20.0 USD:N:admin:2 pieces:" + formatExcelDateRange(dateFrom, dateTo) + ":mobile");
	}

	@Test
	public void testExportDataNullDateRange()
	{

		final PriceRowModel priceRowModel = mockPriceRow();
		when(priceRowModel.getDateRange()).thenReturn(null);

		final Optional<Object> data = translator.exportData(Lists.newArrayList(priceRowModel));

		assertThat(data.isPresent()).isTrue();
		assertThat(data.get()).isEqualTo("20.0 USD:N:admin:2 pieces::mobile");
	}

	@Test
	public void testExportDataUserGroupGiven()
	{
		final UserPriceGroup priceGroup = mock(UserPriceGroup.class);
		when(priceGroup.getCode()).thenReturn("priceGroup");

		final PriceRowModel priceRowModel = mockPriceRow();
		when(priceRowModel.getUg()).thenReturn(priceGroup);

		final Optional<Object> data = translator.exportData(Lists.newArrayList(priceRowModel));

		assertThat(data.isPresent()).isTrue();
		assertThat(data.get()).isEqualTo("20.0 USD:N:priceGroup:2 pieces:" + formatExcelDateRange(dateFrom, dateTo) + ":mobile");
	}

	@Test
	public void testImportData()
	{
		//given
		final ImportParameters importParameters = new ImportParameters("a", "b", "c", "d", Lists.newArrayList(importParams()));
		given(commonI18NService.getCurrentLanguage()).willReturn(mock(LanguageModel.class));
		given(commonI18NService.getLocaleForLanguage(any())).willReturn(Locale.ENGLISH);

		//when
		final ImpexValue impexValue = translator.importValue(attributeDescriptor, importParameters);

		//then
		assertThat(impexValue.getValue())
				.isEqualTo("axel 10 pieces = 10 EUR G " + formatTranslatorDateRange(dateFrom, dateTo) + " mobile");
		assertThat(impexValue.getHeaderValue().getDateFormat()).isEqualTo(excelDateUtils.getDateTimeFormat());
		assertThat(impexValue.getHeaderValue().getTranslator()).isEqualTo(Europe1PricesTranslator.class.getName());
	}

	@Test
	public void testImportSimpleDate()
	{
		//given
		final Map<String, String> params = new HashMap<>();
		params.put(ExcelEurope1PricesTypeTranslator.PRICE_CURRENCY, TEN_EUR);
		given(commonI18NService.getCurrentLanguage()).willReturn(mock(LanguageModel.class));
		given(commonI18NService.getLocaleForLanguage(any())).willReturn(Locale.ENGLISH);
		final ImportParameters importParameters = new ImportParameters("a", "b", "c", "d", Lists.newArrayList(params));

		//when
		final ImpexValue impexValue = translator.importValue(attributeDescriptor, importParameters);

		//then
		assertThat(impexValue.getValue()).isEqualTo(TEN_EUR);
		assertThat(impexValue.getHeaderValue().getDateFormat()).isEqualTo(excelDateUtils.getDateTimeFormat());
		assertThat(impexValue.getHeaderValue().getTranslator()).isEqualTo(Europe1PricesTranslator.class.getName());
	}

	@Test
	public void testImportDoublePriceInEnglish()
	{
		//given
		final Map<String, String> params = new HashMap<>();
		params.put(ExcelEurope1PricesTypeTranslator.PRICE_CURRENCY, "10.5 USD");
		given(commonI18NService.getCurrentLanguage()).willReturn(mock(LanguageModel.class));
		given(commonI18NService.getLocaleForLanguage(any())).willReturn(Locale.ENGLISH);
		final ImportParameters importParameters = new ImportParameters("a", "b", "c", "d", Lists.newArrayList(params));

		//when
		final ImpexValue impexValue = translator.importValue(attributeDescriptor, importParameters);

		//then
		assertThat(impexValue.getValue()).isEqualTo("10.5 USD");
	}

	@Test
	public void testImportDoublePriceInGerman()
	{
		//given
		final Map<String, String> params = new HashMap<>();
		params.put(ExcelEurope1PricesTypeTranslator.PRICE_CURRENCY, "10.5 USD");
		given(commonI18NService.getCurrentLanguage()).willReturn(mock(LanguageModel.class));
		given(commonI18NService.getLocaleForLanguage(any())).willReturn(Locale.GERMAN);
		final ImportParameters importParameters = new ImportParameters("a", "b", "c", "d", Lists.newArrayList(params));

		//when
		final ImpexValue impexValue = translator.importValue(attributeDescriptor, importParameters);

		//then
		assertThat(impexValue.getValue()).isEqualTo("10,5 USD");
	}

	@Test
	public void shouldNotThrowErrorIfPriceCurrencyIsNull()
	{
		//given
		final Map<String, String> params = new HashMap<>();
		params.put(ExcelEurope1PricesTypeTranslator.PRICE_CURRENCY, null);
		final ImportParameters importParameters = new ImportParameters("a", "b", "c", "d", Lists.newArrayList(params));

		//when
		final ImpexValue impexValue = translator.importValue(attributeDescriptor, importParameters);

		//then
		assertThat(impexValue.getValue()).isEqualTo("");
	}

	@Test
	public void testHeaderValueIsEqualToAttributeDescriptorQualifier()
	{
		// given
		final ImportParameters importParameters = new ImportParameters("typeCode", "isoCode", "cellValue", "entryRef",
				"formatError");
		final String expectedAttributeDescriptorQualifier = "expectedAttributeDescriptorQualifier";
		when(attributeDescriptor.getQualifier()).thenReturn(expectedAttributeDescriptorQualifier);

		// when
		final ImpexValue impexValue = translator.importValue(attributeDescriptor, importParameters);

		// then
		assertThat(impexValue.getHeaderValue().getName()).isEqualTo(expectedAttributeDescriptorQualifier);
	}

	protected Map<String, String> importParams()
	{
		final Map<String, String> params = new HashMap<>();
		params.put(ExcelEurope1PricesTypeTranslator.PRICE_CURRENCY, TEN_EUR);
		params.put(ExcelEurope1PricesTypeTranslator.NET_GROSS, "G");
		params.put(ExcelEurope1PricesTypeTranslator.USER_OR_UPG, "axel");
		params.put(ExcelEurope1PricesTypeTranslator.QUANTITY_UNIT, "10 pieces");
		params.put(excelDateUtils.getDateRangeParamKey(),
				excelDateUtils.exportDate(dateFrom) + " to " + excelDateUtils.exportDate(dateTo));
		params.put(ExcelEurope1PricesTypeTranslator.CHANNEL, "mobile");
		return params;
	}

	protected PriceRowModel mockPriceRow()
	{
		final PriceRowModel priceRowModel = mock(PriceRowModel.class);
		when(priceRowModel.getCurrency()).thenReturn(currencyUsd);
		when(priceRowModel.getPrice()).thenReturn(20.0);
		when(priceRowModel.getNet()).thenReturn(true);
		when(priceRowModel.getUnit()).thenReturn(pieces);
		when(priceRowModel.getMinqtd()).thenReturn(2L);
		when(priceRowModel.getChannel()).thenReturn(PriceRowChannel.MOBILE);
		when(priceRowModel.getDateRange()).thenReturn(dateRange);
		when(priceRowModel.getUser()).thenReturn(admin);
		return priceRowModel;
	}

	protected String formatExcelDateRange(final Date from, final Date to)
	{
		return String.format("[%s to %s]", excelDateUtils.exportDate(from), excelDateUtils.exportDate(to));
	}

	protected String formatTranslatorDateRange(final Date from, final Date to)
	{
		return String.format("[%s,%s]", excelDateUtils.importDate(excelDateUtils.exportDate(from)),
				excelDateUtils.importDate(excelDateUtils.exportDate(to)));
	}
}
