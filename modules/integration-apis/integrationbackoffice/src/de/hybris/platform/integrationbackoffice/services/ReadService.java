/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.services;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.AtomicTypeModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.CollectionTypeModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.MapTypeModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.inboundservices.model.InboundChannelConfigurationModel;
import de.hybris.platform.integrationservices.config.ReadOnlyAttributesConfiguration;
import de.hybris.platform.integrationservices.model.IntegrationObjectModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectVirtualAttributeDescriptorModel;
import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.odata2services.odata.InvalidODataSchemaException;
import de.hybris.platform.odata2services.odata.schema.SchemaGenerator;
import de.hybris.platform.odata2webservices.enums.IntegrationType;
import de.hybris.platform.scripting.model.ScriptModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.type.TypeService;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.stream.XMLStreamWriter;

import org.apache.olingo.odata2.api.edm.provider.DataServices;
import org.apache.olingo.odata2.api.edm.provider.Schema;
import org.apache.olingo.odata2.core.commons.XmlHelper;
import org.apache.olingo.odata2.core.ep.producer.XmlMetadataProducer;
import org.apache.olingo.odata2.core.ep.util.CircleStreamBuffer;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;

/**
 * Handles the read requests from the extension's widgets
 */
public class ReadService
{
	private static final Logger LOG = Log.getLogger(ReadService.class);
	private FlexibleSearchService flexibleSearchService;
	private TypeService typeService;
	private SchemaGenerator oDataDefaultSchemaGenerator;
	private ReadOnlyAttributesConfiguration readOnlyAttributesConfiguration;

	@Required
	public void setODataDefaultSchemaGenerator(final SchemaGenerator oDataDefaultSchemaGenerator)
	{
		this.oDataDefaultSchemaGenerator = oDataDefaultSchemaGenerator;
	}

