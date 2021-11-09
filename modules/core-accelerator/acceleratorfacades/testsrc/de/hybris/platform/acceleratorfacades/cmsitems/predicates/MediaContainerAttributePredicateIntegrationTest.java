/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorfacades.cmsitems.predicates;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.acceleratorcms.model.components.AbstractMediaContainerComponentModel;
import de.hybris.platform.cmsfacades.common.predicate.DefaultClassTypeAttributePredicate;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.type.TypeService;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class MediaContainerAttributePredicateIntegrationTest extends ServicelayerTest
{
	@Resource
	private TypeService typeService;

	private AttributeDescriptorModel mediaAttribute;

	@Resource
	private DefaultClassTypeAttributePredicate cmsMediaContainerTypeAttributePredicate;

	@Before
	public void setup()
	{
		final ComposedTypeModel composedType = typeService.getComposedTypeForCode(
				AbstractMediaContainerComponentModel._TYPECODE);

		this.mediaAttribute = composedType //
				.getDeclaredattributedescriptors() //
				.stream() //
				.filter(attribute -> attribute.getQualifier().equals("media")) //
				.findFirst() //
				.get();
	}

	@Test
	public void testMediaContainerPredicateAttributeIsValid()
	{
		assertThat(Boolean.valueOf(cmsMediaContainerTypeAttributePredicate.test(mediaAttribute)), is(Boolean.TRUE));
	}
}
