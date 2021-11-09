/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package ydocumentcartpackage.persistence.polyglot.repository.documentcart.serializer.json;

import static de.hybris.platform.persistence.polyglot.PolyglotPersistence.getNonlocalizedKey;
import static de.hybris.platform.persistence.polyglot.PolyglotPersistence.getSerializableValue;
import static de.hybris.platform.persistence.polyglot.PolyglotPersistence.identityFromLong;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.order.EntryGroup;
import de.hybris.platform.persistence.polyglot.model.Identity;
import de.hybris.platform.util.Key;

import java.util.Set;

import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;

import ydocumentcartpackage.persistence.polyglot.repository.documentcart.Document;
import ydocumentcartpackage.persistence.polyglot.repository.documentcart.Entity;

@UnitTest
public class JsonSerializerTest
{


	@Test
	public void shouldFailOnSavingUnsupportedSerializableType()
	{
		final Identity id = identityFromLong(RandomUtils.nextLong());

		final Document document = new Document(id);
		final Entity entity = document.newEntityBuilder()
		                              .withAttribute(getNonlocalizedKey("attr"), getSerializableValue(new EntryGroup()))
		                              .withAttribute(getNonlocalizedKey("invAttr"),
				                              getSerializableValue(Key.create("left", "right")))
		                              .withId(identityFromLong(RandomUtils.nextLong()))
		                              .build();
		document.addEntity(entity);

		final JsonSerializer jsonSerializer = new JsonSerializer(Set.of(EntryGroup.class.getName()));
		assertThatThrownBy(() -> jsonSerializer.serialize(document)).hasMessageContaining("Unsupported serializable type detected")
		                                                            .hasMessageContaining(Key.class.getName());

	}

	@Test
	public void shouldFailOnDeserializeUnsupportedSerializableType()
	{

		final Identity id = identityFromLong(RandomUtils.nextLong());

		final Document document = new Document(id);
		final Entity entity = document.newEntityBuilder()
		                              .withAttribute(getNonlocalizedKey("attr"), getSerializableValue(new EntryGroup()))
		                              .withAttribute(getNonlocalizedKey("invAttr"),
				                              getSerializableValue(Key.create("left", "right")))
		                              .withId(identityFromLong(RandomUtils.nextLong()))
		                              .build();
		document.addEntity(entity);

		final String json = new JsonSerializer(Set.of(EntryGroup.class.getName(), Key.class.getName())).serialize(document);

		final JsonSerializer deserializer = new JsonSerializer(Set.of(EntryGroup.class.getName()));
		assertThatThrownBy(() -> deserializer.deserialize(json)).hasMessageContaining(Key.class.getName());
	}

}
