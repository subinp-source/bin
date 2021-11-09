/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.consignmenttrackingoccaddon.validation;

import de.hybris.platform.commercefacades.order.OrderFacade;
import de.hybris.platform.commercefacades.order.data.ConsignmentData;
import de.hybris.platform.consignmenttrackingfacades.ConsignmentTrackingFacade;
import de.hybris.platform.consignmenttrackingoccaddon.constants.ConsignmentErrorConstants;
import de.hybris.platform.consignmenttrackingoccaddon.exceptions.NotShippedException;
import de.hybris.platform.webservicescommons.errors.exceptions.NotFoundException;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;


/**
 * validate the consignment data
 */
@Component("consignmentTrackingValidator")
public class ConsignmentTrackingValidator
{

	private static final String STATUS_DISPLAY = "shipped";

	@Resource
	private OrderFacade orderFacade;
	@Resource
	private ConsignmentTrackingFacade consignmentFacade;

	public ConsignmentData checkIfConsignmentDataExist(final String orderCode, final String consignmentCode)
	{
		final List<ConsignmentData> consignments = consignmentFacade.getConsignmentsByOrder(orderCode);
		if (CollectionUtils.isEmpty(consignments))
		{

			throw new NotFoundException(ConsignmentErrorConstants.CONSIGNMENT_NOT_FOUND_MESSAGE,
					ConsignmentErrorConstants.CONSIGNMENT_NOT_FOUND, orderCode);
		}

		return consignments.stream().filter(data -> data.getCode().equals(consignmentCode)).findFirst()
				.orElseThrow(() -> new NotFoundException(ConsignmentErrorConstants.CONSIGNMENT_INCORRECT_MESSAGE,
						ConsignmentErrorConstants.CONSIGNMENT_INCORRECT, consignmentCode));
	}

	public void checkIfConsignmentShipped(final ConsignmentData data)
	{
		if(!data.getStatusDisplay().equals(STATUS_DISPLAY)){
			throw new NotShippedException(NotShippedException.NOT_SHIPPED_MESSAGE, NotShippedException.NOT_SHIPPED,
					data.getCode());
		}
	}

}


