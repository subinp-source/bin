/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { SeFilterConstructor } from './types';
import { diNameUtils } from './DINameUtils';

/**
 * @ngdoc object
 * @name smarteditServicesModule.object:@SeFilter
 * @deprecated since 2005
 * @description
 * Decorator used to compose alter original filter constuctor that will later be added to angularJS module filters.
 * @deprecated since 2005
 */

export const SeFilter = function() {
    return function(filterConstructor: SeFilterConstructor): SeFilterConstructor {
        filterConstructor.filterName = diNameUtils.buildFilterName(filterConstructor);

        return filterConstructor;
    };
};
