/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservices.core.errors.converters;

import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.commerceservices.order.CommerceCartModificationStatus;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.webservicescommons.dto.error.ErrorWsDTO;
import de.hybris.platform.webservicescommons.errors.converters.AbstractLocalizedErrorConverter;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Required;


/**
 * Converts {@link CartModificationData} to a list of
 * {@link de.hybris.platform.webservicescommons.dto.error.ErrorWsDTO}.
 */
public class CartModificationDataErrorConverter extends AbstractLocalizedErrorConverter
{
	public static final String TYPE_STOCK = "InsufficientStockError";
	/**
	 * @deprecated since 2011. Please use {@link CartModificationDataErrorConverter#TYPE_STOCK} instead
	 */
	@Deprecated(since = "2011", forRemoval = true)
	public static final String TYPE = TYPE_STOCK;
	public static final String TYPE_OTHER = "GenericValidationError";
	public static final String SUBJECT_TYPE_ENTRY = "entry";
	public static final String SUBJECT_TYPE_PRODUCT = "product";
	/**
	 * @deprecated since 2011. Please use {@link CartModificationDataErrorConverter#SUBJECT_TYPE_ENTRY} instead
	 */
	@Deprecated(since = "2011", forRemoval = true)
	public static final String SUBJECT_TYPE = SUBJECT_TYPE_ENTRY;
	/**
	 * @deprecated since 2011. Please use {@link CommerceCartModificationStatus#NO_STOCK} instead
	 */
	@Deprecated(since = "2011", forRemoval = true)
	public static final String NO_STOCK = "noStock";
	/**
	 * @deprecated since 2011. Please use {@link CommerceCartModificationStatus#LOW_STOCK} instead
	 */
	@Deprecated(since = "2011", forRemoval = true)
	public static final String LOW_STOCK = "lowStock";
	/**
	 * @deprecated since 2011. Please use {@link CartModificationDataErrorConverter#MSG_KEY_PREFIX} followed by reason,
	 *  for example: {@link CommerceCartModificationStatus#NO_STOCK}
	 */
	@Deprecated(since = "2011", forRemoval = true)
	public static final String NO_STOCK_MESSAGE = "cart.noStock";
	/**
	 * @deprecated since 2011. Please use {@link CartModificationDataErrorConverter#MSG_KEY_PREFIX} followed by reason,
	 *  for example: {@link CommerceCartModificationStatus#LOW_STOCK}
	 */
	@Deprecated(since = "2011", forRemoval = true)
	public static final String LOW_STOCK_MESSAGE = "cart.lowStock";
	public static final String MSG_KEY_PREFIX = "cart.";

	private CommerceCommonI18NService commerceCommonI18NService;

	@Override
	public boolean supports(final Class clazz)
	{
		return CartModificationData.class.isAssignableFrom(clazz);
	}

	@Override
	public void populate(final Object obj, final List<ErrorWsDTO> webserviceErrorList)
	{
		final CartModificationData cartModification = (CartModificationData) obj;
		ServicesUtil.validateParameterNotNull(cartModification.getEntry(), //
				"Parameter Entry of CartModificationData may not be null");
		ServicesUtil.validateParameterNotNull(cartModification.getStatusCode(), //
				"Parameter StatusCode of CartModificationData may not be null");

		final ErrorWsDTO errorDto = createTargetElement();
		populateSubject(cartModification, errorDto);
		populateMessage(cartModification, errorDto);
		webserviceErrorList.add(errorDto);
	}

	/**
	 * @deprecated since 2011. Please use {@link CartModificationDataErrorConverter#getMessage} instead
	 */
	@Deprecated(since = "2011", forRemoval = true)
	protected String getNoStockMessage(final CartModificationData cartModification, final Locale locale)
	{
		final Object[] args = new Object[] { cartModification.getEntry().getProduct().getCode(),
				cartModification.getEntry().getEntryNumber() };
		return getMessage(NO_STOCK_MESSAGE, args, locale);
	}

	/**
	 * @deprecated since 2011. Please use {@link CartModificationDataErrorConverter#getMessage} instead
	 */
	@Deprecated(since = "2011", forRemoval = true)
	protected String getLowStockMessage(final CartModificationData cartModification, final Locale locale)
	{
		final Object[] args = new Object[] { cartModification.getEntry().getProduct().getCode(),
				cartModification.getEntry().getEntryNumber(), Long.valueOf(cartModification.getQuantityAdded()) };
		return getMessage(LOW_STOCK_MESSAGE, args, locale);
	}

	protected void populateMessage(final CartModificationData cartModification, final ErrorWsDTO errorDto)
	{
		final String statusCode = cartModification.getStatusCode();
		errorDto.setReason(statusCode);
		if (CommerceCartModificationStatus.NO_STOCK.equals(statusCode)) // backward compatibility
		{
			errorDto.setType(TYPE_STOCK);
			errorDto.setMessage(getNoStockMessage(cartModification, commerceCommonI18NService.getCurrentLocale()));
		}
		else if (CommerceCartModificationStatus.LOW_STOCK.equals(statusCode)) // backward compatibility
		{
			errorDto.setType(TYPE_STOCK);
			errorDto.setMessage(getLowStockMessage(cartModification, commerceCommonI18NService.getCurrentLocale()));
		}
		else // new behavior
		{
			errorDto.setMessage(getMessage(cartModification));
			errorDto.setType(TYPE_OTHER);
		}
	}

	protected void populateSubject(final CartModificationData cartModification, final ErrorWsDTO errorDto)
	{
		final OrderEntryData entry = cartModification.getEntry();
		if (entry.getEntryNumber() != null)
		{
			errorDto.setSubject(entry.getEntryNumber().toString());
			errorDto.setSubjectType(SUBJECT_TYPE_ENTRY);
		}
		else if (entry.getProduct() != null && entry.getProduct().getCode() != null) // new behavior
		{
			errorDto.setSubject(entry.getProduct().getCode());
			errorDto.setSubjectType(SUBJECT_TYPE_PRODUCT);
		}
		else
		{
			// neither product code nor entry number are given
			errorDto.setSubject(null);
			errorDto.setSubjectType(null);
		}
	}

	protected String getMessage(final CartModificationData cartModification)
	{
		final OrderEntryData entry = cartModification.getEntry();

		final Object productCode = entry.getProduct() != null ? entry.getProduct().getCode() : null;
		final Object entryNumber = entry.getEntryNumber();
		final Long quantity = cartModification.getQuantityAdded();

		return getMessage(MSG_KEY_PREFIX + cartModification.getStatusCode(), new Object[] { productCode, entryNumber, quantity },
				commerceCommonI18NService.getCurrentLocale());
	}

	@Required
	public void setCommerceCommonI18NService(final CommerceCommonI18NService commerceCommonI18NService)
	{
		this.commerceCommonI18NService = commerceCommonI18NService;
	}
}
