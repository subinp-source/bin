/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Controller, Get, Param, Query } from '@nestjs/common';

import {
    IcmsLinkToComponentAttribute,
    IComponentType,
    IComponentTypeAttribute
} from '../../fixtures/entities/components';
import {
    componentTypes,
    generalComponentType,
    CMSLinkComponentCategory,
    CMSLinkComponentContent,
    CMSLinkComponentExternal,
    CMSLinkComponentProduct
} from '../../fixtures/constants/components';
import {
    generalRestrictions,
    CMSCategoryRestrictionModeAdd,
    CMSCategoryRestrictionModeCreate,
    CMSCategoryRestrictionModeEdit,
    CMSTimeRestrictionModeAdd,
    CMSTimeRestrictionModeCreate,
    CMSTimeRestrictionModeEdit,
    CMSUserGroupRestrictionModeAdd,
    CMSUserGroupRestrictionModeCreate,
    CMSUserGroupRestrictionModeEdit
} from '../../fixtures/constants/restrictions';

import { categoryPage, contentPage, productPage } from '../../fixtures/constants/pages';

@Controller()
export class TypesController {
    @Get('cmswebservices/v1/types/:code')
    getComponentTypeByCode(@Param('code') code: string) {
        switch (code) {
            case 'CategoryPage':
                return categoryPage;
            case 'ContentPage':
                return contentPage;
            case 'ProductPage':
                return productPage;
        }
        const result: IComponentType | undefined = componentTypes.find(
            (type: IComponentType) => type.code === code
        );
        return result === undefined ? generalComponentType : result;
    }

    @Get('cmswebservices/v1/types*')
    getComponentType(
        @Query('code') code: string,
        @Query('mode') mode: string,
        @Query('category') category: string
    ) {
        if (code === 'PreviewData' && (mode === 'DEFAULT' || mode === 'PREVIEWVERSION')) {
            const res = generalComponentType.attributes.filter(
                (attr: IComponentTypeAttribute | IcmsLinkToComponentAttribute) =>
                    attr.cmsStructureType === 'EditableDropdown' ||
                    attr.cmsStructureType === 'DateTime' ||
                    attr.cmsStructureType === 'ProductCatalogVersionsSelector'
            );
            const editableDropdown: IComponentTypeAttribute | undefined = res.find(
                (attr: IComponentTypeAttribute) =>
                    attr.cmsStructureType === 'EditableDropdown' &&
                    attr.qualifier === 'previewCatalog'
            );
            if (editableDropdown) {
                editableDropdown.editable = mode === 'DEFAULT' ? true : false;
            }
            return { attributes: res };
        } else if (mode === 'DEFAULT') {
            const resultComponent: IComponentType | undefined = componentTypes.find(
                (type: IComponentType) => type.code === code
            );
            return { componentTypes: [resultComponent] };
        } else if (code === 'CMSLinkComponent') {
            switch (mode) {
                case 'CATEGORY':
                    return CMSLinkComponentCategory;
                case 'PRODUCT':
                    return CMSLinkComponentProduct;
                case 'CONTENT':
                    return CMSLinkComponentContent;
                case 'EXTERNAL':
                    return CMSLinkComponentExternal;
            }
        } else if (code === 'CMSTimeRestriction') {
            switch (mode) {
                case 'ADD':
                    return CMSTimeRestrictionModeAdd;
                case 'CREATE':
                    return CMSTimeRestrictionModeCreate;
                case 'EDIT':
                    return CMSTimeRestrictionModeEdit;
            }
        } else if (code === 'CMSCategoryRestriction') {
            switch (mode) {
                case 'ADD':
                    return CMSCategoryRestrictionModeAdd;
                case 'CREATE':
                    return CMSCategoryRestrictionModeCreate;
                case 'EDIT':
                    return CMSCategoryRestrictionModeEdit;
            }
        } else if (code === 'CMSUserGroupRestriction') {
            switch (mode) {
                case 'ADD':
                    return CMSUserGroupRestrictionModeAdd;
                case 'CREATE':
                    return CMSUserGroupRestrictionModeCreate;
                case 'EDIT':
                    return CMSUserGroupRestrictionModeEdit;
            }
        } else if (category === 'COMPONENT') {
            const filteredTypes: IComponentType[] = componentTypes.filter(
                (type: IComponentType) => type.category === 'COMPONENT'
            );
            return { componentTypes: filteredTypes };
        } else if (category === 'RESTRICTION') {
            return { componentTypes: generalRestrictions };
        }
        const result: IComponentType | undefined = componentTypes.find(
            (type: IComponentType) => type.code === code
        );
        return result === undefined ? generalComponentType : result;
    }

    @Get('/cmswebservices/v1/pagetypes')
    getPageTypes() {
        return {
            pageTypes: [
                {
                    code: 'ContentPage',
                    name: {
                        en: 'Content Page',
                        fr: 'Content Page in French'
                    },
                    description: {
                        en: 'Description for content page',
                        fr: 'Description for content page in French'
                    }
                },
                {
                    code: 'ProductPage',
                    name: {
                        en: 'Product Page',
                        fr: 'Product Page in French'
                    },
                    description: {
                        en: 'Description for product page',
                        fr: 'Description for product page in French'
                    }
                },
                {
                    code: 'CategoryPage',
                    name: {
                        en: 'Category Page',
                        fr: 'Category Page in French'
                    },
                    description: {
                        en: 'Description for category page',
                        fr: 'Description for category page in French'
                    }
                }
            ]
        };
    }
}
