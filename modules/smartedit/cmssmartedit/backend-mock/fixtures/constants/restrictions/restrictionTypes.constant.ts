/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { IRestrictionType } from '../../entities/restrictions';

export const restrictionTypes: IRestrictionType[] = [
    {
        code: 'CMSTimeRestriction',
        name: {
            de: 'DAS blabla',
            en: 'Time Restriction'
        }
    },
    {
        code: 'CMSCatalogRestriction',
        name: {
            en: 'Catalog Restriction'
        }
    },
    {
        code: 'CMSCategoryRestriction',
        name: {
            en: 'category Restriction'
        }
    },
    {
        code: 'CMSUserRestriction',
        name: {
            en: 'User Restriction'
        }
    },
    {
        code: 'CMSUserGroupRestriction',
        name: {
            en: 'User Group Restriction'
        }
    }
];
