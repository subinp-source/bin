/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.modals.builders;

import de.hybris.platform.integrationbackoffice.widgets.modals.data.visualizer.EDMXAssociation;
import de.hybris.platform.integrationbackoffice.widgets.modals.data.visualizer.EDMXEntityType;
import de.hybris.platform.integrationbackoffice.widgets.modals.data.visualizer.EDMXNavigationProperty;
import de.hybris.platform.integrationbackoffice.widgets.modals.data.visualizer.EDMXSchema;
import de.hybris.platform.integrationservices.util.Log;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.slf4j.Logger;

import com.hybris.cockpitng.components.visjs.network.data.Arrow;
import com.hybris.cockpitng.components.visjs.network.data.Arrows;
import com.hybris.cockpitng.components.visjs.network.data.Color;
import com.hybris.cockpitng.components.visjs.network.data.Edge;
import com.hybris.cockpitng.components.visjs.network.data.Font;
import com.hybris.cockpitng.components.visjs.network.data.Hierarchical;
import com.hybris.cockpitng.components.visjs.network.data.Interaction;
import com.hybris.cockpitng.components.visjs.network.data.Layout;
import com.hybris.cockpitng.components.visjs.network.data.Network;
import com.hybris.cockpitng.components.visjs.network.data.Node;
import com.hybris.cockpitng.components.visjs.network.data.NodeColor;
import com.hybris.cockpitng.components.visjs.network.data.Options;
import com.hybris.cockpitng.components.visjs.network.data.Physics;
import com.hybris.cockpitng.components.visjs.network.data.Smooth;
import com.hybris.cockpitng.components.visjs.network.data.WidthConstraint;

public class NetworkDataBuilder
{
	private static final Logger LOG = Log.getLogger(NetworkDataBuilder.class);

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
	private static final int FONT_SIZE = 20;
	private static final int COL_SPLIT = 3;
	private static final int LEVEL_SPLIT = 3;
	private static final double FONT_RATIO = 1.618;
	private static final double HEIGHT_RATIO = 1.5;
	private static final String ROOT = "Root";
	private final String rootNode;
	private int maxWidth = 0;
	private int maxHeight = 0;
	private final InputStream inputStream;

	public NetworkDataBuilder(final InputStream inputStream, final String rootNodeName)
	{
		this.inputStream = inputStream;
		this.rootNode = rootNodeName;
	}

	public Options getNetworkOptions()
	{
		final int MINIMUM_SEPARATION = 220;
		return new Options.Builder()
				.withPhysics(new Physics.Builder()
						.withEnabled(false).build())
				.withLayout(new Layout.Builder()
						.withHierarchical(new Hierarchical.Builder()
								.withSortMethod("hubsize")
								.withDirection("DU")
								.withNodeSpacing((maxWidth + COL_SPLIT) * (int) Math.ceil(FONT_SIZE / FONT_RATIO))
								.withLevelSeparation(
										Math.max((maxHeight + LEVEL_SPLIT) * (int) Math.ceil(FONT_SIZE * HEIGHT_RATIO),
												MINIMUM_SEPARATION))
								.withBlockShifting(true)
								.withEdgeMinimization(true)
								.withEnabled(true)
								.build())
						.build())
				.withInteraction(new Interaction.Builder().withNavigationButtons(true).build())
				.build();
	}

	public Network generateNetwork()
	{
		try
		{
			final EDMXSchema edmxSchemaData = getSchemaData(inputStream);
			return createNetwork(edmxSchemaData);
		}
		catch (final JAXBException | XMLStreamException e)
		{
			LOG.error("Exception occurred while parsing the EDMX file", e);
			return new Network(Collections.emptyList(), Collections.emptyList());
		}
	}

	public int maxLength(final EDMXEntityType edmxEntityType)
	{
		final int maxLengthOfNavigationProperties = edmxEntityType.getNavigationProperties().stream()
		                                                          .mapToInt(edmxNavigationProperty -> edmxNavigationProperty.getName()
		                                                                                                              .length() + edmxNavigationProperty
				                                                    .getToRole()
				                                                    .length()).max().orElse(0);
		final int maxLengthOfProperties = edmxEntityType.getProperties()
		                                                .stream()
		                                                .mapToInt(
				                                          edmxProperty -> edmxProperty.getName().length() + edmxProperty.getType()
				                                                                                                        .length())
		                                                .max()
		                                                .orElse(0);
		int lengthOfEntityName = edmxEntityType.getName().length();
		if (isEntityRoot(edmxEntityType.getName()))
		{
			lengthOfEntityName += ROOT.length();
		}
		return Math.max(lengthOfEntityName, Math.max(maxLengthOfNavigationProperties, maxLengthOfProperties));
	}

