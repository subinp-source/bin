/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackofficetest.widgets.modals.builders;

import com.hybris.cockpitng.components.visjs.network.data.Edge;
import com.hybris.cockpitng.components.visjs.network.data.Network;
import com.hybris.cockpitng.components.visjs.network.data.Node;

import de.hybris.platform.integrationbackoffice.widgets.modals.builders.NetworkDataBuilder;
import de.hybris.platform.integrationbackoffice.widgets.modals.data.visualizer.EDMXEntityType;
import de.hybris.platform.integrationbackoffice.widgets.modals.data.visualizer.EDMXSchema;
import de.hybris.platform.servicelayer.ServicelayerTest;

import org.apache.commons.collections.CollectionUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class NetworkDataBuilderIntegrationTest extends ServicelayerTest
{

	private static final String VIS_TABLE_START = "<table style='border-spacing:5px;'>";
	private static final String VIS_TABLE_END = "</table>";
	private static final String VIS_HEADER_ROW_START = "<tr class='z-listhead'>";
	private static final String VIS_HEADER_COL1_START = "<th style='text-align: left;'>";
	private static final String VIS_HEADER_COL2_START = "<th style='text-align: left;'>";
	private static final String VIS_HEADER_COL_END = "</th>";
	private static final String VIS_HEADER_ROW_END = "</tr>";
	private static final String VIS_ROW_START = "<tr class='z-listitem'>";
	private static final String VIS_ROW_END = "</tr>";
	private static final String VIS_COL1_START = "<td class='z-listcell' style='padding-top:0.5em; text-align:left;'>";
	private static final String VIS_COL1_END = "</td>";
	private static final String VIS_COL2_START = "<td class='z-listcell' style='padding-top:0.5em; text-align:left;'>";
	private static final String VIS_COL2_END = "</td>";
	private static final String VIS_WHITESPACE = "&nbsp;";
	private static final String EXPECTED_CV_NODE_LABEL = "CatalogVersion---------------------------integrationKeyversionEdm.StringintegrationKeyEdm.StringcatalogCatalogterritoriesCountry";
	private static final String EXPECTED_PRODUCT_NODE_LABEL = "ProductRoot-------------------------------integrationKeycodeEdm.StringvirtualIntEdm.Int32integrationKeyEdm.StringcatalogVersionCatalogVersion";
	private static final String EXPECTED_CATALOG_NODE_LABEL = "Catalog---------------------------integrationKeyidEdm.StringintegrationKeyEdm.String";
	private static final String EXPECTED_COUNTRY_NODE_LABEL = "Country---------------------------integrationKeyisocodeEdm.StringintegrationKeyEdm.String";
	private static NetworkDataBuilder networkDataBuilder;
	private static EDMXSchema schema;
	private static Network network;
	private static final String WHITESPACE_REGEX_PATTERN = "\\s+";

	@BeforeClass
	public static void setupNetwork() throws FileNotFoundException, JAXBException, XMLStreamException
	{
		final InputStream inputStream = loadFile("test/text/Product_EDMX.txt");
		networkDataBuilder = new NetworkDataBuilder(inputStream, "Product");
		schema = networkDataBuilder.getSchemaData(inputStream);
		network = networkDataBuilder.createNetwork(schema);
	}

	public static InputStream loadFile(final String fileLocation) throws FileNotFoundException
	{
		final ClassLoader classLoader = NetworkDataBuilderIntegrationTest.class.getClassLoader();
		final URL url = classLoader.getResource(fileLocation);
		File file = null;
		if (url != null)
		{
			file = new File(url.getFile());
		}
		assert file != null;
		return new FileInputStream(file);
	}

	@Test
	public void createNetworkVerifyNodesLabels()
	{
		final List<String> expectedNodeLabels = Arrays.asList(EXPECTED_PRODUCT_NODE_LABEL, EXPECTED_CATALOG_NODE_LABEL,
				EXPECTED_CV_NODE_LABEL, EXPECTED_COUNTRY_NODE_LABEL);
		final Collection<String> actualNodesLabels = network.getNodes().stream()
		                                                    .map(n -> n.getLabel().replaceAll(WHITESPACE_REGEX_PATTERN, ""))
		                                                    .collect(Collectors.toList());
		assertEquals(expectedNodeLabels.size(), actualNodesLabels.size());
		assertTrue(CollectionUtils.isEqualCollection(expectedNodeLabels, actualNodesLabels));
	}


	@Test
	public void createNetworkVerifyNodesTitles()
	{
		// Expected titles (tooltips)
		final String cvNodeTitleFormat = VIS_TABLE_START + VIS_HEADER_ROW_START + VIS_HEADER_COL1_START + "%s" + VIS_HEADER_COL_END + VIS_HEADER_COL2_START + "%s"
				+ VIS_HEADER_COL_END + VIS_HEADER_ROW_END
				+ VIS_ROW_START + VIS_COL1_START + "%s" + VIS_COL1_END + VIS_COL2_START + VIS_WHITESPACE + VIS_COL2_END + VIS_ROW_END
				+ VIS_ROW_START + VIS_COL1_START + "%s" + VIS_COL1_END + VIS_COL2_START + "%s" + VIS_COL2_END + VIS_ROW_END
				+ VIS_ROW_START + VIS_COL1_START + "%s" + VIS_COL1_END + VIS_COL2_START + "%s" + VIS_COL2_END + VIS_ROW_END
				+ VIS_ROW_START + VIS_COL1_START + "%s" + VIS_COL1_END + VIS_COL2_START + "%s" + VIS_COL2_END + VIS_ROW_END
				+ VIS_ROW_START + VIS_COL1_START + "%s" + VIS_COL1_END + VIS_COL2_START + "%s" + VIS_COL2_END + VIS_ROW_END
				+ VIS_TABLE_END;
		final String cvNodeTitle = String.format(cvNodeTitleFormat, "CatalogVersion", VIS_WHITESPACE, "integrationKey", "version",
				"Edm.String", "integrationKey", "Edm.String", "catalog", "Catalog", "territories", "Country");
		final String productNodeTitleFormat = VIS_TABLE_START + VIS_HEADER_ROW_START + VIS_HEADER_COL1_START + "%s" + VIS_HEADER_COL_END + VIS_HEADER_COL2_START + "%s"
				+ VIS_HEADER_COL_END + VIS_HEADER_ROW_END
				+ VIS_ROW_START + VIS_COL1_START + "%s" + VIS_COL1_END + VIS_COL2_START + VIS_WHITESPACE + VIS_COL2_END + VIS_ROW_END
				+ VIS_ROW_START + VIS_COL1_START + "%s" + VIS_COL1_END + VIS_COL2_START + "%s" + VIS_COL2_END + VIS_ROW_END
				+ VIS_ROW_START + VIS_COL1_START + "%s" + VIS_COL1_END + VIS_COL2_START + "%s" + VIS_COL2_END + VIS_ROW_END
				+ VIS_ROW_START + VIS_COL1_START + "%s" + VIS_COL1_END + VIS_COL2_START + "%s" + VIS_COL2_END + VIS_ROW_END
				+ VIS_ROW_START + VIS_COL1_START + "%s" + VIS_COL1_END + VIS_COL2_START + "%s" + VIS_COL2_END + VIS_ROW_END
				+ VIS_TABLE_END;
		final String productNodeTitle = String.format(productNodeTitleFormat, "Product", "Root", "integrationKey", "code",
				"Edm.String", "virtualInt", "Edm.Int32", "integrationKey", "Edm.String", "catalogVersion", "CatalogVersion");
		final String catalogNodeTitleFormat = VIS_TABLE_START + VIS_HEADER_ROW_START + VIS_HEADER_COL1_START + "%s" + VIS_HEADER_COL_END + VIS_HEADER_COL2_START + VIS_WHITESPACE + VIS_HEADER_COL_END + VIS_HEADER_ROW_END
				+ VIS_ROW_START + VIS_COL1_START + "%s" + VIS_COL1_END + VIS_COL2_START + VIS_WHITESPACE + VIS_COL2_END + VIS_ROW_END
				+ VIS_ROW_START + VIS_COL1_START + "%s" + VIS_COL1_END + VIS_COL2_START + "%s" + VIS_COL1_END + VIS_ROW_END
				+ VIS_ROW_START + VIS_COL1_START + "%s" + VIS_COL1_END + VIS_COL2_START + "%s" + VIS_COL1_END + VIS_ROW_END
				+ VIS_TABLE_END;
		final String catalogNodeTitle = String.format(catalogNodeTitleFormat, "Catalog", "integrationKey", "id", "Edm.String",
				"integrationKey", "Edm.String");
		final String countryNodeTitleFormat = VIS_TABLE_START + VIS_HEADER_ROW_START + VIS_HEADER_COL1_START + "%s" + VIS_HEADER_COL_END + VIS_HEADER_COL2_START + VIS_WHITESPACE + VIS_HEADER_COL_END + VIS_HEADER_ROW_END
				+ VIS_ROW_START + VIS_COL1_START + "%s" + VIS_COL1_END + VIS_COL2_START + VIS_WHITESPACE + VIS_COL2_END + VIS_ROW_END
				+ VIS_ROW_START + VIS_COL1_START + "%s" + VIS_COL1_END + VIS_COL2_START + "%s" + VIS_COL1_END + VIS_ROW_END
				+ VIS_ROW_START + VIS_COL1_START + "%s" + VIS_COL1_END + VIS_COL2_START + "%s" + VIS_COL1_END + VIS_ROW_END
				+ VIS_TABLE_END;
		final String countryNodeTitle = String.format(countryNodeTitleFormat, "Country", "integrationKey", "isocode",
				"Edm.String", "integrationKey", "Edm.String");
		final List<String> expectedNodeTitles = Arrays.asList(cvNodeTitle, productNodeTitle, catalogNodeTitle, countryNodeTitle);

		final Collection<Node> actualNodes = network.getNodes();
		final List<String> actualNodeTitles = new ArrayList<>();
		actualNodes.forEach(actualNode -> actualNodeTitles.add(actualNode.getTitle()));
		assertEquals(expectedNodeTitles.size(), actualNodeTitles.size());
		assertTrue(CollectionUtils.isEqualCollection(expectedNodeTitles, actualNodeTitles));
	}

	@Test
	public void nodeMaxLength()
	{
		final List<EDMXEntityType> edmxEntityTypes = schema.getEdmxEntityTypes();
		assertEquals(4, edmxEntityTypes.size());
		edmxEntityTypes.forEach((edmxEntityType) -> {
			final int maxLengthForTheNode = networkDataBuilder.maxLength(edmxEntityType);
			switch (edmxEntityType.getName())
			{
				case "CatalogVersion":
				case "Catalog":
				case "Country":
					assertEquals(24, maxLengthForTheNode);
					break;
				case "Product":
					assertEquals(28, maxLengthForTheNode);
					break;
			}
		});
	}

	@Test
	public void createNetworkVerifyEdgesTest()
	{
		final Collection<Edge> edges = network.getEdges();
		final Collection<Node> nodes = network.getNodes();

		final Node productNode;
		final Node catalogNode;
		final Node cvNode;
		final Node countryNode;
		productNode = nodes.stream()
		                   .filter(node -> node.getLabel()
		                                       .replaceAll(WHITESPACE_REGEX_PATTERN, "")
		                                       .equals(EXPECTED_PRODUCT_NODE_LABEL))
		                   .findFirst()
		                   .get();
		catalogNode = nodes.stream()
		                   .filter(node -> node.getLabel()
		                                       .replaceAll(WHITESPACE_REGEX_PATTERN, "")
		                                       .equals(EXPECTED_CATALOG_NODE_LABEL))
		                   .findFirst()
		                   .get();
		cvNode = nodes.stream()
		              .filter(node -> node.getLabel().replaceAll(WHITESPACE_REGEX_PATTERN, "").equals(EXPECTED_CV_NODE_LABEL))
		              .findFirst()
		              .get();
		countryNode = nodes.stream()
		                   .filter(node -> node.getLabel()
		                                       .replaceAll(WHITESPACE_REGEX_PATTERN, "")
		                                       .equals(EXPECTED_COUNTRY_NODE_LABEL))
		                   .findFirst()
		                   .get();

		assertEquals(3, edges.size());

		for (final Edge edge : edges)
		{
			if (edge.getFromNode().equals(productNode))
			{
				assertEquals(cvNode, edge.getToNode());
				assertEquals("0..1", edge.getLabel());
			}
			else if (edge.getFromNode().equals(cvNode) && !edge.getToNode().equals(countryNode))
			{
				assertEquals(catalogNode, edge.getToNode());
				assertEquals("0..1", edge.getLabel());
			}
			else if (edge.getFromNode().equals(cvNode) && !edge.getToNode().equals(catalogNode))
			{
				assertEquals(countryNode, edge.getToNode());
				assertEquals("*", edge.getLabel());
			}
			else
			{
				// Any other condition should result in test failure.
				fail();
			}
		}
	}

}