	@Required
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}

	@Required
	public void setTypeService(final TypeService typeService)
	{
		this.typeService = typeService;
	}

	@Required
	public void setReadOnlyAttributesConfiguration(final ReadOnlyAttributesConfiguration readOnlyAttributesConfiguration)
	{
		this.readOnlyAttributesConfiguration = readOnlyAttributesConfiguration;
	}

	/**
	 * Checks whether a TypeModel's attribute type is a collection type or a flexible collection type
	 *
	 * @param attributeType the attribute type to evaluate
	 * @return if the attribute type is of CollectionType
	 */
	public boolean isCollectionType(final String attributeType)
	{
		return typeService.isAssignableFrom("CollectionType", attributeType);
	}

	/**
	 * Checks whether a TypeModel's attribute type is a composed type or a flexible composed type
	 *
	 * @param attributeType the attribute type to evaluate
	 * @return if the attribute type is of ComposedType
	 */
	public boolean isComposedType(final String attributeType)
	{
		return typeService.isAssignableFrom("ComposedType", attributeType);
	}

	/**
	 * Checks whether a TypeModel's attribute type is an enumeration meta type or a flexible enumeration meta type
	 *
	 * @param attributeType the attribute type to evaluate
	 * @return if the attribute type is of EnumerationMetaType
	 */
	public boolean isEnumerationMetaType(final String attributeType)
	{
		return typeService.isAssignableFrom("EnumerationMetaType", attributeType);
	}

	/**
	 * Checks whether a TypeModel's attribute type is an atomic type or a flexible atomic type
	 *
	 * @param attributeType the attribute type to evaluate
	 * @return if the attribute type is of AtomicType
	 */
	public boolean isAtomicType(final String attributeType)
	{
		return typeService.isAssignableFrom("AtomicType", attributeType);
	}

	/**
	 * Checks whether a TypeModel's attribute type is a map type or a flexible map type
	 *
	 * @param attributeType the attribute type to evaluate
	 * @return if the attribute type is of MapType
	 */
	public boolean isMapType(final String attributeType)
	{
		return typeService.isAssignableFrom("MapType", attributeType);
	}

	/**
	 * Checks whether a TypeModel is a ComposedType or an EnumerationMetaType
	 *
	 * @param typeModel the type model to evaluate
	 * @return if the attribute is a complex type
	 */
	public boolean isComplexType(final TypeModel typeModel)
	{
		return isComposedType(typeModel.getItemtype()) || isEnumerationMetaType(typeModel.getItemtype());
	}

	/**
	 * Determines if the given code is assignable as a ProductModel or one of its subtypes
	 *
	 * @param code the code of the type model
	 * @return if the model code is assignable as a Product type/subtype
	 */
	public boolean isProductType(final String code)
	{
		return typeService.isAssignableFrom(ProductModel._TYPECODE, code);
	}

	/**
	 * Gets a CollectionType's or MapType's element's ComposedTypeModel (if it is a collection of complex types)
	 *
	 * @param attributeDescriptorModel the attribute descriptor of the collection
	 * @return the collection's element's ComposedTypeModel
	 */
	public ComposedTypeModel getComplexTypeForAttributeDescriptor(final AttributeDescriptorModel attributeDescriptorModel)
	{
		final TypeModel typeModel = attributeDescriptorModel.getAttributeType();
		return getComposedTypeModelFromTypeModel(typeModel);
	}

	/**
	 * Attempts to acquire the ComposedTypeModel from a TypeModel based on which inherited class it belongs to.
	 *
	 * @param typeModel the typeModel that will attempt to be casted
	 * @return the element's ComposedTypeModel representation
	 */
	public ComposedTypeModel getComposedTypeModelFromTypeModel(final TypeModel typeModel)
	{
		if (isComplexType(typeModel))
		{
			return (ComposedTypeModel) typeModel;
		}
		else if (isCollectionType(typeModel.getItemtype()))
		{
			final TypeModel elementTypeModel = ((CollectionTypeModel) typeModel).getElementType();
			if (isComplexType(elementTypeModel))
			{
				return (ComposedTypeModel) elementTypeModel;
			}
			else
			{
				return null;
			}
		}
		if (isMapType(typeModel.getItemtype()))
		{
			final MapTypeModel mapTypeModel = (MapTypeModel) typeModel;
			if (mapTypeModel.getReturntype() instanceof MapTypeModel)
			{
				return null;
			}
			if (mapTypeModel.getReturntype() instanceof CollectionTypeModel)
			{
				final CollectionTypeModel collectionTypeModel = (CollectionTypeModel) mapTypeModel.getReturntype();
				final TypeModel elementTypeModel = collectionTypeModel.getElementType();
				if (isComplexType(elementTypeModel))
				{
					return (ComposedTypeModel) elementTypeModel;
				}
				else
				{
					return null;
				}
			}
			else
			{
				return null;
			}
		}
		return null;
	}

	/**
	 * Retrieves all IntegrationObjectModels
	 *
	 * @return list of all IntegrationObjectModels
	 */
	public List<IntegrationObjectModel> getIntegrationObjectModels()
	{
		return flexibleSearchService.<IntegrationObjectModel>search("SELECT PK FROM {IntegrationObject}").getResult();
	}

	/**
	 * Retrieves all InboundChannelConfigurationModels
	 *
	 * @return list of all InboundChannelConfigurationModels
	 */
	public List<InboundChannelConfigurationModel> getInboundChannelConfigModels()
	{
		return flexibleSearchService.<InboundChannelConfigurationModel>search(
				"SELECT PK FROM {InboundChannelConfiguration}").getResult();
	}

	/**
	 * Retrieves matching IntegrationObjectModels by code.
	 *
	 * @param code Code of the IO model
	 * @return list of IntegrationObjectModels matching criteria
	 */
	public List<IntegrationObjectModel> getIntgrationObjectModelByCode(final String code)
	{
		return flexibleSearchService.<IntegrationObjectModel>search(
				String.format("SELECT PK FROM {IntegrationObject} WHERE p_code = '%s'", code)).getResult();
	}

	/**
	 * Retrieves all available IntegrationTypes
	 *
	 * @return list of IntegrationTypes
	 */
	public List<IntegrationType> getIntegrationTypes()
	{
		// This will become a list query once more IntegrationTypes are released
		final List<IntegrationType> types = new ArrayList<>();
		types.add(IntegrationType.INBOUND);
		return types;
	}

	/**
	 * Get ComposedTypedModels from type system. The 'WHERE' clause filters relational and other non-basic ComposedType from being queried.
	 *
	 * @return list of ComposedTypeModels
	 */
	public List<ComposedTypeModel> getAvailableTypes()
	{
		final SearchResult<ComposedTypeModel> composedTypeSearchResult = flexibleSearchService.search(
				"SELECT PK FROM {composedtype} WHERE (p_sourcetype is null AND p_generate =1) OR p_sourcetype = 8796093382738");
		return composedTypeSearchResult.getResult()
		                               .stream()
		                               .filter(composedType -> !composedType.getAbstract())
		                               .collect(Collectors.toList());
	}

	/**
	 * Get ScriptModels from type system. The 'WHERE' clause filters relational and other non-basic ComposedType from being queried.
	 *
	 * @return list of ComposedTypeModels
	 */
	public List<ScriptModel> getScriptModels()
	{
		return flexibleSearchService.<ScriptModel>search("SELECT PK FROM {Script}").getResult();
	}

	/**
	 * Get IntegrationObjectVirtualAttributeDescriptorModels from type system. The 'WHERE' clause filters relational and other non-basic ComposedType from being queried.
	 * @param code Code of an IntegrationObjectVirtualAttributeDescriptorModel
	 * @return list of IntegrationObjectVirtualAttributeDescriptorModels
	 */
	public List<IntegrationObjectVirtualAttributeDescriptorModel> getVirtualAttributeDescriptorModelsByCode(String code)
	{
		return flexibleSearchService.<IntegrationObjectVirtualAttributeDescriptorModel>search(
				String.format("SELECT PK FROM {IntegrationObjectVirtualAttributeDescriptor} WHERE p_code = '%s'", code)).getResult();
	}

	/**
	 * Get all IntegrationObjectVirtualAttributeDescriptorModels from type system.
	 *
	 * @return list of IntegrationObjectVirtualAttributeDescriptorModel
	 */
	public List<IntegrationObjectVirtualAttributeDescriptorModel> getVirtualAttributeDescriptorModels()
	{
		return flexibleSearchService.<IntegrationObjectVirtualAttributeDescriptorModel>search(	"SELECT PK FROM {IntegrationObjectVirtualAttributeDescriptor}").getResult();
	}

	/**
	 * Get the set of AttributeDescriptionModel for a given ComposedTypeModel
	 *
	 * @param type a ComposedTypeModel object
	 * @return the set of AttributeDescriptorModel of the ComposedTypeModel's attributes
	 */
	public Set<AttributeDescriptorModel> getAttributesForType(final ComposedTypeModel type)
	{
		return typeService.getAttributeDescriptorsForType(type);
	}

	/**
	 * Get an EDMX representation of a given integration object
	 *
	 * @param integrationObject an integration object to represent
	 * @return an input stream containing the EDMX representation of the integration object
	 * @throws InvalidODataSchemaException when schema generator fails
	 */
	public InputStream getEDMX(final IntegrationObjectModel integrationObject)
	{
		final Schema schema = oDataDefaultSchemaGenerator.generateSchema(integrationObject.getItems());

		final CircleStreamBuffer csb = new CircleStreamBuffer();
		final DataServices metadata = (new DataServices()).setSchemas(Collections.singletonList(schema))
		                                                  .setDataServiceVersion("2.0");

		InputStream inputStream = null;
		try (final OutputStreamWriter writer = new OutputStreamWriter(csb.getOutputStream(), StandardCharsets.UTF_8))
		{
			final XMLStreamWriter xmlStreamWriter = XmlHelper.getXMLOutputFactory().createXMLStreamWriter(writer);
			XmlMetadataProducer.writeMetadata(metadata, xmlStreamWriter, null);
			inputStream = csb.getInputStream();
		}
		catch (final Exception e)
		{
			LOG.error("Failed to generate EDMX", e);
		}

		return inputStream;
	}

	/**
	 * Provides the read-only attributes as AttributeDescriptorModels
	 *
	 * @param type Parent type that encloses this attribute
	 * @return set of read-only attributes
	 */
	public Set<AttributeDescriptorModel> getReadOnlyAttributesAsAttributeDescriptorModels(final ComposedTypeModel type)
	{
		Set<AttributeDescriptorModel> attributes = new HashSet<>();

		readOnlyAttributesConfiguration.getReadOnlyAttributes().forEach(qualifier -> {
			final AttributeDescriptorModel adm = typeService.getAttributeDescriptor(type, qualifier);
			if (adm != null)
			{
				attributes.add(adm);
			}
		});

		return attributes;
	}

	/**
	 * Provides the read-only attributes as a set of the qualifiers
	 *
	 * @return set of read-only attributes
	 */
	public Set<String> getReadOnlyAttributes()
	{
		return readOnlyAttributesConfiguration.getReadOnlyAttributes();
	}

	/**
	 * Retrieves the AtomicTypeModel present in the type system.
	 * @param code value for the AtomicTypeModel requested.
	 * @return an AtomicTypeModel for the given type or null if the type isn't found.
	 */
	public AtomicTypeModel getAtomicTypeModelByCode(final String code){

			final var sample = new AtomicTypeModel();
			sample.setCode(code);
			try
			{
				return flexibleSearchService.getModelByExample(sample);
			}
			catch (final ModelNotFoundException e)
			{
				LOG.debug("Can't find atomic type '{}'", code);
			}
		return null;
	}
}