	public EDMXSchema getSchemaData(final InputStream inputStream) throws XMLStreamException, JAXBException
	{
		final XMLInputFactory xif = XMLInputFactory.newFactory();
		// or disallow DTDs entirely
		xif.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
		final XMLStreamReader xsr = xif.createXMLStreamReader(inputStream);

		// Advance to edmx:Edmx
		xsr.nextTag();
		xsr.nextTag();
		xsr.nextTag();

		final JAXBContext jaxbContext = JAXBContext.newInstance(EDMXSchema.class);
		final Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		final JAXBElement<EDMXSchema> schema = jaxbUnmarshaller.unmarshal(xsr, EDMXSchema.class);
		return schema.getValue();
	}

	public Network createNetwork(final EDMXSchema edmxSchema)
	{
		final Map<String, Node> nodeMap = new HashMap<>();
		final List<Edge> edges = new ArrayList<>();

		StringBuilder nodeLabelBuilder = new StringBuilder();
		StringBuilder nodeTooltipBuilder = new StringBuilder();

		int nodeId = 0;

		final PriorityQueue<EDMXEntityType> navigationPropertyPriority = composePriorityQueue(edmxSchema.getEdmxEntityTypes());
		for (final EDMXEntityType EDMXEntityType : navigationPropertyPriority)
		{
			composeNodeStringBuilders(nodeLabelBuilder, nodeTooltipBuilder, EDMXEntityType);
			final Node newNode = new Node.Builder()
					.withId(nodeId++ + "")
					.withLabel(nodeLabelBuilder.toString())
					.withShape("box")
					.withTitle(nodeTooltipBuilder.toString())
					.withFont(new Font.Builder()
							.withFace("Monospace")
							.withSize(FONT_SIZE)
							.withAlign("left")
							.withMulti(false)
							.build())
					.withWidthConstraint(new WidthConstraint.Builder()
							.withMaximum((maxWidth + COL_SPLIT) * (int) Math.ceil(FONT_SIZE / FONT_RATIO))
							.build())
					.withColor(new NodeColor.Builder()
							.withBackground("#ffffff")
							.withHighlight(new Color.Builder()
									.withBackground("#d5eeff")
									.build())
							.build())
					.build();
			nodeMap.put(EDMXEntityType.getName(), newNode);

			nodeLabelBuilder = new StringBuilder();
			nodeTooltipBuilder = new StringBuilder();
		}

		final double ROUNDNESS_VALUE = 0.1;
		final int EDGE_LABEL_FONT_SIZE = 28;
		final Map<String, EDMXAssociation> mapOfEDMXAssociation = edmxSchema.getMapOfEDMXAssociation();

		for (final EDMXEntityType EDMXEntityType : edmxSchema.getEdmxEntityTypes())
		{
			for (final EDMXNavigationProperty navProperty : EDMXEntityType.getNavigationProperties())
			{
				String toRole = navProperty.getToRole();
				final String relationshipName = navProperty.getRelationship();
				final String associationName = relationshipName.substring(relationshipName.indexOf('.') + 1);
				final String edgeLabel = mapOfEDMXAssociation.containsKey(associationName) ? mapOfEDMXAssociation.get(associationName)
				                                                                                                 .getEdmxEnd()
				                                                                                                 .stream()
				                                                                                                 .filter(x -> x.getRole()
				                                                                                                         .equalsIgnoreCase(
						                                                                                                         navProperty
								                                                                                                         .getToRole()))
				                                                                                                 .findFirst()
				                                                                                                 .map(x -> x.getMultiplicity())
				                                                                                                 .orElse("") : "";
				if ("java.lang.String".equals(toRole) || "java.lang.Double".equals(toRole) || "java.lang.Boolean".equals(
						toRole) || "java.util.Date".equals(toRole))
				{
					toRole = toRole.substring(toRole.lastIndexOf('.') + 1);
				}
				final Edge edge = new Edge.Builder(nodeMap.get(navProperty.getFromRole()), nodeMap.get(toRole))
						.withLabel(edgeLabel)
						.withFont(new Font.Builder().withSize(EDGE_LABEL_FONT_SIZE).build())
						.withArrows(new Arrows.Builder()
								.withTo(new Arrow.Builder()
										.withEnabled(true)
										.build())
								.withFrom(new Arrow.Builder().withEnabled(true).withType("circle").build())
								.build())
						.withSmooth(new Smooth.Builder()
								.withEnabled(true)
								.withType("curvedCW")
								.withRoundness(ROUNDNESS_VALUE)
								.build())
						.withArrowStrikethrough(false)
						.build();
				edges.add(edge);
			}
		}
		return new Network(nodeMap.values(), edges);
	}

	private boolean isEntityRoot(final String entityName)
	{
		return entityName.equals(rootNode);
	}

