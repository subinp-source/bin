/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package ydocumentcartpackage.persistence.polyglot.repository.documentcart.serializer.json;


import de.hybris.platform.persistence.polyglot.PolyglotPersistence;
import de.hybris.platform.persistence.polyglot.model.Identity;
import de.hybris.platform.persistence.polyglot.model.LocalizedKey;
import de.hybris.platform.persistence.polyglot.model.Reference;
import de.hybris.platform.persistence.polyglot.model.SerializableValue;
import de.hybris.platform.persistence.polyglot.model.SingleAttributeKey;
import de.hybris.platform.util.Config;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.lang3.SerializationUtils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableSet;

import ydocumentcartpackage.persistence.polyglot.repository.documentcart.Document;
import ydocumentcartpackage.persistence.polyglot.repository.documentcart.Entity;
import ydocumentcartpackage.persistence.polyglot.repository.documentcart.Entity.EntityBuilder;
import ydocumentcartpackage.persistence.polyglot.repository.documentcart.serializer.Serializer;


public class JsonSerializer implements Serializer
{
	protected static final String TYPE = "_t";
	protected static final String VALUE = "value";
	protected static final String ROOT_TYPE = "DOCUMENT";
	protected static final String ROOT_ID = "id";
	protected static final String ROOT_VERSION = "version";
	protected static final String ROOT_ENTITIES = "entities";
	protected static final String LONG = "L";
	protected static final String STRING = "S";
	protected static final String DOUBLE = "D";
	protected static final String BOOLEAN = "B";
	protected static final String INTEGER = "I";
	protected static final String FLOAT = "F";
	protected static final String BIG_DECIMAL = "BD";
	protected static final String REFERENCE = "R";
	protected static final String IDENTITY = "ID";
	protected static final String DATE = "T";
	protected static final String SERIALIZABLE = "SR";
	protected static final String LIST = List.class.getSimpleName().toUpperCase();
	protected static final String LANGUAGE_SEPARATOR = ":";
	private static final String CLASS = "_c";
	private static final String SUPPORTED_SERIALIZABLE_TYPES = "ydocumentcart.storage.jsonSerializer.supportedSerializableTypes";

	private final ObjectMapper objectMapper;

	public JsonSerializer()
	{
		this(getSupportedSerializableTypesFromConfig());
	}

	public JsonSerializer(final Set<String> supportedSerializableTypes)
	{

		this.objectMapper = configureObjectMapper(ImmutableSet.copyOf(supportedSerializableTypes));
	}

	private static Set<String> getSupportedSerializableTypesFromConfig()
	{
		return ImmutableSet.copyOf(
				Splitter.on(",").trimResults().omitEmptyStrings().split(Config.getString(SUPPORTED_SERIALIZABLE_TYPES, "")));
	}

	@Override
	public String serialize(final Document document)
	{
		return serialize(new DocumentWrapper(document, null));
	}

	@Override
	public String serializeWithOverriddenVersion(final Document document, final long version)
	{
		return serialize(new DocumentWrapper(document, version));
	}

	private String serialize(final DocumentWrapper documentWrapper)
	{
		try
		{
			return objectMapper.writeValueAsString(documentWrapper);
		}
		catch (final JsonProcessingException e)
		{
			throw new JSONSerializerException(e);
		}
	}

	@Override
	public Document deserialize(final String json)
	{
		try
		{
			return objectMapper.readValue(json, Document.class);
		}
		catch (final IOException e)
		{
			throw new JSONSerializerException(e);
		}
	}

	private ObjectMapper configureObjectMapper(final Set<String> supportedSerializableTypes)
	{

		final ObjectMapper mapper = new ObjectMapper();
		final SimpleModule module = new SimpleModule();

		module.addSerializer(DocumentWrapper.class, new DocumentSerializer(supportedSerializableTypes));
		module.addDeserializer(Document.class, new DocumentDeserializer(supportedSerializableTypes));

		mapper.registerModule(module);

		return mapper;
	}

	protected static class DocumentSerializer extends StdSerializer<DocumentWrapper>
	{
		private final Set<String> supportedSerializableTypes;

		/**
			Please use {@code DocumentSerializer(Set<String>)}
		*/
		@Deprecated
		protected DocumentSerializer()
		{
			this(Set.of());
		}

		protected DocumentSerializer(final Set<String> supportedSerializableTypes)
		{
			super(DocumentWrapper.class);
			this.supportedSerializableTypes = ImmutableSet.copyOf(supportedSerializableTypes);
		}

		@Override
		public void serialize(final DocumentWrapper wrapper, final JsonGenerator gen, final SerializerProvider prov)
				throws IOException
		{
			final Document document = wrapper.document;
			gen.writeStartObject();
			gen.writeStringField(TYPE, ROOT_TYPE);
			gen.writeNumberField(ROOT_ID, document.getRootId().toLongValue());
			gen.writeNumberField(ROOT_VERSION, wrapper.versionToOverride.orElse(document.getVersion()));
			gen.writeFieldName(ROOT_ENTITIES);


			gen.writeStartArray();
			try
			{
				document.allEntities().forEach(i -> serializeEntity(i, gen));
			}
			catch (final WrappedIOException e)
			{
				throw e.unwrap();
			}
			gen.writeEndArray();
			gen.writeEndObject();
		}

