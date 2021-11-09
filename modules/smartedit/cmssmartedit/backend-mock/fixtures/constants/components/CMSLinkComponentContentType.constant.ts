/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { IComponentType } from '../../entities/components';
import { cmsLinkToComponentAttribute } from './cmsLinkToComponentAttribute.constant';

export const CMSLinkComponentContent: IComponentType = {
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
            i18nKey: 'type.cmslinkcomponent.contentpage.name',
            localized: false,
            paged: true,
            qualifier: 'contentPage',
            required: true,
            idAttribute: 'uid',
            labelAttributes: ['name']
        },
        {
            cmsStructureType: 'Boolean',
            collection: false,
            editable: true,
            i18nKey: 'type.cmslinkcomponent.target.name',
            localized: false,
            paged: false,
            qualifier: 'target',
            required: true
        }
    ],
    category: 'COMPONENT',
    code: 'CMSLinkComponent',
    i18nKey: 'type.cmslinkcomponent.name',
    name: 'Link',
    type: 'cmsLinkComponentData'
};
