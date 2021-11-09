/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { StringUtils } from '@smart/utils';
import { IPrioritized } from './interfaces/IPrioritized';
import { SeDowngradeService } from 'smarteditcommons/di';

/**
 * @ngdoc service
 * @name smarteditServicesModule.service:PriorityService
 * @description
 * The PriorityService handles arrays of {@link smarteditServicesModule.interface:IPrioritized IPrioritized} elements
 */
@SeDowngradeService()
export class PriorityService {
    constructor(private stringUtils: StringUtils) {}
    /**
     * @ngdoc method
     * @name smarteditServicesModule.service:PriorityService#sort<T>
     * @methodOf smarteditServicesModule.service:PriorityService
     *
     * @description
     * Will sort the candidate array by ascendign or descending priority.
     * Even if the priority is not defined for a number of elements, the sorting will still be consistent over invocations
     * @param {T[]} candidate the array of @link smarteditServicesModule.interface:IPrioritized IPrioritized} elements to be sorted
     * @param {boolean=} [ascending=true] if true, candidate will be sorted by ascending priority.
     * @returns {T[]} The sorted candidate array.
     */
    sort<T extends IPrioritized>(candidate: T[], ascending: boolean = true): T[] {
        return candidate.sort((item1: IPrioritized, item2: IPrioritized) => {
            let output: number = item1.priority - item2.priority;
            if (output === 0) {
                output = this.stringUtils
                    .encode(item1)
                    .localeCompare(this.stringUtils.encode(item2));
            }
            return output;
        });
    }
}
