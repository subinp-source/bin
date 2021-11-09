/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { SeFilter } from '../../../common/di/SeFilter';
import { FilterByFieldPipe, TypedMap } from 'smarteditcommons';

/**
 * @ngdoc filter
 * @name smarteditServicesModule.filter:FilterByFieldFilter
 * @deprecated since 2005, use {@link FilterByFieldPipeModule.filter.FilterByFieldPipe}
 */
@SeFilter()
export class FilterByFieldFilter {
    public static transform() {
        return (
            items: TypedMap<string>[],
            query: string,
            keys?: string[],
            callbackFcn?: (filtered: TypedMap<string>[]) => void
        ): TypedMap<string>[] => {
            return FilterByFieldPipe.transform(items, query, keys, callbackFcn);
        };
    }
}
