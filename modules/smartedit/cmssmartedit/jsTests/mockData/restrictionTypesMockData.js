/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/* jshint unused:false, undef:false */
unit.mockData.restrictionTypes = function() {
    return {
        getMocks: function() {
            return {
                restrictionTypes: [
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
                    }
                ]
            };
        }
    };
};
