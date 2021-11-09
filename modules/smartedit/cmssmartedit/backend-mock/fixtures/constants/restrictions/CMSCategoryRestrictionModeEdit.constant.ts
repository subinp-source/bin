/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { IComponentType } from '../../entities/components';

export const CMSCategoryRestrictionModeEdit: IComponentType = {
    attributes: [
        {
            cmsStructureType: 'ShortString',
            collection: false,
            editable: true,
            i18nKey: 'type.cmscategoryrestriction.name.name',
            localized: false,
            paged: false,
            qualifier: 'name',
            required: true
        },
        {
            cmsStructureType: 'Boolean',
            collection: false,
            editable: true,
            i18nKey: 'type.cmscategoryrestriction.recursive.name',
            localized: false,
            paged: false,
            qualifier: 'recursive',
            required: false
        },
        {
            cmsStructureType: 'MultiCategorySelector',
            collection: false,
            editable: true,
            i18nKey: 'type.cmscategoryrestriction.categories.name',
            localized: false,
            paged: false,
            qualifier: 'categories',
            required: true
        }
    ],
    category: 'RESTRICTION',
    code: 'CMSCategoryRestriction',
    i18nKey: 'type.cmscategoryrestriction.name',
    name: 'Category Restriction',
    type: 'categoryRestrictionData'
};