		protected void serializeEntity(final Entity entity, final JsonGenerator gen)
		{
			try
			{
				gen.writeStartObject();

				entity.forEveryAttribute((key, value) -> {
					String fieldName = key.getQualifier();
					if (key instanceof LocalizedKey)
					{
						fieldName += LANGUAGE_SEPARATOR + ((LocalizedKey) key).getLanguageCode();
					}
					try
					{
						gen.writeFieldName(fieldName);
						writeEntityField(value, gen);
					}
					catch (final IOException e)
					{
						throw new WrappedIOException(e);
					}
				});

				gen.writeEndObject();
			}
			catch (final IOException e)
			{
				throw new WrappedIOException(e);
			}
		}

		protected void writeEntityField(final Object value, final JsonGenerator gen) throws IOException
		{
			gen.writeStartObject();
			if (value instanceof String)
			{
				gen.writeStringField(TYPE, STRING);
				gen.writeStringField(VALUE, (String) value);
			}
			else if (value instanceof Long)
			{
				gen.writeStringField(TYPE, LONG);
				gen.writeNumberField(VALUE, (Long) value);
			}
			else if (value instanceof Double)
			{
				gen.writeStringField(TYPE, DOUBLE);
				gen.writeNumberField(VALUE, (Double) value);
			}
			else if (value instanceof Boolean)
			{
				gen.writeStringField(TYPE, BOOLEAN);
				gen.writeBooleanField(VALUE, (Boolean) value);
			}
			else if (value instanceof Integer)
			{
				gen.writeStringField(TYPE, INTEGER);
				gen.writeNumberField(VALUE, (Integer) value);
			}
			else if (value instanceof Float)
			{
				gen.writeStringField(TYPE, FLOAT);
				gen.writeNumberField(VALUE, (Float) value);
			}
			else if (value instanceof Reference)
			{
				gen.writeStringField(TYPE, REFERENCE);
				gen.writeNumberField(VALUE, ((Reference) value).getIdentity().toLongValue());
			}
			else if (value instanceof Identity)
			{
				gen.writeStringField(TYPE, IDENTITY);
				gen.writeNumberField(VALUE, ((Identity) value).toLongValue());
			}
			else if (value instanceof Date)
			{
				gen.writeStringField(TYPE, DATE);
				gen.writeObjectField(VALUE, ((Date) value).getTime());
			}
			else if (value instanceof BigDecimal)
			{
				gen.writeStringField(TYPE, BIG_DECIMAL);
				gen.writeObjectField(VALUE, value);
			}
			else if (value instanceof Collection<?>)
			{
				writeCollection((Collection<?>) value, gen);
			}
			else if (value instanceof SerializableValue)
			{
				final SerializableValue serializableValue = (SerializableValue) value;
				if (isSupportedSerializableType(serializableValue))
				{
					writeSerializable(serializableValue, gen);
				}
				else
				{
					throw new UnsupportedOperationException(
							"Unsupported serializable type detected '" + (serializableValue).getSerializableObject()
							                                                                .getClass() + "'.");
				}
			}
			else
			{
				throw new UnsupportedOperationException("Unsupported type detected '" + value.getClass() + "'.");
			}
			gen.writeEndObject();
		}

		private void writeSerializable(final SerializableValue value, final JsonGenerator gen) throws IOException
		{
			gen.writeStringField(TYPE, SERIALIZABLE);
			gen.writeStringField(CLASS, value.getSerializableObject().getClass().getName());
			gen.writeBinaryField(VALUE, SerializationUtils.serialize(value.getSerializableObject()));
		}

		private boolean isSupportedSerializableType(final SerializableValue value)
		{
			return supportedSerializableTypes.contains(value.getSerializableObject().getClass().getName());
		}

		private void writeCollection(final Collection<?> collection, final JsonGenerator gen) throws IOException
		{
			if (collection instanceof List<?>)
			{
				gen.writeStringField(TYPE, List.class.getSimpleName().toUpperCase());
			}
			else
			{
				throw new UnsupportedOperationException("Unsupported type detected '" + collection.getClass() + "'.");
			}
			gen.writeFieldName(VALUE);
			gen.writeStartArray(collection.size());
			for (final Object v : collection)
			{
				writeEntityField(v, gen);
			}
			gen.writeEndArray();
		}
	}

	protected static class DocumentDeserializer extends StdDeserializer<Document>
	{
		private final Set<String> supportedSerializableTypes;

		/**
			Please use {@code DocumentDeserializer(Set<String>)}
		*/
		@Deprecated
		protected DocumentDeserializer()
		{
			this(Set.of());
		}

