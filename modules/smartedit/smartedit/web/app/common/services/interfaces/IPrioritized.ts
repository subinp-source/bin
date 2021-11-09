/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/**
 * @ngdoc interface
 * @name smarteditServicesModule.interface:IPrioritized
 * @description
 * Interface of entities aimed at being sorted by priority
 */
export interface IPrioritized {
    /**
     * @ngdoc method
     * @name number
     * @methodOf smarteditServicesModule.interface:IPrioritized
     * @description priority an optional number ranging from 0 to 1000 used for sorting
     */
    priority?: number;
}
