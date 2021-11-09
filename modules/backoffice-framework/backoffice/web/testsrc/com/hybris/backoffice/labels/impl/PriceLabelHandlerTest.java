/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.labels.impl;

import static java.util.Currency.getInstance;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.servicelayer.i18n.I18NService;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.cockpitng.testing.AbstractCockpitngUnitTest;
import com.hybris.cockpitng.testing.annotation.ExtensibleWidget;


@RunWith(MockitoJUnitRunner.class)
@ExtensibleWidget(level = ExtensibleWidget.ALL)
public class PriceLabelHandlerTest extends AbstractCockpitngUnitTest<PriceLabelHandler>
{

	public static final String DONG_ISO_CODE = "VND";
	public static final String DONG_SYMBOL = "₫";

	@Spy
	@InjectMocks
	private PriceLabelHandler handler;

	@Mock
	private I18NService i18NService;
	@Mock
	private CurrencyModel dong;
	@Mock
	private CurrencyModel yen;
	@Mock
	private CurrencyModel quid;


	@Test
	public void shouldReturnValidLabelsForDongIsoCode()
	{
		when(dong.getIsocode()).thenReturn(DONG_ISO_CODE);
		when(dong.getSymbol()).thenReturn(DONG_SYMBOL);
		when(i18NService.getCurrentLocale()).thenReturn(Locale.ENGLISH);
		when(i18NService.getBestMatchingJavaCurrency(DONG_ISO_CODE)).thenReturn(getInstance(DONG_ISO_CODE));

		final NumberFormat formatter = getJavaCurrencyFormatter(Locale.ENGLISH, getInstance(DONG_ISO_CODE), dong);

		assertThat(handler.getLabel(0d, dong)).isEqualTo(formatter.format(0d));
		assertThat(handler.getLabel(22725.77d, dong)).isEqualTo(formatter.format(22725.77d));
		assertThat(handler.getLabel(0.123d, dong)).isEqualTo(formatter.format(0.123d));
		assertThat(handler.getLabel(-1.1598d, dong)).isEqualTo(formatter.format(-1.1598d));
	}

	@Test
	public void shouldReturnValidLabelsForYenCurrencyForEnglishLocale()
	{
		when(yen.getIsocode()).thenReturn("JPY");
		when(yen.getSymbol()).thenReturn("¥");
		when(i18NService.getCurrentLocale()).thenReturn(Locale.ENGLISH);
		when(i18NService.getBestMatchingJavaCurrency("JPY")).thenReturn(getInstance("JPY"));
		when(yen.getDigits()).thenReturn(0);

		assertValidLabelsForYen(getJavaCurrencyFormatter(Locale.ENGLISH, getInstance("JPY"), yen));
	}

	@Test
	public void shouldReturnValidLabelsForYenCurrencyForFrenchLocale()
	{
		when(yen.getIsocode()).thenReturn("JPY");
		when(yen.getSymbol()).thenReturn("¥");
		when(i18NService.getCurrentLocale()).thenReturn(Locale.FRENCH);
		when(i18NService.getBestMatchingJavaCurrency("JPY")).thenReturn(getInstance("JPY"));
		when(yen.getDigits()).thenReturn(0);

		assertValidLabelsForYen(getJavaCurrencyFormatter(Locale.FRENCH, getInstance("JPY"), yen));
	}

	@Test
	public void getLabelShouldBeAbleToFormatQuasiUniversalIntergalacticDenomination()
	{
		//given
		when(quid.getIsocode()).thenReturn("QUID");
		when(quid.getSymbol()).thenReturn("QUID");
		when(i18NService.getCurrentLocale()).thenReturn(Locale.ENGLISH);
		doThrow(IllegalArgumentException.class).when(i18NService).getBestMatchingJavaCurrency("QUID");

		//when
		final String label = handler.getLabel(42.5d, quid);

		//then
		assertThat(label).isEqualTo(getNonJavaCurrencyFormatter(Locale.ENGLISH, quid.getSymbol()).format(42.5d));
	}

	private NumberFormat getJavaCurrencyFormatter(final Locale locale, final Currency currency, final CurrencyModel currencyModel)
	{
		final DecimalFormatSymbols decimalSymbols = DecimalFormatSymbols.getInstance(locale);
		decimalSymbols.setCurrency(currency);
		decimalSymbols.setCurrencySymbol(currencyModel.getSymbol());

		final NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
		((DecimalFormat) numberFormat).setDecimalFormatSymbols(decimalSymbols);
		numberFormat.setMaximumFractionDigits(currencyModel.getDigits());
		numberFormat.setMinimumFractionDigits(currencyModel.getDigits());
		((DecimalFormat) numberFormat).setDecimalSeparatorAlwaysShown(currencyModel.getDigits() > 0);

		return numberFormat;
	}

	private NumberFormat getNonJavaCurrencyFormatter(final Locale locale, final String currencySymbol)
	{
		final DecimalFormatSymbols decimalSymbols = new DecimalFormatSymbols();
		decimalSymbols.setCurrencySymbol(ObjectUtils.defaultIfNull(currencySymbol, StringUtils.EMPTY));
		decimalSymbols.setMonetaryDecimalSeparator('.');

		final NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
		((DecimalFormat) numberFormat).setDecimalFormatSymbols(decimalSymbols);

		return numberFormat;
	}

	private void assertValidLabelsForYen(final NumberFormat formatter)
	{
		assertThat(handler.getLabel(0d, yen)).isEqualTo(formatter.format(0d));
		assertThat(handler.getLabel(2272577d, yen)).isEqualTo(formatter.format(2272577d));
		assertThat(handler.getLabel(0.123d, yen)).isEqualTo(formatter.format(0.123d));
		assertThat(handler.getLabel(-1.1598d, yen)).isEqualTo(formatter.format(-1.1598d));
	}

}
