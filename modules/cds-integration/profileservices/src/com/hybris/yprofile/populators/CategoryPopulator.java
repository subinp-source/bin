/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.yprofile.populators;

import com.hybris.yprofile.dto.Category;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.converters.Populator;

public class CategoryPopulator implements Populator<CategoryModel, Category> {

    @Override
    public void populate(CategoryModel categoryModel, Category category) {
        category.setId(categoryModel.getCode());
    }
}
