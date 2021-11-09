/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.modals.utility;

import de.hybris.platform.inboundservices.model.InboundChannelConfigurationModel;
import de.hybris.platform.integrationbackoffice.dto.AbstractListItemDTO;
import de.hybris.platform.integrationbackoffice.services.ReadService;
import de.hybris.platform.integrationbackoffice.utility.QualifierNameUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.zkoss.zul.Filedownload;

public final class ModalUtils
{
	private ModalUtils()
	{
		throw new IllegalStateException("Utility class");
	}

	public static boolean isAlphaNumericName(final String serviceName)
	{
		return QualifierNameUtils.isAlphaNumericName(serviceName);
	}

	public static boolean isServiceNameUnique(final String serviceName, final ReadService readService)
	{
		return readService.getIntgrationObjectModelByCode(serviceName).isEmpty();
	}

	public static boolean isAliasUnique(final String alias, final String qualifier, final List<AbstractListItemDTO> attributes)
	{
		for (final AbstractListItemDTO listItemDTO : attributes)
		{
			boolean matchingAlias = listItemDTO.getAlias().equalsIgnoreCase(alias);
			boolean matchingSourceOfTruthName = listItemDTO.getQualifier().equalsIgnoreCase(alias);
			boolean isOwnQualifier = qualifier.equalsIgnoreCase(alias);

			if((matchingAlias || matchingSourceOfTruthName) && !isOwnQualifier)
			{
				return false;
			}
		}
		return true;
	}

	public static boolean isVADMCodeUnique(final String code, final ReadService readService)
	{
		return readService.getVirtualAttributeDescriptorModelsByCode(code).isEmpty();
	}

	/**
	 * Calls the appropriate method to download the file in the given format using the browser's download functionality
	 *
	 * @param fileContent Content of file in byte[] format
	 * @param fileFormat  Format that the file is saved as
	 * @param fileName    Name of the file
	 */
	public static void executeMediaDownload(final byte[] fileContent, final String fileFormat, final String fileName)
	{
		InputStream stream = new ByteArrayInputStream(fileContent);
		Filedownload.save(stream, fileFormat, fileName);
	}

	public static void executeReportDownload(InputStream stream, final String fileFormat, final String fileName)
	{
		Filedownload.save(stream, fileFormat, fileName);
	}

	/**
	 * Calls the appropriate method to check if an InboundChannelConfiguration with given IO has already existed.
	 *
	 * @param IntegrationObjectName The IO name with which an ICC will be created.
	 * @param readService The service used to handles the read requests.
	 */
	public static boolean isInboundChannelConfigNameUnique(final String IntegrationObjectName, final ReadService readService)
	{
		for (final InboundChannelConfigurationModel iCCObject : readService.getInboundChannelConfigModels())
		{
			if (iCCObject.getIntegrationObject().getCode().equalsIgnoreCase(IntegrationObjectName))
			{
				return false;
			}
		}
		return true;
	}
}
