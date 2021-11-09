/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.servicelayer.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.test.TestThreadsHolder;
import de.hybris.platform.util.Utilities;

import java.util.Collections;
import java.util.UUID;

import javax.annotation.Resource;

import org.junit.Test;


@IntegrationTest
public class RefreshModeltemConverterAndRemovingAttributeTest extends ServicelayerTest
{
	@Resource
	private ModelService modelService;
	@Resource
	private TypeService typeService;


	@Test
	public void testRemoveAttribute()
	{
		ComposedTypeModel ctm = typeService.getComposedTypeForClass(TitleModel.class);
		final String qualifier = generateQualifier();

		final AttributeDescriptorModel atm = createAttribute(ItemModel.class, qualifier);

		assertThat(typeService.getAttributeDescriptorsForType(ctm).stream().map(AttributeDescriptorModel::getQualifier))
				.contains(atm.getQualifier());

		modelService.remove(atm);
		detachAndInvalidateModel(ctm);
		detachAndInvalidateModel(atm);

		ctm = typeService.getComposedTypeForClass(TitleModel.class);

		typeService.getAttributeDescriptorsForType(ctm);

		assertThat(typeService.getAttributeDescriptorsForType(ctm).stream().map(AttributeDescriptorModel::getQualifier))
				.doesNotContain(atm.getQualifier());
	}

	@Test
	public void refreshForItemModelConverterCouldFailIfAttributeIsRemoved()
	{

		final TestThreadsHolder<Runnable> createAndRemoveAttributesThread = createRuntimeAttribsAndRemoveThem(1);
		final TestThreadsHolder<Runnable> getDeclaredAttributesThread = getDeclaredAttributesForComposedTypeThread(1);

		assertTrue("not all threads finished in time", getDeclaredAttributesThread.waitAndDestroy(1200));
		assertTrue("not all threads finished in time", createAndRemoveAttributesThread.waitAndDestroy(1200));
		assertEquals(Collections.emptyMap(), getDeclaredAttributesThread.getErrors());
		assertEquals(Collections.emptyMap(), createAndRemoveAttributesThread.getErrors());
	}

	private TestThreadsHolder<Runnable> createRuntimeAttribsAndRemoveThem(final int THREADS)
	{
		final TestThreadsHolder<Runnable> threads = new TestThreadsHolder<Runnable>(THREADS, true)
		{
			@Override
			public Runnable newRunner(final int threadNumber)
			{
				return () -> {
					for (int i = 0; i < 25; i++)
					{
						final String qualifier = generateQualifier() + "_" + i;
						final AttributeDescriptorModel atm = createAttribute(ItemModel.class, qualifier);
						modelService.remove(atm);
					}
				};
			}
		};
		threads.startAll();
		return threads;
	}

	private TestThreadsHolder<Runnable> getDeclaredAttributesForComposedTypeThread(final int THREADS)
	{
		final TestThreadsHolder<Runnable> threads = new TestThreadsHolder<Runnable>(THREADS, true)
		{
			@Override
			public Runnable newRunner(final int threadNumber)
			{
				return () -> {
					for (int i = 0; i < 25; i++)
					{
						typeService.getComposedTypeForClass(TitleModel.class).getDeclaredattributedescriptors().stream()
								.forEach(AttributeDescriptorModel::getQualifier);
						try
						{
							Thread.sleep(5);
						}
						catch (final Exception e)
						{
							e.printStackTrace();
						}
					}
				};
			}
		};
		threads.startAll();
		return threads;
	}

	AttributeDescriptorModel createAttribute(final ComposedTypeModel ctm, final String qualifier)
	{
		final AttributeDescriptorModel runtimeAttribute = modelService.create(AttributeDescriptorModel.class);
		runtimeAttribute.setAttributeType(typeService.getAtomicTypeForJavaClass(Integer.class));
		runtimeAttribute.setEnclosingType(ctm);
		runtimeAttribute.setGenerate(false);
		runtimeAttribute.setPartOf(false);
		runtimeAttribute.setAttributeHandler("dynamicAttributesIntSampleBean");
		runtimeAttribute.setQualifier(qualifier);
		runtimeAttribute.setPrivate(false);
		modelService.saveAll();
		detachAndInvalidateModel(runtimeAttribute);
		return runtimeAttribute;
	}

	AttributeDescriptorModel createAttribute(final Class clazz, final String qualifier)
	{

		final ComposedTypeModel ctm = typeService.getComposedTypeForClass(clazz);
		return createAttribute(ctm, qualifier);
	}

	void detachAndInvalidateModel(final ItemModel modelType)
	{
		Utilities.invalidateCache(modelType.getPk());
		modelService.detach(modelType);
	}

	String generateQualifier()
	{
		return "runtime-" + UUID.randomUUID();
	}


}
