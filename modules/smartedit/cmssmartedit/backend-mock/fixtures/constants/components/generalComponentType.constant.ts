/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { IComponentType } from '../../entities/components';
import { cmsLinkToComponentAttribute } from './cmsLinkToComponentAttribute.constant';

export const generalComponentType: IComponentType = {
    attributes: [
        {
            cmsStructureType: 'ShortString',
            collection: false,
            editable: true,
            i18nKey: 'type.cmslinkcomponent.linkname.name',
            localized: true,
            paged: false,
            qualifier: 'linkName',
            required: true
        },
        cmsLinkToComponentAttribute,
        {
            cmsStructureType: 'EditableDropdown',
            collection: false,
            editable: true,
            i18nKey: 'experience.selector.catalog',
            localized: false,
            paged: true,
            qualifier: 'previewCatalog',
            required: true,
            idAttribute: 'uid',
            labelAttributes: ['name']
        },
        {
            cmsStructureType: 'EditableDropdown',
            collection: false,
            editable: true,
            i18nKey: 'experience.selector.language',
            localized: false,
            paged: true,
            qualifier: 'language',
            required: true,
            idAttribute: 'uid',
            dependsOn: 'previewCatalog',
            labelAttributes: ['name']
        },
        {
            cmsStructureType: 'DateTime',
            collection: false,
            editable: true,
            i18nKey: 'experience.selector.date.and.time',
            localized: false,
            paged: false,
            qualifier: 'time',
            required: true
        },
        {
            cmsStructureType: 'ProductCatalogVersionsSelector',
            collection: false,
            editable: true,
            i18nKey: 'experience.selector.catalogversions',
            localized: false,
            paged: false,
            qualifier: 'productCatalogVersions',
            required: true
        }
    ],
    category: 'COMPONENT',
    code: 'CMSLinkComponent',
    i18nKey: 'type.cmslinkcomponent.name',
    name: 'Link',
    type: 'cmsLinkComponentData'
};