		protected DocumentDeserializer(final Set<String> supportedSerializableTypes)
		{
			super(Document.class);
			this.supportedSerializableTypes = ImmutableSet.copyOf(supportedSerializableTypes);
		}

		@Override
		public Document deserialize(final JsonParser p, final DeserializationContext ctx)
				throws IOException
		{
			//PZU: In case of performance issues parse it directly
			final TreeNode cart = p.getCodec().readTree(p);
			final TextNode type = (TextNode) cart.get(TYPE);
			if (!ROOT_TYPE.equals(type.asText()))
			{
				throw MismatchedInputException.from(p, Document.class,
						"Expected Document type but received '" + type.asText() + "'");
			}

			final NumericNode id = (NumericNode) cart.get(ROOT_ID);
			final NumericNode version = (NumericNode) cart.get(ROOT_VERSION);
			final ArrayNode entities = (ArrayNode) cart.get(ROOT_ENTITIES);

			final Document document = new Document(PolyglotPersistence.identityFromLong(id.asLong()), version.asLong());

			StreamSupport.stream(Spliterators.spliterator(entities.elements(), entities.size(), 0), false)
			             .map(ObjectNode.class::cast).map(n -> readEntity(n, document)).forEach(document::addEntity);

			return document;
		}

		protected Entity readEntity(final ObjectNode entity, final Document document)
		{
			final EntityBuilder entityBuilder = document.newEntityBuilder();

			StreamSupport.stream(Spliterators.spliterator(entity.fields(), entity.size(), 0), false).forEach(field -> {
				final String fieldName = field.getKey();
				final int langIndex = fieldName.indexOf(LANGUAGE_SEPARATOR);
				final SingleAttributeKey key;
				if (langIndex < 0)
				{
					key = PolyglotPersistence.getNonlocalizedKey(fieldName);
				}
				else
				{
					key = PolyglotPersistence.getLocalizedKey(fieldName.substring(0, langIndex),
							fieldName.substring(langIndex + LANGUAGE_SEPARATOR.length()));
				}
				final Object value = readEntityField(field.getValue());
				entityBuilder.withAttribute(key, value);
			});

			return entityBuilder.build();
		}

		protected Object readEntityField(final JsonNode valueNode)
		{
			final JsonNode value = valueNode.get(VALUE);
			final String valueType = valueNode.get(TYPE).asText();

			if (LIST.equals(valueType))
			{
				return readListField((ArrayNode) value);
			}

			switch (valueType)
			{
				case STRING:
					return value.asText();
				case LONG:
					return value.asLong();
				case DOUBLE:
					return value.asDouble();
				case BOOLEAN:
					return value.asBoolean();
				case INTEGER:
					return value.asInt();
				case FLOAT:
					return Float.parseFloat(value.asText());
				case BIG_DECIMAL:
					return new BigDecimal(value.asText());
				case REFERENCE:
					return PolyglotPersistence.getReferenceTo(PolyglotPersistence.identityFromLong(value.asLong()));
				case IDENTITY:
					return PolyglotPersistence.identityFromLong(value.asLong());
				case DATE:
					return new Date(value.asLong());
				case SERIALIZABLE:
					return readSerializableField(valueNode, value);
				default:
					throw new UnsupportedOperationException("Unexpected value type detected '" + valueType + "'");
			}
		}

		private Object readSerializableField(final JsonNode valueNode, final JsonNode value)
		{
			try
			{
				final String serializedType = valueNode.get(CLASS).asText();
				if (!supportedSerializableTypes.contains(serializedType))
				{
					throw new UnsupportedOperationException(
							"Unexpected serialized value of type '" + serializedType + "' detected");
				}

				return PolyglotPersistence.getSerializableValue(SerializationUtils.deserialize(value.binaryValue()));
			}
			catch (final IOException e)
			{
				throw new WrappedIOException(e);
			}
		}

		private Object readListField(final ArrayNode array)
		{
			if (array.size() == 0)
			{
				return Collections.emptyList();
			}
			return StreamSupport.stream(Spliterators.spliterator(array.elements(), array.size(), 0), false)
			                    .map(this::readEntityField).collect(Collectors.toList());
		}

	}

	private static class DocumentWrapper
	{
		private final Document document;
		private final Optional<Long> versionToOverride;

		public DocumentWrapper(final Document document, final Long versionToOverride)
		{
			this.document = document;
			this.versionToOverride = Optional.ofNullable(versionToOverride);
		}
	}

	private static final class WrappedIOException extends RuntimeException
	{
		private final IOException wrappedException;

		private WrappedIOException(final IOException exceptionToWrap)
		{
			super(exceptionToWrap);
			wrappedException = exceptionToWrap;
		}

		public IOException unwrap()
		{
			return wrappedException;
		}
	}

	public static final class JSONSerializerException extends RuntimeException
	{
		public JSONSerializerException(final Throwable cause)
		{
			super(cause);
		}
	}
}
