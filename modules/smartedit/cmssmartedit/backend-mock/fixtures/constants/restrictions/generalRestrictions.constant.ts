/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { IComponentType } from '../../entities/components';

export const generalRestrictions: IComponentType[] = [
    {
        attributes: [
            {
                cmsStructureType: 'ShortString',
                collection: false,
                editable: true,
                i18nKey: 'type.abstractrestriction.uid.name',
                localized: false,
                paged: false,
                qualifier: 'uid',
                required: false
            },
            {
                cmsStructureType: 'ShortString',
                collection: false,
                editable: true,
                i18nKey: 'type.abstractrestriction.name.name',
                localized: false,
                paged: false,
                qualifier: 'name',
                required: true
            },
            {
                cmsStructureType: 'ShortString',
                collection: false,
                editable: true,
                i18nKey: 'type.abstractrestriction.description.name',
                localized: false,
                paged: false,
                qualifier: 'description',
                required: false
            }
        ],
        category: 'RESTRICTION',
        code: 'AbstractRestriction',
        i18nKey: 'type.abstractrestriction.name',
        name: 'Restriction',
        type: 'abstractRestrictionData'
    },
    {
        attributes: [
            {
                cmsStructureType: 'EditableDropdown',
                collection: false,
                editable: true,
                i18nKey: 'type.cmsusergrouprestriction.usergroups.name',
                localized: false,
                paged: false,
                qualifier: 'userGroups',
                required: true
            },
            {
                cmsStructureType: 'ShortString',
                collection: false,
                editable: true,
                i18nKey: 'type.cmsusergrouprestriction.uid.name',
                localized: false,
                paged: false,
                qualifier: 'uid',
                required: false
            },
            {
                cmsStructureType: 'ShortString',
                collection: false,
                editable: true,
                i18nKey: 'type.cmsusergrouprestriction.name.name',
                localized: false,
                paged: false,
                qualifier: 'name',
                required: true
            },
            {
                cmsStructureType: 'ShortString',
                collection: false,
                editable: true,
                i18nKey: 'type.cmsusergrouprestriction.description.name',
                localized: false,
                paged: false,
                qualifier: 'description',
                required: false
            }
        ],
        category: 'RESTRICTION',
        code: 'CMSUserGroupRestriction',
        i18nKey: 'type.cmsusergrouprestriction.name',
        name: 'Usergroup Restriction',
        type: 'userGroupRestrictionData'
    },
    {
        attributes: [
            {
                cmsStructureType: 'DateTime',
                collection: false,
                editable: true,
                i18nKey: 'type.cmstimerestriction.activefrom.name',
                localized: false,
                paged: false,
                qualifier: 'activeFrom',
                required: true
            },
            {
                cmsStructureType: 'DateTime',
                collection: false,
                editable: true,
                i18nKey: 'type.cmstimerestriction.activeuntil.name',
                localized: false,
                paged: false,
                qualifier: 'activeUntil',
                required: true
            },
            {
                cmsStructureType: 'ShortString',
                collection: false,
                editable: true,
                i18nKey: 'type.cmstimerestriction.uid.name',
                localized: false,
                paged: false,
                qualifier: 'uid',
                required: false
            },
            {
                cmsStructureType: 'ShortString',
                collection: false,
                editable: true,
                i18nKey: 'type.cmstimerestriction.name.name',
                localized: false,
                paged: false,
                qualifier: 'name',
                required: true
            },
            {
                cmsStructureType: 'ShortString',
                collection: false,
                editable: true,
                i18nKey: 'type.cmstimerestriction.description.name',
                localized: false,
                paged: false,
                qualifier: 'description',
                required: false
            }
        ],
        category: 'RESTRICTION',
        code: 'CMSTimeRestriction',
        i18nKey: 'type.cmstimerestriction.name',
        name: 'Time Restriction',
        type: 'timeRestrictionData'
    },
    {
        attributes: [
            {
                cmsStructureType: 'MultiCategorySelector',
                collection: false,
                editable: true,
                i18nKey: 'type.cmscategoryrestriction.categories.name',
                localized: false,
                paged: false,
                qualifier: 'categories',
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
                cmsStructureType: 'ShortString',
                collection: false,
                editable: true,
                i18nKey: 'type.cmscategoryrestriction.uid.name',
                localized: false,
                paged: false,
                qualifier: 'uid',
                required: false
            },
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
                cmsStructureType: 'ShortString',
                collection: false,
                editable: true,
                i18nKey: 'type.cmscategoryrestriction.description.name',
                localized: false,
                paged: false,
                qualifier: 'description',
                required: false
            }
        ],
        category: 'RESTRICTION',
        code: 'CMSCategoryRestriction',
        i18nKey: 'type.cmscategoryrestriction.name',
        name: 'Category Restriction',
        type: 'categoryRestrictionData'
    }
];
