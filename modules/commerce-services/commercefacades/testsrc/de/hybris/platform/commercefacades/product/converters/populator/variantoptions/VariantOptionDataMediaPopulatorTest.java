/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.product.converters.populator.variantoptions;

import com.google.common.collect.Iterables;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.variants.model.GenericVariantProductModel;
import de.hybris.platform.commercefacades.product.ImageFormatMapping;
import de.hybris.platform.commercefacades.product.data.VariantOptionData;
import de.hybris.platform.commercefacades.product.data.VariantOptionQualifierData;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.variants.model.VariantProductModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class VariantOptionDataMediaPopulatorTest {

    private static final String FORMAT_QUALIFIER = "FORMAT_QUALIFIER";
    private static final String MEDIA_FORMAT_NAME = "Media Format Name";
    private static final String BASE_PRODUCT_CODE = "product";

    @Mock
    private ImageFormatMapping acceleratorImageFormatMapping;

    @InjectMocks
    private final VariantOptionDataMediaPopulator variantOptionDataMediaPopulator = new VariantOptionDataMediaPopulator<>();

    @Before
    public void setUp() {
        when(acceleratorImageFormatMapping.getMediaFormatQualifierForImageFormat(FORMAT_QUALIFIER)).thenReturn(MEDIA_FORMAT_NAME);

        variantOptionDataMediaPopulator.setImageFormats(new ArrayList<>());
        variantOptionDataMediaPopulator.getImageFormats().add(FORMAT_QUALIFIER);
    }

    @Test
    public void shouldPopulateMediaInformation() {
        final VariantProductModel variantProductModel = mockNewVariantModel();
        final MediaModel mockMediaModel = mockMediaModel(true);
        variantProductModel.getOthers().add(mockMediaModel);

        final VariantOptionData optionData = new VariantOptionData();

        variantOptionDataMediaPopulator.populate(variantProductModel, optionData);

        final VariantOptionQualifierData optionQualifierData = Iterables.get(optionData.getVariantOptionQualifiers(), 0);
        assertThat(variantProductModel.getOthers().size(), is(optionData.getVariantOptionQualifiers().size()));
        assertThat(optionQualifierData.getImage().getUrl(), is(mockMediaModel.getURL()));
        assertThat(optionQualifierData.getImage().getFormat(), is(FORMAT_QUALIFIER));

    }

    @Test
    public void shouldPopulateMediaInformationWithNullFormat() {
        final VariantProductModel variantProductModel = mockNewVariantModel();
        final MediaModel mockMediaModel = mockMediaModel(false);
        variantProductModel.getOthers().add(mockMediaModel);

        final VariantOptionData optionData = new VariantOptionData();

        variantOptionDataMediaPopulator.populate(variantProductModel, optionData);

        final VariantOptionQualifierData optionQualifierData = Iterables.get(optionData.getVariantOptionQualifiers(), 0);
        assertThat(variantProductModel.getOthers().size(), is(optionData.getVariantOptionQualifiers().size()));
        assertThat(optionQualifierData.getImage().getUrl(), is(mockMediaModel.getURL()));
        assertNull(optionQualifierData.getImage().getFormat());
    }

    private MediaModel mockMediaModel(boolean withName) {
        final MediaModel mediaModel = mock(MediaModel.class);
        when(mediaModel.getURL()).thenReturn("media model url");
        final MediaFormatModel mediaFormat = mock(MediaFormatModel.class);
        if(withName) {
            when(mediaFormat.getName()).thenReturn(MEDIA_FORMAT_NAME);
        }

        when(mediaModel.getMediaFormat()).thenReturn(mediaFormat);

        return mediaModel;
    }

    private GenericVariantProductModel mockNewVariantModel() {
        GenericVariantProductModel variantModel;
        variantModel = new GenericVariantProductModel();
        variantModel.setStockLevels(new HashSet<>());
        variantModel.setOthers(new ArrayList<>());
        variantModel.setCode(BASE_PRODUCT_CODE);

        return variantModel;
    }

}
