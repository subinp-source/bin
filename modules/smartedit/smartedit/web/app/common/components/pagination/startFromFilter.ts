/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { SeFilter } from '../../di/SeFilter';
import { StartFromPipe } from '../../pipes/startFrom';

/**
 * @ngdoc filter
 * @name smarteditCommonsModule.filter:startFrom
 *
 * @deprecated since 2005, use {@link smarteditCommonsModule.pipe:StartFromPipe StatFromPipe}
 *
 * @description
 *
 * Deprecated since 2005, use {@link smarteditCommonsModule.pipe:StartFromPipe StatFromPipe}
 */

@SeFilter()
export class StartFromFilter {
    static transform<T>() {
        return (input: T[], start: number): T[] => {
            return StartFromPipe.transform(input, start);
        };
    }
}
