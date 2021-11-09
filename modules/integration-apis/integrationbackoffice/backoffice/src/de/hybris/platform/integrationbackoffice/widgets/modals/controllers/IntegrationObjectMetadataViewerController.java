/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.modals.controllers;

import de.hybris.platform.integrationbackoffice.constants.IntegrationbackofficeConstants;
import de.hybris.platform.integrationbackoffice.services.ReadService;
import de.hybris.platform.integrationbackoffice.widgets.modals.builders.NetworkDataBuilder;
import de.hybris.platform.integrationbackoffice.widgets.modals.generator.IntegrationObjectImpexGenerator;
import de.hybris.platform.integrationbackoffice.widgets.modals.generator.IntegrationObjectJsonGenerator;
import de.hybris.platform.integrationbackoffice.widgets.modals.utility.ModalUtils;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectModel;
import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.odata2services.odata.InvalidODataSchemaException;
import de.hybris.platform.odata2services.odata.schema.entity.EntitySetNameGenerator;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.zkoss.lang.Strings;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Button;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Textbox;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.components.visjs.network.NetworkChart;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.cockpitng.util.notifications.NotificationService;
import com.hybris.cockpitng.util.notifications.event.NotificationEvent;

/**
 * Controls the functionality of the viewer
 */
public final class IntegrationObjectMetadataViewerController extends DefaultWidgetController
{
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Log.getLogger(IntegrationObjectMetadataViewerController.class);
	private static final String EXAMPLE_PATH = "https://<your-host-name>/odata2webservices/";

	@WireVariable
	private transient ReadService readService;
	@WireVariable
	private transient EntitySetNameGenerator pluralizer;
	@WireVariable
	private transient NotificationService notificationService;
	@WireVariable
	private transient IntegrationObjectJsonGenerator integrationObjectJsonGenerator;
	@WireVariable
	private transient IntegrationObjectImpexGenerator integrationObjectImpexGenerator;

	private Textbox integrationbackofficeImpexTextbox;
	private Textbox integrationbackofficeEdmxTextbox;
	private Textbox integrationbackofficeEdmxPathTextbox;
	private Textbox integrationbackofficeJsonTextBox;
	private Textbox integrationbackofficeJsonPathTextbox;
	private Tabbox integrationbackofficeMetadataTabbox;
	private Tab integrationbackofficeImpexTab;
	private Tab integrationbackofficeEdmxTab;
	private Tab integrationbackofficeJsonTab;
	private Tab integrationbackofficeRelationshipGraphTab;
	private Button integrationbackofficeMetadataDownloadButton;

	private NetworkChart integrationbackofficeNetworkChart;
	private Textbox canvasAsString;

	private IntegrationObjectModel selectedIntegrationObject;

	protected NotificationService getNotificationService()
	{
		return notificationService;
	}

	public IntegrationObjectModel getSelectedIntegrationObject()
	{
		return this.selectedIntegrationObject;
	}

	public void setSelectedIntegrationObject(final IntegrationObjectModel selectedIntegrationObject)
	{
		this.selectedIntegrationObject = selectedIntegrationObject;
	}

	public void setReadService(final ReadService readService)
	{
		this.readService = readService;
	}

	public void setPluralizer(final EntitySetNameGenerator pluralizer)
	{
		this.pluralizer = pluralizer;
	}

	@SocketEvent(socketId = "showModal")
	public void loadMetadata(final IntegrationObjectModel integrationObjectModel)
	{
		if (integrationObjectModel != null)
		{
			setSelectedIntegrationObject(integrationObjectModel);
			try
			{
				integrationbackofficeImpexTextbox.setValue(
						integrationObjectImpexGenerator.generateImpex(getSelectedIntegrationObject()));
				integrationbackofficeEdmxTextbox.setValue(generateEDMX());
				integrationbackofficeEdmxPathTextbox.setValue(generateServicePath());
				integrationbackofficeJsonTextBox.setValue(integrationObjectJsonGenerator.generateJson(integrationObjectModel));
				integrationbackofficeJsonPathTextbox.setValue(generateEndpointPath());
				final String rootNodeName = getRootNodeName();
				final NetworkDataBuilder networkDataBuilder = new NetworkDataBuilder(
						readService.getEDMX(getSelectedIntegrationObject()), rootNodeName);
				integrationbackofficeNetworkChart.setNetwork(networkDataBuilder.generateNetwork());
				integrationbackofficeNetworkChart.setOptions(networkDataBuilder.getNetworkOptions());
			}
			catch (final InvalidODataSchemaException e)
			{
				integrationbackofficeMetadataDownloadButton.setDisabled(true);
				getNotificationService().notifyUser(Strings.EMPTY, IntegrationbackofficeConstants.NOTIFICATION_TYPE_STANDARD,
						NotificationEvent.Level.WARNING,
						getLabel("integrationbackoffice.metadata.warning.msg.failedEdmx"));
				LOG.error("Error in generating metadata", e);
			}
		}
	}