	private void composeNodeStringBuilders(final StringBuilder nodeLabelBuilder, final StringBuilder nodeTooltipBuilder,
	                                       final EDMXEntityType edmxEntityType)
	{
		final int maxLength = maxLength(edmxEntityType);
		if (maxWidth < maxLength)
		{
			maxWidth = maxLength;
		}
		if (isEntityRoot(edmxEntityType.getName()))
		{
			nodeLabelBuilder.append(createRowText(edmxEntityType.getName(), ROOT, maxLength));
		}
		else
		{
			nodeLabelBuilder.append(createRowText(edmxEntityType.getName(), "", maxLength));
		}
		nodeLabelBuilder.append(createSeparatorLine(maxLength));
		nodeTooltipBuilder.append(VIS_TABLE_START);
		composeNodeTooltipHeader(nodeTooltipBuilder, edmxEntityType);

		if (edmxEntityType.getEdmxKey() != null && edmxEntityType.getEdmxKey().getEdmxPropertyRef() != null)
		{
			final String keyName = edmxEntityType.getEdmxKey().getEdmxPropertyRef().getName();
			nodeLabelBuilder.append(createRowText(keyName, "", maxLength));
			composeNodeTooltipBody(nodeTooltipBuilder, keyName, VIS_WHITESPACE);
		}

		for (de.hybris.platform.integrationbackoffice.widgets.modals.data.visualizer.EDMXProperty EDMXProperty : edmxEntityType.getProperties())
		{
			nodeLabelBuilder.append(createRowText(EDMXProperty.getName(), EDMXProperty.getType(), maxLength));
			composeNodeTooltipBody(nodeTooltipBuilder, EDMXProperty.getName(), EDMXProperty.getType());
		}

		for (final EDMXNavigationProperty navProperty : edmxEntityType.getNavigationProperties())
		{
			nodeLabelBuilder.append(createRowText(navProperty.getName(), navProperty.getToRole(), maxLength));
			composeNodeTooltipBody(nodeTooltipBuilder, navProperty.getName(), navProperty.getToRole());
		}

		nodeTooltipBuilder.append(VIS_TABLE_END);
	}

	private void composeNodeTooltipHeader(final StringBuilder nodeTooltipBuilder, final EDMXEntityType edmxEntityType)
	{
		nodeTooltipBuilder.append(VIS_HEADER_ROW_START);
		nodeTooltipBuilder.append(VIS_HEADER_COL1_START);
		nodeTooltipBuilder.append(edmxEntityType.getName());
		nodeTooltipBuilder.append(VIS_HEADER_COL_END);
		nodeTooltipBuilder.append(VIS_HEADER_COL2_START);
		if (isEntityRoot(edmxEntityType.getName()))
		{
			nodeTooltipBuilder.append(ROOT);
		}
		else
		{
			nodeTooltipBuilder.append(VIS_WHITESPACE);
		}
		nodeTooltipBuilder.append(VIS_HEADER_COL_END);
		nodeTooltipBuilder.append(VIS_HEADER_ROW_END);
	}

	private void composeNodeTooltipBody(final StringBuilder nodeTooltipBuilder, final String name, final String type)
	{
		nodeTooltipBuilder.append(VIS_ROW_START);
		nodeTooltipBuilder.append(VIS_COL1_START);
		nodeTooltipBuilder.append(name);
		nodeTooltipBuilder.append(VIS_COL1_END);
		nodeTooltipBuilder.append(VIS_COL2_START);
		nodeTooltipBuilder.append(type);
		nodeTooltipBuilder.append(VIS_COL2_END);
		nodeTooltipBuilder.append(VIS_ROW_END);
	}

	private PriorityQueue<EDMXEntityType> composePriorityQueue(final List<EDMXEntityType> entities)
	{
		final Comparator<EDMXEntityType> entityTypeComparator = Comparator.comparing((EDMXEntityType e) -> {
			int totalAttributes = 0;
			totalAttributes += e.getNavigationProperties() != null ? e.getNavigationProperties().size() : 0;
			totalAttributes += e.getProperties() != null ? e.getProperties().size() : 0;
			if (totalAttributes > maxHeight)
			{
				maxHeight = totalAttributes;
			}
			return totalAttributes;
		});
		final PriorityQueue<EDMXEntityType> navigationPropertyPriority = new PriorityQueue<>(entityTypeComparator);
		navigationPropertyPriority.addAll(entities);
		return navigationPropertyPriority;
	}

	private String createRowText(final String name, final String type, int maxLength)
	{
		final int MINIMUM_SPACE = 3;
		maxLength = maxLength + MINIMUM_SPACE;
		final int totalTextLength = name.length() + type.length();
		final int spaceLength = maxLength - totalTextLength;
		final StringBuilder space = new StringBuilder();
		space.append(" ");
		for (int i = 1; i < spaceLength; i++)
		{
			space.append(" ");
		}
		return name + space.toString() + type + "\n";

	}

	private String createSeparatorLine(int maxLength)
	{
		final int MINIMUM_SPACE = 3;
		maxLength += MINIMUM_SPACE;
		final StringBuilder br = new StringBuilder();
		for (int i = 0; i < maxLength; i++)
		{
			br.append("-");
		}
		br.append("\n");
		return br.toString();
	}
}