	@ViewEvent(componentID = "integrationbackofficeMetadataDownloadButton", eventName = Events.ON_CLICK)
	public void downloadMetadata()
	{
		final byte[] fileContent;
		final String fileFormat;
		final String fileName = getSelectedIntegrationObject().getCode();

		if (integrationbackofficeMetadataTabbox.getSelectedTab().equals(integrationbackofficeImpexTab))
		{
			fileContent = integrationbackofficeImpexTextbox.getText().getBytes(StandardCharsets.UTF_8);
			fileFormat = "application/impex";
			ModalUtils.executeMediaDownload(fileContent, fileFormat, fileName);
		}
		else if (integrationbackofficeMetadataTabbox.getSelectedTab().equals(integrationbackofficeEdmxTab))
		{
			fileContent = integrationbackofficeEdmxTextbox.getText().getBytes(StandardCharsets.UTF_8);
			fileFormat = "application/xml";
			ModalUtils.executeMediaDownload(fileContent, fileFormat, fileName);
		}
		else if (integrationbackofficeMetadataTabbox.getSelectedTab().equals(integrationbackofficeJsonTab))
		{
			fileContent = integrationbackofficeJsonTextBox.getText().getBytes(StandardCharsets.UTF_8);
			fileFormat = "application/json";
			ModalUtils.executeMediaDownload(fileContent, fileFormat, fileName);
		}
		else if (integrationbackofficeMetadataTabbox.getSelectedTab().equals(integrationbackofficeRelationshipGraphTab))
		{
			fileContent = canvasAsString.getValue().split(",")[1].getBytes(StandardCharsets.UTF_8);
			canvasAsString.setValue("");
			final byte[] decodedContent = Base64.decodeBase64(fileContent);
			fileFormat = "image/png";
			ModalUtils.executeMediaDownload(decodedContent, fileFormat, fileName);
		}
	}

	@Listen("onChange=#canvasAsString")
	public void updateBackValueOfCanvasString(final InputEvent event)
	{
		canvasAsString.setValue(event.getValue());
	}

	private String generateEDMX() throws InvalidODataSchemaException
	{
		try (final InputStream inputStream = readService.getEDMX(getSelectedIntegrationObject()))
		{
			if (inputStream != null)
			{
				return parseEdmxContentToXml(IOUtils.toString(inputStream, StandardCharsets.UTF_8));
			}
		}
		catch (final IOException e)
		{
			LOG.error("There was a problem closing the InputStream", e);
		}

		return "";
	}

	public String parseEdmxContentToXml(final String edmxContent)
	{
		try
		{
			final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newDefaultInstance();
			documentBuilderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
			documentBuilderFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
			documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
			documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");

			final Document document = documentBuilderFactory.newDocumentBuilder()
			                                                .parse(new InputSource(new ByteArrayInputStream(
					                                                edmxContent.getBytes(StandardCharsets.UTF_8))));

			final TransformerFactory transformerFactory = TransformerFactory.newDefaultInstance();
			transformerFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
			transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
			transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
			final Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");


			final StringWriter stringWriter = new StringWriter();
			transformer.transform(new DOMSource(document), new StreamResult(stringWriter));
			return stringWriter.toString();
		}
		catch (final IOException | IllegalArgumentException | ParserConfigurationException | TransformerException | SAXException | WrongValueException e)
		{
			LOG.error(e.getLocalizedMessage());
			LOG.error("Failed to format EDMX", e);
		}

		return "";

	}

	public String generateServicePath()
	{
		return EXAMPLE_PATH + getSelectedIntegrationObject().getCode() + "/$metadata";
	}

	public String generateEndpointPath()
	{
		final IntegrationObjectModel integrationObjectModel = getSelectedIntegrationObject();
		return EXAMPLE_PATH + integrationObjectModel.getCode() + "/" + pluralizer.generate(
				integrationObjectModel.getRootItem().getCode());
	}

	private String getRootNodeName()
	{
		return Optional.ofNullable(getSelectedIntegrationObject())
		               .flatMap(select -> Optional.ofNullable(select.getRootItem()))
		               .map(IntegrationObjectItemModel::getCode)
		               .orElse("");
	}
}